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

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.transaction.controller.PartListController;
import in.co.bel.olms.transaction.dto.PartListDto;
import in.co.bel.olms.transaction.entity.PartList;
import in.co.bel.olms.transaction.service.PartListService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class PartListControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(PartListControllerTest.class);
	
	@InjectMocks
	PartListController partListController;
	
	@Mock
	PartListService partListService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getPartListByIdValidTest() {
		logger.info("PartListControllerTest.getPartListByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 12;
			PartList partList = new PartList();
			partList.setId(id);
			when(partListService.getPartListById(anyInt())).thenReturn(partList);
			when(mapper.map(any(PartList.class), eq(PartListDto.class))).thenReturn(new PartListDto());
			ResponseEntity<PartListDto> result = partListController.getPartListById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getPartListByIdValidTest ");
		logger.info("PartListControllerTest.getPartListByIdValidTest : Finished");
	}
	
}
