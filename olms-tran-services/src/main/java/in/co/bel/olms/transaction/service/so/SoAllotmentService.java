package in.co.bel.olms.transaction.service.so;

import static in.co.bel.core.constants.NotificationConstants.SO_ALLOTMENT;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.dto.SoAllotmentDto;
import in.co.bel.olms.transaction.dto.SoAllotmentWithEmpListDto;
import in.co.bel.olms.transaction.entity.SoAllotment;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SoAllotmentRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SoAllotmentService {

	private final EmployeeDao employeeDao;

	private final SupplyOrderService supplyOrderService;

	private final SoAllotmentRepository soAllotmentRepository;

	private final EmployeeRepository employeeRepository;

	private final NotificationService notificationService;

	private final WSService wsService;	

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(SoAllotmentService.class);
	
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
	
	private Gson gson = new Gson();

	/**
	 * @param soAllotmentDto
	 * @return
	 * 
	 * Allot SO to subordinate
	 * 
	 * Modified: Shubham Anand
	 * Added existing condition, notification to parent chain of allotedTo
	 */
	public SoAllotment allotSubordinate(@Valid SoAllotmentDto soAllotmentDto) {
		SoAllotment returnSoAllotment;
		Employee allotedBy = employeeDao.getEmployee(soAllotmentDto.getAllotedBy());
		Employee allotedTo = employeeDao.getEmployee(soAllotmentDto.getAllotedTo());
		
		//check for existing allotment; return if existing, else save new
//		SoAllotment existing = soAllotmentRepository.getAllotedSoToEmp(soAllotmentDto.getSupplyOrderId(), soAllotmentDto.getAllotedTo());
//		if (existing != null)
//			returnSoAllotment = existing;
//		else {
			SoAllotment allotment = new SoAllotment();
			allotment.setAllotedBy(allotedBy);
			allotment.setAllotedTo(allotedTo);
			SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soAllotmentDto.getSupplyOrderId());
			allotment.setSupplyOrder(supplyOrder);
			allotment.setCreatedDate(new Date());
			returnSoAllotment = soAllotmentRepository.save(allotment);
//		}
		
		//notify parent chain of allotedTo
		try {
			List<Integer> receivers = new ArrayList<>();			
			List<Object[]> response = employeeRepository.getAllParentIncCurrentEmpOfEmpByRoleId(soAllotmentDto.getAllotedTo(), -1);
			for(Object[] item : response) {
				receivers.add(Integer.parseInt(item[0].toString()));	
			}
			
			String msg = "Supply Order " + soAllotmentDto.getSupplyOrderName() + " alloted to " + allotedTo.getEname() + " by " + allotedBy.getEname();
			
			NotificationDto notificationDto = new NotificationDto();
			notificationDto.setNotificationName(msg);
			notificationDto.setSenderId(soAllotmentDto.getAllotedBy());
			notificationDto.setReceiverId(soAllotmentDto.getAllotedTo());
      notificationDto.setNotificationCategory(SO_ALLOTMENT);
			notificationDto.setCategoryId(soAllotmentDto.getSupplyOrderId());
			notificationDto.setSbuName(returnSoAllotment.getSupplyOrder().getSalesGroup().getName());
			notificationService.saveNotification(notificationDto);
			
						
			MessageBean message = new MessageBean();
			message.setSender(1);
      message.setMessageType(SO_ALLOTMENT);
			message.setMessage(msg);
			message.setItemNo(soAllotmentDto.getSupplyOrderName());
			message.setReceivers(receivers);
			message.setSbuName(msg);
			wsService.notifyCqae(message);					

		} catch (Exception e) {			
			logger.error("Notification error in Allot subordinate for SO {} ", e.getMessage());
			logger.error("Error in Allot subordinate for SO ", e);
		}
		
		return returnSoAllotment;
		
	}

	public @Valid SoAllotmentDto convertBotoDto(SoAllotment soAllotment) {
		SoAllotmentDto soAllotmentDto =  new SoAllotmentDto();
		soAllotmentDto.setId(soAllotment.getId());
		
		// To be completed
		
		return soAllotmentDto;
	}
	
	
	/**
	 * @author Shubham Anand
	 * @param empId
	 * @return
	 * @throws ParseException 
	 */
	public List<SoAllotmentDto> getSoAllotmentForEmpId(int empId) throws DataNotValidException, ParseException {
	
		List<Object[]> response = soAllotmentRepository.getAllotmentDetailsByEmpId(empId);
		
		List<SoAllotmentDto> soAllotmentDtos = new ArrayList<>();
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		//[0]: supply_order_id; [1]: order_no; [2]: order_date; [3]: alloted_to; [4]: alloted_to_id; [5]: alloted_by
		for(Object[] item : response) {
			SoAllotmentDto dto = new SoAllotmentDto();
			dto.setSupplyOrderId(Integer.parseInt(item[0].toString()));
			dto.setSupplyOrderName(item[1].toString());
			  dto.setSupplyOrderDate(dateFormat.format((java.util.Date) item[2]));
			dto.setAllotedToName(item[3].toString());
			dto.setAllotedByName(item[5].toString());
			dto.setEpcode(item[6] != null ? item[6].toString() : "");
			dto.setItemCategory(item[7] != null ? item[7].toString() : "");
			if(item[4].toString() != null && item[4].toString().split(",").length == 1 && Integer.parseInt(item[4].toString().split(",")[0]) == empId) 
				dto.setIsAlloteBtnVisible(true);
			else
				dto.setIsAlloteBtnVisible(false);
			
			if(null != item[8]) {
//				String dt = dateFormat.format(item[6].toString());
//				String date = dt.substring(0, dt.indexOf(' '));
//				String time = dt.substring(dt.indexOf(' ')+1);
				String dt = item[8].toString();
				dto.setDate(dt);
//				dto.setTime(time);
			}
			else {
				dto.setDate(" ");
//				dto.setTime(" ");
				
			}
			
			soAllotmentDtos.add(dto);
		}
		
		return soAllotmentDtos;
	}
	
	
	/**
	 * @author Shubham Anand
	 * @param empId
	 * @return
	 * @throws DataNotValidException
	 */
	public Map<SupplyOrder, String> getSoForAllotedEmp(int empId) throws DataNotValidException{
		Map<SupplyOrder, String> map = new LinkedHashMap<>();
		List<SoAllotment> allotments = soAllotmentRepository.getSoAllotmentByEmpId(empId);
		for(SoAllotment allotment : allotments) {
			map.put(allotment.getSupplyOrder(), allotment.getAllotedBy().getEname());
		}
		return map;
	}


  public SoAllotmentWithEmpListDto getSoAllotmentsWithNextEmps(Integer soId, Integer empId, Integer roleId) {
    List<SoAllotment> allotments = soAllotmentRepository.findBySupplyOrderIdOrderByCreatedDateDesc(soId);

    SoAllotmentWithEmpListDto dto = new SoAllotmentWithEmpListDto();
    List<Integer> allotedToIds = new ArrayList<>();
    List<SoAllotmentDto> allotmentDtos = new ArrayList<>();

    for (SoAllotment allot : allotments) {
        SoAllotmentDto allotmentDto = mapper.map(allot, SoAllotmentDto.class);
        
        if (allot.getAllotedBy() != null) {
            String staffNo = allot.getAllotedBy().getStaffNo();
            if (staffNo != null && !staffNo.isEmpty()) {
                allotmentDto.setAllotedByNameAndStaffNo(allot.getAllotedBy().getEname() + " - " + staffNo);
            } else {
                allotmentDto.setAllotedByNameAndStaffNo(allot.getAllotedBy().getEname());
            }
        }

        allotmentDtos.add(allotmentDto);

        if (allot.getAllotedTo() != null) {
        	String staffNo = allot.getAllotedTo().getStaffNo();
            if (staffNo != null && !staffNo.isEmpty()) {
                allotmentDto.setAllotedToNameAndStaffNo(allot.getAllotedTo().getEname() + " - " + staffNo);
            } else {
                allotmentDto.setAllotedToNameAndStaffNo(allot.getAllotedTo().getEname());
            }
            allotedToIds.add(allot.getAllotedTo().getId());
        }
    }
    dto.setSoAllotmentDtos(allotmentDtos);

    List<Object[]> qaEmployees = allotedToIds.isEmpty()
        ? employeeRepository.getEmpsForQA()
        : employeeRepository.getAvailableQaEmps(allotedToIds);

    if (qaEmployees.isEmpty()) throw new DataNotFoundException("QA Officers not found");

    dto.setQaUsers(qaEmployees.stream().map(emp -> {
      EmpRoleDto eDto = new EmpRoleDto();
      eDto.setId((Integer) emp[0]);
      eDto.setName(emp[1] != null ? emp[1].toString() : "");
      if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
          eDto.setNameAndStaffNo(eDto.getName() + " - " + emp[2].toString());
      } else {
          eDto.setNameAndStaffNo(eDto.getName());
      }
      return eDto;
    }).toList());

    if (roleId == ApplicationConstants.Role.PLANNER) { // If planner, get All available HoS
      List<Object[]> hosEmployees = allotedToIds.isEmpty()
          ? employeeRepository.getAllHoS(ApplicationConstants.Dept.ASSEMBLY)
          : employeeRepository.findAvailableHoSByDeptType(ApplicationConstants.Dept.ASSEMBLY, allotedToIds);

      if (hosEmployees.isEmpty()) throw new DataNotFoundException("QC Officers not found");

      dto.setQcUsers(hosEmployees.stream().map(emp -> {
        EmpRoleDto eDto = new EmpRoleDto();
        eDto.setId((Integer) emp[0]);
        eDto.setName(emp[1] != null ? emp[1].toString() : "");
        if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
            eDto.setNameAndStaffNo(eDto.getName() + " - " + emp[2].toString());
        } else {
            eDto.setNameAndStaffNo(eDto.getName());
        }
        return eDto;
      }).toList());
    } else {
      List<Employee> qcEmployees = allotedToIds.isEmpty()
          ? employeeRepository.getSubByEmp(empId)
          : employeeRepository.getAvailableSubordinates(empId, allotedToIds);

      if (qcEmployees.isEmpty()) throw new DataNotFoundException("QC Officers not found");

      dto.setQcUsers(qcEmployees.stream().map(emp -> {
        EmpRoleDto eDto = new EmpRoleDto();
        eDto.setId(emp.getId());
        String roleName = emp.getEmpRole() != null ? emp.getEmpRole().getEmpRole() : "";
        eDto.setName(emp.getEname() != null ? emp.getEname() + " (" + roleName + ")" : "");
        if (emp.getStaffNo() != null && !emp.getStaffNo().toString().trim().isEmpty()) {
            eDto.setNameAndStaffNo(eDto.getName() + " - " + emp.getStaffNo().toString());
        } else {
            eDto.setNameAndStaffNo(eDto.getName());
        }
        return eDto;
      }).toList());
    }

    return dto;
  }
}
