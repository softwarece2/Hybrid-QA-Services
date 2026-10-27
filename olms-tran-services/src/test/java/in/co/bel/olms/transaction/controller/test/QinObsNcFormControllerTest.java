package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import in.co.bel.olms.transaction.controller.QinObsNcFormController;
import in.co.bel.olms.transaction.dto.QinObsNcFormDto;
import in.co.bel.olms.transaction.service.QinObsNcFormService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class QinObsNcFormControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(QinObsNcFormControllerTest.class);
	
	@InjectMocks
	QinObsNcFormController qinObsNcFormController;
	
	@Mock
	QinObsNcFormService qinObsNcFormService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveQinObsNcFormValidTest() {
		logger.info("QinObsNcFormControllerTest.saveQinObsNcFormValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> respMap = new HashMap<>();
			QinObsNcFormDto qinObsNcFormDto = new QinObsNcFormDto();
			qinObsNcFormDto.setId(105);
			String resp = gson.toJson(qinObsNcFormDto);
			respMap.put(true, resp);
			when(qinObsNcFormService.saveQinObsNcForm(any(QinObsNcFormDto.class))).thenReturn(respMap);
			ResponseEntity<String> result = qinObsNcFormController.saveQinObsNcForm(qinObsNcFormDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveQinObsNcFormValidTest ");
		logger.info("QinObsNcFormControllerTest.saveQinObsNcFormValidTest : Finished");
	}
	
	@Test
	void saveQinObsNcFormSecScenarioTest() {
		logger.info("QinObsNcFormControllerTest.saveQinObsNcFormSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> respMap = new HashMap<>();
			respMap.put(false, "Internal Server Failure");
			when(qinObsNcFormService.saveQinObsNcForm(any(QinObsNcFormDto.class))).thenReturn(respMap);
			ResponseEntity<String> result = qinObsNcFormController.saveQinObsNcForm(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveQinObsNcFormSecScenarioTest ");
		logger.info("QinObsNcFormControllerTest.saveQinObsNcFormSecScenarioTest : Finished");
	}
	
	@Test
	void updateQinObsNcFormActionValidTest() {
		logger.info("QinObsNcFormControllerTest.updateQinObsNcFormActionValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> respMap = new HashMap<>();
			QinObsNcFormDto qinObsNcFormDto = new QinObsNcFormDto();
			qinObsNcFormDto.setId(105);
			String resp = gson.toJson(qinObsNcFormDto);
			respMap.put(true, resp);
			when(qinObsNcFormService.updateQinObsNcFormAction(any(QinObsNcFormDto.class))).thenReturn(respMap);
			ResponseEntity<String> result = qinObsNcFormController.updateQinObsNcFormAction(qinObsNcFormDto);
			assertNotNull(result);
		}, "Test failed due to an exception: updateQinObsNcFormActionValidTest ");
		logger.info("QinObsNcFormControllerTest.updateQinObsNcFormActionValidTest : Finished");
	}
	
	@Test
	void updateQinObsNcFormActionSecScenarioTest() {
		logger.info("QinObsNcFormControllerTest.updateQinObsNcFormActionSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> respMap = new HashMap<>();
			respMap.put(false, "Internal Server Failure");
			when(qinObsNcFormService.updateQinObsNcFormAction(any(QinObsNcFormDto.class))).thenReturn(respMap);
			ResponseEntity<String> result = qinObsNcFormController.updateQinObsNcFormAction(null);
			assertNotNull(result);
		}, "Test failed due to an exception: updateQinObsNcFormActionSecScenarioTest ");
		logger.info("QinObsNcFormControllerTest.updateQinObsNcFormActionSecScenarioTest : Finished");
	}
	
	@Test
	void getQinNcFormDetailsValidTest() {
		logger.info("QinObsNcFormControllerTest.getQinNcFormDetailsValidTest : Initiated");
		assertDoesNotThrow(() -> {
			when(qinObsNcFormService.getQinNcFormDetails(anyInt())).thenReturn(new QinObsNcFormDto());
			ResponseEntity<QinObsNcFormDto> result = qinObsNcFormController.getQinNcFormDetails(1);
			assertNotNull(result);
		}, "Test failed due to an exception: getQinNcFormDetailsValidTest ");
		logger.info("QinObsNcFormControllerTest.getQinNcFormDetailsValidTest : Finished");
	}

}
