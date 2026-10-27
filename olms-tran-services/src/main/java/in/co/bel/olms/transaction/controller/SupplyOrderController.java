package in.co.bel.olms.transaction.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import in.co.bel.olms.transaction.dto.*;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SoAllotment;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.exception.ResourceNotFoundException;
import in.co.bel.olms.master.dto.ConsigneeDto;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SoAllotmentRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

/**
 * SupplyOrderController class
 *
 * @version 1.0
 * @since 2026-08-26
 */

@RestController
@RequestMapping("/supplyOrder")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SupplyOrderController {

	private final SupplyOrderService supplyOrderService;	

	private final Mapper mapper;

	private final NotifyUtil notifyUtil;

	private final EmployeeRepository employeeRepository;

	private final SoAllotmentRepository soAllotmentRepository;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	private final JwtUtils jwtUtils;

	private static final Logger logger = LoggerFactory.getLogger(SupplyOrderController.class);

	/**
	 * 
	 * @return
	 */

	@GetMapping("/getAll")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrders() {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		List<SupplyOrder> supplyOrders = supplyOrderService.getAllSupplyOrders();
		for (SupplyOrder supplyOrder : supplyOrders) {
			SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
			List<Consignee> consignees = supplyOrder.getSoConsigneeMappings();
			List<Integer> consigneeIds = consignees.stream().map(consignee -> consignee.getId())
					.toList();
			List<String> consigneeNames = consignees.stream().map(consignee -> consignee.getName())
					.toList();
			String consigneeNamesStr = String.join(",", consigneeNames);
			supplyOrderDto.setConsigneeName(consigneeNamesStr);
			supplyOrderDto.setConsigneeIds(consigneeIds);
			supplyOrderDtos.add(supplyOrderDto);
		}
		Collections.sort(supplyOrderDtos, Comparator.comparing(SupplyOrderDto::getId).reversed());
		return ResponseEntity.ok(supplyOrderDtos);

	}

	/**
	 * 
	 * @param supplyOrderInputDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/save")
	public ResponseEntity<String> saveSupplyOrder(@RequestBody @Valid SupplyOrderInputDto supplyOrderInputDto) {
		try {
			Map<Boolean, String> response = supplyOrderService.saveSupplyOrders(supplyOrderInputDto);
			if (response.containsKey(true)) {
				String returnString = response.get(true);
				return new ResponseEntity<>(returnString, HttpStatus.CREATED);
			} else {
				String returnString = response.get(false);
				return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ResponseStatusException e) {
			return new ResponseEntity<>(e.getMessage(), e.getStatus());
		} catch (DataNotValidException de) {
			logger.error("Validation failed: {}", de.getMessage());
			return new ResponseEntity<>("Error: " + de.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("An unexpected error occurred: {}", e.getMessage());
			return new ResponseEntity<>("Some error occurred. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@GetMapping("/delete/{soId}")
	public ResponseEntity<Boolean> deleteSupplyOrder(@PathVariable int soId) {

		Boolean savedStatus = supplyOrderService.deleteSupplyOrder(soId);
		if (savedStatus.booleanValue()) {
			return new ResponseEntity<>(savedStatus, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(savedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<SupplyOrderDto> getSupplyOrder(@PathVariable int id) throws DataNotFoundException {

		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(id);
		SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
		List<Consignee> consignees = supplyOrder.getSoConsigneeMappings();
		List<Integer> consigneeIds = consignees.stream().map(consignee -> consignee.getId())
				.toList();
		supplyOrderDto.setConsigneeIds(consigneeIds);
		List<String> projectNames = supplyOrder.getProjects().stream().map(Project::getProjectName).distinct()
				.toList();
		supplyOrderDto.setProjectName(String.join(", ", projectNames));
		return ResponseEntity.ok(supplyOrderDto);

	}

	/**
	 * @author Manasa B
	 * @param id
	 * @return
	 */
	@GetMapping("/cqae/{id}")
	public ResponseEntity<List<SupplyOrderDto>> getSupplyOrderByCqaeId(@PathVariable int id) {
		return ResponseEntity.ok(supplyOrderService.getSupplyOrderByCqaeId(id));

	}

	@GetMapping("getSoById/{id}")
	public ResponseEntity<SupplyOrderDto> getSupplyOrderById(@PathVariable int id) throws DataNotFoundException {
		return ResponseEntity.ok(supplyOrderService.getSupplyOrderById(id));
	}

	
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/updateSupplyOrder")
	public ResponseEntity<?> updateSupplyOrder(@RequestBody SupplyOrderDto supplyOrderDto) {
		try {
			Boolean updatedStatus = supplyOrderService.updateSupplyOrder(supplyOrderDto);
			
			if (updatedStatus.booleanValue()) {
	
				return new ResponseEntity<>(updatedStatus, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(updatedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
	
			}
		} catch (DataNotValidException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	}

	@GetMapping("/getAllSupplyOrdersByOrgId/{orgId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersByOrgId(@PathVariable int orgId) {

		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersByOrgId(orgId);
		return ResponseEntity.ok(supplyOrderDtos);

	}

	@GetMapping("/getAllSupplyOrdersByOrgIdOptimized/{orgId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersByOrgIdOptimized(@PathVariable int orgId) {

		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllNotifiedSupplyOrdersByOrgIdOptimized(orgId);
		return ResponseEntity.ok(supplyOrderDtos);

	}

	@GetMapping("/getAllSupplyOrdersByEmpId/{empId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersByEmpId(@PathVariable int empId) {

		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersByEmpId(empId);
		return ResponseEntity.ok(supplyOrderDtos);

	}

	@GetMapping("/getAllSupplyOrdersBySalesGrp/{empId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersBySalesGrp(@PathVariable int empId) {

		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersBySalesGrp(empId);
		Collections.sort(supplyOrderDtos, Comparator.comparing(SupplyOrderDto::getId).reversed());
		return ResponseEntity.ok(supplyOrderDtos);

	}

	@GetMapping("/getAllSupplyOrdersBySalesGrpId/{salesGrpId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersBySalesGrpId(@PathVariable int salesGrpId) {

		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersBySalesGrpId(salesGrpId);
		return ResponseEntity.ok(supplyOrderDtos);

	}

	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/updateSoAttachments")
	public ResponseEntity<SupplyOrderDto> updateSoAttachments(
			@RequestParam(value = "supplyOrderDtoStr", required = true) String supplyOrderDtoStr,
			@RequestParam(value = "qapFiles", required = false) MultipartFile[] qapFiles,
			@RequestParam(value = "drawings", required = false) MultipartFile[] drawings,
			@RequestParam(value = "atp", required = false) MultipartFile[] atp,
			@RequestParam(value = "others", required = false) MultipartFile[] others,
			@RequestParam(value = "amendments", required = false) MultipartFile[] amendments,
			@RequestParam(value = "po", required = false) MultipartFile[] po,
			@RequestParam(value = "itemlots", required = false) MultipartFile[] itemLots,
			@RequestParam(value = "dd", required = false) MultipartFile[] dd) {

		Gson gson = new Gson();
		SupplyOrderDto supplyOrderDto = gson.fromJson(supplyOrderDtoStr, new TypeToken<SupplyOrderDto>() {
		}.getType());
		SupplyOrderDto updatedOrderDto = supplyOrderService.updateSoAttachments(supplyOrderDto, qapFiles, drawings, atp,
				others, amendments, po, itemLots, dd);
		if (updatedOrderDto != null) {

			return new ResponseEntity<>(updatedOrderDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOrderDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/notifyCqaeAndJcqao")
	public ResponseEntity<String> notifyCqaeAndJcqao(@RequestParam int soId, @RequestParam int empId,
			@RequestParam(name = "qaRecievers") List<Integer> qaRecievers,
			@RequestParam(name = "firmRecievers") List<Integer> firmRecievers,
			@RequestHeader(name = "Authorization") String token) throws Exception{

		try {
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soId);
			Optional<Employee> empOpt = employeeRepository.findById(empId);
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);
			if (null != empOpt) {
				Employee allotedBy = empOpt.get();
				List<Integer> allottedToIdList = new ArrayList<>(qaRecievers);
				allottedToIdList.addAll(firmRecievers);
				List<Employee> allotedToList = employeeRepository.findByQaRecievers(allottedToIdList);
				for (Employee allotedTo : allotedToList) {
					SoAllotment allotment = new SoAllotment();
					allotment.setAllotedBy(allotedBy);
					allotment.setAllotedTo(allotedTo);
					allotment.setSupplyOrder(supplyOrder);
					allotment.setCreatedDate(new Date());
					soAllotmentRepository.save(allotment);
				}

			}

			supplyOrder = supplyOrderService.updateSoNotifyStatus(supplyOrder, roleId);
			List<Integer> receivers = firmRecievers;
			receivers.addAll(qaRecievers);
			notifyUtil.notifyCqae("New Supply Order received with Order No: " + supplyOrder.getOrderNo(), soId,
					supplyOrder.getOrderNo(), empId, NotificationConstants.SO, receivers,
					supplyOrder.getSalesGroup().getName());

		} catch (Exception e) {
			logger.error("Notification error: {}", e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/getAllotedSupplyOrders/{orgId}/{empId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllotedSupplyOrders(@PathVariable int orgId,
			@PathVariable int empId) {

		return ResponseEntity.ok(supplyOrderService.getAllotedSupplyOrders(empId));

	}

	@GetMapping("/getAllSupplyOrdersBySalesGrpId/{soId}/{salesGrpId}")
	public ResponseEntity<List<SoTreeViewDto>> getAllSupplyOrdersBySalesGrpId(@PathVariable int soId,
			@PathVariable int salesGrpId) {

		List<SoTreeViewDto> soTreeViewDtos = supplyOrderService.getAllSOBySalesGrpIdOrSoID(soId, salesGrpId);
		return ResponseEntity.ok(soTreeViewDtos);

	}

	@GetMapping("/getOverallTreeView/{supplyOrderId}")
	public ResponseEntity<List<SoTreeViewDto>> getOverallTreeView(@PathVariable int supplyOrderId) {

		List<SoTreeViewDto> soTreeViewDtos = supplyOrderService.getOverallTreeView(supplyOrderId);
		return ResponseEntity.ok(soTreeViewDtos);

	}

	@GetMapping("/getConsigneesBySoId/{supplyOrderId}")
	public ResponseEntity<List<ConsigneeDto>> getConsigneesBySoId(@PathVariable int supplyOrderId) {

		List<ConsigneeDto> consigneeDtos = supplyOrderService.getConsigneesBySoId(supplyOrderId);
		return ResponseEntity.ok(consigneeDtos);

	}

	// Function to delete the attachment in supply order
	@PreAuthorize("hasAnyAuthority('PLANNER')")
	@PostMapping("/deleteSoAttachment")
	public ResponseEntity<String> deleteSoAttachment(@RequestBody String inputString) {

		Gson gson = new Gson();
		KeyValuePair keyValuePair = gson.fromJson(inputString, new TypeToken<KeyValuePair>() {
		}.getType());
		int attachmentId = keyValuePair.getId();
		String fileName = keyValuePair.getName();

		String deletionStatus = supplyOrderService.deleteSoAttachment(attachmentId, fileName);
		if (deletionStatus.equals(ApplicationConstants.SUCCESS)) {

			return new ResponseEntity<>(deletionStatus, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(deletionStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@GetMapping("/getAllSupplyOrdersBySalesGrpOptimized/{empId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersBySalesGrpOptimized(@PathVariable int empId,@RequestParam(required = false) List<Integer> statusIds) {
		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersBySalesGrpOptimized(empId, statusIds);
		return ResponseEntity.ok(supplyOrderDtos);
	}

	@GetMapping("/getAllSupplyOrdersBySalesGrpOptimizedByEmpId/{empId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllSupplyOrdersBySalesGrpOptimizedByEmpId(@PathVariable int empId) {
		List<SupplyOrderDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersBySalesGrpOptimizedByEmpId(empId);
		return ResponseEntity.ok(supplyOrderDtos);
	}

	@GetMapping("/getAttachmentBySoId/{soId}")
	public ResponseEntity<SoAttachmentDto> getAttachmentBySoId(@PathVariable int soId) {
		try {
			return ResponseEntity.ok(supplyOrderService.getAttachmentsBySoId(soId));
		} catch (Exception e) {
			logger.error("getAttachmentBySoId error: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	// method to get the file nos for cross/surprise check register
	@GetMapping("/getAllFileNos/{orgId}")
	public ResponseEntity<List<KeyValuePair>> getAllFileNos(@PathVariable int orgId) {
		try {
			List<Object[]> supplyOrderList = supplyOrderService.getAllFileNos(orgId);
			List<KeyValuePair> keyValuePairs = new ArrayList<>();
			for (Object[] fileNoArray : supplyOrderList) {
				KeyValuePair keyValuePair = new KeyValuePair();
				keyValuePair.setId(fileNoArray[0] != null ? new BigDecimal(fileNoArray[0].toString()).intValue() : 0);
				keyValuePair.setName(fileNoArray[1] != null ? fileNoArray[1].toString() : "");
				keyValuePairs.add(keyValuePair);
			}
			return ResponseEntity.ok(keyValuePairs);
		} catch (Exception e) {
			logger.error("getAttachmentBySoId error: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	// method to get the so details for the surprise/cross check form
	@GetMapping("/getSODeatils/{supplyOrderId}")
	public ResponseEntity<SurpriseCheckDto> getSODetailsForSurpriseCheck(@PathVariable int supplyOrderId) {
		try {
			SurpriseCheckDto surpriseCheckDto = null;
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(supplyOrderId);
			if (supplyOrder != null) {
				List<PartListDto> partListDtoList = getPartListDtos(supplyOrder);
				List<Object[]> supplyOrderObjList = supplyOrderService.getSODetailsForSurpriseCheck(supplyOrderId);
				if (!supplyOrderObjList.isEmpty()) {
					Object[] supplyOrderObj = supplyOrderObjList.get(0);
					surpriseCheckDto = new SurpriseCheckDto();
					surpriseCheckDto.setFirm(supplyOrderObj[0] != null ? supplyOrderObj[0].toString() : "");
					String orgName = supplyOrderObj[1] != null ? supplyOrderObj[1].toString() : "";
					String orgAddress = supplyOrderObj[2] != null ? supplyOrderObj[2].toString() : "";
					String address = orgName + "\n" + orgAddress;
					surpriseCheckDto.setFirmAddress(address);
					surpriseCheckDto.setSupplyOrderNo(supplyOrderObj[3] != null ? supplyOrderObj[3].toString() : "");
					surpriseCheckDto.setSupplyOrderDate(supplyOrderObj[4] != null ? supplyOrderObj[4].toString() : "");
					surpriseCheckDto.setSoDescription(supplyOrderObj[5] != null ? supplyOrderObj[5].toString() : "");
					surpriseCheckDto.setItems(partListDtoList);
				}
			}
			return ResponseEntity.ok(surpriseCheckDto);
		} catch (Exception e) {
			logger.error("Error in getting SO details: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	private static List<PartListDto> getPartListDtos(SupplyOrder supplyOrder) {
		List<SOLineItem> soLineItemList = supplyOrder.getSoLineItems();
		List<PartListDto> partListDtoList = new ArrayList<>();
		for (SOLineItem soLineItem : soLineItemList) {
			PartListDto partListDto = new PartListDto();
			partListDto.setId(soLineItem.getId());
			partListDto.setName(soLineItem.getLineItemNo());
			partListDto.setDescription(soLineItem.getDescription());
			partListDtoList.add(partListDto);
		}
		return partListDtoList;
	}

	@PostMapping("/checkUniqueIndent")
	public ResponseEntity<Object> checkUniqueIndent(@RequestBody SupplyOrderDto supplyOrderDto) {
		try {
			List<Boolean> booleans = supplyOrderService.checkUniqueIndent(supplyOrderDto);
			return ResponseEntity.ok(booleans);
		} catch (Exception e) {
			logger.error("/supplyOrder/checkUniqueIndent Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/expiring/{empId}")
	public List<SupplyOrderExpiryDTO> getExpiringOrders(@PathVariable int empId) {
		return supplyOrderService.getExpiringOrdersByEmpId(empId);
	}

	@GetMapping("/expiring-orders/count/{empId}")
	public int getExpiringOrderCount(@PathVariable int empId) {
		return supplyOrderService.getExpiringOrderCountByEmpId(empId);
	}

	@GetMapping("/expiring/org/{orgId}")
	public List<SupplyOrderExpiryDTO> getExpiringOrdersByOrgId(@PathVariable int orgId) {
		return supplyOrderService.getExpiringOrdersByOrgId(orgId);
	}

	@GetMapping("/countExpiring/org/{orgId}")
	public ResponseEntity<Integer> countExpiringOrdersByOrgId(@PathVariable int orgId) {
		int count = supplyOrderService.countExpiringOrdersByOrgId(orgId);
		return ResponseEntity.ok(count);
	}

	@GetMapping("/getApprovedSoBySalesGrp/{salesGrpId}")
	public List<SupplyOrderDto> getApprovedSoBySalesGrp(@PathVariable int salesGrpId) {
		
		return supplyOrderService.getApprovedSoBySalesGrp(salesGrpId);

	}

	@GetMapping("/getAllotedSupplyOrdersBySalesGrp/{empId}/{salesGrpId}")
	public ResponseEntity<List<SupplyOrderDto>> getAllotedSupplyOrdersBySalesGrp(@PathVariable int empId,
			@PathVariable int salesGrpId, @RequestParam(required= false) List<Integer> statusIds) {
		return ResponseEntity.ok(supplyOrderService.getAllotedSupplyOrdersBySalesGrpId(empId, salesGrpId, statusIds));
	}

	@GetMapping("/getAllTargetAcceptedQuantityofSo")
	public ResponseEntity<List<SupplyOrderDto>> getAllTargetAcceptedQuantityofSo() {
		return ResponseEntity.ok(supplyOrderService.getAllTargetAcceptedQuantityofSo());
	}
	
	
	 /**
     * @author Chirag Swamy
     * @since 29-09-26
     * 
     * Performs a soft delete for Supply Order.
     * Accessible ONLY by SUPER_ADMIN.
     */
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public ResponseEntity<ApiResponse<String>> softDelete(@PathVariable Integer id) {
	    try {
	        supplyOrderService.softDeleteSupplyOrder(id);
	        return ResponseEntity.ok(new ApiResponse<>(true, "Supply Order successfully soft-deleted."));
	    } catch (ResourceNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(new ApiResponse<>(false, e.getMessage()));
	    } catch (IllegalStateException e) {
	        // Returns the specific "INote generated" error message to the user
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(new ApiResponse<>(false, e.getMessage()));
	    } catch (Exception e) {
	        logger.error("Error during soft delete for SO ID: {}", id, e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(false, "An internal error occurred."));
	    }
	}
	
	/**
     * @author Chirag Swamy
     * @since 30-09-26
     * 
     * Get All Supply Orders for Super Admin.
     */
	@GetMapping("/getAllSupplyOrdersSuperAdmin")
	public ResponseEntity<List<SupplyOrderSuperAdminDto>> getAllSupplyOrdersSuperAdmin() {
	    List<SupplyOrderSuperAdminDto> supplyOrderDtos = supplyOrderService.getAllSupplyOrdersForSuperAdmin();
	    return ResponseEntity.ok(supplyOrderDtos);
	}

}
