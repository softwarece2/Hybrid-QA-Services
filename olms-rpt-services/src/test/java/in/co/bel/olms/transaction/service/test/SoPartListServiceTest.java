package in.co.bel.olms.transaction.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.entity.SoPartList;
import in.co.bel.olms.transaction.repository.SoPartListRepository;
import in.co.bel.olms.transaction.service.SoPartListService;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class SoPartListServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(SoPartListServiceTest.class);
	
	@InjectMocks
	SoPartListService soPartListService;
	
	@Mock
	SoPartListRepository soPartListRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getSoPartListByIdValidTest() {
		logger.info("SoPartListServiceTest.getSoPartListByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 12;
			SoPartList soPartList = new SoPartList();
			soPartList.setId(11);
			Optional<SoPartList> optData = Optional.of(soPartList);
			when(soPartListRepository.findById(anyInt())).thenReturn(optData);
			SoPartList result = soPartListService.getSoPartListById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSoPartListByIdValidTest ");
		logger.info("SoPartListServiceTest.getSoPartListByIdValidTest : Finished");
	}
	
	@Test
	void getSoPartListByIdSecScenarioTest() {
		logger.info("SoPartListServiceTest.getSoPartListByIdSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 12;
			when(soPartListRepository.findById(anyInt())).thenReturn(Optional.empty());
			SoPartList result = soPartListService.getSoPartListById(id);
			assertEquals(DataNotFoundException.class, result.getClass());
		}, "Test failed due to an exception: getSoPartListByIdSecScenarioTest ");
		logger.info("SoPartListServiceTest.getSoPartListByIdSecScenarioTest : Finished");
	}
	
	
	@Test
	void getAllSoPartListValidTest() {
		logger.info("SoPartListServiceTest.getAllSoPartListValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<SoPartList> lSoPartList = new ArrayList<>();
			SoPartList soPartList = new SoPartList();
			soPartList.setId(1);
			soPartList.setCreatedDateTime(new Date());
			lSoPartList.add(soPartList);
			lSoPartList.add(soPartList);
			when(soPartListRepository.findAll()).thenReturn(lSoPartList);
			List<SoPartList> result = soPartListService.getAllSoPartList();
			assertNotNull(result);
			assertNotEquals("[]", result);
		}, "Test failed due to an exception: getAllSoPartListValidTest ");
		logger.info("SoPartListServiceTest.getAllSoPartListValidTest : Finished");
	}
	
}
