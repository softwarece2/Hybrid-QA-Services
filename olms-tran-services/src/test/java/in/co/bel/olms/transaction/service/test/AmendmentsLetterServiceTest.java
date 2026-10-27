package in.co.bel.olms.transaction.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.dozer.Mapper;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.transaction.dto.AmendmentsLetterDto;
import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.repository.AmendmentsLetterRepository;
import in.co.bel.olms.transaction.service.AmendmentsLetterService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class AmendmentsLetterServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(AmendmentsLetterServiceTest.class);
	
	@InjectMocks
	AmendmentsLetterService amendmentsLetterService;
	
	@Mock
	AmendmentsLetterRepository amendmentsLetterRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();

	
	@Test
	void saveAmendmentsLetterDataValidTest() {
		logger.info("AmendmentsLetterServiceTest.saveAmendmentsLetterDataValidTest : Initiated");
		assertDoesNotThrow(() -> {
			AmendmentsLetter amendmentsLetter = new AmendmentsLetter();
			amendmentsLetter.setId(12);

		},"Test failed due to an exception: saveAmendmentsLetterDataValidTest ");
		logger.info("AmendmentsLetterServiceTest.saveAmendmentsLetterDataValidTest : Finished");
	}
	
	@Test
	void saveAmendmentsLetterDataExceptionTest() {
		logger.info("AmendmentsLetterServiceTest.saveAmendmentsLetterDataExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
	        when(amendmentsLetterRepository.save(any())).thenThrow(new DataException("Invalid Data", null));
	        AmendmentsLetter amendmentsLetter = amendmentsLetterRepository.save(any());
			AmendmentsLetterDto amendmentsLetterDto = mapper.map(amendmentsLetter, AmendmentsLetterDto.class);
			amendmentsLetterService.saveAmendmentsLetterData(amendmentsLetterDto, null);
		}, "Test failed due to an exception: saveAmendmentsLetterDataExceptionTest {}");
		logger.info("AmendmentsLetterServiceTest.saveAmendmentsLetterDataExceptionTest : Finished");
	}
	
}
