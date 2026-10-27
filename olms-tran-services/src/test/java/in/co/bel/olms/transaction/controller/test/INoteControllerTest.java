package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import in.co.bel.olms.transaction.controller.INoteController;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.service.OfferLetterService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class INoteControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(INoteControllerTest.class);
	
	@InjectMocks
	INoteController iNoteController;
	
	@Mock
	OfferLetterService offerLetterService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveINoteValidTest() {
		logger.info("INoteControllerTest.saveINoteValidTest : Initiated");
		assertDoesNotThrow(() -> {
			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setOlId(213);
			String resp = gson.toJson(iNoteDto);
			when(offerLetterService.saveINote(any(INoteDto.class))).thenReturn(resp);
			ResponseEntity<String> result = iNoteController.saveINote(iNoteDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveINoteValidTest ");
		logger.info("INoteControllerTest.saveINoteValidTest : Finished");
	}
	
	@Test
	void saveINoteExceptionTest() {
		logger.info("INoteControllerTest.saveINoteExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(offerLetterService.saveINote(any(INoteDto.class))).thenThrow(new Exception());
			ResponseEntity<String> result = iNoteController.saveINote(new INoteDto());
			assertEquals("<500 INTERNAL_SERVER_ERROR Internal Server Error,failure,[]>", result.toString());
		}, "Test failed due to an exception: saveINoteExceptionTest ");
		logger.info("INoteControllerTest.saveINoteExceptionTest : Finished");
	}
	
	@Test
	void getINoteOfOfferLetterValidTest() {
		logger.info("INoteControllerTest.getINoteOfOfferLetterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int olId = 213;
			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setOlId(213);
			when(offerLetterService.getINoteByOlId(anyInt())).thenReturn(iNoteDto);
			ResponseEntity<INoteDto> result = iNoteController.getINoteOfOfferLetter(olId);
			assertNotNull(result);
		}, "Test failed due to an exception: getINoteOfOfferLetterValidTest ");
		logger.info("INoteControllerTest.getINoteOfOfferLetterValidTest : Finished");
	}
	
	@Test
	void getINoteOfOfferLetterExceptionTest() {
		logger.info("INoteControllerTest.getINoteOfOfferLetterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(offerLetterService.getINoteByOlId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<INoteDto> result = iNoteController.getINoteOfOfferLetter(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getINoteOfOfferLetterExceptionTest ");
		logger.info("INoteControllerTest.getINoteOfOfferLetterExceptionTest : Finished");
	}
	
	@Test
	void getForm4DetailsOfOfferLetterValidTest() {
		logger.info("INoteControllerTest.getForm4DetailsOfOfferLetterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int olId = 84;
			INoteDto iNoteDto = new INoteDto();
			iNoteDto.setIndentDate(new Date());
			when(offerLetterService.getNewINote(anyInt())).thenReturn(iNoteDto);
			ResponseEntity<INoteDto> result = iNoteController.getForm4DetailsOfOfferLetter(olId);
			assertNotNull(result);
		}, "Test failed due to an exception: getForm4DetailsOfOfferLetterValidTest ");
		logger.info("INoteControllerTest.getForm4DetailsOfOfferLetterValidTest : Finished");
	}
	
	@Test
	void getForm4DetailsOfOfferLetterExceptionTest() {
		logger.info("INoteControllerTest.getForm4DetailsOfOfferLetterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(offerLetterService.getNewINote(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<INoteDto> result = iNoteController.getForm4DetailsOfOfferLetter(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getForm4DetailsOfOfferLetterExceptionTest ");
		logger.info("INoteControllerTest.getForm4DetailsOfOfferLetterExceptionTest : Finished");
	}
	
}
