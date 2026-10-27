package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import in.co.bel.olms.transaction.controller.ReturnOfferLetterController;
import in.co.bel.olms.transaction.dto.ReturnOfferLetterDto;
import in.co.bel.olms.transaction.entity.ReturnOfferLetter;
import in.co.bel.olms.transaction.service.ol.ReturnOfferLetterService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class ReturnOfferLetterControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ReturnOfferLetterControllerTest.class);
	
	@InjectMocks
	ReturnOfferLetterController returnOfferLetterController;
	
	@Mock
	ReturnOfferLetterService returnOfferLetterService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveReturnOfferLetterValidTest() {
		logger.info("ReturnOfferLetterControllerTest.saveReturnOfferLetterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			ReturnOfferLetterDto retOfferDto = new ReturnOfferLetterDto();
			when(returnOfferLetterService.saveReturnOfferLetter(any())).thenReturn(new ReturnOfferLetter());
			when(mapper.map(any(ReturnOfferLetter.class), eq(ReturnOfferLetterDto.class))).thenReturn(retOfferDto);
			ResponseEntity<ReturnOfferLetterDto> res = returnOfferLetterController.saveReturnOfferLetter(retOfferDto);
			assertNotNull(res);
		}, "Test failed due to an exception: saveReturnOfferLetterValidTest ");
		logger.info("ReturnOfferLetterControllerTest.saveReturnOfferLetterValidTest : Finished");
	}
	
	@Test
	void getByIdValidTest() {
		logger.info("ReturnOfferLetterControllerTest.getByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			when(returnOfferLetterService.getById(anyInt())).thenReturn(new ReturnOfferLetterDto());
			ResponseEntity<ReturnOfferLetterDto> res = returnOfferLetterController.getById(id);
			assertNotNull(res);
		}, "Test failed due to an exception: getByIdValidTest ");
		logger.info("ReturnOfferLetterControllerTest.getByIdValidTest : Finished");
	}
	
	@Test
	void getByOlIdValidTest() {
		logger.info("ReturnOfferLetterControllerTest.getByOlIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int offerLetterId = 84;
			when(returnOfferLetterService.getByOlId(anyInt())).thenReturn(new ReturnOfferLetterDto());
			ResponseEntity<ReturnOfferLetterDto> res = returnOfferLetterController.getByOlId(offerLetterId);
			assertNotNull(res);
		}, "Test failed due to an exception: getByOlIdValidTest ");
		logger.info("ReturnOfferLetterControllerTest.getByOlIdValidTest : Finished");
	}
	
	@Test
	void getBySalesGrpIdValidTest() {
		logger.info("ReturnOfferLetterControllerTest.getBySalesGrpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int salesGrpId = 1;
			List<ReturnOfferLetterDto> lOlDto = new ArrayList<>();
			ReturnOfferLetterDto olDto = new ReturnOfferLetterDto();
			olDto.setCreatedDate(new Date());
			lOlDto.add(olDto);
			lOlDto.add(olDto);
			when(returnOfferLetterService.getBySalesGrpId(anyInt())).thenReturn(lOlDto);
			ResponseEntity<List<ReturnOfferLetterDto>> res = returnOfferLetterController.getBySalesGrpId(salesGrpId);
			assertNotNull(res);
		}, "Test failed due to an exception: getBySalesGrpIdValidTest ");
		logger.info("ReturnOfferLetterControllerTest.getBySalesGrpIdValidTest : Finished");
	}
	
	@Test
	void getByEmpValidTest() {
		logger.info("ReturnOfferLetterControllerTest.getByEmpValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 12;
			List<ReturnOfferLetterDto> lOlDto = new ArrayList<>();
			ReturnOfferLetterDto olDto = new ReturnOfferLetterDto();
			olDto.setCreatedDate(new Date());
			lOlDto.add(olDto);
			lOlDto.add(olDto);
			when(returnOfferLetterService.getByEmp(anyInt())).thenReturn(lOlDto);
			ResponseEntity<List<ReturnOfferLetterDto>> res = returnOfferLetterController.getByEmp(empId);
			assertNotNull(res);
		}, "Test failed due to an exception: getByEmpValidTest ");
		logger.info("ReturnOfferLetterControllerTest.getByEmpValidTest : Finished");
	}
	
	@Test
	void getByEmpnSubValidTest() {
		logger.info("ReturnOfferLetterControllerTest.getByEmpnSubValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 12;
			List<ReturnOfferLetterDto> lOlDto = new ArrayList<>();
			ReturnOfferLetterDto olDto = new ReturnOfferLetterDto();
			olDto.setCreatedDate(new Date());
			lOlDto.add(olDto);
			lOlDto.add(olDto);
			when(returnOfferLetterService.getByEmpnSub(anyInt())).thenReturn(lOlDto);
			ResponseEntity<List<ReturnOfferLetterDto>> res = returnOfferLetterController.getByEmpnSub(empId);
			assertNotNull(res);
		}, "Test failed due to an exception: getByEmpnSubValidTest ");
		logger.info("ReturnOfferLetterControllerTest.getByEmpnSubValidTest : Finished");
	}
	
}
