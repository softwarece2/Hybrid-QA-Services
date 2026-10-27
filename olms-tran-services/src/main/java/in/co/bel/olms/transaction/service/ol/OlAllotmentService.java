package in.co.bel.olms.transaction.service.ol;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants.Role;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.OlAllotmentDto;
import in.co.bel.olms.transaction.dto.OlAllotmentInspDto;
import in.co.bel.olms.transaction.dto.OlAllotmentWithEmpListDto;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlAllotment;
import in.co.bel.olms.transaction.entity.OlCrossCheckAllotment;
import in.co.bel.olms.transaction.entity.OlSurpriseCheckAllotment;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.OlAllotmentRepository;
import in.co.bel.olms.transaction.repository.OlCrossCheckAllotmentRepository;
import in.co.bel.olms.transaction.repository.OlSurpriseCheckAllotmentRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OlAllotmentService {

	private final EmployeeDao employeeDao;

	private final OfferLetterService offerLetterService;

	private final OlAllotmentRepository olAllotmentRepository;

	private final OlCrossCheckAllotmentRepository olCrossCheckAllotmentRepository;

	private final OlSurpriseCheckAllotmentRepository olSurpriseCheckAllotmentRepository;

	private final  EmployeeRepository employeeRepository;

	private final NotificationService notificationService;

	private final  WSService wsService;

	private final OfferLetterRepository offerLetterRepository;

	private final Mapper mapper;

	private Gson gson = new Gson();

	private static final Logger logger = LoggerFactory.getLogger(OlAllotmentService.class);
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

	/**
	 * @param olAllotmentDto
	 * @return
	 * 
	 *         Allot OL to subordinate
	 * 
	 * 
	 */
	public List<OlAllotment> allotSubordinate(@Valid OlAllotmentInspDto olAllotmentDto) {

		OfferLetter offerletter = offerLetterService.getOfferLetterById(olAllotmentDto.getOfferLetterId());
		offerletter.setIsNotified(true);
		offerLetterRepository.save(offerletter);
		
		
		List<OlAllotment> allotmentList = new ArrayList<>();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		List<Integer> receivers = new ArrayList<>();
		List<String> allotedToNameList = new ArrayList<>();		

		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		
		for (Integer allotedToId : olAllotmentDto.getAllotedTo()) {
			OlAllotment returnOlAllotment;
			Employee allotedTo = employeeDao.getEmployee(allotedToId);
			String sbuName = null;

			OlAllotment allotment = new OlAllotment();
			allotment.setAllotedBy(allotedBy);
			allotment.setAllotedTo(allotedTo);
			allotment.setOfferletter(offerletter);
			allotment.setCreatedDate(new Date());
			allotment.setIsDeligation(false);
			// Manasa sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
			allotmentList.add(allotment);
			allotedToNameList.add(allotedTo.getEname());
			
			
//			if (olAllotmentDto.getIsDeligation())
//				olAllotmentRepository.updateDeligationStatus(allotedBy.getId(), olAllotmentDto.getOfferLetterId(),
//						returnOlAllotment.getCreatedDate());

			// Updating the OL Status on successful allotment of OL to inspector
			if (allotedTo.getEmpRole().getId() == ApplicationConstants.INSPECTOR) {
				logger.info("Updating the OL Status");
				offerLetterService.updateOlStatus(olAllotmentDto.getOfferLetterId(), ApplicationConstants.OL_PENDING);
			}

			// notify all alloted to and by employees of allotment
			try {
				List<Object[]> response = employeeRepository
						.getAllParentIncCurrentEmpOfEmpByRoleId(allotedToId, -1);
				for (Object[] item : response) {
					receivers.add(Integer.parseInt(item[0].toString()));
				}

				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setSenderId(olAllotmentDto.getAllotedBy());
				notificationDto.setReceiverId(allotedToId);
				notificationDto.setNotificationCategory(NotificationConstants.OL_ALLOTMENT);
				notificationDto.setCategoryId(olAllotmentDto.getOfferLetterId());
				notificationDto.setSbuName(sbuName);
//				notificationService.saveNotification(notificationDto);
				notificationDtoList.add(notificationDto);

			} catch (Exception e) {
				logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
				logger.error("Error in Allot subordinate for SO ", e);
			}
		}
		List<OlAllotment> savedAllotments = olAllotmentRepository.saveAll(allotmentList);
		
		// notify all alloted to and by employees of allotment
		try {
			String allotedTName = allotedToNameList.stream().collect(Collectors.joining(", "));
			String msg = "Offer Letter " + offerletter.getOlNumber() + " alloted to "
					+ allotedTName + " by " + allotedBy.getEname();
			
			for(NotificationDto dto : notificationDtoList) {
				dto.setNotificationName(msg);
			}
			
			notificationService.saveNotifications(notificationDtoList);
			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.OL);
			message.setMessage(msg);
			message.setItemNo(offerletter.getOlNumber());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);
		} catch (Exception e) {
			logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
			logger.error("Error in Allot subordinate for SO ", e);
		}

		return savedAllotments;

	}

	public @Valid OlAllotmentDto convertBotoDto(OlAllotment olAllotment) {
		OlAllotmentDto olAllotmentDto = new OlAllotmentDto();
		olAllotmentDto.setId(olAllotment.getId());

		// To be completed

		return olAllotmentDto;
	}

	/**
	 * 
	 * @param empId
	 * @return
	 */
	public List<OlAllotmentDto> getOlAllotmentForEmpId(int orgId,int empId) throws DataNotValidException {

		List<Object[]> response = olAllotmentRepository.getAllotmentDetailsByEmpId( orgId,empId);

		List<OlAllotmentDto> olAllotmentDtos = new ArrayList<>();

		// [0]: offer_letter_id; [1]: ol_no; [2]: offer_date; [3]: alloted_to; [4]:
		// alloted_to_id; [5]: alloted_by; [6]: project_name; [7]: order_no; [8]:
		// sales_grp
		for (Object[] item : response) {
			try {
				OlAllotmentDto dto = new OlAllotmentDto();
				dto.setOfferLetterId(Integer.parseInt(item[0].toString()));

				if (null != item[1])
					dto.setOfferLetterName(item[1].toString());
				else
					dto.setOfferLetterName("");

				if (null != item[2])
					dto.setOfferLetterDate(item[2].toString());
				else
					dto.setOfferLetterDate("");

				if (null != item[3])
					dto.setAllotedToName(item[3].toString());
				else
					dto.setAllotedToName("");

				if (null != item[5])
					dto.setAllotedByName(item[5].toString());
				else
					dto.setAllotedByName("");

				if (null != item[6])
					dto.setProjectName(item[6].toString());
				else
					dto.setProjectName("");

				if (null != item[7])
					dto.setSupplyOrderNo(item[7].toString());
				else
					dto.setSupplyOrderNo("");

				if (null != item[8])
					dto.setSalesGroup(item[8].toString());
				else
					dto.setSalesGroup("");
				dto.setIsAllotedToEmp((boolean) item[12]);

				if (null != item[10])
					dto.setEpcode(item[10].toString());
				else
					dto.setEpcode("");
				if (null != item[11])
					dto.setMaterialDescription(item[11].toString());
				else
					dto.setMaterialDescription("");
				
				if (item[4].toString() != null && item[4].toString().split(",").length == 1
						&& Integer.parseInt(item[4].toString().split(",")[0]) == empId) {
					dto.setIsAlloteBtnVisible(true);
				} else {
					dto.setIsAlloteBtnVisible(false);
				}
				
				if(null != item[9]) {
				    String date = item[9].toString();
				    dto.setDate(date);
				} else {
					dto.setDate(" ");
				}
				
				if(null != item[13]) {
					dto.setItemCategory(item[13].toString()); 
				} else {
					dto.setItemCategory(" ");
				}

				String allotedToIdsStr = (item[4] != null) ? item[4].toString() : "";

				List<Integer> allotedToIds = new ArrayList<>();
				if (!allotedToIdsStr.isEmpty()) {
				    allotedToIds = Arrays.stream(allotedToIdsStr.split(","))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty())
                         .map(Integer::parseInt)
                         .toList();
				}
				
				List<Object[]> availableEmps = employeeRepository.getAllAvailableActiveEmpsByOrgId(empId, orgId, allotedToIds);

				List<EmpRoleDto> userDtos = availableEmps.stream().map(emp -> {
	                EmpRoleDto empDto = new EmpRoleDto();
	                empDto.setId(Integer.parseInt(emp[0].toString()));
	                empDto.setName(String.valueOf(emp[1].toString()));
	                if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
	                	empDto.setNameAndStaffNo(empDto.getName() +" - "+ emp[2]);
	                } else {
	                	empDto.setNameAndStaffNo(empDto.getName());
	                }
	                return empDto;
	            }).toList();
				
				dto.setEmpLists(userDtos);
				olAllotmentDtos.add(dto);
			} catch (Exception e) {
				logger.error("Error in getting OlAllotment For EmpId {}", e.getMessage());
			}
		}

		Collections.sort(olAllotmentDtos, new Comparator<OlAllotmentDto>() {

			@Override
			public int compare(OlAllotmentDto o1, OlAllotmentDto o2) {

				return o2.getIsAlloteBtnVisible().compareTo(o1.getIsAlloteBtnVisible());
			}

		});
		return olAllotmentDtos;
	}

	/**
	 * 
	 * @param empId
	 * @return
	 */
	public List<OlAllotmentDto> getOlAllotmentToEmpId(int empId) throws DataNotValidException {

		List<Object[]> response = olAllotmentRepository.getAllotmentDetailsForEmpId(empId);

		List<OlAllotmentDto> olAllotmentDtos = new ArrayList<>();

		// [0]: offer_letter_id; [1]: ol_no; [2]: offer_date; [3]: alloted_to; [4]:
		// alloted_to_id; [5]: alloted_by; [6]: project_name; [7]: order_no; [8]:
		// sales_grp
		for (Object[] item : response) {
			try {
				OlAllotmentDto dto = new OlAllotmentDto();
				dto.setOfferLetterId(Integer.parseInt(item[0].toString()));

				if (null != item[1])
					dto.setOfferLetterName(item[1].toString());
				else
					dto.setOfferLetterName("");

				if (null != item[2])
					dto.setOfferLetterDate(item[2].toString());
				else
					dto.setOfferLetterDate("");

				if (null != item[3])
					dto.setAllotedToName(item[3].toString());
				else
					dto.setAllotedToName("");

				if (null != item[5])
					dto.setAllotedByName(item[5].toString());
				else
					dto.setAllotedByName("");

				if (null != item[6])
					dto.setProjectName(item[6].toString());
				else
					dto.setProjectName("");

				if (null != item[7])
					dto.setSupplyOrderNo(item[7].toString());
				else
					dto.setSupplyOrderNo("");

				if (null != item[8])
					dto.setSalesGroup(item[8].toString());
				else
					dto.setSalesGroup("");
				dto.setIsAllotedToEmp((boolean) item[10]);

				if (item[4].toString() != null && item[4].toString().split(",").length == 1
						&& Integer.parseInt(item[4].toString().split(",")[0]) == empId) {
					dto.setIsAlloteBtnVisible(true);
				} else {
					dto.setIsAlloteBtnVisible(false);
				}
				if(null != item[9]) {
//					String dt = dateFormat.format((Date)item[9]);
//					String dt = dateUtils 
//				    System.out.println("datetime " + dt);
//				    String date = dt.substring(0, dt.indexOf(' '));
				    String date = item[9].toString();
//				    String time = dt.substring(dt.indexOf(' ')+1);
				    dto.setDate(date);
//				    dto.setTime(time);
				    		
				}
				else {
					dto.setDate(" ");
//				    dto.setTime(" ");
				}
				olAllotmentDtos.add(dto);
			} catch (Exception e) {
				logger.error("Error in getting OlAllotment to EmpId {}", e.getMessage());
			}
		}

		Collections.sort(olAllotmentDtos, new Comparator<OlAllotmentDto>() {

			@Override
			public int compare(OlAllotmentDto o1, OlAllotmentDto o2) {

				return o2.getIsAlloteBtnVisible().compareTo(o1.getIsAlloteBtnVisible());
			}

		});
		return olAllotmentDtos;
	}

	public List<OfferLetterDto> getOLAllotedToEmployee(int empId) {
		DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(2);
//		List<OlAllotment> allotments = olAllotmentRepository.getAllotedOlToEmployee(empId);
		List<Object[]> allotments1 = olAllotmentRepository.getAllotedOlToEmployee1(empId);
		List<OfferLetterDto> olDtos = new LinkedList<>();
		for (Object[] allot : allotments1) {
			OfferLetterDto olDto = new OfferLetterDto();
			olDto.setId(allot[0] != null ? (int) allot[0] : null);
			olDto.setName(allot[1] != null ? allot[1].toString() : null);
			olDto.setOfferDate(allot[2] != null ? allot[2].toString() : null);
			olDto.setSoNumber(allot[3] != null ? allot[3].toString() : null);
			olDto.setSalesGroupName(allot[4] != null ? allot[4].toString() : null);
			olDto.setProjectName(allot[5] != null ? allot[5].toString() : null);
			Integer returnedVal = (Integer) (allot[6] != null ? allot[6] : null);

			if (returnedVal != null && returnedVal == ApplicationConstants.OL_REJECTED) {  
			    olDto.setIsReturned(true);
			}
			else
				olDto.setIsReturned(false);
			if (allot[7] != null) {
				olDto.setIsScrunityDone(true);
			}
			// dto.setTotalValue(df.format(supplyOrder.getValue()));
			BigDecimal totalItems = allot[8] != null ? new BigDecimal(allot[8].toString()) : null;
			if (totalItems != null) {
				olDto.setTotalItems(totalItems);
			}
			olDto.setTotalValue(allot[10] != null ? new BigDecimal(allot[10].toString()) : null);
			olDto.setSoDate(allot[11] != null ? allot[11].toString() : null);
			olDto.setRemarks(allot[12] != null ? allot[12].toString() : null);
			olDto.setStoresDescription(allot[13] != null ? allot[13].toString() : null);
			olDto.setSoId(allot[14] != null ? (int) allot[14] : null);

			if (allot[15] != null) {
				olDto.setConsigneeId(((Number) allot[15]).intValue());
			} else {
				olDto.setConsigneeId(null);
			}
			olDto.setFileNo(allot[16] != null ? allot[16].toString() : "NA");
			olDto.setSoType(allot[17] != null ? allot[17].toString() : null);
			olDto.setIsSurpriseCheckAllotted(allot[18] != null ? (boolean) allot[18] : null);
			olDto.setIsCrossCheckAllotted(allot[19] != null ? (boolean) allot[19] : null);
			olDto.setInspAuthority(allot[20] != null ? allot[20].toString() : null);
			olDto.setConsignee(allot[21] != null ? allot[21].toString() : null);
			olDtos.add(olDto);

		}
//	    Collections.reverse(olDtos);
		return olDtos;

	}

	public List<Map<Integer, String>> getEmployeesByOlId(int olId) {
		List<Object[]> response = olAllotmentRepository.getEmployeesByOlId(olId);
		Set<Integer> empSet = new HashSet<>();
		for (Object[] objects : response) {
			empSet.add(Integer.parseInt(String.valueOf(objects[0])));
			empSet.add(Integer.parseInt(String.valueOf(objects[1])));
		}

		List<Map<Integer, String>> maps = new ArrayList<>();
		List<Object[]> empIdName = employeeRepository.getEmpIDNameByEmpIds(empSet);
		for (Object[] objects : empIdName) {
			Map<Integer, String> map = new HashMap<>();
			map.put(Integer.parseInt(String.valueOf(objects[0])), String.valueOf(objects[1]));
			maps.add(map);
		}

		return maps;
	}

	public OlCrossCheckAllotment allotForCrossCheck(@Valid OlAllotmentDto olAllotmentDto) {

		OlCrossCheckAllotment returnOlAllotment;
		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		Employee allotedTo = employeeDao.getEmployee(olAllotmentDto.getAllotedTo());
		String sbuName = null;

		OlCrossCheckAllotment allotment = new OlCrossCheckAllotment();
		allotment.setAllotedBy(allotedBy);
		allotment.setAllotedTo(allotedTo);
		OfferLetter offerletter = offerLetterService.getOfferLetterById(olAllotmentDto.getOfferLetterId());
		allotment.setOfferletter(offerletter);
		allotment.setCreatedDate(new Date());
		allotment.setIsDeligation(false);
		sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
		returnOlAllotment = olCrossCheckAllotmentRepository.save(allotment);
		if (olAllotmentDto.getIsDeligation())
			olCrossCheckAllotmentRepository.updateDeligationStatus(allotedBy.getId(), olAllotmentDto.getOfferLetterId(),
					returnOlAllotment.getCreatedDate());

		// notify all alloted to and by employees of allotment
		try {
			List<Integer> receivers = new ArrayList<>();
			List<Object[]> response = employeeRepository
					.getAllParentIncCurrentEmpOfEmpByRoleId(olAllotmentDto.getAllotedTo(), -1);
			for (Object[] item : response) {
				receivers.add(Integer.parseInt(item[0].toString()));
			}

			String msg = "QACL " + olAllotmentDto.getOfferLetterName() + " alloted to " + allotedTo.getEname() + " by "
					+ allotedBy.getEname() + " for Tier II";

			NotificationDto notificationDto = new NotificationDto();
			notificationDto.setSenderId(olAllotmentDto.getAllotedBy());
			notificationDto.setReceiverId(olAllotmentDto.getAllotedTo());
			notificationDto.setNotificationName(msg);
			notificationDto.setNotificationCategory(NotificationConstants.OL_ALLOTMENT);
			notificationDto.setCategoryId(olAllotmentDto.getOfferLetterId());
			notificationDto.setSbuName(sbuName);
			notificationService.saveNotification(notificationDto);

			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.OL);
			message.setMessage(msg);
			message.setItemNo(olAllotmentDto.getOfferLetterName());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);

		} catch (Exception e) {
			logger.error("Notification error in allot subordinate for Cross Check {} ", e.getMessage());
		}
		return returnOlAllotment;
	}

	public OlSurpriseCheckAllotment allotForSurpriseCheck(@Valid OlAllotmentDto olAllotmentDto) {

		OlSurpriseCheckAllotment returnOlAllotment;
		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		Employee allotedTo = employeeDao.getEmployee(olAllotmentDto.getAllotedTo());
		String sbuName = null;

		OlSurpriseCheckAllotment allotment = new OlSurpriseCheckAllotment();
		allotment.setAllotedBy(allotedBy);
		allotment.setAllotedTo(allotedTo);
		OfferLetter offerletter = offerLetterService.getOfferLetterById(olAllotmentDto.getOfferLetterId());
		allotment.setOfferletter(offerletter);
		allotment.setCreatedDate(new Date());
		allotment.setIsDeligation(false);
		sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
		returnOlAllotment = olSurpriseCheckAllotmentRepository.save(allotment);
		if (olAllotmentDto.getIsDeligation())
			olSurpriseCheckAllotmentRepository.updateDeligationStatus(allotedBy.getId(),
					olAllotmentDto.getOfferLetterId(), returnOlAllotment.getCreatedDate());

		// notify all alloted to and by employees of allotment
		try {
			List<Integer> receivers = new ArrayList<>();
			List<Object[]> response = employeeRepository
					.getAllParentIncCurrentEmpOfEmpByRoleId(olAllotmentDto.getAllotedTo(), -1);
			for (Object[] item : response) {
				receivers.add(Integer.parseInt(item[0].toString()));
			}

			String msg = "QACL " + olAllotmentDto.getOfferLetterName() + " alloted to " + allotedTo.getEname() + " by "
					+ allotedBy.getEname() + " for Tier III";

			NotificationDto notificationDto = new NotificationDto();
			notificationDto.setSenderId(olAllotmentDto.getAllotedBy());
			notificationDto.setReceiverId(olAllotmentDto.getAllotedTo());
			notificationDto.setNotificationName(msg);
			notificationDto.setNotificationCategory(NotificationConstants.OL_ALLOTMENT);
			notificationDto.setCategoryId(olAllotmentDto.getOfferLetterId());
			notificationDto.setSbuName(sbuName);
			notificationService.saveNotification(notificationDto);

			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.OL);
			message.setMessage(msg);
			message.setItemNo(olAllotmentDto.getOfferLetterName());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);

		} catch (Exception e) {
			logger.error("Notification error in allot subordinate for Surprise Check {} ", e.getMessage());
		}
		return returnOlAllotment;
	}
	
