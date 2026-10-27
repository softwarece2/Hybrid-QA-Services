package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import in.co.bel.olms.transaction.controller.OlConcessionController;
import in.co.bel.olms.transaction.dto.OlConcessionDto;
import in.co.bel.olms.transaction.entity.OlConcession;
import in.co.bel.olms.transaction.service.OlConcessionService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class OlConcessionControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(OlConcessionControllerTest.class);
	
	@InjectMocks
	OlConcessionController olConcessionController;
	
	@Mock
	OlConcessionService olConcessionService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getOLConcessionsByOlIdValidTest() {
		logger.info("OlConcessionControllerTest.getOLConcessionsByOlIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int olId = 84;
			int olId2 = 85;
			String dt = "06-07-2023";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dateFormat.parse(dt);
			List<OlConcession> lOlConcessions = new ArrayList<>();
			OlConcession olConcessions = new OlConcession();
			olConcessions.setCreatedDate(date);
			lOlConcessions.add(olConcessions);
			lOlConcessions.add(olConcessions);
			when(olConcessionService.getOLConcessionsByOlIds(anyList())).thenReturn(lOlConcessions);
			when(mapper.map(any(OlConcession.class), eq(OlConcessionDto.class))).thenReturn(new OlConcessionDto());
			ResponseEntity<List<OlConcessionDto>> result = olConcessionController.getOLConcessionsByOlIds(Arrays.asList(olId, olId2));
			assertNotNull(result);
		}, "Test failed due to an exception: getOLConcessionsByOlIdValidTest ");
		logger.info("OlConcessionControllerTest.getOLConcessionsByOlIdValidTest : Finished");
	}
	
	@Test
	void saveOlConcessionValidTest() {
		logger.info("OlConcessionControllerTest.saveOlConcessionValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<OlConcessionDto> lOlConcDto = new ArrayList<>();
			Map<Boolean, String> response = new HashMap<>();
			OlConcessionDto olConcDto = new OlConcessionDto();
			olConcDto.setId(223);
			lOlConcDto.add(olConcDto);
			lOlConcDto.add(olConcDto);
			String resp = gson.toJson(lOlConcDto);
			response.put(true, resp);
			when(olConcessionService.saveOlConcession(anyList())).thenReturn(response);
			ResponseEntity<String> result = olConcessionController.saveOlConcession(lOlConcDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveOlConcessionValidTest ");
		logger.info("OlConcessionControllerTest.saveOlConcessionValidTest : Finished");
	}
	
	@Test
	void saveOlConcessionSecScenarioTest() {
		logger.info("OlConcessionControllerTest.saveOlConcessionSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> response = new HashMap<>();
			response.put(false, "false Condition");
			when(olConcessionService.saveOlConcession(anyList())).thenReturn(response);
			ResponseEntity<String> result = olConcessionController.saveOlConcession(new ArrayList<>());
			assertNotNull(result);
		}, "Test failed due to an exception: saveOlConcessionSecScenarioTest ");
		logger.info("OlConcessionControllerTest.saveOlConcessionSecScenarioTest : Finished");
	}
	
}
