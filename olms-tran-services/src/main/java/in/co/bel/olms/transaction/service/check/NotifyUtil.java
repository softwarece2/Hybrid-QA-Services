package in.co.bel.olms.transaction.service.check;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.transaction.dto.NotificationDto;
import in.co.bel.olms.transaction.service.NotificationService;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 19-04-2023
 */
@Component
@RequiredArgsConstructor
public class NotifyUtil {

	private final NotificationService notificationService;

	private final WSService wsService;

	private final EmployeeRepository employeeRepository;
	
	private static final Logger logger =LoggerFactory.getLogger(NotifyUtil.class);	

	private final SalesGroupRepository salesGroupRepository;
	
	/**
	 * 
	 * @param message
	 * @param organization
	 * @param CategoryId
	 * @param itemNo
	 * @param sender
	 * @param notifyCategory
	 * @param receivers
	 * @return
	 */
	public String notifyUser( String message, Organization organization, Integer categoryId, String itemNo, int sender, String notifyCategory, List<Integer> receivers ,String sbuName)
	{
		try {
			
			List<NotificationDto> notificationDtos = new ArrayList<>();
			
			for(Integer receiver : receivers ) {
				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setNotificationName(message);
				notificationDto.setNotificationCategory(notifyCategory);
				notificationDto.setCategoryId(categoryId);
				notificationDto.setSenderId(sender);
				notificationDto.setReceiverId(receiver);
				notificationDto.setSbuName(sbuName);
				notificationDtos.add(notificationDto);
			}
			
			notificationService.saveNotifications(notificationDtos);			
			
			if(organization != null) {				
				MessageBean messageBean = new MessageBean();
				messageBean.setSender(sender);
				messageBean.setMessageType(ApplicationConstants.SUPPLY_ORDER);
				messageBean.setMessage(message);
				messageBean.setItemNo(itemNo);
				messageBean.setReceivers(receivers);
				//notificationDto.setSbuName(sbuName);
				wsService.notifyCqae(messageBean);					
			}
		} catch (Exception e) {
			return ApplicationConstants.FAILURE;
		}
		
		return ApplicationConstants.SUCCESS;
		
	}
	
	/**
	 * 
	 * @param message
	 * @param categoryId
	 * @param itemNo
	 * @param sender
	 * @param notifyCategory
	 * @param receivers
	 * @return
	 */
	public String notifyCqae(String message, Integer categoryId, String itemNo, int sender,
			String notifyCategory, List<Integer> receivers,String sbuName) {
try	 {
			MessageBean messageBean = new MessageBean();
			messageBean.setSender(sender);
			messageBean.setMessageType(notifyCategory);
			messageBean.setMessage(message);
			messageBean.setItemNo(itemNo);
			messageBean.setReceivers(receivers);
			messageBean.setSbuName(sbuName);
			wsService.notifyCqae(messageBean);

			for (Integer receiverId : receivers) {
				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setNotificationName(message);
				notificationDto.setNotificationCategory(notifyCategory);
				notificationDto.setCategoryId(categoryId);
				notificationDto.setSenderId(sender);
				notificationDto.setReceiverId(receiverId);
				notificationDto.setSbuName(sbuName);
				notificationService.saveNotification(notificationDto);
			}
		} catch (Exception e) {
			return ApplicationConstants.FAILURE;
		}
		return ApplicationConstants.SUCCESS;
	}
	
	/**
	 * 
	 * @param message
	 * @param categoryId
	 * @param itemNo
	 * @param sender
	 * @param notifyCategory
	 * @param sbu
	 * @return
	 */
	public String  NotifySBUnCqae( String message,  Integer categoryId, String itemNo, int sender, String notifyCategory, Integer sbuId, int empId) {		
		try {						
//			List<Integer> orgIds  =new ArrayList<>();			
//			orgIds.add(sbu);
//			orgIds.add(cqaeId);
			
			//Get all the users of the Organization ID
			List<Integer> receivers =	employeeRepository.getParentByEmpId(empId);		
			
			List<Integer> sbuEmpIds = employeeRepository.getEmpIdsBySalesGrpId(sbuId);
			String sbuName=salesGroupRepository.getSbuName(sbuId);
			receivers.addAll(sbuEmpIds);
			
			MessageBean messageBean = new MessageBean();
			messageBean.setSender(sender);
			messageBean.setMessageType(ApplicationConstants.SUPPLY_ORDER);
			messageBean.setMessage(message);
			messageBean.setItemNo(itemNo);
			messageBean.setReceivers(receivers);
			messageBean.setSbuName(sbuName);
			wsService.notifyCqae(messageBean);				
			
			for (Integer receiverId : receivers) {
				NotificationDto notificationDto = new NotificationDto();
				notificationDto.setNotificationName(message);
				notificationDto.setNotificationCategory(notifyCategory);
				notificationDto.setCategoryId(categoryId);
				notificationDto.setSenderId(sender);
				notificationDto.setReceiverId(receiverId);
				notificationDto.setSbuName(sbuName);
				notificationService.saveNotification(notificationDto);
			}
		}
		catch(Exception e) {			
			return ApplicationConstants.FAILURE;			
		}		
		return ApplicationConstants.SUCCESS;		
	}
}
