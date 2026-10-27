package in.co.bel.olms.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import in.co.bel.olms.rpt.dto.WorkLoadDto;
import in.co.bel.olms.service.WorkloadService;
import in.co.bel.olms.transaction.repository.WorkLoadOlRepository;
import in.co.bel.olms.transaction.repository.WorkLoadRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class WorkloadServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(WorkloadServiceTest.class);
	
	@InjectMocks
    WorkloadService workloadService;
	
	@Mock
	WorkLoadOlRepository workLoadOlRepository;
	
	@Mock
	WorkLoadRepository workLoadRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getWorkloadReportsbyDatesValidTest() {
		logger.info("WorkloadServiceTest.getWorkloadReportsbyDatesValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Calendar from = Calendar.getInstance();
			from.set(2022, 03, 13);
			Calendar to = Calendar.getInstance();
			to.set(2023, 07, 21);
			List<Object[]> dbResp = Arrays.asList(
					new Object[] {"1.00", "New SO 2", "200.00", "160.00"},
					new Object[] {"1.00", "New SO 2", "200.00", "160.00"}
			);
			when(workLoadRepository.getwrkLoadDataByDate(any(), any())).thenReturn(dbResp);
			when(workLoadOlRepository.getwrkLoadDataByDate(any(), any())).thenReturn(dbResp);
			List<WorkLoadDto> result = workloadService.getWorkloadReportsbyDates(from.getTime(), to.getTime());
			assertNotNull(result);
			assertNotEquals("[]", result.toString());
		}, "Test failed due to an exception: getWorkloadReportsbyDatesValidTest ");
		logger.info("WorkloadServiceTest.getWorkloadReportsbyDatesValidTest : Finished");
	}	
	
	@Test
	void getWorkloadReportsbyDatesNullTest() {
		logger.info("WorkloadServiceTest.getWorkloadReportsbyDatesNullTest : Initiated");
		assertDoesNotThrow(() -> {
			Calendar from = Calendar.getInstance();
			from.set(2022, 03, 13);
			Calendar to = Calendar.getInstance();
			to.set(2023, 07, 21);
			List<Object[]> dbResp = Arrays.asList(
					new Object[] {"1.00", null, null, null},
					new Object[] {"1.00", null, null, null}
			);
			when(workLoadRepository.getwrkLoadDataByDate(any(), any())).thenReturn(dbResp);
			when(workLoadOlRepository.getwrkLoadDataByDate(any(), any())).thenReturn(dbResp);
			List<WorkLoadDto> result = workloadService.getWorkloadReportsbyDates(from.getTime(), to.getTime());
			assertNotNull(result);
		}, "Test failed due to an exception: getWorkloadReportsbyDatesNullTest ");
		logger.info("WorkloadServiceTest.getWorkloadReportsbyDatesNullTest : Finished");
	}	
	
}