public String getOlAllotmentsWithNextEmps(Integer offerLetterId, Integer deptTypeId, Integer empId, Integer roleId){
		
		OlAllotmentWithEmpListDto dto = new OlAllotmentWithEmpListDto();
		
		List<OlAllotment> allotments = olAllotmentRepository.getByOlIdOrderByCreatedAtDesc(offerLetterId);
		List<Integer> allotedToIds = allotments.stream().map(allot -> allot.getAllotedTo().getId()).toList();
//		List<OlAllotmentDto> allotmentDtos = allotments.stream().map(allotment -> mapper.map(allotment, OlAllotmentDto.class)).collect(Collectors.toList());
		
		List<OlAllotmentDto> allotmentDtos = allotments.stream().map(allotment -> {
			OlAllotmentDto allotmentDto = mapper.map(allotment, OlAllotmentDto.class); 
			
			//format the createdDate string in 12Hr format
			if(allotment.getCreatedDate() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
				allotmentDto.setCreatedDate(sdf.format(allotment.getCreatedDate()));
			}
			
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
			
		
		dto.setOlAllotmentDtos(allotmentDtos);
		List<EmpRoleDto> userDtos = new ArrayList<>();
		
		if(roleId == Role.DO) {
			List<Object[]> empList = allotedToIds.isEmpty() 
					? employeeRepository.getAllEmpAndReportingOfficersByRoleId(Role.CQAE_ADMIN) 
					: employeeRepository.getAvailableEmpAndReportingOfficersByRoleId(Role.CQAE_ADMIN, allotedToIds);	
			
			userDtos = empList.stream().map(emp -> {
                EmpRoleDto empDto = new EmpRoleDto();
                empDto.setId(Integer.parseInt(emp[0].toString()));
                empDto.setName(String.valueOf(emp[1].toString()));
                
                if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
                	empDto.setNameAndStaffNo(empDto.getName() + " - " + emp[2].toString());
                } else {
                	empDto.setNameAndStaffNo(empDto.getName());
                }
                return empDto;
            }).toList();
		} else {
			List<Employee> employees = allotedToIds.isEmpty()
					? employeeRepository.getSubByEmp(empId)
					: employeeRepository.getAvailableSubordinates(empId, allotedToIds);	
			
//			if (employees.isEmpty()) 
//				throw new DataNotFoundException("Section Officers not found by OrgId: " );
			
			userDtos = employees.stream().map(emp -> {
				EmpRoleDto eDto = new EmpRoleDto();
	            eDto.setId(emp.getId());
	            eDto.setName(emp.getEname());
	            return eDto;
	        }).toList();
		}
		dto.setEmps(userDtos);
		
		return gson.toJson(dto);
	}
}
