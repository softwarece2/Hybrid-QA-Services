package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import in.co.bel.olms.transaction.controller.PoObservationController;
import in.co.bel.olms.transaction.dto.PoObservationDto;
import in.co.bel.olms.transaction.entity.PoObservation;
import in.co.bel.olms.transaction.service.so.PoObservationService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class PoObservationControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(PoObservationControllerTest.class);
	
	@InjectMocks
	PoObservationController poObservationController;
	
	@Mock
	PoObservationService poObservationService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getPoObservationByIdValidTest() {
		logger.info("PoObservationControllerTest.getPoObservationByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 105;
			when(poObservationService.getPoObservationById(anyInt())).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(new PoObservationDto());
			ResponseEntity<PoObservationDto> result = poObservationController.getPoObservationById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getPoObservationByIdValidTest ");
		logger.info("PoObservationControllerTest.getPoObservationByIdValidTest : Finished");
	}
	
	@Test
	void createPoObservationValidTest() {
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.createPoObservation(any(PoObservationDto.class))).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<PoObservationDto> result = poObservationController.createPoObservation(poObservationDto);
			assertNotNull(result);
		}, "Test failed due to an exception: createPoObservationValidTest ");
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Finished");
	}
	
	@Test
	void createPoObservationDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.createPoObservation(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.createPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: createPoObservationValidTest ");
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Finished");
	}
	
	@Test
	void createPoObservationExceptionTest() {
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.createPoObservation(any())).thenThrow(new NullPointerException());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.createPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: createPoObservationValidTest ");
		logger.info("PoObservationControllerTest.createPoObservationValidTest : Finished");
	}
	
	
	@Test
	void saveRemActPoObservationValidTest() {
		logger.info("PoObservationControllerTest.saveRemActPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.saveRemActPoObservation(any(PoObservationDto.class))).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemActPoObservation(poObservationDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemActPoObservationValidTest ");
		logger.info("PoObservationControllerTest.saveRemActPoObservationValidTest : Finished");
	}
	
	@Test
	void saveRemActPoObservationDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.saveRemActPoObservationDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.saveRemActPoObservation(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemActPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemActPoObservationDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.saveRemActPoObservationValidTest : Finished");
	}
	
	@Test
	void saveRemActPoObservationExceptionTest() {
		logger.info("PoObservationControllerTest.saveRemActPoObservationExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.saveRemActPoObservation(any())).thenThrow(new NullPointerException());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemActPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemActPoObservationExceptionTest ");
		logger.info("PoObservationControllerTest.saveRemActPoObservationExceptionTest : Finished");
	}
	
	@Test
	void submitRemActPoObservationValidTest() {
		logger.info("PoObservationControllerTest.submitRemActPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.submitRemActPoObservation(any(PoObservationDto.class))).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemActPoObservation(poObservationDto);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemActPoObservationValidTest ");
		logger.info("PoObservationControllerTest.submitRemActPoObservationValidTest : Finished");
	}
	
	@Test
	void submitRemActPoObservationDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.submitRemActPoObservationDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.submitRemActPoObservation(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemActPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemActPoObservationDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.submitRemActPoObservationDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void submitRemActPoObservationExceptionTest() {
		logger.info("PoObservationControllerTest.submitRemActPoObservationExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.submitRemActPoObservation(any())).thenThrow(new NullPointerException());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemActPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemActPoObservationExceptionTest ");
		logger.info("PoObservationControllerTest.submitRemActPoObservationExceptionTest : Finished");
	}
	
	@Test
	void saveRemarkPoObservationValidTest() {
		logger.info("PoObservationControllerTest.saveRemarkPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.saveRemarkPoObservation(any(PoObservationDto.class))).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemarkPoObservation(poObservationDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemarkPoObservationValidTest ");
		logger.info("PoObservationControllerTest.saveRemarkPoObservationValidTest : Finished");
	}
	
	@Test
	void saveRemarkPoObservationDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.saveRemarkPoObservationDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.saveRemarkPoObservation(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemarkPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemarkPoObservationDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.saveRemarkPoObservationDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveRemarkPoObservationExceptionTest() {
		logger.info("PoObservationControllerTest.saveRemarkPoObservationExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.saveRemarkPoObservation(any())).thenThrow(new NullPointerException());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.saveRemarkPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: saveRemarkPoObservationExceptionTest ");
		logger.info("PoObservationControllerTest.saveRemarkPoObservationExceptionTest : Finished");
	}
	
	
	@Test
	void submitRemarkPoObservationValidTest() {
		logger.info("PoObservationControllerTest.submitRemarkPoObservationValidTest : Initiated");
		assertDoesNotThrow(() -> {
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.submitRemarkPoObservation(any(PoObservationDto.class))).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemarkPoObservation(poObservationDto);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemarkPoObservationValidTest ");
		logger.info("PoObservationControllerTest.submitRemarkPoObservationValidTest : Finished");
	}
	
	@Test
	void submitRemarkPoObservationDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.submitRemarkPoObservationDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.submitRemarkPoObservation(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemarkPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemarkPoObservationDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.submitRemarkPoObservationDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void submitRemarkPoObservationExceptionTest() {
		logger.info("PoObservationControllerTest.submitRemarkPoObservationExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.submitRemarkPoObservation(any())).thenThrow(new NullPointerException());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(null);
			ResponseEntity<PoObservationDto> result = poObservationController.submitRemarkPoObservation(null);
			assertNotNull(result);
		}, "Test failed due to an exception: submitRemarkPoObservationExceptionTest ");
		logger.info("PoObservationControllerTest.submitRemarkPoObservationExceptionTest : Finished");
	}
	
	@Test
	void getBySupplyOrderValidTest() {
		logger.info("PoObservationControllerTest.getBySupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			when(poObservationService.getPoObservationBySoId(anyInt())).thenReturn(new PoObservationDto());
			ResponseEntity<PoObservationDto> result = poObservationController.getBySupplyOrder(soId);
			assertNotNull(result);
		}, "Test failed due to an exception: getBySupplyOrderValidTest ");
		logger.info("PoObservationControllerTest.getBySupplyOrderValidTest : Finished");
	}
	
	@Test
	void getByOpaValidTest() {
		logger.info("PoObservationControllerTest.getByOpaValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			List<PoObservationDto> lPoObservationDto = new ArrayList<>();
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			lPoObservationDto.add(poObservationDto);
			lPoObservationDto.add(poObservationDto);
			when(poObservationService.getPoObservationByOpaId(anyInt())).thenReturn(lPoObservationDto);
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getByOpa(orgId);
			assertNotNull(result);
		}, "Test failed due to an exception: getByOpaValidTest ");
		logger.info("PoObservationControllerTest.getByOpaValidTest : Finished");
	}
	
	@Test
	void getByOpaDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.getByOpaDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.getPoObservationByOpaId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getByOpa(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getByOpaDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.getByOpaDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getByOpaExceptionTest() {
		logger.info("PoObservationControllerTest.getByOpaExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.getPoObservationByOpaId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getByOpa(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getByOpaExceptionTest ");
		logger.info("PoObservationControllerTest.getByOpaExceptionTest : Finished");
	}
	
	
	@Test
	void getBySalesGrpValidTest() {
		logger.info("PoObservationControllerTest.getBySalesGrpValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int salesGrpId = 1;
			List<PoObservationDto> lPoObservationDto = new ArrayList<>();
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			lPoObservationDto.add(poObservationDto);
			lPoObservationDto.add(poObservationDto);
			when(poObservationService.getPoObservationBySalesGrpId(anyInt())).thenReturn(lPoObservationDto);
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getBySalesGrp(salesGrpId);
			assertNotNull(result);
		}, "Test failed due to an exception: getBySalesGrpValidTest ");
		logger.info("PoObservationControllerTest.getBySalesGrpValidTest : Finished");
	}
	
	@Test
	void getBySalesGrpDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("PoObservationControllerTest.getBySalesGrpDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.getPoObservationBySalesGrpId(anyInt())).thenThrow(new DataNotValidException("Invalid Data"));
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getBySalesGrp(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getBySalesGrpDataNotValidExceptionTest ");
		logger.info("PoObservationControllerTest.getBySalesGrpDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void getBySalesGrpExceptionTest() {
		logger.info("PoObservationControllerTest.getBySalesGrpExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(poObservationService.getPoObservationBySalesGrpId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getBySalesGrp(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getBySalesGrpExceptionTest ");
		logger.info("PoObservationControllerTest.getBySalesGrpExceptionTest : Finished");
	}
	
	@Test
	void getByCqaeValidTest() {
		logger.info("PoObservationControllerTest.getByCqaeValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			List<PoObservationDto> lPoObservationDto = new ArrayList<>();
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			lPoObservationDto.add(poObservationDto);
			lPoObservationDto.add(poObservationDto);
			when(poObservationService.getByCqae(anyInt())).thenReturn(lPoObservationDto);
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getByCqae(orgId);
			assertNotNull(result);
		}, "Test failed due to an exception: getByCqaeValidTest ");
		logger.info("PoObservationControllerTest.getByCqaeValidTest : Finished");
	}
	
	@Test
	void getByEmpnSubValidTest() {
		logger.info("PoObservationControllerTest.getByEmpnSubValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 101;
			List<PoObservationDto> lPoObservationDto = new ArrayList<>();
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			lPoObservationDto.add(poObservationDto);
			lPoObservationDto.add(poObservationDto);
			when(poObservationService.getByEmpnSub(anyInt())).thenReturn(lPoObservationDto);
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getByEmpnSub(empId);
			assertNotNull(result);
		}, "Test failed due to an exception: getByEmpnSubValidTest ");
		logger.info("PoObservationControllerTest.getByEmpnSubValidTest : Finished");
	}
	
	@Test
	void getPoObservationByIdsValidTest() {
		logger.info("PoObservationControllerTest.getPoObservationByIdsValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<Integer> ids = Arrays.asList(12, 13);
			PoObservationDto poObservationDto = new PoObservationDto();
			poObservationDto.setId(105);
			when(poObservationService.getPoObservationById(anyInt())).thenReturn(new PoObservation());
			when(poObservationService.convertBoToDto(any(PoObservation.class))).thenReturn(poObservationDto);
			ResponseEntity<List<PoObservationDto>> result = poObservationController.getPoObservationById(ids);
			assertNotNull(result);
		}, "Test failed due to an exception: getPoObservationByIdsValidTest ");
		logger.info("PoObservationControllerTest.getPoObservationByIdsValidTest : Finished");
	}
	
}
