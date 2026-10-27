package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import in.co.bel.olms.transaction.controller.SoApprovalDetailsController;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.service.so.SoApprovalDetailsService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SoApprovalDetailsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SoApprovalDetailsControllerTest.class);
	
	@InjectMocks
	SoApprovalDetailsController soApprovalDetailsController;
	
	@Mock
	SoApprovalDetailsService soApprovalService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void updateSoApprovalValidTest() {
		logger.info("SoApprovalDetailsControllerTest.updateSoApprovalValidTest : Initiated");
		assertDoesNotThrow(() -> {
			OlApprovalDto soApprovalDto = new OlApprovalDto();
			when(soApprovalService.updateSoApproval(any(OlApprovalDto.class),null)).thenReturn(true);
			ResponseEntity<Boolean> res = soApprovalDetailsController.updateSoApproval(soApprovalDto.toString(),null);
			assertNotNull(res);
		}, "Test failed due to an exception: updateSoApprovalValidTest ");
		logger.info("SoApprovalDetailsControllerTest.updateSoApprovalValidTest : Finished");
	}
	
	@Test
	void updateSoApprovalSecScenarioTest() {
		logger.info("SoApprovalDetailsControllerTest.updateSoApprovalSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			OlApprovalDto soApprovalDto = new OlApprovalDto();
			when(soApprovalService.updateSoApproval(any(OlApprovalDto.class), null)).thenReturn(false);
			ResponseEntity<Boolean> res = soApprovalDetailsController.updateSoApproval(soApprovalDto.toString(),null);
			assertNotNull(res);
		}, "Test failed due to an exception: updateSoApprovalSecScenarioTest ");
		logger.info("SoApprovalDetailsControllerTest.updateSoApprovalSecScenarioTest : Finished");
	}
	
	@Test
	void getOlApprovalDetailsBySoIdValidTest() {
		logger.info("SoApprovalDetailsControllerTest.getOlApprovalDetailsBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			int empId = 101;
			when(soApprovalService.getSoApprovalDetailsBySoId(anyInt(), anyInt())).thenReturn(new SoApprovalDto());
			ResponseEntity<SoApprovalDto> res = soApprovalDetailsController.getOlApprovalDetailsBySoId(soId, empId);
			assertNotNull(res);
		}, "Test failed due to an exception: getOlApprovalDetailsBySoIdValidTest ");
		logger.info("SoApprovalDetailsControllerTest.getOlApprovalDetailsBySoIdValidTest : Finished");
	}

}
