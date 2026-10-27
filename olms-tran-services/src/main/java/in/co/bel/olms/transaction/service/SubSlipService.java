package in.co.bel.olms.transaction.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.olms.ate.repository.InspectionRoundRepository;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.constants.ApplicationConstants.COMMON_STATUS_CONSTANTS;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.InspectionLevel;
import in.co.bel.olms.master.entity.SerialNoStatus;
import in.co.bel.olms.master.entity.Status;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.ConsigneeRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.InspectionLevelRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.LineItemLotsRegisterDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.SubSlipDetByInspDto;
import in.co.bel.olms.transaction.dto.SubSlipDto;
import in.co.bel.olms.transaction.dto.SubSlipLineItemDto;
import in.co.bel.olms.transaction.dto.SubSlipSerialNoDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.entity.WarrantDetails;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.LineItemSerialNoRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipMappingRepository;
import in.co.bel.olms.transaction.repository.OlLineItemRepository;
import in.co.bel.olms.transaction.repository.SoLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipAllotmentRepository;
import in.co.bel.olms.transaction.repository.SubSlipApprovalDetailsRepository;
import in.co.bel.olms.transaction.repository.SubSlipLineItemRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.transaction.repository.WarrantDetailsRepository;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubSlipService {

	@Value("${file.domain-url}")
	private String domainUrl;

	@Value("${spring.application.name}")
	private String applicationName;

	private final SubmissionSlipRepository submissionSlipRepository;	
	
	private final InspectionLevelRepository inspLevelRepository;

	private final SoLineItemRepository soLineItemRepository;

	private final OlLineItemRepository olLineItemRepository;

	private final SubSlipLineItemRepository subSlipLineItemRepository;

	private final CategoryRepository categoryRepository;

	private final ConsigneeRepository consigneeRepository;

	private final LineItemSerialNoRepository lineItemSerialNoRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final WarrantDetailsRepository warrantDetailsRepository;

	private final Mapper mapper;

	private final SubSlipAllotmentRepository subSlipAllotmentRepository;

	private final SubSlipApprovalDetailsRepository subSlipApprovalDetailsRepository;

	private final LineItemSerialNoService lineItemSerialNoService;

	private final StatusRepository statusRepository;

	private final EmployeeRepository employeeRepository;

	private final MergeSubSlipMappingRepository mergeSubSlipMappingRepository;
	
	private final InspectionRoundRepository inspectionRoundRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SubSlipService.class);
	
	public SubmissionSlip getOfferLetterById(int id) {

		Optional<SubmissionSlip> offerLetterOptional = submissionSlipRepository.findById(id);
		if (offerLetterOptional.isPresent()) {
			return offerLetterOptional.get();
		} else {
			throw new DataNotFoundException("OfferLetter not found by Id: " + id);
		}
	}
	
	public boolean updateOlStatus(int offerLetterId, int status) {

		Optional<SubmissionSlip> olOptional = submissionSlipRepository.findById(offerLetterId);
		if (olOptional.isPresent()) {
			SubmissionSlip offerLetter = olOptional.get();
			Optional<Status> olStatusOptional = statusRepository.findById(status);
			if (olStatusOptional.isPresent()) {
				Status olStatus = olStatusOptional.get();
				offerLetter.setOlStatus(olStatus);
				submissionSlipRepository.save(offerLetter);
				return true;
			} else {
				throw new DataNotFoundException("No OL Status found with given status Id: " + status);
			}
		} else {
			throw new DataNotFoundException("No OL found with given OL Id: " + offerLetterId);
		}

	}
	
	@Transactional
	public Map<Boolean, String> saveOfferLetterWarrant(SubSlipDto subSlipDto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<Boolean, String> returnMap = new HashMap<>();
		Date todayDate = new Date();
		try {
			Set<SubmissionSlip> offerLetters = new HashSet<>();
			List<SubSlipLineItemDto> olLineItemDtos = subSlipDto.getSubSlipLineItemDtos();
			List<String> offerLetterNos = submissionSlipRepository.findOfferLetterNos();
			InspectionLevel inspLevel = null;
			if (subSlipDto.getInspectionLevelId() != null) {
				Optional<InspectionLevel> inspLevelOptional = inspLevelRepository
						.findById(subSlipDto.getInspectionLevelId());
				if (inspLevelOptional.isPresent()) {
					inspLevel = inspLevelOptional.get();
				}
			}			
			SOLineItem soLineItem = soLineItemRepository.getSoLineItemsByCategoryId(subSlipDto.getWarrantId());

			for (SubSlipLineItemDto olLineItemDto : olLineItemDtos) {
				if (!offerLetterNos.contains(subSlipDto.getOlNumber())) {
					SubmissionSlip offerLetter = null;
						offerLetter = new SubmissionSlip();
						if (!InputNameValidation.checkString(subSlipDto.getOlNumber())) {

							String respString = "Special characters other than '-', '/', '.', '()' and '@' .  ";
							logger.error(respString);
							returnMap.put(false, respString);
							return returnMap;
						}
						
						Category warrant =categoryRepository.getById(subSlipDto.getWarrantId());
						offerLetter.setWarrant(warrant)  ;    	
					
						Optional<Status> olStatusOptional = statusRepository.findById(14);
						if (olStatusOptional.isPresent()) {
							offerLetter.setOlStatus(olStatusOptional.get());
						}
						
						offerLetter.setOlNumber(subSlipDto.getOlNumber());
						offerLetter.setCreatedDate(todayDate);
						offerLetter.setActive(true);
						offerLetter.setOfferDate(sdf.parse(subSlipDto.getOfferDate()));
						offerLetter.setCreatedBy(subSlipDto.getEmployeeId());

						if (inspLevel != null) {
							offerLetter.setInspectionLevel(inspLevel);
						}
						if (subSlipDto.getConsigneeId() != null) {
							Optional<Consignee> consigneeOptional = consigneeRepository
									.findById(subSlipDto.getConsigneeId());
							if (consigneeOptional.isPresent()) {
								offerLetter.setConsignee(consigneeOptional.get());
							}
						}

					if (soLineItem != null) {

						BigDecimal offeredQty = olLineItemDto.getOfferedQty();
						BigDecimal remainingQty = olLineItemRepository.getRemainingQtyBySoLineItemId(soLineItem.getId());
						remainingQty = (remainingQty == null) ? soLineItem.getOrderQty() : remainingQty;

						SubSlipLineItem olLineItem = new SubSlipLineItem();
						olLineItem.setCreatedDateTime(todayDate);
						olLineItem.setCreatedBy(subSlipDto.getEmployeeId());
						olLineItem.setSubmissionSlipId(offerLetter);
						olLineItem.setSoLineItem(soLineItem);
						olLineItem.setOfferedQty(offeredQty);

						List<SubSlipLineItem> existingLineItems = offerLetter.getSubSlipLineItems();
						if (existingLineItems == null) {
							existingLineItems = new ArrayList<>();
						}
						existingLineItems.add(olLineItem);
						offerLetter.setSubSlipLineItems(existingLineItems);
						offerLetter.setInternal(subSlipDto.getIsInternal());
						offerLetters.add(offerLetter);
					} else {
						String respString = "Line item no:" + olLineItemDto.getSoLineItemNo()
								+ " doesn't belong to selected Sale Order";
						returnMap.put(false, respString);
						return returnMap;
					}
				} else {
          String respString = "Submission slip exists with name: " + subSlipDto.getOlNumber();
					logger.error(respString);
					returnMap.put(false, respString);
					return returnMap;
				}
			}

			List<SubmissionSlip> savedOfferLetters = submissionSlipRepository.saveAll(offerLetters);

			for (SubmissionSlip offerLetter : savedOfferLetters) {
				for (SubSlipLineItemDto olLineItemDto : olLineItemDtos) {
					LineItemLotsRegisterDto dto= olLineItemDto.getRegisterNumbers();
					List<LineItemSerialNo> serialNos = new ArrayList<>();
					for (Integer regno: dto.getIds()) {
						LineItemSerialNo serialNo = lineItemSerialNoRepository.findById(regno)
								.orElseThrow(() -> new IllegalArgumentException("LineItemSerialNo not found"));
						serialNo.setSubmissionSlipId(offerLetter);
						
						SerialNoStatus status = new SerialNoStatus();
						status.setId(SERIAL_NO_STATUS_CONSTANTS.SUB_SLIP_CREATED);
						serialNo.setStatus(status);
						serialNos.add(serialNo);
					}

					lineItemSerialNoRepository.saveAll(serialNos);

					LineItemSerialNoHistoryDto historyDto = new LineItemSerialNoHistoryDto();
					historyDto.setCreatedById(subSlipDto.getEmployeeId());
					historyDto.setLineItemSerialNoIds(dto.getIds());
					historyDto.setFkName(offerLetter.getOlNumber());
					historyDto.setFkId(offerLetter.getId());
					historyDto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.SUB_SLIP_CREATED);
					lineItemSerialNoService.addHistory(historyDto);
					
				}
				
//				for(int id: subSlipDto.getSubSlipLineItemDtos().get)
				List<SubSlipLineItem> olLineItems = offerLetter.getSubSlipLineItems();

				for (SubSlipLineItemDto olLineItemDto : olLineItemDtos) {
					// Check if the offerLetterNo matches the current SubSlipLineItemDto
					//if (offerLetter.getOlNumber().equals(olLineItemDto.getOfferLetterNo())) {
						// Find the matching SubSlipLineItem based on SoLineItemNo
						SubSlipLineItem targetOlLineItem = null;
						for (SubSlipLineItem olLineItem : olLineItems) {
							if (olLineItem.getSoLineItem().getId().equals(soLineItem.getId())) {
								targetOlLineItem = olLineItem;
								olLineItemDto.setId(olLineItem.getId());
								break;
							}
						}

						if (targetOlLineItem != null && !olLineItemDto.getRegisterNumbers().getIds().isEmpty()) {
							LineItemLotsRegisterDto requestDto = new LineItemLotsRegisterDto();
							requestDto.setOlLineItemId(targetOlLineItem.getId());
							requestDto.setIds(olLineItemDto.getRegisterNumbers().getIds());

							List<SubSlipSerialNoDto> subSlipSerialNoDtos = new ArrayList<>();
							List<Integer> serialNoIds = olLineItemDto.getRegisterNumbers().getIds();
							serialNoIds.forEach(serialNoId -> {
								SubSlipSerialNoDto subSlipSerialNoDto = new SubSlipSerialNoDto();
								subSlipSerialNoDto.setSubSlipId(offerLetter.getId());
								subSlipSerialNoDto.setSubSlipLineItemId(olLineItemDto.getId());
								subSlipSerialNoDto.setCreatedById(subSlipDto.getEmployeeId());
								subSlipSerialNoDto.setFinalStatus("in-process"); // to be discussed
								subSlipSerialNoDto.setSerialNoId(serialNoId);
								
								WarrantDetails wd = warrantDetailsRepository.findByLineItemSerialNoId(serialNoId, subSlipDto.getWarrantId()).orElse(null);
								subSlipSerialNoDto.setWarrantDetailsId(wd.getId());

								subSlipSerialNoDtos.add(subSlipSerialNoDto);
							});
							
							List<SubSlipSerialNo> serialNos = subSlipSerialNoDtos.stream().map(dto -> mapper.map(dto, SubSlipSerialNo.class)).toList();
							subSlipSerialNoRepository.saveAll(serialNos);
							
						} else {
							String respString = (targetOlLineItem == null) ? "No matching SubSlipLineItem found"
									: "No Register Numbers provided";
							logger.warn(respString);
						}
				
				}
			}
			returnMap.put(true, "Data saved successfully.");
			return returnMap;
		} catch (Exception e) {
        	logger.error("Error occured while saving OL ", e);
			logger.error("Error in saving OL {}", e.getMessage());
			returnMap.put(false, "Some error occurred. Plz try again.");
			return returnMap;
		}
	}

	public List<SubSlipDto> getSubSlipsByWarrantId(Integer warrantId) {		
		List<SubmissionSlip> subSlips = submissionSlipRepository.findByWarrantId(warrantId);		
		return subSlips.stream().map(subSlip -> mapper.map(subSlip, SubSlipDto.class)).toList();		
	}
	
	
	public List<SubSlipDto> getSubSlipsBySalesGrpId(Integer salesGrpId, Integer warrantId, List<Integer> statusIds) {
		List<Object[]> ols = new ArrayList<>();

		if(statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS, COMMON_STATUS_CONSTANTS.COMPLETED); // created, in-progress, completed
		
		if(warrantId == null) {
			ols = submissionSlipRepository.getSubSlipBySalesGrpId(salesGrpId, statusIds);
		} else {
			ols = submissionSlipRepository.findByWarrantIdAndSalesGrpId(salesGrpId, warrantId);
		}

		List<SubSlipDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			SubSlipDto dto = new SubSlipDto();
			int index=0;
			dto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			dto.setOlNumber((ol[index].toString()));
			index++;
			dto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null );
			index++;
			dto.setINoteTypeId(ol[index] != null ? (int) ol[index] : null);
			index++;
			dto.setOlStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspectionLevelStr(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setIsNotified(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setIsGcc(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setSoId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setConsigneeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrantId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrant(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setEpCode(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setNomenclature(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspAuthority(ol[index]!=null ? ol[index].toString(): null);		
			
			try {			
				String allotetdToNames = subSlipAllotmentRepository.getAllotedSubSlipToEmployeeName(Integer.parseInt(ol[0].toString()));				
				dto.setAllotedToName(allotetdToNames);
			} catch (Exception e) {
				logger.info("Alloted details is empty");
			}
			
			dtos.add(dto);

		}
		return dtos;
	}

	public List<SubSlipDto> getSubSlipBySalesGrpIdEmpId(Integer salesGrpId, Integer warrantId, Integer empId, List<Integer> statusIds) {
		List<Object[]> ols = new ArrayList<>();

		if(statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS, COMMON_STATUS_CONSTANTS.COMPLETED); // created, in-progress, completed
		
		if(warrantId == null) {
			ols = submissionSlipRepository.getSubSlipBySalesGrpIdEmpId(salesGrpId, empId, statusIds);
		} else {
			ols = submissionSlipRepository.findByWarrantIdAndSalesGrpId(salesGrpId, warrantId);
		}

		List<SubSlipDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			SubSlipDto dto = new SubSlipDto();
			int index=0;
			dto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			dto.setOlNumber((ol[index].toString()));
			index++;
			dto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null );
			index++;
			dto.setINoteTypeId(ol[index] != null ? (int) ol[index] : null);
			index++;
			dto.setOlStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspectionLevelStr(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setIsNotified(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setIsGcc(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setSoId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setConsigneeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrantId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrant(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setEpCode(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setNomenclature(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspAuthority(ol[index]!=null ? ol[index].toString(): null);
		
			try {			
				String allotetdToNames = subSlipAllotmentRepository.getAllotedSubSlipToEmployeeName(Integer.parseInt(ol[0].toString()));				
				dto.setAllotedToName(allotetdToNames);
			} catch (Exception e) {
				logger.info("Alloted details is empty");
			}
			
			dtos.add(dto);

		}
		return dtos;
	}
	
	/**
	 * Modified on 29-07-2026
	 * 
	 * @return
	 */
	
	public List<SubSlipDto> getSubSlipBySalesGrpIdForDOA(String roleId, Integer salesGrpId, List<Integer> statusIds) {
		
		List<Object[]> ols = new ArrayList<>();
	
		if(statusIds == null || statusIds.isEmpty())
			statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.IN_PROGRESS, COMMON_STATUS_CONSTANTS.COMPLETED); // created, in-progress, completed
		
		if(roleId.contains("19")) {
			ols = submissionSlipRepository.getSubSlipBySalesGrpIdEmpId(salesGrpId, statusIds);
		} else {
			throw new IllegalArgumentException("Submission slip not found for the Role DOA : " + roleId);
		}
	
		List<SubSlipDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			SubSlipDto dto = new SubSlipDto();
			int index=0;
			dto.setId(Integer.parseInt(ol[index].toString()));
			index++;
			dto.setOlNumber((ol[index].toString()));
			index++;
			dto.setStatusId(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setOfferDate(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSalesGroupName(ol[index] != null ? ol[index].toString() : null );
			index++;
			dto.setINoteTypeId(ol[index] != null ? (int) ol[index] : null);
			index++;
			dto.setOlStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspectionLevelStr(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setIsNotified(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setIsGcc(ol[index] != null ? (Boolean) ol[index] : false);
			index++;
			dto.setSoId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setConsigneeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrantId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			dto.setWarrant(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setEpCode(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setNomenclature(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);
			index++;
			dto.setInspAuthority(ol[index]!=null? ol[index].toString(): null);
		
			try {			
				String allotetdToNames = subSlipAllotmentRepository.getAllotedSubSlipToEmployeeName(Integer.parseInt(ol[0].toString()));				
				dto.setAllotedToName(allotetdToNames);
			} catch (Exception e) {
				logger.info("Alloted details is empty");
			}
			
			dtos.add(dto);
	
		}
		return dtos;
	}

/**
 * Modified on 05-02-2026
 * @param orgId
 * @param empId
 * @return
 */
	public List<OfferLetterDto> getAllottedSubSlipsByStatusForFirm(int orgId, int empId) {
		List<Object[]> ols = submissionSlipRepository.getAllottedSubSlipsByStatusForFirm(orgId, empId);

		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			int index=0;
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
			offerletterDto.setNomenclature(ol[index] != null ? ol[index].toString() : null); // project name == nomenclature
			index++;
			offerletterDto.setINoteTypeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setFileNoteStatus(ol[index] != null ? ol[index].toString() : null);
			offerletterDto.setOlStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);  // sale order no == work order no
			index++;
			offerletterDto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setEpCode(ol[index] != null ? ol[index].toString() : null);  // EP code == part number
			index++;
			offerletterDto.setWarrant(ol[index] != null ? ol[index].toString() : null);  // warrant == category
			index++;			
			
			try {			
				
				String allotetdToNames = subSlipAllotmentRepository.getAllotedSubSlipToEmployeeName(Integer.parseInt(ol[0].toString()));				
				offerletterDto.setAllotedToName(allotetdToNames);
			} catch (Exception e) {
				logger.info("Alloted details is empty");
			}
			
			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
		return dtos;
	}
	
	public List<OfferLetterDto> getAllottedSubSlipsApprovalByStatusForFirm(int orgId, int empId) {
		List<Object[]> ols = new ArrayList<>();		
		Integer roleId = null;
		Employee emp = employeeRepository.findById(empId).orElse(null);
		if(emp !=null)
		{
			roleId = emp.getEmpRole().getId();
		}
		
		if(roleId == MasterConstants.EMP_ROLE.ID_EXAMINER || roleId == MasterConstants.EMP_ROLE.ID_INSPECTOR || roleId == MasterConstants.EMP_ROLE.ID_HOSQC) {
			List<Object[]> data =submissionSlipRepository.getAllottedSubSlipsApprovalForExaminerInspector(orgId, empId);
			List<Integer> subSlipIds = data.stream()
			        .map(row -> ((Integer) row[0]).intValue())
			        .toList();
			if(roleId == MasterConstants.EMP_ROLE.ID_HOSQC) {
				Set<Integer> subSlipAlloted = new HashSet<>(
						subSlipApprovalDetailsRepository.findAllotedSubSlipsIds(subSlipIds, empId));
				Set<Integer> existingIdsInLot = new HashSet<>(
						mergeSubSlipMappingRepository.findExistingSubSlipIds(subSlipIds));
				Set<Integer> compareIds = new HashSet<>(); // Temporary set to track IDs
				ols = data.stream()
					    // checks if sub slip is sent for approval or not	
						.filter(row -> subSlipAlloted.contains(((Integer) row[0]).intValue()))
						// checks if lot is created for sub slip
						.filter(row -> !existingIdsInLot.contains(((Integer) row[0]).intValue()))
					    .filter(row -> compareIds.add(((Integer) row[0]).intValue())) // add() returns false if ID already exists , filtering if still duplicate id exists
					    .toList();
			}else {
				Set<Integer> existingIds = new HashSet<>(
				        subSlipApprovalDetailsRepository.findExistingSubSlipIds(subSlipIds));
				// filters out sub slips if it is approved
				ols = data.stream()
				        .filter(row -> !existingIds.contains(((Integer) row[0]).intValue()))
				        .distinct()
				        .toList();
			}			
		 }else {				
			ols = submissionSlipRepository.getAllottedSubSlipsApprovalByStatusForFirm(orgId, empId);
		}


		List<OfferLetterDto> dtos = new ArrayList<>();
		for (Object[] ol : ols) {
			OfferLetterDto offerletterDto = new OfferLetterDto();
			int index=0;
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
			offerletterDto.setNomenclature(ol[index] != null ? ol[index].toString() : null); // project name == nomenclature
			index++;
			offerletterDto.setINoteTypeId(ol[index] != null ? Integer.parseInt(ol[index].toString()) : null);
			index++;
			offerletterDto.setFileNoteStatus(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setWorkOrderNo(ol[index] != null ? ol[index].toString() : null);  // sale order no == work order no
			index++;
			offerletterDto.setSoNumber(ol[index] != null ? ol[index].toString() : null);
			index++;
			offerletterDto.setEpCode(ol[index] != null ? ol[index].toString() : null);  // EP code == part number
			index++;
			offerletterDto.setWarrant(ol[index] != null ? ol[index].toString() : null);  // warrant == category
			index++;
	
			
			try {				
				String allotetdToNames = subSlipAllotmentRepository.getAllotedSubSlipToEmployeeName(Integer.parseInt(ol[0].toString()));				
				offerletterDto.setAllotedToName(allotetdToNames);
			} catch (Exception e) {
				logger.info("Alloted details is empty");
			}
			
			dtos.add(offerletterDto);

		}
		Collections.sort(dtos, new OfferLetterStatusComparator());
		return dtos;
	}

public List<SubSlipDetByInspDto> getAllSubSlipListByInspOrgId() {
	
	List<SubSlipDetByInspDto> responseDtos = new ArrayList<>();
	
	try {
	List<Object[]> offerLetters = submissionSlipRepository.getAllSubSlipListByInspOrgId();
	
	for(Object[] ol: offerLetters) {
		SubSlipDetByInspDto olDto = new SubSlipDetByInspDto();
		olDto.setOlId((Integer)ol[0]);
		olDto.setSoNumber((String)ol[1]) ;
		olDto.setSoDate((String)ol[2]);
		olDto.setOlNo((String)ol[3]);
		olDto.setOlDate((String)ol[4]);
		olDto.setWorkOrderNo((String)ol[5]);		
		responseDtos.add(olDto);
	}
	}catch (Exception e) {
		logger.error("Error processing offer letters: {}", e.getMessage());
	return new ArrayList<>();
	}
	return responseDtos;
}

public List<SubSlipDetByInspDto> getAllPassedSubSlipListByInspOrgId(int empId) {
	
	List<SubSlipDetByInspDto> responseDtos = new ArrayList<>();
	
	try {
	List<Object[]> offerLetters = submissionSlipRepository.getAllPassedSubSlipListByInspOrgId(empId);
	
	for(Object[] ol: offerLetters) {
		SubSlipDetByInspDto olDto = new SubSlipDetByInspDto();
		olDto.setOlId((Integer)ol[0]);
		olDto.setSoNumber((String)ol[1]) ;
		olDto.setSoDate((String)ol[2]);
		olDto.setOlNo((String)ol[3]);
		olDto.setOlDate((String)ol[4]);
		olDto.setWorkOrderNo((String)ol[5]);
		olDto.setEpcode(ol[6]!=null? (String)ol[6]: null);
		olDto.setNomenclature(ol[7]!= null? (String)ol[7]: null);		
		olDto.setInspAuth(ol[8]!=null?(String)ol[8]: null);
		responseDtos.add(olDto);
	}
	}catch (Exception e) {
		logger.error("Error processing offer letters: {}", e.getMessage());
	return new ArrayList<>();
	}
	return responseDtos;
}

//Code to be changed
public List<SubSlipLineItemDto> getLineItemsByOl(int olId) {
	DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	List<SubSlipLineItemDto> olLineItemDtos = new ArrayList<>();
	List<SubSlipLineItem> olLineItems = subSlipLineItemRepository.getLineItemsByOl(olId);
	for (SubSlipLineItem olLineItem : olLineItems) {
		SubSlipLineItemDto olLineItemDto = mapper.map(olLineItem, SubSlipLineItemDto.class);
		if (olLineItem.getTmrFileName() != null) {
			String fileDownloadUri = domainUrl + applicationName + ApplicationConstants.SEPERATOR + "files/"
					+ olLineItem.getTmrFileName();
			olLineItemDto.setUpdatedTmrFileUrl(fileDownloadUri);
			
		}
		SOLineItem soLineItem = olLineItem.getSoLineItem();
		olLineItemDto.setSoLineItemNo(olLineItem.getSoLineItem().getLineItemNo());
		if (soLineItem != null) {
			olLineItemDto.setStatus(soLineItem.getStatus());
			BigDecimal unitCost = soLineItem.getUnitCost();
			if (unitCost != null) {
				olLineItemDto.setUnitCostStr(df.format(unitCost));
				BigDecimal itemValue = unitCost.multiply(olLineItem.getOfferedQty())  ;
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
			olLineItemDto.setOfferLetterNo(olLineItem.getSubmissionSlip().getOlNumber());
			BigDecimal pendingQty = olLineItemRepository.getPendingQtyByLineItemNo(olLineItem.getId());
			olLineItemDto.setPendingQty(pendingQty != null ? pendingQty : BigDecimal.ZERO);
			;
			if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
				olLineItemDto.setPartNumberId(soLineItem.getPartNumber().getId());
				olLineItemDto.setPartNumberName(soLineItem.getPartNumber().getPartNo());
				olLineItemDto.setPartNo(soLineItem.getPartNumber().getPartNo());
				olLineItemDto.setMaterialDescription(soLineItem.getPartNumber().getMaterialDescription());
				olLineItemDto.setLineItemNo(soLineItem.getLineItemNo());
				olLineItemDto.setItemCategoryName(soLineItem.getPartNumber().getItemCategory().getCategory());
				//Code to be changed
		Integer productId = submissionSlipRepository
						.findProductNoByPartNumberId(soLineItem.getPartNumber().getId());
				olLineItemDto.setProductId(productId);
			}
		}
		olLineItemDtos.add(olLineItemDto);
	}
	return olLineItemDtos;
}

public List<SubSlipSerialNoDto> getRegisterNosBySubSlipLineItemId(Integer subSlipLineItemId) {
		if (subSlipLineItemId == null) {
			throw new IllegalArgumentException("ERROR: No sub-slip-line-Item ID provided.");
		}
		Integer productId = null;
		SubSlipLineItem subSlipLineItem = subSlipLineItemRepository.findById(subSlipLineItemId).orElseThrow(
				() -> new IllegalArgumentException("subSlipLineItem not found for id: " + subSlipLineItemId));

		SOLineItem soLineItem = subSlipLineItem.getSoLineItem();
		if (soLineItem.getPartNumber() != null && soLineItem.getPartNumber().getId() != null) {
			productId = submissionSlipRepository.findProductNoByPartNumberId(soLineItem.getPartNumber().getId());
		}

		List<SubSlipSerialNoDto> subSlipSerialNoDtoList = new ArrayList<>();

		List<SubSlipSerialNo> registerNumbers = subSlipSerialNoRepository
				.getRegisterNosBySubSlipLineItemId(subSlipLineItemId);
		for (SubSlipSerialNo lineItemLotsRegister : registerNumbers) {

			SubSlipSerialNoDto dto = new SubSlipSerialNoDto();
			dto.setId(lineItemLotsRegister.getId());
			dto.setSerialNo(lineItemLotsRegister.getWarrantDetails().getLineItemSerialNo().getSerialNo());
			dto.setSerialNoId(lineItemLotsRegister.getWarrantDetails().getLineItemSerialNo().getId());
			
			dto.setFinalStatus(lineItemLotsRegister.getFinalStatus());
			dto.setSubSlipLineItemId(lineItemLotsRegister.getSubSlipLineItem().getId());
			dto.setRegistrationNo(soLineItem.getRegistrationNo().getId());
			subSlipSerialNoDtoList.add(dto);
		}

		if (registerNumbers.isEmpty() || registerNumbers == null) {
			return List.of(); // returns an empty list
		}

		return subSlipSerialNoDtoList;
	}
	
	@Transactional
	public void deleteSubSlip(int sslipId) {
				
		if(!submissionSlipRepository.existsById(sslipId))
			throw new IllegalArgumentException("Submission slip not found for Id : " + sslipId);
		
		List<Integer> serialNoIds = subSlipSerialNoRepository.getIdsBySubSlipId(sslipId);
		
		//Modified By: Chirag , Date: 13-10-26
		if (!serialNoIds.isEmpty()) {
			// Delete from insp_round first to clear the Foreign Key constraint
			inspectionRoundRepository.deleteBySubSlipSlNoIdIn(serialNoIds);
	        
			subSlipSerialNoRepository.deleteInProcessBySubmissionSlipId(sslipId);
			
			List<LineItemSerialNo> lineItemSerialNos = lineItemSerialNoRepository.getLineItemSerialNosBySubSlipId(sslipId);
			
			for(LineItemSerialNo lineItemSerialNo : lineItemSerialNos) {
				SerialNoStatus status = new SerialNoStatus();
				status.setId(SERIAL_NO_STATUS_CONSTANTS.MAPPED);
				lineItemSerialNo.setStatus(status);
				lineItemSerialNo.setSubmissionSlipId(null);
				lineItemSerialNoRepository.save(lineItemSerialNo);
			}
		}
		
		submissionSlipRepository.deleteById(sslipId);
	}
	
	    /**
	     *  Edit Submission Slip	
	     * 
	     * @author Chirag Swamy
	     * @since 10/04/2026
	     * 
	     */
		@Transactional
		public Map<Boolean, String> updateOfferLetterWarrant(SubSlipDto subSlipDto) {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    Map<Boolean, String> returnMap = new HashMap<>();
		    Date todayDate = new Date();
		    
		    try {
		        // Fetch the existing SubmissionSlip
		        SubmissionSlip existingOfferLetter = submissionSlipRepository.findById(subSlipDto.getId())
		            .orElseThrow(() -> new IllegalArgumentException("Offer Letter not found with ID: " + subSlipDto.getId()));
		        
		        if (Boolean.TRUE.equals(existingOfferLetter.getIsNotified())) {
		            returnMap.put(false, "Cannot edit: This Offer Letter has already been notified.");
		            return returnMap;
		        }

		        // --- RESET LINE ITEM SERIAL NUMBERS ---
		        // Find all serial numbers currently linked to this Submission Slip and set their link to null
		        lineItemSerialNoRepository.resetSubmissionSlipIdForSlip(subSlipDto.getId());

		        // --- DELETE IN-PROCESS SUB-SLIP SERIAL RECORDS ---
		        // If the status is 'in-process', remove them to allow a fresh re-insertion
		        subSlipSerialNoRepository.deleteInProcessBySubmissionSlipId(subSlipDto.getId());

		        // Update Parent Fields
		        existingOfferLetter.setOlNumber(subSlipDto.getOlNumber());
		        existingOfferLetter.setOfferDate(sdf.parse(subSlipDto.getOfferDate()));
		        existingOfferLetter.setModifiedDate(new Date());
		        existingOfferLetter.setModifiedBy(subSlipDto.getEmployeeId());

		        // Handle Line Items
		        SOLineItem soLineItem = soLineItemRepository.getSoLineItemsByCategoryId(subSlipDto.getWarrantId());
		        if (soLineItem == null) {
		        	
		        	logger.warn("Validation failed: Warrant ID {} does not correspond to a valid Sale Order Line Item", subSlipDto.getWarrantId());
		        	throw new Exception("Line item doesn't belong to selected Sale Order.");
		        }

		        Employee emp = employeeRepository.findById(subSlipDto.getEmployeeId())
		                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

		        for (SubSlipLineItemDto olLineItemDto : subSlipDto.getSubSlipLineItemDtos()) {
		            // Find Existing Line Items
		            SubSlipLineItem lineItem = existingOfferLetter.getSubSlipLineItems().stream()
		                .filter(li -> li.getSoLineItem().getId().equals(soLineItem.getId()))
		                .findFirst()
		                .orElse(null);

		            if (lineItem == null) {
		                lineItem = new SubSlipLineItem();
		                lineItem.setCreatedDateTime(todayDate);
		                lineItem.setCreatedBy(subSlipDto.getEmployeeId());
		                lineItem.setSubmissionSlipId(existingOfferLetter);
		                lineItem.setSoLineItem(soLineItem);
		                lineItem = subSlipLineItemRepository.save(lineItem);
		            } else {
		                lineItem.setModifiedDateTime(new Date());
		                lineItem.setModifiedBy(subSlipDto.getEmployeeId());
		                lineItem.setOfferedQty(olLineItemDto.getOfferedQty());
		                subSlipLineItemRepository.save(lineItem);
		            }

		            // Handle Serial Numbers
		            if (olLineItemDto.getRegisterNumbers() != null && !olLineItemDto.getRegisterNumbers().getIds().isEmpty()) {
		                List<Integer> serialNoIds = olLineItemDto.getRegisterNumbers().getIds();

		                for (Integer sId : serialNoIds) {
		                    // Find and Update the Master Serial Number record (Re-linking)
		                    LineItemSerialNo masterSerialNo = lineItemSerialNoRepository.findById(sId)
		                            .orElseThrow(() -> new IllegalArgumentException("SerialNo not found: " + sId));
		                    
		                    //Update the Reg Nos that have status: "Mapped" or "SSlip Created" and (sts_id: 2 or 3) 
		                    Integer statusId = (masterSerialNo.getStatus() != null) ? masterSerialNo.getStatus().getId() : null;	                    
		                    if (Integer.valueOf(3).equals(statusId) || Integer.valueOf(2).equals(statusId)) {
		                    	SerialNoStatus status = new SerialNoStatus();
		                    	if(Integer.valueOf(2).equals(statusId)) {
		                    		status.setId(SERIAL_NO_STATUS_CONSTANTS.SUB_SLIP_CREATED);
		                    		masterSerialNo.setStatus(status);
		                    		logger.info("Status: {}",masterSerialNo.getStatus());
		                    	}
			                    masterSerialNo.setSubmissionSlipId(existingOfferLetter);
			                    masterSerialNo.setModifiedAt(new Date());
			                    lineItemSerialNoRepository.save(masterSerialNo);
		
			                    // Handle the Warrant Details linking
			                    WarrantDetails wd = warrantDetailsRepository.findByLineItemSerialNoId(sId, subSlipDto.getWarrantId())
			                            .orElse(null);
		
			                    if (wd != null) {
                            //  create new Sub SliP SerialNo
                            SubSlipSerialNo subSlipSerialNo = new SubSlipSerialNo();
                            subSlipSerialNo.setSubSlip(existingOfferLetter);
                            subSlipSerialNo.setSubSlipLineItem(lineItem);
                            subSlipSerialNo.setFinalStatus(ApplicationConstants.REGISTRATION_CONSTANTS.INPROCESS);
                            subSlipSerialNo.setCreatedBy(emp);
                            subSlipSerialNo.setWarrantDetails(wd);
                            subSlipSerialNoRepository.save(subSlipSerialNo);
			                    }
		                    } else {
//		                        logger.warn("Skipping update for Serial No ID: {} because serNoStsId is not 2 or 3 (Current status: {})", 
//		                                     sId, masterSerialNo.getStatus())
		                    	 throw new Exception("Validation Failed: Serial Number " + sId + " is not in Mapped or SSlip Created status.");
		                    }
		                }
		            }
		        }
		        submissionSlipRepository.save(existingOfferLetter);
		        returnMap.put(true, "Data updated successfully.");
		        return returnMap;
		    } catch (Exception e) {
		    	logger.error("Error in updating OL {}: {}", subSlipDto.getId(), e.getMessage(), e);
		    	returnMap.put(false, e.getMessage());
		        throw new RuntimeException("Transaction failed. Rolling back changes. Original error: " + e.getMessage());
		    }
		}
		public String getSoNumberBySubSlipId(int subSlipId) {
		String soNumber = null;
		try {
			soNumber = submissionSlipRepository.getSoNumberBySubSlipId(subSlipId);
    } catch (Exception e) {
			logger.error("Error in fetching so number by sub slip id {}, due to {}", subSlipId, e.getMessage());
		}
		return soNumber;
	}

  @Transactional
  public void checkAndUpdateSslipStatus(Integer id, int status, boolean isInternalInote) {
    String idName = isInternalInote ? "MergeSubSlipId " : "OlId ";
    if (id == null)
      throw new IllegalArgumentException(idName + "cannot be null");
    List<SubmissionSlip> subSlips = isInternalInote ? submissionSlipRepository.findByMergedSslipId(id) : submissionSlipRepository.findByOlId(id);
    if (subSlips.isEmpty())
      throw new IllegalArgumentException("Submission slips not found for " + idName + id);
    Status olStatus = statusRepository.findById(status)
        .orElseThrow(() -> new IllegalArgumentException("No OL Status found with given status Id: " + status));
    subSlips.forEach(submissionSlip -> submissionSlip.setOlStatus(olStatus));
    submissionSlipRepository.saveAll(subSlips);
  }
}

