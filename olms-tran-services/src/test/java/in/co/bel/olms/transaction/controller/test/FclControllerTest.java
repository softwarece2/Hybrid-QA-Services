package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import in.co.bel.olms.transaction.controller.FclController;
import in.co.bel.olms.transaction.dto.FclDto;
import in.co.bel.olms.transaction.entity.Fcl;
import in.co.bel.olms.transaction.service.so.FclService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class FclControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(FclControllerTest.class);
	
	@InjectMocks
	FclController fclController;
	
	@Mock
	FclService fclService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveFclValidTest() {
		logger.info("FclControllerTest.saveFclValidTest : Initiated");
		assertDoesNotThrow(() -> {
			FclDto fclDto = new FclDto();
			fclDto.setId(1);
			when(fclService.saveFcl(any(FclDto.class))).thenReturn(new Fcl());
			when(fclService.convertBotoDto(any(Fcl.class))).thenReturn(fclDto);
			ResponseEntity<FclDto> result = fclController.saveFcl(fclDto);
			assertNotEquals("", result.toString());
		},"Test failed due to an exception: saveFclValidTest ");
		logger.info("FclControllerTest.saveFclValidTest : Finished");
	}
	
	@Test
	void saveFclDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FclControllerTest.saveFclDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fclService.saveFcl(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(fclService.convertBotoDto(any(Fcl.class))).thenReturn(null);
			ResponseEntity<FclDto> result = fclController.saveFcl(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveFclDataNotValidExceptionTest ");
		logger.info("FclControllerTest.saveFclDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveFclExceptionTest() {
		logger.info("FclControllerTest.saveFclDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fclService.saveFcl(any())).thenThrow(new NullPointerException());
			when(fclService.convertBotoDto(any(Fcl.class))).thenReturn(null);
			ResponseEntity<FclDto> result = fclController.saveFcl(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveFclDataNotValidExceptionTest ");
		logger.info("FclControllerTest.saveFclDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void submitFclValidTest() {
		logger.info("FclControllerTest.submitFclValidTest : Initiated");
		assertDoesNotThrow(() -> {
			FclDto fclDto = new FclDto();
			fclDto.setId(1);
			when(fclService.submitFcl(any(FclDto.class))).thenReturn(new Fcl());
			when(fclService.convertBotoDto(any(Fcl.class))).thenReturn(fclDto);
			ResponseEntity<FclDto> result = fclController.submitFcl(fclDto);
			assertNotEquals("", result.toString());
		}, "Test failed due to an exception: submitFclValidTest ");
		logger.info("FclControllerTest.submitFclValidTest : Finished");
	}
	
	@Test
	void submitFclDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FclControllerTest.submitFclDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fclService.submitFcl(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(fclService.convertBotoDto(any())).thenReturn(null);
			ResponseEntity<FclDto> result = fclController.submitFcl(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFclDataNotValidExceptionTest ");
		logger.info("FclControllerTest.submitFclDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void submitFclExceptionTest() {
		logger.info("FclControllerTest.submitFclExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(fclService.submitFcl(any())).thenThrow(new NullPointerException());
			when(fclService.convertBotoDto(any())).thenReturn(null);
			ResponseEntity<FclDto> result = fclController.submitFcl(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFclExceptionTest ");
		logger.info("FclControllerTest.submitFclExceptionTest : Finished");
	}
	
	@Test
	void getFclBySoIdValidTest() {
		logger.info("FclControllerTest.getFclBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soid = 223;
			when(fclService.getFclBySoId(anyInt())).thenReturn(new FclDto());
			ResponseEntity<FclDto> result = fclController.getFclBySoId(soid);
			assertNotNull(result);
			assertNotEquals("", result.toString());
		}, "Test failed due to an exception: getFclBySoIdValidTest ");
		logger.info("FclControllerTest.getFclBySoIdValidTest : Finished");
	}
	
	@Test
	void getFclBySoIdDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FclControllerTest.getFclBySoIdDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			int soid = 223;
			when(fclService.getFclBySoId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<FclDto> result = fclController.getFclBySoId(soid);
			assertNotNull(result);
		}, "Test failed due to an exception: getFclBySoIdDataNotValidExceptionTest ");
		logger.info("FclControllerTest.getFclBySoIdDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getFclBySoIdExceptionTest() {
		logger.info("FclControllerTest.getFclBySoIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			int soid = 223;
			when(fclService.getFclBySoId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<FclDto> result = fclController.getFclBySoId(soid);
			assertNotNull(result);
		}, "Test failed due to an exception: getFclBySoIdExceptionTest ");
		logger.info("FclControllerTest.getFclBySoIdExceptionTest : Finished");
	}
	
}
