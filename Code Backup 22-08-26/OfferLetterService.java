package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.ate.repository.ProductsRepository;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.REGISTRATION_CONSTANTS;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.dto.ChartValuePair;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.InspectionLevel;
import in.co.bel.olms.master.entity.OlStatus;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.ConsigneeRepository;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.InspectionLevelRepository;
import in.co.bel.olms.master.repository.OlStatusRepository;
import in.co.bel.olms.transaction.dto.AllottedRegNosDto;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.INoteLineItemDto;
import in.co.bel.olms.transaction.dto.InoteDetailsDto;
import in.co.bel.olms.transaction.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.OfferLetterStatusDto;
import in.co.bel.olms.transaction.dto.OlAttachmentDto;
import in.co.bel.olms.transaction.dto.OlDetByInspOrgDto;
import in.co.bel.olms.transaction.dto.OlEditDto;
import in.co.bel.olms.transaction.dto.OlLineItemDto;
import in.co.bel.olms.transaction.dto.SubSlipLineItemDto;
import in.co.bel.olms.transaction.dto.insp.InspectionFileNoteDto;
import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.entity.CqaiInoteNo;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.INote;
import in.co.bel.olms.transaction.entity.INoteLineItem;
import in.co.bel.olms.transaction.entity.InspectionFileNote;
import in.co.bel.olms.transaction.entity.InternalINote;
import in.co.bel.olms.transaction.entity.InternalINoteLineItem;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.MergeSubSlip;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OfferLetterAttachment;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.InspOlIb;
import in.co.bel.olms.transaction.enums.DeliveryTypeEnum;
import in.co.bel.olms.transaction.file.service.FileStorageService;
import in.co.bel.olms.transaction.repository.ClearanceLetterRepository;
import in.co.bel.olms.transaction.repository.CqaiInoteNoRepository;
import in.co.bel.olms.transaction.repository.INoteLineItemRepository;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.InternalINoteLineItemRepository;
import in.co.bel.olms.transaction.repository.InternalINoteRepository;
import in.co.bel.olms.transaction.repository.LineItemLotsRegisterRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.OLLineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.OfferLetterAttachmentRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlAllotmentRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.ProjectRepository;
import in.co.bel.olms.transaction.repository.SaleOrderRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.repository.insp.InspectionFileNoteRepository;
import in.co.bel.olms.transaction.service.inote.InoteUtilService;
import in.co.bel.olms.upload.util.FYMonthSorter;
import in.co.bel.olms.validator.InputNameValidation;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;

