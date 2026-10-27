package in.co.bel.olms.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.service.INoteDashboardService;
import in.co.bel.olms.transaction.repository.INoteRepository;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class INoteDashboardServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(INoteDashboardServiceTest.class);
	
	@InjectMocks
	INoteDashboardService iNoteDashboardService;
	
	@Mock
	SalesGroupRepository salesGroupRepository;
	
	@Mock
	INoteRepository iNoteRepository;
	
	@Mock
	ObjectMapper mapper2;
	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getINoteValueBySalesGrpIdValidTest() {
		logger.info("INoteDashboardServiceTest.getINoteValueBySalesGrpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Integer salesGrpId = 1;
			Integer selectedFY = 2022;
			List<Object[]> lObj = Arrays.asList(new Object[] {"APR", "100.00"}, new Object[] {"JUL", "120.00"});
			when(iNoteRepository.getINoteValueBySalesGrpId(anyInt(), any(), any())).thenReturn(lObj);
			List<Map<String, String>> result = iNoteDashboardService.getINoteValueBySalesGrpId(salesGrpId, selectedFY);
			assertNotNull(result);
			assertNotEquals("[]", result.toString());
		}, "Test failed due to an exception: getINoteValueBySalesGrpIdValidTest ");
		logger.info("INoteDashboardServiceTest.getINoteValueBySalesGrpIdValidTest : Finished");
	}
	
	@Test
	void getINoteValueByFYValidTest() {
		logger.info("INoteDashboardServiceTest.getINoteValueByFYValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Integer orgId = 100303;
			Integer selectedFY = 2022;
			List<SalesGroup> lSGrp = new ArrayList<>();
			List<String> salesGrpList = new ArrayList<>();
			SalesGroup sGrp = new SalesGroup();
			sGrp.setName("MCE");
			salesGrpList.add("MCE");
			salesGrpList.add("MCS/NCS");
            lSGrp.add(sGrp);
            lSGrp.add(sGrp);
			List<Object[]> lObj = Arrays.asList(new Object[] {"APR", "100.00"}, new Object[] {"JUL", "120.00"});
			when(iNoteRepository.getINoteValueByFY(anyInt(), any(), any())).thenReturn(lObj);
			when(salesGroupRepository.findByActive(anyBoolean())).thenReturn(lSGrp);
			List<Map<String, String>> result = iNoteDashboardService.getINoteValueByFY(orgId, selectedFY);
			assertNotNull(result);
			assertNotEquals("[]", result);
		}, "Test failed due to an exception: getINoteValueByFYValidTest ");
		logger.info("INoteDashboardServiceTest.getINoteValueByFYValidTest : Finished");
	}
	
}
