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
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.service.SupplyOrderService;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class SupplyOrderServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(SupplyOrderServiceTest.class);
	
	@InjectMocks
	SupplyOrderService supplyOrderService;
	
	@Mock
	SupplyOrderRepository supplyOrderRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveSupplyOrderValidTest() {
		logger.info("SupplyOrderServiceTest.saveSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			SupplyOrder supplyOrder = new SupplyOrder();
			supplyOrder.setId(12);
			when(mapper.map(any(SupplyOrderDto.class), eq(SupplyOrder.class))).thenReturn(supplyOrder);
			when(supplyOrderRepository.save(any(SupplyOrder.class))).thenReturn(supplyOrder);
			SupplyOrder result = supplyOrderService.saveSupplyOrder(supplyOrderDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveSupplyOrderValidTest ");
		logger.info("SupplyOrderServiceTest.saveSupplyOrderValidTest : Finished");
	}
	
	@Test
	void getSupplyOrderValidTest() {
		logger.info("SupplyOrderServiceTest.getSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 3;
			SupplyOrder supplyOrder = new SupplyOrder();
			supplyOrder.setId(12);
			Optional<SupplyOrder> optData = Optional.of(supplyOrder);
			when(supplyOrderRepository.findById(anyInt())).thenReturn(optData);
			SupplyOrder result = supplyOrderService.getSupplyOrder(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSupplyOrderValidTest ");
		logger.info("SupplyOrderServiceTest.getSupplyOrderValidTest : Finished");
	}
	
	@Test
	void getSupplyOrderValidSecScenarioTest() {
		logger.info("SupplyOrderServiceTest.getSupplyOrderValidSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			when(supplyOrderRepository.findById(anyInt())).thenReturn(Optional.empty());
			SupplyOrder result = supplyOrderService.getSupplyOrder(0);
			assertEquals(DataNotFoundException.class, result.getClass());
		}, "Test failed due to an exception: getSupplyOrderValidSecScenarioTest {}");
		logger.info("SupplyOrderServiceTest.getSupplyOrderValidSecScenarioTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersValidTest() {
		logger.info("SupplyOrderServiceTest.getAllSupplyOrdersValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<SupplyOrder> lSupplyOrder = new ArrayList<>();
			SupplyOrder supplyOrder = new SupplyOrder();
			supplyOrder.setId(12);
			lSupplyOrder.add(supplyOrder);
			lSupplyOrder.add(supplyOrder);
			when(supplyOrderRepository.findAll()).thenReturn(lSupplyOrder);
			List<SupplyOrder> result = supplyOrderService.getAllSupplyOrders();
			assertNotEquals("[]", result);
			assertNotNull(result);
		}, "Test failed due to an exception: getAllSupplyOrdersValidTest ");
		logger.info("SupplyOrderServiceTest.getAllSupplyOrdersValidTest : Finished");
	}
	
}
