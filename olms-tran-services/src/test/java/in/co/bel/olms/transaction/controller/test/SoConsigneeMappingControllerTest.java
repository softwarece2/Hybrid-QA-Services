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
import in.co.bel.olms.transaction.controller.SoConsigneeMappingController;
import in.co.bel.olms.transaction.dto.SoConsigneeMappingDto;
import in.co.bel.olms.transaction.entity.SoConsigneeMapping;
import in.co.bel.olms.transaction.service.so.SoConsigneeMappingService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SoConsigneeMappingControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SoConsigneeMappingControllerTest.class);
	
	@InjectMocks
	SoConsigneeMappingController soConsigneeMappingController;
	
	@Mock
	SoConsigneeMappingService soConsigneeMappingService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getSoConsigneeMappingDtoByIdValidTest() {
		logger.info("SoConsigneeMappingControllerTest.getSoConsigneeMappingDtoByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 12;
			when(soConsigneeMappingService.getSoConsigneeMappingById(anyInt())).thenReturn(new SoConsigneeMapping());
			when(mapper.map(any(SoConsigneeMapping.class), eq(SoConsigneeMappingDto.class))).thenReturn(new SoConsigneeMappingDto());
			ResponseEntity<SoConsigneeMappingDto> res = soConsigneeMappingController.getSoConsigneeMappingDtoById(id);
			assertNotNull(res);
		}, "Test failed due to an exception: getSoConsigneeMappingDtoByIdValidTest ");
		logger.info("SoConsigneeMappingControllerTest.getSoConsigneeMappingDtoByIdValidTest : Finished");
	}

}
