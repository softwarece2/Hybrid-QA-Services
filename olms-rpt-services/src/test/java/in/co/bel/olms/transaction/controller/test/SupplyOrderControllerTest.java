package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import in.co.bel.olms.OlmsReportServicesApplication;
import in.co.bel.olms.transaction.controller.SupplyOrderController;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.service.SupplyOrderService;

@SpringBootTest
@ContextConfiguration(classes = OlmsReportServicesApplication.class)
class SupplyOrderControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SupplyOrderControllerTest.class);
	
	@InjectMocks
	SupplyOrderController supplyOrderController;
	
	@Mock
	SupplyOrderService supplyOrderService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getAllSupplyOrdersValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<SupplyOrder> lSupOrd = new ArrayList<>();
			SupplyOrder supOrd = new SupplyOrder();
			supOrd.setId(213);
			lSupOrd.add(supOrd);
			lSupOrd.add(supOrd);
			when(supplyOrderService.getAllSupplyOrders()).thenReturn(lSupOrd);
			when(mapper.map(any(SupplyOrder.class), eq(SupplyOrderDto.class))).thenReturn(new SupplyOrderDto());
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getAllSupplyOrders();
			assertNotNull(result);
		}, "Test failed due to an exception: getAllSupplyOrdersValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersValidTest : Finished");
	}
	
	@Test
	void getSupplyOrderValidTest() {
		logger.info("SupplyOrderControllerTest.getSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 213;
			when(supplyOrderService.getSupplyOrder(anyInt())).thenReturn(new SupplyOrder());
			when(mapper.map(any(SupplyOrder.class), eq(SupplyOrderDto.class))).thenReturn(new SupplyOrderDto());
			ResponseEntity<SupplyOrderDto> result = supplyOrderController.getSupplyOrder(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSupplyOrderValidTest ");
		logger.info("SupplyOrderControllerTest.getSupplyOrderValidTest : Finished");
	}
	
}
