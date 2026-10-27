package in.co.bel.olms.dash.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import in.co.bel.olms.dash.controller.InpectorDashboardController;
import in.co.bel.olms.transaction.dto.InpectorDto;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class InpectorDashboardControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(InpectorDashboardControllerTest.class);
	
	@InjectMocks
	InpectorDashboardController inpectorDashboardController;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getOlByInspIdValidTest() {
		logger.info("InpectorDashboardControllerTest.getOlByInspIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			InpectorDto result = inpectorDashboardController.getOlByInspId();
			assertNotNull(result);
		}, "Test failed due to an exception: getOlByInspIdValidTest ");
		logger.info("InpectorDashboardControllerTest.getOlByInspIdValidTest : Finished");
	}
	
}
