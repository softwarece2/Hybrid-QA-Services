package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.core.constants.ApplicationConstants.Role;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.LOT_STATUS;
import in.co.bel.olms.constants.ApplicationConstants.SO_STATUS_CONSTANTS;
import in.co.bel.olms.exception.ResourceNotFoundException;
import in.co.bel.olms.master.dto.ChartValuePair;
import in.co.bel.olms.master.dto.ConsigneeDto;
import in.co.bel.olms.master.entity.Account;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.INoteType;
import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.AccountRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.INoteTypeRepository;
import in.co.bel.olms.master.repository.IndenterRepository;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.master.repository.SoStatusRepository;
import in.co.bel.olms.master.repository.SoTypeRepository;
import in.co.bel.olms.master.repository.SupplierRepository;
import in.co.bel.olms.transaction.dto.AmendmentsLetterDto;
import in.co.bel.olms.transaction.dto.DdExtensionDto;
import in.co.bel.olms.transaction.dto.ItemLotsDto;
import in.co.bel.olms.transaction.dto.SoAttachmentDetailDto;
import in.co.bel.olms.transaction.dto.SoAttachmentDto;
import in.co.bel.olms.transaction.dto.SoTreeViewDto;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.dto.SupplyOrderExpiryDTO;
import in.co.bel.olms.transaction.dto.SupplyOrderInputDto;
import in.co.bel.olms.transaction.dto.SupplyOrderSuperAdminDto;
import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.entity.AmendmentsLetterAttachment;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.DdExtensionAttachment;
import in.co.bel.olms.transaction.entity.ItemLots;
import in.co.bel.olms.transaction.entity.ItemLotsAttachment;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.Ship;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.SupplyOrderAttachment;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.ItemLotsRepository;
import in.co.bel.olms.transaction.repository.PoObservationRepository;
import in.co.bel.olms.transaction.repository.SoAllotmentRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderAttachmentRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.validator.FileValidator;
import in.co.bel.olms.validator.InputValidation;
import lombok.RequiredArgsConstructor;

/**
 * SupplyOrder service class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2022-08-16
 */

