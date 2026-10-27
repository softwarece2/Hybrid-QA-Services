package in.co.bel.olms.rpt.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import in.co.bel.olms.OlmsReportServicesApplication;
import in.co.bel.olms.rpt.controller.WorkloadController;
import in.co.bel.olms.rpt.dto.WorkLoadDto;
import in.co.bel.olms.service.WorkloadService;
import in.co.bel.olms.transaction.repository.WorkLoadOlRepository;
import in.co.bel.olms.transaction.repository.WorkLoadRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class WorkloadControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(WorkloadControllerTest.class);
	
	@InjectMocks
	WorkloadController workloadController;
	
	@Mock
	WorkloadService workLoadService;
	
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
	void getByDateValidTest() {
		logger.info("WorkloadControllerTest.getByDateValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<WorkLoadDto> lWorkLoad = new ArrayList<>();
			WorkLoadDto wLoadDto = new WorkLoadDto();
			wLoadDto.setEstMandays(3);
			lWorkLoad.add(wLoadDto);
			lWorkLoad.add(wLoadDto);
			when(workLoadService.getWorkloadReportsbyDates(any(), any())).thenReturn(lWorkLoad);
		    ResponseEntity<List<WorkLoadDto>> result = workloadController.getByDate("2022-03-12", "2023-05-13");
			assertNotNull(result);
		}, "Test failed due to an exception: getByDateValidTest ");
		logger.info("WorkloadControllerTest.getByDateValidTest : Finished");
	}
	
	@Test
	void getByDateExceptionTest() {
		logger.info("WorkloadControllerTest.getByDateExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(workLoadService.getWorkloadReportsbyDates(any(), any())).thenThrow(new NullPointerException());
		    ResponseEntity<List<WorkLoadDto>> result = workloadController.getByDate(null, null);
			assertNotNull(result);
		}, "Test failed due to an exception: getByDateExceptionTest ");
		logger.info("WorkloadControllerTest.getByDateExceptionTest : Finished");
	}
}
