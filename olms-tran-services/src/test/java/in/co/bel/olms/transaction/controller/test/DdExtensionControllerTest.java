package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import in.co.bel.olms.transaction.controller.DdExtensionController;
import in.co.bel.olms.transaction.dto.DdExtensionDto;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.service.DdExtensionService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class DdExtensionControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(DdExtensionControllerTest.class);
	
	@InjectMocks
	DdExtensionController ddExtensionController;
	
	@Mock
	DdExtensionService ddExtensionService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getDdExtensionOfSoValidTest() {
		logger.info("DdExtensionControllerTest.getDdExtensionOfSoValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<DdExtension> ddExtensions = new ArrayList<>();
			DdExtension ddExtension = new DdExtension();
			ddExtension.setId(1);
			ddExtension.setRemarks("rem1");
			ddExtensions.add(ddExtension);
			ddExtensions.add(ddExtension);
			when(ddExtensionService.getDdExtensionOfSo()).thenReturn(ddExtensions);
			when(mapper.map(any(DdExtension.class), eq(DdExtensionDto.class))).thenReturn(new DdExtensionDto());
			ResponseEntity<List<DdExtensionDto>> result = ddExtensionController.getDdExtensionOfSo();
			assertNotNull(result);
			assertNotEquals("", result.toString());
		}, "Test failed due to an exception: getDdExtensionOfSoValidTest ");
		logger.info("DdExtensionControllerTest.getDdExtensionOfSoValidTest : Finished");
	}
	
	@Test
	void getByIdValidTest() {
		logger.info("DdExtensionControllerTest.getByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			when(ddExtensionService.getById(anyInt())).thenReturn(new DdExtension());
			when(mapper.map(any(DdExtension.class), eq(DdExtensionDto.class))).thenReturn(new DdExtensionDto());
			ResponseEntity<DdExtensionDto> result = ddExtensionController.getById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getByIdValidTest " );
		logger.info("DdExtensionControllerTest.getByIdValidTest : Finished");
	}
	
	@Test
	void getBySoIdValidTest() {
		logger.info("DdExtensionControllerTest.getBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 223;
			List<DdExtensionDto> lDdExtension = new ArrayList<>();
			DdExtensionDto ddExtension = new DdExtensionDto();
			ddExtension.setId(soId);
			ddExtension.setLetterDate(new Date());
			lDdExtension.add(ddExtension);
			lDdExtension.add(ddExtension);
			when(ddExtensionService.getByIdAndType(anyInt(),anyInt())).thenReturn(lDdExtension);
			ResponseEntity< DdExtensionDto> result = ddExtensionController.getById(soId);
			assertNotNull(result);
		}, "Test failed due to an exception: getBySoIdValidTest ");
		logger.info("DdExtensionControllerTest.getBySoIdValidTest : Finished");
	}
	
	@Test
	void saveDdExtensionDataValidTest() {
		logger.info("DdExtensionControllerTest.saveDdExtensionDataValidTest : Initiated");
		assertDoesNotThrow(() -> {
			DdExtensionDto ddExtensionDto = new DdExtensionDto();
			when(ddExtensionService.saveDdExtensionData(any(DdExtensionDto.class), null)).thenReturn(ddExtensionDto);
			ResponseEntity<DdExtensionDto> result = ddExtensionController.saveDdExtensionData(ddExtensionDto.toString(),null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveDdExtensionDataValidTest ");
		logger.info("DdExtensionControllerTest.saveDdExtensionDataValidTest : Finished");
	}
	
	@Test
	void saveDdExtensionDataValidSecScenarioTest() {
		logger.info("DdExtensionControllerTest.saveDdExtensionDataValidSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			DdExtensionDto ddExtensionDto = new DdExtensionDto();
			when(ddExtensionService.saveDdExtensionData(any(DdExtensionDto.class), null)).thenReturn(ddExtensionDto);
			ResponseEntity<DdExtensionDto> result = ddExtensionController.saveDdExtensionData(ddExtensionDto.toString(),null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveDdExtensionDataValidSecScenarioTest ");
		logger.info("DdExtensionControllerTest.saveDdExtensionDataValidSecScenarioTest : Finished");
	}
	
}