@Service
public class OfferLetterService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;
	@Autowired
	private OfferLetterRepository offerLetterRepository;

	@Autowired
	private SaleOrderRepository saleOrderRepository;

	@Autowired
	private ConsigneeRepository consigneeRepository;

	@Autowired
	private InspectionLevelRepository inspLevelRepository;

	@Autowired
	private SoLineItemRepository soLineItemRepository;

	@Autowired
	private SubSlipLineItemRepository subSlipLineItemRepository;

	@Autowired
	private OlLineItemRepository olLineItemRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private EmployeeRepository empRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private OfferLetterAttachmentRepository offerLetterAttachmentRepository;

	@Autowired
	private OlStatusRepository olStatusRepository;


	@Autowired
	InternalINoteRepository internalINoteRepository;

	@Autowired
	InternalINoteLineItemRepository internalINoteLineItemRepository;

	@Autowired
	INoteRepository iNoteRepository;

	@Autowired
	INoteLineItemRepository iNoteLineItemRepository;

	@Autowired
	InspectionFileNoteRepository inspectionFileNoteRepository;

	@Autowired
	ClearanceLetterRepository clearanceLetterRepository;

	@Autowired
	private LineItemLotsRegisterService lineItemLotsRegisterService;

	@Autowired
	private LineItemLotsRegisterRepository lineItemLotsRegisterRepository;

	@Autowired
	private OlAllotmentRepository olAllotmentRepository;

	@Autowired
	private InspectionRoundRepository inspectionRoundRepository;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private WSService wsService;

	@Autowired
	private LineItemSerialNoRepository lineItemSerialNoRepository;

	@Autowired
	private OLLineItemSerialNoRepository olLineItemSerialNoRepository;

	@Autowired
	private SubSlipService subSlipService;

	@Autowired
	private WarrantDetailsService warrantDetailsService;

	@Autowired
	private MergeSubSlipRepository mergeSubSlipRepository;

	@Autowired
	private MergeSubSlipService mergeSubSlipService;

	private final SupplyOrderRepository supplyOrderRepository;

	@Autowired
	private LineItemSerialNoService lineItemSerialNoservice;


	@Autowired
	private SpecRepository specRepository;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private InoteUtilService inoteUtilService;

	public OfferLetterService(SupplyOrderRepository supplyOrderRepository) {
		this.supplyOrderRepository = supplyOrderRepository;
	}

	@Autowired
	private FileStorageService fileStorageService;
	Logger logger = LoggerFactory.getLogger(OfferLetterService.class);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private CqaiInoteNoRepository cqaiInoteNoRepository;

	@Autowired
	private Mapper mapper;

	/**
	 * 
	 * @param id
	 * @return
	 */

	public OfferLetter getOfferLetterById(int id) {

		Optional<OfferLetter> offerLetterOptional = offerLetterRepository.findById(id);
		if (offerLetterOptional.isPresent()) {
			return offerLetterOptional.get();
		} else {
			throw new DataNotFoundException("OfferLetter not found by Id: " + id);
		}
	}

	/**
	 * @author Manasa B
	 * @since 09-02-2023
	 * @param id
	 * @return
	 */
	public List<OfferLetterDto> getOfferLetterByCqaeId(int id) {
		List<OfferLetterDto> supplyOrderDtos = new ArrayList<>();
		List<OfferLetter> offerLetters = offerLetterRepository.getOfferLetterByCqaeId(id);
		for (OfferLetter offerLetter : offerLetters) {
			OfferLetterDto offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
			offerLetterDto.setName(offerLetter.getOlNumber());

			SaleOrder saleorder = offerLetter.getSaleOrder();
			SupplyOrder supplyOrder = saleorder.getSupplyOrder();
			offerLetterDto.setSoNumber(supplyOrder.getOrderNo());

			if (null != saleorder.getCategory() && null != saleorder.getCategory().getShip()
					&& null != saleorder.getCategory().getShip().getProject()) {
				offerLetterDto.setProjectName(saleorder.getCategory().getShip().getProject().getProjectName());
			}
			if (offerLetter.getEmployee() != null) {
				offerLetterDto.setInspectorName(offerLetter.getEmployee().getEname());
			}
			supplyOrderDtos.add(offerLetterDto);
		}
		return supplyOrderDtos;
	}

	/**
	 * 
	 * @return
	 */

	public List<OfferLetter> getAllOfferLetter() {
		return offerLetterRepository.findAll();
	}

	@Transactional
	public Map<Boolean, String> saveOfferLetterOld(OfferLetterDto offerLetterDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			List<OfferLetter> offerLetters = new ArrayList<>();
			List<OlLineItemDto> olLineItemDtos = offerLetterDto.getOlLineItemDtos();
			List<String> offerLetterNos = offerLetterRepository.findOfferLetterNos();
			Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(offerLetterDto.getSaleOrderNo());
			List<String> lineItemNoList = offerLetterDto.getOlLineItemDtos().stream()
					.map(OlLineItemDto::getSoLineItemNo).collect(Collectors.toList());
			List<SOLineItem> soLineItems = soLineItemRepository.getLineItemsByNos(lineItemNoList);
			Map<String, SOLineItem> soLineItemMap = new HashMap<>();
			for (SOLineItem soLineItem : soLineItems) {
				soLineItemMap.put(soLineItem.getLineItemNo(), soLineItem);
			}
			SaleOrder saleOrder = null;
			if (saleOrderOptional.isPresent()) {
				saleOrder = saleOrderOptional.get();
			} else {
				String respString = "No sale order found with no :" + offerLetterDto.getSaleOrderNo();
				returnMap.put(false, respString);
				return returnMap;
			}
			for (OlLineItemDto olLineItemDto : olLineItemDtos) {
				if (!offerLetterNos.contains(olLineItemDto.getOfferLetterNo())) {
					OfferLetter offerLetter = new OfferLetter();
					offerLetter.setOlNumber(olLineItemDto.getOfferLetterNo());
					offerLetter.setCreatedDate(todayDate);
					offerLetter.setActive(true);
					offerLetter.setOfferDate(olLineItemDto.getOfferDate());
					offerLetter.setCreatedBy(offerLetterDto.getEmployeeId());
					offerLetter.setSaleOrder(saleOrder);

					SOLineItem soLineItem = soLineItemMap.get(olLineItemDto.getSoLineItemNo());
					if (soLineItem != null) {

						OlLineItem olLineItem = new OlLineItem();
						olLineItem.setCreatedDateTime(todayDate);
						olLineItem.setCreatedBy(offerLetterDto.getEmployeeId());
						olLineItem.setOfferLetter(offerLetter);
						olLineItem.setSoLineItem(soLineItem);
						olLineItem.setOfferedQty(olLineItemDto.getOfferedQty());
						offerLetter.setOlLineItems(Arrays.asList(olLineItem));
						offerLetters.add(offerLetter);
					} else {
						String respString = "No line item found with no:" + olLineItemDto.getSoLineItemNo();
						returnMap.put(false, respString);
						return returnMap;
					}
				} else {
					String respString = "OL exists with name:" + olLineItemDto.getOfferLetterNo();
					logger.error(respString);
					returnMap.put(false, respString);
					return returnMap;
				}
			}
			List<OfferLetter> savedOfferLetters = offerLetterRepository.saveAll(offerLetters);

			for (OfferLetter offerLetter : savedOfferLetters) {

				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setNotificationName("New Offer Letter created with Id " + offerLetter.getOlNumber());
				notificationDto.setNotificationCategory("OL");
				notificationDto.setCategoryId(offerLetter.getId());
				notificationService.saveNotification(notificationDto);
				Integer cqaeId = offerLetterRepository.getCqaeOrgIdByOlId(offerLetter.getId());
				if (cqaeId != null) {
					List<Integer> receivers = empRepository.getJCnAdminIdsByOrgId(cqaeId);
					MessageBean message = new MessageBean();
					message.setSender(offerLetter.getCreatedBy());
					message.setMessageType(ApplicationConstants.SUPPLY_ORDER);
					message.setMessage("New Offer Letter created with Id: " + offerLetter.getOlNumber());
					message.setItemNo(offerLetter.getOlNumber());
					message.setReceivers(receivers);
					wsService.notifyCqae(message);
				}
			}
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (Exception e) {
			logger.error("Error in saving OL {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public List<OfferLetter> getOLBySaleOrderId(int saleOrderId) {

		List<OfferLetter> offerLetters = offerLetterRepository.getOLBySaleOrderId(saleOrderId);
		return offerLetters;
	}

	public List<OlLineItemDto> getLineItemsByOl(int olId) {

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<OlLineItemDto> olLineItemDtos = new ArrayList<>();
		List<OlLineItem> olLineItems = olLineItemRepository.getLineItemsByOl(olId);
		for (OlLineItem olLineItem : olLineItems) {
			OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
			if (olLineItem.getTmrFileName() != null) {
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ olLineItem.getTmrFileName();
				olLineItemDto.setUpdatedTmrFileUrl(fileDownloadUri);

			}
			SOLineItem soLineItem = olLineItem.getSoLineItem();

			if (soLineItem != null) {
				olLineItemDto.setStatus(soLineItem.getStatus());
				BigDecimal unitCost = soLineItem.getUnitCost();
				if (unitCost != null) {
					olLineItemDto.setUnitCostStr(df.format(unitCost));
					BigDecimal itemValue = unitCost.multiply(olLineItem.getOfferedQty()) ;
					olLineItemDto.setItemValueStr(df.format(itemValue));
				}
				if (soLineItem.getTmrFileName() != null) {
					String fileName = soLineItem.getTmrFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
							+ fileName;
					olLineItemDto.setTmrFileUrl(fileDownloadUri);
				}
				olLineItemDto.setOrderedQty(soLineItem.getOrderQty());
				olLineItemDto.setIsImported(soLineItem.getImported());
				olLineItemDto.setOfferLetterNo(olLineItem.getOfferLetter().getOlNumber());
				BigDecimal pendingQty = olLineItemRepository.getPendingQtyByLineItemNo(olLineItem.getId());
				olLineItemDto.setPendingQty(pendingQty != null ? pendingQty : BigDecimal.ZERO);
				
				if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
					olLineItemDto.setPartNumberId(soLineItem.getPartNumber().getId());
					olLineItemDto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
					olLineItemDto.setPartNo(soLineItem.getPartNumber().getPartNo());
					olLineItemDto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
					olLineItemDto.setLineItemNo(soLineItem.getLineItemNo());
					Integer productId = offerLetterRepository
							.findProductNoByPartNumberId(soLineItem.getPartNumber().getId());

					olLineItemDto.setProductId(productId);
				}
			}
			olLineItemDtos.add(olLineItemDto);
		}
		return olLineItemDtos;
	}

	public List<SubSlipLineItemDto> getLineItemsBySubSlip(Integer subSlipId) {

		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		List<SubSlipLineItemDto> subSlipLineItemDtos = new ArrayList<>();
		List<SubSlipLineItem> subSlipLineItems = subSlipLineItemRepository.getLineItemsByOl(subSlipId);
		for (SubSlipLineItem subSlipLineItem : subSlipLineItems) {
			SubSlipLineItemDto subSlipLineItemDto = mapper.map(subSlipLineItem, SubSlipLineItemDto.class);
			if (subSlipLineItem.getTmrFileName() != null) {
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ subSlipLineItem.getTmrFileName();
				subSlipLineItemDto.setUpdatedTmrFileUrl(fileDownloadUri);

			}
			SOLineItem soLineItem = subSlipLineItem.getSoLineItem();

			if (soLineItem != null) {
				subSlipLineItemDto.setStatus(soLineItem.getStatus());
				BigDecimal unitCost = soLineItem.getUnitCost();
				if (unitCost != null) {
					subSlipLineItemDto.setUnitCostStr(df.format(unitCost));
					BigDecimal itemValue = unitCost.multiply(subSlipLineItem.getOfferedQty())  ;
					subSlipLineItemDto.setItemValueStr(df.format(itemValue));
				}
				if (soLineItem.getTmrFileName() != null) {
					String fileName = soLineItem.getTmrFileName();
					String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
							+ fileName;
					subSlipLineItemDto.setTmrFileUrl(fileDownloadUri);
				}
				subSlipLineItemDto.setOrderedQty(soLineItem.getOrderQty());
				subSlipLineItemDto.setIsImported(soLineItem.getImported());
				subSlipLineItemDto.setOfferLetterNo(subSlipLineItem.getSubmissionSlip().getOlNumber());
				BigDecimal pendingQty = BigDecimal.valueOf(subSlipLineItemRepository.getPendingQtyByLineItemNo(subSlipLineItem.getId())) ;
				subSlipLineItemDto.setPendingQty(pendingQty != null ? pendingQty : BigDecimal.ZERO);
				
				if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
					subSlipLineItemDto.setPartNumberId(soLineItem.getPartNumber().getId());
					subSlipLineItemDto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
					subSlipLineItemDto.setPartNo(soLineItem.getPartNumber().getPartNo());
					subSlipLineItemDto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
					subSlipLineItemDto.setLineItemNo(soLineItem.getLineItemNo());
					Integer productId = offerLetterRepository
							.findProductNoByPartNumberId(soLineItem.getPartNumber().getId());

					subSlipLineItemDto.setProductId(productId);
				}
			}
			subSlipLineItemDtos.add(subSlipLineItemDto);
		}
		return subSlipLineItemDtos;
	}
	
	/**
	 * @author Shubham Anand
	 * 
	 */
	public List<OfferLetter> getOfferLettersByIdList(Set<Integer> idList) {
		return offerLetterRepository.getOLByIdlist(idList);
	}

	public List<Object[]> getLineItemsPoQtyGroupByUom(int soId) {
		return soLineItemRepository.getfLineItemsPoQtyGroupByUom(soId);
	}

	public List<Object[]> getLineItemsQtyGroupByUom(int olId) {
		return olLineItemRepository.getLineItemQtyGroupByUom(olId);
	}

	public List<Object[]> getSoLineItemsQtyWithListGroupByUom(List<Integer> soLiIdList) {
		return olLineItemRepository.getSoLineItemQtyWithListGroupByUom(soLiIdList);
	}

	public List<Object[]> getClearedLineItemQtyGroupByUom(Set<Integer> clearedOlLiIdList) {
		return olLineItemRepository.getClearedLineItemQtyGroupByUom(clearedOlLiIdList);
	}

	public List<Integer> getSuppliedLineItemListBySoId(int soId) {
		return soLineItemRepository.getSuppliedLineItemListBySoId(soId);
	}

	public Set<Integer> getClearedOlLineItemsByOlId(Set<Integer> olLiIdList) {
		List<Integer> response = olLineItemRepository.getClearedLineItemsByOlLiIdList(olLiIdList);
		return new HashSet<>(response);
	}

	public List<OfferLetter> getOLBySupplyOrderId(int supplyOrderId) {

		List<OfferLetter> offerLetters = offerLetterRepository.getOLBySupplyOrderId(supplyOrderId);
		return offerLetters;
	}

	// Returns the list of offer letters alloted to the input employee id
	public List<OfferLetter> getAllotedOfferLetters(int empId) {

		List<OfferLetter> offerLetters = new ArrayList<>();
		Optional<Employee> empOptional = empRepository.findById(empId);
		if (empOptional.isPresent()) {
			Employee employee = empOptional.get();
			EmpRole empRole = employee.getEmpRole();
			int empRoleId = empRole.getId();
			switch (empRoleId) {
			case ApplicationConstants.INSPECTOR:
				offerLetters = offerLetterRepository.getAllOfferLettersByInspectorId(empId);
				break;
			case ApplicationConstants.ACQAO:
				offerLetters = offerLetterRepository.getAllOfferLettersBySectionOffcrId(empId);
				break;
			case ApplicationConstants.DCQAO:
				offerLetters = offerLetterRepository.getAllOfferLettersByDcqaoId(empId);
				break;
			case ApplicationConstants.JCQAO:
				offerLetters = offerLetterRepository.getAllOfferLettersByJcQaoId(empId);
				break;
			case ApplicationConstants.CQAO:
				offerLetters = offerLetterRepository.getAllOfferLettersByCqaoId(empId);
				break;
			default:
				break;
			}// new Comparator<OfferLetter>()

		}
		Collections.sort(offerLetters, new Comparator<OfferLetter>() {
			@Override
			public int compare(OfferLetter id1, OfferLetter id2) {
				int returnVal = 0;
				InspOlIb inspOlIb1 = id1.getInspOlIb();
				InspOlIb inspOlIb2 = id2.getInspOlIb();
				if ((inspOlIb1.getSubmittedDate() == null && inspOlIb2.getSubmittedDate() == null)
						|| (inspOlIb1.getSubmittedDate() != null && inspOlIb2.getSubmittedDate() != null)) {
					Date offerDate1 = id1.getOfferDate();
					Date offerDate2 = id2.getOfferDate();
					System.out.println("1st condition....");
					returnVal = offerDate1.compareTo(offerDate2);
				} else if (inspOlIb1.getSubmittedDate() == null && inspOlIb2.getSubmittedDate() != null) {
//                            System.out.println("2nd condition....");
//                            Date offerDate1 = id1.getOfferDate();
//                            Date offerDate2 = id2.getOfferDate();
//                            returnVal = offerDate1.compareTo(offerDate2);
					returnVal = 1;
				} else if (inspOlIb1.getSubmittedDate() != null && inspOlIb2.getSubmittedDate() == null) {
					System.out.println("3rd condition....");
					returnVal = -1;
				}
				return returnVal;
			}
		});
		return offerLetters;
	}

	@Transactional
	public Map<Boolean, String> saveOfferLetter(OfferLetterDto offerLetterDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			Set<OfferLetter> offerLetters = new HashSet<>();
			List<OlLineItemDto> olLineItemDtos = offerLetterDto.getOlLineItemDtos();
//				Set<String> distinctOls = new HashSet<>(
//						olLineItemDtos.stream().map(li -> li.getOfferLetterNo()).collect(Collectors.toSet()));
			List<String> offerLetterNos = offerLetterRepository.findOfferLetterNos();
			InspectionLevel inspLevel = null;
			if (offerLetterDto.getInspectionLevelId() != null) {
				Optional<InspectionLevel> inspLevelOptional = inspLevelRepository
						.findById(offerLetterDto.getInspectionLevelId());
				if (inspLevelOptional.isPresent()) {
					inspLevel = inspLevelOptional.get();
				}
			}
//				List<String> lineItemNoList = offerLetterDto.getOlLineItemDtos().stream()
//						.map(OlLineItemDto::getSoLineItemNo).collect(Collectors.toList());
			List<SOLineItem> soLineItems = soLineItemRepository
					.getSoLineItemsBySaleOrderId(offerLetterDto.getSaleOrderNo());
			Map<String, SOLineItem> soLineItemMap = new HashMap<>();
			for (SOLineItem soLineItem : soLineItems) {
				soLineItemMap.put(soLineItem.getLineItemNo(), soLineItem);
			}
			SaleOrder saleOrder = null;
			if (offerLetterDto.getSaleOrderNo() != null) {
				Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(offerLetterDto.getSaleOrderNo());
				if (saleOrderOptional.isPresent()) {
					saleOrder = saleOrderOptional.get();
				} else {
					String respString = "No sale order found with no :" + offerLetterDto.getSaleOrderNo();
					returnMap.put(false, respString);
					return returnMap;
				}
			}
			Map<String, OfferLetter> olMap = new ConcurrentHashMap<>();
			for (OlLineItemDto olLineItemDto : olLineItemDtos) {
				if (!offerLetterNos.contains(olLineItemDto.getOfferLetterNo())) {
					OfferLetter offerLetter = null;
					if (olMap.containsKey(olLineItemDto.getOfferLetterNo())) {
						offerLetter = olMap.get(olLineItemDto.getOfferLetterNo());
					} else {
						offerLetter = new OfferLetter();
						if (!InputNameValidation.checkString(olLineItemDto.getOfferLetterNo())) {

							String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
							logger.error(respString);
							returnMap.put(false, respString);
							return returnMap;
						}
						offerLetter.setOlNumber(olLineItemDto.getOfferLetterNo());
						offerLetter.setCreatedDate(todayDate);
						offerLetter.setActive(true);
						offerLetter.setOfferDate(olLineItemDto.getOfferDate());
						offerLetter.setCreatedBy(offerLetterDto.getEmployeeId());
						offerLetter.setSaleOrder(saleOrder);
						offerLetter.setLotNumber(offerLetterDto.getLotNumber());
						if (inspLevel != null) {
							offerLetter.setInspectionLevel(inspLevel);
						}
						if (offerLetterDto.getConsigneeId() != null) {
							Optional<Consignee> consigneeOptional = consigneeRepository
									.findById(offerLetterDto.getConsigneeId());
							if (consigneeOptional.isPresent()) {
								offerLetter.setConsignee(consigneeOptional.get());
							}
						}
						olMap.put(olLineItemDto.getOfferLetterNo(), offerLetter);
					}

					SOLineItem soLineItem = soLineItemMap.get(olLineItemDto.getSoLineItemNo());
					if (soLineItem != null) {

						BigDecimal offeredQty = olLineItemDto.getOfferedQty();
						BigDecimal remainingQty = olLineItemRepository.getRemainingQtyBySoLineItemId(soLineItem.getId());
						remainingQty = (remainingQty == null) ? soLineItem.getOrderQty() : remainingQty;
//						System.out.println("remaining qty "+ remainingQty);
//						if (offeredQty > remainingQty) {
//
//							String respString = "Offered qty(" + offeredQty + ") is more than remaining qty("
//									+ remainingQty + ") for line item: " + soLineItem.getLineItemNo();
//							returnMap.put(false, respString);
//							return returnMap;
//
//						}
						OlLineItem olLineItem = new OlLineItem();
						olLineItem.setCreatedDateTime(todayDate);
						olLineItem.setCreatedBy(offerLetterDto.getEmployeeId());
						olLineItem.setOfferLetter(offerLetter);
						olLineItem.setSoLineItem(soLineItem);
						if (!InputNameValidation.checkString(olLineItemDto.getPriority())) {
							String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
							logger.error(respString);
							returnMap.put(false, respString);
							return returnMap;
						}
						olLineItem.setPriority(olLineItemDto.getPriority());
						if (!InputNameValidation.checkString(olLineItemDto.getIssueLevel())) {
							String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
							logger.error(respString);
							returnMap.put(false, respString);
							return returnMap;
						}
						olLineItem.setIssueLevel(olLineItemDto.getIssueLevel());
						olLineItem.setOfferedQty(offeredQty);
//							line
						List<OlLineItem> existingLineItems = offerLetter.getOlLineItems();
						if (existingLineItems == null) {
							existingLineItems = new ArrayList<>();
						}
						existingLineItems.add(olLineItem);
						offerLetter.setOlLineItems(existingLineItems);
						offerLetter.setInternal(offerLetterDto.getIsInternal());
						offerLetters.add(offerLetter);
					} else {
						String respString = "Line item no:" + olLineItemDto.getSoLineItemNo()
								+ " doesn't belong to selected Sale Order";
						returnMap.put(false, respString);
						return returnMap;
					}
				} else {
					String respString = "OL exists with name:" + olLineItemDto.getOfferLetterNo();
					logger.error(respString);
					returnMap.put(false, respString);
					return returnMap;
				}
			}

			List<OfferLetter> savedOfferLetters = offerLetterRepository.saveAll(offerLetters);

			for (OfferLetter offerLetter : savedOfferLetters) {
				List<OlLineItem> olLineItems = offerLetter.getOlLineItems();

				for (OlLineItemDto olLineItemDto : olLineItemDtos) {
					// Check if the offerLetterNo matches the current OlLineItemDto
					if (offerLetter.getOlNumber().equals(olLineItemDto.getOfferLetterNo())) {
						// Find the matching OlLineItem based on SoLineItemNo
						OlLineItem targetOlLineItem = null;
						for (OlLineItem olLineItem : olLineItems) {
							if (olLineItem.getSoLineItem().getLineItemNo().equals(olLineItemDto.getSoLineItemNo())) {
								targetOlLineItem = olLineItem;
								break;
							}
						}

						if (targetOlLineItem != null && olLineItemDto.getRegisterNumbers() != null
								&& !olLineItemDto.getRegisterNumbers().getIds().isEmpty()) {
							LineItemLotsRegisterDto requestDto = new LineItemLotsRegisterDto();
							requestDto.setOlLineItemId(targetOlLineItem.getId());
							requestDto.setIds(olLineItemDto.getRegisterNumbers().getIds());
							if (olLineItemDto.getRegisterNumbers().getStatus() != null) {
								requestDto.setStatus(olLineItemDto.getRegisterNumbers().getStatus());
							} else {
								requestDto.setStatus("in-process");
							}

							try {
								lineItemLotsRegisterService.update(requestDto);
							} catch (Exception e) {
								logger.error(
										"Error updating register for OL: " + offerLetter.getOlNumber() + ", Line Item: "
												+ olLineItemDto.getSoLineItemNo() + ", Error: " + e.getMessage());
							}

						} else {
							String respString = (targetOlLineItem == null) ? "No matching OlLineItem found"
									: "No Register Numbers provided";
							logger.warn(respString);
						}
					}
				}

				// creating a notification for the CQAE user
				Integer cqaeId = offerLetterRepository.getCqaeOrgIdByOlId(offerLetter.getId());
				if (cqaeId != null) {
					List<Integer> receivers = empRepository.getJCnAdminIdsByOrgId(cqaeId);
					MessageBean message = new MessageBean();
					message.setSender(offerLetter.getCreatedBy());
					message.setMessageType(ApplicationConstants.SUPPLY_ORDER);
					message.setMessage("New Offer Letter created with Id: " + offerLetter.getOlNumber());
					message.setItemNo(offerLetter.getOlNumber());
					message.setReceivers(receivers);
					wsService.notifyCqae(message);

					for (Integer receiverId : receivers) {
						NotificationDto notificationDto = new NotificationDto();
						notificationDto
								.setNotificationName("New Offer Letter created with Id " + offerLetter.getOlNumber());
						notificationDto.setNotificationCategory("OL");
						notificationDto.setCategoryId(offerLetter.getId());
						notificationDto.setSenderId(offerLetterDto.getEmployeeId());
						notificationDto.setReceiverId(receiverId);
						notificationService.saveNotification(notificationDto);
					}
				}
			}
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in saving OL {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 * 
	 *         change required to offer letter and remove dependency on internal
	 *         inote
	 */
	public INoteDto getINoteByOlId(int olId) {
		INote existing = iNoteRepository.getByOlId(olId);
		InternalINote offerLetter = internalINoteRepository.getByOlId(olId);

		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			iNoteDto.setInternalInoteId(existing.getOlId());
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (INoteLineItem item : existing.getINoteLineItems()) {
				INoteLineItemDto dto = mapper.map(item, INoteLineItemDto.class);

				String soLineItemNo = olLineItemRepository.getSoLiNoByOlId(olId);
				dto.setLineItemNo(soLineItemNo);

				if (item.getRegNo() != null) {
					String regNo = item.getRegNo();
					String[] parts = regNo.split(",");

					// Filter out empty strings from the array
					List<String> result = Arrays.stream(parts).filter(s -> !s.trim().isEmpty()) // Trim whitespace and
																								// filter empty strings
							.collect(Collectors.toList());
					dto.setRegNos(result);
				}
				iNoteLineItemDtos.add(dto);
			}

			// Getting product full name for Nomenclature
			List<OlLineItem> olLineItem1 = olLineItemRepository.getLineItemsByOl(olId);
			for (OlLineItem ol : olLineItem1) {
				List<LineItemLotsRegister> lineItemLotsRegister = lineItemLotsRegisterRepository
						.getRegisterNosByOlLineItemId(ol.getId());
				for (LineItemLotsRegister liItemLotsRegister1 : lineItemLotsRegister) {
					List<InspectionRound> inspectionRound = inspectionRoundRepository
							.findInspectionRoundByDeviceId(liItemLotsRegister1.getId());
					for (InspectionRound inspectionRound2 : inspectionRound) {
						@SuppressWarnings("deprecation")
						Products products = productsRepository.getById(inspectionRound2.getProduct().getId());
						iNoteDto.setNomenclature(products.getProdFullName());
					}
				}
			}
			Optional<OfferLetter> OpOl = offerLetterRepository.findById(olId);

			if (OpOl != null) {
				OfferLetter ol = OpOl.get();
				SupplyOrder supplyOrder = ol.getSaleOrder().getSupplyOrder();
				iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setInoteDate(existing.getInoteDate());
			iNoteDto.setExisting(true);
			iNoteDto.setOrderedQty(offerLetter.getOrderedQty());
			iNoteDto.setSupplyOrderDateFormatted(dateFormat.format(iNoteDto.getSupplyOrderDate()));
			iNoteDto.setOfferLetterDateFormatted(dateFormat.format(iNoteDto.getOfferLetterDate()));
			iNoteDto.setInoteDateFormatted(outputDateFormat.format(iNoteDto.getInoteDate()));
			// author: Shridhar R
			// digital signature for qac
			iNoteDto.setSubmittedByName(
					existing.getSubmittedBy() != null ? existing.getSubmittedBy().getEname() : null);
			iNoteDto.setSubmittedByDesignation(
					existing.getSubmittedBy() != null ? existing.getSubmittedBy().getEmpRole().getEmpRole() : null);

			return iNoteDto;
		} else {
			return getNewINote(olId);
		}

	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 */
	public INoteDto getNewINote(int olId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		Optional<OfferLetter> optionalOl = offerLetterRepository.findById(olId);
		if (optionalOl.isPresent()) {

			OfferLetter oll = optionalOl.get();
			List<Object[]> obj = offerLetterRepository.getDepartSecCode(olId);
			String secCode = null;
			if (obj != null) {
				for (Object[] det : obj) {
					secCode = det[1] != null ? det[1].toString() : null;
				}
			}

			InternalINote offerLetter = internalINoteRepository.getByOlId(olId);

			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setSupplyOrderNo(offerLetter.getSupplyOrderNo());
			iNoteDto.setSupplyOrderDate(offerLetter.getSupplyOrderDate());
			iNoteDto.setWorkOrderNo(offerLetter.getWorkOrderNo());
			iNoteDto.setRfEstNo(offerLetter.getRfEstNo());
			iNoteDto.setWarranty(offerLetter.getWarranty() != null ? offerLetter.getWarranty() : null);
			iNoteDto.setEpCode(offerLetter.getEpCode());
			iNoteDto.setOrderedQty(offerLetter.getOrderedQty());
			iNoteDto.setClearedQty(offerLetter.getClearedQty());
			iNoteDto.setInternalInoteId(offerLetter.getId());
			iNoteDto.setOlId(offerLetter.getId());
			SupplyOrder supplyOrder = oll.getSaleOrder().getSupplyOrder();
			iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
			String fY = getFinancialYearStrForInote();
			Integer inoteCountInCurrentFY = null;
			inoteCountInCurrentFY = iNoteRepository.getInoteCountInCurrentFY(null) == null ? 0
					: iNoteRepository.getInoteCountInCurrentFY(null);
			String inoteNo = "T" + secCode + fY + (String.format("%05d", inoteCountInCurrentFY + 1));
			iNoteDto.setINoteNo(inoteNo);
			iNoteDto.setContractorName(offerLetter.getContractorName());
			iNoteDto.setContractorAddress(offerLetter.getContractorAddress());
			iNoteDto.setIndentor(offerLetter.getIndentor());
			iNoteDto.setIndentNo(offerLetter.getIndentNo());
			iNoteDto.setIndentDate(offerLetter.getIndentDate());
			iNoteDto.setOrgName(offerLetter.getContractorName());
			iNoteDto.setConsignee(offerLetter.getConsignee());
			iNoteDto.setOfferLetterNo(offerLetter.getOfferLetterNo());
			iNoteDto.setOfferLetterDate(offerLetter.getOfferLetterDate());
			iNoteDto.setInspectionStart(offerLetter.getCreatedDatetime());
			iNoteDto.setInspectionEnd(new Date());
			iNoteDto.setDeliveryDate(offerLetter.getDeliveryDate());

//			iNoteDto.setDescription(offerLetter.getStoresDescription());
			// inspection Start Date and End Date, description

			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (InternalINoteLineItem olLineItem : offerLetter.getINoteLineItems()) {

				INoteLineItemDto dto = new INoteLineItemDto();
				dto.setPartNo(olLineItem.getPartNo());
				dto.setDescription(olLineItem.getDescription());
				dto.setOrderedQty(olLineItem.getOrderedQty());
				dto.setOlLineItemId(olLineItem.getId());
				dto.setUom(olLineItem.getUom());
				dto.setTendered(olLineItem.getTendered());
				dto.setAccepted(olLineItem.getAccepted());
				dto.setRejected(olLineItem.getRejected());
				dto.setLedgerFolio(olLineItem.getLedgerFolio());
				dto.setQtyAcceptedToDate(olLineItem.getQtyAcceptedToDate());
				dto.setIndentNo(olLineItem.getIndentNo()); // crg newInote
				List<LineItemLotsRegister> lineItemLotRegisters = olLineItem.getOlLineItem().getLineItemLotsRegisters();
				List<LineItemLotsRegister> filtered = lineItemLotRegisters.stream().filter(
						reg -> Objects.equals(reg.getStatus(), ApplicationConstants.REGISTRATION_CONSTANTS.ACCEPTED))
						.collect(Collectors.toList());
				dto.setRegNos(filtered.stream().map(LineItemLotsRegister::getRegisterNo).collect(Collectors.toList()));
				// set internalInote Line Item Id as ol line item id
				dto.setOlLineItemId(olLineItem.getId());

				iNoteLineItemDtos.add(dto);
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setExisting(false);

			return iNoteDto;

		} else
			return null;
	}

	public INoteDto getINoteByOlIdV2(Integer olId) {
		INote existing = iNoteRepository.getByOlId(olId);
		OfferLetter offerLetter = offerLetterRepository.findById(olId)
				.orElseThrow(() -> new IllegalArgumentException("Ol not found."));

		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			iNoteDto.setInternalInoteId(existing.getOlId());
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (INoteLineItem item : existing.getINoteLineItems()) {
				INoteLineItemDto dto = mapper.map(item, INoteLineItemDto.class);
				List<Object[]> soLi = olLineItemRepository.getSoLiNoDetailsByOlId(olId);
				for (Object[] row : soLi) {
					dto.setCatPartNo(row[5] != null ? (String) row[5] : null);
					dto.setNsnPartNo(row[6] != null ? (String) row[6] : null);
					dto.setItemCategory(row[7] != null ? (String) row[7] : null);
					dto.setLineItemNo(row[8] != null ? (String) row[8] : null);
					dto.setNomenclature(row[3] != null ? (String) row[3] : null);
				}
				
				List<OLLineItemSerialNo> passedSerialNos = item.getOlLineItemSerialNos().stream()
						.filter(reg -> !reg.getSerialNo().getStatus().getStatus()
								.equalsIgnoreCase(ApplicationConstants.REGISTRATION_CONSTANTS.RFR))
						.sorted(Comparator.comparing(reg -> reg.getSerialNo().getId()))
						.collect(Collectors.toList());

				dto.setRegs(passedSerialNos.stream().map(serialno -> {
					HashMap<String, Object> regsDto = new HashMap<>();
					regsDto.put("id", serialno.getId());
					regsDto.put("serialNoId", serialno.getSerialNo().getId());
					regsDto.put("serialNo", serialno.getSerialNo().getSerialNo());
					if (serialno.getIsSample() != null && serialno.getIsSample())
						regsDto.put("isSample", true);
					else if (serialno.getIsFaiSample() != null && serialno.getIsFaiSample())
						regsDto.put("isFaiSample", true);
					return regsDto;
				}).collect(Collectors.toList()));

				List<OLLineItemSerialNo> rejectedSerialNos = item.getOlLineItemSerialNos().stream()
						.filter(reg -> reg.getSerialNo().getStatus().getStatus()
								.equalsIgnoreCase(ApplicationConstants.REGISTRATION_CONSTANTS.RFR))
						.sorted(Comparator.comparing(reg -> reg.getSerialNo().getId()))
						.collect(Collectors.toList());
				
				dto.setFailedRegs(rejectedSerialNos.stream().map(serialno -> {
					HashMap<String, Object> regsDto = new HashMap<>();
					regsDto.put("id", serialno.getId());
					regsDto.put("serialNoId", serialno.getSerialNo().getId());
					regsDto.put("serialNo", serialno.getSerialNo().getSerialNo());
					if (serialno.getIsSample() != null && serialno.getIsSample())
						regsDto.put("isSample", true);
					else if (serialno.getIsFaiSample() != null && serialno.getIsFaiSample())
						regsDto.put("isFaiSample", true);
					return regsDto;
				}).collect(Collectors.toList()));
				
				BigDecimal accepted = passedSerialNos!=null ? BigDecimal.valueOf(passedSerialNos.size()) : BigDecimal.ZERO;
				BigDecimal rejected = rejectedSerialNos!=null ?BigDecimal.valueOf(rejectedSerialNos.size()): BigDecimal.ZERO;
				BigDecimal tendered =  accepted.add(rejected);
				dto.setAccepted(accepted);
				dto.setRejected(rejected);
				dto.setTendered(tendered);
				if(dto.getLedgerFolio() == null) {
					dto.setCumulativePassed(accepted);
				} else {
					dto.setCumulativePassed(dto.getLedgerFolio().add(accepted));
				}
				String failedParam = specRepository.getFailedSpecForInote(dto.getOlLineItemId());
				dto.setFailedParamDesc(failedParam);

				iNoteLineItemDtos.add(dto);
			}

			// Getting product full name for Nomenclature
			List<Object[]> soLi = olLineItemRepository.getSoLiNoDetailsByOlId(olId);
			DateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
			for (Object[] row : soLi) {
				String date = sdf.format(row[0]);
				iNoteDto.setDeliveryDate(date);
				iNoteDto.setOrderTypeNo((String) row[1]);
				iNoteDto.setDrawingNo((String) row[2]);
				iNoteDto.setNomenclature((String) row[3]);
				String OrderTypeDate = dateFormat.format(row[4]);
				iNoteDto.setOrderTypeDate(OrderTypeDate);
//	       	iNoteDto.setCatPartNo(row[5]!=null ? (String)row[5]: null);
//	       	iNoteDto.setNsnNo(row[6]!=null? (String)row[6]: null);
//	       	iNoteDto.setItemCategory(row[7]!=null? (String)row[7]: null);
			}

			List<OlLineItem> olLineItem1 = olLineItemRepository.getLineItemsByOl(olId);
			for (OlLineItem ol : olLineItem1) {
//				dto.setTendered(ol.getOfferedQty());
				List<LineItemLotsRegister> lineItemLotsRegister = lineItemLotsRegisterRepository
						.getRegisterNosByOlLineItemId(ol.getId());
				for (LineItemLotsRegister liItemLotsRegister1 : lineItemLotsRegister) {
					List<InspectionRound> inspectionRound = inspectionRoundRepository
							.findInspectionRoundByDeviceId(liItemLotsRegister1.getId());
					for (InspectionRound inspectionRound2 : inspectionRound) {
						@SuppressWarnings("deprecation")
						Products products = productsRepository.getById(inspectionRound2.getProduct().getId());
						iNoteDto.setNomenclature(products.getProdFullName());
					}
				}
			}
			OfferLetter ol = offerLetterRepository.findById(olId)
					.orElseThrow(() -> new IllegalArgumentException("Ol not found: " + olId));

			SupplyOrder supplyOrder = ol.getSaleOrder().getSupplyOrder();
			if (supplyOrder != null) {
				iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
				iNoteDto.setOrderTypeNo(supplyOrder.getSoLineItems().get(0).getOrderTypeNo());
				iNoteDto.setOrderedQty(supplyOrder.getTotalQty());
				iNoteDto.setIndentor(supplyOrder.getIndenter() != null ? supplyOrder.getIndenter().getName() : "");
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setInoteDate(existing.getInoteDate());
			iNoteDto.setExisting(true);
//			iNoteDto.setOrderedQty(offerLetter.getOrderedQty());
			iNoteDto.setSupplyOrderDateFormatted(dateFormat.format(iNoteDto.getSupplyOrderDate()));
			iNoteDto.setOfferLetterDateFormatted(dateFormat.format(iNoteDto.getOfferLetterDate()));
			iNoteDto.setInoteDateFormatted(outputDateFormat.format(iNoteDto.getInoteDate()));
			// author: Shridhar R
			// digital signature for qac
			iNoteDto.setSubmittedByName(
					existing.getSubmittedBy() != null ? existing.getSubmittedBy().getEname() : null);
			iNoteDto.setSubmittedByDesignation(
					existing.getSubmittedBy() != null ? existing.getSubmittedBy().getEmpRole().getEmpRole() : null);
			iNoteDto.setCqaeInoteNo(existing.getCqaeInoteNo());

			return iNoteDto;
		} else {
			return getNewINoteV2(olId);
		}

	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 */
	public INoteDto getNewINoteV2(int olId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		Optional<OfferLetter> optionalOl = offerLetterRepository.findById(olId);
		if (optionalOl.isPresent()) {

			OfferLetter offerLetter = optionalOl.get();
			List<Object[]> obj = offerLetterRepository.getDepartSecCode(olId);
			String secCode = null;
			Integer deptId = null;
			if (obj != null) {
				for (Object[] det : obj) {
					deptId = det[0] != null ? (Integer) det[0] : null;
					secCode = det[2] != null ? det[2].toString() : null;
				}
			}


			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setSupplyOrderNo(offerLetter.getSupplyOrderNo());
			iNoteDto.setSupplyOrderDate(offerLetter.getSaleOrder().getSupplyOrder().getOrderDate());
			iNoteDto.setWorkOrderNo(offerLetter.getSaleOrder().getSaleOrderNo());
			iNoteDto.setWarranty(offerLetter.getSaleOrder().getCategory() != null
					? offerLetter.getSaleOrder().getCategory().getCategoryName()
					: null);

			iNoteDto.setInternalInoteId(offerLetter.getId());
			iNoteDto.setOlId(offerLetter.getId());
			SupplyOrder supplyOrder = offerLetter.getSaleOrder().getSupplyOrder();
			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
			iNoteDto.setReferenceNo(supplyOrder.getReferenceNo());
			
			String inoteNo = inoteUtilService.getNextInotenumber(deptId, secCode);
			
			
			iNoteDto.setINoteNo(inoteNo);

			iNoteDto.setIndentNo(offerLetter.getSaleOrder().getSupplyOrder().getIndentNo());
			iNoteDto.setIndentDate(offerLetter.getSaleOrder().getSupplyOrder().getIndentDate());
			iNoteDto.setConsignee(offerLetter.getConsignee() != null ? offerLetter.getConsignee().getName() : null);
			iNoteDto.setOfferLetterNo(offerLetter.getOlNumber());
			iNoteDto.setOfferLetterDate(offerLetter.getOfferDate());
			iNoteDto.setInspectionStart(offerLetter.getCreatedDate());
			iNoteDto.setInspectionEnd(new Date());
			iNoteDto.setMergeSubSlipId(offerLetter.getLot().getId());

			// inspection Start Date and End Date, description
			iNoteDto.setIndentor(supplyOrder.getIndenter() != null ? supplyOrder.getIndenter().getName() : "");
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (OlLineItem olLineItem : offerLetter.getOlLineItems()) {

				INoteLineItemDto dto = new INoteLineItemDto();
				List<Object[]> soLi = olLineItemRepository.getSoLiNoDetailsByOlId(olId);

				for (Object[] row : soLi) {
					dto.setNomenclature(row[3] != null ? (String) row[3] : null);
					dto.setCatPartNo(row[5] != null ? (String) row[5] : null);
					dto.setNsnPartNo(row[6] != null ? (String) row[6] : null);
					dto.setItemCategory(row[7] != null ? (String) row[7] : null);
					dto.setLineItemNo(row[8] != null ? (String) row[8] : null);
				}

				dto.setPartNo(olLineItem.getSoLineItem().getPartNumber().getPartNo());
				dto.setDescription(olLineItem.getSoLineItem().getDescription());
				dto.setOrderedQty(olLineItem.getOfferedQty());
				dto.setOlLineItemId(olLineItem.getId());
				dto.setUom(olLineItem.getSoLineItem().getPartNumber().getUom().getUnit());
				dto.setTendered(olLineItem.getOfferedQty());

				// crg newInote

				List<OLLineItemSerialNo> serialNos = olLineItem.getOlLineitemSerialNos();
//        olLineItem.getOlLineitemSerialNos().stream().map(s -> !s.getSerialNo().getStatus().equals(REGISTRATION_CONSTANTS.RFR));
				List<OLLineItemSerialNo> accpetedSerialNos = serialNos.stream()
						.filter(reg -> !reg.getSerialNo().getStatus().getStatus()
								.equalsIgnoreCase(ApplicationConstants.REGISTRATION_CONSTANTS.RFR))
						.sorted(Comparator.comparing(reg -> reg.getSerialNo().getId()))
						.collect(Collectors.toList());
				dto.setRegs(accpetedSerialNos.stream().map(serialno -> {
					HashMap<String, Object> regsDto = new HashMap<>();
					regsDto.put("id", serialno.getId());
					regsDto.put("serialNoId", serialno.getSerialNo().getId());
					regsDto.put("serialNo", serialno.getSerialNo().getSerialNo());
					if (serialno.getIsSample() != null && serialno.getIsSample())
						regsDto.put("isSample", true);
					else if (serialno.getIsFaiSample() != null && serialno.getIsFaiSample())
						regsDto.put("isFaiSample", true);
					return regsDto;
				}).collect(Collectors.toList()));

				
				List<OLLineItemSerialNo> rejectedSerialNos = serialNos.stream()
						.filter(reg -> reg.getSerialNo().getStatus().getStatus()
								.equalsIgnoreCase(ApplicationConstants.REGISTRATION_CONSTANTS.RFR))
						.sorted(Comparator.comparing(reg -> reg.getSerialNo().getId()))
						.collect(Collectors.toList());
				dto.setFailedRegs(rejectedSerialNos.stream().map(serialno -> {
					HashMap<String, Object> regsDto = new HashMap<>();
					regsDto.put("id", serialno.getId());
					regsDto.put("serialNoId", serialno.getSerialNo().getId());
					regsDto.put("serialNo", serialno.getSerialNo().getSerialNo());
					if (serialno.getIsSample() != null && serialno.getIsSample())
						regsDto.put("isSample", true);
					else if (serialno.getIsFaiSample() != null && serialno.getIsFaiSample())
						regsDto.put("isFaiSample", true);
					return regsDto;
				}).collect(Collectors.toList()));
			
        
        if( olLineItem.getSoLineItem().getRegistrationNo().getId() == ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER ) {
        	if(!accpetedSerialNos.isEmpty() && accpetedSerialNos!= null) {
           	 dto.setAccepted(olLineItem.getOfferedQty());
         	dto.setRejected(BigDecimal.ZERO);
        	}else {        		
        		dto.setRejected(olLineItem.getOfferedQty());
        	}
         
        }else {
        	dto.setAccepted(BigDecimal.valueOf(accpetedSerialNos.size()));
        	dto.setRejected(olLineItem.getOfferedQty().subtract(BigDecimal.valueOf(accpetedSerialNos.size())))  ;
        }
        	
		dto.setLedgerFolio(iNoteRepository.getTotalAcceptedQtyforSOLineItem(olLineItem.getSoLineItem().getId()));
		

				// set internalInote Line Item Id as ol line item id
				dto.setOlLineItemId(olLineItem.getId());
				iNoteDto.setOrderTypeNo(olLineItem.getSoLineItem().getOrderTypeNo());
				String OrderTypeDate = dateFormat.format(olLineItem.getSoLineItem().getOrderTypeDate());
				iNoteDto.setOrderTypeDate(OrderTypeDate);

				String failedParam = specRepository.getFailedSpecForInote(dto.getOlLineItemId());
				dto.setFailedParamDesc(failedParam);

				iNoteLineItemDtos.add(dto);
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);
			iNoteDto.setExisting(false);
			iNoteDto.setCqaeInoteNo(getPreviewInoteNo());

			return iNoteDto;

		} else
			return null;
	}

	@Transactional
	public String saveINote(INoteDto iNoteDto) throws Exception {
		Date todayDate = new Date();
		INote existing = iNoteRepository.getInoteByInternalInoteId(iNoteDto.getInternalInoteId());
		INote savedINote;

		if (existing != null) {
			existing.setModifiedDatetime(todayDate);
			mapper.map(iNoteDto, existing);

			savedINote = iNoteRepository.save(existing);

			processLineItems(iNoteDto, savedINote, existing, todayDate);
		} else {
			INote iNote = mapper.map(iNoteDto, INote.class);
			if (iNoteDto.getCreatedById() != null) {
				iNote.setCreatedBy(iNoteDto.getCreatedById());
			}
			iNote.setCreatedDatetime(todayDate);
			iNote.setInoteDate(todayDate);

			String generatedInoteNo = generateUniqueInoteNoWithRetry();
			iNote.setCqaeInoteNo(generatedInoteNo);

//      Optional<InternalINote> internalINote = internalINoteRepository.findById(iNoteDto.getInternalInoteId());
//      internalINote.ifPresent(note -> iNote.setOlId(note.getOlId()));
			List<Object[]> obj = offerLetterRepository.getDepartSecCode(iNoteDto.getOlId());
			String secCode = null;
			Integer deptId = null;
			if (obj != null) {
				for (Object[] det : obj) {
					deptId = det[0] != null ? (Integer) det[0] : null;
					secCode = det[1] != null ? det[1].toString() : null;
				}
			}
			Department department = departmentRepository.findById(deptId).orElse(null);
			iNote.setDepartment(department);

			savedINote = iNoteRepository.save(iNote);

			processLineItems(iNoteDto, savedINote, null, todayDate);
		}

		// After generating iNote, update Warrants status and subSlip Status to
		// completed based on condition
		updateLineItemSerialNoStatus(iNoteDto.getOlId(), savedINote);
		warrantDetailsService.checkAndUpdateWarrantStatus(iNoteDto.getOlId(),
				ApplicationConstants.COMMON_STATUS_CONSTANTS.COMPLETED, false);
		subSlipService.checkAndUpdateSslipStatus(iNoteDto.getOlId(),
				ApplicationConstants.COMMON_STATUS_CONSTANTS.COMPLETED, false);
		mergeSubSlipService.updateMergeSubSLipStatus(iNoteDto.getMergeSubSlipId(),
				ApplicationConstants.COMMON_STATUS_CONSTANTS.I_NOTE_GENERATED);
		return ApplicationConstants.SUCCESS;
	}

	private String generateUniqueInoteNoWithRetry() {
		int maxRetries = 3;
		int attempts = 0;

		while (attempts < maxRetries) {
			try {
				String prospectiveNo = getPreviewInoteNo();

				int counter = extractCounterFromInoteNo(prospectiveNo);

				CqaiInoteNo logEntry = CqaiInoteNo.builder().iNoteNo(prospectiveNo).counter(counter).build();

				cqaiInoteNoRepository.save(logEntry);

				return prospectiveNo;

			} catch (DataIntegrityViolationException e) {
				attempts++;
				if (attempts >= maxRetries) {
					throw new RuntimeException(
							"Could not generate a unique Inote Number. System is busy. Please try again.");
				}
			}
		}
		throw new RuntimeException("Failed to generate Inote Number.");
	}

	private int extractCounterFromInoteNo(String inoteNo) {
		String[] parts = inoteNo.split("/");
		return Integer.parseInt(parts[4]);
	}

	private void processLineItems(INoteDto iNoteDto, INote savedINote, INote existing, Date todayDate) {
		List<INoteLineItem> iNoteLineItems = new ArrayList<>();
		boolean isUpdate = (existing != null);

		for (INoteLineItemDto dto : iNoteDto.getINoteLineItems()) {
			INoteLineItem item;
            
			List<Integer> regIds = dto.getRegs().stream().map(obj -> (Integer) obj.get("id"))
					.collect(Collectors.toList());
			List<Integer> failedRegsIds = dto.getFailedRegs().stream().map(obj -> (Integer) obj.get("id"))
					.collect(Collectors.toList());
			List<OLLineItemSerialNo> olisns = olLineItemSerialNoRepository.findAllById(regIds);
			List<OLLineItemSerialNo> failedOlisns = olLineItemSerialNoRepository.findAllById(failedRegsIds);

			if (isUpdate) {
				Optional<INoteLineItem> itemOpt = iNoteLineItemRepository.findById(dto.getId());
				if (itemOpt.isPresent()) {
					item = itemOpt.get();
					mapper.map(dto, item);
					item.setINote(savedINote);
					item.setCreatedDatetime(todayDate);
					item.setRemarks(dto.getRemarks());
					olisns.forEach(olisn -> olisn.setINoteLineItem(item));
					failedOlisns.forEach(failedOlisn -> failedOlisn.setINoteLineItem(item));
					iNoteLineItems.add(item);
				}
			} else {
				item = mapper.map(dto, INoteLineItem.class);
				item.setINote(savedINote);
				item.setCreatedDatetime(todayDate);
				item.setRemarks(dto.getRemarks());
				olisns.forEach(olisn -> olisn.setINoteLineItem(item));
				failedOlisns.forEach(failedOlisn -> failedOlisn.setINoteLineItem(item));

				
				iNoteLineItems.add(item);
			}
		}

		if (!iNoteLineItems.isEmpty()) {
		   iNoteLineItemRepository.saveAll(iNoteLineItems);
			
		}
	}

	public List<OfferLetterDto> getOlApprovalDetailsByOrgId(int orgId) {

		List<Object[]> approvedOLs = offerLetterRepository.getOlApprovalDetailsByOrgIdAndEmpId(orgId,
				ApplicationConstants.CQAO);

		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();

		for (Object[] objects : approvedOLs) {

			OfferLetterDto offerLetterDto = new OfferLetterDto();
			offerLetterDto.setId((int) objects[0]);
			offerLetterDto.setOlNumber(objects[1].toString());
			offerLetterDto.setOfferDate(objects[2].toString());
			offerLetterDto.setSalesGroupName(objects[3].toString());
			offerLetterDto.setProjectName(objects[4].toString());
			offerLetterDto.setInoteDate(objects[5].toString());
			offerLetterDto.setINoteTypeId((int) objects[6]);
			offerLetterDto.setInoteStatus(objects[8].toString());
			// Integer
			// clearanceId=clearanceLetterRepository.getByOlId(offerLetterDto.getId());
			boolean status = objects[7] != null ? true : false;
			offerLetterDto.setClearanceStatus(status);
			offerLetterDtos.add(offerLetterDto);

		}

		return offerLetterDtos;

	}

	public List<ChartValuePair> getActiveOlForDashboard(int orgId, int projectId, int inspectorId) throws Exception {

		List<ChartValuePair> chartValuePairs = new LinkedList<>();

		Integer activeOls = offerLetterRepository.getActiveOlForDashboard(orgId, projectId, inspectorId);

		ChartValuePair activeOlPair = new ChartValuePair();
		activeOlPair.setLabel("Active OLs");
		activeOlPair.setValue(activeOls);

		chartValuePairs.add(activeOlPair);

		return chartValuePairs;
	}

	public List<Map<String, Object>> getReturnedNClearedOlsDashboard(int orgId, int category, int categoryValue)
			throws Exception {

		List<Map<String, Object>> olChartValuePairs = new LinkedList<>();
//		category = 1 is financial year wise
		if (category == 1) {
			// Getting current financial year start and end date
			LocalDate fyStartDate = LocalDate.of(categoryValue - 1, 4, 1); // Financial year starts on April 1st of
																			// selectedFY
			LocalDate fyEndDate = LocalDate.of(categoryValue, 3, 31);

			List<Object[]> returnedNClearedOlList = offerLetterRepository.getReturnedNClearedOlsDashboard(orgId,
					fyStartDate, fyEndDate);

			List<String> availableMonthsInResult = new ArrayList<>();
			if (!returnedNClearedOlList.isEmpty()) {

				for (Object[] returnedNClearedOlObj : returnedNClearedOlList) {

//					OlChartValuePair olChartValuePair = new OlChartValuePair();
//					olChartValuePair.setMonth(returnedNClearedOlObj[0].toString());
//					olChartValuePair.setCleared(new BigDecimal(returnedNClearedOlObj[2].toString()).intValue());
//					olChartValuePair.setReturned(new BigDecimal(returnedNClearedOlObj[1].toString()).intValue());
//
//					olChartValuePairs.add(olChartValuePair);

					availableMonthsInResult.add(returnedNClearedOlObj[0].toString());

					Map<String, Object> olChartMap = new LinkedHashMap<>();
					olChartMap.put("month", returnedNClearedOlObj[0].toString());
					olChartMap.put("returned", new BigDecimal(returnedNClearedOlObj[2].toString()).intValue());
					olChartMap.put("cleared", new BigDecimal(returnedNClearedOlObj[1].toString()).intValue());

					olChartValuePairs.add(olChartMap);
				}
			}

//			List<String> availableMonthsInResult = olChartValuePairs.stream()
//					.map(olChartValuePair -> olChartValuePair.getMonth()).collect(Collectors.toList());

//			List<String> availableMonthsInResult = olChartValuePairs.stream()
//					.map(olChartValuePair -> olChartValuePair.getMonth()).collect(Collectors.toList());

			List<String> sortedMonthsList = Arrays.asList("APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
					"JAN", "FEB", "MAR");

			for (String monthName : sortedMonthsList) {
				if (!availableMonthsInResult.contains(monthName)) {
//					OlChartValuePair olChartValuePair = new OlChartValuePair();
//					olChartValuePair.setMonth(monthName);
//					olChartValuePair.setCleared(0);
//					olChartValuePair.setReturned(0);
//					olChartValuePairs.add(olChartValuePair);

					Map<String, Object> olChartMap = new LinkedHashMap<>();
					olChartMap.put("month", monthName);
					olChartMap.put("returned", 0);
					olChartMap.put("cleared", 0);

					olChartValuePairs.add(olChartMap);
				}
			}
			FYMonthSorter.sortList(olChartValuePairs, sortedMonthsList);
		}
//		category = 2 is project wise
		else if (category == 2) {

			Project project = projectRepository.findById(categoryValue).get();
			List<Object[]> projectWiseOlList = offerLetterRepository.getProjectWiseOls(orgId, categoryValue);

			if (!projectWiseOlList.isEmpty()) {

				for (Object[] projectWiseOlObj : projectWiseOlList) {

//					OlChartValuePair olChartValuePair = new OlChartValuePair();
//					olChartValuePair.setMonth(projectWiseOlObj[0].toString());
//					olChartValuePair.setCleared(new BigDecimal(projectWiseOlObj[2].toString()).intValue());
//					olChartValuePair.setReturned(new BigDecimal(projectWiseOlObj[1].toString()).intValue());
//
//					olChartValuePairs.add(olChartValuePair);

					Map<String, Object> olChartMap = new LinkedHashMap<>();
					olChartMap.put("project", project.getProjectName());
					olChartMap.put("active", new BigDecimal(projectWiseOlObj[0].toString()).intValue());
//					olChartMap.put("pending", new BigDecimal(projectWiseOlObj[1].toString()).intValue());

					olChartValuePairs.add(olChartMap);
				}
			}

		}
//		category = 3 is inspector wise
		else if (category == 3) {

			Employee employee = empRepository.findById(categoryValue).get();
			List<Object[]> inspectorWiseOlList = offerLetterRepository.getInspectorWiseOls(orgId, categoryValue);

			if (!inspectorWiseOlList.isEmpty()) {

				for (Object[] inspectorWiseOlObj : inspectorWiseOlList) {

//					OlChartValuePair olChartValuePair = new OlChartValuePair();
//					olChartValuePair.setMonth(inspectorWiseOlObj[0].toString());
//					olChartValuePair.setCleared(new BigDecimal(inspectorWiseOlObj[2].toString()).intValue());
//					olChartValuePair.setReturned(new BigDecimal(inspectorWiseOlObj[1].toString()).intValue());
//					olChartValuePairs.add(olChartValuePair);

					Map<String, Object> olChartMap = new LinkedHashMap<>();
					olChartMap.put("inspector", employee.getEname());
					olChartMap.put("inprogress", new BigDecimal(inspectorWiseOlObj[0].toString()).intValue());
					olChartMap.put("pending", new BigDecimal(inspectorWiseOlObj[1].toString()).intValue());

					olChartValuePairs.add(olChartMap);

				}
			}
		}
		return olChartValuePairs;
	}

	public Map<Boolean, String> updateOfferLetter(OfferLetterDto offerLetterDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		Optional<OfferLetter> olOptional = offerLetterRepository.findById(offerLetterDto.getId());
		if (olOptional.isPresent()) {
			OfferLetter offerLetter = olOptional.get();

			if (!InputNameValidation.checkRemarks(offerLetterDto.getStoresDescription())) {
				throw new IllegalArgumentException("Invalid characters in Store Description");
			}
			if (!InputNameValidation.checkRemarks(offerLetterDto.getRemarks())) {
				throw new IllegalArgumentException("Invalid characters in Remarks");
			}

			offerLetter.setStoresDescription(offerLetterDto.getStoresDescription());
			offerLetter.setRemarks(offerLetterDto.getRemarks());
			offerLetter.setModifiedDate(new Date());
			offerLetter.setModifiedBy(offerLetterDto.getEmployeeId());

			if (offerLetterDto.getConsigneeId() != null) {
				Optional<Consignee> consigneeOptional = consigneeRepository.findById(offerLetterDto.getConsigneeId());
				if (consigneeOptional.isPresent()) {
					offerLetter.setConsignee(consigneeOptional.get());
				} else {
					throw new DataNotFoundException("No Consignee found with the given Id.");
				}
			}

			offerLetterRepository.save(offerLetter);
			offerLetterRepository.updateINoteDetails(offerLetter.getId(), offerLetter.getStoresDescription(),
					offerLetter.getRemarks());
			returnMap.put(true, "OL details updated successfully.");
			return returnMap;
		} else {
			throw new DataNotFoundException("No Offer Letter found with given Id.");
		}

	}

	public List<Object[]> getOlAllotedForApproval(int empId) {
		return offerLetterRepository.getOlAllotedForApproval(empId);
	}

	public OfferLetterDto getOlDetailsForPrint(int olId) {

		OfferLetterDto offerLetterDto = null;
		DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		try {

			Optional<OfferLetter> olOptional = offerLetterRepository.findById(olId);
			if (olOptional.isPresent()) {
				OfferLetter offerLetter = olOptional.get();
				offerLetterDto = mapper.map(offerLetter, OfferLetterDto.class);
				SaleOrder saleOrder = offerLetter.getSaleOrder();
				if (saleOrder != null) {
					SupplyOrder supplyOrder = saleOrder.getSupplyOrder();
					String indentDate = dateFormat.format(supplyOrder.getOrderDate());
					offerLetterDto.setIndentDate(indentDate);
					String OrgName = supplyOrder.getOrganization().getOrganizationName();
					offerLetterDto.setOrganizationName(OrgName);
					Consignee consignee = offerLetter.getConsignee();
					if (consignee != null) {
						offerLetterDto.setConsigneeAddress(consignee.getAddress());
					}
					List<OlLineItemDto> lineItemDtos = new ArrayList<>();
					List<OlLineItem> olLineItems = offerLetter.getOlLineItems();

					for (OlLineItem olLineItem : olLineItems) {
						OlLineItemDto olLineItemDto = mapper.map(olLineItem, OlLineItemDto.class);
						SOLineItem soLineItem = olLineItem.getSoLineItem();
						olLineItemDto.setUnitCost(soLineItem.getUnitCost());
						olLineItemDto.setItemValue(olLineItem.getOfferedQty().multiply( soLineItem.getUnitCost()));
						olLineItemDto.setOrderedQty(soLineItem.getOrderQty());
						olLineItemDto.setDeliveredQty(
								offerLetterRepository.getDlvrddQtyByOlIdNSoLineItemId(olId, soLineItem.getId()));
						if (soLineItem.getUnitCost() != null) {
							olLineItemDto.setUnitCostStr(df.format(soLineItem.getUnitCost()));
						}
						lineItemDtos.add(olLineItemDto);

					}
					offerLetterDto.setOlLineItemDtos(lineItemDtos);
				}

			}
		} catch (Exception e) {
			logger.error("Exception in getting OL details for print: {}", e.getMessage());
		}
		return offerLetterDto;
	}

	public boolean updateOlStatus(int offerLetterId, int status) {

		Optional<OfferLetter> olOptional = offerLetterRepository.findById(offerLetterId);
		if (olOptional.isPresent()) {
			OfferLetter offerLetter = olOptional.get();
			Optional<OlStatus> olStatusOptional = olStatusRepository.findById(status);
			if (olStatusOptional.isPresent()) {
				OlStatus olStatus = olStatusOptional.get();
//				offerLetter.setIsNotified(true);
				offerLetter.setOlStatus(olStatus);
				offerLetterRepository.save(offerLetter);
				return true;
			} else {
				throw new DataNotFoundException("No OL Status found with given status Id: " + status);
			}
		} else {
			throw new DataNotFoundException("No OL found with given OL Id: " + offerLetterId);
		}

	}

	public Map<Boolean, String> updateOl(@Valid OfferLetterDto offerLetterDto) {

		Map<Boolean, String> returnMap = new HashMap<>();
		try {
			Optional<OfferLetter> olOptional = offerLetterRepository.findById(offerLetterDto.getId());
			if (olOptional.isPresent()) {
				Date date = new Date();
				OfferLetter offerLetter = olOptional.get();
				boolean status = offerLetterDto.getStatus().equalsIgnoreCase("Active");
				offerLetter.setActive(status);
				offerLetter.setModifiedDate(date);
				offerLetterRepository.save(offerLetter);
				returnMap.put(true, "Offer Letter updated successfully.");
			} else {
				String respString = "Offer Letter doesn't exist with name:" + offerLetterDto.getOlNumber();
				logger.error(respString);
				returnMap.put(false, respString);
			}
		} catch (Exception e) {
			logger.error("Error in updating Offer Letter {}", e.getMessage());
			return returnMap;
		}
		return returnMap;
	}

	public OfferLetterDto updateOlAttachments(OfferLetterDto offerLetterDto, MultipartFile[] attachments,
			MultipartFile[] preInspectionReports, MultipartFile[] qap, MultipartFile[] drawings, MultipartFile[] ateAtp,
			String olStatus) {

		try {
			Optional<OfferLetter> olOptional = offerLetterRepository.findById(offerLetterDto.getId());

			if (olOptional.isPresent()) {
				OfferLetter offerLetter = olOptional.get();
				getSavedOlAttachments(offerLetterDto, offerLetter);
				mapper.map(offerLetter, offerLetterDto);
				List<OfferLetterAttachment> olAttachments = new ArrayList<>();
				if (attachments != null && attachments.length > 0) {
					saveOlAttachment(attachments, "Attachment", offerLetter, offerLetterDto, olAttachments);
				}
				if (preInspectionReports != null && preInspectionReports.length > 0) {
					saveOlAttachment(preInspectionReports, "PreInspectionReport", offerLetter, offerLetterDto,
							olAttachments);
				}
				if (qap != null && qap.length > 0) {
					saveOlAttachment(qap, "QAP", offerLetter, offerLetterDto, olAttachments);
				}
				if (drawings != null && drawings.length > 0) {
					saveOlAttachment(drawings, "Drawings", offerLetter, offerLetterDto, olAttachments);
				}
				if (ateAtp != null && ateAtp.length > 0) {
					saveOlAttachment(ateAtp, "ATE/ATP", offerLetter, offerLetterDto, olAttachments);
				}
				olAttachments.addAll(offerLetter.getOlattachments());
				Date date = new Date();
				boolean status = olStatus.equalsIgnoreCase("Active");
				offerLetter.setActive(status);
				offerLetter.setModifiedDate(date);
				offerLetter.setOlattachments(olAttachments);
				offerLetterRepository.save(offerLetter);
			}
		} catch (Exception e) {
			logger.error("Error in  updating supply order attachments{}", e.getMessage());
		}
		return offerLetterDto;
	}

	private void saveOlAttachment(MultipartFile[] files, String category, OfferLetter offerLetter,
			OfferLetterDto offerLetterDto, List<OfferLetterAttachment> olAttachments) {

		Date date = new Date();
		List<String> uploadedFilesUrl = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			String fileName = fileStorageService.storeFile(files[i], offerLetter.getId(), i);
			if (StringUtils.isNotBlank(fileName) && !fileName.equalsIgnoreCase(ApplicationConstants.FAILURE)) {
				OfferLetterAttachment attachment = new OfferLetterAttachment();
				attachment.setCategory(category);
				attachment.setCreatedDate(date);
				attachment.setFileName(fileName);
				attachment.setOfferLetter(offerLetter);
				olAttachments.add(attachment);
				String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
						+ fileName;
				uploadedFilesUrl.add(fileDownloadUri);
			}
		}

		switch (category) {

		case "Attachment":
			List<String> olAttachmentsList = offerLetterDto.getOlAttachments();
			olAttachmentsList.addAll(uploadedFilesUrl);
			offerLetterDto.setOlAttachments(olAttachmentsList);
			break;

		case "PreInspectionReport":
			List<String> preInspectionReports = offerLetterDto.getPreInspectionReports();
			preInspectionReports.addAll(uploadedFilesUrl);
			offerLetterDto.setPreInspectionReports(preInspectionReports);
			break;

		default:
			break;

		}
	}

	public OfferLetterDto getSavedOlAttachments(OfferLetterDto offerLetterDto, OfferLetter offerLetter) {

		List<OfferLetterAttachment> OfferLetterAttachments = offerLetter.getOlattachments();

//		List<OfferLetterAttachment> qapAttachments = OfferLetterAttachments.stream()
//				.filter(s -> s.getCategory().equals("QAP")).collect(Collectors.toList());
		List<String> olAttachments = new ArrayList<>();
		List<String> preInspectionReports = new ArrayList<>();
		for (OfferLetterAttachment olAttachment : OfferLetterAttachments) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
					+ olAttachment.getFileName();
			if (olAttachment.getCategory().equalsIgnoreCase("Attachment")) {
				olAttachments.add(fileDownloadUri);
			} else if (olAttachment.getCategory().equalsIgnoreCase("PreInspectionReport")) {
				preInspectionReports.add(fileDownloadUri);
			}
		}
		offerLetterDto.setOlAttachments(olAttachments);
		offerLetterDto.setPreInspectionReports(preInspectionReports);
		return offerLetterDto;

	}

	public OlAttachmentDto getAttachmentsByOlId(int olId) {
		OlAttachmentDto olAttachmentDto = new OlAttachmentDto();
		List<String> olAttachments = new ArrayList<>();
		List<String> preInspectionReports = new ArrayList<>();
		List<String> qap = new ArrayList<>();
		List<String> drawings = new ArrayList<>();
		List<String> ateAtp = new ArrayList<>();
		List<Object[]> response = offerLetterAttachmentRepository.getByOlId(olId);
		for (Object[] obj : response) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
					+ obj[1].toString() + "\t" + obj[2].toString();
			switch (obj[0].toString()) {
			case "Attachment":
				olAttachments.add(fileDownloadUri);
				break;
			case "PreInspectionReport":
				preInspectionReports.add(fileDownloadUri);
				break;
			case "QAP":
				qap.add(fileDownloadUri);
				break;
			case "Drawings":
				drawings.add(fileDownloadUri);
				break;
			case "ATE/ATP":
				ateAtp.add(fileDownloadUri);
				break;
			default:
				break;
			}
			olAttachmentDto.setAttachments(olAttachments);
			olAttachmentDto.setPreInspectionReports(preInspectionReports);
			olAttachmentDto.setQap(qap);
			olAttachmentDto.setDrawings(drawings);
			olAttachmentDto.setAteAtp(ateAtp);
			olAttachmentDto.setCreatedDate(obj[2].toString());

		}
		return olAttachmentDto;
	}

	public OlAttachmentDto getAttachmentsByOlIds(List<Integer> olIds) {

		OlAttachmentDto olAttachmentDto = new OlAttachmentDto();
		List<String> olAttachments = new ArrayList<>();
		List<String> preInspectionReports = new ArrayList<>();
		for (int olId : olIds) {
			OlAttachmentDto attachmentDto = getAttachmentsByOlId(olId);
			if (attachmentDto != null) {
				if (attachmentDto.getAttachments() != null && !attachmentDto.getAttachments().isEmpty()) {
					olAttachments.addAll(attachmentDto.getAttachments());
				}
				if (attachmentDto.getPreInspectionReports() != null
						&& !attachmentDto.getPreInspectionReports().isEmpty()) {
					preInspectionReports.addAll(attachmentDto.getPreInspectionReports());
				}
			}
		}
		olAttachmentDto.setAttachments(olAttachments);
		olAttachmentDto.setPreInspectionReports(preInspectionReports);
		return olAttachmentDto;
	}

	public String deleteOlAttachment(int offerLetterId, String fileName) {

		String returnString = ApplicationConstants.FAILURE;
		OfferLetterAttachment olAttachment = offerLetterAttachmentRepository.getByOlIdNFileName(offerLetterId,
				fileName);
		if (olAttachment != null) {
			String fileDeletionStatus = fileStorageService.deleteFile(fileName);
			if (fileDeletionStatus.equals(ApplicationConstants.SUCCESS)) {
				offerLetterAttachmentRepository.delete(olAttachment);
				returnString = ApplicationConstants.SUCCESS;
			}
		}
		return returnString;
	}

	public String getSoNumberByOlId(int olId) {
		String soNumber = null;
		try {
			soNumber = offerLetterRepository.getSoNumberByOlId(olId);
			soNumber = (soNumber == null) ? null : soNumber;
		} catch (Exception e) {
			logger.error("Error in fetching so number by ol id {}, due to {}", olId, e.getMessage());
		}
		return soNumber;
	}

	public String getFileNoteStatusByOlId(int olId) {
		String status = offerLetterRepository.getFileNoteStatusByOlId(olId);
		return status == null ? "Pending" : status;
	}

	public List<OfferLetterDto> getOfferLettersByStatus(int orgId, int empId) {

		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getOLsByStatus(orgId, empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			offerletterDto.setId(Integer.parseInt(ol[0].toString()));
			offerletterDto.setOlNumber((ol[1].toString()));
			offerletterDto.setStatusId(ol[2] != null ? ol[2].toString() : null);
			offerletterDto.setOfferDate(ol[3].toString());
			offerletterDto.setSalesGroupName(ol[4].toString());
			offerletterDto.setProjectName(ol[5].toString());
			offerletterDto.setFileNoteStatus(ol[7] != null ? ol[7].toString() : null);
			offerletterDto.setSaleOrderNoStr(ol[8] != null ? ol[8].toString() : null);
			offerletterDto.setSoNumber(ol[9].toString());
			offerletterDto.setReceivedDate(ol[11] != null ? ol[11].toString() : null);
			offerletterDto.setIsNotified(ol[13] != null ? (Boolean) ol[13] : null);
			offerletterDto.setSoType(ol[14] != null ? ol[14].toString() : null);
			offerletterDto.setIsSurpriseCheckAllotted(ol[15] != null ? (boolean) ol[15] : null);
			offerletterDto.setIsCrossCheckAllotted(ol[16] != null ? (boolean) ol[16] : null);
			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	public List<OfferLetterDto> getOfferLettersByStatusForFirm(int orgId, int empId) {

		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getOLsByStatusForFirm(orgId, empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			offerletterDto.setId(Integer.parseInt(ol[0].toString()));
			offerletterDto.setOlNumber((ol[1].toString()));
			offerletterDto.setStatusId(ol[2] != null ? ol[2].toString() : null);
			offerletterDto.setOfferDate(ol[3].toString());
			offerletterDto.setSalesGroupName(ol[4].toString());
			offerletterDto.setProjectName(ol[5].toString());
			offerletterDto.setFileNoteStatus(ol[7] != null ? ol[7].toString() : null);
			offerletterDto.setSaleOrderNoStr(ol[8] != null ? ol[8].toString() : null);
			offerletterDto.setSoNumber(ol[9].toString());
			offerletterDto.setReceivedDate(ol[11] != null ? ol[11].toString() : null);
			offerletterDto.setIsNotified(ol[13] != null ? (Boolean) ol[13] : null);
			offerletterDto.setSoType(ol[14] != null ? ol[14].toString() : null);
			offerletterDto.setIsSurpriseCheckAllotted(ol[15] != null ? (boolean) ol[15] : null);
			offerletterDto.setIsCrossCheckAllotted(ol[16] != null ? (boolean) ol[16] : null);

			String allotetdToNames = olAllotmentRepository
					.getAllotedOlToEmployeeName(Integer.parseInt(ol[0].toString()));

			offerletterDto.setAllotedToName(allotetdToNames);

			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	public List<OfferLetterDto> getAllottedOfferLettersByStatusForFirm(int orgId, int empId) {

		List<Object[]> ols = new ArrayList<>();

//		ols = offerLetterRepository.getAllottedOLsByStatusFroFirm(empId);
		ols = offerLetterRepository.getAllottedOLsApprovalByStatusFroFirm(empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			int index = 0;
			offerletterDto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			offerletterDto.setOlNumber((ol[index].toString()));
			index++;
			offerletterDto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setNomenclature(ol[index] != null ? ol[index].toString() : null); // project name ==
																								// nomenclature
			index++;
			offerletterDto.setINoteTypeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setFileNoteStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null); // sale order no == work
																							// order no
			index++;
			offerletterDto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setEpCode(ol[index] != null ? ol[index].toString() : null); // EP code == part number
			index++;
			offerletterDto.setWarrant(ol[index] != null ? ol[index].toString() : null); // warrant == category
			index++;

			String allotetdToNames = olAllotmentRepository
					.getAllotedOlToEmployeeName(Integer.parseInt(ol[0].toString()));

			offerletterDto.setAllotedToName(allotetdToNames);

			dtos.add(offerletterDto);

		}
//		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	public List<OfferLetterDto> getAllottedOfferLettersApprovalByStatusForFirm(int orgId, int empId) {

		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getAllottedOLsApprovalByStatusFroFirm(empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			int index = 0;
			offerletterDto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			offerletterDto.setOlNumber((ol[index].toString()));
			index++;
			offerletterDto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setNomenclature(ol[index] != null ? ol[index].toString() : null); // project name ==
																								// nomenclature
			index++;
			offerletterDto.setINoteTypeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setFileNoteStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null); // sale order no == work
																							// order no
			index++;
			offerletterDto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setEpCode(ol[index] != null ? ol[index].toString() : null); // EP code == part number
			index++;
			offerletterDto.setWarrant(ol[index] != null ? ol[index].toString() : null); // warrant == category
			index++;

			String allotetdToNames = olAllotmentRepository
					.getAllotedOlToEmployeeName(Integer.parseInt(ol[0].toString()));

			offerletterDto.setAllotedToName(allotetdToNames);

			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	public List<OfferLetterDto> getNotifiedOfferLettersByStatusForFirm(int orgId, int empId) {

		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getNotifiedOLsByStatusForFirm(orgId, empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			offerletterDto.setId(Integer.parseInt(ol[0].toString()));
			offerletterDto.setOlNumber((ol[1].toString()));
			offerletterDto.setStatusId(ol[2] != null ? ol[2].toString() : null);
			offerletterDto.setOfferDate(ol[3].toString());
			offerletterDto.setSalesGroupName(ol[4].toString());
			offerletterDto.setProjectName(ol[5].toString());
			offerletterDto.setFileNoteStatus(ol[7] != null ? ol[7].toString() : null);
			offerletterDto.setSaleOrderNoStr(ol[8] != null ? ol[8].toString() : null);
			offerletterDto.setSoNumber(ol[9].toString());
			offerletterDto.setReceivedDate(ol[11] != null ? ol[11].toString() : null);
//			offerletterDto.setIsNotified(ol[13] != null ? (Boolean) ol[13] : null);
			offerletterDto.setSoType(ol[13] != null ? ol[13].toString() : null);
			offerletterDto.setIsSurpriseCheckAllotted(ol[14] != null ? (boolean) ol[14] : null);
			offerletterDto.setIsCrossCheckAllotted(ol[15] != null ? (boolean) ol[15] : null);
			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	// getOfferLettersForCqao

	public List<OfferLetterDto> getOfferLettersForCqao(int orgId) {

		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getOfferLettersForCqao(orgId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			offerletterDto.setId(Integer.parseInt(ol[0].toString()));
			offerletterDto.setOlNumber((ol[1].toString()));
			offerletterDto.setStatusId(ol[2] != null ? ol[2].toString() : null);
			offerletterDto.setOfferDate(ol[3].toString());
			offerletterDto.setSalesGroupName(ol[4].toString());
			offerletterDto.setProjectName(ol[5].toString());
			offerletterDto.setFileNoteStatus(ol[7] != null ? ol[7].toString() : null);
			offerletterDto.setSaleOrderNoStr(ol[8] != null ? ol[8].toString() : null);
			offerletterDto.setSoNumber(ol[9].toString());
			offerletterDto.setReceivedDate(ol[11] != null ? ol[11].toString() : null);
			offerletterDto.setIsNotified((Boolean) ol[13]);
			offerletterDto.setSoType(ol[14] != null ? ol[14].toString() : null);
			offerletterDto.setInoteApprovalRemarks(ol[15] != null ? ol[15].toString() : null);
			if (ol[16] != null)
				offerletterDto.setInoteApproval((Boolean) ol[16]);

			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
//		 Collections.sort(dtos, new FileNoteStatusComparator());
		return dtos;
	}

	public OfferLetterStatusDto getOLCount(int orgId) {
		OfferLetterStatusDto olStatusDto = new OfferLetterStatusDto();
		try {

			List<Object[]> ols = offerLetterRepository.getOLCount(orgId);

			int countProgress = 0;
			int countPending = 0;
			for (Object[] ol : ols) {
				if (ol[0] != null) {
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_PENDING) {

						countPending = countPending + Integer.parseInt(ol[1].toString());

					}
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_CLEARED) {
						olStatusDto.setCompleted(Integer.parseInt(ol[1].toString()));
					}
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_APPROVED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_COMPLETED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_REJECTED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_INPROGRESS) {
						countProgress = countProgress + Integer.parseInt(ol[1].toString());
					}
				}

				if (ol[0] == null) {
					countPending = countPending + Integer.parseInt(ol[1].toString());
				}
			}
			olStatusDto.setPending(countPending);
			olStatusDto.setInProgress(countProgress);

		} catch (Exception e) {
			logger.error("Error in fetching  ol  due to {}", e.getMessage());
		}
		return olStatusDto;
	}

	// getOLStatusBySalesGrpId
	public List<OfferLetterDto> getOfferLetterStatusBySalesGrpId(int salesGrpId) {
		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getOLStatusBySalesGrpId(salesGrpId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			offerletterDto.setId(Integer.parseInt(ol[0].toString()));
			offerletterDto.setOlNumber((ol[1].toString()));
			offerletterDto.setStatusId(ol[2] != null ? ol[2].toString() : null);
			offerletterDto.setOfferDate(ol[3] != null ? ol[3].toString() : null);
			offerletterDto.setSalesGroupName(ol[4] != null ? ol[4].toString() : null);
			offerletterDto.setProjectName(ol[5] != null ? ol[5].toString() : null);
			offerletterDto.setOlStatus(ol[7] != null ? ol[7].toString() : null);
			offerletterDto.setINoteTypeId(ol[6] != null ? (int) ol[6] : null);
			offerletterDto.setInspectionLevelStr(ol[8] != null ? ol[8].toString() : null);
			offerletterDto.setSoNumber(ol[9] != null ? ol[9].toString() : null);
			offerletterDto.setSaleOrderId(ol[10] != null ? Integer.parseInt(ol[10].toString()) : null);
			offerletterDto.setSaleOrderNoStr(ol[11] != null ? ol[11].toString() : null);
			offerletterDto.setIsNotified(ol[12] != null ? (Boolean) ol[12] : false);
			offerletterDto.setIsGcc(ol[13] != null ? (Boolean) ol[13] : false);
			offerletterDto.setSoId(ol[14] != null ? Integer.parseInt(ol[14].toString()) : null);
			offerletterDto.setConsigneeId(ol[15] != null ? Integer.parseInt(ol[15].toString()) : null);

			String allotetdToNames = olAllotmentRepository
					.getAllotedOlToEmployeeName(Integer.parseInt(ol[0].toString()));

			offerletterDto.setAllotedToName(allotetdToNames);

			dtos.add(offerletterDto);

		}
		return dtos;
	}

	// Manasa draft version 30-01-2026
	// getOLStatusBySalesGrpId
	public List<OfferLetterDto> getOfferLetterStsBySalesGrpId(int salesGrpId) {
		List<Object[]> ols = new ArrayList<>();

		ols = offerLetterRepository.getOLBySalesGrpId(salesGrpId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			int index = 0;
			offerletterDto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			offerletterDto.setOlNumber((ol[index].toString()));
			index++;
			offerletterDto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setINoteTypeId(ol[index] != null ? (int) ol[index] : null);
			index++;
			offerletterDto.setOlStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setInspectionLevelStr(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setIsNotified(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			offerletterDto.setIsGcc(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			offerletterDto.setSoId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setConsigneeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setWarrant(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setEpCode(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setNomenclature(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);
			// offerletterDto.setConsigneeId(ol[15] != null ?
			// Integer.parseInt(ol[15].toString()) : null);

			// String allotetdToNames =
			// olAllotmentRepository.getAllotedOlToEmployeeName(Integer.parseInt(ol[0].toString()));

			// offerletterDto.setAllotedToName(allotetdToNames);

			dtos.add(offerletterDto);

		}
		return dtos;
	}

	public OfferLetterStatusDto getOLCountBySalesGrp(int salesGrpId) {
		OfferLetterStatusDto olStatusDto = new OfferLetterStatusDto();
		try {

			List<Object[]> ols = offerLetterRepository.getOLCountBySalesGrpId(salesGrpId);

			int countProgress = 0;
			int countPending = 0;
			for (Object[] ol : ols) {
				if (ol[0] != null) {
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_PENDING) {

						countPending = countPending + Integer.parseInt(ol[1].toString());

					}
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_CLEARED) {
						olStatusDto.setCompleted(Integer.parseInt(ol[1].toString()));
					}
					if (Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_APPROVED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_COMPLETED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_REJECTED
							|| Integer.parseInt(ol[0].toString()) == ApplicationConstants.OL_INPROGRESS) {
						countProgress = countProgress + Integer.parseInt(ol[1].toString());
					}
				}

				if (ol[0] == null) {
					countPending = countPending + Integer.parseInt(ol[1].toString());
				}
			}
			olStatusDto.setPending(countPending);
			olStatusDto.setInProgress(countProgress);
		} catch (Exception e) {
			logger.error("Error in fetching  ol  due to {}", e.getMessage());
		}
		return olStatusDto;
	}

	public OfferLetter updateOLNotifyStatus(OfferLetter ol) {
		ol.setIsNotified(true);
		return offerLetterRepository.save(ol);
	}

	public List<Map<String, Object>> getReturnedNClearedOlsDGQADashboard(int orgId, int category, int categoryValue)
			throws Exception {

		List<Map<String, Object>> olChartValuePairs = new LinkedList<>();
		// Getting current financial year start and end date
		LocalDate fyStartDate = LocalDate.of(categoryValue - 1, 4, 1); // Financial year starts on April 1st of
		// selectedFY
		LocalDate fyEndDate = LocalDate.of(categoryValue, 3, 31);

		List<Object[]> returnedNClearedOlList = offerLetterRepository.getReturnedNClearedOlsDashboard(orgId,
				fyStartDate, fyEndDate);

		List<String> availableMonthsInResult = new ArrayList<>();
		if (!returnedNClearedOlList.isEmpty()) {

			for (Object[] returnedNClearedOlObj : returnedNClearedOlList) {

				availableMonthsInResult.add(returnedNClearedOlObj[0].toString());

				Map<String, Object> olChartMap = new LinkedHashMap<>();
				olChartMap.put("month", returnedNClearedOlObj[0].toString());
				olChartMap.put("returned", new BigDecimal(returnedNClearedOlObj[2].toString()).intValue());
				olChartMap.put("cleared", new BigDecimal(returnedNClearedOlObj[1].toString()).intValue());

				olChartValuePairs.add(olChartMap);
			}
		}

		List<String> sortedMonthsList = Arrays.asList("APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
				"JAN", "FEB", "MAR");

		for (String monthName : sortedMonthsList) {
			if (!availableMonthsInResult.contains(monthName)) {

				Map<String, Object> olChartMap = new LinkedHashMap<>();
				olChartMap.put("month", monthName);
				olChartMap.put("returned", 0);
				olChartMap.put("cleared", 0);

				olChartValuePairs.add(olChartMap);
			}
		}
		FYMonthSorter.sortList(olChartValuePairs, sortedMonthsList);
		return olChartValuePairs;
	}

	public SupplyOrder getSupplyOrderByOlIds(List<Integer> olIds) {
		return supplyOrderRepository.getSupplyOrderByOlIds(olIds);
	}

	public List<OfferLetter> getOfferLettersByIds(List<Integer> olIds) {
		return offerLetterRepository.findAllById(olIds);
	}

	public List<Object[]> getLineItemQtyNUomByOlIds(List<Integer> olIds) {
		return olLineItemRepository.getLineItemQtyNUomByOlIds(olIds);
	}

	/**
	 * To generate the INote data based on file note id
	 *
	 * @param fileNoteId
	 * @return INoteDto
	 * @author Awadhesh Verma
	 */
	public INoteDto getINoteByFileNoteId(int fileNoteId) {

		INote existing = iNoteRepository.getByFilenoteId(fileNoteId);
		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (INoteLineItem item : existing.getINoteLineItems()) {
				INoteLineItemDto dto = mapper.map(item, INoteLineItemDto.class);
				// dto.setItemRemarks(olLineItem.getHologramRemarks());
				iNoteLineItemDtos.add(dto);
			}
			int supplyOrderId = 0;
			Optional<InspectionFileNote> fileNoteOptional = inspectionFileNoteRepository.findById(fileNoteId);
			if (fileNoteOptional.isPresent()) {
				InspectionFileNote inspectionFileNote = fileNoteOptional.get();
//			if (inspectionFileNote.getCqaoApprovalDate() != null) {
//				iNoteDto.setCqaoApprovalDate(cqaoDateFormat.format(inspectionFileNote.getCqaoApprovalDate()));
//			}
				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				iNoteDto.setINoteLineItems(iNoteLineItemDtos);
				iNoteDto.setExisting(true);
				iNoteDto.setRemarks(existing.getRemarks());
				iNoteDto.setOlDateStr(dateFormat.format(inspectionFileNote.getOlDate()));
				supplyOrderId = inspectionFileNote.getSupplyOrderId();

				supplyOrderId = inspectionFileNote.getSupplyOrderId();
			}
			iNoteDto.setAmendment(iNoteDto.getAmendment());
			List<Object[]> prevINoteDetails = iNoteRepository.getPrevInoteByInoteId(supplyOrderId, existing.getId());


			Optional<SupplyOrder> so = supplyOrderRepository.findById(supplyOrderId);
			if (so.isPresent()) {
				SupplyOrder supplyOrder = so.get();
				iNoteDto.setSoDescription(supplyOrder.getDescription());

			}
			return iNoteDto;
		} else {
			return getNewINoteByFileNoteId(fileNoteId);
		}
	}

	/**
	 * @param fileNoteId
	 * @return INoteDto
	 * @author Awadhesh Verma
	 */
	public INoteDto getNewINoteByFileNoteId(int fileNoteId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SupplyOrder supplyOrder = supplyOrderRepository.getSupplyOrderByFileNoteId(fileNoteId);
		if (supplyOrder != null) {

			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
			iNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
			iNoteDto.setSoDescription(supplyOrder.getDescription());

			Organization organization = supplyOrder.getOrganization();
			// Generation of I-Note no based on STD code, serial no for the FY and CQAE
			// naval code
			String stdCode = organization.getStdCode();
			String inoteCode = organization.getInoteCode();
			Integer inoteSerialStart = organization.getInoteSerialStrt();
			String inoteSerialStartStr = inoteSerialStart != null ? String.valueOf(inoteSerialStart) : "0000";

			Integer inoteCountInCurrentFY = iNoteRepository.getInoteCountInCurrentFY(null) == null ? 0
					: iNoteRepository.getInoteCountInCurrentFY(null);
			String inoteNo = "";
			if (!inoteSerialStartStr.equals("0000")) {
				inoteNo = stdCode + ((Integer.parseInt(inoteSerialStartStr) + inoteCountInCurrentFY + 1)) + inoteCode;
			} else {
				inoteNo = stdCode + (inoteSerialStartStr + inoteCountInCurrentFY + 1) + inoteCode;
			}
			iNoteDto.setINoteNo(inoteNo);

			Optional<InspectionFileNote> inspectionFileNoteOptional = inspectionFileNoteRepository.findById(fileNoteId);
			if (inspectionFileNoteOptional.isPresent()) {
				InspectionFileNote inspectionFileNote = inspectionFileNoteOptional.get();
				iNoteDto.setCreatedDate(inspectionFileNote.getInspectionCompleteDate() != null
						? dateFormat.format(inspectionFileNote.getInspectionCompleteDate())
						: null);
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));

				int createdBy = inspectionFileNote.getCreatedBy();
				String sealNo = empRepository.getInspSealNoByEmpId(createdBy);
				iNoteDto.setSealNo(sealNo);

				List<OfferLetter> offerLetters = inspectionFileNote.getOfferLetters();
				String olNumbers = offerLetters.stream().map(OfferLetter::getOlNumber).collect(Collectors.joining(","));
				String storeDescrption = offerLetters.stream().map(OfferLetter::getStoresDescription)
						.filter(Objects::nonNull).collect(Collectors.joining(","));
				String olRemarks = offerLetters.stream().map(OfferLetter::getRemarks).filter(Objects::nonNull)
						.collect(Collectors.joining(","));
				Set<String> offerDateSet = new HashSet<>();
				List<OlLineItem> olLineItems = new ArrayList<>();
				for (OfferLetter offerLetter : offerLetters) {
					if (offerLetter.getOfferDate() != null) {
						Date offerDate = offerLetter.getOfferDate();
						offerDateSet.add(dateFormat.format(offerDate));
					}
					olLineItems.addAll(offerLetter.getOlLineItems());
				}
				// Assuming consignee of all offer letters are same
				// String consigneeAddress = offerLetters.get(0).getConsignee().getAddress();
				if (!offerLetters.isEmpty()) {
					Consignee consignee = offerLetters.get(0).getConsignee();
					if (consignee != null) {
						String consigneeAddress = consignee.getAddress();
						iNoteDto.setConsignee(consigneeAddress);
					}
				}
				iNoteDto.setOfferLetterNo(olNumbers);
				iNoteDto.setOlDateStr(String.join(",", offerDateSet));
				iNoteDto.setDescription(storeDescrption);
				iNoteDto.setRemarks(olRemarks);

				// Getting last I-Note details By Supply Order Id
				List<Object[]> lastINoteDetails = iNoteRepository.getLastInoteDetailsBySoId(supplyOrder.getId());

				if (!lastINoteDetails.isEmpty()) {
					iNoteDto.setPrevINoteNo(
							lastINoteDetails.get(0)[0] != null ? lastINoteDetails.get(0)[0].toString() : null);
					iNoteDto.setPrevINoteDate(
							lastINoteDetails.get(0)[1] != null ? lastINoteDetails.get(0)[1].toString() : null);
				}

				List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
				for (OlLineItem olLineItem : olLineItems) {
					BigDecimal tendered = olLineItem.getOfferedQty();
					BigDecimal accepted = olLineItem.getClearedQty() != null ? olLineItem.getClearedQty() : tendered;

					BigDecimal rejected = tendered.subtract(accepted);


					INoteLineItemDto dto = new INoteLineItemDto();
					SOLineItem soLineItem = olLineItem.getSoLineItem();
					dto.setPartNo(soLineItem.getPartNo());
					if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
						dto.setPartNumberId(soLineItem.getPartNumber().getId());
						dto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
						dto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
					}
					dto.setDescription(soLineItem.getDescription());
					dto.setOrderedQty(soLineItem.getOrderQty());
					dto.setOlLineItemId(olLineItem.getId());
					dto.setUom(soLineItem.getUom() != null ? soLineItem.getUom().getUnit() : null);
					dto.setTendered(tendered);
					dto.setAccepted(accepted);
					dto.setRejected(rejected);
					dto.setLedgerFolio(iNoteRepository.getTotalAcceptedQtyforSOLineItem(soLineItem.getId()));

					dto.setIndentNo(soLineItem.getLineItemNo());
					dto.setItemRemarks(olLineItem.getHologramRemarks());
					dto.setQtyAcceptedToDate(
							offerLetterRepository.getCountOfCompletedLineItemsBySoLiId(soLineItem.getId()));
					iNoteLineItemDtos.add(dto);
				}
				iNoteDto.setINoteLineItems(iNoteLineItemDtos);

			}

			List<DdExtension> ddExtensions = supplyOrder.getDdExtensions();
			if (!ddExtensions.isEmpty()) {

				Collections.sort(ddExtensions, (o1, o2) -> o2.getNewDdDate().compareTo(o1.getNewDdDate()));
				DdExtension latestDdExt = ddExtensions.get(0);
				// iNoteDto.setNewDdDate(iNoteDateFormat.format(latestDdExt.getNewDdDate()));
				iNoteDto.setDdExtLetterDate(iNoteDateFormat.format(latestDdExt.getLetterDate()));
				iNoteDto.setDdExtLetterNo(latestDdExt.getLetterNo());
			}
			List<AmendmentsLetter> amendments = supplyOrder.getAmendmentsLetters();
			if (!amendments.isEmpty()) {

				Collections.sort(amendments, (e1, e2) -> e2.getLetterDate().compareTo(e1.getLetterDate()));
				String amendment = "";
				for (AmendmentsLetter amdt : amendments) {

					amendment = amendment.concat(
							amdt.getLetterNo() + "  dated   " + iNoteDateFormat.format(amdt.getLetterDate()) + ",");

				}

				iNoteDto.setAmendment(amendment);

			}
			SalesGroup salesGroup = supplyOrder.getSalesGroup();
			if (salesGroup != null) {
				Organization salesGrpOrganization = salesGroup.getOrganization();
				iNoteDto.setContractorName(salesGrpOrganization.getOrganizationName());
				iNoteDto.setContractorAddress(salesGrpOrganization.getAddress());
			}

			iNoteDto.setIndentor(supplyOrder.getIndenter().getAddress());

			iNoteDto.setIndentNo(supplyOrder.getIndentNo());
			iNoteDto.setIndentDate(supplyOrder.getIndentDate());
			iNoteDto.setOrgName(supplyOrder.getOrganization().getAddress());

			iNoteDto.setExisting(false);

			return iNoteDto;
		} else
			return null;
	}

	/**
	 * To save the INote based on the file noting Id
	 *
	 * @param iNoteDto
	 * @return String
	 * @throws Exception
	 * @author Awadhesh Verma
	 */
	@Transactional(propagation = Propagation.NESTED)
	public String saveINoteV2(INoteDto iNoteDto) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat prevINoteDateFormatter = new SimpleDateFormat("dd-MM-yyyy");


		Date todayDate = new Date();
		INote iNote = mapper.map(iNoteDto, INote.class);
		iNote.setDescription(iNoteDto.getDescription());

		// description validation
		if (StringUtils.isNotBlank(iNoteDto.getDescription())) {
			if (!InputNameValidation.checkString(iNoteDto.getDescription())) {
				throw new DataNotValidException("Invalid description");
			}
		}

		if (iNoteDto.getCqaoApprovalDate() != null) {
			Date cqaoApprovalDate = formatter.parse(iNoteDto.getCqaoApprovalDate());
			iNote.setInoteDate(cqaoApprovalDate);
		}
		iNote.setRemarks(iNoteDto.getRemarks());

		// remarks validation
		if (StringUtils.isNotBlank(iNoteDto.getRemarks())) {
			if (!InputNameValidation.checkRemarks(iNoteDto.getRemarks())) {
				throw new DataNotValidException("Invalid remarks");
			}
		}

		if (iNoteDto.getCqaoApprovalDate() != null) {
			Date cqaoApprovalDate = formatter.parse(iNoteDto.getCqaoApprovalDate());
			iNote.setInoteDate(cqaoApprovalDate);
		}
		iNote.setRemarks(iNoteDto.getRemarks());
		iNote.setCreatedDatetime(todayDate);
		iNote.setOfferLetterDate(iNote.getOfferLetterDate());
		iNote.setINoteNo(iNoteDto.getINoteNo());

		List<INoteLineItem> iNoteLineItems = new ArrayList<>();
		for (INoteLineItemDto dto : iNoteDto.getINoteLineItems()) {
			String itemRemarks = dto.getItemRemarks();
			String remarks = dto.getRemarks();

			if (StringUtils.isNotBlank(remarks) && !InputNameValidation.checkRemarks(remarks)) {
				throw new DataNotValidException("Invalid remarks");
			}

			if (StringUtils.isNotBlank(itemRemarks) && !InputNameValidation.checkRemarks(itemRemarks)) {
				throw new DataNotValidException("Invalid item remarks");

			}
			INoteLineItem item = mapper.map(dto, INoteLineItem.class);
			item.setINote(iNote);
			item.setCreatedDatetime(todayDate);
			iNoteLineItems.add(item);
		}
		iNoteRepository.save(iNote);
		iNoteLineItemRepository.saveAll(iNoteLineItems);

		updateOlStatus(iNoteDto.getOlId(), ApplicationConstants.OL_CLEARED);

		// Updating remaining qty of OL Line Items
		try {
			List<OlLineItem> olLineItems = new ArrayList<>();
			for (INoteLineItemDto dto : iNoteDto.getINoteLineItems()) {
				Optional<OlLineItem> olLineItemOptional = olLineItemRepository.findById(dto.getOlLineItemId());
				if (olLineItemOptional.isPresent()) {
					OlLineItem olLineItem = olLineItemOptional.get();
					BigDecimal alreadyClearedQty = olLineItem.getClearedQty() == null ? BigDecimal.ZERO : olLineItem.getClearedQty();
					olLineItem.setClearedQty(alreadyClearedQty.add(dto.getAccepted()));
					olLineItems.add(olLineItem);
				}
			}
			olLineItemRepository.saveAll(olLineItems);
		} catch (Exception e) {
			logger.error("Error in updating remaining qty");
		}
		return ApplicationConstants.SUCCESS;
	}

	public List<OfferLetterDto> getAllOLs() {

		List<Object[]> offerLetters = offerLetterRepository.getOLs();
		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
	

		for (Object[] objects : offerLetters) {

			OfferLetterDto offerLetterDto = new OfferLetterDto();
			offerLetterDto.setId((int) objects[0]);
			offerLetterDto.setSoId((int) objects[1]);
			offerLetterDto.setSoNumber(objects[2].toString());
			offerLetterDto.setName(objects[3].toString());
			offerLetterDto.setOfferDate(objects[4] != null ? objects[4].toString() : null);
			offerLetterDto.setSalesGroupName(objects[5].toString());
			offerLetterDto.setProjectName(objects[6] != null ? objects[6].toString() : null);

			offerLetterDtos.add(offerLetterDto);

		}

		return offerLetterDtos;
	}

	public boolean updateOlStatusV2(int fileNoteId, int status) {

		List<Integer> ids = offerLetterRepository.getOLIds(fileNoteId);
		List<OfferLetter> offerLetters = offerLetterRepository.findAllById(ids);
		for (OfferLetter ol : offerLetters) {
			Optional<OlStatus> olStatusOptional = olStatusRepository.findById(status);
			if (olStatusOptional.isPresent()) {
				OlStatus olStatus = olStatusOptional.get();
				ol.setOlStatus(olStatus);
				offerLetterRepository.save(ol);
				return true;
			} else {
				throw new DataNotFoundException("No OL Status found with given status Id: " + status);
			}
		}
		return false;
	}

	// getAllINoteByFileNoteId
	public List<InspectionFileNoteDto> getAllINoteByOrgId(int orgId) {

		List<Object[]> objs = offerLetterRepository.getOlApprovalDetailsByOrgId(orgId);
		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setInternalInoteId((int) obj[0]);
			dto.setInoteId(obj[1] != null ? (int) obj[1] : null);
			dto.setOlNumber(obj[2] != null ? obj[2].toString() : "");
			dto.setOlDateStr(obj[3] != null ? obj[3].toString() : "");
			dto.setSalesGrpName(obj[4] != null ? obj[4].toString() : "");
			dto.setProjectDescription(obj[5] != null ? obj[5].toString() : "");
			dto.setINoteDate(obj[6] != null ? obj[6].toString() : "");
			dto.setINoteTypeId(obj[7] != null ? (int) obj[7] : 0);
			dto.setInoteStatus(obj[8] != null ? obj[8].toString() : "");
			dto.setSupplyOrderNo(obj[9] != null ? obj[9].toString() : "");
//			dto.setConsigneeName(obj[10] != null ? obj[10].toString() : "NA");
			dto.setConsigneeAddr(obj[10] != null ? obj[10].toString() : "NA");
			dto.setOfferLetterId(obj[11] != null ? (int) obj[11] : null);
			dto.setSupplyOrderId(obj[12] != null ? (int) obj[12] : null);
			dto.setInspectionAgency(obj[13] != null ? obj[13].toString() : "");
			inotesList.add(dto);

		}
		return inotesList;

	}

	// getAllINoteByFileNoteId
	public List<InspectionFileNoteDto> getAllINoteByOrgIdV2(Integer orgId) {

		List<Object[]> objs = offerLetterRepository.getOlApprovalDetailsByOrgIdV3(orgId);
		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

				InspectionFileNoteDto dto = new InspectionFileNoteDto();
				dto.setInternalInoteId((int) obj[0]);
				dto.setInoteId(obj[1] != null ? (int) obj[1] : null);
				dto.setOlNumber(obj[2] != null ? obj[2].toString() : "");
				dto.setOlDateStr(obj[3] != null ? obj[3].toString() : "");
				dto.setSalesGrpName(obj[4] != null ? obj[4].toString() : "");
				dto.setProjectDescription(obj[5] != null ? obj[5].toString() : "");
				dto.setINoteDate(obj[6] != null ? obj[6].toString() : "");
				dto.setINoteTypeId(obj[7] != null ? (int) obj[7] : 0);
				dto.setInoteStatus(obj[8] != null ? obj[8].toString() : "");
				dto.setSupplyOrderNo(obj[9] != null ? obj[9].toString() : "");
//				dto.setConsigneeName(obj[10] != null ? obj[10].toString() : "NA");
				dto.setConsigneeAddr(obj[10] != null ? obj[10].toString() : "NA");
				dto.setOfferLetterId(obj[11] != null ? (int) obj[11] : null);
				dto.setSupplyOrderId(obj[12] != null ? (int) obj[12] : null);
				dto.setInspectionAgency(obj[13]!=null ? obj[13].toString() : "");
//				if((int)obj[17] == ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER ) {
//					dto.setAcceptedQty(new BigDecimal(obj[16].toString()) );
//					dto.setRejectedQty((obj[15]!=null && (int)obj[15]!=0)? new BigDecimal(obj[16].toString()): BigDecimal.ZERO);
//				}else {
					dto.setAcceptedQty(obj[14] != null ? new BigDecimal(obj[14].toString()) : null);
			        dto.setRejectedQty(obj[15] != null ? new BigDecimal(obj[15].toString()) : null);
//				}

//				dto.setRfr(obj[16] != null ? (int) obj[16] : null);
			int rejQty = (int) obj[15];
//			int rfrQty = (int) obj[16];
//			int totalrejQty= rejQty + rfrQty;
//			dto.setRejectedQty(totalrejQty);
			inotesList.add(dto);

		}
		return inotesList;

	}

	public List<InspectionFileNoteDto> getAllINoteForFirm(int orgId) {

		List<Object[]> objs = offerLetterRepository.getInotesForFirm(orgId);
		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setOfferLetterId((int) obj[0]);
			dto.setInoteId(obj[1] != null ? (int) obj[1] : null);
			dto.setOlNumber(obj[2] != null ? obj[2].toString() : "");
			dto.setOlDateStr(obj[3] != null ? obj[3].toString() : "");
			dto.setSalesGrpName(obj[4] != null ? obj[4].toString() : "");
			dto.setProjectDescription(obj[5] != null ? obj[5].toString() : "");
			dto.setINoteDate(obj[6] != null ? obj[6].toString() : "");
			dto.setINoteTypeId(obj[7] != null ? (int) obj[7] : 0);
			dto.setInoteStatus(obj[8] != null ? obj[8].toString() : "");
			dto.setSupplyOrderNo(obj[9] != null ? obj[9].toString() : "");
			dto.setConsigneeName(obj[10] != null ? obj[10].toString() : "NA");
			dto.setConsigneeAddr(obj[11] != null ? obj[11].toString() : "NA");
			inotesList.add(dto);

		}
		return inotesList;

	}

	public List<InspectionFileNoteDto> getAllottedINoteForFirm(int orgId, int empId) {

		List<Object[]> objs = offerLetterRepository.getAllottedInotesForFirm(orgId, empId);
		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setOfferLetterId((int) obj[0]);
			dto.setInoteId(obj[1] != null ? (int) obj[1] : null);
			dto.setOlNumber(obj[2] != null ? obj[2].toString() : "");
			dto.setOlDateStr(obj[3] != null ? obj[3].toString() : "");
			dto.setSalesGrpName(obj[4] != null ? obj[4].toString() : "");
			dto.setProjectDescription(obj[5] != null ? obj[5].toString() : "");
			dto.setINoteDate(obj[6] != null ? obj[6].toString() : "");
			dto.setINoteTypeId(obj[7] != null ? (int) obj[7] : 0);
			dto.setInoteStatus(obj[8] != null ? obj[8].toString() : "");
			dto.setSupplyOrderNo(obj[9] != null ? obj[9].toString() : "");
			dto.setConsigneeName(obj[10] != null ? obj[10].toString() : "NA");
			dto.setConsigneeAddr(obj[11] != null ? obj[11].toString() : "NA");
			dto.setSupplyOrderId(obj[12] != null ? Integer.parseInt(obj[12].toString()) : null);
			dto.setInspectionAgency(obj[13] != null ? obj[13].toString() : "");
			inotesList.add(dto);

		}
		return inotesList;

	}

	public List<InspectionFileNoteDto> getAllottedINote(int orgId, int empId) {

		List<Object[]> objs = offerLetterRepository.getAllottedInotes(orgId, empId);
		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setOfferLetterId((int) obj[0]);
			dto.setInoteId(obj[1] != null ? (int) obj[1] : null);
			dto.setOlNumber(obj[2] != null ? obj[2].toString() : "");
			dto.setOlDateStr(obj[3] != null ? obj[3].toString() : "");
			dto.setSoType(obj[4] != null ? obj[4].toString() : "NA");
			dto.setSalesGrpName(obj[5] != null ? obj[5].toString() : "");
			dto.setProjectDescription(obj[6] != null ? obj[6].toString() : "");
			dto.setINoteDate(obj[7] != null ? obj[7].toString() : "");
			dto.setINoteTypeId(obj[8] != null ? (int) obj[8] : 0);
			dto.setInoteStatus(obj[9] != null ? obj[9].toString() : "");
			dto.setSupplyOrderNo(obj[10] != null ? obj[10].toString() : "");
			dto.setConsigneeName(obj[11] != null ? obj[11].toString() : "NA");
			dto.setConsigneeAddr(obj[12] != null ? obj[12].toString() : "NA");
			inotesList.add(dto);

		}
		return inotesList;

	}

	public List<OfferLetter> getNotifiedOLsBySOId(int supplyOrderId) {

		List<OfferLetter> offerLetters = offerLetterRepository.getNotifiedOLs(supplyOrderId);
		return offerLetters;
	}

	public List<InspectionFileNoteDto> getAllINotesBySalesGrpId(int salesGrpId) {
		List<Object[]> objs = offerLetterRepository.getINotesBySalesGrpId(salesGrpId);

		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setId((int) obj[0]);
			dto.setOlNumber(obj[1].toString());
			dto.setOlDateStr(obj[2].toString());
			dto.setSalesGrpName(obj[3].toString());
			dto.setProjectDescription(obj[4].toString());
			dto.setINoteDate(obj[5] != null ? obj[5].toString() : null);
			dto.setINoteTypeId((int) obj[6]);
			List<Integer> intIdsArray = Arrays.stream(obj[8].toString().split(",")).map(Integer::parseInt)
					.collect(Collectors.toList());
			;
			;
			dto.setOfferLetterIds(intIdsArray);
			dto.setInoteStatus(obj[9].toString());
			dto.setSupplyOrderNo(obj[10].toString());
			dto.setConsigneeName(obj[12] != null ? obj[12].toString() : "NA");
			dto.setConsigneeAddr(obj[13] != null ? obj[13].toString() : "NA");
			// Integer
			// clearanceId=clearanceLetterRepository.getByOlId(offerLetterDto.getId());
			boolean status = obj[7] != null ? true : false;
			dto.setClearanceStatus(status);
			inotesList.add(dto);

		}
		return inotesList;

	}

	public List<InspectionFileNoteDto> getAllGCINotesBySalesGrpId(int salesGrpId) {
		List<Object[]> objs = offerLetterRepository.getINotesBySalesGrpId(salesGrpId);

		List<InspectionFileNoteDto> inotesList = new ArrayList<>();
		for (Object[] obj : objs) {

			InspectionFileNoteDto dto = new InspectionFileNoteDto();
			dto.setId((int) obj[0]);
			dto.setOlNumber(obj[1].toString());
			dto.setOlDateStr(obj[2].toString());
			dto.setSalesGrpName(obj[3].toString());
			dto.setProjectDescription(obj[4].toString());
			dto.setINoteTypeId((int) obj[6]);
			if (obj[8] != null) {
				List<Integer> intIdsArray = Arrays.stream(obj[8].toString().split(",")).map(Integer::parseInt)
						.collect(Collectors.toList());
				dto.setOfferLetterIds(intIdsArray);
			}
			dto.setInoteStatus(obj[9].toString());
			// Integer
			// clearanceId=clearanceLetterRepository.getByOlId(offerLetterDto.getId());
			boolean status = obj[7] != null ? true : false;
			dto.setClearanceStatus(status);
			inotesList.add(dto);

		}
		return inotesList;

	}

	// for draft inote
	public INoteDto getINoteByOLId(int olId) {

		INote existing = iNoteRepository.getByOlId(olId);
		if (existing != null) {
			INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
			if (existing.getCreatedDatetime() != null) {
				iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
			}
			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (INoteLineItem item : existing.getINoteLineItems()) {
				INoteLineItemDto dto = mapper.map(item, INoteLineItemDto.class);
				// dto.setItemRemarks(olLineItem.getHologramRemarks());
				iNoteLineItemDtos.add(dto);
			}
			int supplyOrderId = 0;
			Optional<InspectionFileNote> fileNoteOptional = inspectionFileNoteRepository.findById(olId);
			if (fileNoteOptional.isPresent()) {
				InspectionFileNote inspectionFileNote = fileNoteOptional.get();
//				if (inspectionFileNote.getCqaoApprovalDate() != null) {
//					iNoteDto.setCqaoApprovalDate(cqaoDateFormat.format(inspectionFileNote.getCqaoApprovalDate()));
//				}
				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				iNoteDto.setINoteLineItems(iNoteLineItemDtos);
				iNoteDto.setExisting(true);
				iNoteDto.setRemarks(existing.getRemarks());
				iNoteDto.setOlDateStr(dateFormat.format(inspectionFileNote.getOlDate()));
				supplyOrderId = inspectionFileNote.getSupplyOrderId();
//	            int createdBy = inspectionFileNote.getCreatedBy();
//	            String sealNo = empRepository.getInspSealNoByEmpId(createdBy);
//	            iNoteDto.setSealNo(sealNo);
				supplyOrderId = inspectionFileNote.getSupplyOrderId();
			}
			iNoteDto.setAmendment(iNoteDto.getAmendment());
			List<Object[]> prevINoteDetails = iNoteRepository.getPrevInoteByInoteId(supplyOrderId, existing.getId());
//				if (!prevINoteDetails.isEmpty()) {
//					iNoteDto.setPrevINoteNo(prevINoteDetails.get(0)[1] != null?prevINoteDetails.get(0)[0].toString():"NA");
//					iNoteDto.setPrevINoteDate(
//							prevINoteDetails.get(0)[1] != null ? prevINoteDetails.get(0)[1].toString() : "NA");
//				}

			Optional<SupplyOrder> so = supplyOrderRepository.findById(supplyOrderId);
			if (so.isPresent()) {
				SupplyOrder supplyOrder = so.get();
				iNoteDto.setSoDescription(supplyOrder.getDescription());

			}
			return iNoteDto;
		} else {
			return getNewINoteByOlId(olId);
		}
	}

	private INoteDto getNewINoteByOlId(int olId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SupplyOrder supplyOrder = supplyOrderRepository.getSupplyOrderByOlIds(Arrays.asList(olId));
		if (supplyOrder != null) {

			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
			iNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
			iNoteDto.setSoDescription(supplyOrder.getDescription());

			Organization organization = supplyOrder.getOrganization();
			// Generation of I-Note no based on STD code, serial no for the FY and CQAE
			// naval code
			String stdCode = organization.getStdCode();
			String inoteCode = organization.getInoteCode();
			Integer inoteSerialStart = organization.getInoteSerialStrt();
			String inoteSerialStartStr = inoteSerialStart != null ? String.valueOf(inoteSerialStart) : "0000";

			Integer inoteCountInCurrentFY = iNoteRepository.getInoteCountInCurrentFY(null) == null ? 0
					: iNoteRepository.getInoteCountInCurrentFY(null);
			String inoteNo = "";
			if (!inoteSerialStartStr.equals("0000")) {
				inoteNo = stdCode + ((Integer.parseInt(inoteSerialStartStr) + inoteCountInCurrentFY + 1)) + inoteCode;
			} else {
				inoteNo = stdCode + (inoteSerialStartStr + inoteCountInCurrentFY + 1) + inoteCode;
			}
			iNoteDto.setINoteNo(inoteNo);

			InspectionFileNote inspectionFileNote = inspectionFileNoteRepository
					.findByOfferLetterIds(Arrays.asList(olId));
			List<OfferLetter> offerLetters = null;
			if (inspectionFileNote != null) {
//				if (inspectionFileNote.getInspectionStartDate() != null)
//					iNoteDto.setInspectionStart(dateFormat.format(inspectionFileNote.getInspectionStartDate()));
//				if (inspectionFileNote.getInspectionCompleteDate() != null)
//					iNoteDto.setInspectionEnd(dateFormat.format(inspectionFileNote.getInspectionCompleteDate()));
//				if (inspectionFileNote.getCqaoApprovalDate() != null) {
//					iNoteDto.setCqaoApprovalDate(cqaoDateFormat.format(inspectionFileNote.getCqaoApprovalDate()));
//				}
				iNoteDto.setCreatedDate(inspectionFileNote.getInspectionCompleteDate() != null
						? dateFormat.format(inspectionFileNote.getInspectionCompleteDate())
						: null);
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));

				int createdBy = inspectionFileNote.getCreatedBy();
				String sealNo = empRepository.getInspSealNoByEmpId(createdBy);
				iNoteDto.setSealNo(sealNo);
				offerLetters = inspectionFileNote.getOfferLetters();

			} else {
				offerLetters = offerLetterRepository.findAllById(Arrays.asList(olId));
			}
			String olNumbers = offerLetters.stream().map(OfferLetter::getOlNumber).collect(Collectors.joining(","));
			String storeDescrption = offerLetters.stream().map(OfferLetter::getStoresDescription)
					.filter(Objects::nonNull).collect(Collectors.joining(","));
			String olRemarks = offerLetters.stream().map(OfferLetter::getRemarks).filter(Objects::nonNull)
					.collect(Collectors.joining(","));
			Set<String> offerDateSet = new HashSet<>();
			List<OlLineItem> olLineItems = new ArrayList<>();
			for (OfferLetter offerLetter : offerLetters) {
				if (offerLetter.getOfferDate() != null) {
					Date offerDate = offerLetter.getOfferDate();
					offerDateSet.add(dateFormat.format(offerDate));
				}
				olLineItems.addAll(offerLetter.getOlLineItems());
			}
			// Assuming consignee of all offer letters are same
			if (!offerLetters.isEmpty()) {
				Consignee consignee = offerLetters.get(0).getConsignee();
				if (consignee != null) {
					String consigneeAddress = consignee.getAddress();
					iNoteDto.setConsignee(consigneeAddress);
				}
			}
			iNoteDto.setOfferLetterNo(olNumbers);
			iNoteDto.setOlDateStr(String.join(",", offerDateSet));
			iNoteDto.setDescription(storeDescrption);
			iNoteDto.setRemarks(olRemarks);

			// Getting last I-Note details By Supply Order Id
			List<Object[]> lastINoteDetails = iNoteRepository.getLastInoteDetailsBySoId(supplyOrder.getId());

			if (!lastINoteDetails.isEmpty()) {
				iNoteDto.setPrevINoteNo(
						lastINoteDetails.get(0)[0] != null ? lastINoteDetails.get(0)[0].toString() : null);
				iNoteDto.setPrevINoteDate(
						lastINoteDetails.get(0)[1] != null ? lastINoteDetails.get(0)[1].toString() : null);
			}

			// inspection Start Date and End Date, description

			List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
			for (OlLineItem olLineItem : olLineItems) {
				BigDecimal tendered = olLineItem.getOfferedQty();
				BigDecimal accepted = olLineItem.getClearedQty() != null ? olLineItem.getClearedQty() : tendered;
//					int accepted = tendered;
				BigDecimal rejected = tendered.subtract(accepted);
//					int rejected = 0;

				INoteLineItemDto dto = new INoteLineItemDto();
				SOLineItem soLineItem = olLineItem.getSoLineItem();
				dto.setPartNo(soLineItem.getPartNo());
				dto.setDescription(soLineItem.getDescription());
				if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
					dto.setPartNumberId(soLineItem.getPartNumber().getId());
					dto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
					dto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
				}
				dto.setOrderedQty(soLineItem.getOrderQty());
				dto.setOlLineItemId(olLineItem.getId());
				dto.setUom(soLineItem.getUom() != null ? soLineItem.getUom().getUnit() : null);
				dto.setTendered(tendered);
				dto.setAccepted(accepted);
				dto.setRejected(rejected);
				dto.setLedgerFolio(tendered);
				dto.setIndentNo(soLineItem.getLineItemNo());
				dto.setItemRemarks(olLineItem.getHologramRemarks());
				dto.setQtyAcceptedToDate(
						offerLetterRepository.getCountOfCompletedLineItemsBySoLiId(soLineItem.getId()));
				iNoteLineItemDtos.add(dto);
			}
			iNoteDto.setINoteLineItems(iNoteLineItemDtos);

			List<DdExtension> ddExtensions = supplyOrder.getDdExtensions();
			if (!ddExtensions.isEmpty()) {

				Collections.sort(ddExtensions, (o1, o2) -> o2.getNewDdDate().compareTo(o1.getNewDdDate()));
				DdExtension latestDdExt = ddExtensions.get(0);
				// iNoteDto.setNewDdDate(iNoteDateFormat.format(latestDdExt.getNewDdDate()));
				iNoteDto.setDdExtLetterDate(iNoteDateFormat.format(latestDdExt.getLetterDate()));
				iNoteDto.setDdExtLetterNo(latestDdExt.getLetterNo());
			}
			List<AmendmentsLetter> amendments = supplyOrder.getAmendmentsLetters();
			if (!amendments.isEmpty()) {

				Collections.sort(amendments, (e1, e2) -> e2.getLetterDate().compareTo(e1.getLetterDate()));
				String amendment = "";
				for (AmendmentsLetter amdt : amendments) {

					amendment = amendment.concat(
							amdt.getLetterNo() + "  dated   " + iNoteDateFormat.format(amdt.getLetterDate()) + ",");

				}

				iNoteDto.setAmendment(amendment);
			}
			SalesGroup salesGroup = supplyOrder.getSalesGroup();
			if (salesGroup != null) {
				Organization salesGrpOrganization = salesGroup.getOrganization();
				iNoteDto.setContractorName(salesGrpOrganization.getOrganizationName());
				iNoteDto.setContractorAddress(salesGrpOrganization.getAddress());
			}

			iNoteDto.setIndentor(supplyOrder.getIndenter().getAddress());

			iNoteDto.setIndentNo(supplyOrder.getIndentNo());
			iNoteDto.setIndentDate(supplyOrder.getIndentDate());
			iNoteDto.setOrgName(supplyOrder.getOrganization().getAddress());

			iNoteDto.setExisting(false);

			return iNoteDto;
		} else
			return null;

	}

	public String addLineItem(OfferLetterDto offerLetterDto) {
		OfferLetter offerLetter = getOfferLetterById(offerLetterDto.getId());
		List<OlLineItem> existingOllineItems = offerLetter.getOlLineItems();
		List<SOLineItem> existingSOLineItems = existingOllineItems.stream()
				.map(olLineItem -> olLineItem.getSoLineItem()).collect(Collectors.toList());
		Map<SOLineItem, OlLineItem> olLineItemMap = existingOllineItems.stream()
				.collect(Collectors.toMap(OlLineItem::getSoLineItem, olLineItem -> olLineItem));
		List<OlLineItem> ollineItems = new ArrayList<>();
		List<OlLineItemDto> olLineItemDtos = offerLetterDto.getOlLineItemDtos();
		Date todayDate = new Date();

		List<SOLineItem> soLineItems = soLineItemRepository
				.getSoLineItemsBySaleOrderId(offerLetterDto.getSaleOrderNo());
		Map<String, SOLineItem> soLineItemMap = new HashMap<>();
		for (SOLineItem soLineItem : soLineItems) {
			soLineItemMap.put(soLineItem.getLineItemNo(), soLineItem);
		}

		for (OlLineItemDto olLineItemDto : olLineItemDtos) {
			if (!InputNameValidation.checkString(olLineItemDto.getPriority())) {
				String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
				logger.error(respString);
				throw new IllegalArgumentException(respString);
			}
			if (!InputNameValidation.checkString(olLineItemDto.getIssueLevel())) {
				String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
				logger.error(respString);
				throw new IllegalArgumentException(respString);
			}
			SOLineItem soLineItem = soLineItemMap.get(olLineItemDto.getSoLineItemNo());
			OlLineItem olLineItem = null;
			if (existingSOLineItems.contains(soLineItem)) {
				olLineItem = olLineItemMap.get(soLineItem);
				olLineItem.setOfferedQty(olLineItemDto.getOfferedQty());
				olLineItem.setIssueLevel(olLineItemDto.getIssueLevel());
				olLineItem.setModifiedDateTime(todayDate);
				olLineItem.setModifiedBy(offerLetterDto.getEmployeeId());
				olLineItem.setPriority(olLineItemDto.getPriority());
			} else {
				olLineItem = new OlLineItem();
				olLineItem.setCreatedDateTime(todayDate);
				olLineItem.setCreatedBy(offerLetterDto.getEmployeeId());
				olLineItem.setOfferedQty(olLineItemDto.getOfferedQty());
				olLineItem.setPriority(olLineItemDto.getPriority());
				olLineItem.setIssueLevel(olLineItemDto.getIssueLevel());
				olLineItem.setOfferLetter(offerLetter);
				olLineItem.setSoLineItem(soLineItem);
			}
			ollineItems.add(olLineItem);
		}
		existingOllineItems.addAll(ollineItems);
		offerLetter.setOlLineItems(existingOllineItems);
		OfferLetter savedOfferLetter = offerLetterRepository.save(offerLetter);
		List<OlLineItem> olLineItems = savedOfferLetter.getOlLineItems();

		for (OlLineItemDto olLineItemDto : olLineItemDtos) {
			// Find the matching OlLineItem based on SoLineItemNo
			if (olLineItemDto.getRegisterNumbers() != null && !olLineItemDto.getRegisterNumbers().getIds().isEmpty()) {
				OlLineItem targetOlLineItem = null;
				for (OlLineItem olLineItem : olLineItems) {
					if (olLineItem.getSoLineItem().getLineItemNo().equals(olLineItemDto.getSoLineItemNo())) {
						targetOlLineItem = olLineItem;
						break;
					}
				}

				if (targetOlLineItem != null) {
					LineItemLotsRegisterDto requestDto = new LineItemLotsRegisterDto();
					requestDto.setOlLineItemId(targetOlLineItem.getId());
					requestDto.setIds(olLineItemDto.getRegisterNumbers().getIds());

					if (olLineItemDto.getRegisterNumbers().getStatus() != null) {
						requestDto.setStatus(olLineItemDto.getRegisterNumbers().getStatus());
					} else {
						requestDto.setStatus("in-process");
					}

					lineItemLotsRegisterService.update(requestDto);
				} else {
					String respString = "No matching OlLineItem found for OlLineItemDto with SoLineItemNo: "
							+ olLineItemDto.getSoLineItemNo();
					logger.error(respString);
					return respString;
				}
			}
		}

		return ApplicationConstants.SUCCESS;
	}

	public void deleteLineItem(OfferLetterDto offerLetterDto) {
		OfferLetter offerLetter = getOfferLetterById(offerLetterDto.getId());
		List<OlLineItem> existingOllineItems = offerLetter.getOlLineItems();
		List<Integer> deletedOlLineItemIds = offerLetterDto.getOlLineItemDtos().stream().map(OlLineItemDto::getId)
				.collect(Collectors.toList());
		List<Integer> finalDeleteIds = new ArrayList<>();
		for (OlLineItem olLineItem : existingOllineItems) {
			if (deletedOlLineItemIds.contains(olLineItem.getId())) {
				finalDeleteIds.add(olLineItem.getId());
			}
		}
		offerLetterRepository.deleteLineItems(finalDeleteIds);
	}

	public String editOl(OlEditDto olEditDto) {
		try {
			Optional<OfferLetter> offerLetterOptional = offerLetterRepository.findById(olEditDto.getOfferLetterId());

			if (offerLetterOptional.isPresent()) {
				OfferLetter offerLetter = offerLetterOptional.get();

				if (olEditDto.getOfferDate() != null) {
					offerLetter.setOfferDate(olEditDto.getOfferDate());
				}

				if (olEditDto.getConsigneeId() != null) {
					Optional<Consignee> consigneeOptional = consigneeRepository
							.findById(Integer.parseInt(olEditDto.getConsigneeId()));
					if (consigneeOptional.isPresent()) {
						offerLetter.setConsignee(consigneeOptional.get());
					} else {
						return "Consignee not found.";
					}
				}
				offerLetterRepository.save(offerLetter);

				return "Offer letter updated successfully.";
			} else {
				return "Offer letter not found.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while updating the offer letter.";
		}
	}

	public void deleteOl(int olid) {
		offerLetterRepository.deleteById(olid);
	}

	/**
	 * To generate the INote data based on offer letter Ids
	 *
	 * @param olIds
	 * @return INoteDto
	 * @author Awadhesh Verma
	 */
	public INoteDto getINoteByOLIds(List<Integer> olIds) {

		InspectionFileNote inspectionFileNote = inspectionFileNoteRepository.findFileNoteByOlIds(olIds);
		if (inspectionFileNote != null) {
			INote existing = iNoteRepository.getByOlNumbers(olIds);
			DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (existing != null) {
				INoteDto iNoteDto = mapper.map(existing, INoteDto.class);
				iNoteDto.setInoteId(existing.getId());
				if (existing.getCreatedDatetime() != null) {
					iNoteDto.setCreatedDate(dateFormat.format(existing.getCreatedDatetime()));
				}
				List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
				for (INoteLineItem item : existing.getINoteLineItems()) {
					INoteLineItemDto dto = mapper.map(item, INoteLineItemDto.class);
					// dto.setItemRemarks(olLineItem.getHologramRemarks());
					iNoteLineItemDtos.add(dto);
				}
				iNoteLineItemDtos.sort(Comparator.comparingInt(INoteLineItemDto::getId));
				int supplyOrderId = 0;

				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				iNoteDto.setINoteLineItems(iNoteLineItemDtos);
				iNoteDto.setExisting(true);
				iNoteDto.setRemarks(existing.getRemarks());
				iNoteDto.setOlDateStr(dateFormat.format(inspectionFileNote.getOlDate()));
				supplyOrderId = inspectionFileNote.getSupplyOrderId();

				iNoteDto.setAmendment(iNoteDto.getAmendment());
				Optional<SupplyOrder> so = supplyOrderRepository.findById(supplyOrderId);
				if (so.isPresent()) {
					SupplyOrder supplyOrder = so.get();
					iNoteDto.setSoDescription(supplyOrder.getDescription());
				}
				return iNoteDto;
			} else {
				return getNewINoteByOlIds(olIds, inspectionFileNote.getId());
			}
		} else {
			logger.error("No File Noting not found for the given Offer Letter: {}", olIds);
			throw new DataNotFoundException("File Noting not found for the given Offer Letter");
		}

	}

	/**
	 * @param olIds
	 * @return INoteDto
	 * @author Awadhesh Verma
	 */
	public INoteDto getNewINoteByOlIds(List<Integer> olIds, int fileNoteId) {

		DateFormat iNoteDateFormat = new SimpleDateFormat("dd-MMM-yy");
		DateFormat cqaoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SupplyOrder supplyOrder = supplyOrderRepository.getSupplyOrderByOlIds(olIds);
		if (supplyOrder != null) {

			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setSupplyOrderNo(supplyOrder.getOrderNo());
			iNoteDto.setSupplyOrderDate(supplyOrder.getOrderDate());
			iNoteDto.setSoDescription(supplyOrder.getDescription());
			/*
			 * Organization organization = supplyOrder.getOrganization(); // Generation of
			 * I-Note no based on STD code, serial no for the FY and CQAE // naval code
			 * String stdCode = organization.getStdCode(); String inoteCode =
			 * organization.getInoteCode(); Integer inoteSerialStart =
			 * organization.getInoteSerialStrt(); String inoteSerialStartStr =
			 * inoteSerialStart != null ? String.valueOf(inoteSerialStart) : "0000";
			 * 
			 * int inoteCountInCurrentFY = iNoteRepository.getInoteCountInCurrentFY();
			 * String inoteNo = ""; if (!inoteSerialStartStr.equals("0000")) { inoteNo =
			 * stdCode + ((Integer.parseInt(inoteSerialStartStr) + inoteCountInCurrentFY +
			 * 1)) + inoteCode; } else { inoteNo = stdCode + (inoteSerialStartStr +
			 * inoteCountInCurrentFY + 1) + inoteCode; } iNoteDto.setINoteNo(inoteNo);
			 */
			Optional<InspectionFileNote> inspectionFileNoteOptional = inspectionFileNoteRepository.findById(fileNoteId);
			if (inspectionFileNoteOptional.isPresent()) {
				InspectionFileNote inspectionFileNote = inspectionFileNoteOptional.get();
//				if (inspectionFileNote.getInspectionStartDate() != null)
//					iNoteDto.setInspectionStart(dateFormat.format(inspectionFileNote.getInspectionStartDate()));
//				if (inspectionFileNote.getInspectionCompleteDate() != null)
//					iNoteDto.setInspectionEnd(dateFormat.format(inspectionFileNote.getInspectionCompleteDate()));
//				if (inspectionFileNote.getCqaoApprovalDate() != null) {
//					iNoteDto.setCqaoApprovalDate(cqaoDateFormat.format(inspectionFileNote.getCqaoApprovalDate()));
//				}
				iNoteDto.setCreatedDate(inspectionFileNote.getInspectionCompleteDate() != null
						? dateFormat.format(inspectionFileNote.getInspectionCompleteDate())
						: null);
				iNoteDto.setFileNo(inspectionFileNote.getFileNumber());
				String typeOfDelivery = inspectionFileNote.getTypeOfDelivery();
				iNoteDto.setTypeOfDelivery(typeOfDelivery);
				iNoteDto.setTypeOfDeliveryINote(DeliveryTypeEnum.getValueByKey(typeOfDelivery));

				int createdBy = inspectionFileNote.getCreatedBy();
				String sealNo = empRepository.getInspSealNoByEmpId(createdBy);
				iNoteDto.setSealNo(sealNo);

				List<OfferLetter> offerLetters = offerLetterRepository.findAllById(olIds);
				String olNumbers = offerLetters.stream().map(OfferLetter::getOlNumber).collect(Collectors.joining(","));
				String storeDescrption = offerLetters.stream().map(OfferLetter::getStoresDescription)
						.filter(Objects::nonNull).collect(Collectors.joining(","));
				String olRemarks = offerLetters.stream().map(OfferLetter::getRemarks).filter(Objects::nonNull)
						.collect(Collectors.joining(","));
				String iNoteNo = inspectionFileNoteRepository.getINoteNoByOlIds(olIds);
				iNoteDto.setINoteNo(iNoteNo);
				Set<String> offerDateSet = new HashSet<>();
				List<OlLineItem> olLineItems = new ArrayList<>();
				for (OfferLetter offerLetter : offerLetters) {
					if (offerLetter.getOfferDate() != null) {
						Date offerDate = offerLetter.getOfferDate();
						offerDateSet.add(dateFormat.format(offerDate));
					}
					olLineItems.addAll(offerLetter.getOlLineItems());
				}
				// Assuming consignee of all offer letters are same
				// String consigneeAddress = offerLetters.get(0).getConsignee().getAddress();
				if (!offerLetters.isEmpty()) {
					Consignee consignee = offerLetters.get(0).getConsignee();
					if (consignee != null) {
						String consigneeAddress = consignee.getAddress();
						iNoteDto.setConsignee(consigneeAddress);
					}
				}
				iNoteDto.setOfferLetterNo(olNumbers);
				iNoteDto.setOlDateStr(String.join(",", offerDateSet));
				iNoteDto.setDescription(storeDescrption);
				iNoteDto.setRemarks(olRemarks);

				// Getting last I-Note details By Supply Order Id
				List<Object[]> lastINoteDetails = iNoteRepository.getLastInoteDetailsBySoId(supplyOrder.getId());

				if (!lastINoteDetails.isEmpty()) {
					iNoteDto.setPrevINoteNo(
							lastINoteDetails.get(0)[0] != null ? lastINoteDetails.get(0)[0].toString() : null);
					iNoteDto.setPrevINoteDate(
							lastINoteDetails.get(0)[1] != null ? lastINoteDetails.get(0)[1].toString() : null);
				}
//				 for (Object[] obj : lastINoteDetails) {
//						iNoteDto.setPrevINoteNo(obj[0].toString());
//						iNoteDto.setPrevINoteDate(obj[1] != null ? obj[1].toString() : null);
//			        }
				// inspection Start Date and End Date, description

				List<INoteLineItemDto> iNoteLineItemDtos = new ArrayList<>();
				for (OlLineItem olLineItem : olLineItems) {
					BigDecimal tendered = olLineItem.getOfferedQty();
					BigDecimal accepted = olLineItem.getClearedQty() != null ? olLineItem.getClearedQty() : tendered;
//					int accepted = tendered;
					BigDecimal rejected = tendered.subtract(accepted);
//					int rejected = 0;

					INoteLineItemDto dto = new INoteLineItemDto();
					SOLineItem soLineItem = olLineItem.getSoLineItem();
					dto.setPartNo(soLineItem.getPartNo());
					dto.setDescription(soLineItem.getDescription());
					if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
						dto.setPartNumberId(soLineItem.getPartNumber().getId());
						dto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
						dto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
					}
					dto.setOrderedQty(soLineItem.getOrderQty());
					dto.setOlLineItemId(olLineItem.getId());
					dto.setUom(soLineItem.getUom() != null ? soLineItem.getUom().getUnit() : null);
					dto.setTendered(tendered);
					dto.setAccepted(accepted);
					dto.setRejected(rejected);
					dto.setLedgerFolio(tendered);
					dto.setIndentNo(soLineItem.getLineItemNo());
					dto.setItemRemarks(olLineItem.getHologramRemarks());
					dto.setQtyAcceptedToDate(
							offerLetterRepository.getCountOfCompletedLineItemsBySoLiId(soLineItem.getId()));
					iNoteLineItemDtos.add(dto);
				}
				iNoteDto.setINoteLineItems(iNoteLineItemDtos);

			}

			List<DdExtension> ddExtensions = supplyOrder.getDdExtensions();
			if (!ddExtensions.isEmpty()) {

				Collections.sort(ddExtensions, (o1, o2) -> o2.getNewDdDate().compareTo(o1.getNewDdDate()));
				DdExtension latestDdExt = ddExtensions.get(0);
				// iNoteDto.setNewDdDate(iNoteDateFormat.format(latestDdExt.getNewDdDate()));
				iNoteDto.setDdExtLetterDate(iNoteDateFormat.format(latestDdExt.getLetterDate()));
				iNoteDto.setDdExtLetterNo(latestDdExt.getLetterNo());
			}
			List<AmendmentsLetter> amendments = supplyOrder.getAmendmentsLetters();
			if (!amendments.isEmpty()) {

				Collections.sort(amendments, (e1, e2) -> e2.getLetterDate().compareTo(e1.getLetterDate()));
				String amendment = "";
				for (AmendmentsLetter amdt : amendments) {

					amendment = amendment.concat(
							amdt.getLetterNo() + " dated " + iNoteDateFormat.format(amdt.getLetterDate()) + ",");
				}

				iNoteDto.setAmendment(amendment);
//
			}
			SalesGroup salesGroup = supplyOrder.getSalesGroup();
			if (salesGroup != null) {
				Organization salesGrpOrganization = salesGroup.getOrganization();
				iNoteDto.setContractorName(salesGrpOrganization.getOrganizationName());
				iNoteDto.setContractorAddress(salesGrpOrganization.getAddress());
			}

			iNoteDto.setIndentor(supplyOrder.getIndenter().getAddress());

			iNoteDto.setIndentNo(supplyOrder.getIndentNo());
			iNoteDto.setIndentDate(supplyOrder.getIndentDate());
			iNoteDto.setOrgName(supplyOrder.getOrganization().getAddress());

			iNoteDto.setExisting(false);

			return iNoteDto;
		} else
			return null;
	}

	public List<OfferLetterDto> getAllOLs(int orgId) {

		List<Object[]> offerLetters = offerLetterRepository.getOLs(orgId);
		List<OfferLetterDto> offerLetterDtos = new ArrayList<>();
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		for (Object[] objects : offerLetters) {

			OfferLetterDto offerLetterDto = new OfferLetterDto();
			offerLetterDto.setId((int) objects[0]);
			offerLetterDto.setSoId((int) objects[1]);
			offerLetterDto.setSoNumber(objects[2].toString());
			offerLetterDto.setName(objects[3].toString());
			offerLetterDto.setOfferDate(objects[4] != null ? objects[4].toString() : null);
			offerLetterDto.setSalesGroupName(objects[5].toString());
			offerLetterDto.setProjectName(objects[6] != null ? objects[6].toString() : null);

			offerLetterDtos.add(offerLetterDto);

		}

		return offerLetterDtos;
	}

	public String updateQaclForInote(OfferLetterDto offerLetterDto) {

		if (offerLetterDto != null && offerLetterDto.getId() != 0) {
			Optional<OfferLetter> olOptional = offerLetterRepository.findById(offerLetterDto.getId());
			if (olOptional.isPresent()) {
				OfferLetter offerLetter = olOptional.get();
				offerLetter.setInoteApproval(offerLetterDto.getInoteApproval());
				offerLetter.setInoteApprovalRemarks(offerLetterDto.getInoteApprovalRemarks());
				offerLetterRepository.save(offerLetter);
				if (offerLetterDto.getInoteApproval() == true)
					updateOlStatus(offerLetter.getId(), ApplicationConstants.OL_COMPLETED);
				else {
					updateOlStatus(offerLetter.getId(), ApplicationConstants.OL_REJECTED);
				}
				return ApplicationConstants.SUCCESS;

			}
		}
		return ApplicationConstants.FAILURE;
	}

	
	private static String getFinancialYearStrForInote() {
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		Month month = currentDate.getMonth();

		// If the current month is from January to March, the financial year belongs to
		// the previous calendar year.
		if (month.getValue() >= 1 && month.getValue() <= 3) {
			year = year - 1;
		}

		// The financial year is represented by the last two digits of the starting
		// year.
		return String.valueOf(year % 100);
	}

	public List<AllottedRegNosDto> getAllottedOfferLettersByStatusForFirmTest(int orgId, int empId, String fromDateStr,
			String toDateStr, Integer productId, String status) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<AllottedRegNosDto> responseDtos = new ArrayList<>();

		List<Object[]> offerLetters = offerLetterRepository.getAllottedOLsByStatusForFirm2(orgId, empId);

		for (Object[] ol : offerLetters) {
			// ol[0] = olId, ol[1] = ol_numbe, ol[2] = soLiId, ol[3] = so_line_item_no,
			// ol[4] = olLiId, ol[5] = olDate,
			// ol[6] = partNo, ol[7] = regId, ol[8] = regNo, ol[9] = regStatus, ol[10] =
			// prodId, ol[11] = prodName

			AllottedRegNosDto olNewDto = new AllottedRegNosDto();
			olNewDto.setOlId(Integer.parseInt(ol[0].toString()));
			olNewDto.setOlNumber(ol[1] != null ? ol[1].toString() : "");
			olNewDto.setSoLineItemId(ol[2] != null ? Integer.parseInt(ol[2].toString()) : null);
			olNewDto.setSoLineItemNo(ol[3] != null ? ol[3].toString() : "");
			olNewDto.setOlLineItemId(ol[4] != null ? Integer.parseInt(ol[4].toString()) : null);
			olNewDto.setOlLineItemDate(ol[5] != null ? ol[5].toString() : "");
			olNewDto.setPartNo(ol[6] != null ? ol[6].toString() : null);
			olNewDto.setRegisterNoId(ol[7] != null ? Integer.parseInt(ol[7].toString()) : null);
			olNewDto.setRegisterNo(ol[8] != null ? ol[8].toString() : "");
			olNewDto.setStatus(ol[9] != null ? ol[9].toString() : null);
			olNewDto.setProductId(ol[10] != null ? Integer.parseInt(ol[10].toString()) : null);
			olNewDto.setProductName(ol[11] != null ? ol[11].toString() : null);
			responseDtos.add(olNewDto);
		}

		if (productId != null) {
			responseDtos = responseDtos.stream()
					.filter(dto -> dto.getProductId() != null && dto.getProductId().equals(productId))
					.collect(Collectors.toList());
		}

		if (status != null && !status.trim().isEmpty()) {
			responseDtos = responseDtos.stream().filter(
					dto -> dto.getStatus() != null && dto.getStatus().toLowerCase().equals(status.toLowerCase()))
					.collect(Collectors.toList());
		}

		if (fromDateStr != null && toDateStr != null) {
			try {
				Date fromDate = sdf.parse(fromDateStr);
				Date toDate = sdf.parse(toDateStr);
				responseDtos = responseDtos.stream().filter(dto -> {
					Date olLineItemDate = null;
					try {
						olLineItemDate = sdf.parse(dto.getOlLineItemDate());
					} catch (ParseException e) {
						logger.error("Error parsing date in filter for dto: " + dto.getOlLineItemDate());
						return false;
					}
					return olLineItemDate.after(fromDate) && olLineItemDate.before(toDate);
				}).collect(Collectors.toList());
			} catch (ParseException e) {
				logger.error("Error parsing dates on filter.", e);
			}
		}

		return responseDtos;
	}

	public List<AllottedRegNosDto> getAllottedOfferLettersByStatusForFirmTestByStatusV2(int orgId, int empId,
			List<Integer> olStatusIds, String fromDateStr, String toDateStr, Integer productId, String status) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		List<AllottedRegNosDto> responseDtos = new ArrayList<>();

		List<Object[]> offerLetters = offerLetterRepository.getAllottedOLsByStatusForFirmByStatusV2(orgId, empId,
				olStatusIds);

		for (Object[] ol : offerLetters) {
			// ol[0] = olId, ol[1] = ol_numbe, ol[2] = soLiId, ol[3] = so_line_item_no,
			// ol[4] = olLiId, ol[5] = olDate,
			// ol[6] = partNo, ol[7] = regId, ol[8] = regNo, ol[9] = regStatus, ol[10] =
			// prodId, ol[11] = prodName

			AllottedRegNosDto olNewDto = new AllottedRegNosDto();
			olNewDto.setOlId(Integer.parseInt(ol[0].toString()));
			olNewDto.setOlNumber(ol[1] != null ? ol[1].toString() : "");
			olNewDto.setSoLineItemId(ol[2] != null ? Integer.parseInt(ol[2].toString()) : null);
			olNewDto.setSoLineItemNo(ol[3] != null ? ol[3].toString() : "");
			olNewDto.setOlLineItemId(ol[4] != null ? Integer.parseInt(ol[4].toString()) : null);
			olNewDto.setOlLineItemDate(ol[5] != null ? ol[5].toString() : "");
			olNewDto.setPartNo(ol[6] != null ? ol[6].toString() : null);
			olNewDto.setRegisterNoId(ol[7] != null ? Integer.parseInt(ol[7].toString()) : null);
			olNewDto.setRegisterNo(ol[8] != null ? ol[8].toString() : "");
			olNewDto.setStatus(ol[9] != null ? ol[9].toString() : null);
			olNewDto.setProductId(ol[10] != null ? Integer.parseInt(ol[10].toString()) : null);
			olNewDto.setProductName(ol[11] != null ? ol[11].toString() : null);
			responseDtos.add(olNewDto);
		}

		if (productId != null) {
			responseDtos = responseDtos.stream()
					.filter(dto -> dto.getProductId() != null && dto.getProductId().equals(productId))
					.collect(Collectors.toList());
		}

		if (status != null && !status.trim().isEmpty()) {
			responseDtos = responseDtos.stream().filter(
					dto -> dto.getStatus() != null && dto.getStatus().toLowerCase().equals(status.toLowerCase()))
					.collect(Collectors.toList());
		}

		if (fromDateStr != null && toDateStr != null) {
			try {
				Date fromDate = sdf.parse(fromDateStr);
				Date toDate = sdf.parse(toDateStr);
				responseDtos = responseDtos.stream().filter(dto -> {
					Date olLineItemDate = null;
					try {
						olLineItemDate = sdf.parse(dto.getOlLineItemDate());
					} catch (ParseException e) {
						logger.error("Error parsing date in filter for dto: " + dto.getOlLineItemDate());
						return false;
					}
					return olLineItemDate.after(fromDate) && olLineItemDate.before(toDate);
				}).collect(Collectors.toList());
			} catch (ParseException e) {
				logger.error("Error parsing dates on filter.", e);
			}
		}

		return responseDtos;
	}

	public List<InoteDetailsDto> getInotesByProdId(int partNumberId) {

		List<InoteDetailsDto> responseDtos = new ArrayList<>();

		List<Object[]> offerLetters = offerLetterRepository.getInotesByProdId(partNumberId);

		for (Object[] ol : offerLetters) {
			InoteDetailsDto olNewDto = new InoteDetailsDto();
			olNewDto.setGrp((String) ol[0]);
            olNewDto.setInoteNo((String) ol[1]);
            olNewDto.setInoteDate((String) ol[2]);
            olNewDto.setOfferLetterNumber((String) ol[3]);
            olNewDto.setOfferLetterDate((String) ol[4]);
            olNewDto.setWarrantyNo((String) ol[6]);
            olNewDto.setWorkOrderNo((String) ol[5]);
            olNewDto.setSubQty((BigDecimal) ol[7]);
            olNewDto.setAcceptedQty((BigDecimal) ol[8]);
            //olNewDto.setRfrQty((Integer) ol[9]);
            olNewDto.setRejectedQty((BigDecimal) ol[9]);
			olNewDto.setOrganization((String) ol[10]);
//			olNewDto.setOlid((Integer)ol[12]);	
			olNewDto.setMergeSslipNo(ol[11].toString());
			olNewDto.setMergeSslipId((Integer) ol[12]);
			olNewDto.setOlid((Integer) ol[13]);
			olNewDto.setInoteId((Integer) ol[14]);
			responseDtos.add(olNewDto);
		}
		return responseDtos;
	}

