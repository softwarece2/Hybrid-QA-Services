package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsTranServicesApplication;
//import in.co.bel.olms.transaction.controller.NotificationController;
//import in.co.bel.olms.transaction.dto.NotificationDto;
//import in.co.bel.olms.transaction.entity.Notification;
//import in.co.bel.olms.transaction.service.NotificationService;
//import in.co.bel.olms.transaction.service.OfferLetterService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class NotificationControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(NotificationControllerTest.class);
//	
//	@InjectMocks
//	NotificationController notificationController;
//	
//	@Mock
//	NotificationService notificationService;
//	
//	@Mock
//	OfferLetterService olService;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void updateReadStatusValidTest() {
//		logger.info("NotificationControllerTest.updateReadStatusValidTest : Initiated");
//		try {
//			NotificationDto notificationDto = new NotificationDto();
//			notificationDto.setId(12);
//			notificationDto.setReadBy(2);
//			when(notificationService.updateReadStatus(anyInt(), anyInt())).thenReturn(true);
//			ResponseEntity<Boolean> result = notificationController.updateReadStatus(notificationDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateReadStatusValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateReadStatusValidTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.updateReadStatusValidTest : Finished");
//	}
//	
//	@Test
//	void updateReadStatusSecScenarioTest() {
//		logger.info("NotificationControllerTest.updateReadStatusSecScenarioTest : Initiated");
//		try {
//			NotificationDto notificationDto = new NotificationDto();
//			notificationDto.setId(12);
//			notificationDto.setReadBy(2);
//			when(notificationService.updateReadStatus(anyInt(), anyInt())).thenReturn(false);
//			ResponseEntity<Boolean> result = notificationController.updateReadStatus(notificationDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateReadStatusSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateReadStatusSecScenarioTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.updateReadStatusSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getUnreadNotificationsValidTest() {
//		logger.info("NotificationControllerTest.getUnreadNotificationsValidTest : Initiated");
//		try {
//			int empId = 1;
//			List<Notification> lNotification = new ArrayList<>();
//			Notification notification = new Notification();
//			notification.setCreatedDateTime(new Date());
//			notification.setReadStatus(true);
//			notification.setSbuName("Supply SO 0703");
//			lNotification.add(notification);
//			lNotification.add(notification);
//			when(notificationService.getUnreadNotifications(anyInt())).thenReturn(lNotification);
//			when(mapper.map(any(Notification.class), eq(NotificationDto.class))).thenReturn(new NotificationDto());
//			ResponseEntity<List<NotificationDto>> result = notificationController.getUnreadNotifications(empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getUnreadNotificationsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getUnreadNotificationsValidTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.getUnreadNotificationsValidTest : Finished");
//	}
//	
//	@Test
//	void getUnreadNotificationsValidSecScenarioTest() {
//		logger.info("NotificationControllerTest.getUnreadNotificationsValidSecScenarioTest : Initiated");
//		try {
//			int empId = 1;
//			List<Notification> lNotification = new ArrayList<>();
//			Notification notification = new Notification();
//			notification.setCreatedDateTime(new Date());
//			notification.setReadStatus(false);
//			notification.setSbuName("Supply SO 0703");
//			lNotification.add(notification);
//			lNotification.add(notification);
//			when(notificationService.getUnreadNotifications(anyInt())).thenReturn(lNotification);
//			when(mapper.map(any(Notification.class), eq(NotificationDto.class))).thenReturn(new NotificationDto());
//			ResponseEntity<List<NotificationDto>> result = notificationController.getUnreadNotifications(empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getUnreadNotificationsValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getUnreadNotificationsValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.getUnreadNotificationsValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getNotifByCategoryValidTest() {
//		logger.info("NotificationControllerTest.getNotifByCategoryValidTest : Initiated");
//		try {
//			String category = "ol";
//			int empId = 1;
//			List<Notification> lNotification = new ArrayList<>();
//			Notification notification = new Notification();
//			notification.setCreatedDateTime(new Date());
//			notification.setCategoryId(226);
//			notification.setReadStatus(true);
//			notification.setSbuName("Supply SO 0703");
//			lNotification.add(notification);
//			lNotification.add(notification);
//			when(notificationService.getNotifByCategory(anyString(), anyInt())).thenReturn(lNotification);
//			when(mapper.map(any(Notification.class), eq(NotificationDto.class))).thenReturn(new NotificationDto());
//			when(olService.getSoNumberByOlId(anyInt())).thenReturn("New SO 2");
//			ResponseEntity<List<NotificationDto>> result = notificationController.getNotifByCategory(category, empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getNotifByCategoryValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getNotifByCategoryValidTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.getNotifByCategoryValidTest : Finished");
//	}
//
//	@Test
//	void getNotifByCategoryValidSecScenarioTest() {
//		logger.info("NotificationControllerTest.getNotifByCategoryValidSecScenarioTest : Initiated");
//		try {
//			String category = "ol";
//			int empId = 1;
//			List<Notification> lNotification = new ArrayList<>();
//			Notification notification = new Notification();
//			notification.setCreatedDateTime(new Date());
//			notification.setCategoryId(226);
//			notification.setReadStatus(false);
//			notification.setSbuName("Supply SO 0703");
//			lNotification.add(notification);
//			lNotification.add(notification);
//			when(notificationService.getNotifByCategory(anyString(), anyInt())).thenReturn(lNotification);
//			when(mapper.map(any(Notification.class), eq(NotificationDto.class))).thenReturn(new NotificationDto());
//			when(olService.getSoNumberByOlId(anyInt())).thenReturn("New SO 2");
//			ResponseEntity<List<NotificationDto>> result = notificationController.getNotifByCategory(category, empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getNotifByCategoryValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getNotifByCategoryValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("NotificationControllerTest.getNotifByCategoryValidSecScenarioTest : Finished");
//	}
//	
//}
