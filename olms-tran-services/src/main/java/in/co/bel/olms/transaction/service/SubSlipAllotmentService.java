package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants.Dept;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.SubSlipAllotmentDto;
import in.co.bel.olms.transaction.dto.SubSlipAllotmentWithEmpListDto;
import in.co.bel.olms.transaction.entity.SubSlipAllotment;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.repository.SubSlipAllotmentRepository;
import in.co.bel.olms.transaction.repository.SubmissionSlipRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubSlipAllotmentService {

	private final EmployeeDao employeeDao;

	private final SubSlipService subSlipService;

	private final SubmissionSlipRepository submissionSlipRepository;

	private final EmployeeRepository employeeRepository;

	private final SubSlipAllotmentRepository subSlipAllotmentRepository;

	private final NotificationService notificationService;

	private final WSService wsService;

	private final Mapper mapper;
	
	private Gson gson = new Gson();
	
	private static final Logger logger = LoggerFactory.getLogger(SubSlipAllotmentService.class);
	
	public List<SubSlipAllotment> allotSubordinate(@Valid SubSlipAllotmentDto olAllotmentDto) {

		SubmissionSlip offerletter = subSlipService.getOfferLetterById(olAllotmentDto.getOfferLetterId());
		offerletter.setIsNotified(true);
		submissionSlipRepository.save(offerletter);
		
		
		List<SubSlipAllotment> allotmentList = new ArrayList<>();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		List<Integer> receivers = new ArrayList<>();
		List<String> allotedToNameList = new ArrayList<>();		

		Employee allotedBy = employeeDao.getEmployee(olAllotmentDto.getAllotedBy());
		
		for (Integer allotedToId : olAllotmentDto.getAllotedTo()) {
			
			Employee allotedTo = employeeDao.getEmployee(allotedToId);
			String sbuName = null;

			SubSlipAllotment allotment = new SubSlipAllotment();
			allotment.setAllotedBy(allotedBy);
			allotment.setAllotedTo(allotedTo);
			allotment.setSubmissionSlip(offerletter);
			allotment.setCreatedDate(new Date());
			allotment.setIsDeligation(false);
			// Manasa sbuName = offerletter.getSaleOrder().getSupplyOrder().getSalesGroup().getName();
			allotmentList.add(allotment);
			allotedToNameList.add(allotedTo.getEname());			
			

			// Updating the OL Status on successful allotment of OL to inspector
			if (allotedTo.getEmpRole().getId() == ApplicationConstants.INSPECTOR) {
				logger.info("Updating the OL Status");
				subSlipService.updateOlStatus(olAllotmentDto.getOfferLetterId(), ApplicationConstants.OL_PENDING);
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
				notificationDto.setNotificationCategory(NotificationConstants.OL);
				notificationDto.setCategoryId(olAllotmentDto.getOfferLetterId());
				notificationDto.setSbuName(sbuName);
				notificationDtoList.add(notificationDto);

			} catch (Exception e) {
				logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
				logger.error("Error in Allot subordinate for SO {", e);
			}
		}
		List<SubSlipAllotment> savedAllotments = subSlipAllotmentRepository.saveAll(allotmentList);
		
		// notify all alloted to and by employees of allotment
		try {
			String allotedTName = allotedToNameList.stream().collect(Collectors.joining(", "));
			String msg = "Submission Slip " + offerletter.getOlNumber() + " alloted to "
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
			logger.error("Error in Allot subordinate for SO {", e);
		}

		return savedAllotments;

	}
	public String getSubSlipAllotmentsWithNextEmps(Integer offerLetterId, Integer empId, Integer roleId){
		
		SubSlipAllotmentWithEmpListDto dto = new SubSlipAllotmentWithEmpListDto();

        Employee employee = employeeRepository.findById(empId).orElseThrow(() ->  new IllegalArgumentException("Employee not found: " + empId));
        Integer deptTypeId = employee.getDeptType().getId();

		List<SubSlipAllotment> allotments = subSlipAllotmentRepository.findBySubSlipIdOrderByCreatedDateDesc(offerLetterId);
		List<Integer> allotedToIds = allotments.stream().map(allot -> allot.getAllotedTo().getId()).toList();
		
		List<SubSlipAllotmentDto> allotmentDtos = allotments.stream().map(allotment -> {
		    SubSlipAllotmentDto allotmentDto = mapper.map(allotment, SubSlipAllotmentDto.class);
		    
		    //format the createdDate string in 12Hr format
		    if (allotment.getCreatedDate() != null) {
		        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		        allotmentDto.setCreatedDate(sdf.format(allotment.getCreatedDate()));
		    }
		    
		    if (allotment.getAllotedBy() != null) {
		        Employee allotByEmp = allotment.getAllotedBy();
		        String ename = allotByEmp.getEname();
		        String staffNo = allotByEmp.getStaffNo();
		        
		        if (staffNo != null && !staffNo.trim().isEmpty()) {
		            allotmentDto.setAllotedByNameAndStaffNo(ename + " - " + staffNo);
		        } else {
		            allotmentDto.setAllotedByNameAndStaffNo(ename);
		        }
		    }

		    if (allotment.getAllotedTo() != null) {
		        Employee allotToEmp = allotment.getAllotedTo();
		        String ename = allotToEmp.getEname();
		        String staffNo = allotToEmp.getStaffNo();

		        if (staffNo != null && !staffNo.trim().isEmpty()) {
		            allotmentDto.setAllotedToNameAndStaffNo(ename + " - " + staffNo);
		        } else {
		            allotmentDto.setAllotedToNameAndStaffNo(ename);
		        }
		    }
		    
		    return allotmentDto;
		}).toList();
		
		
		dto.setSubSlipAllotmentDtos(allotmentDtos);

        List<Object[]> employees = new ArrayList<>();
        if(deptTypeId != null && deptTypeId == Dept.ASSEMBLY){
            employees = allotedToIds.isEmpty()
                ? employeeRepository.getAllHoS(Dept.QC)
                : employeeRepository.findAvailableHoSByDeptType(Dept.QC, allotedToIds);
        } else {
            employees = allotedToIds.isEmpty()
                ? employeeRepository.findAllSubordinatesRecursive(empId)
                : employeeRepository.findAvailableSubordinatesRecursive(empId, allotedToIds);
        }

        List<EmpRoleDto> userDtos = employees.stream().map(emp -> {
            EmpRoleDto empDto = new EmpRoleDto();
            empDto.setId(Integer.parseInt(emp[0].toString()));
            empDto.setName(String.valueOf(emp[1].toString()));
            
            if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
                empDto.setNameAndStaffNo(empDto.getName() + " - " + emp[2].toString());
            } else {
                empDto.setNameAndStaffNo(empDto.getName());
            }
            if (emp[3] != null) {              
                empDto.setActive(emp[3].toString()); 
            } else {
                empDto.setActive("false");
            }
            
            return empDto;
        }).toList();

		dto.setEmps(userDtos);
		
		return gson.toJson(dto);
	}
	
	
	public String getSubSlipAllotmentsWithNextEmps1(Integer offerLetterId, Integer seniorityId, Integer deptId) {

		SubSlipAllotmentWithEmpListDto dto = new SubSlipAllotmentWithEmpListDto();

		List<SubSlipAllotment> allotments = subSlipAllotmentRepository
				.findBySubSlipIdOrderByCreatedDateDesc(offerLetterId);
		List<Integer> allotedToIds = allotments.stream().map(allot -> allot.getAllotedTo().getId())
				.toList();
		List<SubSlipAllotmentDto> allotmentDtos = allotments.stream()
				.map(allotment -> mapper.map(allotment, SubSlipAllotmentDto.class)).toList();

		dto.setSubSlipAllotmentDtos(allotmentDtos);
		List<EmpRoleDto> userDtos = new ArrayList<>();


			List<Object[]> empList = allotedToIds.isEmpty()
					? employeeRepository.getActiveEmpBySubOrdinateofDeptId(seniorityId, deptId)
					: employeeRepository.findAvailableHoSByDeptType1(seniorityId, deptId, allotedToIds);

			userDtos = empList.stream().map(emp -> {
				EmpRoleDto empDto = new EmpRoleDto();
				empDto.setId(Integer.parseInt(emp[0].toString()));
				empDto.setName(String.valueOf(emp[1].toString()));
				return empDto;
			}).toList();
		
		dto.setEmps(userDtos);

		return gson.toJson(dto);
	}

}
