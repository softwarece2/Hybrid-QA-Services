package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

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
import in.co.bel.olms.transaction.controller.SurpriseCheckController;
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.service.SurpriseCheckService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SurpriseCheckControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SurpriseCheckControllerTest.class);
	
	@InjectMocks
	SurpriseCheckController surpriseCheckController;
	
	@Mock
	SurpriseCheckService surpriseCheckService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveSurpriseCheckValidTest() {
		logger.info("SurpriseCheckControllerTest.saveSurpriseCheckValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> response = new HashMap<>();
			SurpriseCheckDto surpriseCheckDto = new SurpriseCheckDto();
			surpriseCheckDto.setId(12);
			String resp = gson.toJson(surpriseCheckDto);
			response.put(true, resp);
			when(surpriseCheckService.saveSurpriseCheck(any(SurpriseCheckDto.class))).thenReturn(response);
			ResponseEntity<String> result = surpriseCheckController.saveSurpriseCheck(surpriseCheckDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveSurpriseCheckValidTest ");
		logger.info("SurpriseCheckControllerTest.saveSurpriseCheckValidTest : Finished");
	}
	
	@Test
	void saveSurpriseCheckSecScenarioTest() {
		logger.info("SurpriseCheckControllerTest.saveSurpriseCheckSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> response = new HashMap<>();
			SurpriseCheckDto surpriseCheckDto = new SurpriseCheckDto();
			surpriseCheckDto.setId(12);
			response.put(false, "Invalid Data");
			when(surpriseCheckService.saveSurpriseCheck(any(SurpriseCheckDto.class))).thenReturn(response);
			ResponseEntity<String> result = surpriseCheckController.saveSurpriseCheck(surpriseCheckDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveSurpriseCheckSecScenarioTest ");
		logger.info("SurpriseCheckControllerTest.saveSurpriseCheckSecScenarioTest : Finished");
	}
	
}
