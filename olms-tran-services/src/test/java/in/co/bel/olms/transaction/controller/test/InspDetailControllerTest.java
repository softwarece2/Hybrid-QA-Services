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
import in.co.bel.olms.transaction.controller.InspDetailController;
import in.co.bel.olms.transaction.dto.InspDetailDto;
import in.co.bel.olms.transaction.entity.InspDetail;
import in.co.bel.olms.transaction.service.InspDetailService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class InspDetailControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(InspDetailControllerTest.class);
	
	@InjectMocks
	InspDetailController inspDetailController;
	
	@Mock
	InspDetailService inspDetailService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getInspDetailValidTest() {
		logger.info("InspDetailControllerTest.getInspDetailValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 101;
			InspDetail inspDetail = new InspDetail();
			inspDetail.setId(id);
			when(inspDetailService.getInspDetailById(anyInt())).thenReturn(inspDetail);
			when(mapper.map(any(InspDetail.class), eq(InspDetailDto.class))).thenReturn(new InspDetailDto());
			ResponseEntity<InspDetailDto> result = inspDetailController.getInspDetail(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getInspDetailValidTest ");
		logger.info("InspDetailControllerTest.getInspDetailValidTest : Finished");
	}
	
}
