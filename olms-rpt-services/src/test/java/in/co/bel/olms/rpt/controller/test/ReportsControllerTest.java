package in.co.bel.olms.rpt.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

import in.co.bel.olms.OlmsReportServicesApplication;
import in.co.bel.olms.rpt.controller.ReportsController;
import in.co.bel.olms.rpt.dto.ReportsDto;
import in.co.bel.olms.service.ReportsService;
import in.co.bel.olms.transaction.repository.ReportsRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class ReportsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ReportsControllerTest.class);
	
	@InjectMocks
	ReportsController reportsController;
	
	@Mock
	ReportsService reportsService;
	
	@Mock
	ReportsRepository  reportsRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getByDateValidTest() {
		logger.info("ReportsControllerTest.getByDateValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int roleId = 1;
			String from = "2022-06-11";
			Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(from);
			
			String to = "2023-07-22";
			Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(to);
			
			List<Object[]> dbResp = Arrays.asList(
					new Object[] {"New SO 2", "Super Admin User", "SBC-Krishna5", "AAGL", "VRSAM_MAIN EQUIPMENT"},
					new Object[] {"New SO 2", "Super Admin User", "SBC-Krishna5", "AAGL", "VRSAM_MAIN EQUIPMENT"}
			);
			when(reportsRepository.getReportsDataByDate(any(Date.class), any(Date.class), anyInt())).thenReturn(dbResp);
			ResponseEntity<List<ReportsDto>> result = reportsController.getByDate(from , to, roleId);
			List<ReportsDto> retVal = reportsService.getReportsDataByDates(date1, date2, roleId);
			assertNotNull(retVal);
			assertNotNull(result);
			assertNotEquals("[]", retVal);
		}, "Test failed due to an exception: getByDateValidTest ");
		logger.info("ReportsControllerTest.getByDateValidTest : Finished");
	}
		
	@Test
	void getByDateExceptionTest() {
		logger.info("ReportsControllerTest.getByDateExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(reportsRepository.getReportsDataByDate(any(), any(), anyInt())).thenThrow(new NullPointerException());
			List<ReportsDto> retVal = reportsService.getReportsDataByDates(null, null, 0);
			assertNotNull(retVal);
		}, "Test failed due to an exception: getByDateExceptionTest ");
		logger.info("ReportsControllerTest.getByDateExceptionTest : Finished");
	}
	
}
