package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.controller.SoAllotmentController;
import in.co.bel.olms.transaction.dto.SoAllotmentDto;
import in.co.bel.olms.transaction.entity.SoAllotment;
import in.co.bel.olms.transaction.service.so.SoAllotmentService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SoAllotmentControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SoAllotmentControllerTest.class);
	
	@InjectMocks
	SoAllotmentController soAllotmentController;
	
	@Mock
	SoAllotmentService soAllotmentService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void allotSubordinateValidTest() {
		logger.info("SoAllotmentControllerTest.allotSubordinateValidTest : Initiated");
		assertDoesNotThrow(() -> {
			SoAllotmentDto soAllotmentDto = new SoAllotmentDto();
			when(soAllotmentService.allotSubordinate(any())).thenReturn(new SoAllotment());
			when(soAllotmentService.convertBotoDto(any())).thenReturn(soAllotmentDto);
			ResponseEntity<SoAllotmentDto> res = soAllotmentController.allotSubordinate(soAllotmentDto);
			assertNotNull(res);
		}, "Test failed due to an exception: allotSubordinateValidTest ");
		logger.info("SoAllotmentControllerTest.allotSubordinateValidTest : Finished");
	}
	
	@Test
	void allotSubordinateDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("SoAllotmentControllerTest.allotSubordinateDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(soAllotmentService.allotSubordinate(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(soAllotmentService.convertBotoDto(any())).thenReturn(null);
			ResponseEntity<SoAllotmentDto> res = soAllotmentController.allotSubordinate(null);
			assertNotNull(res);
		}, "Test failed due to an exception: allotSubordinateDataNotValidExceptionTest ");
		logger.info("SoAllotmentControllerTest.allotSubordinateDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void allotSubordinateExceptionTest() {
		logger.info("SoAllotmentControllerTest.allotSubordinateExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(soAllotmentService.allotSubordinate(any())).thenThrow(new NullPointerException());
			when(soAllotmentService.convertBotoDto(any())).thenReturn(null);
			ResponseEntity<SoAllotmentDto> res = soAllotmentController.allotSubordinate(null);
			assertNotNull(res);
		}, "Test failed due to an exception: allotSubordinateExceptionTest ");
		logger.info("SoAllotmentControllerTest.allotSubordinateExceptionTest : Finished");
	}
	
	@Test
	void getByEmpIdValidTest() {
		logger.info("SoAllotmentControllerTest.getByEmpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 101;
			List<SoAllotmentDto> lSoAllotmentDto = new ArrayList<>();
			SoAllotmentDto soAllotmentDto = new SoAllotmentDto();
			soAllotmentDto.setId(12);
			lSoAllotmentDto.add(soAllotmentDto);
			lSoAllotmentDto.add(soAllotmentDto);
			when(soAllotmentService.getSoAllotmentForEmpId(anyInt())).thenReturn(lSoAllotmentDto);
			ResponseEntity<List<SoAllotmentDto>> res = soAllotmentController.getByEmpId(empId);
			assertNotNull(res);
		}, "Test failed due to an exception: getByEmpIdValidTest ");
		logger.info("SoAllotmentControllerTest.getByEmpIdValidTest : Finished");
	}
	
	@Test
	void getByEmpIdDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("SoAllotmentControllerTest.getByEmpIdDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(soAllotmentService.getSoAllotmentForEmpId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<List<SoAllotmentDto>> res = soAllotmentController.getByEmpId(0);
			assertNotNull(res);
		}, "Test failed due to an exception: getByEmpIdDataNotValidExceptionTest ");
		logger.info("SoAllotmentControllerTest.getByEmpIdDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getByEmpIdExceptionTest() {
		logger.info("SoAllotmentControllerTest.getByEmpIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(soAllotmentService.getSoAllotmentForEmpId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<List<SoAllotmentDto>> res = soAllotmentController.getByEmpId(0);
			assertNotNull(res);
		}, "Test failed due to an exception: getByEmpIdExceptionTest ");
		logger.info("SoAllotmentControllerTest.getByEmpIdExceptionTest : Finished");
	}

}
