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

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.transaction.controller.SaleOrderController;
import in.co.bel.olms.transaction.dto.SaleOrderDto;
import in.co.bel.olms.transaction.entity.SaleOrder;
import in.co.bel.olms.transaction.service.so.SaleOrderService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SaleOrderControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SaleOrderControllerTest.class);
	
	@InjectMocks
	SaleOrderController saleOrderController;
	
	@Mock
	SaleOrderService saleOrderService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getAllSaleOrdersValidTest() {
		logger.info("SaleOrderControllerTest.getAllSaleOrdersValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<SaleOrder> lSaleOrders = new ArrayList<>();
			SaleOrder saleOrder = new SaleOrder();
			saleOrder.setId(101);
			lSaleOrders.add(saleOrder);
			lSaleOrders.add(saleOrder);
			when(saleOrderService.getAllSaleOrders()).thenReturn(lSaleOrders);
			when(mapper.map(any(SaleOrder.class), eq(SaleOrderDto.class))).thenReturn(new SaleOrderDto());
			ResponseEntity<List<SaleOrderDto>> res = saleOrderController.getAllSaleOrders();
			assertNotNull(res);
		}, "Test failed due to an exception: getAllSaleOrdersValidTest ");
		logger.info("SaleOrderControllerTest.getAllSaleOrdersValidTest : Finished");
	}
	
	@Test
	void getSaleOrdersBySoIdValidTest() {
		logger.info("SaleOrderControllerTest.getSaleOrdersBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			List<SaleOrder> lSaleOrders = new ArrayList<>();
			SaleOrder saleOrder = new SaleOrder();
			saleOrder.setId(101);
			lSaleOrders.add(saleOrder);
			lSaleOrders.add(saleOrder);
			when(saleOrderService.getSaleOrdersBySoId(anyInt())).thenReturn(lSaleOrders);
			when(mapper.map(any(SaleOrder.class), eq(SaleOrderDto.class))).thenReturn(new SaleOrderDto());
			ResponseEntity<List<SaleOrderDto>> res = saleOrderController.getSaleOrdersBySoId(soId);
			assertNotNull(res);
		}, "Test failed due to an exception: getSaleOrdersBySoIdValidTest ");
		logger.info("SaleOrderControllerTest.getSaleOrdersBySoIdValidTest : Finished");
	}	

}
