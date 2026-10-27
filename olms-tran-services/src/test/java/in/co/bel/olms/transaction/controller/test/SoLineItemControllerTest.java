package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsTranServicesApplication;
//import in.co.bel.olms.master.entity.DurationType;
//import in.co.bel.olms.transaction.controller.SoLineItemController;
//import in.co.bel.olms.transaction.dto.SoLineItemDto;
//import in.co.bel.olms.transaction.dto.SoLineItemInputDto;
//import in.co.bel.olms.transaction.entity.OfferLetter;
//import in.co.bel.olms.transaction.entity.OlLineItem;
//import in.co.bel.olms.transaction.entity.SOLineItem;
//import in.co.bel.olms.transaction.service.so.SoLineItemService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class SoLineItemControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(SoLineItemControllerTest.class);
//	
//	@InjectMocks
//	SoLineItemController soLineItemController;
//	
//	@Mock
//	SoLineItemService soLineItemService;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	private Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void getSoLineItemByIdValidTest() {
//		logger.info("SoLineItemControllerTest.getSoLineItemByIdValidTest : Initiated");
//		try {
//			int id = 1;
//			when(soLineItemService.getSoLineItemById(anyInt())).thenReturn(new SOLineItem());
//			when(mapper.map(any(SOLineItem.class), eq(SoLineItemDto.class))).thenReturn(new SoLineItemDto());
//			ResponseEntity<SoLineItemDto> res = soLineItemController.getSoLineItemById(id);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getSoLineItemByIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getSoLineItemByIdValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.getSoLineItemByIdValidTest : Finished");
//	}
//	
//	@Test
//	void saveSoLineItemsValidTest() {
//		logger.info("SoLineItemControllerTest.saveSoLineItemsValidTest : Initiated");
//		try {
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			SoLineItemInputDto soLineItemInputDto = new SoLineItemInputDto();
//			soLineItemInputDto.setCreatedBy(2);
//			String resp = gson.toJson(soLineItemInputDto);
//			savedStatus.put(true, resp);
//			when(soLineItemService.saveSoLineItems(any(SoLineItemInputDto.class))).thenReturn(savedStatus);
//			ResponseEntity<String> res = soLineItemController.saveSoLineItems(soLineItemInputDto);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveSoLineItemsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: saveSoLineItemsValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.saveSoLineItemsValidTest : Finished");
//	}
//	
//	@Test
//	void saveSoLineItemsSecScenarioTest() {
//		logger.info("SoLineItemControllerTest.saveSoLineItemsSecScenarioTest : Initiated");
//		try {
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			savedStatus.put(false, "Internal Server Error");
//			when(soLineItemService.saveSoLineItems(any(SoLineItemInputDto.class))).thenReturn(savedStatus);
//			ResponseEntity<String> res = soLineItemController.saveSoLineItems(null);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveSoLineItemsSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: saveSoLineItemsSecScenarioTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.saveSoLineItemsSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void getAllSoLineItemsValidTest() {
//		logger.info("SoLineItemControllerTest.getAllSoLineItemsValidTest : Initiated");
//		try {
//			List<SOLineItem> lSoLineItem = new ArrayList<>();
//			SOLineItem soLineItem = new SOLineItem();
//			soLineItem.setCreatedBy(2);
//			lSoLineItem.add(soLineItem);
//			lSoLineItem.add(soLineItem);
//			when(soLineItemService.getAllSoLineItems()).thenReturn(lSoLineItem);
//			when(mapper.map(any(SOLineItem.class), eq(SoLineItemDto.class))).thenReturn(new SoLineItemDto());
//			ResponseEntity<List<SoLineItemDto>> res = soLineItemController.getAllSoLineItems();
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllSoLineItemsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllSoLineItemsValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.getAllSoLineItemsValidTest : Finished");
//	}
//	
//	@Test
//	void getSoLineItemsBySoIdValidTest() {
//		logger.info("SoLineItemControllerTest.getSoLineItemsBySoIdValidTest : Initiated");
//		try {
//			int soId = 84;
//			List<SOLineItem> soLineItems = new ArrayList<>();
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			Calendar date = Calendar.getInstance();
//			date.set(1996, 10, 23);
//			SOLineItem soLineItem = new SOLineItem();
//			OlLineItem olLineItem = new OlLineItem();
//			OfferLetter offerLetter = new OfferLetter();
//			soLineItem.setIsUnderWarranty(true);
//			soLineItem.setDuration(1);
//			soLineItem.setDurationType(new DurationType());
//			soLineItem.getDurationType().setName("Day");
//			soLineItem.setTmrFileName("File1.txt");
//			olLineItem.setClearedQty(12);
//			olLineItem.setTmrFileName("File1.txt");
//			offerLetter.setOlNumber("Ol 1");
//			offerLetter.setOfferDate(date.getTime());
//			olLineItem.setOfferLetter(offerLetter);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			soLineItem.setOlLineItems(olLineItems);
//			soLineItem.setOrderQty(30);
//			soLineItem.setUnitCost(120.00);
//			soLineItems.add(soLineItem);
//			soLineItems.add(soLineItem);
//			when(soLineItemService.getSoLineItemsBySoId(anyInt())).thenReturn(soLineItems);
//			when(mapper.map(any(SOLineItem.class), eq(SoLineItemDto.class))).thenReturn(new SoLineItemDto());
//			ResponseEntity<List<SoLineItemDto>> result = soLineItemController.getSoLineItemsBySoId(soId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getSoLineItemsBySoIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getSoLineItemsBySoIdValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.getSoLineItemsBySoIdValidTest : Finished");
//	}
//	
//	@Test
//	void getSoLineItemsBySaleOrderIdValidTest() {
//		logger.info("SoLineItemControllerTest.getSoLineItemsBySaleOrderIdValidTest : Initiated");
//		try {
//			int saleOrderId = 199;
//			List<SOLineItem> soLineItems = new ArrayList<>();
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			Calendar date = Calendar.getInstance();
//			date.set(1996, 10, 23);
//			SOLineItem soLineItem = new SOLineItem();
//			OlLineItem olLineItem = new OlLineItem();
//			OfferLetter offerLetter = new OfferLetter();
//			soLineItem.setIsUnderWarranty(true);
//			soLineItem.setDuration(1);
//			soLineItem.setDurationType(new DurationType());
//			soLineItem.getDurationType().setName("Day");
//			soLineItem.setTmrFileName("File1.txt");
//			olLineItem.setClearedQty(12);
//			olLineItem.setTmrFileName("File1.txt");
//			offerLetter.setOlNumber("Ol 1");
//			offerLetter.setOfferDate(date.getTime());
//			olLineItem.setOfferLetter(offerLetter);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			soLineItem.setOlLineItems(olLineItems);
//			soLineItem.setOrderQty(30);
//			soLineItem.setUnitCost(120.00);
//			soLineItems.add(soLineItem);
//			soLineItems.add(soLineItem);
//			when(soLineItemService.getSoLineItemsBySaleOrderId(anyInt())).thenReturn(soLineItems);
//			when(mapper.map(any(SOLineItem.class), eq(SoLineItemDto.class))).thenReturn(new SoLineItemDto());
//			ResponseEntity<List<SoLineItemDto>> result = soLineItemController.getSoLineItemsBySaleOrderId(saleOrderId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getSoLineItemsBySaleOrderIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getSoLineItemsBySaleOrderIdValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.getSoLineItemsBySaleOrderIdValidTest : Finished");
//	}
//	
//	@Test
//	void updateSoLineItemValidTest() {
//		logger.info("SoLineItemControllerTest.updateSoLineItemValidTest : Initiated");
//		try {
//			SoLineItemDto soLineItemDto = new SoLineItemDto();
//			soLineItemDto.setId(656);
//			when(soLineItemService.updateSoLineItem(any(SoLineItemDto.class))).thenReturn(true);
//			ResponseEntity<Boolean> result = soLineItemController.updateSoLineItem(soLineItemDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateSoLineItemValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateSoLineItemValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.updateSoLineItemValidTest : Finished");
//	}
//	
//	@Test
//	void updateSoLineItemSecScenarioTest() {
//		logger.info("SoLineItemControllerTest.updateSoLineItemSecScenarioTest : Initiated");
//		try {
//			SoLineItemDto soLineItemDto = new SoLineItemDto();
//			soLineItemDto.setId(656);
//			when(soLineItemService.updateSoLineItem(any(SoLineItemDto.class))).thenReturn(false);
//			ResponseEntity<Boolean> result = soLineItemController.updateSoLineItem(soLineItemDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateSoLineItemSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateSoLineItemSecScenarioTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.updateSoLineItemSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void updateTmrSheetValidTest() {
//		logger.info("SoLineItemControllerTest.updateTmrSheetValidTest : Initiated");
//		try {
//			SoLineItemDto soLineItemDto = new SoLineItemDto();
//			soLineItemDto.setId(656);
//			String resp = gson.toJson(soLineItemDto);
//			byte[] fileContent1 = "File 1 content".getBytes();
//	        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", fileContent1);
//	        MultipartFile tmrFile = (MultipartFile) file1;
//	        when(soLineItemService.updateTmrSheet(any(SoLineItemDto.class), any(MultipartFile.class))).thenReturn(soLineItemDto);
//	        ResponseEntity<SoLineItemDto> result = soLineItemController.updateTmrSheet(resp, tmrFile);
//	        assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateTmrSheetValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateTmrSheetValidTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.updateTmrSheetValidTest : Finished");
//	}
//	
//	@Test
//	void updateTmrSheetSecScenarioTest() {
//		logger.info("SoLineItemControllerTest.updateTmrSheetSecScenarioTest : Initiated");
//		try {
//			byte[] fileContent1 = "File 1 content".getBytes();
//	        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", fileContent1);
//	        MultipartFile tmrFile = (MultipartFile) file1;
//	        when(soLineItemService.updateTmrSheet(any(SoLineItemDto.class), any(MultipartFile.class))).thenReturn(null);
//	        ResponseEntity<SoLineItemDto> result = soLineItemController.updateTmrSheet(null, tmrFile);
//	        assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateTmrSheetSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateTmrSheetSecScenarioTest " + e.getMessage());
//		}
//		logger.info("SoLineItemControllerTest.updateTmrSheetSecScenarioTest : Finished");
//	}
//	
//}
