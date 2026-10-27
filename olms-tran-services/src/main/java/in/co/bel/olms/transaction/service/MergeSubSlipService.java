package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.core.constants.RegistrationConstants;
import in.co.bel.olms.constants.ApplicationConstants.COMMON_STATUS_CONSTANTS;
import in.co.bel.olms.constants.ApplicationConstants.SERIAL_NO_STATUS_CONSTANTS;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.Status;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.StatusRepository;
import in.co.bel.olms.transaction.dto.LineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.LineItemSerialNoHistoryDto;
import in.co.bel.olms.transaction.dto.LotAllotmentDto;
import in.co.bel.olms.transaction.dto.LotAllotmentEmpDto;
import in.co.bel.olms.transaction.dto.MergeSubSlipDto;
import in.co.bel.olms.transaction.dto.MergedOlDetDto;
import in.co.bel.olms.transaction.dto.MergedSubSlipDto;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlDetailDto;
import in.co.bel.olms.transaction.entity.LotAllotment;
import in.co.bel.olms.transaction.entity.MergeSubSlip;
import in.co.bel.olms.transaction.entity.MergeSubSlipMapping;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.LotAllotmentRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipMappingRepository;
import in.co.bel.olms.transaction.repository.MergeSubSlipRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.SubSlipSerialNoRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MergeSubSlipService {

	private final MergeSubSlipMappingRepository mergeSubSlipMappingRepository;

	private final MergeSubSlipRepository mergeSubSlipRepository;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final SubSlipSerialNoRepository subSlipSerialNoRepository;

	private final LineItemSerialNoService lineItemSerialNoService;

	private final OfferLetterRepository offerLetterRepository;

	private final DepartmentRepository departmentRepository;

	private final LotAllotmentRepository lotAllotmentRepository;

	private final EmployeeRepository employeeRepository;

	private final NotificationService notificationService;

	private final WSService wsService;

	private final EmployeeDao employeeDao;

	private final StatusRepository statusRepositoy;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(MergeSubSlipService.class);

  public String saveMergeOl(MergeSubSlipDto reqDto) {
    List<Integer> subSlipIds = reqDto.getOlIds();

    if (subSlipIds == null || subSlipIds.isEmpty()) {
      throw new IllegalArgumentException("No subSlipIds provided. Operation completed.");
		}

    if (reqDto.getMergeOlNo() == null || reqDto.getMergeOlNo().trim().isEmpty()) {
      throw new IllegalArgumentException("No mergeOl name provided. Can not save, Operation completed.");
    }
 
    // check if same EpCode and Inspection Agency
    boolean allSamePartNoAndInspAuth = subSlipIds.stream()
            .map(subSlipId -> {

                SubmissionSlip subSlip = submissionSlipRepository.findById(subSlipId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "SubmissionSlip not found: " + subSlipId));

                Integer partNo = subSlip.getWarrant()
                        .getSoLineItem()
                        .getPartNumber()
                        .getId();

                Integer inspAuth = subSlip.getWarrant()
                        .getSoLineItem()
                        .getInspectionAuthorityId()
                        .getId();

                return partNo + "-" + inspAuth;
            })
            .distinct()
            .count() == 1;

    if (!allSamePartNoAndInspAuth) {
        throw new IllegalArgumentException(
                "Submission Slips must have the same Part Number and Inspection Agency.");
    }


    MergeSubSlip mergeSubSlip = mapper.map(reqDto, MergeSubSlip.class);
    Status status = statusRepositoy.findById(COMMON_STATUS_CONSTANTS.CREATED)
        .orElseThrow(() -> new IllegalArgumentException("Status not found for id: "));
    mergeSubSlip.setStatus(status);
    List<Object[]> obj = offerLetterRepository.getDepartSecCodeBySubSlipId(reqDto.getOlIds().get(0));
	String secCode = null;
	if(obj!=null) {
		for(Object[] det: obj) {
		secCode = det[2]!=null? det[2].toString(): null;
		}
	}
	
	mergeSubSlip.setDepartment(departmentRepository.findBySectionCode(Integer.parseInt(secCode)));
    MergeSubSlip savedMergeSubSlip = mergeSubSlipRepository.save(mergeSubSlip);

    List<MergeSubSlipMapping> mergeSubSlipMappinglist = new ArrayList<>();
    for (Integer subSlipId : subSlipIds) {

      SubmissionSlip subSlip = submissionSlipRepository.findById(subSlipId)
          .orElseThrow(() -> new IllegalArgumentException("SubmissionSlip not found for the given Id: " + subSlipId));
      subSlip.setIsMerged(true);

      MergeSubSlipMapping mergeOlMapping = new MergeSubSlipMapping();
      mergeOlMapping.setMergeOlNoId(savedMergeSubSlip);
      mergeOlMapping.setSubSlip(subSlip);
      mergeOlMapping.setIsMerged(true);
      mergeSubSlipMappinglist.add(mergeOlMapping);
    }
    mergeSubSlipMappingRepository.saveAll(mergeSubSlipMappinglist);

    // update serial no status logic.
    List<Integer> passedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(subSlipIds, RegistrationConstants.PASS);
    List<Integer> failedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(subSlipIds, RegistrationConstants.FAIL);

    if (!passedSerialNos.isEmpty()) {
      LineItemSerialNoDto dto = new LineItemSerialNoDto();
      dto.setIds(passedSerialNos);
      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.LOT_GENERATED);
      lineItemSerialNoService.update(dto);

      LineItemSerialNoHistoryDto passedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(passedSerialNos, SERIAL_NO_STATUS_CONSTANTS.LOT_GENERATED, reqDto.getCreatedBy(), savedMergeSubSlip.getId(), reqDto.getMergeOlNo());
      lineItemSerialNoService.addHistory(passedRegsHistoryDto);
    }

    if (!failedSerialNos.isEmpty()) {
      LineItemSerialNoDto dto = new LineItemSerialNoDto();
      dto.setIds(failedSerialNos);
      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.RFR);
      lineItemSerialNoService.update(dto);

      LineItemSerialNoHistoryDto failedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(failedSerialNos, SERIAL_NO_STATUS_CONSTANTS.RFR, reqDto.getCreatedBy(), savedMergeSubSlip.getId(), reqDto.getMergeOlNo());
      lineItemSerialNoService.addHistory(failedRegsHistoryDto);
    }
    return "Data submitted successfully";

	}

	
	// Still code to be changed
  public List<MergedSubSlipDto> getAllMergedOls(int empId, int roleId, List<Integer> statusIds) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    if (statusIds == null || statusIds.isEmpty()) { //default statuses
      statusIds = List.of(COMMON_STATUS_CONSTANTS.CREATED, COMMON_STATUS_CONSTANTS.QCC_GENERATED, COMMON_STATUS_CONSTANTS.DOA_APPROVED, COMMON_STATUS_CONSTANTS.DOQC_APPROVED, COMMON_STATUS_CONSTANTS.OL_GENERATED);
    }

		if( roleId == MasterConstants.EMP_ROLE.ID_DO) {
      data = mergeSubSlipRepository.getAllMergedOlsDOA(statusIds, empId);
    } else
      data = mergeSubSlipRepository.getAllMergedOls(empId, statusIds);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<MergedSubSlipDto> mergedOlDetDtos = new ArrayList<>();
		for (Object[] row : data) {
			try {
				MergedSubSlipDto mergedOlDetDto = new MergedSubSlipDto();
				int index=0;
				
				mergedOlDetDto.setMergedOlNo((String) row[index]);
				index++;
				mergedOlDetDto.setMergedDate(sdf.format((Date) row[index]));
				index++;
				mergedOlDetDto.setSoNumber((String) row[index]);
				index++;
				mergedOlDetDto.setSoDate((String) row[index]);
				index++;
				mergedOlDetDto.setGrp((String) row[index]);
				index++;
				mergedOlDetDto.setMergedOlId((Integer)row[index]);
				index++;
				mergedOlDetDto.setOrganizationTypeId((Integer)  row[index]);
				index++;
				mergedOlDetDto.setInspectionAuthority((String) row[index]);			
				index++;
				mergedOlDetDto.setEpcode((String)row[index]);
        index++;
        mergedOlDetDto.setStatusId((Integer) row[index]);
        index++;
        mergedOlDetDto.setStatus(row[index] != null ? (String) row[index] : "");
				mergedOlDetDtos.add(mergedOlDetDto);
			} catch (ClassCastException e) {
        // Handle ClassCastExceptions - happens if row[0], row[1], etc. are not strings or dates
				logger.error("Error casting row data: {}", e.getMessage(), e);
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting all mergedOls: {}", e.getMessage());
			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting all mergedOls: {}", e.getMessage());
			}
		}

		return mergedOlDetDtos;
	}

	public List<OlDetailDto> getAllOlDetails(List<Integer> olIds) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		data = mergeSubSlipRepository.getAllOlDetails(olIds);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, OlDetailDto> resultMapp = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				String supplyOrderNo = (String) row[0];
				String supplyOrderDate = sdf.format((Date) row[1]);
				String olNo = (String) row[2];
				String olDate = sdf.format((Date) row[3]);
			
				String regNo = (String) row[4];

				if (olNo != null) {
					OlDetailDto dto = resultMapp.computeIfAbsent(olNo,
							key -> new OlDetailDto(supplyOrderNo, supplyOrderDate, olDate, olNo,   new ArrayList<>()));
					if (regNo != null)
						dto.getRegNos().add(regNo);
				} else
					logger.info("olNo. is null, skipping record.");
			} catch (ClassCastException e) {
				logger.error("Error casting row data: {}" , e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting all olDetails: {}", e.getMessage());
			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting all olDetails: {}", e.getMessage());
			}
		}
		return new ArrayList<>(resultMapp.values());
	}

	public List<MergedOlDetDto> getMergedOlDetById(Integer mergedOlId) {
		List<Object[]> data;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		data = mergeSubSlipRepository.getMergedOlDetById(mergedOlId);

		if (data == null || data.isEmpty()) {
			return Collections.emptyList();
		}
		Map<String, MergedOlDetDto> resultMap = new LinkedHashMap<>();

		for (Object[] row : data) {
			try {
				 String mergedOlNo = row[0] != null ? (String) row[0] : null;

				    SubmissionSlip ol = row[1] != null ? submissionSlipRepository.findById((Integer) row[1]).orElse(null) : null;				    
				    String olNo = (ol != null) ? ol.getOlNumber() : null;
				    Integer olId = (ol != null) ? ol.getId() : null;				    
				    String submissionSlipNo = row[2] != null ? (String) row[2] : null;
				    String olDate = row[3] != null ? sdf.format((Date) row[3]) : null;
				    String mergedDate = row[4] != null ? sdf.format((Date) row[4]) : null;			    
				    String soNumber = row[5] != null ? (String) row[5] : null;
				    String soDate = row[6] != null ? (String) row[6] : null;
				    String grp = row[7] != null ? (String) row[7] : null;				    
				    Integer mergedOLId = row[8] != null ? (Integer) row[8] : null;
				    String regNo = row[9] != null ? (String) row[9] : null;
				    String Epcode = row[10] != null ? (String) row[10] : null;
				    String nomenclature = row[11] != null ? (String) row[11] : null;
				    Integer registrationNo = row[12] != null ? (Integer) row[12] : null;
				if (olNo != null) {
					MergedOlDetDto dto = resultMap.computeIfAbsent(olNo,
							key -> new MergedOlDetDto(mergedOlNo, olNo, olId, submissionSlipNo,
									olDate, mergedDate, soNumber, soDate, grp, mergedOLId, new ArrayList<>(),Epcode, nomenclature,registrationNo));

					if (regNo != null)
						dto.getRegNos().add(regNo);

				} else
					logger.info("iNoteno is null, skipping record.");
			} catch (ClassCastException e) {
				logger.error("Error casting row data: {}", e.getMessage());

			} catch (ArrayIndexOutOfBoundsException e) {
				logger.error("Error accessing row elements in getting MergedOlDetById: {}", e.getMessage());

			} catch (Exception e) {
				logger.error("An unexpected error occurred in getting MergedOlDetById: {}", e.getMessage());
			}
		}
		return new ArrayList<>(resultMap.values());
	}


	public List<Map<String, Object>> getRegsByMergeSslipId(Integer mergeSslipId) {

		if(!mergeSubSlipRepository.existsById(mergeSslipId)) 
			throw new IllegalArgumentException("Merge SubSlip details not found for id: " + mergeSslipId);
		
		List<Object[]> regsData = mergeSubSlipRepository.getRegsByMergeSslipId(mergeSslipId);
		
		List<Map<String, Object>> returnMap = new ArrayList<>();
		
		regsData.forEach(reg -> {
			Map<String, Object> map = new HashMap<>();
			map.put("sslipSerialNoId", Integer.parseInt(reg[0].toString()));
			map.put("serialNo", String.valueOf(reg[1]));
			map.put("sslipLiId", Integer.parseInt(reg[2].toString()));
			map.put("roundId", Integer.parseInt(reg[3].toString()));
			map.put("productId", Integer.parseInt(reg[4].toString()));
			map.put("registrationNo", Integer.parseInt(reg[5].toString()));
			returnMap.add(map);
		});
		
		return returnMap;
		
	}

  public void updateMergeSubSLipStatus(Integer id, Integer statusId) {
    if (id != null && statusId != null) {
      MergeSubSlip mergeSubSlip = mergeSubSlipRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Lot details not found for id: " + id));

      Status status = statusRepositoy.findById(statusId)
          .orElseThrow(() -> new IllegalArgumentException("Status not found for id: " + statusId));
      mergeSubSlip.setStatus(status);

      mergeSubSlipRepository.save(mergeSubSlip);
    }
  }
  
  public void addToRfr(SubmissionSlip sSlip) {
	  List<Integer> failedSerialNos = subSlipSerialNoRepository.findLineItemSerialNosByStatus(Arrays.asList(sSlip.getId()), RegistrationConstants.FAIL);
	  
	  if (!failedSerialNos.isEmpty()) {
	      LineItemSerialNoDto dto = new LineItemSerialNoDto();
	      dto.setIds(failedSerialNos);
	      dto.setStatusId(SERIAL_NO_STATUS_CONSTANTS.RFR);
	      lineItemSerialNoService.update(dto);
	
	      LineItemSerialNoHistoryDto failedRegsHistoryDto = lineItemSerialNoService.buildHistoryDto(failedSerialNos, SERIAL_NO_STATUS_CONSTANTS.RFR, sSlip.getCreatedBy(), sSlip.getId(), sSlip.getOlNumber());
	      lineItemSerialNoService.addHistory(failedRegsHistoryDto);
	    }
  }


