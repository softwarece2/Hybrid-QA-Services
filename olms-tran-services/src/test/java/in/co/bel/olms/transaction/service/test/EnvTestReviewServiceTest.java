package in.co.bel.olms.transaction.service.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.EnvTestReviewDto;
import in.co.bel.olms.transaction.entity.EnvTestReview;
import in.co.bel.olms.transaction.repository.EnvTestReviewRepository;
import in.co.bel.olms.transaction.service.EnvTestReviewService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class EnvTestReviewServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(EnvTestReviewServiceTest.class);
	
	@InjectMocks
	EnvTestReviewService envTestReviewService;
	
	@Mock
	EnvTestReviewRepository envTestReviewRepo;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getEnvTestReviewByIdValidTest() {
		logger.info("EnvTestReviewServiceTest.getEnvTestReviewByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 12;
			EnvTestReview envTestReview = new EnvTestReview();
			envTestReview.setId(id);
			Optional<EnvTestReview> envTestOptional = Optional.of(envTestReview);
			when(envTestReviewRepo.findById(anyInt())).thenReturn(envTestOptional);
			EnvTestReview res = envTestReviewService.getEnvTestReviewById(id);
			assertNotNull(res);
		}, "Test failed due to an exception: getEnvTestReviewByIdValidTest ");
		logger.info("EnvTestReviewServiceTest.getEnvTestReviewByIdValidTest : Finished");
	}

	@Test
	void getEnvTestReviewByIdDataNotFoundExceptionTest() {
		logger.info("EnvTestReviewServiceTest.getEnvTestReviewByIdDataNotFoundExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			Optional<EnvTestReview> envTestOptional = Optional.empty();
			when(envTestReviewRepo.findById(anyInt())).thenReturn(envTestOptional);
			EnvTestReview res = envTestReviewService.getEnvTestReviewById(0);
			assertEquals(DataNotFoundException.class, res.getClass());
		}, "Test failed due to an exception: getEnvTestReviewByIdDataNotFoundExceptionTest {}");
		logger.info("EnvTestReviewServiceTest.getEnvTestReviewByIdDataNotFoundExceptionTest : Finished");
	}
	
	@Test
	void saveEnvTestReviewValidTest() {
		logger.info("EnvTestReviewServiceTest.saveEnvTestReviewValidTest : Initiated");
		assertDoesNotThrow(() -> {
			EnvTestReviewDto envTestReviewDto = new EnvTestReviewDto();
			when(mapper.map(any(EnvTestReviewDto.class), eq(EnvTestReview.class))).thenReturn(new EnvTestReview());
			when(envTestReviewRepo.save(any())).thenReturn(new EnvTestReview());
			EnvTestReview res = envTestReviewService.saveEnvTestReview(envTestReviewDto);
			assertNotNull(res);
		}, "Test failed due to an exception: saveEnvTestReviewValidTest ");
		logger.info("EnvTestReviewServiceTest.saveEnvTestReviewValidTest : Finished");
	}
	
}
