package in.co.bel.olms.transaction.service.insp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.core.constants.NotificationConstants;
import in.co.bel.olms.master.dao.EmployeeDao;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.repository.InspectionAuthorityRepository;
import in.co.bel.olms.master.repository.InspectorRepository;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.dto.insp.AllotSoDto;
import in.co.bel.olms.transaction.entity.Project;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.repository.insp.SoInspRepository;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;
import in.co.bel.olms.transaction.service.so.SoAllotmentService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */

@Service
@RequiredArgsConstructor
public class SoInspService {

	private final SoInspRepository soInspRepository;

	private final InspectionAuthorityRepository inspectionAuthorityRepository;

	private final InspectorRepository inspectorRepository;

	private final SupplyOrderRepository supplyOrderRepository;

	private final  SupplyOrderService supplyOrderService;

	private final  EmployeeDao employeeDao;

	private final  Mapper mapper;

	Gson gson = new Gson();

	private final  NotifyUtil notifyUtil;

	private final NotificationService notificationService;

	private final SoAllotmentService soAllotmentService;
	
	private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
	
	private static final Logger logger =LoggerFactory.getLogger(SoInspService.class);
	
	/**
	 * To fetch the supply orders based on the inspector id. The filtering to be
	 * made based on the so status
	 * @since 06-03-2023
	 * @param inspId
	 * @return the List of the supply orders as dto
	 */
	public String getSoByInspId(Integer inspId) {
		List<SupplyOrderDto> dtoLsit = new ArrayList<>();
		
		//Modified: Shubham Anand
		//Get SupplyOrder List from soAllotment table
		Map<SupplyOrder, String> map = soAllotmentService.getSoForAllotedEmp(inspId);
		
		for (Map.Entry<SupplyOrder, String> entry : map.entrySet()) {
			try {
				SupplyOrder so = entry.getKey();
				SupplyOrderDto dto = new SupplyOrderDto();
				dto.setId(so.getId());
				dto.setAllotedBy(entry.getValue());
				dto.setOrderNo(so.getOrderNo());
				dto.setFileNo(so.getFileNo());
				dto.setOrderDateStr(outputDateFormat.format(so.getOrderDate()));

				List<Project> projects = so.getProjects();
				// Changed after multiple projects for one PO/SO
				String allProjectNames = "";
				if (!projects.isEmpty()) {

					allProjectNames = projects.stream()
							.map(Project::getProjectName)
							.collect(Collectors.joining(","));
				}
				dto.setProjectName(allProjectNames);

				if(null!=so.getSalesGroup() ) {
					dto.setSalesGroupName(so.getSalesGroup().getName());
				}
				else
				{
					dto.setSalesGroupName("");
				}	
				
				List<String> alertmessage=new ArrayList<>();
				
				if(null==so.getFcm() ) {
					dto.setIsDisplayAlert(true);
					alertmessage.add("FCM to be completed withing 7 working days  ");  
				}
				else
				{
					if(null ==  so.getFcm().getSubmittedDate())
					{
						dto.setIsDisplayAlert(true);
						alertmessage.add("FCM to be completed withing 7 working days ");  
					}
				}

				if(null==so.getFcl() ) {
					dto.setIsDisplayAlert(true);
					alertmessage.add("FCL to be completed withing 7 working days");  
				}
				else
				{
					if(null ==  so.getFcl().getSubmittedDate())
					{
						dto.setIsDisplayAlert(true);
						alertmessage.add("FCL to be completed withing 7 working days  ");  
					}
				}
				
				dto.setAlertMessage(alertmessage);
				dtoLsit.add(dto);
			} catch (MappingException e) {
				logger.error("Error in getting So By InspId{}", e.getMessage());
			}
		}
		Gson gsondate = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
		return gsondate.toJson(dtoLsit);

	}

