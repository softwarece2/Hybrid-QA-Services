package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
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
import in.co.bel.olms.transaction.controller.CleranceLetterController;
import in.co.bel.olms.transaction.dto.ClearanceLetterDto;
import in.co.bel.olms.transaction.repository.ClearanceLetterRepository;
import in.co.bel.olms.transaction.service.ClearanceLetterService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class CleranceLetterControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(CleranceLetterControllerTest.class);
	
	@InjectMocks
	CleranceLetterController cleranceLetterController;
	
	@Mock
	ClearanceLetterService clearanceLetterService;
	
	@Mock
	ClearanceLetterRepository clearanceLetterRepo;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveValidTest() {
		logger.info("CleranceLetterControllerTest.saveValidTest : Initiated");
		assertDoesNotThrow(() -> {
			ClearanceLetterDto clearanceLetterDto = new ClearanceLetterDto();
			Map<Boolean, String> savedStatus = new HashMap<>();
			clearanceLetterDto.setId(12);
			String resp = gson.toJson(clearanceLetterDto);
			savedStatus.put(true, resp);
			when(clearanceLetterService.save(clearanceLetterDto)).thenReturn(savedStatus);
			ResponseEntity<String> result = cleranceLetterController.save(clearanceLetterDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveValidTest ");
		logger.info("CleranceLetterControllerTest.saveValidTest : Finished");
	}

	@Test
	void saveValidSecScenarioTest() {
		logger.info("CleranceLetterControllerTest.saveValidSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			ClearanceLetterDto clearanceLetterDto = new ClearanceLetterDto();
			Map<Boolean, String> savedStatus = new HashMap<>();
			savedStatus.put(false, "false Value");
			when(clearanceLetterService.save(clearanceLetterDto)).thenReturn(savedStatus);
			ResponseEntity<String> result = cleranceLetterController.save(clearanceLetterDto);
			assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,false Value,[]>", result.toString());
		}, "Test failed due to an exception: saveValidSecScenarioTest ");	
		logger.info("CleranceLetterControllerTest.saveValidSecScenarioTest : Finished");
	}
	
	@Test
	void getAddressDetailsValidTest() {
		logger.info("CleranceLetterControllerTest.getAddressDetailsValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			ClearanceLetterDto clearanceLetterDto = new ClearanceLetterDto();
			clearanceLetterDto.setId(11);
			clearanceLetterDto.setCqaeName("BEL Bg Cx");
			when(clearanceLetterService.getAddress(anyInt())).thenReturn(clearanceLetterDto);
			ResponseEntity<ClearanceLetterDto> result = cleranceLetterController.getAddressDetails(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getAddressDetailsValidTest ");
		logger.info("CleranceLetterControllerTest.getAddressDetailsValidTest : Finished");
	}
	
	@Test
	void getClearanceLetterValidTest() {
		logger.info("CleranceLetterControllerTest.getClearanceLetterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			ClearanceLetterDto clearanceLetterDto = new ClearanceLetterDto();
			clearanceLetterDto.setId(11);
			clearanceLetterDto.setCqaeName("BEL Bg Cx");
			when(clearanceLetterService.getClearanceLetter(anyInt())).thenReturn(clearanceLetterDto);
			ResponseEntity<ClearanceLetterDto> result = cleranceLetterController.getClearanceLetter(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getClearanceLetterValidTest ");
		logger.info("CleranceLetterControllerTest.getClearanceLetterValidTest : Finished");
	}
			
}
