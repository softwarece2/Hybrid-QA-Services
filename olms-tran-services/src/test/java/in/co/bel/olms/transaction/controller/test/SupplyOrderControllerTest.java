package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.master.dto.ConsigneeDto;
import in.co.bel.olms.master.dto.KeyValuePair;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.transaction.controller.SupplyOrderController;
import in.co.bel.olms.transaction.dto.SoAttachmentDetailDto;
import in.co.bel.olms.transaction.dto.SoAttachmentDto;
import in.co.bel.olms.transaction.dto.SoTreeViewDto;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import in.co.bel.olms.transaction.dto.SupplyOrderInputDto;
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.entity.SOLineItem;
import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import in.co.bel.olms.transaction.service.check.NotifyUtil;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class SupplyOrderControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(SupplyOrderControllerTest.class);
	
	@InjectMocks
	SupplyOrderController supplyOrderController;
	
	@Mock
	NotifyUtil notifyUtil;
	
	@Mock
	SupplyOrderService supplyOrderService;
	
	@Mock
	EmployeeRepository employeeRepository;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void getAllSupplyOrdersValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersValidTest : Initiated");
		assertDoesNotThrow(() -> {
			List<SupplyOrder> lSupplyOrder = new ArrayList<>();
			SupplyOrder supplyOrder = new SupplyOrder();
			List<Consignee> lConsignee = new ArrayList<>();
			Consignee consignee = new Consignee();
			consignee.setId(2);
			consignee.setName("SBC-Krishna5");
			lConsignee.add(consignee);
			lConsignee.add(consignee);
			supplyOrder.setSoConsigneeMappings(lConsignee);
			lSupplyOrder.add(supplyOrder);
			lSupplyOrder.add(supplyOrder);
			when(supplyOrderService.getAllSupplyOrders()).thenReturn(lSupplyOrder);
			when(mapper.map(any(SupplyOrder.class), eq(SupplyOrderDto.class))).thenReturn(new SupplyOrderDto());
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getAllSupplyOrders();
			assertNotNull(result);
		}, "Test failed due to an exception: getAllSupplyOrdersValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersValidTest : Finished");
	}
	
	@Test
	void saveSupplyOrderValidTest() {
		logger.info("SupplyOrderControllerTest.saveSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> response = new HashMap<>();
			SupplyOrderInputDto supplyOrderInputDto = new SupplyOrderInputDto();
			supplyOrderInputDto.setOrgId(2);
			String resp = gson.toJson(supplyOrderInputDto);
			response.put(true, resp);
			when(supplyOrderService.saveSupplyOrders(any(SupplyOrderInputDto.class))).thenReturn(response);
			ResponseEntity<String> result = supplyOrderController.saveSupplyOrder(supplyOrderInputDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveSupplyOrderValidTest ");
		logger.info("SupplyOrderControllerTest.saveSupplyOrderValidTest : Finished");
	}
	
	@Test
	void saveSupplyOrderSecScenarioTest() {
		logger.info("SupplyOrderControllerTest.saveSupplyOrderSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			Map<Boolean, String> response = new HashMap<>();
			SupplyOrderInputDto supplyOrderInputDto = new SupplyOrderInputDto();
			supplyOrderInputDto.setOrgId(2);
			response.put(false, "Invalid Data");
			when(supplyOrderService.saveSupplyOrders(any(SupplyOrderInputDto.class))).thenReturn(response);
			ResponseEntity<String> result = supplyOrderController.saveSupplyOrder(supplyOrderInputDto);
			assertNotNull(result);
		}, "Test failed due to an exception: saveSupplyOrderSecScenarioTest ");
		logger.info("SupplyOrderControllerTest.saveSupplyOrderSecScenarioTest : Finished");
	}
	
	@Test
	void deleteSupplyOrderValidTest() {
		logger.info("SupplyOrderControllerTest.deleteSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			when(supplyOrderService.deleteSupplyOrder(anyInt())).thenReturn(true);
			ResponseEntity<Boolean> result = supplyOrderController.deleteSupplyOrder(soId);
			assertNotNull(result);
		}, "Test failed due to an exception: deleteSupplyOrderValidTest ");
		logger.info("SupplyOrderControllerTest.deleteSupplyOrderValidTest : Finished");
	}
	
	@Test
	void deleteSupplyOrderSecScenarioTest() {
		logger.info("SupplyOrderControllerTest.deleteSupplyOrderSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			when(supplyOrderService.deleteSupplyOrder(anyInt())).thenReturn(false);
			ResponseEntity<Boolean> result = supplyOrderController.deleteSupplyOrder(soId);
			assertNotNull(result);
		}, "Test failed due to an exception: deleteSupplyOrderSecScenarioTest ");
		logger.info("SupplyOrderControllerTest.deleteSupplyOrderSecScenarioTest : Finished");
	}
	
	@Test
	void getSupplyOrderValidTest() {
		logger.info("SupplyOrderControllerTest.getSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 223;
			SupplyOrder supplyOrder = new SupplyOrder();
			List<Consignee> lConsignee = new ArrayList<>();
			Consignee consignee = new Consignee();
			consignee.setId(2);
			consignee.setName("SBC-Krishna5");
			lConsignee.add(consignee);
			lConsignee.add(consignee);
			supplyOrder.setSoConsigneeMappings(lConsignee);
			when(supplyOrderService.getSupplyOrder(anyInt())).thenReturn(supplyOrder);
			when(mapper.map(any(SupplyOrder.class), eq(SupplyOrderDto.class))).thenReturn(new SupplyOrderDto());
			ResponseEntity<SupplyOrderDto> res = supplyOrderController.getSupplyOrder(id);
			assertNotNull(res);
		}, "Test failed due to an exception: getSupplyOrderValidTest ");
		logger.info("SupplyOrderControllerTest.getSupplyOrderValidTest : Finished");
	}
	
	@Test
	void getSupplyOrderByCqaeIdValidTest() {
		logger.info("SupplyOrderControllerTest.getSupplyOrderByCqaeIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 224;
			List<SupplyOrderDto> lSoDto = new ArrayList<>();
			SupplyOrderDto soDto = new SupplyOrderDto();
			soDto.setId(id);
			lSoDto.add(soDto);
			lSoDto.add(soDto);
			when(supplyOrderService.getSupplyOrderByCqaeId(anyInt())).thenReturn(lSoDto);
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getSupplyOrderByCqaeId(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSupplyOrderByCqaeIdValidTest ");
		logger.info("SupplyOrderControllerTest.getSupplyOrderByCqaeIdValidTest : Finished");
	}
	
	@Test
	void getSupplyOrderByIdValidTest() {
		logger.info("SupplyOrderControllerTest.getSupplyOrderByIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int id = 123;
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			supplyOrderDto.setId(id);
			when(supplyOrderService.getSupplyOrderById(anyInt())).thenReturn(supplyOrderDto);
			ResponseEntity<SupplyOrderDto> result = supplyOrderController.getSupplyOrderById(id);
			assertNotNull(result);
		}, "Test failed due to an exception: getSupplyOrderByIdValidTest ");
		logger.info("SupplyOrderControllerTest.getSupplyOrderByIdValidTest : Finished");
	}
	
	@Test
	void updateSupplyOrderValidTest() {
		logger.info("SupplyOrderControllerTest.updateSupplyOrderValidTest : Initiated");
		assertDoesNotThrow(() -> {
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			when(supplyOrderService.updateSupplyOrder(any(SupplyOrderDto.class))).thenReturn(true);
			ResponseEntity<?> res = supplyOrderController.updateSupplyOrder(supplyOrderDto);
			assertNotNull(res);
		}, "Test failed due to an exception: updateSupplyOrderValidTest ");
		logger.info("SupplyOrderControllerTest.updateSupplyOrderValidTest : Finished");
	}
	
	@Test
	void updateSupplyOrderSecScenarioTest() {
		logger.info("SupplyOrderControllerTest.updateSupplyOrderSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			when(supplyOrderService.updateSupplyOrder(any(SupplyOrderDto.class))).thenReturn(false);
			ResponseEntity<?> res = supplyOrderController.updateSupplyOrder(supplyOrderDto);
			assertNotNull(res);
		}, "Test failed due to an exception: updateSupplyOrderSecScenarioTest ");
		logger.info("SupplyOrderControllerTest.updateSupplyOrderSecScenarioTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersByOrgIdValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByOrgIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 2;
			List<SupplyOrderDto> lSOrderDto = new ArrayList<>();
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			lSOrderDto.add(sOrderDto);
			lSOrderDto.add(sOrderDto);
			when(supplyOrderService.getAllSupplyOrdersByOrgId(anyInt())).thenReturn(lSOrderDto);
			ResponseEntity<List<SupplyOrderDto>> res = supplyOrderController.getAllSupplyOrdersByOrgId(orgId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllSupplyOrdersByOrgIdValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByOrgIdValidTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersByOrgIdOptimizedValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByOrgIdOptimizedValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 2;
			List<SupplyOrderDto> lSOrderDto = new ArrayList<>();
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			lSOrderDto.add(sOrderDto);
			lSOrderDto.add(sOrderDto);
			when(supplyOrderService.getAllNotifiedSupplyOrdersByOrgIdOptimized(anyInt())).thenReturn(lSOrderDto);
			ResponseEntity<List<SupplyOrderDto>> res = supplyOrderController.getAllSupplyOrdersByOrgIdOptimized(orgId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllSupplyOrdersByOrgIdOptimizedValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByOrgIdOptimizedValidTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersByEmpIdValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByEmpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 19;
			List<SupplyOrderDto> lSOrderDto = new ArrayList<>();
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			lSOrderDto.add(sOrderDto);
			lSOrderDto.add(sOrderDto);
			when(supplyOrderService.getAllSupplyOrdersByEmpId(anyInt())).thenReturn(lSOrderDto);
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getAllSupplyOrdersByEmpId(empId);
			assertNotNull(result);
		}, "Test failed due to an exception: getAllSupplyOrdersByEmpIdValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersByEmpIdValidTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersBySalesGrpValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 19;
			List<SupplyOrderDto> lSOrderDto = new ArrayList<>();
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			lSOrderDto.add(sOrderDto);
			lSOrderDto.add(sOrderDto);
			when(supplyOrderService.getAllSupplyOrdersBySalesGrp(anyInt())).thenReturn(lSOrderDto);
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getAllSupplyOrdersBySalesGrp(empId);
			assertNotNull(result);
		}, "Test failed due to an exception: getAllSupplyOrdersBySalesGrpValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpValidTest : Finished");
	}
	
	@Test
	void updateSoAttachmentsValidTest() {
		logger.info("SupplyOrderControllerTest.updateSoAttachmentsValidTest : Initiated");
		assertDoesNotThrow(() -> {
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			MockMultipartFile qapFiles = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile drawing = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile atp = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile others = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile amendments = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile po = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile itemLots = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile dd = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MultipartFile[] qapFile = {qapFiles};
			MultipartFile[] drawings = {drawing};
			MultipartFile[] atps = {atp};
			MultipartFile[] other = {others};
			MultipartFile[] amendment = {amendments};
			MultipartFile[] pos = {po};
			MultipartFile[] itemLot = {itemLots};
			MultipartFile[] ddd = {dd};
			supplyOrderDto.setId(223);
			String resp = gson.toJson(supplyOrderDto);
			when(supplyOrderService.updateSoAttachments(any(SupplyOrderDto.class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class))).thenReturn(supplyOrderDto);
			ResponseEntity<SupplyOrderDto> result = supplyOrderController.updateSoAttachments(resp, qapFile, drawings, atps, other, amendment, pos, itemLot, ddd);
			assertNotNull(result);
		}, "Test failed due to an exception: updateSoAttachmentsValidTest ");
		logger.info("SupplyOrderControllerTest.updateSoAttachmentsValidTest : Finished");
	}
	
	@Test
	void updateSoAttachmentsSecScenarioTest() {
		logger.info("SupplyOrderControllerTest.updateSoAttachmentsSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			SupplyOrderDto supplyOrderDto = new SupplyOrderDto();
			MockMultipartFile qapFiles = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile drawing = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile atp = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile others = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile amendments = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile po = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile itemLots = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MockMultipartFile dd = new MockMultipartFile("data", "filename.pdf", "text/plain", "File Upload".getBytes());
			MultipartFile[] qapFile = {qapFiles};
			MultipartFile[] drawings = {drawing};
			MultipartFile[] atps = {atp};
			MultipartFile[] other = {others};
			MultipartFile[] amendment = {amendments};
			MultipartFile[] pos = {po};
			MultipartFile[] itemLot = {itemLots};
			MultipartFile[] ddd = {dd};
			supplyOrderDto.setId(223);
			String resp = gson.toJson(supplyOrderDto);
			when(supplyOrderService.updateSoAttachments(any(SupplyOrderDto.class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class), any(MultipartFile[].class))).thenReturn(supplyOrderDto);
			ResponseEntity<SupplyOrderDto> result = supplyOrderController.updateSoAttachments(resp, qapFile, drawings, atps, other, amendment, pos, itemLot, ddd);
			assertNotNull(result);
		}, "Test failed due to an exception: updateSoAttachmentsSecScenarioTest ");
		logger.info("SupplyOrderControllerTest.updateSoAttachmentsSecScenarioTest : Finished");
	}
	
	
	
	@Test
	void getAllotedSupplyOrdersValidTest() {
		logger.info("SupplyOrderControllerTest.getAllotedSupplyOrdersValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			int empId = 2;
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			when(supplyOrderService.getAllotedSupplyOrders(anyInt())).thenReturn(new ArrayList<>());
			ResponseEntity<List<SupplyOrderDto>> result = supplyOrderController.getAllotedSupplyOrders(soId, empId);
			assertNotNull(result);
		}, "Test failed due to an exception: getAllotedSupplyOrdersValidTest ");
		logger.info("SupplyOrderControllerTest.getAllotedSupplyOrdersValidTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersBySalesGrpIdValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			int salesGrpId = 223;
			List<SoTreeViewDto> soTreeViewDtos = new ArrayList<>();
			SoTreeViewDto soTreeViewDto = new SoTreeViewDto();
			soTreeViewDto.setId(123);
			soTreeViewDtos.add(soTreeViewDto);
			soTreeViewDtos.add(soTreeViewDto);
			when(supplyOrderService.getAllSOBySalesGrpIdOrSoID(anyInt(), anyInt())).thenReturn(soTreeViewDtos);
			ResponseEntity<List<SoTreeViewDto>> res = supplyOrderController.getAllSupplyOrdersBySalesGrpId(soId, salesGrpId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllSupplyOrdersBySalesGrpIdValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpIdValidTest : Finished");
	}
	
	@Test
	void getOverallTreeViewValidTest() {
		logger.info("SupplyOrderControllerTest.getOverallTreeViewValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int supplyOrderId = 123;
			List<SoTreeViewDto> soTreeViewDtos = new ArrayList<>();
			SoTreeViewDto soTreeViewDto = new SoTreeViewDto();
			soTreeViewDto.setId(123);
			soTreeViewDtos.add(soTreeViewDto);
			soTreeViewDtos.add(soTreeViewDto);
			when(supplyOrderService.getOverallTreeView(anyInt())).thenReturn(soTreeViewDtos);
			ResponseEntity<List<SoTreeViewDto>> result = supplyOrderController.getOverallTreeView(supplyOrderId);
			assertNotNull(result);
		}, "Test failed due to an exception: getOverallTreeViewValidTest ");
		logger.info("SupplyOrderControllerTest.getOverallTreeViewValidTest : Finished");
	}
	
	@Test
	void getConsigneesBySoIdValidTest() {
		logger.info("SupplyOrderControllerTest.getConsigneesBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int supplyOrderId = 123;
			List<ConsigneeDto> consigneeDtos = new ArrayList<>();
			ConsigneeDto consigneeDto = new ConsigneeDto();
			consigneeDto.setActive(true);
			consigneeDtos.add(consigneeDto);
			consigneeDtos.add(consigneeDto);
			when(supplyOrderService.getConsigneesBySoId(anyInt())).thenReturn(consigneeDtos);
			ResponseEntity<List<ConsigneeDto>> result = supplyOrderController.getConsigneesBySoId(supplyOrderId);
			assertNotNull(result);
		}, "Test failed due to an exception: getConsigneesBySoIdValidTest ");
		logger.info("SupplyOrderControllerTest.getConsigneesBySoIdValidTest : Finished");
	}
	
	@Test
	void deleteSoAttachmentValidTest() {
		logger.info("SupplyOrderControllerTest.deleteSoAttachmentValidTest : Initiated");
		assertDoesNotThrow(() -> {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId(12);
			keyValuePair.setName("name");
			String respString = gson.toJson(keyValuePair);
			when(supplyOrderService.deleteSoAttachment(anyInt(), anyString())).thenReturn("success");
			ResponseEntity<String> result = supplyOrderController.deleteSoAttachment(respString);
			assertNotNull(result);
		}, "Test failed due to an exception: deleteSoAttachmentValidTest ");
		logger.info("SupplyOrderControllerTest.deleteSoAttachmentValidTest : Finished");
	}
	
	@Test
	void deleteSoAttachmentSecScenarioTest() {
		logger.info("SupplyOrderControllerTest.deleteSoAttachmentSecScenarioTest : Initiated");
		assertDoesNotThrow(() -> {
			KeyValuePair keyValuePair = new KeyValuePair();
			keyValuePair.setId(12);
			keyValuePair.setName("name");
			String respString = gson.toJson(keyValuePair);
			when(supplyOrderService.deleteSoAttachment(anyInt(), anyString())).thenReturn("failure");
			ResponseEntity<String> result = supplyOrderController.deleteSoAttachment(respString);
			assertNotNull(result);
		}, "Test failed due to an exception: deleteSoAttachmentSecScenarioTest ");
		logger.info("SupplyOrderControllerTest.deleteSoAttachmentSecScenarioTest : Finished");
	}
	
	@Test
	void getAllSupplyOrdersBySalesGrpOptimizedValidTest() {
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpOptimizedValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int empId = 101;
			List<SupplyOrderDto> supplyOrderDtos = new ArrayList<>();
			SupplyOrderDto sOrderDto = new SupplyOrderDto();
			sOrderDto.setId(223);
			supplyOrderDtos.add(sOrderDto);
			supplyOrderDtos.add(sOrderDto);
			when(supplyOrderService.getAllSupplyOrdersBySalesGrpOptimized(anyInt(),anyList())).thenReturn(supplyOrderDtos);
			ResponseEntity<List<SupplyOrderDto>> res = supplyOrderController.getAllSupplyOrdersBySalesGrpOptimized(empId,anyList());
			assertNotNull(res);
		}, "Test failed due to an exception: getAllSupplyOrdersBySalesGrpOptimizedValidTest ");
		logger.info("SupplyOrderControllerTest.getAllSupplyOrdersBySalesGrpOptimizedValidTest : Finished");
	}
	
	@Test
	void getAttachmentBySoIdValidTest() {
		logger.info("SupplyOrderControllerTest.getAttachmentBySoIdValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			SoAttachmentDto soAttachmentDto = new SoAttachmentDto();
			soAttachmentDto.setQapFiles(Arrays.asList(any(SoAttachmentDetailDto.class)));
			when(supplyOrderService.getAttachmentsBySoId(anyInt())).thenReturn(soAttachmentDto);
			ResponseEntity<SoAttachmentDto> result = supplyOrderController.getAttachmentBySoId(soId);
			assertNotNull(result);
		}, "Test failed due to an exception: getAttachmentBySoIdValidTest ");
		logger.info("SupplyOrderControllerTest.getAttachmentBySoIdValidTest : Finished");
	}
	
	@Test
	void getAttachmentBySoIdExceptionTest() {
		logger.info("SupplyOrderControllerTest.getAttachmentBySoIdExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(supplyOrderService.getAttachmentsBySoId(anyInt())).thenThrow(new NullPointerException());
			ResponseEntity<SoAttachmentDto> result = supplyOrderController.getAttachmentBySoId(0);
			assertNotNull(result);
		}, "Test failed due to an exception: getAttachmentBySoIdExceptionTest ");
		logger.info("SupplyOrderControllerTest.getAttachmentBySoIdExceptionTest : Finished");
	}
	
	@Test
	void getAllFileNosValidTest() {
		logger.info("SupplyOrderControllerTest.getAllFileNosValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			List<Object[]> supplyOrderList = Arrays.asList(
					new Object[] {"100.00", "SupplyFile1"},
					new Object[] {"200.00", "SupplyFile2"}
					);
			when(supplyOrderService.getAllFileNos(anyInt())).thenReturn(supplyOrderList);
			ResponseEntity<List<KeyValuePair>> res = supplyOrderController.getAllFileNos(orgId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllFileNosValidTest ");
		logger.info("SupplyOrderControllerTest.getAllFileNosValidTest : Finished");
	}
	
	@Test
	void getAllFileNosNullValueTest() {
		logger.info("SupplyOrderControllerTest.getAllFileNosValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			when(supplyOrderService.getAllFileNos(anyInt())).thenReturn(null);
			ResponseEntity<List<KeyValuePair>> res = supplyOrderController.getAllFileNos(orgId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllFileNosValidTest ");
		logger.info("SupplyOrderControllerTest.getAllFileNosValidTest : Finished");
	}
	
	@Test
	void getAllFileNosExceptionTest() {
		logger.info("SupplyOrderControllerTest.getAllFileNosExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			int orgId = 100303;
			when(supplyOrderService.getAllFileNos(anyInt())).thenThrow(new Exception());
			ResponseEntity<List<KeyValuePair>> res = supplyOrderController.getAllFileNos(orgId);
			assertNotNull(res);
		}, "Test failed due to an exception: getAllFileNosExceptionTest ");
		logger.info("SupplyOrderControllerTest.getAllFileNosExceptionTest : Finished");
	}
	
	@Test
	void getSODetailsForSurpriseCheckValidTest() {
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckValidTest : Initiated");
		assertDoesNotThrow(() -> {
			int supplyOrderId = 223;
			SupplyOrder supplyOrder = new SupplyOrder();
			List<SOLineItem> soLineItemList = new ArrayList<>();
			List<Object[]> supplyOrderObjList = Arrays.asList(
					new Object[] {"MCE", "BEL Bg Cx", "Ms Bharat Electronics Limited, Jalahalli P.O., Bangalore 560 013", "New SO 2", "2023-01-02", "SO Description"},
					new Object[] {"MCE", "BEL Bg Cx", "Ms Bharat Electronics Limited, Jalahalli P.O., Bangalore 560 013", "New SO 2", "2023-01-02", "SO Description"}
					);
			SOLineItem soLineItem = new SOLineItem();
			soLineItem.setId(656);
			soLineItem.setLineItemNo("Demo Line 11");
			soLineItem.setDescription("Material units 1");
			soLineItemList.add(soLineItem);
			soLineItemList.add(soLineItem);
			supplyOrder.setSoLineItems(soLineItemList);
			when(supplyOrderService.getSupplyOrder(anyInt())).thenReturn(supplyOrder);
			when(supplyOrderService.getSODetailsForSurpriseCheck(anyInt())).thenReturn(supplyOrderObjList);
			ResponseEntity<SurpriseCheckDto> res = supplyOrderController.getSODetailsForSurpriseCheck(supplyOrderId);
			assertNotNull(res);
		}, "Test failed due to an exception: getSODetailsForSurpriseCheckValidTest ");
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckValidTest : Finished");
	}
	
	@Test
	void getSODetailsForSurpriseCheckNullValueTest() {
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckNullValueTest : Initiated");
		assertDoesNotThrow(() -> {
			int supplyOrderId = 223;
			SupplyOrder supplyOrder = new SupplyOrder();
			List<SOLineItem> soLineItemList = new ArrayList<>();
			List<Object[]> supplyOrderObjList = Arrays.asList(
					new Object[] {null, null, null, null, null, null},
					new Object[] {null, null, null, null, null, null}
					);
			SOLineItem soLineItem = new SOLineItem();
			soLineItem.setId(656);
			soLineItem.setLineItemNo("Demo Line 11");
			soLineItem.setDescription("Material units 1");
			soLineItemList.add(soLineItem);
			soLineItemList.add(soLineItem);
			supplyOrder.setSoLineItems(soLineItemList);
			when(supplyOrderService.getSupplyOrder(anyInt())).thenReturn(supplyOrder);
			when(supplyOrderService.getSODetailsForSurpriseCheck(anyInt())).thenReturn(supplyOrderObjList);
			ResponseEntity<SurpriseCheckDto> res = supplyOrderController.getSODetailsForSurpriseCheck(supplyOrderId);
			assertNotNull(res);
		}, "Test failed due to an exception: getSODetailsForSurpriseCheckNullValueTest ");
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckNullValueTest : Finished");
	}
	
	@Test
	void getSODetailsForSurpriseCheckExceptionTest() {
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			when(supplyOrderService.getSupplyOrder(anyInt())).thenThrow(new NullPointerException());
			when(supplyOrderService.getSODetailsForSurpriseCheck(anyInt())).thenReturn(null);
			ResponseEntity<SurpriseCheckDto> res = supplyOrderController.getSODetailsForSurpriseCheck(0);
			assertNotNull(res);
		}, "Test failed due to an exception: getSODetailsForSurpriseCheckExceptionTest");
		logger.info("SupplyOrderControllerTest.getSODetailsForSurpriseCheckExceptionTest : Finished");
	}
	
}