	/**
	 * 
	 * @param roleId
	 * @param empId
	 * @return
	 */
	public String getSoByRolenEmpId(Integer roleId, Integer empId) {

		List<AllotSoDto> dtoLsit = new ArrayList<>();
		List<SupplyOrder> soList ;
		switch (roleId) {
		case ApplicationConstants.Role.INSPECTOR:
			soList = soInspRepository.getSupplyOrderByInspectionAuthId(empId);
		break;
		case ApplicationConstants.Role.ACQAO:
			 soList = soInspRepository.getSupplyOrderByAcqaoId(empId);
			break;
		case ApplicationConstants.Role.DCQAO:
			soList = soInspRepository.getSupplyOrderByDcqaoId(empId);
			break;
 
		case ApplicationConstants.Role.JCQAO:
			soList = soInspRepository.getSupplyOrderByJcqaoId(empId);
			break;
		default:
			soList= new ArrayList<>();
			break;
		}

		
		for (SupplyOrder so : soList) {
			AllotSoDto dto = mapper.map(so, AllotSoDto.class);			
			dtoLsit.add(dto);
		}
		Gson gsondate = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
		return gsondate.toJson(dtoLsit);

	}

//	/**
//	 * @author Manasa B
//	 * @since 20-01-2023
//	 * @param soDto
//	 * @return
//	 */
//	public AllotSoDto allotInsp(SupplyOrderDto soDto) {
//		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soDto.getId());
//
//		Employee emp = employeeDao.getEmployee(soDto.getEmployeeId());
//		supplyOrder.setEmployee(emp);
//
//		supplyOrder = supplyOrderRepository.save(supplyOrder);
//
//		AllotSoDto alDto = mapper.map(supplyOrder, AllotSoDto.class);
//		try {
//			
//			List<Integer> receivers  =new ArrayList<>();
//			receivers.add(soDto.getEmployeeId());
//			notifyUtil.NotifyUser("SO alloted to Inspector for the order no " + supplyOrder.getOrderNo(), supplyOrder.getOrganization(), 1, supplyOrder.getOrderNo(), 1, NotificationConstants.SO, receivers);
//		} catch (Exception e) {
//			logger.error("Notification error in Allot inspector for SO {} ", e.getMessage());
//			e.printStackTrace();
//		}
//		return alDto;
//	}

//	/**
//	 * @author Manasa B
//	 * @since 21-02-2023
//	 * @param soDto
//	 * @return
//	 */
//	public AllotSoDto allotSecOfficer(SupplyOrderDto soDto) {
//		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soDto.getId());
//
//		Employee emp = employeeDao.getEmployee(soDto.getSecOfficerId());
//		supplyOrder.setSectionOfficer(emp);
//
//		supplyOrder = supplyOrderRepository.save(supplyOrder);
//
//		AllotSoDto alDto = mapper.map(supplyOrder, AllotSoDto.class);
//		try {
//			
//			List<Integer> receivers  =new ArrayList<>();
//			receivers.add(soDto.getSecOfficerId());					
//			notifyUtil.NotifyUser("SO alloted to ACQAO for the order no " + supplyOrder.getOrderNo(), supplyOrder.getOrganization(), 1, supplyOrder.getOrderNo(), 1, NotificationConstants.SO, receivers);
//		} catch (Exception e) {
//			logger.error("Notification error in Allot acqao for SO {} ", e.getMessage());
//			e.printStackTrace();
//		}
//		return alDto;
//	}
	
//	/**
//	 * @author Manasa B
//	 * @since 23-02-2023
//	 * @param soDto
//	 * @return
//	 */
//	public AllotSoDto allotDcqao(SupplyOrderDto soDto) {
//		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soDto.getId());
//
//		Employee emp = employeeDao.getEmployee(soDto.getDcqaoId());
//		supplyOrder.setDcqao(emp);
//
//		supplyOrder = supplyOrderRepository.save(supplyOrder);
//
//		AllotSoDto alDto = mapper.map(supplyOrder, AllotSoDto.class);
//		try {
//			
//			List<Integer> receivers  =new ArrayList<>();
//			receivers.add(soDto.getDcqaoId());					
//			notifyUtil.NotifyUser("SO alloted to DCQAO for the order no " + supplyOrder.getOrderNo(), supplyOrder.getOrganization(), 1, supplyOrder.getOrderNo(), 1, NotificationConstants.SO, receivers);
//		} catch (Exception e) {
//			logger.error("Notification error in Allot dcqao for SO {} ", e.getMessage());
//			e.printStackTrace();
//		}
//		return alDto;
//	}
	
//	/**
//	 * @author Manasa B
//	 * @since 17-03-2023
//	 * @param soDto
//	 * @return
//	 */
//	public AllotSoDto allotJcqao(SupplyOrderDto soDto) {
//		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(soDto.getId());
//
//		Employee emp = employeeDao.getEmployee(soDto.getJcqaoId());
//		supplyOrder.setJcqao(emp);
//
//		supplyOrder = supplyOrderRepository.save(supplyOrder);
//
//		AllotSoDto alDto = mapper.map(supplyOrder, AllotSoDto.class);
//		
//		try {
//			NotificationDto notificationDto = new NotificationDto();
//			notificationDto.setNotificationName("Supply Order received for allotment: " + supplyOrder.getOrderNo());
//			notificationDto.setNotificationCategory("SO");
//			notificationDto.setCategoryId(supplyOrder.getId());
//			notificationService.saveNotification(notificationDto);
//			if(supplyOrder.getOrganization() != null) {
//				List<Integer> receivers =  new ArrayList<>();
//				receivers.add(emp.getId());				
//				MessageBean message = new MessageBean();
//				message.setSender(1);
//				message.setMessageType(ApplicationConstants.SUPPLY_ORDER);
//				message.setMessage("Supply Order received for allotment: "+supplyOrder.getOrderNo());
//				message.setItemNo(supplyOrder.getOrderNo());
//				message.setReceivers(receivers);
//				wsService.notifyCqae(message);					
//			}
//		} catch (Exception e) {			
//			logger.error("Notification error in Allot jcqao for SO {} ", e.getMessage());
//			e.printStackTrace();
//		}
//		return alDto;
//	}
	
	
	
	public AllotSoDto allotSubordinate(AllotSoDto allotDto)
	{
		SupplyOrder supplyOrder = supplyOrderService.getSupplyOrder(allotDto.getId());
		Employee emp = employeeDao.getEmployee(allotDto.getSubordinateId());
		switch (emp.getEmpRole().getId()) {
//		case ApplicationConstants.Role.INSPECTOR:
//			supplyOrder.setEmployee(emp);
//		break;
//		case ApplicationConstants.Role.ACQAO:
//			supplyOrder.setSectionOfficer(emp);
//			break;
//		case ApplicationConstants.Role.DCQAO:
//			supplyOrder.setDcqao(emp);
//			break; 
//		case ApplicationConstants.Role.JCQAO:
//			supplyOrder.setJcqao(emp);
//			break;
		default:			
			break;
		}
		supplyOrder = supplyOrderRepository.save(supplyOrder);
		try {			
			List<Integer> receivers  =new ArrayList<>();
			receivers.add(allotDto.getSubordinateId());
			notifyUtil.notifyUser("SO alloted to Subordinate for the order no " + supplyOrder.getOrderNo(), supplyOrder.getOrganization(), 1, supplyOrder.getOrderNo(), 1, NotificationConstants.SO, receivers,supplyOrder.getSalesGroup().getName());			
		} catch (Exception e) {
			logger.error("Notification error in Allot subordiante for OL {} ", e.getMessage());
			logger.error("Error Allot subordiante for OL ", e);
		}
		return new AllotSoDto();
	}
}
