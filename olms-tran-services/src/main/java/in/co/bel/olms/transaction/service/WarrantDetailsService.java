package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.COMMON_STATUS_CONSTANTS;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.entity.Status;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.SerialNoStatusRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.CategoryDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.WarrantCommonDto;
import in.co.bel.olms.transaction.dto.WarrantDetailsDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.WarrantDetails;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.transaction.repository.WarrantDetailsRepository;
import in.co.bel.olms.transaction.repository.WarrantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarrantDetailsService {

	private final Mapper mapper;

	private final WarrantDetailsRepository warrantDetailsRepository;

	private final LineItemSerialNoService lineItemSerialNoService;

	private final WarrantRepository warrantRepository;

	private final LineItemSerialNoRepository lineItemSerialNoRepository;

	private final SerialNoStatusRepository serialNoStatusRepo;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final SoLineItemRepository soLineItemRepository;

	private final StatusRepository statusRepository;

	private static final Logger logger = LoggerFactory.getLogger(WarrantDetailsService.class);
	
	@Transactional
	public ResponseEntity<String> saveWarrantDetials(WarrantDetailsDto reqDto) {

		if (reqDto.getCategoryId() == null) {
			throw new IllegalArgumentException("Warrant Id is required");
		}

		try {
			Category warrant = warrantRepository.findById(reqDto.getCategoryId())
					.orElseThrow(() -> new IllegalArgumentException("Category/Warrant not found."));
			warrantRepository.updateStatus(warrant.getId(), ApplicationConstants.COMMON_STATUS_CONSTANTS.IN_PROGRESS);
			List<Integer> serialNoIds = reqDto.getLineItemSerialNoIds();

			if (warrant.getSoLineItem().getRegistrationNo()
					.getId() != ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
				List<WarrantDetails> warrDets = new ArrayList<>();
				serialNoIds.forEach(serialNo -> {
					reqDto.setLineItemSerialNoId(serialNo);
					reqDto.setLineItemSerialNoStatusId(SERIAL_NO_STATUS_CONSTANTS.IN_PROGRESS);
					warrDets.add(mapper.map(reqDto, WarrantDetails.class));
				});

				LineItemSerialNoDto dto = new LineItemSerialNoDto();
				dto.setIds(serialNoIds);
				dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.MAPPED);
				lineItemSerialNoService.update(dto);

				warrantDetailsRepository.saveAll(warrDets);
			}

			LineItemSerialNoHistoryDto historyDto = new LineItemSerialNoHistoryDto();
			historyDto.setCreatedById(reqDto.getCreatedById());
			historyDto.setLineItemSerialNoIds(serialNoIds);
			historyDto.setFkName(warrant.getCategoryName());
			historyDto.setFkId(warrant.getId());
			historyDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.MAPPED);
			lineItemSerialNoService.addHistory(historyDto);

			return ResponseEntity.ok("Data Submitted successfully");
		} catch (Exception e) {
			logger.error("error saving warrant details {}", e.getMessage());
			return ResponseEntity.internalServerError().body("error saving warrant details" + e.getMessage());
		}
	}

	public List<WarrantDetailsDto> getAllWarrantDetails() {
		List<WarrantDetails> data = warrantDetailsRepository.findAll();
		List<WarrantDetailsDto> dtos = new ArrayList<>();
		for (WarrantDetails det : data) {

			WarrantDetailsDto dett = mapper.map(det, WarrantDetailsDto.class);
			dett.setLineItemSerialNoStatus(det.getSerialNoStatus().getStatus());
			dtos.add(dett);
		}
		return dtos;

	}

	public List<CategoryDto> getBySoLineId(Integer soLiId) throws DataNotFoundException {

		List<Object[]> warrantList = warrantRepository.getBySoLineId(soLiId);

		List<CategoryDto> dtos = new ArrayList<>();

		for (Object[] item : warrantList) {
			CategoryDto catDto = new CategoryDto();
			catDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
			catDto.setCategoryName(item[1] != null ? item[1].toString() : null);
			catDto.setQty(item[2] != null ? new BigDecimal(item[2].toString().trim()) : null);
			catDto.setShipName(item[3] != null ? item[3].toString() : null);
			catDto.setEstNo(item[3] != null ? item[3].toString() : null);
			catDto.setEstDate(item[4] != null ? (Date) item[4] : null);
			catDto.setWtIssueDate(item[5] != null ? item[5].toString() : null);
			catDto.setOpNo(item[6] != null ? item[6].toString() : null);
			catDto.setRevNo(item[7] != null ? item[7].toString() : null);
			dtos.add(catDto);
		}

		return dtos;

	}

	public List<Map<String, Object>> getWarrantData(Integer soLiId) {

		List<Map<String, Object>> warrantLists = new ArrayList<>();
		List<Category> categories = warrantRepository.findBySoLineItemId(soLiId);

		categories.forEach(cat -> {
			Map<String, Object> newMap = new HashMap<>();
			newMap.put("id", cat.getId());
			newMap.put("warrantName", cat.getCategoryName());
			warrantLists.add(newMap);
		});

		return warrantLists;
	}

	public List<LineItemSerialNoDto> getRegs(Integer soLiId, Integer warrantId, String type) {

		List<LineItemSerialNo> serialNosBySoLiId = lineItemSerialNoRepository.findBySoLineItemIdOrderByIdAsc(soLiId,
				SERIAL_NO_STATUS_CONSTANTS.RFR, SERIAL_NO_STATUS_CONSTANTS.CREATED);

		if (type.equalsIgnoreCase("unmapped")) {
			return serialNosBySoLiId.stream().map(d -> mapper.map(d, LineItemSerialNoDto.class))
					.toList();
		} else { // mapped
			List<WarrantDetails> warrantDets = warrantDetailsRepository.findBySoLineItemIdAndWarrantId(soLiId,
					warrantId);

			if (warrantDets.isEmpty()) {
				warrantRepository.updateStatus(warrantId, COMMON_STATUS_CONSTANTS.CREATED);
			}

			List<LineItemSerialNoDto> dtos = warrantDets.stream().map(warrantDet -> {
				LineItemSerialNoDto dto = new LineItemSerialNoDto();
				dto.setId(warrantDet.getLineItemSerialNo().getId());
				dto.setSerialNo(warrantDet.getLineItemSerialNo().getSerialNo());
				dto.setSoLineItemId(warrantDet.getLineItemSerialNo().getSoLineItem().getId());
				dto.setCreatedBy(warrantDet.getCreatedBy().getEname());
				dto.setCreatedById(warrantDet.getCreatedBy().getId());
				dto.setCreatedAt(warrantDet.getCreatedDate());
				dto.setStatus(warrantDet.getLineItemSerialNo().getStatus().getStatus());
				return dto;
			}).collect(Collectors.toList());
			Collections.sort(dtos, Comparator.comparing(LineItemSerialNoDto::getId));
			return dtos;
		}

	}

	public List<LineItemSerialNoDto> getRegsByWarrantIds(List<Integer> warrantIds) {
		List<Object[]> serialNos = lineItemSerialNoRepository.findRegNos(warrantIds);
		List<LineItemSerialNoDto> dtos = new ArrayList<>();
		for (Object[] item : serialNos) {
			log.info("item: " + item[0]);
			if (item[0] != null) {
				LineItemSerialNoDto liDto = new LineItemSerialNoDto();
				liDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
				liDto.setSerialNo(item[1] != null ? item[1].toString() : null);
				liDto.setStatusId(item[2] != null ? Integer.parseInt(item[2].toString()) : null);

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // Correct format
				Date createdDateTime = null;
				try {
					createdDateTime = format.parse(item[3].toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error("Error in getting Regs by warrantIds ", e);
				}
				liDto.setCreatedAt(createdDateTime);

				liDto.setSoLineItemId(item[5] != null ? Integer.parseInt(item[5].toString()) : null);

				liDto.setLineItemNo(item[8] != null ? item[8].toString() : null);
				dtos.add(liDto);
			}
		}
		return dtos;
	}

	public List<CategoryDto> getAllWarrants(List<Integer> statusIds) throws DataNotFoundException {
		if (statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS,
					COMMON_STATUS_CONSTANTS.COMPLETED); // created, in-progress, completed

		List<Object[]> warrantList = warrantRepository.getAllWarrants(statusIds);

		List<CategoryDto> dtos = new ArrayList<>();

		for (Object[] item : warrantList) {
			if (item[0] != null) {
				CategoryDto catDto = new CategoryDto();
				catDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
				catDto.setCategoryName(item[1] != null ? item[1].toString() : null);
				catDto.setQty(item[2] != null ? new BigDecimal(item[2].toString().trim()) : null);
				catDto.setShipName(item[3] != null ? item[3].toString() : null);
				catDto.setEstNo(item[3] != null ? item[3].toString() : null);
				catDto.setEstDate(item[4] != null ? (Date) item[4] : null);
				catDto.setWtIssueDate(item[5] != null ? item[5].toString() : null);
				catDto.setOpNo(item[6] != null ? item[6].toString() : null);
				catDto.setRevNo(item[7] != null ? item[7].toString() : null);
				catDto.setSoLiId(item[8] != null ? Integer.parseInt(item[8].toString()) : null);
				catDto.setSaleOrderId(item[9] != null ? Integer.parseInt(item[9].toString()) : null);
				catDto.setSaleOrder(item[10] != null ? item[10].toString() : null);
				catDto.setPartNo(item[11] != null ? item[11].toString() : null);
				catDto.setDrawingNo(item[12] != null ? item[12].toString() : null);
				catDto.setMaterialDescription(item[13] != null ? item[13].toString() : null);
				catDto.setOrderMasterNo(item[14] != null ? item[14].toString() : null);
				catDto.setWarrantStaus(
						item[15] != null ? item[15].toString() : ApplicationConstants.REGISTRATION_CONSTANTS.CREATED);
				catDto.setSoNumber(item[16] != null ? item[16].toString() : null);
				catDto.setGrp(item[17] != null ? item[17].toString() : null);
				catDto.setInspAuthorityName(item[18] != null ? item[18].toString() : null);
				catDto.setRegistrationNo(item[19] != null ? (Integer) item[19] : null);
				dtos.add(catDto);
			}
		}

		return dtos;

	}

	public WarrantCommonDto getWarrantDataForTCreation(Integer warrantId) {

		WarrantCommonDto commonDto = new WarrantCommonDto();
		Category cat = warrantRepository.findById(warrantId)
				.orElseThrow(() -> new IllegalArgumentException("Warrant not found"));

		if (cat.getSoLineItem() != null) {
			commonDto.setSoLineItemNo(cat.getSoLineItem().getLineItemNo());
			commonDto.setSoLineItemId(cat.getSoLineItem().getId());
			commonDto.setRegistrationNo(cat.getSoLineItem().getRegistrationNo().getId());
			commonDto.setOfferedQty(cat.getSoLineItem().getOrderQty());
			PartNumber partNo = cat.getSoLineItem().getPartNumber();
			if (partNo != null) {
				commonDto.setMaterialDescription(partNo.getMaterialDescription());
				commonDto.setPartNumber(partNo.getPartNo());
			}

			List<WarrantDetails> warrantDetails = warrantDetailsRepository.findByCategoryId(cat.getId());

			List<WarrantDetailsDto> warrantDetailsDto = warrantDetails.stream()
					.map(warrant -> mapper.map(warrant, WarrantDetailsDto.class))
					.sorted(Comparator.comparing(WarrantDetailsDto::getLineItemSerialNoId))
					.toList();
			commonDto.setWarrantDetailsDtos(warrantDetailsDto);

			List<LineItemSerialNo> serialNos = lineItemSerialNoRepository.findByCategoryId(cat.getId());

			List<LineItemSerialNo> remainingSerialNos = serialNos.stream().filter(item -> item.getStatus() != null)
					.filter(item -> item.getStatus().getStatus() != null)
					.filter(item -> item.getStatus().getStatus().equalsIgnoreCase("Mapped")
							|| item.getStatus().getStatus().equalsIgnoreCase("RFR"))
					.toList();

			BigDecimal remQt = BigDecimal.valueOf(remainingSerialNos.size());
			commonDto.setRemQuantity(remQt);
		}

		return commonDto;
	}
	
	public List<WarrantCommonDto> getSubSlipDataByWarrantId(Integer warrantId) {
		
		List<Object[]> rows = submissionSlipRepository.getSubSlipDetailsByWarrantId(warrantId);
		
		List<WarrantCommonDto> dtoList = new ArrayList<>();
		for (Object[] row : rows) {
	        WarrantCommonDto dto = new WarrantCommonDto();
	        
	        if (row[0] != null) {
	            dto.setSoLineItemId(((Number) row[0]).intValue());
	        }
	        dto.setOlNumber((String) row[1]);
	        dto.setOfferDate(row[2] == null ? null : 
	            ((row[2] instanceof java.sql.Date) ? ((java.sql.Date)row[2]).toLocalDate() : (LocalDate)row[2])
	            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	        
	        if (row[3] != null) {
	        	String regNosString = (String) row[3];
	            
	            // Split the string into an array
	            String[] regArray = regNosString.split(",");
	            
	            
	            List<String> cleanedList = Arrays.asList(regArray).stream()
	                                              .map(String::trim)
	                                              .toList();
	            dto.setRegNosList(cleanedList);
	        }
	        if (row[4] != null) {
	            dto.setOfferedQty(new BigDecimal (row[4].toString()));
	        }
	        dtoList.add(dto);
		}    
		return dtoList;
	}
	
	  public WarrantCommonDto getSubSlipDataForTUpdation(Integer sSlipId) {

		List<Object[]> rows = submissionSlipRepository.getSubSlipDetails(sSlipId);

		WarrantCommonDto commonDto = new WarrantCommonDto();

		if (rows != null && !rows.isEmpty()) {

			Object[] subSlip = rows.get(0);

			logger.info("Actual columns found: {}", subSlip.length);

			commonDto.setOlNumber(subSlip[0] != null ? subSlip[0].toString() : null);
			commonDto.setOfferDate(subSlip[1] != null ? subSlip[1].toString() : null);

			if (subSlip[2] != null) {
				commonDto.setOfferedQty(new BigDecimal(subSlip[2].toString()));
			}
			List<Object[]> regNos = submissionSlipRepository.getSerialNosBySubSlipId(sSlipId);

			List<Map<String, Object>> regMapList = regNos.stream().map(regNo -> {
				Map<String, Object> dto = new HashMap<>();

				dto.put("lineItemSerialId", regNo[0]);
				dto.put("lineItemSerialNo", regNo[1]);

				return dto;
			}).toList();
			commonDto.setRegNos(regMapList);

			List<Object[]> allRegNos = submissionSlipRepository.getAllSerialNosBySubSlipId(sSlipId);
			List<Map<String, Object>> regMapList1 = allRegNos.stream().map(regNo1 -> {
				Map<String, Object> dto = new HashMap<>();

				dto.put("lineItemSerialId", regNo1[0]);
				dto.put("lineItemSerialNo", regNo1[1]);

				return dto;
			}).toList();
			commonDto.setAllRegNos(regMapList1);
			BigDecimal size = BigDecimal.valueOf(regNos.size());
			BigDecimal total = subSlip[3] != null ? (BigDecimal) subSlip[3] : null;
			BigDecimal remQt = total.subtract(size);
			commonDto.setRemQuantity(remQt);
		}
		return commonDto;
	}

	@Transactional
	public String unmapSerialNo(WarrantDetailsDto dto) {

		Integer warrantId = dto.getCategoryId();
		Integer serialNoId = dto.getLineItemSerialNoId();

		WarrantDetails wd = warrantDetailsRepository.findByWarrantIdAndSerialNoId(warrantId, serialNoId).orElseThrow(
				() -> new IllegalArgumentException("Warrant details not found the given WarrantId and serialNoId"));
		SerialNoStatus status = serialNoStatusRepo.findById(SERIAL_NO_STATUS_CONSTANTS.CREATED).orElse(null);
		LineItemSerialNo serialNo = lineItemSerialNoRepository.findById(serialNoId).orElse(null);
		serialNo.setStatus(status);
		lineItemSerialNoRepository.save(serialNo);
		warrantDetailsRepository.delete(wd);

		LineItemSerialNoHistoryDto historyDto = new LineItemSerialNoHistoryDto();
		historyDto.setCreatedById(dto.getCreatedById());
		historyDto.setLineItemSerialNoId(serialNoId);
		historyDto.setFkName(wd.getCategory().getCategoryName());
		historyDto.setFkId(wd.getCategory().getId());
		historyDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.UNMAPPED);
		lineItemSerialNoService.addHistory(historyDto);

		return "Serial number unmapped succesfully!";
	}

	public List<CategoryDto> getWarrantById(Integer warrantId) throws DataNotFoundException {

		List<Object[]> warrantList = warrantRepository.getWarrantById(warrantId);

		List<CategoryDto> dtos = new ArrayList<>();

		for (Object[] item : warrantList) {
			if (item[0] != null) {
				CategoryDto catDto = new CategoryDto();
				catDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
				catDto.setCategoryName(item[1] != null ? item[1].toString() : null);
				catDto.setQty(item[2] != null ? new BigDecimal(item[2].toString().trim()) : null);
				catDto.setShipName(item[3] != null ? item[3].toString() : null);
				catDto.setEstNo(item[3] != null ? item[3].toString() : null);
				catDto.setEstDate(item[4] != null ? (Date) item[4] : null);
				catDto.setWtIssueDate(item[5] != null ? item[5].toString() : null);
				catDto.setOpNo(item[6] != null ? item[6].toString() : null);
				catDto.setRevNo(item[7] != null ? item[7].toString() : null);
				catDto.setSoLiId(item[8] != null ? Integer.parseInt(item[8].toString()) : null);
				catDto.setSaleOrderId(item[9] != null ? Integer.parseInt(item[9].toString()) : null);
				catDto.setSaleOrder(item[10] != null ? item[10].toString() : null);
				catDto.setPartNo(item[11] != null ? item[11].toString() : null);
				catDto.setDrawingNo(item[12] != null ? item[12].toString() : null);
				catDto.setMaterialDescription(item[13] != null ? item[13].toString() : null);
				catDto.setOrderMasterNo(item[14] != null ? item[14].toString() : null);
				dtos.add(catDto);
			}
		}
		return dtos;
	}

	public List<CategoryDto> getWarrantyByHosEmpId(Integer empId, Integer empRoleId, List<Integer> statusIds) {
		List<CategoryDto> dtos = new ArrayList<>();
		if (statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS,
					COMMON_STATUS_CONSTANTS.COMPLETED);

		List<Object[]> warrantList = warrantRepository.getWarrantyByHosEmpId(empId, statusIds);// , empRoleId);
		for (Object[] item : warrantList) {
			if (item[0] != null) {
				CategoryDto catDto = new CategoryDto();
				catDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
				catDto.setCategoryName(String.valueOf(item[1]));
				catDto.setQty(item[2] != null ? new BigDecimal(item[2].toString().trim()) : null);
				catDto.setShipName(String.valueOf(item[3]));
				catDto.setEstNo(String.valueOf(item[3]));
				catDto.setEstDate(item[4] != null ? (Date) item[4] : null);
				catDto.setWtIssueDate(String.valueOf(item[5]));
				catDto.setOpNo(String.valueOf(item[6]));
				catDto.setRevNo(String.valueOf(item[7]));
				catDto.setSoLiId(item[8] != null ? Integer.parseInt(item[8].toString()) : null);
				SOLineItem soLineItem = soLineItemRepository.findById(catDto.getSoLiId()).orElseThrow(
						() -> new IllegalArgumentException("SoLineItem not found for id: " + catDto.getSoLiId()));
				String inspAuth = soLineItem.getInspectionAuthorityId().getOrganizationName();
				Integer regType = soLineItem.getRegistrationNo().getId();
				catDto.setRegistrationNo(regType);
				catDto.setInspAuthorityName(inspAuth);
				catDto.setSaleOrderId(item[9] != null ? Integer.parseInt(item[9].toString()) : null);
				catDto.setSaleOrder(String.valueOf(item[10]));
				catDto.setPartNo(String.valueOf(item[11]));
				catDto.setDrawingNo(String.valueOf(item[12]));
				catDto.setMaterialDescription(String.valueOf(item[13]));
				catDto.setOrderMasterNo(String.valueOf(item[14]));

				catDto.setWarrantStaus(item[17] != null ? String.valueOf(item[17])
						: ApplicationConstants.REGISTRATION_CONSTANTS.CREATED);

				catDto.setGrp(item[18] != null ? item[18].toString() : null);
				catDto.setRegistrationNo(item[19] != null ? (Integer) item[19] : null);
				dtos.add(catDto);
			}
		}
		return dtos;
	}

	public List<CategoryDto> getAllotedWarrants(Integer empId, List<Integer> statusIds) {
		List<CategoryDto> dtos = new ArrayList<>();

		if (statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS,
					COMMON_STATUS_CONSTANTS.COMPLETED);

		List<Object[]> warrantList = warrantRepository.getAllotedWarrants(empId, statusIds);
		for (Object[] item : warrantList) {
			if (item[0] != null) {
				CategoryDto catDto = new CategoryDto();
				catDto.setId(item[0] != null ? Integer.parseInt(item[0].toString()) : null);
				catDto.setCategoryName(String.valueOf(item[1]));
				catDto.setQty(item[2] != null ? new BigDecimal(item[2].toString().trim()) : null);
				catDto.setShipName(String.valueOf(item[3]));
				catDto.setEstNo(String.valueOf(item[3]));
				catDto.setEstDate(item[4] != null ? (Date) item[4] : null);
				catDto.setWtIssueDate(String.valueOf(item[5]));
				catDto.setOpNo(String.valueOf(item[6]));
				catDto.setRevNo(String.valueOf(item[7]));
				catDto.setSoLiId(item[8] != null ? Integer.parseInt(item[8].toString()) : null);
				catDto.setSaleOrderId(item[9] != null ? Integer.parseInt(item[9].toString()) : null);
				catDto.setSaleOrder(String.valueOf(item[10]));
				catDto.setPartNo(String.valueOf(item[11]));
				catDto.setDrawingNo(String.valueOf(item[12]));
				catDto.setMaterialDescription(String.valueOf(item[13]));
				catDto.setOrderMasterNo(String.valueOf(item[14]));
				catDto.setGrp(item[15] != null ? item[15].toString() : null);
				catDto.setInspAuthorityName(item[16] != null ? item[16].toString() : null);
				catDto.setWarrantStaus(item[17] != null ? item[17].toString() : null);
				dtos.add(catDto);
			}
		}
		return dtos;
	}

	@Transactional
	public void checkAndUpdateWarrantStatus(Integer id , boolean isInternalInote) {

		String idName = isInternalInote ? "SubSlipId " : "OlId ";
		try {
			
		
		if (id == null)
			throw new IllegalArgumentException(idName + "cannot be null");

		List<Category> categories = isInternalInote ? warrantRepository.findbySslipId(id)
				: warrantRepository.findbyOlId(id);
		
		if (categories.isEmpty())
			throw new IllegalArgumentException("Warrants not found for " + idName + id);

		List<Category> newCategories = new ArrayList<>();
		for (Category category : categories) {
			BigDecimal getPassedQty = isInternalInote? warrantRepository.findPassedByWarrantIdInternal(category.getId()): warrantRepository.findPassedBywarrantIdExternal(category.getId());
			if(getPassedQty.compareTo(category.getQty())== 0) {
				Status warrStatus = statusRepository.findById(ApplicationConstants.COMMON_STATUS_CONSTANTS.COMPLETED)
						.orElseThrow(()-> new IllegalArgumentException("No Status found with given statud id: "+ ApplicationConstants.COMMON_STATUS_CONSTANTS.COMPLETED));
				category.setSerialNoStatus(warrStatus);			
			}
			newCategories.add(category);
		}
		warrantRepository.saveAll(newCategories);
//		
//		Status warrantStatus = statusRepository.findById(status)
//				.orElseThrow(() -> new IllegalArgumentException("No Status found with given status Id: " + status));
//
//		categories.forEach(category -> category.setSerialNoStatus(warrantStatus));
//		warrantRepository.saveAll(categories);
		} catch(Exception e) {
			log.error("Error in updating warrant status for ID: {}. Error: {}", id, e.getMessage());
			throw e;
		}

	}

}