public List<InoteDetailsDto> getInternalInotesByProdId(int partNumberId) {
	List<InoteDetailsDto> responseDtos = new ArrayList<>();
	
	List<Object[]> offerLetters  = offerLetterRepository.getInternalInotesByProdId(partNumberId);
	
	for (Object[] ol : offerLetters) {
		InoteDetailsDto olNewDto = new InoteDetailsDto();
		olNewDto.setGrp((String) ol[0]);
        olNewDto.setInternalInoteNo((String) ol[1]);
        olNewDto.setInoteDate((String) ol[2]);
        olNewDto.setOfferLetterNumber((String) ol[3]);
        olNewDto.setOfferLetterDate((String) ol[4]);
        olNewDto.setWarrantyNo((String) ol[6]);
        olNewDto.setWorkOrderNo((String) ol[5]);
        olNewDto.setSubQty((BigDecimal) ol[7]);
        olNewDto.setAcceptedQty((BigDecimal) ol[8]);
        //olNewDto.setRfrQty((Integer) ol[9]);
        olNewDto.setRejectedQty((BigDecimal) ol[9]);
		olNewDto.setOrganization((String) ol[10]);
//		olNewDto.setOlid((Integer)ol[12]);	
			olNewDto.setMergeSslipNo((String) ol[11]);
			olNewDto.setMergeSslipId((Integer) ol[12]);
			;
			olNewDto.setInoteId((Integer) ol[13]);
			responseDtos.add(olNewDto);
		}
		return responseDtos;
	}

	public List<InoteDetailsDto> getAllInotesList() {

	
	List<InoteDetailsDto> responseDtos = new ArrayList<>();
	
	List<Object[]> offerLetters  = offerLetterRepository.getAllInotesList();
	
	for (Object[] ol : offerLetters) {
		InoteDetailsDto olNewDto = new InoteDetailsDto();
		olNewDto.setGrp((String) ol[0]);
        olNewDto.setInoteNo((String) ol[1]);
        olNewDto.setInoteDate((String) ol[2]);
        olNewDto.setInternalInoteNo((String) ol[3]);
        olNewDto.setInternalInoteDate((String) ol[4]);
        olNewDto.setWarrantyNo((String) ol[5]);
        olNewDto.setWorkOrderNo((String) ol[6]);
        olNewDto.setSubQty((BigDecimal) ol[7]);
        olNewDto.setAcceptedQty((BigDecimal) ol[8]);
        olNewDto.setRejectedQty((BigDecimal) ol[9]);
        olNewDto.setRfrQty((BigDecimal) ol[10]);
		olNewDto.setOrganization((String) ol[11]);
		olNewDto.setOlid((Integer)ol[12]);	
		responseDtos.add(olNewDto);
	}
	return responseDtos;
}

	public List<InoteDetailsDto> getAllInternalInoteList() {

	List<InoteDetailsDto> responseDtos = new ArrayList<>();
	
	List<Object[]> offerLetters  = offerLetterRepository.getAllInternalInotesList();
	
	for (Object[] ol : offerLetters) {
		InoteDetailsDto olNewDto = new InoteDetailsDto();
		olNewDto.setGrp((String) ol[0]);
        olNewDto.setInternalInoteNo((String) ol[1]);
        olNewDto.setInternalInoteDate((String) ol[2]);
//        olNewDto.setSubQty(BigDecimal.valueOf((Integer) ol[3]));
        olNewDto.setSubQty(ol[3]!= null? new BigDecimal(ol[3].toString()): null);
        olNewDto.setAcceptedQty(ol[4]!= null? new BigDecimal(ol[4].toString()): null);
        olNewDto.setRejectedQty(ol[5]!= null? new BigDecimal(ol[5].toString()): null);
        olNewDto.setRfrQty(ol[6]!= null? new BigDecimal(ol[6].toString()): null);
        olNewDto.setWarrantyNo((String) ol[7]);
        olNewDto.setWorkOrderNo((String) ol[8]);
		olNewDto.setOrganization((String) ol[9]);
		olNewDto.setMergeSslipNo((String)ol[10]);	
		olNewDto.setMergeSslipId((Integer)ol[11]);	
		olNewDto.setEpcode((String) ol[12]);	
		responseDtos.add(olNewDto);
	}
	return responseDtos;
}

