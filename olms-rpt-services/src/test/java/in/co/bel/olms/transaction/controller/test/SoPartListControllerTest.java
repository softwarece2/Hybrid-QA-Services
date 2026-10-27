package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
import in.co.bel.olms.transaction.controller.SoPartListController;
import in.co.bel.olms.transaction.dto.SoPartListDto;
import in.co.bel.olms.transaction.entity.SoPartList;
import in.co.bel.olms.transaction.service.SoPartListService;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class SoPartListControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SoPartListControllerTest.class);
	
	@InjectMocks
	SoPartListController soPartListController;
	
	@Mock
	SoPartListService soPartListService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getSoPartListByIdValidTest() {
		logger.info("SoPartListControllerTest.getSoPartListByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 1;
			SoPartListDto soPartListDto = new SoPartListDto();
			soPartListDto.setId(1);
			when(soPartListService.getSoPartListById(anyInt())).thenReturn(new SoPartList());
			when(mapper.map(any(SoPartList.class), eq(SoPartListDto.class))).thenReturn(soPartListDto);
			ResponseEntity<SoPartListDto> result = soPartListController.getSoPartListById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSoPartListByIdValidTest ");
		logger.info("SoPartListControllerTest.getSoPartListByIdValidTest : Finished");
	}
	
}
