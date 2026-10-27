package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyMap;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
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
//import in.co.bel.olms.transaction.controller.OlLineItemController;
//import in.co.bel.olms.transaction.dto.LineItemApplicableTestDto;
//import in.co.bel.olms.transaction.dto.LineItemDto;
//import in.co.bel.olms.transaction.dto.LineItemHologramDto;
//import in.co.bel.olms.transaction.dto.OlLineItemDto;
//import in.co.bel.olms.transaction.entity.OlLineItem;
//import in.co.bel.olms.transaction.entity.SOLineItem;
//import in.co.bel.olms.transaction.service.OlLineItemService;
//
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class OlLineItemControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(OlLineItemControllerTest.class);
//	
//	@InjectMocks
//	OlLineItemController olLineItemController;
//
//	@Mock
//	OlLineItemService olLineItemService;
//	
//	@Mock
//	ObjectMapper mapper2;
//
//	@Mock
//	Mapper mapper;
//	
//	Gson gson = new Gson();
//	
//	@Test
//	void getOlLineItemByIdValidTest() {
//		logger.info("OlLineItemControllerTest.getOlLineItemByIdValidTest : Initiated");
//		try {
//			int id =2;
//			when(olLineItemService.getOlLineItemById(anyInt())).thenReturn(new OlLineItem());
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			ResponseEntity<OlLineItemDto> result = olLineItemController.getOlLineItemById(id);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlLineItemByIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlLineItemByIdValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.getOlLineItemByIdValidTest : Finished");
//	}
//	
//	@Test
//	void getOlLineItemByOlIdValidTest() {
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidTest : Initiated");
//		try {
//			int olId = 84;
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			OlLineItem olLineItem = new OlLineItem();
//			SOLineItem soLineItem = new SOLineItem();
//			olLineItem.setTmrFileName("82_2_1680586714792.pdf");
//			soLineItem.setIsUnderWarranty(true);
//			soLineItem.setDuration(1);
//			soLineItem.setDurationType(new DurationType());
//			soLineItem.getDurationType().setName("quarterly");
//			soLineItem.setUnitCost(12.00);
//			soLineItem.setTmrFileName("82_2_1680586714792.pdf");
//			soLineItem.setImported(true);
//			olLineItem.setSoLineItem(soLineItem);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			when(olLineItemService.getOlLineItemByOlId(anyInt())).thenReturn(olLineItems);
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			ResponseEntity<List<OlLineItemDto>> result = olLineItemController.getOlLineItemByOlId(olId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlLineItemByOlIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlLineItemByOlIdValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidTest : Finished");
//	}
//	
//	@Test
//	void getOlLineItemByOlIdValidSecScenarioTest() {
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidSecScenarioTest : Initiated");
//		try {
//			int olId = 84;
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			OlLineItem olLineItem = new OlLineItem();
//			SOLineItem soLineItem = new SOLineItem();
//			olLineItem.setTmrFileName(null);
//			soLineItem.setIsUnderWarranty(false);
//			soLineItem.setDuration(1);
//			soLineItem.setDurationType(new DurationType());
//			soLineItem.getDurationType().setName("quarterly");
//			soLineItem.setUnitCost(null);
//			soLineItem.setTmrFileName(null);
//			soLineItem.setImported(true);
//			olLineItem.setSoLineItem(soLineItem);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			when(olLineItemService.getOlLineItemByOlId(anyInt())).thenReturn(olLineItems);
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			ResponseEntity<List<OlLineItemDto>> result = olLineItemController.getOlLineItemByOlId(olId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlLineItemByOlIdValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlLineItemByOlIdValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getOlLineItemByOlIdValidThirdScenarioTest() {
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidThirdScenarioTest : Initiated");
//		try {
//			int olId = 84;
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			OlLineItem olLineItem = new OlLineItem();
//			SOLineItem soLineItem = new SOLineItem();
//			olLineItem.setTmrFileName(null);
//			soLineItem.setIsUnderWarranty(null);
//			soLineItem.setDuration(1);
//			soLineItem.setDurationType(new DurationType());
//			soLineItem.getDurationType().setName("quarterly");
//			soLineItem.setUnitCost(null);
//			soLineItem.setTmrFileName(null);
//			soLineItem.setImported(true);
//			olLineItem.setSoLineItem(soLineItem);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			when(olLineItemService.getOlLineItemByOlId(anyInt())).thenReturn(olLineItems);
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			ResponseEntity<List<OlLineItemDto>> result = olLineItemController.getOlLineItemByOlId(olId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlLineItemByOlIdValidThirdScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlLineItemByOlIdValidThirdScenarioTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.getOlLineItemByOlIdValidThirdScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void updateTmrSheetValidTest() {
//		logger.info("OlLineItemControllerTest.updateTmrSheetValidTest : Initiated");
//		try {
//			byte[] fileContent = "File 1 Content".getBytes();
//	        MockMultipartFile tmrFile = new MockMultipartFile(
//	                "file",
//	                "file2.txt",
//	                "text/plain",
//	                fileContent
//	        );
//	        OlLineItemDto olLineItemDto = new OlLineItemDto();
//	        olLineItemDto.setCreatedBy(2);
//	        String resp = gson.toJson(olLineItemDto);
//	        when(olLineItemService.updateTmrSheet(any(OlLineItemDto.class), any(MultipartFile.class))).thenReturn(olLineItemDto);
//	        ResponseEntity<OlLineItemDto> result = olLineItemController.updateTmrSheet(resp, tmrFile);
//	        assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateTmrSheetValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateTmrSheetValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateTmrSheetValidTest : Finished");
//	}
//	
//	@Test
//	void updateTmrSheetValidSecScenarioTest() {
//		logger.info("OlLineItemControllerTest.updateTmrSheetValidSecScenarioTest : Initiated");
//		try {
//			byte[] fileContent = "File 1 Content".getBytes();
//	        MockMultipartFile tmrFile = new MockMultipartFile(
//	                "file",
//	                "file2.txt",
//	                "text/plain",
//	                fileContent
//	        );
//	        when(olLineItemService.updateTmrSheet(any(OlLineItemDto.class), any(MultipartFile.class))).thenReturn(null);
//	        ResponseEntity<OlLineItemDto> result = olLineItemController.updateTmrSheet(null, tmrFile);
//	        assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateTmrSheetValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateTmrSheetValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateTmrSheetValidSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void updateHologramDetailsValidTest() {
//		logger.info("OlLineItemControllerTest.updateHologramDetailsValidTest : Initiated");
//		try {
//			LineItemDto lineItemDto = new LineItemDto();
//			List<LineItemHologramDto> lLItemHoloDto = new ArrayList<>();
//			LineItemHologramDto lItemHoloDto = new LineItemHologramDto();
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			lItemHoloDto.setHologramRemarks("Rem1");
//			lLItemHoloDto.add(lItemHoloDto);
//			lLItemHoloDto.add(lItemHoloDto);
//			lineItemDto.setHologramDtos(lLItemHoloDto);
//			String resp = gson.toJson(lineItemDto);
//			savedStatus.put(true, resp);
//			when(olLineItemService.updateHologramDetails(anyList())).thenReturn(savedStatus);
//			ResponseEntity<String> result = olLineItemController.updateHologramDetails(lineItemDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateHologramDetailsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateHologramDetailsValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateHologramDetailsValidTest : Finished");
//	}
//	
//	@Test
//	void updateHologramDetailsValidSecScenarioTest() {
//		logger.info("OlLineItemControllerTest.updateHologramDetailsValidSecScenarioTest : Initiated");
//		try {
//			LineItemDto lineItemDto = new LineItemDto();
//			List<LineItemHologramDto> lLItemHoloDto = new ArrayList<>();
//			LineItemHologramDto lItemHoloDto = new LineItemHologramDto();
//			lItemHoloDto.setHologramRemarks("Rem1");
//			lLItemHoloDto.add(lItemHoloDto);
//			lLItemHoloDto.add(lItemHoloDto);
//			lineItemDto.setHologramDtos(lLItemHoloDto);
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			savedStatus.put(false, "falseString");
//			when(olLineItemService.updateHologramDetails(anyList())).thenReturn(savedStatus);
//			ResponseEntity<String> result = olLineItemController.updateHologramDetails(lineItemDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateHologramDetailsValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateHologramDetailsValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateHologramDetailsValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void updateApplicableTestsValidTest() {
//		logger.info("OlLineItemControllerTest.updateApplicableTestsValidTest : Initiated");
//		try {
//			byte[] fileContent = "File 1 Content".getBytes();
//			LineItemApplicableTestDto lineItemApplicableTestDto = new LineItemApplicableTestDto();
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			MultipartFile tmrFile = new MockMultipartFile(
//	                "file",
//	                "file2.txt",
//	                "text/plain",
//	                fileContent
//	        );
//			MultipartFile[] mArr = {tmrFile, tmrFile};
//			lineItemApplicableTestDto.setId(12);
//			String resp = gson.toJson(lineItemApplicableTestDto);
//			savedStatus.put(true, resp);
//			when((olLineItemService.updateApplicableTests(any(LineItemApplicableTestDto.class), anyMap(), anyMap()))).thenReturn(savedStatus);
//			ResponseEntity<String> result = olLineItemController.updateApplicableTests(resp, mArr);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateApplicableTestsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateApplicableTestsValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateApplicableTestsValidTest : Finished");
//	}
//	
//	@Test
//	void updateApplicableTestsValidSecScenarioTest() {
//		logger.info("OlLineItemControllerTest.updateApplicableTestsValidSecScenarioTest : Initiated");
//		try {
//			byte[] fileContent = "File 1 Content".getBytes();
//			LineItemApplicableTestDto lineItemApplicableTestDto = new LineItemApplicableTestDto();
//			Map<Boolean, String> savedStatus = new HashMap<>();
//			MultipartFile tmrFile = new MockMultipartFile(
//	                "file",
//	                "file2.txt",
//	                "text/plain",
//	                fileContent
//	        );
//			MultipartFile[] mArr = {tmrFile, tmrFile};
//			lineItemApplicableTestDto.setId(12);
//			savedStatus.put(false, "Invalid Input");
//			when(olLineItemService.updateApplicableTests(any(LineItemApplicableTestDto.class), anyMap(), anyMap())).thenReturn(savedStatus);
//			ResponseEntity<String> result = olLineItemController.updateApplicableTests(null, mArr);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateApplicableTestsValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateApplicableTestsValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.updateApplicableTestsValidSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void getApplicableTestsByLineItemIdValidTest() {
//		logger.info("OlLineItemControllerTest.getApplicableTestsByLineItemIdValidTest : Initiated");
//		try {
//			int olLineItemId = 78;
//			when(olLineItemService.getApplicableTestsByLineItemId(anyInt())).thenReturn(new LineItemApplicableTestDto());
//			ResponseEntity<LineItemApplicableTestDto> result = olLineItemController.getApplicableTestsByLineItemId(olLineItemId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getApplicableTestsByLineItemIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getApplicableTestsByLineItemIdValidTest " + e.getMessage());
//		}
//		logger.info("OlLineItemControllerTest.getApplicableTestsByLineItemIdValidTest : Finished");
//	}
//	
//}
