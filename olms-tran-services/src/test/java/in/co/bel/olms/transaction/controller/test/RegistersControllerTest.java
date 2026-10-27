package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.transaction.controller.RegistersController;
import in.co.bel.olms.transaction.dto.INoteRegisterDto;
import in.co.bel.olms.transaction.dto.OLRegisterDto;
import in.co.bel.olms.transaction.dto.SORegisterDto;
import in.co.bel.olms.transaction.dto.SORegisterListDto;
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.service.RegistersService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class RegistersControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(RegistersControllerTest.class);
	
	@InjectMocks
	RegistersController registersController;
	
	@Mock
	RegistersService registersService;

	@Mock
	SalesGroupRepository salesGroupRepository;

	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getSORegisterValidTest() {
		logger.info("RegistersControllerTest.getSORegisterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<SupplyOrder> lSupplyOrders = new ArrayList<>();
			SupplyOrder supplyOrder = new SupplyOrder();
			SORegisterDto soRegisterDto = new SORegisterDto();
			Indenter indenter = new Indenter();
			Supplier supplier = new Supplier();
			indenter.setName("AAGL");
			supplier.setName("BEL SOFTWARE SBU");
		    supplyOrder.setIndenter(indenter);
		    supplyOrder.setSupplier(supplier);
		    supplyOrder.setDescription("SO 2103 created with file no IS-000031 on 21-03-2023");
		    soRegisterDto.setValue(1040000.00);
		    lSupplyOrders.add(supplyOrder);
		    lSupplyOrders.add(supplyOrder);
		    when(registersService.getSORegister(anyInt(), anyString(), anyString())).thenReturn(lSupplyOrders);
		    when(mapper.map(any(SupplyOrder.class), eq(SORegisterDto.class))).thenReturn(soRegisterDto);
		    ResponseEntity<SORegisterListDto> result = registersController.getSORegister(orgId, fromDate, toDate);
		    assertNotNull(result);
		    assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: getSORegisterValidTest ");
		logger.info("RegistersControllerTest.getSORegisterValidTest : Finished");
	}
	
	@Test
	void getSORegisterExceptionTest() throws NullPointerException {
		logger.info("RegistersControllerTest.getSORegisterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
		    when(registersService.getSORegister(anyInt(), anyString(), anyString())).thenThrow(new NullPointerException());
		    when(mapper.map(any(SupplyOrder.class), eq(SORegisterDto.class))).thenReturn(null);
		    ResponseEntity<SORegisterListDto> result = registersController.getSORegister(0, null, null);
		    assertNotNull(result);
		}, "Test failed due to an exception: getSORegisterExceptionTest ");
		logger.info("RegistersControllerTest.getSORegisterExceptionTest : Finished");
	}
	
	
	@Test
	void getOLRegisterValidTest() {
		logger.info("RegistersControllerTest.getOLRegisterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> offerLetters = Arrays.asList(
					new Object[] {"OL BB1", new Date(), "Ind123", new Date(), new Date(), "VRSAM_MAIN EQUIPMENT", 12, 138, 1040000.00, "2026-10-15"},
					new Object[] {"OL BB1", new Date(), "Ind123", new Date(), new Date(), "VRSAM_MAIN EQUIPMENT", 12, 138, 1040000.00, "2026-10-15"}
					);
			when(registersService.getOLRegister(anyInt(), anyString(), anyString())).thenReturn(offerLetters);
			ResponseEntity<List<OLRegisterDto>> result = registersController.getOLRegister(orgId, fromDate, toDate);
			assertNotNull(result);
			assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: getOLRegisterValidTest ");
		logger.info("RegistersControllerTest.getOLRegisterValidTest : Finished");
	}
	
	@Test
	void getOLRegisterNullValuesTest() {
		logger.info("RegistersControllerTest.getOLRegisterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> offerLetters = Arrays.asList(
					new Object[] {null, null, null, null, null, null, null, null, null, null},
					new Object[] {null, null, null, null, null, null, null, null, null, null}
					);
			when(registersService.getOLRegister(anyInt(), anyString(), anyString())).thenReturn(offerLetters);
			ResponseEntity<List<OLRegisterDto>> result = registersController.getOLRegister(orgId, fromDate, toDate);
			assertNotNull(result);
			assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: getOLRegisterValidTest ");
		logger.info("RegistersControllerTest.getOLRegisterValidTest : Finished");
	}
	
	@Test
	void getOLRegisterExceptionTest() {
		logger.info("RegistersControllerTest.getOLRegisterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(registersService.getOLRegister(anyInt(), anyString(), anyString())).thenThrow(new NullPointerException());
			ResponseEntity<List<OLRegisterDto>> result = registersController.getOLRegister(0, null, null);
			assertNotNull(result);
		}, "Test failed due to an exception: getOLRegisterExceptionTest ");
		logger.info("RegistersControllerTest.getOLRegisterExceptionTest : Finished");
	}
	
	@Test
	void getSurpriseCheckRegisterValidTest() {
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> offerLetters = Arrays.asList(
					new Object[] {"2023-08-26", "MCE", "Demo Line 11", "Material units 1", "New SO 2", "Indi Resp", "Sample tests carried", "Vijayakumar", "2023-08-27", "Verification Done"},
					new Object[] {"2023-08-26", "MCE", "Demo Line 11", "Material units 1", "New SO 2", "Indi Resp", "Sample tests carried", "Vijayakumar", "2023-08-27", "Verification Done"}
					);
			when(registersService.getSurpriseCheckRegister(anyInt(), anyString(), anyString())).thenReturn(offerLetters);
			ResponseEntity<List<SurpriseCheckDto>> result = registersController.getSurpriseCheckRegister(orgId, fromDate, toDate);
			assertNotNull(result);
		}, "Test failed due to an exception: getSurpriseCheckRegisterValidTest ");
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterValidTest : Finished");
	}
	
	@Test
	void getSurpriseCheckRegisterNullValueTest() {
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterNullValueTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> offerLetters = Arrays.asList(
					new Object[] {null, null, null, null, null, null, null, null, null, null},
					new Object[] {null, null, null, null, null, null, null, null, null, null}
					);
			when(registersService.getSurpriseCheckRegister(anyInt(), anyString(), anyString())).thenReturn(offerLetters);
			ResponseEntity<List<SurpriseCheckDto>> result = registersController.getSurpriseCheckRegister(orgId, fromDate, toDate);
			assertNotNull(result);
		}, "Test failed due to an exception: getSurpriseCheckRegisterNullValueTest ");
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterNullValueTest : Finished");
	}
	
	@Test
	void getSurpriseCheckRegisterExceptionTest() {
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(registersService.getSurpriseCheckRegister(anyInt(), anyString(), anyString())).thenThrow(new NullPointerException());
			ResponseEntity<List<SurpriseCheckDto>> result = registersController.getSurpriseCheckRegister(0, null, null);
			assertNotNull(result);
		}, "Test failed due to an exception: getSurpriseCheckRegisterExceptionTest ");
		logger.info("RegistersControllerTest.getSurpriseCheckRegisterExceptionTest : Finished");
	}
	
	@Test
	void getInoteRegisterValidTest() {
		logger.info("RegistersControllerTest.getInoteRegisterValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> inotes = Arrays.asList(
					new Object[] {"000802002N10", "MCE", 1, "2023-04-24", "0405 SO", "2023-04-24", "AAGL", "OL BB1", "2023-12-20", "IS-000019", "2026-10-15", "Part Supply", 12.00, 280.00, 20, 30.00, "Sample stores Description"},
					new Object[] {"000802002N10", "MCE", 1, "2023-04-24", "0405 SO", "2023-04-24", "AAGL", "OL BB1", "2023-12-20", "IS-000019", "2026-10-15", "Part Supply", 12.00, 280.00, 20, 30.00, "Sample stores Description"}
			);
			List<Object[]> salesGrpList = Arrays.asList(
					new Object[] {1, "AAGL"},
					new Object[] {2, "AAGL"}
					);
			when(registersService.getINoteRegister(anyInt(), anyString(), anyString())).thenReturn(inotes);
			when(salesGroupRepository.findByStatus()).thenReturn(salesGrpList);
			ResponseEntity<INoteRegisterDto> result = registersController.getInoteRegister(orgId, fromDate, toDate);
			assertNotNull(result);
			assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: getInoteRegisterValidTest ");
		logger.info("RegistersControllerTest.getInoteRegisterValidTest : Finished");
	}
	
	@Test
	void getInoteRegisterNullValuesTest() {
		logger.info("RegistersControllerTest.getInoteRegisterNullValuesTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			String fromDate = "2022-03-22";
			String toDate = "2023-05-12";
			List<Object[]> inotes = Arrays.asList(
					new Object[] {null, null, 1, null, null, null, null, null, null, null, null, null, 12.00, 280.00, null, 30.00, null},
					new Object[] {null, null, 1, null, null, null, null, null, null, null, null, null, 12.00, 280.00, null, 30.00, null}
			);
			List<Object[]> salesGrpList = Arrays.asList(
					new Object[] {1, "AAGL"},
					new Object[] {2, "AAGL"}
					);
			when(registersService.getINoteRegister(anyInt(), anyString(), anyString())).thenReturn(inotes);
			when(salesGroupRepository.findByStatus()).thenReturn(salesGrpList);
			ResponseEntity<INoteRegisterDto> result = registersController.getInoteRegister(orgId, fromDate, toDate);
			assertNotNull(result);
			assertNotEquals(Exception.class, result.getClass());
		}, "Test failed due to an exception: getInoteRegisterNullValuesTest ");
		logger.info("RegistersControllerTest.getInoteRegisterNullValuesTest : Finished");
	}
	
	@Test
	void getInoteRegisterExceptionTest() {
		logger.info("RegistersControllerTest.getInoteRegisterExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(registersService.getINoteRegister(anyInt(), anyString(), anyString())).thenThrow(new NullPointerException());
			when(salesGroupRepository.findByStatus()).thenReturn(null);
			ResponseEntity<INoteRegisterDto> result = registersController.getInoteRegister(0, null, null);
			assertNotNull(result);
		}, "Test failed due to an exception: getInoteRegisterExceptionTest ");
		logger.info("RegistersControllerTest.getInoteRegisterExceptionTest : Finished");
	}
	
}
