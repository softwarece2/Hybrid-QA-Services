package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.eq;
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
//import in.co.bel.olms.master.exception.DataNotValidException;
//import in.co.bel.olms.transaction.controller.WorkLoadSoController;
//import in.co.bel.olms.transaction.dto.WorkLoadDto;
//import in.co.bel.olms.transaction.entity.WorkLoad;
//import in.co.bel.olms.transaction.service.WorkLoadSoService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class WorkLoadSoControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(WorkLoadSoControllerTest.class);
//	
//	@InjectMocks
//	WorkLoadSoController workLoadSoController;
//	
//	@Mock
//	WorkLoadSoService workLoadService;
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
//	void getWorkLoadByIDValidTest() {
//		logger.info("WorkLoadSoControllerTest.getWorkLoadByIDValidTest : Initiated");
//		try {
//			int id = 0;
//			when(workLoadService.getWorkLoadById(anyInt())).thenReturn(new WorkLoad());
//			when(mapper.map(any(WorkLoad.class), eq(WorkLoadDto.class))).thenReturn(new WorkLoadDto());
//			ResponseEntity<WorkLoadDto> result = workLoadSoController.getWorkLoadByID(id);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getWorkLoadByIDValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getWorkLoadByIDValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.getWorkLoadByIDValidTest : Finished");
//	}
//	
//	@Test
//	void getBySupplyOrderValidTest() {
//		logger.info("WorkLoadSoControllerTest.getBySupplyOrderValidTest : Initiated");
//		try {
//			int soid = 84;
//			when(workLoadService.getWorkLoadBySoId(anyInt())).thenReturn(new WorkLoadDto());
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.getBySupplyOrder(soid);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getBySupplyOrderValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getBySupplyOrderValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.getBySupplyOrderValidTest : Finished");
//	}
//	
//	@Test
//	void savePlanValidTest() {
//		logger.info("WorkLoadSoControllerTest.savePlanValidTest : Initiated");
//		try {
//			WorkLoadDto workLoadDto = new WorkLoadDto();
//			workLoadDto.setId(12);
//			when(workLoadService.savePlan(any(WorkLoadDto.class))).thenReturn(new WorkLoad());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.savePlan(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanValidTest " + e.getMessage());
//			fail("Test failed due to an exception: savePlanValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.savePlanValidTest : Finished");
//	}
//	
//	@Test
//	void savePlanDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadSoControllerTest.savePlanDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadService.savePlan(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.savePlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.savePlanValidsavePlanDataNotValidExceptionTestTest : Finished");
//	}
//	
//	@Test
//	void savePlanExceptionTest() {
//		logger.info("WorkLoadSoControllerTest.savePlanExceptionTest : Initiated");
//		try {
//			when(workLoadService.savePlan(any())).thenThrow(new NullPointerException());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.savePlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.savePlanExceptionTest : Finished");
//	}
//	
//	
//	@Test
//	void submitPlanValidTest() {
//		logger.info("WorkLoadSoControllerTest.submitPlanValidTest : Initiated");
//		try {
//			WorkLoadDto workLoadDto = new WorkLoadDto();
//			workLoadDto.setId(12);
//			when(workLoadService.submitPlan(any(WorkLoadDto.class))).thenReturn(new WorkLoad());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitPlan(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitPlanValidTest " + e.getMessage());
//			fail("Test failed due to an exception: submitPlanValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.submitPlanValidTest : Finished");
//	}
//	
//	@Test
//	void submitPlanDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadSoControllerTest.submitPlanDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadService.submitPlan(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitPlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitPlanDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.submitPlanDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitPlanExceptionTest() {
//		logger.info("WorkLoadSoControllerTest.savePlanExceptionTest : Initiated");
//		try {
//			when(workLoadService.submitPlan(any())).thenThrow(new NullPointerException());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitPlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.savePlanExceptionTest : Finished");
//	}
//	
//	@Test
//	void saveActualValidTest() {
//		logger.info("WorkLoadSoControllerTest.saveActualValidTest : Initiated");
//		try {
//			WorkLoadDto workLoadDto = new WorkLoadDto();
//			workLoadDto.setId(12);
//			when(workLoadService.saveAct(any(WorkLoadDto.class))).thenReturn(new WorkLoad());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.saveActual(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualValidTest " + e.getMessage());
//			fail("Test failed due to an exception: saveActualValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.saveActualValidTest : Finished");
//	}
//	
//	@Test
//	void saveActualDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadSoControllerTest.saveActualDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadService.saveAct(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.saveActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.saveActualDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void saveActualExceptionTest() {
//		logger.info("WorkLoadSoControllerTest.saveActualExceptionTest : Initiated");
//		try {
//			when(workLoadService.saveAct(any())).thenThrow(new NullPointerException());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.saveActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.saveActualExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitActualValidTest() {
//		logger.info("WorkLoadSoControllerTest.submitActualValidTest : Initiated");
//		try {
//			WorkLoadDto workLoadDto = new WorkLoadDto();
//			workLoadDto.setId(12);
//			when(workLoadService.submitAct(any(WorkLoadDto.class))).thenReturn(new WorkLoad());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitActual(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActualValidTest " + e.getMessage());
//			fail("Test failed due to an exception: submitActualValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.submitActualValidTest : Finished");
//	}
//	
//	@Test
//	void submitActualDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadSoControllerTest.submitActualDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadService.submitAct(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActualDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.submitActualDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitActualExceptionTest() {
//		logger.info("WorkLoadSoControllerTest.submitActualExceptionTest : Initiated");
//		try {
//			when(workLoadService.submitAct(any())).thenThrow(new NullPointerException());
//			when(workLoadService.convertBoToDto(any(WorkLoad.class))).thenReturn(null);
//			ResponseEntity<WorkLoadDto> res = workLoadSoController.submitActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActualExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadSoControllerTest.submitActualExceptionTest : Finished");
//	}
//	
//}
