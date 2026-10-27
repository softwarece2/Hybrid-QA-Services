package in.co.bel.olms.rpt.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsReportServicesApplication;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.rpt.controller.INoteDashboardController;
import in.co.bel.olms.service.INoteDashboardService;
import in.co.bel.olms.transaction.repository.INoteRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class INoteDashboardControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(INoteDashboardControllerTest.class);
	
	@InjectMocks
	INoteDashboardController iNoteDashboardController;
	
	@Mock
	INoteDashboardService iNoteDashboardService;
	
	@Mock
	INoteRepository iNoteRepository;
	
	@Mock
	SalesGroupRepository salesGroupRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getINoteValueBySalesGrpIdValidTest() {
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			  Integer salesGrpId = 12;
			  Integer selectedFY = 2023;
			  List<Map<String, String>> expectedChartData = Arrays.asList(createChartDataRow("APR", "100.00"),
					  createChartDataRow("MAY", "200.00"));
			  List<Object[]> inoteData = Arrays.asList(new Object[]{"APR", "100.00"}, new Object[]{"MAY", "200.00"});
			  when(iNoteDashboardService.getINoteValueBySalesGrpId(anyInt(), anyInt())).thenReturn(expectedChartData);
			  when(iNoteRepository.getINoteValueBySalesGrpId(anyInt(), any(), any())).thenReturn(inoteData);
			  ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueBySalesGrpId(salesGrpId, selectedFY);
			  assertNotNull(result);
			  assertNotEquals(HttpStatus.BAD_REQUEST, result);
		}, "Test failed due to an exception: getINoteValueBySalesGrpIdValidTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdValidTest : Finished");
	}
	
	@Test
	void getINoteValueBySalesGrpIdDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			  when(iNoteDashboardService.getINoteValueBySalesGrpId(anyInt(), anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			  when(iNoteRepository.getINoteValueBySalesGrpId(anyInt(), any(), any())).thenReturn(null);
			  ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueBySalesGrpId(0, 0);
			  assertNotNull(result);
		}, "Test failed due to an exception: getINoteValueBySalesGrpIdDataNotValidExceptionTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getINoteValueBySalesGrpIdExceptionTest() {
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			  when(iNoteDashboardService.getINoteValueBySalesGrpId(anyInt(), anyInt())).thenThrow(new NullPointerException("Exception"));
			  when(iNoteRepository.getINoteValueBySalesGrpId(anyInt(), any(), any())).thenReturn(null);
			  ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueBySalesGrpId(0, 0);
			  assertNotNull(result);
		}, "Test failed due to an exception: getINoteValueBySalesGrpIdExceptionTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueBySalesGrpIdExceptionTest : Finished");
	}
	
	@Test
	void getINoteValueByFYValidTest() {
		logger.info("INoteDashboardControllerTest.getINoteValueByFYValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Integer orgId = 12;
			Integer selectedFY = 2023;
			SalesGroup sGrp = new SalesGroup();
			List<SalesGroup> lSGrp = new ArrayList<>();
			sGrp.setName("MCE");
			lSGrp.add(sGrp);
			lSGrp.add(sGrp);
			List<Map<String, String>> expectedChartData = Arrays.asList(createChartDataRow("APR", "100.00"),
					createChartDataRow("MAY", "200.00"));
			List<Object[]> inoteData = Arrays.asList(new Object[] { "APR", "100.00" },
					new Object[] { "MAY", "200.00" });
			when(iNoteDashboardService.getINoteValueByFY(anyInt(), anyInt())).thenReturn(expectedChartData);
			when(iNoteRepository.getINoteValueByFY(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(inoteData);
			when(salesGroupRepository.findByActive(anyBoolean())).thenReturn(lSGrp);
			ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueByFY(orgId, selectedFY);
			assertNotNull(result);
			assertNotEquals(HttpStatus.BAD_REQUEST, result);
		}, "Test failed due to an exception: getINoteValueByFYValidTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueByFYValidTest : Finished");
	}
	
	@Test
	void getINoteValueByFYDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("INoteDashboardControllerTest.getINoteValueByFYDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(iNoteDashboardService.getINoteValueByFY(anyInt(), anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			when(iNoteRepository.getINoteValueByFY(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(null);
			when(salesGroupRepository.findByActive(anyBoolean())).thenReturn(null);
			ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueByFY(0, 0);
			assertNotNull(result);
		}, "Test failed due to an exception: getINoteValueByFYDataNotValidExceptionTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueByFYDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getINoteValueByFYExceptionTest() throws Exception {
		logger.info("INoteDashboardControllerTest.getINoteValueByFYExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(iNoteDashboardService.getINoteValueByFY(anyInt(), anyInt())).thenThrow(new NullPointerException("Exception"));
			when(iNoteRepository.getINoteValueByFY(anyInt(), any(LocalDate.class), any(LocalDate.class))).thenReturn(null);
			when(salesGroupRepository.findByActive(anyBoolean())).thenReturn(null);
			ResponseEntity<List<Map<String, String>>> result = iNoteDashboardController.getINoteValueByFY(0, 0);
			assertNotNull(result);
		}, "Test failed due to an exception: getINoteValueByFYExceptionTest ");
		logger.info("INoteDashboardControllerTest.getINoteValueByFYExceptionTest : Finished");
	}
    private Map<String, String> createChartDataRow(String key, String value) {
        Map<String, String> row = new HashMap<>();
        row.put("key", key);
        row.put("value", value);
        return row;
    }
    
}
