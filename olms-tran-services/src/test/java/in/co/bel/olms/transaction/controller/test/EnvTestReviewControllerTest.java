package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Date;

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
import in.co.bel.olms.transaction.controller.EnvTestReviewController;
import in.co.bel.olms.transaction.dto.EnvTestReviewDto;
import in.co.bel.olms.transaction.entity.EnvTestReview;
import in.co.bel.olms.transaction.service.EnvTestReviewService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class EnvTestReviewControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(EnvTestReviewControllerTest.class);
	
	@InjectMocks
	EnvTestReviewController envTestReviewController;
	
	@Mock
	EnvTestReviewService envTestReviewService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getEnvTestReviewByIdValidTest() {
		logger.info("EnvTestReviewControllerTest.getEnvTestReviewByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			EnvTestReview envTestReview = new EnvTestReview();
			envTestReview.setDateOfInsp(new Date());
			envTestReview.setId(1);
			when(envTestReviewService.getEnvTestReviewById(anyInt())).thenReturn(envTestReview);
			when(mapper.map(any(EnvTestReview.class), eq(EnvTestReviewDto.class))).thenReturn(new EnvTestReviewDto());
			ResponseEntity<EnvTestReviewDto> result = envTestReviewController.getEnvTestReviewById(id);
			assertNotNull(result);
			assertNotEquals("", result.toString());
		}, "Test failed due to an exception: getEnvTestReviewByIdValidTest ");
		logger.info("EnvTestReviewControllerTest.getEnvTestReviewByIdValidTest : Finished");
	}
	
	@Test
	void saveEnvTestReviewValidTest() {
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewValidTest : Initiated");
		assertDoesNotThrow(() -> {
			EnvTestReview envTestRev = new EnvTestReview();
			envTestRev.setId(1);
			envTestRev.setDescription("desc1");
			when(envTestReviewService.saveEnvTestReview(any(EnvTestReviewDto.class))).thenReturn(envTestRev);
			ResponseEntity<String> result = envTestReviewController.saveEnvTestReview(new EnvTestReviewDto());
			assertNotNull(result);
			assertEquals("<201 CREATED Created,success,[]>", result.toString());
		}, "Test failed due to an exception: saveEnvTestReviewValidTest ");
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewValidTest : Finished");
	}
	
	@Test
	void saveEnvTestReviewDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(envTestReviewService.saveEnvTestReview(any())).thenThrow(new DataNotValidException("Invalid"));
			ResponseEntity<String> result = envTestReviewController.saveEnvTestReview(null);
			assertNotNull(result);
		},"Test failed due to an exception: saveEnvTestReviewDataNotValidExceptionTest ");
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveEnvTestReviewExceptionTest() {
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(envTestReviewService.saveEnvTestReview(any())).thenThrow(new NullPointerException());
			ResponseEntity<String> result = envTestReviewController.saveEnvTestReview(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveEnvTestReviewExceptionTest ");
		logger.info("EnvTestReviewControllerTest.saveEnvTestReviewExceptionTest : Finished");
	}
}
