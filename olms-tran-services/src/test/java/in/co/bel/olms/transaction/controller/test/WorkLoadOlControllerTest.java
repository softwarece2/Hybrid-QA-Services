package in.co.bel.olms.transaction.controller.test;

//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
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
//import in.co.bel.olms.master.exception.DataNotValidException;
//import in.co.bel.olms.transaction.controller.WorkLoadOlController;
//import in.co.bel.olms.transaction.dto.WorkLoadOlDto;
//import in.co.bel.olms.transaction.entity.WorkLoadOl;
//import in.co.bel.olms.transaction.service.WorkLoadServiceOl;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class WorkLoadOlControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(WorkLoadOlControllerTest.class);
//	
//	@InjectMocks
//	WorkLoadOlController workLoadOlController;
//	
//	@Mock
//	WorkLoadServiceOl workLoadOlService;
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
//		logger.info("WorkLoadOlControllerTest.getWorkLoadByIDValidTest : Initiated");
//		try {
//			int id = 12;
//			when(workLoadOlService.getWorkLoadById(anyInt())).thenReturn(new WorkLoadOl());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(new WorkLoadOlDto());
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.getWorkLoadByID(id);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getWorkLoadByIDValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getWorkLoadByIDValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.getWorkLoadByIDValidTest : Finished");
//	}
//	
//	@Test
//	void getByOfferLetterValidTest() {
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterValidTest : Initiated");
//		try {
//			int olid = 84;
//			when(workLoadOlService.getWorkLoadByOlId(anyInt())).thenReturn(new WorkLoadOlDto());
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.getByOfferLetter(olid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getByOfferLetterValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getByOfferLetterValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterValidTest : Finished");
//	}
//	
//	@Test
//	void getByOfferLetterDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterDataNotValidExceptionTest : Initiated");
//		try {
//			int olid = 84;
//			when(workLoadOlService.getWorkLoadByOlId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.getByOfferLetter(olid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getByOfferLetterDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void getByOfferLetterExceptionTest() {
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterExceptionTest : Initiated");
//		try {
//			int olid = 84;
//			when(workLoadOlService.getWorkLoadByOlId(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.getByOfferLetter(olid);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getByOfferLetterExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.getByOfferLetterExceptionTest : Finished");
//	}
//	
//	@Test
//	void savePlanValidTest() {
//		logger.info("WorkLoadOlControllerTest.savePlanValidTest : Initiated");
//		try {
//			WorkLoadOlDto workLoadDto = new WorkLoadOlDto();
//			workLoadDto.setId(12);
//			when(workLoadOlService.savePlan(any(WorkLoadOlDto.class))).thenReturn(new WorkLoadOl());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.savePlan(workLoadDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanValidTest " + e.getMessage());
//			fail("Test failed due to an exception: savePlanValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.savePlanValidTest : Finished");
//	}
//	
//	@Test
//	void savePlanDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadOlControllerTest.savePlanDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.savePlan(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.savePlan(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.savePlanDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void savePlanExceptionTest() {
//		logger.info("WorkLoadOlControllerTest.savePlanExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.savePlan(any())).thenThrow(new NullPointerException());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.savePlan(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: savePlanExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.savePlanExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitPlanValidTest() {
//		logger.info("WorkLoadOlControllerTest.submitPlanValidTest : Initiated");
//		try {
//			WorkLoadOlDto workLoadDto = new WorkLoadOlDto();
//			workLoadDto.setId(12);
//			when(workLoadOlService.submitPlan(any(WorkLoadOlDto.class))).thenReturn(new WorkLoadOl());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.submitPlan(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitPlanValidTest " + e.getMessage());
//			fail("Test failed due to an exception: submitPlanValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitPlanValidTest : Finished");
//	}
//	
//	@Test
//	void submitPlanDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadOlControllerTest.submitPlanDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.submitPlan(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.submitPlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitPlanDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitPlanDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitPlanExceptionTest() {
//		logger.info("WorkLoadOlControllerTest.submitPlanExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.submitPlan(any())).thenThrow(new NullPointerException());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.submitPlan(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitPlanExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitPlanExceptionTest : Finished");
//	}
//	
//	@Test
//	void saveActualValidTest() {
//		logger.info("WorkLoadOlControllerTest.saveActualValidTest : Initiated");
//		try {
//			WorkLoadOlDto workLoadDto = new WorkLoadOlDto();
//			workLoadDto.setId(12);
//			when(workLoadOlService.saveAct(any(WorkLoadOlDto.class))).thenReturn(new WorkLoadOl());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.saveActual(workLoadDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualValidTest " + e.getMessage());
//			fail("Test failed due to an exception: saveActualValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.saveActualValidTest : Finished");
//	}
//	
//	@Test
//	void saveActualDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadOlControllerTest.saveActualDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.saveAct(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.saveActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.saveActualDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void saveActualExceptionTest() {
//		logger.info("WorkLoadOlControllerTest.saveActualExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.saveAct(any())).thenThrow(new NullPointerException());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> res = workLoadOlController.saveActual(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveActualExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.saveActualExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitActValidTest() {
//		logger.info("WorkLoadOlControllerTest.submitActValidTest : Initiated");
//		try {
//			WorkLoadOlDto workLoadDto = new WorkLoadOlDto();
//			workLoadDto.setId(12);
//			when(workLoadOlService.submitAct(any(WorkLoadOlDto.class))).thenReturn(new WorkLoadOl());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(workLoadDto);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.submitAct(workLoadDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActValidTest " + e.getMessage());
//			fail("Test failed due to an exception: submitActValidTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitActValidTest : Finished");
//	}
//	
//	@Test
//	void submitActDataNotValidExceptionTest() throws DataNotValidException {
//		logger.info("WorkLoadOlControllerTest.submitActDataNotValidExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.submitAct(any())).thenThrow(new DataNotValidException("Invalid Data"));
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.submitAct(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActDataNotValidExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitActDataNotValidExceptionTest : Finished");
//	}
//	
//	@Test
//	void submitActExceptionTest() {
//		logger.info("WorkLoadOlControllerTest.submitActExceptionTest : Initiated");
//		try {
//			when(workLoadOlService.submitAct(any())).thenThrow(new NullPointerException());
//			when(workLoadOlService.convertBoToDto(any(WorkLoadOl.class))).thenReturn(null);
//			ResponseEntity<WorkLoadOlDto> result = workLoadOlController.submitAct(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: submitActExceptionTest " + e.getMessage());
//		}
//		logger.info("WorkLoadOlControllerTest.submitActExceptionTest : Finished");
//	}	
//	
//}
