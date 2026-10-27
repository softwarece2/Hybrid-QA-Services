package in.co.bel.olms.transaction.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants.Role;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.OlAllotmentDto;
import in.co.bel.olms.transaction.dto.WarrantAllotmentDto;
import in.co.bel.olms.transaction.dto.WarrantAllotmentWithEmpListDto;
import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.OlAllotment;
import in.co.bel.olms.transaction.entity.WarrantAllotment;
import in.co.bel.olms.transaction.repository.CategoryRepository;
import in.co.bel.olms.transaction.repository.WarrantAllotmentRepository;
import in.co.bel.olms.transaction.repository.WarrantRepository;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarrantAllotmentService {

	private final EmployeeDao employeeDao;

	private final WarrantService warrantService;

	private final WarrantAllotmentRepository warrantAllotmentRepository;

	private final EmployeeRepository employeeRepository;

	private final CategoryRepository categoryRepository;

	private final NotificationService notificationService;

	private final WSService wsService;

	private final WarrantRepository warrantRepository;

	private final Mapper mapper;
	
	private Gson gson = new Gson();

	private static final Logger logger = LoggerFactory.getLogger(WarrantAllotmentService.class);
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

	/**
	 * @param olAllotmentDto
	 * @return
	 * 
	 *         Allot OL to subordinate
	 * 
	 * 
	 */
	public List<WarrantAllotment> allotSubordinate(@Valid WarrantAllotmentDto warrantAllotmentDto) {

		Category category = categoryRepository.findById(warrantAllotmentDto.getWarrantId())
				.orElseThrow(() -> new IllegalArgumentException("Warrant not found for id: " + warrantAllotmentDto.getWarrantId()));
		
		List<WarrantAllotment> allotmentList = new ArrayList<>();
		List<NotificationDto> notificationDtoList = new ArrayList<>();
		List<Integer> receivers = new ArrayList<>();
		List<String> allotedToNameList = new ArrayList<>();		

		Employee allotedBy = employeeDao.getEmployee(warrantAllotmentDto.getAllotedBy());
		
		for (Integer allotedToId : warrantAllotmentDto.getAllotedToIds()) {
			Employee allotedTo = employeeDao.getEmployee(allotedToId);
			String sbuName = null;

			WarrantAllotment allotment = new WarrantAllotment();
			allotment.setAllotedBy(allotedBy);
			allotment.setAllotedTo(allotedTo);
			allotment.setCategory(category);
			allotment.setIsDeligation(false);
			allotmentList.add(allotment);
			allotedToNameList.add(allotedTo.getEname());
			
			// notify all alloted to and by employees of allotment
			try {
				List<Object[]> response = employeeRepository
						.getAllParentIncCurrentEmpOfEmpByRoleId(allotedToId, -1);
				for (Object[] item : response) {
					receivers.add(Integer.parseInt(item[0].toString()));
				}

				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setSenderId(warrantAllotmentDto.getAllotedBy());
				notificationDto.setReceiverId(allotedToId);
				notificationDto.setNotificationCategory(NotificationConstants.WARRANT_ALLOTMENT_TYPE);
				notificationDto.setCategoryId(warrantAllotmentDto.getWarrantId());
				notificationDto.setSbuName(sbuName);
				notificationDtoList.add(notificationDto);

			} catch (Exception e) {
				logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
				logger.error("Error occured during Allot subordinate for SO ", e);
			}
		}
		List<WarrantAllotment> savedAllotments = warrantAllotmentRepository.saveAll(allotmentList);
		
		// notify all alloted to and by employees of allotment
		try {
			String allotedTName = allotedToNameList.stream().collect(Collectors.joining(", "));
			String msg = "Warrant " + category.getCategoryName() + " alloted to "
					+ allotedTName + " by " + allotedBy.getEname();
			
			for(NotificationDto dto : notificationDtoList) {
				dto.setNotificationName(msg);
			}
			
			notificationService.saveNotifications(notificationDtoList);
			MessageBean message = new MessageBean();
			message.setSender(1);
			message.setMessageType(NotificationConstants.WARRANT_ALLOTMENT_TYPE);
			message.setMessage(msg);
			message.setItemNo(category.getCategoryName());
			message.setReceivers(receivers);
			wsService.notifyCqae(message);
		} catch (Exception e) {
			logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
			logger.error("Error occured during Allot subordinate for SO ", e);
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
	public List<WarrantAllotmentDto> getWarrantAllotmentForEmpId(int orgId,int empId) throws DataNotValidException {

		List<Object[]> response = warrantAllotmentRepository.getAllotmentDetailsByEmpId( orgId,empId);

		List<WarrantAllotmentDto> warrantAllotmentDtos = new ArrayList<>();

		for (Object[] item : response) {
			try {
				WarrantAllotmentDto dto = new WarrantAllotmentDto();
				dto.setWarrantId(Integer.parseInt(item[0].toString()));

				if (null != item[1])
					dto.setWarrantName(item[1].toString());
				else
					dto.setWarrantName("");

				if (null != item[2])
					dto.setWarrantDate(item[2].toString());
				else
					dto.setWarrantDate("");

				if (null != item[3])
					dto.setAllotedToName(item[3].toString());
				else
					dto.setAllotedToName("");

				if (null != item[5])
					dto.setAllotedByName(item[5].toString());
				else
					dto.setAllotedByName("");
				
				dto.setIsAllotedToEmp((boolean) item[10]);

				if (item[4].toString() != null && item[4].toString().split(",").length == 1
						&& Integer.parseInt(item[4].toString().split(",")[0]) == empId) {
					dto.setIsAlloteBtnVisible(true);
				} else {
					dto.setIsAlloteBtnVisible(false);
				}
				
				if(null != item[9]) {
				    String date = item[9].toString();
				    dto.setDate(date);				    		
				}
				else {
					dto.setDate(" ");
				}
					

				warrantAllotmentDtos.add(dto);
			} catch (Exception e) {
				logger.error("Error occured in fetching warrant allotments ", e);
			}
		}

		Collections.sort(warrantAllotmentDtos, new Comparator<WarrantAllotmentDto>() {

			@Override
			public int compare(WarrantAllotmentDto o1, WarrantAllotmentDto o2) {

				return o2.getIsAlloteBtnVisible().compareTo(o1.getIsAlloteBtnVisible());
			}

		});
		return warrantAllotmentDtos;
	}

	

	public String getWarrantAllotmentsWithNextEmps(Integer categoryId, Integer deptTypeId, Integer empId, Integer roleId){
		
		WarrantAllotmentWithEmpListDto dto = new WarrantAllotmentWithEmpListDto();
		
		List<WarrantAllotment> allotments = warrantAllotmentRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
//		List<Integer> allotedToIds = allotments.stream().map(allot -> allot.getAllotedTo().getId()).collect(Collectors.toList());
//		List<WarrantAllotmentDto> allotmentDtos = allotments.stream().map(allotment -> mapper.map(allotment, WarrantAllotmentDto.class)).collect(Collectors.toList());
		List<WarrantAllotmentDto> allotmentDtos = new ArrayList<>();
		List<Integer> allotedToIds = new ArrayList<>();
		
		
		for (WarrantAllotment allot : allotments) {
	        WarrantAllotmentDto allotmentDto = mapper.map(allot, WarrantAllotmentDto.class);
	        
	        if (allot.getAllotedBy() != null) {
	        	String ename = allot.getAllotedBy().getEname();
	            String staffNo = allot.getAllotedBy().getStaffNo();

	            if (staffNo != null && !staffNo.trim().isEmpty()) {
	                allotmentDto.setAllotedByNameAndStaffNo(ename + " - " + staffNo);
	            } else {
	                allotmentDto.setAllotedByNameAndStaffNo(ename);
	            }
	        }

	        if (allot.getAllotedTo() != null) {
	            String ename = allot.getAllotedTo().getEname();
	            String staffNo = allot.getAllotedTo().getStaffNo();

	            if (staffNo != null && !staffNo.trim().isEmpty()) {
	                allotmentDto.setAllotedToNameAndStaffNo(ename + " - " + staffNo);
	            } else {
	                allotmentDto.setAllotedToNameAndStaffNo(ename);
	            }
	            
	            allotedToIds.add(allot.getAllotedTo().getId());
	        }
	        
	        allotmentDtos.add(allotmentDto);
		}
		
		dto.setWarrantAllotmentDtos(allotmentDtos);
		List<EmpRoleDto> userDtos = new ArrayList<>();
		
		if(roleId != Role.HOD) {
			if(deptTypeId != null) {
				List<Object[]> empList = allotedToIds.isEmpty() 
						? employeeRepository.getAllHoS(deptTypeId) 
						: employeeRepository.findAvailableHoSByDeptType(deptTypeId, allotedToIds);	
				
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
			}
		} else {
			List<Employee> employees = allotedToIds.isEmpty()
					? employeeRepository.getSubByEmp(empId)
					: employeeRepository.getAvailableSubordinates(empId, allotedToIds);	
			
			if (employees.isEmpty()) 
				throw new DataNotFoundException("Section Officers not found by OrgId: " );
			
			userDtos = employees.stream().map(emp -> {
				EmpRoleDto eDto = new EmpRoleDto();
	            eDto.setId(emp.getId());
	            eDto.setName(emp.getEname());
	            if (emp.getStaffNo()!= null && !emp.getStaffNo().toString().trim().isEmpty()) {
	            	eDto.setNameAndStaffNo(emp.getEname() + " - " + emp.getStaffNo());
                } else {
                	eDto.setNameAndStaffNo(emp.getEname());
                }
	            return eDto;
	        }).toList();
		}
		dto.setEmps(userDtos);
		
		return gson.toJson(dto);
	}
	
}