@Service
@RequiredArgsConstructor
public class SupplyOrderService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final SupplyOrderRepository supplyOrderRepository;	

	private final EmployeeRepository empRepository;

	private final SalesGroupRepository salesGrpRepository;

	private final INoteTypeRepository iNoteTypeRepository;
	
	private final IndenterRepository indenterRepository;
	
	private final SupplierRepository supplierRepository;
	
	private final SoTypeRepository soTypeRepository;
	
	private final SoAllotmentRepository soAllotmentRepository;

	private final SupplyOrderAttachmentRepository soAttachmentRepository;
	
	private final PoObservationRepository poObservationRepository;

	private final AccountRepository accountRepository;

	private final SoLineItemRepository soLineItemRepository;

	private final Mapper mapper;

	private final FileStorageService fileStorageService;

	private final ItemLotsRepository itemLotsRepository;

	private final SoStatusRepository soStatusRepository;

	private static final Logger logger = LoggerFactory.getLogger(SupplyOrderService.class);
	
	private static final String ITEM_LOTS ="ItemLots";
	
	private static final String PROJECT_NAME ="Project Name";

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	

	/**
	 * 
	 * @param supplyOrderDto
	 * @return
	 */
	public SupplyOrder saveSupplyOrder(SupplyOrderDto supplyOrderDto) {

		SupplyOrder supplyOrder = mapper.map(supplyOrderDto, SupplyOrder.class);
		supplyOrder.setCreatedDate(new Date());
		return supplyOrderRepository.save(supplyOrder);

	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public SupplyOrder getSupplyOrder(int id) {

		Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(id);
		if (supplyOrderOptional.isPresent()) {
			return supplyOrderOptional.get();
		} else {
			throw new DataNotFoundException("SupplyOrder not found with Id: " + id);
		}
	}

	public String getSoNumber(int id) {
		String SoNumber = null;
		List<Object[]> soOrder = supplyOrderRepository.getById(id);
		for (Object[] so : soOrder) {
			SoNumber = (so != null ? so[10].toString() : null);
		}
		return SoNumber;
	}

	public SupplyOrderDto getSupplyOrderById(int id) {

		Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(id);
		if (supplyOrderOptional.isPresent()) {
			SupplyOrder supplyOrder = supplyOrderOptional.get();
			SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
			List<Consignee> consignees = supplyOrder.getSoConsigneeMappings();
			List<ItemLotsDto> itemLots = new ArrayList<>();
			List<DdExtensionDto> ddExtn = new ArrayList<>();
			List<AmendmentsLetterDto> amndt = new ArrayList<>();
			for (ItemLots il : supplyOrder.getItemLots()) {
				ItemLotsDto itDto = mapper.map(il, ItemLotsDto.class);
				List<String> uploadedFilesUrl = new ArrayList<>();
				List<ItemLotsAttachment> attachments = il.getAttachments();
				if (attachments != null) {
					for (ItemLotsAttachment j : attachments) {
						String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
								+ j.getFileName();
						uploadedFilesUrl.add(fileDownloadUri);
					}
					itDto.setFileDownloadUri(uploadedFilesUrl);
				}

				itemLots.add(itDto);

			}
			for (DdExtension ddExt : supplyOrder.getDdExtensions()) {
				DdExtensionDto ddDto = mapper.map(ddExt, DdExtensionDto.class);
				List<String> uploadedFilesUrl = new ArrayList<>();
				List<DdExtensionAttachment> attachments = ddExt.getAttachments();
				if (attachments != null) {
					for (DdExtensionAttachment j : attachments) {
						String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
								+ j.getFileName();
						uploadedFilesUrl.add(fileDownloadUri);
					}
					ddDto.setFileDownloadUri(uploadedFilesUrl);
				}

				ddExtn.add(ddDto);

			}
			for (AmendmentsLetter amd : supplyOrder.getAmendmentsLetters()) {
				AmendmentsLetterDto amdDto = mapper.map(amd, AmendmentsLetterDto.class);
				List<String> uploadedFilesUrl = new ArrayList<>();
				List<AmendmentsLetterAttachment> attachments = amd.getAttachments();
				if (attachments != null) {
					for (AmendmentsLetterAttachment j : attachments) {
						String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
								+ j.getFileName();
						uploadedFilesUrl.add(fileDownloadUri);
					}
					amdDto.setFileDownloadUri(uploadedFilesUrl);
				}

				amndt.add(amdDto);

			}
			Integer count1 = soLineItemRepository.getSoLineItemCount(id);
			int noOfItems = (count1 == null) ? 0 : count1;
			supplyOrderDto.setNoOfItems(noOfItems);
			List<Integer> consigneeIds = consignees.stream().map(consignee -> consignee.getId())
					.toList();
			supplyOrderDto.setConsigneeIds(consigneeIds);
			List<String> consigneeStr = consignees.stream().map(consignee -> consignee.getName())
					.toList();
			supplyOrderDto.setConsigneeName(String.join(",", consigneeStr));
			supplyOrderDto.setItemLots(itemLots);
			supplyOrderDto.setDdExtensions(ddExtn);
			supplyOrderDto.setAmendmentsLetters(amndt);
			supplyOrderDto.setAcctName(
					supplyOrder.getAccountType() != null ? supplyOrder.getAccountType().getAcctType() : null);
			String amendedItemLot = itemLotsRepository.getAmendedLotNo(id);
			supplyOrderDto.setAmendedLotNo(amendedItemLot);
			int count = supplyOrderRepository.getNotifiedCount(supplyOrder.getId());
			if (count > 0)
				supplyOrderDto.setIsNotifiedToJc(true);
//			Adding the attachments URLs to supply order DTO
			List<SupplyOrderAttachment> attachment = supplyOrder.getAttachments();
			SupplyOrderAttachment atmt = new SupplyOrderAttachment();
			List<String> qapFiles = new ArrayList<>();
			List<String> drawingFiles = new ArrayList<>();
			List<String> atpFiles = new ArrayList<>();
			List<String> otherFiles = new ArrayList<>();
			List<String> amendments = new ArrayList<>();
			List<String> poFiles = new ArrayList<>();

			for (SupplyOrderAttachment soAttachment : attachment) {
				atmt.setCategory(soAttachment.getCategory());
				String category = soAttachment.getCategory();
				String fileName = soAttachment.getFileName();
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
						+ fileName;
				switch (category) {
				case "QAP":
					qapFiles.add(fileDownloadUri);
					break;
				case "Drawings":
					drawingFiles.add(fileDownloadUri);
					break;
				case "ATP":
					atpFiles.add(fileDownloadUri);
					break;
				case "Others":
					otherFiles.add(fileDownloadUri);
					break;
				case "Amendments":
					amendments.add(fileDownloadUri);
					break;
				case "PO":
					poFiles.add(fileDownloadUri);
					break;
				default:
					break;
				}
			}
			supplyOrderDto.setAtpFiles(atpFiles);
			supplyOrderDto.setAttachments(otherFiles);
			supplyOrderDto.setDrawings(drawingFiles);
			supplyOrderDto.setQapFiles(qapFiles);
			supplyOrderDto.setAmendments(amendments);
			supplyOrderDto.setPoFiles(poFiles);

			try {
				supplyOrderDto.setOrganizationName(supplyOrder.getOrganization().getOrganizationName());
			} catch (Exception e) {
				supplyOrderDto.setOrganizationName(null);
			}

			return supplyOrderDto;

		} else {
			throw new DataNotFoundException("SupplyOrder not found with Id: " + id);
		}
	}

	/**
	 * @author Manasa B
	 * @param id
	 * @return
	 */
	public List<SupplyOrderDto> getSupplyOrderByCqaeId(int id) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();

		List<SupplyOrder> supplyOrders = supplyOrderRepository.getSupplyOrderByCqaeId(id);
		for (SupplyOrder supplyOrder : supplyOrders) {
			SupplyOrderDto supplyOrderDto = mapper.map(supplyOrder, SupplyOrderDto.class);
			supplyOrderDtos.add(supplyOrderDto);
		}
		return supplyOrderDtos;
	}

	/**
	 * 
	 * @return
	 */
	public List<SupplyOrder> getAllSupplyOrders() {

		return supplyOrderRepository.findAll();

	}

	public Map<Boolean, String> saveSupplyOrders(SupplyOrderInputDto supplyOrderInputDto) {
		Map<Boolean, String> returnMap = new HashMap<>();
		List<String> existingSupplyOrderNos = supplyOrderRepository.getAllSupplyOrdersNos();
		List<SupplyOrder> supplyOrders = new ArrayList<>();
		Integer empId = supplyOrderInputDto.getEmpId();
		Integer salesGroupId = empRepository.getSalesGrpByEmpId(empId);
		Optional<SalesGroup> salesGroupOptional = salesGrpRepository.findById(salesGroupId);
		SalesGroup salesGroup = null;
		if (salesGroupOptional.isPresent()) {
			salesGroup = salesGroupOptional.get();
		}
		List<SupplyOrderDto> supplyOrderDtos = supplyOrderInputDto.getSupplyOrderDtos();
		try {

			for (SupplyOrderDto supplyOrderDto : supplyOrderDtos) {
				if (existingSupplyOrderNos.contains(supplyOrderDto.getOrderNo())) {
					String respString = "Supply order exists with Order No.: " + supplyOrderDto.getOrderNo();
					logger.error(respString);
					returnMap.put(false, respString);
					return returnMap;
				}
				if (supplyOrderDto.getOrderNo() == null
//						|| !InputNameValidation.checkString(supplyOrderDto.getOrderNo())
				) {
					String respString = "Invalid or null Order No.: " + supplyOrderDto.getOrderNo();
					logger.error(respString);
					returnMap.put(false, respString);
					
					return returnMap;
				}

				Date currentDate = new Date();

				// Validate Order Date
				if (supplyOrderDto.getOrderDate() != null && supplyOrderDto.getOrderDate().after(currentDate)) {
					throw new DataNotValidException(
							"Order Date cannot be in the future for Order No.: " + supplyOrderDto.getOrderNo());
				}

				// Validate Receipt Date
				if (supplyOrderDto.getReceiptDate() != null && supplyOrderDto.getReceiptDate().after(currentDate)) {
					throw new DataNotValidException(
							"Receipt Date cannot be in the future for Order No.: " + supplyOrderDto.getOrderNo());
				}

				SupplyOrder supplyOrder = mapper.map(supplyOrderDto, SupplyOrder.class);
				if (salesGroup != null) {
					supplyOrder.setSalesGroup(salesGroup);
				}
				supplyOrder.setCreatedDate(new Date());
//				supplyOrder.setStatus("Active");
				//Modified by Chirag on 25-09-26. Added new isActive Field
				supplyOrder.setIsActive(true);
				Optional<SoStatus> soStatusOptional = soStatusRepository.findById(SO_STATUS_CONSTANTS.CREATED);
				if (soStatusOptional.isPresent()) {
					supplyOrder.setSoStatus(soStatusOptional.get());
				}
				supplyOrders.add(supplyOrder);
			}
			supplyOrderRepository.saveAll(supplyOrders);
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (DataNotValidException de) {
			logger.error("Validation failed: {}", de.getMessage());
			throw de;
		} catch (Exception e) {
			logger.error("Error in saving supply orders {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public boolean updateSupplyOrder(SupplyOrderDto supplyOrderDto) {
		boolean returnStatus = false;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(supplyOrderDto.getId());
			if (supplyOrderOptional.isPresent()) {
				SupplyOrder supplyOrder = supplyOrderOptional.get();
				
	            // Look for an existing so no.
	            SupplyOrder existingOrderWithSameNo = supplyOrderRepository.findByOrderNo(supplyOrderDto.getOrderNo());
	            if (existingOrderWithSameNo != null && !existingOrderWithSameNo.getId().equals(supplyOrderDto.getId())) {
	                throw new DataNotValidException("SO Number " + supplyOrderDto.getOrderNo() + " already exists.");
	            }

				supplyOrder.setFileNo(supplyOrderDto.getFileNo());
				supplyOrder.setOrderNo(supplyOrderDto.getOrderNo());
				if (null != supplyOrderDto.getOrderDate()) {
					supplyOrder.setOrderDate(supplyOrderDto.getOrderDate());
				}
				if (null != supplyOrderDto.getReceiptDate()) {
					supplyOrder.setReceiptDate(supplyOrderDto.getReceiptDate());
				}
				if (null != supplyOrderDto.getIndenterId()) {

					Optional<Indenter> indenterOpt = indenterRepository.findById(supplyOrderDto.getIndenterId());
					if (indenterOpt.isPresent()) {
						supplyOrder.setIndenter(indenterOpt.get());
					}
				}
				if (null != supplyOrderDto.getInoteTypeId()) {

					Optional<INoteType> iNoteOPt = iNoteTypeRepository.findById(supplyOrderDto.getInoteTypeId());
					if (iNoteOPt.isPresent()) {

						supplyOrder.setInoteType(iNoteOPt.get());

					}

				}
				if (null != supplyOrderDto.getSupplierId()) {

					Optional<Supplier> supplierOpt = supplierRepository.findById(supplyOrderDto.getSupplierId());
					if (supplierOpt.isPresent()) {
						supplyOrder.setSupplier(supplierOpt.get());
					}

				}
				if (null != supplyOrderDto.getSoType()) {

					Optional<SoType> soTypeOpt = soTypeRepository.findById(supplyOrderDto.getSoType());
					if (soTypeOpt.isPresent()) {
						supplyOrder.setSoType(soTypeOpt.get());
					}

				}
				if (0 != supplyOrderDto.getAccountTypeId()) {
					Optional<Account> accountOptional = accountRepository.findById(supplyOrderDto.getAccountTypeId());
					if (accountOptional.isPresent()) {
						supplyOrder.setAccountType(accountOptional.get());
					}
				}
				supplyOrder.setLotNo(supplyOrderDto.getLotNo());
				if (!InputValidation.checkRemark(supplyOrderDto.getDescription())) {
					throw new DataNotValidException("so description:  " + supplyOrderDto.getDescription());
				}
				supplyOrder.setDescription(supplyOrderDto.getDescription());

				try {
					Date dd = format.parse(supplyOrderDto.getDdStr());
					supplyOrder.setDd(dd);
				} catch (Exception e) {
					logger.info("Delivery date is empty");
				}
				
				supplyOrder.setIsActive(supplyOrderDto.getIsActive());
				supplyOrder.setModifiedDate(new Date());
				supplyOrder.setIsRegistrationNo(supplyOrderDto.getIsRegistrationNo());
				supplyOrder.setDrawingNo(supplyOrderDto.getDrawingNo());
				supplyOrder.setReferenceNo(supplyOrderDto.getReferenceNo());
				supplyOrderRepository.save(supplyOrder);
				returnStatus = true;
			}
			return returnStatus;
		} catch (DataNotValidException e) {
	        // Re-throw this so the controller can catch it and show the specific error message
	        logger.error("Validation Error: {}", e.getMessage());
	        throw e; 
	    } catch (Exception e) {
        	logger.error("Error occured while updating supply orders ", e);
			logger.error("Error in updating supply orders {}", e.getMessage());
			return false;
		}
	}

	public boolean deleteSupplyOrder(int soId) {

		boolean response = false;
		try {
			Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(soId);
			if (supplyOrderOptional.isPresent()) {
				supplyOrderRepository.delete(supplyOrderOptional.get());
				response = true;
			}
		} catch (Exception e) {
			logger.error("Error in deleting supply order {}", e.getMessage());
		}
		return response;
	}

	public List<SupplyOrderDto> getAllSupplyOrdersByOrgId(int orgId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		try {
			List<SupplyOrder> supplyOrder = supplyOrderRepository.getAllSupplyOrdersByOrgId(orgId);
			for (SupplyOrder so : supplyOrder) {
				SupplyOrderDto supplyOrderDto = mapper.map(so, SupplyOrderDto.class);
				List<Consignee> consignees = so.getSoConsigneeMappings();
				List<Integer> consigneeIds = consignees.stream().map(consignee -> consignee.getId())
						.toList();
				supplyOrderDto.setConsigneeIds(consigneeIds);

				try {
					supplyOrderDto = updateSubmitedDatesSts(so, supplyOrderDto);
				} catch (Exception e) {
					 logger.error("Failed to update submitted dates status for Supply Order: {}. Error: {}", 
			                 (so != null ? so.getId() : "Unknown ID"), e.getMessage(), e);
				}

				List<SupplyOrderAttachment> attachment = so.getAttachments();
				SupplyOrderAttachment atmt = new SupplyOrderAttachment();
				List<String> qapFiles = new ArrayList<>();
				List<String> drawingFiles = new ArrayList<>();
				List<String> atpFiles = new ArrayList<>();
				List<String> otherFiles = new ArrayList<>();
				List<String> amendments = new ArrayList<>();
				List<String> poFiles = new ArrayList<>();

				for (SupplyOrderAttachment soAttachment : attachment) {
					atmt.setCategory(soAttachment.getCategory());
					String category = soAttachment.getCategory();
					String fileName = soAttachment.getFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
							+ fileName;
					switch (category) {
					case "QAP":
						qapFiles.add(fileDownloadUri);
						break;
					case "Drawings":
						drawingFiles.add(fileDownloadUri);
						break;
					case "ATP":
						atpFiles.add(fileDownloadUri);
						break;
					case "Others":
						otherFiles.add(fileDownloadUri);
						break;
					case "Amendments":
						amendments.add(fileDownloadUri);
						break;
					case "PO":
						poFiles.add(fileDownloadUri);
						break;
					default:
						break;
					}
				}
				supplyOrderDto.setAtpFiles(atpFiles);
				supplyOrderDto.setAttachments(otherFiles);
				supplyOrderDto.setDrawings(drawingFiles);
				supplyOrderDto.setQapFiles(qapFiles);
				supplyOrderDto.setAmendments(amendments);
				supplyOrderDto.setPoFiles(poFiles);
				supplyOrderDtos.add(supplyOrderDto);
			}

		} catch (Exception e) {

			logger.error("Error in  fetching supply orders {}", e.getMessage());

		}

		return supplyOrderDtos;

	}

	public List<SupplyOrderDto> getAllNotifiedSupplyOrdersByOrgIdOptimized(int orgId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<Object[]> supplyOrder = supplyOrderRepository.getNotifiedSupplyOrders(orgId);
			for (Object[] obj : supplyOrder) {
				SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
				supplyOrderDto.setId(Integer.parseInt(obj[0].toString()));
				supplyOrderDto.setOrderNo(obj[1].toString());
				supplyOrderDto.setFileNo(obj[2] != null ? obj[2].toString() : null);
				supplyOrderDto.setNoOfItems(obj[3] != null ? Integer.parseInt(obj[3].toString()) : null);
				supplyOrderDto.setTotalQty(obj[4] != null ? Integer.parseInt(obj[4].toString()) : null);
				supplyOrderDto.setDd(obj[5] != null ? format.parse(obj[5].toString()) : null);
				supplyOrderDto.setOrderDate(obj[6] != null ? format.parse(obj[6].toString()) : null);
				supplyOrderDto.setStatus(obj[7] != null ? obj[7].toString() : "Inactive");
				supplyOrderDto.setIsNotified((Boolean) obj[8]);
				supplyOrderDto.setProjectName(obj[9] != null ? obj[9].toString() : null);
				supplyOrderDto.setSalesGroupName(obj[10] != null ? obj[10].toString() : null);
				supplyOrderDtos.add(supplyOrderDto);
			}

			return supplyOrderDtos;
		} catch (Exception e) {
        	logger.error("Error occured while fetching supply orders ", e);
			logger.error("Error in  fetching supply orders {}", e.getMessage());

		}

		return supplyOrderDtos;

	}

	public List<SupplyOrderDto> getAllSupplyOrdersByEmpId(int empId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		try {
			List<Object[]> soAllotments = soAllotmentRepository.getAllotedSoDetailsByEmpId(empId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Object[] obj : soAllotments) {
				SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
				supplyOrderDto.setId(Integer.parseInt(obj[0].toString()));
				supplyOrderDto.setOrderNo(obj[1].toString());
				supplyOrderDto.setOrderDate(obj[2] != null ? sdf.parse(obj[2].toString()) : null);
				supplyOrderDto.setSalesGroupName(obj[3].toString());
				supplyOrderDto.setProjectName(obj[4] != null ? obj[4].toString() : "");
				supplyOrderDto.setStatus(obj[5] != null ? obj[5].toString() : "");
				supplyOrderDto.setIsScrutinySubmitted(null != obj[6]);
				supplyOrderDto.setIsFclSubmitted(null != obj[7]);
				supplyOrderDto.setIsFcmSubmitted(null != obj[8]);
				supplyOrderDto.setIsClosed(null != obj[9]);
				supplyOrderDto.setDescription(String.valueOf(obj[10]));
				supplyOrderDto.setIsScrutinyTenderSubmitted(null != obj[11]);
				supplyOrderDto.setSoTypeName(obj[12] != null ? obj[12].toString() : "");
				supplyOrderDtos.add(supplyOrderDto);
			}

		} catch (Exception e) {

			logger.error("Error in  fetching supply orders {}", e.getMessage());

		}

		return supplyOrderDtos;

	}

	private SupplyOrderDto updateSubmitedDatesSts(SupplyOrder so, SupplyOrderDto supplyOrderDto) {
		try {

			if (null != so.getScrutinySo()) {
				if (null != so.getScrutinySo().getSubmittedDatetime()) {
					supplyOrderDto.setIsScrutinySubmitted(true);
				} else {
					supplyOrderDto.setIsScrutinySubmitted(false);
				}
			} else {
				supplyOrderDto.setIsScrutinySubmitted(false);
			}

		} catch (Exception e) {
			 logger.error("Error calculating Scrutiny submission status for Supply Order: {}", e.getMessage());
			supplyOrderDto.setIsScrutinySubmitted(false);
		}

		try {
			if (null != so.getFcl()) {
				if (null != so.getFcl().getSubmittedDate()) {
					supplyOrderDto.setIsFclSubmitted(true);
				} else {
					supplyOrderDto.setIsFcmSubmitted(false);
				}
			} else {
				supplyOrderDto.setIsFclSubmitted(false);

			}
		} catch (Exception e) {
			logger.error("Error determining FCL submission status for Supply Order: {}", e.getMessage());
			supplyOrderDto.setIsFclSubmitted(false);
		}

		try {
			if (null != so.getFileClosingForm()) {
				if (null != so.getFileClosingForm().getSubmittedDate()) {
					supplyOrderDto.setIsClosed(true);
				} else {
					supplyOrderDto.setIsClosed(false);
				}
			} else {
				supplyOrderDto.setIsClosed(false);

			}
		} catch (Exception e) {
			 logger.error("Error determining File Closing Form status for Supply Order: {}", e.getMessage());
			supplyOrderDto.setIsClosed(false);
		}

		try {
			if (null != so.getFcm()) {
				if (null != so.getFcm().getSubmittedDate()) {
					supplyOrderDto.setIsFcmSubmitted(true);
				} else {
					supplyOrderDto.setIsFcmSubmitted(false);
				}
			} else {
				supplyOrderDto.setIsFcmSubmitted(false);
			}
		} catch (Exception e) {
			logger.error("Error determining FCM submission status for Supply Order: {}", e.getMessage());
			supplyOrderDto.setIsFcmSubmitted(false);
		}

		return supplyOrderDto;
	}

	public List<SupplyOrderDto> getAllSupplyOrdersBySalesGrp(int empId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		try {
			List<SupplyOrder> supplyOrder = supplyOrderRepository.getAllSupplyOrdersBySalesGrp(empId);
			for (SupplyOrder so : supplyOrder) {
				SupplyOrderDto supplyOrderDto = mapper.map(so, SupplyOrderDto.class);

				supplyOrderDto = updateSubmitedDatesSts(so, supplyOrderDto);

				List<SupplyOrderAttachment> attachment = so.getAttachments();
				SupplyOrderAttachment atmt = new SupplyOrderAttachment();
				List<String> qapFiles = new ArrayList<>();
				List<String> drawingFiles = new ArrayList<>();
				List<String> atpFiles = new ArrayList<>();
				List<String> otherFiles = new ArrayList<>();
				List<String> amendments = new ArrayList<>();
				List<String> poFiles = new ArrayList<>();

				for (SupplyOrderAttachment soAttachment : attachment) {
					atmt.setCategory(soAttachment.getCategory());
					String category = soAttachment.getCategory();
					String fileName = soAttachment.getFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
							+ fileName;
					switch (category) {
					case "QAP":
						qapFiles.add(fileDownloadUri);
						break;
					case "Drawings":
						drawingFiles.add(fileDownloadUri);
						break;
					case "ATP":
						atpFiles.add(fileDownloadUri);
						break;
					case "Others":
						otherFiles.add(fileDownloadUri);
						break;
					case "Amendments":
						amendments.add(fileDownloadUri);
						break;
					case "PO":
						poFiles.add(fileDownloadUri);
						break;
					default:
						break;
					}
				}
				supplyOrderDto.setAtpFiles(atpFiles);
				supplyOrderDto.setAttachments(otherFiles);
				supplyOrderDto.setDrawings(drawingFiles);
				supplyOrderDto.setQapFiles(qapFiles);
				supplyOrderDto.setAmendments(amendments);
				supplyOrderDto.setPoFiles(poFiles);
				supplyOrderDtos.add(supplyOrderDto);
			}

		} catch (Exception e) {			
			logger.error("Error in  fetching supply orders {}", e.getMessage());

		}

		return supplyOrderDtos;

	}

	public List<SupplyOrderDto> getAllSupplyOrdersBySalesGrpId(int salesGrpId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		try {
			List<SupplyOrder> supplyOrder = supplyOrderRepository.getAllSupplyOrdersBySalesGrpId(salesGrpId);
			for (SupplyOrder so : supplyOrder) {
				SupplyOrderDto supplyOrderDto = mapper.map(so, SupplyOrderDto.class);

				supplyOrderDto = updateSubmitedDatesSts(so, supplyOrderDto);

				List<SupplyOrderAttachment> attachment = so.getAttachments();
				SupplyOrderAttachment atmt = new SupplyOrderAttachment();
				List<String> qapFiles = new ArrayList<>();
				List<String> drawingFiles = new ArrayList<>();
				List<String> atpFiles = new ArrayList<>();
				List<String> otherFiles = new ArrayList<>();
				List<String> amendments = new ArrayList<>();
				List<String> poFiles = new ArrayList<>();

				for (SupplyOrderAttachment soAttachment : attachment) {
					atmt.setCategory(soAttachment.getCategory());
					String category = soAttachment.getCategory();
					String fileName = soAttachment.getFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
							+ fileName;
					switch (category) {
					case "QAP":
						qapFiles.add(fileDownloadUri);
						break;
					case "Drawings":
						drawingFiles.add(fileDownloadUri);
						break;
					case "ATP":
						atpFiles.add(fileDownloadUri);
						break;
					case "Others":
						otherFiles.add(fileDownloadUri);
						break;
					case "Amendments":
						amendments.add(fileDownloadUri);
						break;
					case "PO":
						poFiles.add(fileDownloadUri);
						break;
					default:
						break;
					}
				}
				supplyOrderDto.setAtpFiles(atpFiles);
				supplyOrderDto.setAttachments(otherFiles);
				supplyOrderDto.setDrawings(drawingFiles);
				supplyOrderDto.setQapFiles(qapFiles);
				supplyOrderDto.setAmendments(amendments);
				supplyOrderDto.setPoFiles(poFiles);
				supplyOrderDtos.add(supplyOrderDto);
			}

		} catch (Exception e) {			
			logger.error("Error in  fetching supply orders {}", e.getMessage());

		}

		return supplyOrderDtos;

	}

	public List<SupplyOrderDto> getAllSupplyOrdersBySalesGrpOptimized(int empId, List<Integer> statusIds) {
		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		if(statusIds == null || statusIds.isEmpty()) {
//			statusIds = List.of(SO_STATUS_CONSTANTS.CREATED, SO_STATUS_CONSTANTS.NOTIFIED_TO_HOSA, SO_STATUS_CONSTANTS.NOTIFIED_TO_GICA);
			statusIds = List.of(SO_STATUS_CONSTANTS.CREATED, SO_STATUS_CONSTANTS.NOTIFIED);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<Object[]> list = supplyOrderRepository.getAllSupplyOrdersBySalesGrpOptimized(empId, statusIds);
			for (Object[] obj : list) {
				SupplyOrderDto dto = new SupplyOrderDto();
				int index = 0;
				dto.setId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setOrganizationId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setOrderNo(obj[index] != null ? String.valueOf(obj[index]) : null);
				List<String> inspAuth = supplyOrderRepository
						.getInspAuthBySoNumber(obj[index] != null ? String.valueOf(obj[index]) : null);
				dto.setInspectionAuthName(inspAuth.get(0) == null ? null : String.join(" & ", inspAuth));
				
				index++;
				dto.setOrderDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setSalesGroupId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setSalesGroupName(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				// dto.setProjectName(obj[6] != null ? String.valueOf(obj[6]) : null);
				dto.setFileNo(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				dto.setReceiptDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setCreatedDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setStatus(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				dto.setIsNotified(obj[index] != null ? (Boolean) obj[index] : null);
				index++;
				// dto.setDescription(obj[12] != null ? String.valueOf(obj[12]) : null);
				dto.setRemarks(obj[index] != null ? String.valueOf(obj[index]) : null);
				
//				dto.setDrawingNo(obj[15] != null ? obj[15].toString(): null);
//				dto.setReferenceNo(obj[16] != null ? obj[16].toString(): null);
				index++;
				dto.setOrganizationName(obj[index] != null ? obj[index].toString() : null);
				index++;
				dto.setSoStatusNo(obj[index] != null ? obj[index].toString() : null);
				index++;
				dto.setSoStatusId(obj[index]!= null ? (Integer)obj[index]: null);
				index++;
				dto.setIsActive(obj[index] != null ? (Boolean) obj[index] : null);
				supplyOrderDtos.add(dto);

			}

		} catch (Exception e) {
			logger.error("Error occured while fetching Supply Orders: ", e);
			logger.error("Error in  fetching supply orders {}", e.getMessage());
		}

		// return supplyOrderDtos;
		List<SupplyOrderDto> sortedSupplyOrderDtos = supplyOrderDtos.stream()
				.sorted(Comparator.comparing(SupplyOrderDto::getId).reversed()) // Sort by id descending
				.collect(Collectors.toList());

		return sortedSupplyOrderDtos;
	}

	public List<SupplyOrderDto> getAllSupplyOrdersBySalesGrpOptimizedByEmpId(int empId) {
		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<Object[]> list = supplyOrderRepository.getAllSupplyOrdersBySalesGrpOptimizedByEmpId(empId);
			for (Object[] obj : list) {
				SupplyOrderDto dto = new SupplyOrderDto();
				int index = 0;
				dto.setId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setOrganizationId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setOrderNo(obj[index] != null ? String.valueOf(obj[index]) : null);
				List<String> inspAuth = supplyOrderRepository
						.getInspAuthBySoNumber(obj[index] != null ? String.valueOf(obj[index]) : null);
				dto.setInspectionAuthName(inspAuth.get(0) == null ? null : String.join(" & ", inspAuth));
				index++;
				dto.setOrderDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setSalesGroupId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
				index++;
				dto.setSalesGroupName(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				// dto.setProjectName(obj[6] != null ? String.valueOf(obj[6]) : null);
				dto.setFileNo(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				dto.setReceiptDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setCreatedDate(obj[index] != null ? sdf.parse(obj[index].toString()) : null);
				index++;
				dto.setStatus(obj[index] != null ? String.valueOf(obj[index]) : null);
				index++;
				dto.setIsNotified(obj[index] != null ? (Boolean) obj[index] : null);
				index++;
				// dto.setDescription(obj[12] != null ? String.valueOf(obj[12]) : null);
				dto.setRemarks(obj[index] != null ? String.valueOf(obj[index]) : null);
				// dto.setIsGcc(obj[14] != null ? (Boolean) obj[14] : null);
//				dto.setDrawingNo(obj[15] != null ? obj[15].toString(): null);
//				dto.setReferenceNo(obj[16] != null ? obj[16].toString(): null);
				index++;
				dto.setOrganizationName(obj[index] != null ? obj[index].toString() : null);
				index++;
				dto.setSoStatusNo(obj[index] != null ? obj[index].toString() : null);
//				dto.setPartNo(obj[18] !=null ? obj[18].toString(): null);				
				supplyOrderDtos.add(dto);

			}

		} catch (Exception e) {
			logger.error("Error in  fetching supply orders {}", e.getMessage());
		}

		// return supplyOrderDtos;
		List<SupplyOrderDto> sortedSupplyOrderDtos = supplyOrderDtos.stream()
				.sorted(Comparator.comparing(SupplyOrderDto::getId).reversed()) // Sort by id descending
				.collect(Collectors.toList());

		return sortedSupplyOrderDtos;
	}

	public SupplyOrderDto updateSoAttachments(SupplyOrderDto supplyOrderDto, MultipartFile[] qapFiles,
			MultipartFile[] drawings, MultipartFile[] atpFiles, MultipartFile[] others, MultipartFile[] amendments,
			MultipartFile[] po, MultipartFile[] itemLots, MultipartFile[] dd) {

		try {
			Optional<SupplyOrder> soOptional = supplyOrderRepository.findById(supplyOrderDto.getId());

			if (soOptional.isPresent()) {
				SupplyOrder supplyOrder = soOptional.get();
				getSavedSoAttachments(supplyOrderDto, supplyOrder);
				supplyOrder.setRemarks(supplyOrderDto.getRemarks());
				// supplyOrder.setDescription(supplyOrderDto.getDescription());

				// supplyOrder.setOrderNo(supplyOrderDto.getOrderNo());
				// supplyOrder.setOrderDate(supplyOrderDto.getOrderDate());
				// supplyOrder.setReceiptDate(supplyOrderDto.getReceiptDate());
				// supplyOrder.setIsGcc(supplyOrderDto.getIsGcc());
				// supplyOrder.setStatus(supplyOrderDto.getStatus());
				// supplyOrder.setDrawingNo(supplyOrderDto.getDrawingNo());
				// supplyOrder.setReferenceNo(supplyOrderDto.getReferenceNo());
				// Inspection agency updation
//				Optional<Organization> organizationOptional = organizationRepository.findById(supplyOrderDto.getOrganizationId());
//
//				if (organizationOptional.isPresent()) {
//				    supplyOrder.setOrganization(organizationOptional.get());
//				} else {
//				  
//				    throw new FileUploadException("Organization not found with ID: " + supplyOrderDto.getOrganizationId());
//				}
				// mapper.map(supplyOrder, supplyOrderDto);
				List<SupplyOrderAttachment> soAttachments = new ArrayList<>();
				if (qapFiles != null && qapFiles.length > 0) {
					saveSoAttachment(qapFiles, "QAP", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (drawings != null && drawings.length > 0) {
					saveSoAttachment(drawings, "Drawings", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (atpFiles != null && atpFiles.length > 0) {
					saveSoAttachment(atpFiles, "ATP", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (others != null && others.length > 0) {
					saveSoAttachment(others, "Others", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (amendments != null && amendments.length > 0) {
					saveSoAttachment(amendments, "Amendments", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (po != null && po.length > 0) {
					saveSoAttachment(po, "PO", supplyOrder, supplyOrderDto, soAttachments);
				}
				if (itemLots != null && itemLots.length > 0) {
					saveSoAttachment(itemLots, ITEM_LOTS, supplyOrder, supplyOrderDto, soAttachments);
				}
				if (dd != null && dd.length > 0) {
					saveSoAttachment(dd, "DDExtensions", supplyOrder, supplyOrderDto, soAttachments);
				}
				supplyOrder.setAttachments(soAttachments);
				supplyOrderRepository.save(supplyOrder);
				;
			}
		} catch (Exception e) {
			logger.error("Error occured while updating supply order attachments: ", e);
			throw new FileUploadException("Error in  updating supply order attachments{}" + e.getMessage());
			// logger.error("Error in updating supply order attachments{}", e.getMessage());
		}
		return supplyOrderDto;
	}

	public SupplyOrderDto getSavedSoAttachments(SupplyOrderDto supplyOrderDto, SupplyOrder supplyOrder) {

		List<SupplyOrderAttachment> supplyOrderAttachments = supplyOrder.getAttachments();

		List<SupplyOrderAttachment> qapAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("QAP")).toList();
		List<String> qapFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : qapAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			qapFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setQapFiles(qapFileUrls);

		List<SupplyOrderAttachment> drawingAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("Drawings")).toList();
		List<String> drawingFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : drawingAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			drawingFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setDrawings(drawingFileUrls);

		List<SupplyOrderAttachment> atpAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("ATP")).toList();
		List<String> atpFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : atpAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			atpFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setAtpFiles(atpFileUrls);

		List<SupplyOrderAttachment> otherAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("Others")).toList();
		List<String> otherFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : otherAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			otherFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setAttachments(otherFileUrls);

		List<SupplyOrderAttachment> amendmentAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("Amendments")).toList();
		List<String> amendmentFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : amendmentAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			amendmentFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setAmendments(amendmentFileUrls);

		List<SupplyOrderAttachment> poAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("PO")).toList();
		List<String> poFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : poAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			poFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setPoFiles(poFileUrls);

		List<SupplyOrderAttachment> itemLots = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals(ITEM_LOTS)).toList();
		List<String> itemLotsUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : itemLots) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			itemLotsUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setItemLotFiles(itemLotsUrls);

		List<SupplyOrderAttachment> ddAttachments = supplyOrderAttachments.stream()
				.filter(s -> s.getCategory().equals("DDExtension")).toList();
		List<String> ddFileUrls = new ArrayList<>();
		for (SupplyOrderAttachment soAttachment : ddAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
					+ soAttachment.getFileName();
			ddFileUrls.add(fileDownloadUri);
		}
		supplyOrderDto.setDdFiles(ddFileUrls);

		return supplyOrderDto;

	}

	private void saveSoAttachment(MultipartFile[] files, String category, SupplyOrder supplyOrder,
			SupplyOrderDto supplyOrderDto, List<SupplyOrderAttachment> soAttachments) {

		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		List<String> uploadedFiles = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (FileValidator.checkFile(files[i])) {
				String fileName = fileStorageService.storeFile(files[i], supplyOrder.getId(), i);
				if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
					SupplyOrderAttachment attachment = new SupplyOrderAttachment();
					attachment.setCategory(category);
					attachment.setCreatedDate(date);
					attachment.setFileName(fileName);
					attachment.setSupplyOrder(supplyOrder);
					soAttachments.add(attachment);
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT
							+ fileName;
					uploadedFilesUrl.add(fileDownloadUri);
					uploadedFiles.add(fileName);

				}

				switch (category) {

				case "QAP":
					List<String> qapFiles = supplyOrderDto.getQapFiles();
					qapFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setQapFiles(qapFiles);
					break;

				case "Drawings":
					List<String> drawingFiles = supplyOrderDto.getDrawings();
					drawingFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setDrawings(drawingFiles);
					break;

				case "ATP":
					List<String> atpFiles = supplyOrderDto.getAtpFiles();
					atpFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setAtpFiles(atpFiles);
					break;

				case "Others":
					List<String> otherFiles = supplyOrderDto.getAttachments();
					otherFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setAttachments(otherFiles);
					break;

				case "Amendments":
					List<String> amendmentFiles = supplyOrderDto.getAmendments();
					amendmentFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setAmendments(amendmentFiles);
					break;

				case "PO":
					List<String> poFiles = supplyOrderDto.getPoFiles();
					poFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setPoFiles(poFiles);
					break;
				case "ItemLots":
					List<String> itemLotFiles = supplyOrderDto.getItemLotFiles();
					itemLotFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setItemLotFiles(itemLotFiles);
					break;
				case "DDExtension":
					List<String> ddFiles = supplyOrderDto.getDdFiles();
					ddFiles.addAll(uploadedFilesUrl);
					supplyOrderDto.setDdFiles(ddFiles);
					break;
				default:
					break;
				}
			} else {

				throw new FileUploadException("   Invalid file detected.");
			}
		}
	}

	class FileUploadException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public FileUploadException(String message) {
			super(message);
		}
	}

	// Returns the list of supply orders alloted to the input employee id
	public List<SupplyOrderDto> getAllotedSupplyOrders(int empId) {

		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		List<Object[]> response = soAllotmentRepository.getAllotmentDetailsByEmpId(empId);

		// [0]: supply_order_id; [1]: order_no; [2]: order_date; [3]: alloted_to; [4]:
		// alloted_to_id; [5]: alloted_by
		for (Object[] item : response) {
			SupplyOrderDto dto = new SupplyOrderDto();
			dto.setId(Integer.parseInt(item[0].toString()));
			dto.setOrderNo(item[1].toString());
			dto.setOrderDate((Date) item[2]);
			supplyOrderDtos.add(dto);
		}

		List<SupplyOrder> allotedSos = supplyOrderRepository.getAllotedSupplyOrders(empId);
		for (SupplyOrder supplyOrder : allotedSos) {
			SupplyOrderDto dto = new SupplyOrderDto();
			dto.setId(supplyOrder.getId());
			dto.setOrderNo(supplyOrder.getOrderNo());
			dto.setOrderDate(supplyOrder.getOrderDate());
			supplyOrderDtos.add(dto);
		}

		List<SupplyOrderDto> uniqueSupplyOrderDtos = supplyOrderDtos.stream().distinct().toList();

		for (SupplyOrderDto supplyOrderDto : uniqueSupplyOrderDtos) {
			List<Integer> poObservationIds = poObservationRepository.getPoObsIdsBySoId(supplyOrderDto.getId());
			supplyOrderDto.setPoObservatioIds(poObservationIds);
		}
		List<SupplyOrderDto> result = uniqueSupplyOrderDtos;
		result.sort(Comparator.comparing(SupplyOrderDto::getId).reversed());
		return uniqueSupplyOrderDtos;

	}

	public List<SoTreeViewDto> getAllSOBySalesGrpIdOrSoID(int supplyOrderId, int salesGrpId) {

		List<SoTreeViewDto> soTreeViewDtos = new ArrayList<>();
		List<SupplyOrder> supplyOrders = null;
		if (supplyOrderId != 0) {
			Optional<SupplyOrder> supplyOrderOptional = supplyOrderRepository.findById(supplyOrderId);
			if (supplyOrderOptional.isPresent()) {
				SupplyOrder supplyOrder = supplyOrderOptional.get();
				supplyOrders = Arrays.asList(supplyOrder);
			}
		} else {
			supplyOrders = supplyOrderRepository.getAllSupplyOrdersBySalesGrp(salesGrpId);
		}

		for (SupplyOrder supplyOrder : supplyOrders) {
			SoTreeViewDto rootDto = new SoTreeViewDto();
			rootDto.setName(supplyOrder.getOrderNo());
			Map<String, Object> rootAttrMap = new HashMap<>();
			List<Project> projects = supplyOrder.getProjects();
			for (Project project : projects) {
				if (project != null) {
					rootAttrMap.put(PROJECT_NAME, project.getProjectName());
					List<SoTreeViewDto> shipDtos = new ArrayList<>();
					List<Ship> ships = project.getShips();
					if (!ships.isEmpty()) {

						for (Ship ship : ships) {
							SoTreeViewDto shipDto = new SoTreeViewDto();
							shipDto.setName(ship.getShipName());

							List<SoTreeViewDto> categoryDtos = new ArrayList<>();
							List<Category> categories = ship.getCategories();
							if (!categories.isEmpty()) {
								for (Category category : categories) {
									SoTreeViewDto categoryDto = new SoTreeViewDto();
									categoryDto.setName(category.getCategoryName());
									List<SaleOrder> saleOrders = category.getSaleOrders();
									List<SoTreeViewDto> saleOrderDtos = new ArrayList<>();
									if (!saleOrders.isEmpty()) {
										for (SaleOrder saleOrder : saleOrders) {
											SoTreeViewDto saleOrderDto = new SoTreeViewDto();
											saleOrderDto.setId(saleOrder.getId());
											saleOrderDto.setName(saleOrder.getSaleOrderNo());
											saleOrderDtos.add(saleOrderDto);
										}
									} else {
										SoTreeViewDto saleOrderDto = new SoTreeViewDto();
										saleOrderDto.setName("NA");
										saleOrderDtos.add(saleOrderDto);
									}
									categoryDto.setChildren(saleOrderDtos);
									categoryDtos.add(categoryDto);
								}
							} else {
								SoTreeViewDto categoryDto = new SoTreeViewDto();
								categoryDto.setName("NA");
								categoryDtos.add(categoryDto);
							}
							shipDto.setChildren(categoryDtos);
							shipDtos.add(shipDto);
						}
					} else {
						SoTreeViewDto shipDto = new SoTreeViewDto();
						shipDto.setName("NA");
						shipDtos.add(shipDto);
					}
					rootDto.setChildren(shipDtos);
					soTreeViewDtos.add(rootDto);
				} else {
					rootAttrMap.put(PROJECT_NAME, "");
				}
			}
			rootDto.setAttributes(rootAttrMap);
		}
		return soTreeViewDtos;
	}

	// Returns the list of supply orders alloted to the input employee id
	public List<SoTreeViewDto> getOverallTreeView(int supplyOrderId) {

		List<SoTreeViewDto> soTreeViewDtos = new ArrayList<>();
		Optional<SupplyOrder> soOptional = supplyOrderRepository.findById(supplyOrderId);
		if (soOptional.isPresent()) {
			SupplyOrder supplyOrder = soOptional.get();
			SoTreeViewDto rootDto = new SoTreeViewDto();
			rootDto.setName(supplyOrder.getOrderNo());
			Map<String, Object> rootAttrMap = new HashMap<>();
			if (supplyOrder.getDd() != null) {
				rootAttrMap.put("DD", dateFormat.format(supplyOrder.getDd()));
			}
			List<Project> projects = supplyOrder.getProjects();
			for (Project project : projects) {
				if (project != null) {
					rootAttrMap.put(PROJECT_NAME, project.getProjectName());

					List<SoTreeViewDto> shipDtos = new ArrayList<>();
					List<Ship> ships = project.getShips();
					for (Ship ship : ships) {
						SoTreeViewDto shipDto = new SoTreeViewDto();
						shipDto.setName(ship.getShipName());

						List<SoTreeViewDto> categoryDtos = new ArrayList<>();
						List<Category> categories = ship.getCategories();
						for (Category category : categories) {
							SoTreeViewDto categoryDto = new SoTreeViewDto();
							categoryDto.setName(category.getCategoryName());
							List<SaleOrder> saleOrders = category.getSaleOrders();
							List<SoTreeViewDto> saleOrderDtos = new ArrayList<>();
							for (SaleOrder saleOrder : saleOrders) {
								SoTreeViewDto saleOrderDto = new SoTreeViewDto();
								saleOrderDto.setId(saleOrder.getId());
								saleOrderDto.setName(saleOrder.getSaleOrderNo());
								saleOrderDtos.add(saleOrderDto);
							}
							categoryDto.setChildren(saleOrderDtos);
							categoryDtos.add(categoryDto);
						}
						shipDto.setChildren(categoryDtos);
						shipDtos.add(shipDto);
					}
					Collections.sort(shipDtos, new Comparator<SoTreeViewDto>() {

						@Override
						public int compare(SoTreeViewDto o1, SoTreeViewDto o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});
					rootDto.setChildren(shipDtos);
				}
			}
			rootDto.setAttributes(rootAttrMap);
			soTreeViewDtos.add(rootDto);
		}
		return soTreeViewDtos;
	}

	public List<ConsigneeDto> getConsigneesBySoId(int supplyOrderId) {

		List<ConsigneeDto> consigneeDtos = new ArrayList<>();

		List<Object[]> consignees = supplyOrderRepository.getConsigneesBySoId(supplyOrderId);
		for (Object[] consigneeObj : consignees) {
			ConsigneeDto consigneeDto = new ConsigneeDto();
			if (consigneeObj[0] != null) {
				consigneeDto.setId((int) consigneeObj[0]);
				consigneeDto.setName(consigneeObj[1].toString());
				consigneeDtos.add(consigneeDto);
			}
		}
		return consigneeDtos;

	}

	public List<ChartValuePair> getPoDetailsForChart(int orgId, int consigneeId, int indentorId, int salesGrpId)
			throws Exception {

		List<ChartValuePair> chartValuePairs = new LinkedList<>();
		// Getting current financial year end date
		LocalDate now = LocalDate.now();
		int year = now.getYear();
		LocalDate end;
		if (now.getMonthValue() > Month.MARCH.getValue()) {
			// current year financial year has ended, so get the end date of the upcoming
			// financial year
			year++;
			end = LocalDate.of(year, Month.MARCH, 31);
		} else {
			// current year financial year has not ended yet, so get the end date of the
			// current financial year
			end = LocalDate.of(year, Month.MARCH, 31);
		}

		List<Object[]> objectArrayList = null;
		if (consigneeId == -1) {
			objectArrayList = supplyOrderRepository.getPoDetailsForChart(orgId, end, indentorId, salesGrpId);
		} else {
			objectArrayList = supplyOrderRepository.getPoDetailsForChartConsigneeWise(orgId, end, indentorId,
					consigneeId, salesGrpId);
		}

		if (!objectArrayList.isEmpty()) {

			Object[] objectArray = objectArrayList.get(0);

			ChartValuePair validDDData = new ChartValuePair();
			validDDData.setLabel("Valid DD");
			validDDData.setValue(new BigDecimal(objectArray[0].toString()).intValue());

			ChartValuePair expdLessThanOne = new ChartValuePair();
			expdLessThanOne.setLabel("Expd. < 1 Yr");
			expdLessThanOne.setValue(new BigDecimal(objectArray[1].toString()).intValue());

			ChartValuePair expdMoreThanOne = new ChartValuePair();
			expdMoreThanOne.setLabel("Expd. > 1 Yr");
			expdMoreThanOne.setValue(new BigDecimal(objectArray[2].toString()).intValue());

			ChartValuePair expdMoreThanThree = new ChartValuePair();
			expdMoreThanThree.setLabel("Expd. > 3 Yr");
			expdMoreThanThree.setValue(new BigDecimal(objectArray[3].toString()).intValue());

			ChartValuePair expdMoreThanFive = new ChartValuePair();
			expdMoreThanFive.setLabel("Expd. > 5 Yr");
			expdMoreThanFive.setValue(new BigDecimal(objectArray[4].toString()).intValue());

			ChartValuePair completedOnDate = new ChartValuePair();
			completedOnDate.setLabel("Completed");
			completedOnDate.setValue(new BigDecimal(objectArray[5].toString()).intValue());

			ChartValuePair expThreeMonth = new ChartValuePair();
			expThreeMonth.setLabel("Exp. 3 Mon");
			expThreeMonth.setValue(new BigDecimal(objectArray[7].toString()).intValue());

			ChartValuePair expSixMonth = new ChartValuePair();
			expSixMonth.setLabel("Exp. 6 Mon");
			expSixMonth.setValue(new BigDecimal(objectArray[8].toString()).intValue());

			ChartValuePair expTwelveMonth = new ChartValuePair();
			expTwelveMonth.setLabel("Exp. 12 Mon");
			expTwelveMonth.setValue(new BigDecimal(objectArray[9].toString()).intValue());

			ChartValuePair expCurrentFY = new ChartValuePair();
			expCurrentFY.setLabel("Exp. Curr FY");
			expCurrentFY.setValue(new BigDecimal(objectArray[6].toString()).intValue());

			chartValuePairs.add(validDDData);
			chartValuePairs.add(expdLessThanOne);
			chartValuePairs.add(expdMoreThanOne);
			chartValuePairs.add(expdMoreThanThree);
			chartValuePairs.add(expdMoreThanFive);
			chartValuePairs.add(completedOnDate);
			chartValuePairs.add(expThreeMonth);
			chartValuePairs.add(expSixMonth);
			chartValuePairs.add(expTwelveMonth);
			chartValuePairs.add(expCurrentFY);

		}
		return chartValuePairs;
	}

	@PostMapping("/deleteSoAttachment")
	public String deleteSoAttachment(int attachmentId, String fileName) {

		String returnString = ApplicationConstants.FAILURE;
		Optional<SupplyOrderAttachment> attachmentOpt = soAttachmentRepository.findById(attachmentId);
		if (attachmentOpt.isPresent()) {
			SupplyOrderAttachment soAttachment = attachmentOpt.get();

			String fileDeletionStatus = fileStorageService.deleteFile(soAttachment.getFileName());

			if (ApplicationConstants.SUCCESS.equals(fileDeletionStatus)) {
				soAttachmentRepository.delete(soAttachment);
				returnString = ApplicationConstants.SUCCESS;
			}
		}
		return returnString;
	}

	public SupplyOrder updateSoNotifyStatus(SupplyOrder so, Integer roleId) {
		SoStatus st = new SoStatus();
		if (so.getIsNotified() == null || !so.getIsNotified())
			so.setIsNotified(true);                           

		if (roleId == Role.PLANNER || roleId == Role.HOD)
			st = soStatusRepository.findById(SO_STATUS_CONSTANTS.NOTIFIED).orElse(null);

//		if (roleId == Role.HOD)
//			st = soStatusRepository.findById(SO_STATUS_CONSTANTS.NOTIFIED_TO_GICA).orElse(null);

//		}
		so.setSoStatus(st);
		return supplyOrderRepository.save(so);
	}

	// Helper to prevent NullPointerException when adding to lists
		private List<SoAttachmentDetailDto> addToList(List<SoAttachmentDetailDto> list, SoAttachmentDetailDto item) {
		    if (list == null) list = new ArrayList<>();
		    list.add(item);
		    return list;
		}

	public SoAttachmentDto getAttachmentsBySoId(int soId) {
		SoAttachmentDto soAttachmentDto = new SoAttachmentDto();
		List<Object[]> response = soAttachmentRepository.getBySoId(soId);
		
		for (Object[] obj : response) {
			String category = obj[0].toString();
			Integer id = ((Number) obj[1]).intValue();
			String fileName = obj[2].toString();
			
	        // Create the URL
			String fileUrl = domainUrl + applicationName + ApplicationConstants.SEPERATOR + ApplicationConstants.FILES_PATH_SEGMENT + fileName;
			
			// Wrap it in the SoAttachmentDetailDto
	        SoAttachmentDetailDto detail = new SoAttachmentDetailDto(id, fileName, fileUrl);
	        
			switch (obj[0].toString()) {
			case "QAP":
				soAttachmentDto.setQapFiles(addToList(soAttachmentDto.getQapFiles(), detail));
				break;
			case "Drawings":
				soAttachmentDto.setDrawings(addToList(soAttachmentDto.getDrawings(), detail));
				break;
			case "ATP":
				soAttachmentDto.setAtpFiles(addToList(soAttachmentDto.getAtpFiles(), detail));
				break;
			case "Others":
				soAttachmentDto.setAttachments(addToList(soAttachmentDto.getAttachments(), detail));
				break;
			case "Amendments":
				soAttachmentDto.setAmendments(addToList(soAttachmentDto.getAmendments(), detail));
				break;
			case "PO":
				soAttachmentDto.setPoFiles(addToList(soAttachmentDto.getPoFiles(), detail));
				break;
			case "ItemLots":
				soAttachmentDto.setItemLotFiles(addToList(soAttachmentDto.getItemLotFiles(), detail));
			case "DDExtension":
				soAttachmentDto.setDdFiles(addToList(soAttachmentDto.getDdFiles(), detail));
			default:
				break;
			}
		}
		return soAttachmentDto;
	}

	// used in surprise check register
	public List<Object[]> getAllFileNos(int orgId) {

		return supplyOrderRepository.getNotifiedFileNos(orgId);

	}

	// used in surprise check register
	public List<Object[]> getSODetailsForSurpriseCheck(int supplyOrderId) {

		return supplyOrderRepository.getSODetailsForSurpriseCheck(supplyOrderId);

	}

	public List<ChartValuePair> getDGQAPoDetailsForChart() throws Exception {

		List<ChartValuePair> chartValuePairs = new LinkedList<>();

		List<Object[]> objectArrayList = null;
		objectArrayList = supplyOrderRepository.getPoDetailsForDGQAChart();

		for (Object[] objectArray : objectArrayList) {

			ChartValuePair soData = new ChartValuePair();
			soData.setLabel(objectArray[0].toString());
			soData.setValue(new BigDecimal(objectArray[1].toString()).intValue());
			chartValuePairs.add(soData);

		}
		return chartValuePairs;
	}

	public List<Boolean> checkUniqueIndent(SupplyOrderDto supplyOrderDto) {
		List<String> list = supplyOrderDto.getIndentSlNoList();
		List<Boolean> booleans = new ArrayList<>();
		for (String indentNo : list) {
			boolean b = supplyOrderRepository.indentExists(supplyOrderDto.getId(), indentNo);
			booleans.add(b);
		}
		return booleans;
	}

	public List<SupplyOrderExpiryDTO> getExpiringOrdersByEmpId(int empId) {
		List<Object[]> rawResults = supplyOrderRepository.findExpiringOrdersByEmpId(empId);

		return rawResults.stream().map(row -> {
			Long id = ((Number) row[0]).longValue();
			String orderNo = (String) row[1];
			Date originalDd = (Date) row[2];
			Date latestNewDd = (Date) row[3];
			Date finalDd = (Date) row[4];

			// Calculate daysRemaining
			long daysRemaining = 0;
			if (finalDd != null) {
				long diffInMillis = finalDd.getTime() - System.currentTimeMillis();
				daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
			}

			return new SupplyOrderExpiryDTO(id, orderNo, originalDd, latestNewDd, finalDd, daysRemaining);
		}).toList();
	}

	public int getExpiringOrderCountByEmpId(int empId) {
		return supplyOrderRepository.countExpiringOrdersByEmpId(empId);
	}

	public List<SupplyOrderExpiryDTO> getExpiringOrdersByOrgId(int orgId) {
		List<Object[]> rawResults = supplyOrderRepository.findExpiringOrdersByOrgId(orgId);

		return rawResults.stream().map(row -> {
			Long id = ((Number) row[0]).longValue();
			String orderNo = (String) row[1];
			Date originalDd = (Date) row[2];
			Date latestNewDd = (Date) row[3];
			Date finalDd = (Date) row[4];
			String salesGrpName = (String) row[5];

			long daysRemaining = 0;
			if (finalDd != null) {
				long diffInMillis = finalDd.getTime() - System.currentTimeMillis();
				daysRemaining = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
			}

			return new SupplyOrderExpiryDTO(id, orderNo, originalDd, latestNewDd, finalDd, daysRemaining, salesGrpName);
		}).toList();
	}

	public int countExpiringOrdersByOrgId(int orgId) {
		return supplyOrderRepository.countExpiringOrdersByOrgId(orgId);
	}

	public List<SupplyOrderDto> getApprovedSoBySalesGrp(int salesGrpId) {
		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<Object[]> supplyOrder = supplyOrderRepository.getApprovedSoBySalesGrp(salesGrpId);
			for (Object[] obj : supplyOrder) {
				SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
				supplyOrderDto.setId(Integer.parseInt(obj[0].toString()));
				supplyOrderDto.setOrderNo(obj[1].toString());
				supplyOrderDto.setOrderDate(obj[2] != null ? sdf.parse(obj[2].toString()) : null);
				supplyOrderDto.setReceiptDate(obj[3] != null ? sdf.parse(obj[3].toString()) : null);
				supplyOrderDtos.add(supplyOrderDto);
			}
			return supplyOrderDtos;
		} catch (Exception e) {
			logger.error("Error occured while fetching supply orders ", e);
			logger.error("Error in  fetching supply orders {}", e.getMessage());
		}
		return supplyOrderDtos;
	}

	public List<SupplyOrderDto> getAllotedSupplyOrdersBySalesGrpId(int empId, int salesGrpId , List<Integer> statusIds) {
        if(statusIds == null || statusIds.isEmpty()) {
//        	statusIds= Arrays.asList(SO_STATUS_CONSTANTS.CREATED, SO_STATUS_CONSTANTS.NOTIFIED_TO_HOSA, SO_STATUS_CONSTANTS.NOTIFIED_TO_GICA);
        	statusIds= Arrays.asList(SO_STATUS_CONSTANTS.CREATED, SO_STATUS_CONSTANTS.NOTIFIED );
        }
		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();

		List<Object[]> response = soAllotmentRepository.getAllotmentDetailsByEmpIdAndSalesGrp(empId, salesGrpId, statusIds);

		for (Object[] item : response) {
			SupplyOrderDto dto = new SupplyOrderDto();
			dto.setId(Integer.parseInt(item[0].toString()));
			dto.setOrderNo(item[1].toString());
			List<String> inspAuth = supplyOrderRepository.getInspAuthBySoNumber(item[1].toString());
			dto.setInspectionAuthName(inspAuth.get(0) == null ? null : String.join(" & ", inspAuth));
			dto.setOrderDate((Date) item[2]);
			dto.setReceiptDate((Date) item[3]);
			dto.setFileNo(item[4].toString());
//			if (empId == (item[7] != null ? Integer.parseInt(item[7].toString()) : null))
//				dto.setSoStatusNo(ApplicationConstants.RECIEVED);
//			if (empId == (item[8] != null ? Integer.parseInt(item[8].toString()) : null))
//				dto.setSoStatusNo(ApplicationConstants.NOTIFIED);
			dto.setOrganizationName(item[9] != null ? item[9].toString() : null);
			dto.setIsNotified(item[10] != null ? (boolean) item[10] : null);
			String soStaus = supplyOrderRepository.getSupplyOrderStatus(dto.getId());
			dto.setStatus(soStaus);
			dto.setSoStatusNo(item[11]!= null ? (String)item[11]: null);
			dto.setSoStatusId(item[12]!= null ? (Integer)item[12]: null);
//	        soStaus = supplyOrderRepository.getSOStatus(dto.getId());
//	        dto.setSoStatusNo(soStaus);
			dto.setPartNo(item[4].toString());
			supplyOrderDtos.add(dto);
		}

//	    List<SupplyOrder> allotedSos = supplyOrderRepository.getAllotedSupplyOrders(empId);
//	    for (SupplyOrder supplyOrder : allotedSos) {
//	        SupplyOrderDto dto = new SupplyOrderDto();
//	        dto.setId(supplyOrder.getId());
//	        dto.setOrderNo(supplyOrder.getOrderNo());
//	        dto.setOrderDate(supplyOrder.getOrderDate());
//	       if(StringUtils.isNotEmpty(supplyOrder.getStatus())){
//	    	   dto.setStatus(supplyOrder.getStatus());
//	       }
//	        supplyOrderDtos.add(dto);
//	    }

		List<SupplyOrderDto> uniqueSupplyOrderDtos = supplyOrderDtos.stream().distinct().toList();

		for (SupplyOrderDto supplyOrderDto : uniqueSupplyOrderDtos) {
			List<Integer> poObservationIds = poObservationRepository.getPoObsIdsBySoId(supplyOrderDto.getId());
			supplyOrderDto.setPoObservatioIds(poObservationIds);
		}

		return uniqueSupplyOrderDtos;

	}

	public List<SupplyOrderDto> getAllTargetAcceptedQuantityofSo() {
		List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();

		List<Object[]> response = supplyOrderRepository.getAllTargetAcceptedQuantityofSo();

		for (Object[] item : response) {
			SupplyOrderDto dto = new SupplyOrderDto();
			dto.setOrderNo(item[0] != null ? item[0].toString() : null);
			dto.setOrderDate((Date) item[1] != null ? (Date) item[1] : null);
			dto.setDeliveryDate(item[2] != null ? item[2].toString() : null);
			dto.setTotalQty(item[3] != null ? Integer.parseInt(item[3].toString()) : null);
			dto.setOfferedQty(item[4] != null ? Integer.parseInt(item[4].toString()) : null);
			dto.setLotNo(item[5] != null ? Integer.parseInt(item[5].toString()) : null);
			dto.setAcceptedQty(item[6] != null ? Integer.parseInt(item[6].toString()) : null);
			supplyOrderDtos.add(dto);
		}
		return supplyOrderDtos;
	}

//	private void prepareChartObj(List<Object[]> resultSet, List<SOChartValuePair> soChartValuePairs) {
//		
//		if (resultSet != null && !resultSet.isEmpty()) {
//	
//			for (Object[] inoteObjArr : resultSet) {
//	
//				SOChartValuePair soChartValuePair = new SOChartValuePair();
//				soChartValuePair.setKey(inoteObjArr[0].toString());
//				soChartValuePair.setOrder(new BigDecimal(inoteObjArr[1].toString()).intValue());
//				soChartValuePair.setApproved(new BigDecimal(inoteObjArr[2].toString()).intValue());
//	
//				soChartValuePairs.add(soChartValuePair);
//			}
//		}
//	}
//public List<SOChartValuePair> getSupplyOrderDetailsForChart(Integer selectedFY, Integer term) {
//		
//		List<SOChartValuePair> soChartValuePairs = new LinkedList<>();
//		LocalDate fyStartDate = LocalDate.of(selectedFY - 1, 4, 1);
//		LocalDate fyEndDate = LocalDate.of(selectedFY, 3, 31);
//		
//		List<Object[]> soDetails = null;
//		if(term == 1) { // Yearly
//			soDetails = supplyOrderRepository.getYearWiseSO(fyStartDate, fyEndDate);
//			if (soDetails != null && !soDetails.isEmpty()) {
//
//				for (Object[] soObjArr : soDetails) {
//					SOChartValuePair soChartValuePair = new SOChartValuePair();
//					soChartValuePair.setKey("FY "+selectedFY);
//					soChartValuePair.setOrder(new BigDecimal(soObjArr[0].toString()).intValue());
//					soChartValuePair.setApproved(new BigDecimal(soObjArr[1].toString()).intValue());
//					soChartValuePairs.add(soChartValuePair);
//				}
//			}
//		}
//		else if(term == 2) {// Monthly for selected Financial year
//			
//			soDetails = supplyOrderRepository.getMonthWiseSO(fyStartDate, fyEndDate);
//			prepareChartObj(soDetails, soChartValuePairs);
//			List<String> availableMonthsInResult = soChartValuePairs.stream()
//					.map(soChartValuePair -> soChartValuePair.getKey()).collect(Collectors.toList());
//
//			List<String> sortedMonthsList = Arrays.asList("APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
//					"JAN", "FEB", "MAR");
//
//			for (String monthName : sortedMonthsList) {
//				if (!availableMonthsInResult.contains(monthName)) {
//					SOChartValuePair soChartValuePair = new SOChartValuePair();
//					soChartValuePair.setKey(monthName);
//					soChartValuePair.setOrder(0);
//					soChartValuePair.setApproved(0);
//					soChartValuePairs.add(soChartValuePair);
//				}
//			}
//			sortFYMonthWise(soChartValuePairs, sortedMonthsList);
//		}
//		else if(term == 3){ // Quarterly for selected Financial year
//			
//			soDetails = supplyOrderRepository.getQtrWiseSO(fyStartDate, fyEndDate);
//			prepareChartObj(soDetails, soChartValuePairs);
//			List<String> availableQtrsInResult = soChartValuePairs.stream()
//					.map(soChartValuePair -> soChartValuePair.getKey()).collect(Collectors.toList());
//
//			List<String> sortedQtrsList = Arrays.asList("Qtr 1", "Qtr 2", "Qtr 3", "Qtr 4");
//
//			for (String qtrName : sortedQtrsList) {
//				if (!availableQtrsInResult.contains(qtrName)) {
//					SOChartValuePair soChartValuePair = new SOChartValuePair();
//					soChartValuePair.setKey(qtrName);
//					soChartValuePair.setOrder(0);
//					soChartValuePair.setApproved(0);
//					soChartValuePairs.add(soChartValuePair);
//				}
//			}
//			soChartValuePairs.sort((o1, o2) -> o1.getKey().compareTo(o2.getKey()));
//			
//		}
//		
//		return soChartValuePairs;
//	}
//private void sortFYMonthWise(List<SOChartValuePair> objectsToOrder, List<String> orderedObjects) {
//	
//	HashMap<String, Integer> indexMap = new HashMap<>();
//	int index = 0;
//	for (String month : orderedObjects) {
//		indexMap.put(month, index);
//		index++;
//	}
//
//	Collections.sort(objectsToOrder, (o1, o2) -> {
//
//		Integer leftIndex = indexMap.get(o1.getKey());
//		Integer rightIndex = indexMap.get(o2.getKey());
//		if (leftIndex == null) {
//			return -1;
//		}
//		if (rightIndex == null) {
//			return 1;
//		}
//
//		return Integer.compare(leftIndex, rightIndex);
//	});
//}

	public String getPartNumberBySoNumber(String soNumber) {
		return supplyOrderRepository.getPartNoBySoNumber(soNumber);
	}

	public List<Object[]> getPartNumberBySubSlipId(Integer categoryId) {
		return supplyOrderRepository.getPartNumberBySubSlipId(categoryId);
	}

	public String getPartNumberByWarrantId(Integer categoryId) {
		return supplyOrderRepository.getPartNumberByWarrantId(categoryId) ;
	}

	public List<Object[]> getPartNumberByOlId(Integer categoryId) {
		return supplyOrderRepository.getPartNumberByOlId(categoryId);
	}
	
	
	@Transactional
	public void softDeleteSupplyOrder(Integer soId) {
	    SupplyOrder supplyOrder = supplyOrderRepository.findById(soId)
	            .orElseThrow(() -> new ResourceNotFoundException("Supply Order not found with ID: " + soId));

	    Boolean hasInoteGeneratedLots = supplyOrderRepository.existsWithInoteStatus(soId, LOT_STATUS.INT_INOTE_GENERATED, LOT_STATUS.INOTE_GENERATED);
	    
	    if (hasInoteGeneratedLots) {
	        throw new IllegalStateException("Cannot delete Supply Order: One or more associated Lots have already INote generated.");
	    }

	    //Proceed with soft delete if no INotes are found
	    supplyOrder.setIsDel(true);
	    supplyOrder.setDelDatetime(new Date());
//	    supplyOrder.setDelBy(empId);
	    supplyOrderRepository.save(supplyOrder);
	}
	
	/**
     * @author Chirag Swamy
     * @since 30-09-26
     * 
     */
	public List<SupplyOrderSuperAdminDto> getAllSupplyOrdersForSuperAdmin() {
	    List<SupplyOrderSuperAdminDto> supplyOrderDtos = new ArrayList<>();
	    
	    try {
	        List<Object[]> list = supplyOrderRepository.getAllSupplyOrdersForSuperAdmin();
	        
	        for (Object[] obj : list) {
	            SupplyOrderSuperAdminDto dto = new SupplyOrderSuperAdminDto();
	            int index = 0;

	            dto.setId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
	            index++;
	            dto.setOrganizationId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
	            index++;
	            dto.setOrderNo(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setOrderDate(obj[index] != null ? (Date) obj[index] : null);
	            index++;
	            dto.setSalesGroupId(obj[index] != null ? Integer.parseInt(String.valueOf(obj[index])) : null);
	            index++;
	            dto.setSalesGroupName(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setFileNo(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setReceiptDate(obj[index] != null ? (Date) obj[index] : null);
	            index++;
	            dto.setCreatedDate(obj[index] != null ? (Date) obj[index] : null);
	            index++;
	            dto.setStatus(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setIsNotified(obj[index] != null ? (Boolean) obj[index] : null);
	            index++;
	            dto.setRemarks(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setOrganizationName(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setSoStatusNo(obj[index] != null ? String.valueOf(obj[index]) : null);
	            index++;
	            dto.setSoStatusId(obj[index] != null ? (Integer) obj[index] : null);
	            index++;
	            dto.setIsActive(obj[index] != null ? (Boolean) obj[index] : null);
	            index++;
	            dto.setIsDel(obj[index] != null ? (Boolean) obj[index] : null);
	            index++;
	            dto.setDelDatetime(obj[index] != null ? ((Timestamp) obj[index]).toLocalDateTime() : null);
	            index++;

	            List<String> inspAuth = supplyOrderRepository.getInspAuthBySoNumber(dto.getOrderNo());
	            dto.setInspectionAuthName(inspAuth == null || inspAuth.isEmpty() ? null : String.join(" & ", inspAuth));

	            supplyOrderDtos.add(dto);
	        }
	    } catch (Exception e) {
	        logger.error("Error occurred while fetching all Supply Orders for Super Admin: ", e);
	    }
	    return supplyOrderDtos;
	}
}
