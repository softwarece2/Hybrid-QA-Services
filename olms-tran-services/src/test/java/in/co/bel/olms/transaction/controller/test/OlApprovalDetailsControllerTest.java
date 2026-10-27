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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.transaction.controller.OlApprovalDetailsController;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.service.ol.OlApprovalDetailsService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class OlApprovalDetailsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(OlApprovalDetailsControllerTest.class);
	
	@InjectMocks
	OlApprovalDetailsController olApprovalDetailsController;
	
	@Mock
	OlApprovalDetailsService olApprovalService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void updateOlApprovalValidTest() {
		logger.info("OlApprovalDetailsControllerTest.updateOlApprovalValidTest : Initiated");
		assertDoesNotThrow(() -> {
			byte[] fileContent = "File 1 Content".getBytes();
		        MockMultipartFile file1 = new MockMultipartFile(
		                "file",
		                "file2.txt",
		                "text/plain",
		                fileContent
		        );
		        MultipartFile[] multipartFiles = {file1, file1};
		        OlApprovalDto olApprovalDto = new OlApprovalDto();
		        olApprovalDto.setId(1);
		        String resp = gson.toJson(olApprovalDto);
		        when(olApprovalService.updateOlApproval(any(OlApprovalDto.class), any(MultipartFile[].class))).thenReturn(olApprovalDto);
		        ResponseEntity<OlApprovalDto> result = olApprovalDetailsController.updateOlApproval(resp, multipartFiles);
		        assertNotNull(result);
		}, "Test failed due to an exception: updateOlApprovalValidTest ");
		logger.info("OlApprovalDetailsControllerTest.updateOlApprovalValidTest : Finished");
	}
	
	@Test
	void updateOlApprovalSecScenarioTest() {
		logger.info("OlApprovalDetailsControllerTest.updateOlApprovalSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			byte[] fileContent = "File 1 Content".getBytes();
		        MockMultipartFile file1 = new MockMultipartFile(
		                "file",
		                "file2.txt",
		                "text/plain",
		                fileContent
		        );
		        MultipartFile[] multipartFiles = {file1, file1};
		        OlApprovalDto olApprovalDto = new OlApprovalDto();
		        olApprovalDto.setId(1);
		        String resp = gson.toJson(olApprovalDto);
		        when(olApprovalService.updateOlApproval(any(OlApprovalDto.class), any(MultipartFile[].class))).thenReturn(null);
		        ResponseEntity<OlApprovalDto> result = olApprovalDetailsController.updateOlApproval(resp, multipartFiles);
		        assertNotNull(result);
		}, "Test failed due to an exception: updateOlApprovalSecScenarioTest ");
		logger.info("OlApprovalDetailsControllerTest.updateOlApprovalSecScenarioTest : Finished");
	}
	
	@Test
	void getOlApprovalDetailsByOlIdValidTest() {
		logger.info("OlApprovalDetailsControllerTest.getOlApprovalDetailsByOlIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			SoApprovalDto soApprovalDto = new SoApprovalDto();
			soApprovalDto.setIsEditAllowed(true);
			when(olApprovalService.getOlApprovalDetailsByOlId(anyInt(), anyInt())).thenReturn(soApprovalDto);
			ResponseEntity<SoApprovalDto> res = olApprovalDetailsController.getOlApprovalDetailsByOlId(84, 12);
			assertNotNull(res);
		}, "Test failed due to an exception: getOlApprovalDetailsByOlIdValidTest ");
		logger.info("OlApprovalDetailsControllerTest.getOlApprovalDetailsByOlIdValidTest : Finished");
	}
	
	
}
