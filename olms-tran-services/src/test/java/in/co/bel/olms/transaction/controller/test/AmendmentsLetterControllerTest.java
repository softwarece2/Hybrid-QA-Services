package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import in.co.bel.olms.transaction.controller.AmendmentsLetterController;
import in.co.bel.olms.transaction.dto.AmendmentsLetterDto;
import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.repository.AmendmentsLetterRepository;
import in.co.bel.olms.transaction.service.AmendmentsLetterService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class AmendmentsLetterControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AmendmentsLetterControllerTest.class);
	
	@InjectMocks
	AmendmentsLetterController amendmentsLetterController;
	
	@Mock
	AmendmentsLetterService amendmentsLetterService;
	
	@Mock
	AmendmentsLetterRepository amendmentsLetterRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getBySoIdValidTest() {
		logger.info("AmendmentsLetterControllerTest.getBySoIdValidTest : Initiated");
		 assertDoesNotThrow(() -> {
			int soId = 223;
			List<AmendmentsLetter> lAmendLet = new ArrayList<>();
		    AmendmentsLetter amendLet = new AmendmentsLetter();
		    amendLet.setId(67);
		    lAmendLet.add(amendLet);
		    lAmendLet.add(amendLet);
		    when(amendmentsLetterRepository.getBySoId(anyInt())).thenReturn(lAmendLet);
		    when(mapper.map(any(), any())).thenReturn(new AmendmentsLetterDto());
		    ResponseEntity< Object> result = amendmentsLetterController.getByIdAndType(soId,anyInt());
		    List<AmendmentsLetterDto> retVal = amendmentsLetterService.getByIdAndType(soId, anyInt());
		    assertNotNull(retVal);
			assertNotNull(result);
			List<AmendmentsLetterDto> emptyVal = new ArrayList<>();
			assertNotEquals(emptyVal, retVal);
		}, "Test failed due to an exception: getBySoIdValidTest ");
			
		logger.info("AmendmentsLetterControllerTest.getBySoIdValidTest : Finished");
	}
	
	@Test
	void saveAmendmentsLetterDataValidTest() {
		logger.info("AmendmentsLetterControllerTest.saveAmendmentsLetterDataValidTest : Initiated");
		assertDoesNotThrow(() -> {
			AmendmentsLetterDto amendmentsLetterDto = new AmendmentsLetterDto();
			when(amendmentsLetterService.saveAmendmentsLetterData(any(AmendmentsLetterDto.class), null)).thenReturn(amendmentsLetterDto);
			ResponseEntity<String> result = amendmentsLetterController.saveAmendmentsLetterData(amendmentsLetterDto.toString(),null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveAmendmentsLetterDataValidTest");
		logger.info("AmendmentsLetterControllerTest.saveAmendmentsLetterDataValidTest : Finished");
	}
	
	@Test
	void saveAmendmentsLetterDataValidSecScenarioTest() {
		logger.info("AmendmentsLetterControllerTest.saveAmendmentsLetterDataValidSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			AmendmentsLetterDto amendmentsLetterDto = new AmendmentsLetterDto();
			when(amendmentsLetterService.saveAmendmentsLetterData(any(AmendmentsLetterDto.class), null)).thenReturn(amendmentsLetterDto);
			ResponseEntity<String> result = amendmentsLetterController.saveAmendmentsLetterData(amendmentsLetterDto.toString(),null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveAmendmentsLetterDataValidSecScenarioTest");
		logger.info("AmendmentsLetterControllerTest.saveAmendmentsLetterDataValidSecScenarioTest : Finished");
	}
	
}
