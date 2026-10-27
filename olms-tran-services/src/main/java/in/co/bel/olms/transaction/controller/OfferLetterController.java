package in.co.bel.olms.transaction.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.AllottedRegNosDto;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.OLDto;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.OfferLetterStatusDto;
import in.co.bel.olms.transaction.dto.OlAttachmentDto;
import in.co.bel.olms.transaction.dto.OlDetByInspOrgDto;
import in.co.bel.olms.transaction.dto.OlEditDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.dto.SubSlipLineItemDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.helper.ol.OlHelper;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.transaction.service.ol.OlAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/offerLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OfferLetterController {

	private final OfferLetterService offerLetterService;

	private final OlAllotmentService olAllotmentService;

	private final EmployeeRepository employeeRepository;

	private final NotifyUtil notifyUtil;

	private final Mapper mapper;

	DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(OfferLetterController.class);

	private final OlHelper olHelper;

	
	private static final String STATUS_RETURNED = "Returned";
	private static final String MSG_INTERNAL_SERVER_ERROR = "Internal Server Error";
	/**
	 *
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<OfferLetterDto> getOfferLetterById(@PathVariable int id) throws DataNotFoundException {

		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);

		OfferLetter ol = offerLetterService.getOfferLetterById(id);
		OfferLetterDto offerLetterDto = mapper.map(ol, OfferLetterDto.class);
		SaleOrder saleOrder = ol.getSaleOrder();
		SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
		offerLetterDto.setTotalItems(supplyOrder.getTotalQty());
		offerLetterDto.setTotalValue(supplyOrder.getValue());
		offerLetterDto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
		offerLetterDto.setName(ol.getOlNumber());
		offerLetterDto.setSoNumber(supplyOrder.getOrderNo());

		return ResponseEntity.ok(offerLetterDto);
	}

	/**
	 * @author Manasa B
	 * @param id
	 * @return
	 */
	@GetMapping("/cqae/{id}")
	public ResponseEntity<List<OfferLetterDto>> getOfferLetterByCqaeId(@PathVariable int id) {
		return ResponseEntity.ok(offerLetterService.getOfferLetterByCqaeId(id));

	}

	@PostMapping("/save")
	public ResponseEntity<String> saveOfferLetter(@RequestBody OfferLetterDto offerLetterDto) {
		try {
			String response = offerLetterService.saveOfferLetterWarrantNew(offerLetterDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			return ResponseEntity.badRequest().body(ae.getLocalizedMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
		}
	}

	@GetMapping("/getOLBySaleOrderId/{saleOrderId}")
	public ResponseEntity<List<OfferLetterDto>> getOLBySaleOrderId(@PathVariable int saleOrderId) {

		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		List<OfferLetter> offerLetters = offerLetterService.getOLBySaleOrderId(saleOrderId);
		for (OfferLetter offerLetter : offerLetters) {
			OfferLetterDto offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
			if (offerLetter.getActive() != null) {
				String status = offerLetter.getActive().booleanValue() ? ApplicationConstants.ACTIVE : ApplicationConstants.INACTIVE;
				offerLetterDto.setStatus(status);
			}
			offerLetterDtos.add(offerLetterDto);
		}
		return ResponseEntity.ok(offerLetterDtos);
	}

	@GetMapping("/getLineItemsByOl/{olId}")
	public ResponseEntity<List<OlLineItemDto>> getLineItemsByOl(@PathVariable int olId) {

		List<OlLineItemDto> olLineItemDtos = offerLetterService.getLineItemsByOl(olId);
		return ResponseEntity.ok(olLineItemDtos);
	}

	@GetMapping("/getLineItemsBySubSlip/{subSlipId}")
	public ResponseEntity<List<SubSlipLineItemDto>> getLineItemsBySubSlip(@PathVariable Integer subSlipId) {

		List<SubSlipLineItemDto> olLineItemDtos = offerLetterService.getLineItemsBySubSlip(subSlipId);
		return ResponseEntity.ok(olLineItemDtos);
	}
	
	@GetMapping("/getOLBySupplyOrderId/{supplyOrderId}")
	public ResponseEntity<List<OfferLetterDto>> getOLBySupplyOrderId(@PathVariable int supplyOrderId) {

		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		try {
			List<OfferLetter> offerLetters = offerLetterService.getOLBySupplyOrderId(supplyOrderId);
			for (OfferLetter offerLetter : offerLetters) {

				OfferLetterDto offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
				SaleOrder saleOrder = offerLetter.getSaleOrder();
				SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
				if (saleOrder.getCategory() != null && saleOrder.getCategory().getShip() != null
						&& saleOrder.getCategory().getShip().getProject() != null) {
					offerLetterDto.setProjectName(saleOrder.getCategory().getShip().getProject().getProjectName());
				}
				offerLetterDto.setFileNo(supplyOrder.getFileNo());
				offerLetterDto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
				String status = offerLetter.getActive().booleanValue() ? ApplicationConstants.ACTIVE : ApplicationConstants.INACTIVE;
				offerLetterDto.setStatus(status);
				boolean isQinAvlbl = !offerLetter.getQinObsNcForms().isEmpty();
				offerLetterDto.setIsQinAvlbl(isQinAvlbl);
				offerLetterDtos.add(offerLetterDto);
			}
		} catch (Exception e) {
			logger.error("Error in getOLBySupplyOrderId for SO Id:  {}. Error: {}", supplyOrderId, e.getMessage());
		}
		return ResponseEntity.ok(offerLetterDtos);
	}
	

	@GetMapping("/getAllotedOfferLetters/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getAllotedOfferLetters(@PathVariable int empId) {
		List<OfferLetterDto> offerLetterDtos = olAllotmentService.getOLAllotedToEmployee(empId);
		return ResponseEntity.ok(offerLetterDtos);

	}
	
	@PreAuthorize("hasAnyAuthority('INSP', 'JCQAO') && @jwtUtils.getEmpIdFromJwtToken(authentication.principal) == #empId")
	@GetMapping("/getApprovedOfferLetters/{empId}")
	public ResponseEntity<List<OLDto>> getApprovedOfferLetters(@PathVariable int empId) {
		try {
			
			List<Object[]> offeLetters = offerLetterService.getOlAllotedForApproval(empId);

			List<OLDto> offerLetterDtos = prepOfferLetterDtosWithoutSort(offeLetters);

			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			logger.error("error in retreiving approved offerLetters: ", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@PreAuthorize("@jwtUtils.getEmpIdFromJwtToken(authentication.principal) == #empId")
	@GetMapping("/notifyCqaeAndJcqao/{olId}/{empId}")
	public ResponseEntity<Boolean> notifyCqaeAndJcqao(@PathVariable int olId, @PathVariable int empId) {

		try {
			OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
			List<Integer> receivers = employeeRepository
					.getJCnAdminIdsByOrgId(offerLetter.getSaleOrder().getSupplyOrder().getOrganization().getId());

			String response = notifyUtil.notifyCqae(
					"New Offer Letter received with OL No: " + offerLetter.getOlNumber(), olId,
					offerLetter.getOlNumber(), empId, NotificationConstants.OL, receivers,
					offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
			if (response.equals(ApplicationConstants.SUCCESS)) {
				logger.info("Updating the OL Status");
				offerLetterService.updateOLNotifyStatus(offerLetter);
				offerLetterService.updateOlStatus(olId, ApplicationConstants.OL_PENDING);
			}

		} catch (Exception e) {
			logger.error("Notification error: {}", e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@PreAuthorize("@jwtUtils.getEmpIdFromJwtToken(authentication.principal) == #empId")
	@PostMapping("/notifyFirmEmployee/{olId}/{empId}")
	public ResponseEntity<Boolean> notifyFirmEmployee(@PathVariable int olId, @PathVariable int empId, @RequestBody List<Integer> receivers) {

		try {
			OfferLetter offerLetter = offerLetterService.getOfferLetterById(olId);
//			List<Integer> receivers = employeeRepository
//					.getJCnAdminIdsByOrgId(offerLetter.getSaleOrder().getSupplyOrder().getOrganization().getId());

			String response = notifyUtil.notifyCqae(
					"New Offer Letter received with Submission Slip No: " + offerLetter.getOlNumber(), olId,
					offerLetter.getOlNumber(), empId, NotificationConstants.OL, receivers,
					offerLetter.getSaleOrder().getSupplyOrder().getSalesGroup().getName());
			if (response.equals(ApplicationConstants.SUCCESS)) {
				logger.info("Updating the OL Status");
				offerLetterService.updateOLNotifyStatus(offerLetter);
				offerLetterService.updateOlStatus(olId, ApplicationConstants.OL_PENDING);
			}

		} catch (Exception e) {
			logger.error("Notification error: {}", e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 *
	 *         Get I-Note details by OL id
	 */
	@GetMapping("/inote/getINoteByOlId/{olId}")
	public ResponseEntity<INoteDto> getINoteOfOfferLetter(@PathVariable int olId) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByOlId(olId);

			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("error in retreiving INote of offerLetter: ", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("getOlApprovalDetails/{orgId}")
	public ResponseEntity<List<OfferLetterDto>> getOlApprovalDetailsByOrgId(@PathVariable int orgId)
			throws DataNotFoundException {
		List<OfferLetterDto> olApprovalDtos = offerLetterService.getOlApprovalDetailsByOrgId(orgId);
		return ResponseEntity.ok(olApprovalDtos);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateOfferLetter(@RequestBody @Valid OfferLetterDto offerLetterDto) {

		try {
			Map<Boolean, String> response = offerLetterService.updateOfferLetter(offerLetterDto);

			if (response.containsKey(true)) {
				String returnString = response.get(true);
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			} else {
				String returnString = response.get(false);
				return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IllegalArgumentException iae) {
			return ResponseEntity.badRequest().body(iae.getMessage());
		} catch (DataNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private List<OfferLetterDto> prepareOfferLetterDtosWithoutSort(List<OfferLetter> offerLetters) {
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);
		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		for (OfferLetter ol : offerLetters) {
			OfferLetterDto dto = mapper.map(ol, OfferLetterDto.class);
			SaleOrder saleOrder = ol.getSaleOrder();
			SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
			dto.setTotalItems(supplyOrder.getTotalQty());
			if (supplyOrder.getValue() != null) {
				dto.setTotalValue(supplyOrder.getValue());
			}
			dto.setFileNo(supplyOrder.getFileNo());
			dto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
			dto.setName(ol.getOlNumber());
			dto.setSoNumber(supplyOrder.getOrderNo());
			if (saleOrder.getCategory() != null && saleOrder.getCategory().getShip() != null
					&& saleOrder.getCategory().getShip().getProject() != null) {
				dto.setProjectName(saleOrder.getCategory().getShip().getProject().getProjectName());
			}
			dto.setStatus(Boolean.TRUE.equals(ol.getActive()) ? ApplicationConstants.ACTIVE :ApplicationConstants.INACTIVE);
			// add the OL flag details for the inspection status
			boolean isReturned = olHelper.isReturned(ol);
			dto.setIsReturned(isReturned);
			dto.setIsScrunityDone(olHelper.isScrutinyDone(ol));
			// Getting the File Noting status
			String status = null;
			status = isReturned ? STATUS_RETURNED : offerLetterService.getFileNoteStatusByOlId(ol.getId());
			dto.setFileNoteStatus(status);

			offerLetterDtos.add(dto);
		}
//		offerLetterDtos.sort(Comparator
//				.comparing(offerLetterDto -> definedOrder.indexOf(offerLetterDto.getFileNoteStatus().split(" ")[0])));
		return offerLetterDtos;
	}

	@GetMapping("/getOlDetailsForPrint/{olId}")
	public ResponseEntity<OfferLetterDto> getOlDetailsForPrint(@PathVariable int olId) {

		OfferLetterDto offerLetterDto = offerLetterService.getOlDetailsForPrint(olId);
		return ResponseEntity.ok(offerLetterDto);

	}

	@PostMapping("/updateOl")
	public ResponseEntity<String> updateOl(@RequestBody @Valid OfferLetterDto offerLetterDto) {

		Map<Boolean, String> response = offerLetterService.updateOl(offerLetterDto);
		if (response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.OK);
		} else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/updateOlAttachments")
	public ResponseEntity<OfferLetterDto> updateOlAttachments(
			@RequestParam(value = "offerLetterDtoStr", required = true) String offerLetterDtoStr,
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments,
			@RequestParam(value = "preInspectionReports", required = false) MultipartFile[] preInspectionReports,
			@RequestParam(value = "qap", required = false) MultipartFile[] qap,
			@RequestParam(value = "drawings", required = false) MultipartFile[] drawings,
			@RequestParam(value = "ateAtp", required = false) MultipartFile[] ateAtp,
			@RequestParam(value = "olStatus", required = true) String olStatus) {

		Gson gson = new Gson();
		OfferLetterDto offerLetterDto = gson.fromJson(offerLetterDtoStr, new TypeToken<OfferLetterDto>() {
		}.getType());
		OfferLetterDto updatedOrderDto = offerLetterService.updateOlAttachments(offerLetterDto, attachments,
				preInspectionReports, qap, drawings, ateAtp, olStatus);
		if (updatedOrderDto != null) {

			return new ResponseEntity<>(updatedOrderDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOrderDto, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping("/getAttachmentByOlId/{olId}")
	public ResponseEntity<OlAttachmentDto> getAttachmentByOlId(@PathVariable int olId) {
		try {
			return ResponseEntity.ok(offerLetterService.getAttachmentsByOlId(olId));
		} catch (Exception e) {
			logger.error("getAttachmentByOlId error: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getAttachmentByOlIds")
	public ResponseEntity<OlAttachmentDto> getAttachmentByOlIds(@RequestParam List<Integer> olIds) {
		try {
			return ResponseEntity.ok(offerLetterService.getAttachmentsByOlIds(olIds));
		} catch (Exception e) {
			 logger.error("Error retrieving attachments for OlIds: {}", olIds, e);
			logger.error("getAttachmentsByOlIds error: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@PostMapping("/deleteOlAttachment")
	public ResponseEntity<String> deleteOlAttachment(@RequestBody String inputString) {

		Gson gson = new Gson();
		KeyValuePair keyValuePair = gson.fromJson(inputString, new TypeToken<KeyValuePair>() {
		}.getType());
		int offerLetterId = keyValuePair.getId();
		String fileName = keyValuePair.getName();

		String deletionStatus = offerLetterService.deleteOlAttachment(offerLetterId, fileName);
		if (deletionStatus.equals(ApplicationConstants.SUCCESS)) {

			return new ResponseEntity<>(deletionStatus, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(deletionStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@GetMapping("/getOfferLettersByStatus/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getOfferLettersByStatus(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getOfferLettersByStatus(orgId,empId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Error fetching offer letters for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}

	@GetMapping("/getOfferLettersByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getOfferLettersByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getOfferLettersByStatusForFirm(orgId,empId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Failed to fetch offer letters for firm. OrgId: {}, EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}

	@GetMapping("/getAllottedOfferLettersByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getAllottedOfferLettersByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getAllottedOfferLettersByStatusForFirm(orgId,empId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			logger.error("Error retrieving allotted offer letters. OrgId: {}, EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}
	
	@GetMapping("/getAllottedOfferLettersApprovalByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getAllottedOfferLettersApprovalByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getAllottedOfferLettersApprovalByStatusForFirm(orgId,empId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Error retrieving allotted offer letter approvals. OrgId: {}, EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}
	
	
	@GetMapping("/getNotifiedOfferLettersByStatusForFirm/{orgId}/{empId}")
	public ResponseEntity<List<OfferLetterDto>> getNotifiedOfferLettersByStatusForFirm(@PathVariable int orgId,@PathVariable int empId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getNotifiedOfferLettersByStatusForFirm(orgId,empId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Error retrieving notified offer letters. OrgId: {}, EmpId: {}", orgId, empId, e);
		        
			return ResponseEntity.internalServerError().body(null);
		}

	}
	
	//getOfferLettersForCqao
	@GetMapping("/getOfferLetters/{orgId}")
	public ResponseEntity<List<OfferLetterDto>> getOfferLetters(@PathVariable int orgId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getOfferLettersForCqao(orgId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			 logger.error("Error retrieving offer letters for OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}

	}
	
	@GetMapping("/getOLCount/{orgId}")
	public ResponseEntity<OfferLetterStatusDto> getOLCount(@PathVariable int orgId) {
		try {
			OfferLetterStatusDto offerLetterStatusDto = offerLetterService.getOLCount(orgId);
			return ResponseEntity.ok(offerLetterStatusDto);
		} catch (Exception e) {
			logger.error("Error retrieving Offer Letter count. OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	// getOLCountBySalesGrp getOfferLetterStatusBySalesGrpId

	@GetMapping("/getOfferLettersBySalesGrpId")
	public ResponseEntity<List<OfferLetterDto>> getOfferLettersStatusBySalesGrpId(
			 @RequestParam(required = true) int salesGrpId) {
		try {
			List<OfferLetterDto> offerLetterDtos = offerLetterService.getOfferLetterStsBySalesGrpId( salesGrpId);
			return ResponseEntity.ok(offerLetterDtos);
		} catch (Exception e) {
			logger.error("Error retrieving offer letters status for Sales Group ID: {}", salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getOLCountBySalesGrpId/{salesGrpId}")
	public ResponseEntity<OfferLetterStatusDto> getOLCountBySalesGrp(@PathVariable int salesGrpId) {
		try {
			OfferLetterStatusDto offerLetterStatusDto = offerLetterService.getOLCountBySalesGrp(salesGrpId);
			return ResponseEntity.ok(offerLetterStatusDto);
		} catch (Exception e) {
			logger.error("Error retrieving Offer Letter count for Sales Group ID: {}", salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	private List<OLDto> prepOfferLetterDtosWithoutSort(List<Object[]> offerLetters) {
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);

		// List<String> definedOrder = Arrays.asList("Pending", "Held", "Approved",
		// "Returned");
		List<OLDto> offerLetterDtos = new ArrayList<>();
		for (Object[] ol : offerLetters) {
			OLDto dto = new OLDto();
			dto.setId(Integer.parseInt(ol[0].toString()));
			dto.setSoDate(ol[2] != null ? (ol[2].toString()) : null);
			dto.setName((String.valueOf(ol[1])));
			dto.setSoNumber((String.valueOf(ol[3])));
			dto.setProjectName((String.valueOf(ol[4])));
			boolean isReturned = (String.valueOf(ol[5]).equalsIgnoreCase(STATUS_RETURNED)) ? true : false;
			String status = isReturned ? STATUS_RETURNED : (String.valueOf(ol[6]));
			if((ol[6])== null) {
			status = "File Note Not Generated";
			}
			dto.setFileNoteStatus(status);
			dto.setOlDate(ol[7].toString());
//			dto.setTotalValue(ol[8] != null ? Float.parseFloat(ol[8].toString()) : null);
//			dto.setTotalItem(ol[9] != null ?Integer.parseInt(ol[9].toString()): null);
			 dto.setSalesGroupName(ol[8] != null ? (ol[8].toString()) : null);
			offerLetterDtos.add(dto);
		}
		return offerLetterDtos;
	}
	
	
	@GetMapping("/getOLs")
	public ResponseEntity<List<OfferLetterDto>> getOLs() {
		try {
			List<OfferLetterDto> offerLetterDto = offerLetterService.getAllOLs();
			return ResponseEntity.ok(offerLetterDto);
		} catch (Exception e) {
			logger.error("Error occurred while retrieving all Offer Letters.", e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getNotifiedOLsBySOId/{supplyOrderId}")
	public ResponseEntity<List<OfferLetterDto>> getNotifiedOLsBySOId(@PathVariable int supplyOrderId) {

		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		try {
			List<OfferLetter> offerLetters = offerLetterService.getNotifiedOLsBySOId(supplyOrderId);
			for (OfferLetter offerLetter : offerLetters) {

				OfferLetterDto offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
				SaleOrder saleOrder = offerLetter.getSaleOrder();
				SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
				if (saleOrder.getCategory() != null && saleOrder.getCategory().getShip() != null
						&& saleOrder.getCategory().getShip().getProject() != null) {
					offerLetterDto.setProjectName(saleOrder.getCategory().getShip().getProject().getProjectName());
				}
				offerLetterDto.setFileNo(supplyOrder.getFileNo());
				offerLetterDto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
				String status = offerLetter.getActive().booleanValue() ? ApplicationConstants.ACTIVE :ApplicationConstants.INACTIVE;
				offerLetterDto.setStatus(status);
				boolean isQinAvlbl = !offerLetter.getQinObsNcForms().isEmpty();
				offerLetterDto.setIsQinAvlbl(isQinAvlbl);
				offerLetterDtos.add(offerLetterDto);
			}
		} catch (Exception e) {
			logger.error("Error in getOLBySupplyOrderId for SO Id:  {}. Error: {}", supplyOrderId, e.getMessage());
		}
		return ResponseEntity.ok(offerLetterDtos);
	}
	
	
	//api to get draft inote	
	@GetMapping("/getINoteByOLId/{olId}")
	public ResponseEntity<INoteDto> getINoteByOLId(@PathVariable int olId) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByOLId(olId);

			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving I-Note for Offer Letter ID: {}", olId, e);
	        
			return ResponseEntity.internalServerError().body(null);
		}
	}
	

    @PostMapping("/addLineItem")
    public ResponseEntity<Object> addLineItem(@RequestBody OfferLetterDto offerLetterDto){
    	try {
    		String response = offerLetterService.addLineItem(offerLetterDto);
    		return ResponseEntity.ok(response);    		
    	} catch(IllegalArgumentException e) {
    		logger.error("/offerLetter/addLineItem IllegalArgumentException: {}", e.getMessage());
    		return ResponseEntity.badRequest().body(e.getMessage());
    	} catch(Exception e) {
    		logger.error("/offerLetter/addLineItem Exception: {}", e.getMessage());
    		return ResponseEntity.internalServerError().body(MSG_INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PostMapping("/deleteLineItem")
    public ResponseEntity<Object> deleteLineItem(@RequestBody OfferLetterDto offerLetterDto){
    	try {
    		offerLetterService.deleteLineItem(offerLetterDto);
    		return ResponseEntity.ok(ApplicationConstants.SUCCESS);    		
    	}catch(Exception e) {
    		logger.error("/offerLetter/deleteLineItem Exception: {}", e.getMessage());
    		return ResponseEntity.internalServerError().body(MSG_INTERNAL_SERVER_ERROR);
    	}
    }
    

    @GetMapping("/deleteOl/{id}")
    public ResponseEntity<Object> deleteOl(@PathVariable int id){
    	try {
    		offerLetterService.deleteOl(id);
    		return ResponseEntity.ok(ApplicationConstants.SUCCESS);    		
    	}catch(Exception e) {
    		logger.error("/offerLetter/deleteOl Exception: {}", e.getMessage());
    		return ResponseEntity.internalServerError().body(MSG_INTERNAL_SERVER_ERROR);
    	}
    }
    
    @PutMapping("/editOl")
	public ResponseEntity<Object> editOL(@RequestBody OlEditDto olEditDto) {
		try {
			String response = offerLetterService.editOl(olEditDto);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("/offerLetter/addLineItem Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(MSG_INTERNAL_SERVER_ERROR);
		}
	}
    
    @GetMapping("/getOLs/{orgId}")
	public ResponseEntity<List<OfferLetterDto>> getOLs(@PathVariable int orgId) {
		try {
			List<OfferLetterDto> offerLetterDto = offerLetterService.getAllOLs(orgId);
			return ResponseEntity.ok(offerLetterDto);
		} catch (Exception e) {
			logger.error("Error retrieving all Offer Letters for OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
    
    @GetMapping("/getAllottedOfferLettersByStatusForFirmTest/{orgId}/{empId}")
	public ResponseEntity<List<AllottedRegNosDto>> getAllottedOfferLettersByStatusForFirmTest2(
			@PathVariable int orgId,
			@PathVariable int empId,
			@RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) String status
      ) {
		try {
			List<AllottedRegNosDto> allottedRegNosDtoDtos = offerLetterService.getAllottedOfferLettersByStatusForFirmTest(
					orgId, 
					empId, 
					fromDate,
                    toDate,
                    productId,
                    status);
			return ResponseEntity.ok(allottedRegNosDtoDtos);
		} catch (Exception e) {
			logger.error("Error retrieving alloted Offer Letters by status : {}", e.getMessage());
			return ResponseEntity.internalServerError().body(null);
		}

	}
    
    @GetMapping("/getAllottedOfferLettersByStatusForFirmTestByStatusV2/{orgId}/{empId}/{olStatusIds}")
   	public ResponseEntity<List<AllottedRegNosDto>> getAllottedOfferLettersByStatusForFirmTestByStatusV2(
   			@PathVariable int orgId,
   			@PathVariable int empId,
   			@PathVariable List<Integer> olStatusIds,
   			@RequestParam(required = false) String fromDate,
               @RequestParam(required = false) String toDate,
               @RequestParam(required = false) Integer productId,
               @RequestParam(required = false) String status
         ) {
   		try {
   			List<AllottedRegNosDto> allottedRegNosDtoDtos = offerLetterService.getAllottedOfferLettersByStatusForFirmTestByStatusV2(
   					orgId, 
   					empId, 
   					olStatusIds,
   					fromDate,
                       toDate,
                       productId,
                       status);
   			return ResponseEntity.ok(allottedRegNosDtoDtos);
   		} catch (Exception e) {
   			logger.error("Error retrieving alloted Offer Letters by status: {}", e.getMessage());
   			return ResponseEntity.internalServerError().body(null);
   		}

   	}
    
    
    @GetMapping()
	public ResponseEntity<List<OfferLetterDto>> getOfferLettersById(@RequestParam("olIds") List<Integer> olIds) throws DataNotFoundException {

		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);

		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		
		olIds.forEach(olId -> {
			OfferLetter ol = offerLetterService.getOfferLetterById(olId);
			OfferLetterDto offerLetterDto = mapper.map(ol, OfferLetterDto.class);
			SaleOrder saleOrder = ol.getSaleOrder();
			SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
			offerLetterDto.setTotalItems(supplyOrder.getTotalQty());
			offerLetterDto.setTotalValue(supplyOrder.getValue());
			offerLetterDto.setSoDate(outputDateFormat.format(supplyOrder.getOrderDate()));
			offerLetterDto.setName(ol.getOlNumber());
			offerLetterDto.setSoNumber(supplyOrder.getOrderNo());
			
			List<OlLineItemDto> olLineItemDtos = offerLetterService.getLineItemsByOl(olId);
			offerLetterDto.setOlLineItemDtos(olLineItemDtos);
			
			offerLetterDtos.add(offerLetterDto);
		});

		return ResponseEntity.ok(offerLetterDtos);
	}
    
    
    
    /**
     * @author Shridhar R
     * get All Inotes List By InspOrgId service	
     */
    
    @GetMapping("/getAllOlListByInspOrgId")
    public ResponseEntity<List<OlDetByInspOrgDto>> getAllOlListByInspOrgId() {
        try {
        	List<OlDetByInspOrgDto> olDto = offerLetterService.getAllOlListByInspOrgId();
            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("Error occurred while retrieving all OL lists by Inspection Org ID.", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/{olId}/getRegs")
    public ResponseEntity<Object> getRegsByOlIdQa(@PathVariable(name = "olId") Integer olId ){
    	try {
    		List<Map<String, Object>> regsByMergeSslipId = offerLetterService.getRegsByOlIdQa(olId);
			return ResponseEntity.ok(regsByMergeSslipId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
    }
}
