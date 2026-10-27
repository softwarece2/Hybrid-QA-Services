package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.controller.VendorRatingController;
import in.co.bel.olms.transaction.dto.VendorRatingDto;
import in.co.bel.olms.transaction.entity.VendorRating;
import in.co.bel.olms.transaction.service.VendorRatingService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class VendorRatingControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(VendorRatingControllerTest.class);
	
	@InjectMocks
	VendorRatingController vendorRatingController;
	
	@Mock
	VendorRatingService vendorRatingService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getVendorRatingByIdValidTest() {
		logger.info("VendorRatingControllerTest.getVendorRatingByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 24;
			when(vendorRatingService.getVendorRatingById(anyInt())).thenReturn(new VendorRating());
			when(mapper.map(any(VendorRating.class), eq(VendorRatingDto.class))).thenReturn(new VendorRatingDto());
			ResponseEntity<VendorRatingDto> result = vendorRatingController.getVendorRatingById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getVendorRatingByIdValidTest ");
		logger.info("VendorRatingControllerTest.getVendorRatingByIdValidTest : Finished");
	}
	
	@Test
	void saveVendorRatingValidTest() {
		logger.info("VendorRatingControllerTest.saveVendorRatingValidTest : Initiated");
		assertDoesNotThrow(() -> {
			VendorRatingDto vendorRatingDto = new VendorRatingDto();
			vendorRatingDto.setId(24);
			when(vendorRatingService.saveVendorRating(any(VendorRatingDto.class))).thenReturn(new VendorRating());
			ResponseEntity<String> result = vendorRatingController.saveVendorRating(vendorRatingDto);
			assertNotNull(result);
			assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: saveVendorRatingValidTest ");
		logger.info("VendorRatingControllerTest.saveVendorRatingValidTest : Finished");
	}
	
	@Test
	void saveVendorRatingDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("VendorRatingControllerTest.saveVendorRatingDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(vendorRatingService.saveVendorRating(any())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<String> result = vendorRatingController.saveVendorRating(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveVendorRatingDataNotValidExceptionTest ");
		logger.info("VendorRatingControllerTest.saveVendorRatingDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveVendorRatingExceptionTest() {
		logger.info("VendorRatingControllerTest.saveVendorRatingExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(vendorRatingService.saveVendorRating(any())).thenThrow(new NullPointerException());
			ResponseEntity<String> result = vendorRatingController.saveVendorRating(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveVendorRatingExceptionTest ");
		logger.info("VendorRatingControllerTest.saveVendorRatingExceptionTest : Finished");
	}
	
}