public LotAllotment allotLot(@Valid LotAllotmentDto lotAllotmentDto) {


	MergeSubSlip lot = mergeSubSlipRepository.findById(lotAllotmentDto.getMergeSubSlipId())
			.orElseThrow(() -> new IllegalArgumentException("Lot not found for id: " + lotAllotmentDto.getMergeSubSlipId()));
	
	List<NotificationDto> notificationDtoList = new ArrayList<>();
	List<Integer> receivers = new ArrayList<>();
	List<String> allotedToNameList = new ArrayList<>();		

	Employee allotedBy = employeeDao.getEmployee(lotAllotmentDto.getAllotedById());
	

		Employee allotedTo = employeeDao.getEmployee(lotAllotmentDto.getAllotedToId());
		String sbuName = null;

		LotAllotment allotment = new LotAllotment();
		allotment.setAllotedBy(allotedBy);
		allotment.setAllotedTo(allotedTo);
		allotment.setMergeSubSlip(lot);
		allotedToNameList.add(allotedTo.getEname());
		
		// notify all alloted to and by employees of allotment
		try {
			List<Object[]> response = employeeRepository
					.getAllParentIncCurrentEmpOfEmpByRoleId(lotAllotmentDto.getAllotedToId(), -1);
			for (Object[] item : response) {
				receivers.add(Integer.parseInt(item[0].toString()));
			}

			NotificationDto notificationDto = new NotificationDto();
			notificationDto.setSenderId(lotAllotmentDto.getAllotedById());
			notificationDto.setReceiverId(lotAllotmentDto.getAllotedToId());
			notificationDto.setNotificationCategory(NotificationConstants.LOT_ALLOTMENT_TYPE);
			notificationDto.setCategoryId(lotAllotmentDto.getMergeSubSlipId());
			notificationDto.setSbuName(sbuName);
			notificationDtoList.add(notificationDto);

		} catch (Exception e) {
			logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
			logger.error("Error occured in Allot subordinate for SO  ", e);
		}
	
	LotAllotment savedAllotment = lotAllotmentRepository.save(allotment);
	
	// notify all alloted to and by employees of allotment
	try {
		String allotedTName = allotedToNameList.stream().collect(Collectors.joining(", "));
		String msg = "Lot " + lot.getMergeOlNo() + " alloted to "
				+ allotedTName + " by " + allotedBy.getEname();
		
		for(NotificationDto dto : notificationDtoList) {
			dto.setNotificationName(msg);
		}
		notificationService.saveNotifications(notificationDtoList);
		MessageBean message = new MessageBean();
		message.setSender(1);
		message.setMessageType(NotificationConstants.LOT_ALLOTMENT_TYPE);
		message.setMessage(msg);
		message.setItemNo(lot.getMergeOlNo());
		message.setReceivers(receivers);
		wsService.notifyCqae(message);
	} catch (Exception e) {
		logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
		logger.error("Error occured whin Allot subordinate for SO ", e);
	}
	return savedAllotment;
}


	public LotAllotmentEmpDto getAllActiveDoBydepTypeIdLotId(int deptTypeId, int mergeSubSlipId, int empId) {
		LotAllotmentEmpDto dto = new LotAllotmentEmpDto();
		List<LotAllotment> lotAllotmentHistory = lotAllotmentRepository.findByMergeSubSlipIdOrderByCreatedAtDesc(mergeSubSlipId);
		List<LotAllotmentDto> lotAllotmentDtos = lotAllotmentHistory.stream().map(allotment-> {
			
			LotAllotmentDto allotmentDto = mapper.map(allotment, LotAllotmentDto.class);
			
			if (allotment.getAllotedBy() != null) {
		            String ename = allotment.getAllotedBy().getEname();
		            String staffNo = (allotment.getAllotedBy().getStaffNo() != null) ? allotment.getAllotedBy().getStaffNo().toString().trim() : "";
		            
		            if (!staffNo.isEmpty()) {
		                allotmentDto.setAllotedByNameAndStaffNo(ename + " - " + staffNo);
		            } else {
		                allotmentDto.setAllotedByNameAndStaffNo(ename);
		            }
		        }
			if (allotment.getAllotedTo() != null) {
				String ename = allotment.getAllotedTo().getEname();
	            String staffNo = (allotment.getAllotedTo().getStaffNo() != null) ? allotment.getAllotedTo().getStaffNo().toString().trim() : "";
		            
	            if (!staffNo.isEmpty()) {
	                allotmentDto.setAllotedToNameAndStaffNo(ename + " - " + staffNo);
	            } else {
	                allotmentDto.setAllotedToNameAndStaffNo(ename);
	            }
            }
			return allotmentDto;
			
		}).toList();
		
		dto.setLotAllotmentDtos(lotAllotmentDtos);
		List<Employee> empList = employeeRepository.getAllActiveDoBydepTypeId(deptTypeId, MasterConstants.EMP_ROLE.ID_DO );
		Set<Integer> existingIds = new HashSet<>(lotAllotmentRepository.findExisitngIdByMergeSubSlipId(mergeSubSlipId));
		List<Employee> filteredEmp = empList.stream().filter(emp-> !existingIds.contains(emp.getId()) )
				.toList();
		List<EmpRoleDto> userDto = new ArrayList<>();
		for (Employee emp : filteredEmp) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(emp.getId());
			dto2.setName(emp.getEname());
			
			if (emp.getStaffNo() != null && !emp.getStaffNo().toString().trim().isEmpty()) {
				dto2.setNameAndStaffNo(dto2.getName() + " - " + emp.getStaffNo().toString());
            } else {
            	dto2.setNameAndStaffNo(dto2.getName());
            }
			
			userDto.add(dto2);
		}
		dto.setEmpDto(userDto);		
		return dto;
	}
	
}
