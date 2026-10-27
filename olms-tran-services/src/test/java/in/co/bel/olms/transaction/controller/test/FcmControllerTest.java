package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.controller.FcmController;
import in.co.bel.olms.transaction.dto.FcmDto;
import in.co.bel.olms.transaction.entity.Fcm;
import in.co.bel.olms.transaction.service.so.FcmService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class FcmControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(FcmControllerTest.class);
	
	@InjectMocks
	FcmController fcmController;
	
	@Mock
	FcmService fcmService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveFcmValidTest() {
		logger.info("FcmControllerTest.saveFcmValidTest : Initiated");
		assertDoesNotThrow(() -> {
			FcmDto fcmdto = new FcmDto();
			fcmdto.setId(1);
			when(fcmService.saveFcm(any(FcmDto.class))).thenReturn(new Fcm());
			when(fcmService.convertBoToDto(any(Fcm.class))).thenReturn(fcmdto);
			ResponseEntity<FcmDto> result = fcmController.saveFcm(fcmdto);
			assertNotEquals(null, result.toString());
		}, "Test failed due to an exception: saveFcmValidTest");
		logger.info("FcmControllerTest.saveFcmValidTest : Finished");
	}
	
	@Test
	void saveFcmDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FcmControllerTest.saveFcmDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.saveFcm(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(fcmService.convertBoToDto(any())).thenReturn(null);
			ResponseEntity<FcmDto> result = fcmController.saveFcm(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveFcmDataNotValidExceptionTest ");
		logger.info("FcmControllerTest.saveFcmDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveFcmExceptionTest() {
		logger.info("FcmControllerTest.saveFcmExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.saveFcm(any())).thenThrow(new NullPointerException());
			when(fcmService.convertBoToDto(any())).thenReturn(null);
			ResponseEntity<FcmDto> result = fcmController.saveFcm(null);
			assertNotNull(result);
		},"Test failed due to an exception: saveFcmExceptionTest ");
		logger.info("FcmControllerTest.saveFcmExceptionTest : Finished");
	}
	
	
	@Test
	void getFcmByIdValidTest() {
		logger.info("FcmControllerTest.getFcmByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 65;
			FcmDto fcmDto = new FcmDto();
			fcmDto.setId(id);
			when(fcmService.getFcmById(anyInt())).thenReturn(new Fcm());
			when(mapper.map(any(Fcm.class), eq(FcmDto.class))).thenReturn(fcmDto);
			ResponseEntity<FcmDto> result = fcmController.getFcmById(id);
			assertNotNull(result);
		},"Test failed due to an exception: getFcmByIdValidTest ");
		logger.info("FcmControllerTest.getFcmByIdValidTest : Finished");
	}
	
	@Test
	void submitFcmValidTest() {
		logger.info("FcmControllerTest.submitFcmValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 65;
			FcmDto fcmDto = new FcmDto();
			fcmDto.setId(id);
			when(fcmService.submitFcm(any(FcmDto.class))).thenReturn(new Fcm());
			when(fcmService.convertBoToDto(any(Fcm.class))).thenReturn(fcmDto);
			ResponseEntity<FcmDto> result = fcmController.submitFcm(fcmDto);
			assertNotEquals("", result.toString());
		}, "Test failed due to an exception: submitFcmValidTest ");
		logger.info("FcmControllerTest.submitFcmValidTest : Finished");
	}
	
	@Test
	void submitFcmDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FcmControllerTest.submitFcmDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.submitFcm(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(fcmService.convertBoToDto(any())).thenReturn(null);
			ResponseEntity<FcmDto> result = fcmController.submitFcm(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFcmDataNotValidExceptionTest ");
		logger.info("FcmControllerTest.submitFcmDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void submitFcmExceptionTest() {
		logger.info("FcmControllerTest.submitFcmExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.submitFcm(any())).thenThrow(new NullPointerException());
			when(fcmService.convertBoToDto(any())).thenReturn(null);
			ResponseEntity<FcmDto> result = fcmController.submitFcm(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFcmExceptionTest ");
		logger.info("FcmControllerTest.submitFcmExceptionTest : Finished");
	}
	
	@Test
	void getFcmBySoIdValidTest() {
		logger.info("FcmControllerTest.getFcmBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soid = 234;
			FcmDto fcm = new FcmDto();
			fcm.setId(soid);
			when(fcmService.getFcmBySoId(anyInt())).thenReturn(fcm);
			ResponseEntity<FcmDto> result = fcmController.getFcmBySoId(soid);
			assertNotNull(result);
		}, "Test failed due to an exception: getFcmBySoIdValidTest ");
		logger.info("FcmControllerTest.getFcmBySoIdValidTest : Finished");
	}
	
	@Test
	void getFcmBySoIdDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FcmControllerTest.getFcmBySoIdDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.getFcmBySoId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<FcmDto> result = fcmController.getFcmBySoId(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getFcmBySoIdDataNotValidExceptionTest ");
		logger.info("FcmControllerTest.getFcmBySoIdDataNotValidExceptionTest : Finished");
	}

	@Test
	void getFcmBySoIdExceptionTest() {
		logger.info("FcmControllerTest.getFcmBySoIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fcmService.getFcmBySoId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<FcmDto> result = fcmController.getFcmBySoId(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getFcmBySoIdExceptionTest ");
		logger.info("FcmControllerTest.getFcmBySoIdExceptionTest : Finished");
	}
}
