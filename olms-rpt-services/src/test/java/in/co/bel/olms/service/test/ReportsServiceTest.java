package in.co.bel.olms.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsReportServicesApplication;
import in.co.bel.olms.rpt.dto.ReportsDto;
import in.co.bel.olms.service.ReportsService;
import in.co.bel.olms.transaction.repository.ReportsRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class ReportsServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(ReportsServiceTest.class);
	
	@InjectMocks
	ReportsService reportsService;
	
	@Mock
	ReportsRepository  reportsRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getReportsDataByDatesValidTest() {
		logger.info("ReportsServiceTest.getReportsDataByDatesValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Calendar from = Calendar.getInstance();
			from.set(2022, 03, 13);
			Calendar to = Calendar.getInstance();
			to.set(2023, 07, 21);
			List<Object[]> dbResp = new ArrayList<>();
			Object[] obj1 = {"1", "New SO 2", "A Vijayakumar", "SBC-Krishna5", "ABG", "Prjct 2001"};
			dbResp.add(obj1);
			dbResp.add(obj1);
			when(reportsRepository.getReportsDataByDate(any(), any(), anyInt())).thenReturn(dbResp);
			List<ReportsDto> result = reportsService.getReportsDataByDates(from.getTime(), to.getTime(), 1);
			assertNotNull(result);
			assertNotEquals("[]", result);
		}, "Test failed due to an exception: getReportsDataByDatesValidTest ");
		logger.info("ReportsServiceTest.getReportsDataByDatesValidTest : Finished");
	}
	
	@Test
	void getReportsDataByDatesNullTest() {
		logger.info("ReportsServiceTest.getReportsDataByDatesNullTest : Initiated");
		assertDoesNotThrow(() -> {
			Calendar from = Calendar.getInstance();
			from.set(2022, 03, 13);
			Calendar to = Calendar.getInstance();
			to.set(2023, 07, 21);
			List<Object[]> dbResp = new ArrayList<>();
			Object[] obj1 = {null, null, null, null, null, null};
			dbResp.add(obj1);
			dbResp.add(obj1);
			when(reportsRepository.getReportsDataByDate(any(), any(), anyInt())).thenReturn(dbResp);
			List<ReportsDto> result = reportsService.getReportsDataByDates(from.getTime(), to.getTime(), 1);
			assertNotNull(result);
		}, "Test failed due to an exception: getReportsDataByDatesNullTest ");
		logger.info("ReportsServiceTest.getReportsDataByDatesNullTest : Finished");
	}
	
}
