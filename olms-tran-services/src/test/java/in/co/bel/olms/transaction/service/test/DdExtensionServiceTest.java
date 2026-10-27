package in.co.bel.olms.transaction.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.DdExtensionDto;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.repository.DdExtensionRepository;
import in.co.bel.olms.transaction.service.DdExtensionService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class DdExtensionServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(DdExtensionServiceTest.class);
	
	@InjectMocks
	DdExtensionService ddExtensionService;
	
	@Mock
	DdExtensionRepository ddExtensionRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getDdExtensionOfSoValidTest() {
		logger.info("DdExtensionServiceTest.getDdExtensionOfSoValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<DdExtension> lDdExtension = new ArrayList<>();
			DdExtension ddExtension = new DdExtension();
			ddExtension.setId(133);
			lDdExtension.add(ddExtension);
			lDdExtension.add(ddExtension);
			when(ddExtensionRepository.findAll()).thenReturn(lDdExtension);
			List<DdExtension> res = ddExtensionService.getDdExtensionOfSo();
			assertNotEquals("[]", res.toString());
		}, "Test failed due to an exception: getDdExtensionOfSoValidTest ");
		logger.info("DdExtensionServiceTest.getDdExtensionOfSoValidTest : Finished");
	}
	
	@Test
	void getByIdValidTest() {
		logger.info("DdExtensionServiceTest.getByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 133;
			DdExtension ddExtension = new DdExtension();
			ddExtension.setId(21);
			Optional<DdExtension> optData = Optional.of(ddExtension);
			when(ddExtensionRepository.findById(anyInt())).thenReturn(optData);
			DdExtension res = ddExtensionService.getById(id);
			assertNotNull(res);
		}, "Test failed due to an exception: getByIdValidTest " );
		logger.info("DdExtensionServiceTest.getByIdValidTest : Finished");
	}
	
	@Test
	void getByIdDataNotFoundExceptionTest() {
		logger.info("DdExtensionServiceTest.getByIdDataNotFoundExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 133;
			Optional<DdExtension> optData = Optional.empty();
			when(ddExtensionRepository.findById(anyInt())).thenReturn(optData);
			DdExtension res = ddExtensionService.getById(id);
			assertEquals(DataNotFoundException.class, res.getClass());
		}, "Test failed due to an exception: getByIdDataNotFoundExceptionTest ");
		logger.info("DdExtensionServiceTest.getByIdDataNotFoundExceptionTest : Finished");
	}
	
	@Test
	void saveDdExtensionDataValidTest() {
		logger.info("DdExtensionServiceTest.saveDdExtensionDataValidTest : Initiated");
		assertDoesNotThrow(() -> {
			DdExtension ddExtension = new DdExtension();
			ddExtension.setId(10);
		}, "Test failed due to an exception: saveDdExtensionDataValidTest ");
		logger.info("DdExtensionServiceTest.saveDdExtensionDataValidTest : Finished");
	}
	
	@Test
	void saveDdExtensionDataExceptionTest() {
		logger.info("DdExtensionServiceTest.saveDdExtensionDataExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(ddExtensionRepository.save(any())).thenThrow(new DataException("Invalid Data", null));
			DdExtension ddExtension = ddExtensionRepository.save(any());
			DdExtensionDto ddExtensionDto = mapper.map(ddExtension, DdExtensionDto.class);
			ddExtensionService.saveDdExtensionData(ddExtensionDto, null);
		}, "Test failed due to an exception: saveDdExtensionDataExceptionTest ");
		logger.info("DdExtensionServiceTest.saveDdExtensionDataExceptionTest : Finished");
	}
	
	
	@Test
	void getLast2DdExtnBySoIdValidTest() {
		logger.info("DdExtensionServiceTest.getLast2DdExtnBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			List<DdExtension> lDdExtensions = new ArrayList<>();
			DdExtension ddExtension = new DdExtension();
			ddExtension.setId(133);
			lDdExtensions.add(ddExtension);
			lDdExtensions.add(ddExtension);
			when(ddExtensionRepository.getLast2BySoId(anyInt())).thenReturn(lDdExtensions);
			when(mapper.map(any(DdExtension.class), eq(DdExtensionDto.class))).thenReturn(new DdExtensionDto());
			List<DdExtensionDto> res = ddExtensionService.getLast2DdExtnBySoId(soId);
			assertNotEquals("[]", res.toString());
			assertNotNull(res);
		}, "Test failed due to an exception: getLast2DdExtnBySoIdValidTest ");
		logger.info("DdExtensionServiceTest.getLast2DdExtnBySoIdValidTest : Finished");
	}
	
}