//public List<InoteDetByInspOrgDto> getAllInotesListByInspOrgId() {
//
//	List<InoteDetByInspOrgDto> responseDtos = new ArrayList<>();
//	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//	List<Object[]> offerLetters = offerLetterRepository.getAllInotesListByInspOrgId();
//
//	for (Object[] ol : offerLetters) {
//		InoteDetByInspOrgDto olNewDto = new InoteDetByInspOrgDto();
//		olNewDto.setGrp((String) ol[0]);
//		olNewDto.setInoteNo((String) ol[1]);
//		olNewDto.setInoteDate((String) ol[2]);
//		olNewDto.setInternalInoteNo((String) ol[3]);
//		olNewDto.setInternalInoteDate((String) ol[4]);
//		olNewDto.setWorkOrderNo((String) ol[5]);
////        olNewDto.setWarrantyNo((String) ol[6]);
//		olNewDto.setSupplyOrder((String) ol[6]);
//		olNewDto.setSoDate(sdf.format((Date) ol[7]));
//		olNewDto.setINoteId((Integer) ol[8]);
//
//		responseDtos.add(olNewDto);
//	}
//	
//	return responseDtos;
//}

	@Transactional
	public Map<Boolean, String> saveOfferLetterWarrant(OfferLetterDto offerLetterDto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			Set<OfferLetter> offerLetters = new HashSet<>();
//		List<OlLineItemDto> olLineItemDtos = offerLetterDto.getOlLineItemDtos();

			List<String> offerLetterNos = offerLetterRepository.findOfferLetterNos();
			InspectionLevel inspLevel = null;
			if (offerLetterDto.getInspectionLevelId() != null) {
				Optional<InspectionLevel> inspLevelOptional = inspLevelRepository
						.findById(offerLetterDto.getInspectionLevelId());
				if (inspLevelOptional.isPresent()) {
					inspLevel = inspLevelOptional.get();
				}
			}
//			List<String> lineItemNoList = offerLetterDto.getOlLineItemDtos().stream()
//					.map(OlLineItemDto::getSoLineItemNo).collect(Collectors.toList());
//		List<SOLineItem> soLineItems = soLineItemRepository
//				.getSoLineItemsByCategoryId(offerLetterDto.getWarrantId());
//		Map<String, SOLineItem> soLineItemMap = new HashMap<>();
//		for (SOLineItem soLineItem : soLineItems) {
//			soLineItemMap.put(soLineItem.getLineItemNo(), soLineItem);
//		}
//		SaleOrder saleOrder = null;
//		if (offerLetterDto.getSaleOrderNo() != null) {
//			Optional<SaleOrder> saleOrderOptional = saleOrderRepository.findById(offerLetterDto.getSaleOrderNo());
//			if (saleOrderOptional.isPresent()) {
//				saleOrder = saleOrderOptional.get();
//			} else {
//				String respString = "No sale order found with no :" + offerLetterDto.getSaleOrderNo();
//				returnMap.put(false, respString);
//				return returnMap;
//			}
//		}

			SOLineItem soLineItem = soLineItemRepository.getSoLineItemByLotId(offerLetterDto.getLotId());
//		Map<String, OfferLetter> olMap = new ConcurrentHashMap<>();
			// for (OlLineItemDto olLineItemDto : olLineItemDtos) {
			if (!offerLetterNos.contains(offerLetterDto.getOlNumber())) {
				OfferLetter offerLetter = null;
//				if (olMap.containsKey(offerLetterDto.getOlNumber())) {
//					offerLetter = olMap.get(offerLetterDto.getOlNumber());
//				} else {
				offerLetter = new OfferLetter();
				if (!InputNameValidation.checkString(offerLetterDto.getOlNumber())) {

					String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
					logger.error(respString);
					returnMap.put(false, respString);
					return returnMap;
				}

				// Add code for the saving the merge submission slip obj
				MergeSubSlip msubslip = mergeSubSlipRepository.findById(offerLetterDto.getLotId())
						.orElseThrow(() -> new IllegalArgumentException("MergeSlip not found"));
				offerLetter.setLot(msubslip);

				offerLetter.setOlNumber(offerLetterDto.getOlNumber());
				offerLetter.setCreatedDate(todayDate);
				offerLetter.setActive(true);
				offerLetter.setOfferDate(sdf.parse(offerLetterDto.getOfferDate()));
				offerLetter.setCreatedBy(offerLetterDto.getEmployeeId());
//					offerLetter.setLotNumber(msubslip.getMergeOlNo());
				if (inspLevel != null) {
					offerLetter.setInspectionLevel(inspLevel);
				}

				if (offerLetterDto.getConsigneeId() == null)
					throw new IllegalArgumentException("Consignee Id cannot be null.");

				Consignee consignee = consigneeRepository.findById(offerLetterDto.getConsigneeId())
						.orElseThrow(() -> new IllegalArgumentException(
								"Consignee not found for id: " + offerLetterDto.getConsigneeId()));
				offerLetter.setConsignee(consignee);
//					olMap.put(olLineItemDto.getOfferLetterNo(), offerLetter);
//				}

//				SOLineItem soLineItem = soLineItemMap.get(offerLetterDto.getSoLiId().toString());
				if (soLineItem != null) {

					BigDecimal offeredQty = BigDecimal.ZERO; //offerLetterDto.get .getOfferedQty();
					BigDecimal remainingQty = olLineItemRepository.getRemainingQtyBySoLineItemId(soLineItem.getId());
					remainingQty = (remainingQty == null) ? soLineItem.getOrderQty() : remainingQty;
//					System.out.println("remaining qty "+ remainingQty);
//					if (offeredQty > remainingQty) {
//
//						String respString = "Offered qty(" + offeredQty + ") is more than remaining qty("
//								+ remainingQty + ") for line item: " + soLineItem.getLineItemNo();
//						returnMap.put(false, respString);
//						return returnMap;
//
//					}
					OlLineItem olLineItem = new OlLineItem();
					olLineItem.setCreatedDateTime(todayDate);
					olLineItem.setCreatedBy(offerLetterDto.getEmployeeId());
					olLineItem.setOfferLetter(offerLetter);
					olLineItem.setSoLineItem(soLineItem);

					olLineItem.setOfferedQty(offeredQty);
//						line
					List<OlLineItem> existingLineItems = offerLetter.getOlLineItems();
					if (existingLineItems == null) {
						existingLineItems = new ArrayList<>();
					}
					existingLineItems.add(olLineItem);
					offerLetter.setOlLineItems(existingLineItems);
					offerLetter.setSaleOrder(soLineItem.getSaleOrder());
					offerLetter.setInternal(offerLetterDto.getIsInternal());
					offerLetters.add(offerLetter);
				} else {
					String respString = "Line item no: doesn't belong to selected Sale Order";
					returnMap.put(false, respString);
					return returnMap;
				}
			} else {
				String respString = "OL exists with name:" + offerLetterDto.getOlNumber();
				logger.error(respString);
				returnMap.put(false, respString);
				return returnMap;
			}
			// }

			List<OfferLetter> savedOfferLetters = offerLetterRepository.saveAll(offerLetters);

			for (OfferLetter offerLetter : savedOfferLetters) {

				List<OlLineItem> olLineItems = offerLetter.getOlLineItems();
//			for (OlLineItemDto olLineItemDto : olLineItemDtos) {
				// Check if the offerLetterNo matches the current OlLineItemDto
				// if (offerLetter.getOlNumber().equals(olLineItemDto.getOfferLetterNo())) {
				// Find the matching OlLineItem based on SoLineItemNo
				OlLineItem targetOlLineItem = null;
				for (OlLineItem olLineItem : olLineItems) {
					if (olLineItem.getSoLineItem().getId().equals(soLineItem.getId())) {
						targetOlLineItem = olLineItem;
						break;
					}
				}

				List<LineItemSerialNo> regNos = lineItemSerialNoRepository
						.findPassedRegsByMergedSSlipId(offerLetter.getLot().getId());

				if (regNos == null || regNos.isEmpty()) {
					throw new IllegalArgumentException(
							"No passed registration number found for merged slip ID: " + offerLetter.getLot().getId());
				}

				List<OLLineItemSerialNo> serialNos = new ArrayList<>();
				for (LineItemSerialNo regNo : regNos) {
					OLLineItemSerialNo serialNo = new OLLineItemSerialNo();
					serialNo.setSerialNo(regNo);
					serialNo.setOlLineItem(targetOlLineItem);
					serialNo.setActive(true);
					serialNos.add(serialNo);
				}

				olLineItemSerialNoRepository.saveAll(serialNos);

				regNos.forEach(regNo -> {
					regNo.setOlId(offerLetter);
				});

				lineItemSerialNoRepository.saveAll(regNos);

				// Add OlId to InternalInote table if present
				InternalINote intInote = internalINoteRepository.findByMergeSubSlipId(offerLetter.getLot().getId())
						.orElse(null);
				if (intInote != null) {
					intInote.setOfferLetter(offerLetter);
					intInote.setOfferLetterNo(offerLetter.getOlNumber());
					internalINoteRepository.save(intInote);
				}

//					if (targetOlLineItem != null && !olLineItemDto.getRegisterNumbers().getIds().isEmpty()) {
//						LineItemLotsRegisterDto requestDto = new LineItemLotsRegisterDto();
//						requestDto.setOlLineItemId(targetOlLineItem.getId());
//						requestDto.setIds(olLineItemDto.getRegisterNumbers().getIds());
//
//						try {
//							lineItemLotsRegisterService.saveRegisterByOl(requestDto);// .update(requestDto);
//						} catch (Exception e) {
//							logger.error(
//									"Error updating register for OL: " + offerLetter.getOlNumber() + ", Line Item: "
//											+ olLineItemDto.getSoLineItemNo() + ", Error: " + e.getMessage());
//						}
//
//					} else {
//						String respString = (targetOlLineItem == null) ? "No matching OlLineItem found"
//								: "No Register Numbers provided";
//						logger.warn(respString);
//					}
				// }
			}

			// creating a notification for the CQAE user
//			Integer cqaeId = offerLetterRepository.getCqaeOrgIdByOlId(offerLetter.getId());
//			if (cqaeId != null) {
//				List<Integer> receivers = empRepository.getJCnAdminIdsByOrgId(cqaeId);
//				MessageBean message = new MessageBean();
//				message.setSender(offerLetter.getCreatedBy());
//				message.setMessageType(ApplicationConstants.SUPPLY_ORDER);
//				message.setMessage("New Offer Letter created with Id: " + offerLetter.getOlNumber());
//				message.setItemNo(offerLetter.getOlNumber());
//				message.setReceivers(receivers);
//				wsService.notifyCqae(message);
//
//				for (Integer receiverId : receivers) {
//					NotificationDto notificationDto = new NotificationDto();
//					notificationDto
//							.setNotificationName("New Offer Letter created with Id " + offerLetter.getOlNumber());
//					notificationDto.setNotificationCategory("OL");
//					notificationDto.setCategoryId(offerLetter.getId());
//					notificationDto.setSenderId(offerLetterDto.getEmployeeId());
//					notificationDto.setReceiverId(receiverId);
//					notificationService.saveNotification(notificationDto);
//				}
//			}
//		}
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error in saving OL {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public List<OlDetByInspOrgDto> getAllOlListByInspOrgId() {

		List<OlDetByInspOrgDto> responseDtos = new ArrayList<>();

		try {
			List<Object[]> offerLetters = offerLetterRepository.getAllOlListByInspOrgId();

			for (Object[] ol : offerLetters) {
				OlDetByInspOrgDto olDto = new OlDetByInspOrgDto();
				olDto.setOlId((Integer) ol[0]);
				// olDto.setGrp((String)ol[1]);
				olDto.setSoNumber((String) ol[1]);
				olDto.setSoDate((String) ol[2]);
				olDto.setOlNo((String) ol[3]);
				olDto.setOlDate((String) ol[4]);
				olDto.setWorkOrderNo((String) ol[5]);

				responseDtos.add(olDto);
			}
		} catch (Exception e) {

			System.err.println("Error processing offer letters" + e.getMessage());
			return new ArrayList<>();

		}
		return responseDtos;

	}

	@Transactional
	public String saveOfferLetterWarrantNew(OfferLetterDto offerLetterDto) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date todayDate = new Date();
		Set<OfferLetter> offerLetters = new HashSet<>();
try {

		List<String> offerLetterNos = offerLetterRepository.findOfferLetterNos();
		InspectionLevel inspLevel = null;
		if (offerLetterDto.getInspectionLevelId() != null) {
			Optional<InspectionLevel> inspLevelOptional = inspLevelRepository
					.findById(offerLetterDto.getInspectionLevelId());
			if (inspLevelOptional.isPresent()) {
				inspLevel = inspLevelOptional.get();
			}
		}

		List<LineItemSerialNo> regNos = lineItemSerialNoRepository
				.findPassedRegsByMergedSSlipId(offerLetterDto.getLotId());

		if (regNos == null || regNos.isEmpty()) {
			throw new IllegalArgumentException(
					"No passed registration number found for merged slip ID: " + offerLetterDto.getLotId());
		}

		SOLineItem soLineItem = soLineItemRepository.getSoLineItemByLotId(offerLetterDto.getLotId());
		if (!offerLetterNos.contains(offerLetterDto.getOlNumber())) {
			OfferLetter offerLetter = null;
			offerLetter = new OfferLetter();

			if (!InputNameValidation.checkString(offerLetterDto.getOlNumber()))
				throw new IllegalArgumentException("Special characters other than '-', '/', '.', '()' and '@' .  ");

			if (offerLetterDto.getWarrantId() == null || offerLetterDto.getLotId() == null
					|| offerLetterDto.getConsigneeId() == null)
				throw new IllegalArgumentException("WarrantId/ MergeSubSlipId/ ConsigneeId cannot be null.");

			MergeSubSlip msubslip = mergeSubSlipRepository.findById(offerLetterDto.getLotId())
					.orElseThrow(() -> new IllegalArgumentException("MergeSlip not found"));
			offerLetter.setLot(msubslip);
			mergeSubSlipService.updateMergeSubSLipStatus(offerLetterDto.getLotId(),
					ApplicationConstants.COMMON_STATUS_CONSTANTS.OL_GENERATED);

			Consignee consignee = consigneeRepository.findById(offerLetterDto.getConsigneeId())
					.orElseThrow(() -> new IllegalArgumentException("Consignee not found"));
			offerLetter.setConsignee(consignee);

			offerLetter.setOlNumber(offerLetterDto.getOlNumber());
			offerLetter.setCreatedDate(todayDate);
			offerLetter.setActive(true);
			offerLetter.setOfferDate(sdf.parse(offerLetterDto.getOfferDate()));
			offerLetter.setCreatedBy(offerLetterDto.getEmployeeId());

      if (inspLevel != null) offerLetter.setInspectionLevel(inspLevel);
      BigDecimal offeredQty = BigDecimal.ZERO;
      BigDecimal remainingQty= BigDecimal.ZERO;
			if (soLineItem != null) {
				if(soLineItem.getRegistrationNo().getId()== ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {					
					 offeredQty = soLineItem.getOrderQty();
				}
				else {
				
					offeredQty = BigDecimal.valueOf(regNos.size()) ; // offerLetterDto.get .getOfferedQty();
					remainingQty = olLineItemRepository.getRemainingQtyBySoLineItemId(soLineItem.getId());
					remainingQty = (remainingQty == null) ? soLineItem.getOrderQty() : remainingQty;
				}
							    
				OlLineItem olLineItem = new OlLineItem();
				olLineItem.setCreatedDateTime(todayDate);
				olLineItem.setCreatedBy(offerLetterDto.getEmployeeId());
				olLineItem.setOfferLetter(offerLetter);
				olLineItem.setSoLineItem(soLineItem);

				olLineItem.setOfferedQty(offeredQty);
				List<OlLineItem> existingLineItems = offerLetter.getOlLineItems() != null ? offerLetter.getOlLineItems()
						: new ArrayList<>();
				existingLineItems.add(olLineItem);
				offerLetter.setOlLineItems(existingLineItems);
				offerLetter.setSaleOrder(soLineItem.getSaleOrder());
				offerLetter.setInternal(offerLetterDto.getIsInternal());

				List<Integer> sslipIds = mergeSubSlipRepository.findsubSlipsByMergedId(offerLetterDto.getLotId());
				List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(sslipIds.get(0));
				String secCode = null;
				if (obj != null) {
					for (Object[] det : obj) {
						secCode = det[2] != null ? det[2].toString() : null;
					}
				}

				offerLetter.setDepartment(departmentRepository.findBySectionCode(Integer.parseInt(secCode)));
				offerLetters.add(offerLetter);
			} else {
				throw new IllegalArgumentException("Line item no: doesn't belong to selected Sale Order");
			}
		} else {
			throw new IllegalArgumentException("OL exists with name:" + offerLetterDto.getOlNumber());
		}

		List<OfferLetter> savedOfferLetters = offerLetterRepository.saveAll(offerLetters);

		for (OfferLetter offerLetter : savedOfferLetters) {

			List<OlLineItem> olLineItems = offerLetter.getOlLineItems();
			OlLineItem targetOlLineItem = null;
			for (OlLineItem olLineItem : olLineItems) {
				if (olLineItem.getSoLineItem().getId().equals(soLineItem.getId())) {
					targetOlLineItem = olLineItem;
					break;
				}
			}

			List<OLLineItemSerialNo> serialNos = new ArrayList<>();
			for (LineItemSerialNo regNo : regNos) {
				OLLineItemSerialNo serialNo = new OLLineItemSerialNo();
				serialNo.setSerialNo(regNo);
				serialNo.setOlLineItem(targetOlLineItem);
				serialNo.setActive(true);
				
				if(soLineItem.getRegistrationNo().getId() == ApplicationConstants.REGISTRATION_NO_TYPE.WITHOUT_REGISTRATION_NUMBER) {
					serialNo.setIsSample(true);
				}
				serialNos.add(serialNo);
			}

			olLineItemSerialNoRepository.saveAll(serialNos);

			regNos.forEach(regNo -> regNo.setOlId(offerLetter));

			lineItemSerialNoRepository.saveAll(regNos);

			// Add OlId to InternalInote table if present
			InternalINote intInote = internalINoteRepository.findByMergeSubSlipId(offerLetter.getLot().getId())
					.orElse(null);
			if (intInote != null) {
				intInote.setOfferLetter(offerLetter);
				intInote.setOfferLetterNo(offerLetter.getOlNumber());
				internalINoteRepository.save(intInote);
			}

		}
		return "Data saved successfully.";
}catch (Exception e){
	e.printStackTrace();
	return "Error occurred while saving data: " + e.getMessage(); 
}

	}
	
	//Update LineItemSerialNo status
	private void updateLineItemSerialNoStatus(Integer olId, INote savedINote) {
		List<OLLineItemSerialNo> olLineItemSerialNos = olLineItemSerialNoRepository.findRegNosByOlId(olId);

		LineItemSerialNoDto passedRegNosDto = new LineItemSerialNoDto();

		List<Integer> passedRegIds = new ArrayList<>();
		List<Integer> failedRegIds = new ArrayList<>(); // RFR

		olLineItemSerialNos.forEach(olisn -> {
			boolean isPassed = olisn.getStatus() != null
					&& olisn.getStatus().equalsIgnoreCase(REGISTRATION_CONSTANTS.PASS);
			boolean isMarkedAsRfr = olisn.getSerialNo().getStatus().getStatus()
					.equalsIgnoreCase(REGISTRATION_CONSTANTS.RFR);
			if (isPassed && !isMarkedAsRfr) {
				passedRegIds.add(olisn.getSerialNo().getId());
			}
			if (isMarkedAsRfr) {
				failedRegIds.add(olisn.getSerialNo().getId());
			}
		});

		passedRegNosDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.I_NOTE_GENERATED);
		passedRegNosDto.setIds(passedRegIds);

		if (!passedRegIds.isEmpty())
			lineItemSerialNoservice.update(passedRegNosDto);

		if (!passedRegNosDto.getIds().isEmpty()) {
			LineItemSerialNoHistoryDto passedRegsHistoryDto = new LineItemSerialNoHistoryDto();
			passedRegsHistoryDto.setCreatedById(savedINote.getCreatedBy());
			passedRegsHistoryDto.setLineItemSerialNoIds(passedRegNosDto.getIds());
			passedRegsHistoryDto.setFkName(savedINote.getINoteNo());
			passedRegsHistoryDto.setFkId(savedINote.getId());
			passedRegsHistoryDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.I_NOTE_GENERATED);
			lineItemSerialNoservice.addHistory(passedRegsHistoryDto);
		}

		if (!failedRegIds.isEmpty()) {
			LineItemSerialNoHistoryDto failedRegsHistoryDto = new LineItemSerialNoHistoryDto();
			failedRegsHistoryDto.setCreatedById(savedINote.getCreatedBy());
			failedRegsHistoryDto.setLineItemSerialNoIds(failedRegIds);
			failedRegsHistoryDto.setFkName(savedINote.getINoteNo());
			failedRegsHistoryDto.setFkId(savedINote.getId());
			failedRegsHistoryDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.RFR);
			lineItemSerialNoservice.addHistory(failedRegsHistoryDto);
		}

	}

	public String getPreviewInoteNo() {
		String currentFY = getFinancialYear();

		Integer maxCounter = cqaiInoteNoRepository.findMaxCounterByFinancialYear(currentFY);

		int nextCounter = (maxCounter == null) ? 1 : maxCounter + 1;

		return String.format("%s%05d/%s", ApplicationConstants.INOTE_PREFIX, nextCounter, currentFY);
	}

	private String getFinancialYear() {
		LocalDate now = LocalDate.now();
		int year = now.getYear() % 100;
		int month = now.getMonthValue();

		String fy;
		if (month >= 4) {
			fy = year + "-" + (year + 1);
		} else {
			fy = (year - 1) + "-" + year;
		}

		return fy;
	}

	public List<Map<String, Object>> getRegsByOlIdQa(Integer olId) {

		if (!offerLetterRepository.existsById(olId))
			throw new IllegalArgumentException("OfferLetter not found for id: " + olId);

		List<Object[]> regsData = offerLetterRepository.getRegsByOlIdQa(olId);

		List<Map<String, Object>> returnMap = new ArrayList<>();

		regsData.forEach(reg -> {
			Map<String, Object> map = new HashMap<>();
			map.put("olId", Integer.parseInt(reg[0].toString()));
			map.put("serialNoId", Integer.parseInt(reg[1].toString()));
			map.put("serialNo", String.valueOf(reg[2]));
			map.put("olLiId", Integer.parseInt(reg[3].toString()));
			map.put("roundId", Integer.parseInt(reg[4].toString()));
			map.put("productId", Integer.parseInt(reg[5].toString()));
			returnMap.add(map);
		});

		return returnMap;

	}
}
