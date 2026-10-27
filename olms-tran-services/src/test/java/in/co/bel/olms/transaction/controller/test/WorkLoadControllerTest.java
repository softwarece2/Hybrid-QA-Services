package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.when;
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
//import in.co.bel.olms.master.exception.DataNotFoundException;
//import in.co.bel.olms.transaction.controller.WorkLoadController;
//import in.co.bel.olms.transaction.dto.WorkLoadDto;
//import in.co.bel.olms.transaction.service.WorkLoadService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class WorkLoadControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(WorkLoadControllerTest.class);
//	
//	@InjectMocks
//	WorkLoadController workLoadController;
//	
//	@Mock
//	WorkLoadService workLoadService;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	private Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void getBySupplyOrderValidTest() {
//		logger.info("WorkLoadControllerTest.getBySupplyOrderValidTest : Initiated");
//		try {
//			int soid = 84;
//			WorkLoadDto workLoad = new WorkLoadDto();
//			workLoad.setCreatedBy(6);
//			workLoad.setId(126);
//			when(workLoadService.getWorkLoadBySoId(anyInt())).thenReturn(workLoad);
//			ResponseEntity<WorkLoadDto> result = workLoadController.getBySupplyOrder(soid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getBySupplyOrderValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getBySupplyOrderValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadControllerTest.getBySupplyOrderValidTest : Finished");
//	}
//	
//	@Test
//	void getBySupplyOrderDataNotFoundExceptionTest() throws DataNotFoundException {
//		logger.info("WorkLoadControllerTest.getBySupplyOrderDataNotFoundExceptionTest : Initiated");
//		try {
//			int soid = 84;
//			WorkLoadDto workLoad = new WorkLoadDto();
//			workLoad.setCreatedBy(6);
//			workLoad.setId(126);
//			when(workLoadService.getWorkLoadBySoId(anyInt())).thenThrow(new DataNotFoundException("Invalid Data"));
//			ResponseEntity<WorkLoadDto> result = workLoadController.getBySupplyOrder(soid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getBySupplyOrderDataNotFoundExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadControllerTest.getBySupplyOrderDataNotFoundExceptionTest : Finished");
//	}
//	
//	@Test
//	void getBySupplyOrderExceptionTest() {
//		logger.info("WorkLoadControllerTest.getBySupplyOrderExceptionTest : Initiated");
//		try {
//			int soid = 84;
//			WorkLoadDto workLoad = new WorkLoadDto();
//			workLoad.setCreatedBy(6);
//			workLoad.setId(126);
//			when(workLoadService.getWorkLoadBySoId(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<WorkLoadDto> result = workLoadController.getBySupplyOrder(soid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getBySupplyOrderExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadControllerTest.getBySupplyOrderExceptionTest : Finished");
//	}	
//
//}
