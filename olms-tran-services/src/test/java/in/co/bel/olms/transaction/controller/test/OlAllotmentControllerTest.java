package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import in.co.bel.olms.transaction.controller.OlAllotmentController;
import in.co.bel.olms.transaction.dto.OlAllotmentDto;
import in.co.bel.olms.transaction.service.ol.OlAllotmentService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class OlAllotmentControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(OlAllotmentControllerTest.class);
	
	@InjectMocks
	OlAllotmentController olAllotmentController;
	
	@Mock
	OlAllotmentService olAllotmentService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	
	@Test
	void getByEmpIdValidTest() {
		logger.info("OlAllotmentControllerTest.getByEmpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 213;
			List<OlAllotmentDto> lOlAllot = new ArrayList<>();
			OlAllotmentDto olAllot = new OlAllotmentDto();
			olAllot.setId(empId);
			lOlAllot.add(olAllot);
			lOlAllot.add(olAllot);
			when(olAllotmentService.getOlAllotmentForEmpId(anyInt(), anyInt())).thenReturn(lOlAllot);
			ResponseEntity<List<OlAllotmentDto>> result = olAllotmentController.getByEmpId(empId,anyInt());
			assertNotNull(result);
		}, "Test failed due to an exception: getByEmpIdValidTest ");
		logger.info("OlAllotmentControllerTest.getByEmpIdValidTest : Finished");
	}
	
	@Test
	void getByEmpIdDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("OlAllotmentControllerTest.getByEmpIdDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(olAllotmentService.getOlAllotmentForEmpId(anyInt(),anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<List<OlAllotmentDto>> result = olAllotmentController.getByEmpId(0,0);
			assertNotNull(result);
		}, "Test failed due to an exception: getByEmpIdDataNotValidExceptionTest ");
		logger.info("OlAllotmentControllerTest.getByEmpIdDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getByEmpIdExceptionTest() {
		logger.info("OlAllotmentControllerTest.getByEmpIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(olAllotmentService.getOlAllotmentForEmpId(anyInt(),anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<List<OlAllotmentDto>> result = olAllotmentController.getByEmpId(0,0);
			assertNotNull(result);
		}, "Test failed due to an exception: getByEmpIdExceptionTest ");
		logger.info("OlAllotmentControllerTest.getByEmpIdExceptionTest : Finished");
	}
	
}
