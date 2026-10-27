//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsMasterServicesApplication;
//import in.co.bel.olms.master.controller.ApplicableTestController;
//import in.co.bel.olms.master.dto.ApplicableTestDto;
//import in.co.bel.olms.master.entity.ApplicableTest;
//import in.co.bel.olms.master.repository.ApplicableTestRepository;
//import in.co.bel.olms.master.service.ApplicableTestService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class ApplicableTestControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(ApplicableTestControllerTest.class);
//	
//	@InjectMocks
//	ApplicableTestController applicableTestController;
//	
//	@Mock
//	ApplicableTestService applicableTestService;
//	
//	@Mock
//	ApplicableTestRepository applicableTestRepository;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	private Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void getAllApplicableTestsValidTest() {
//		logger.info("ApplicableTestControllerTest.getAllApplicableTestsValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			List<ApplicableTest> lApplTests = new ArrayList<>();
//			ApplicableTest applTests = new ApplicableTest();
//			applTests.setId(1);
//			lApplTests.add(applTests);
//			lApplTests.add(applTests);
//			when(applicableTestRepository.findAll()).thenReturn(lApplTests);
//			when(applicableTestService.getAllApplicableTests()).thenReturn(lApplTests);
//			when(mapper.map(any(ApplicableTest.class), eq(ApplicableTestDto.class))).thenReturn(new ApplicableTestDto());
//			ResponseEntity<List<ApplicableTestDto>> result = applicableTestController.getAllApplicableTests();
//			assertNotNull(result);
//			assertNotEquals("[]", result);
//		}, "Test failed due to an exception: getAllApplicableTestsValidTest ");
//		logger.info("ApplicableTestControllerTest.getAllApplicableTestsValidTest : Finished");
//	}
//	
//}
