package in.co.bel.olms.transaction.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
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
//import in.co.bel.olms.master.dto.KeyValuePair;
//import in.co.bel.olms.master.entity.Consignee;
//import in.co.bel.olms.master.entity.Organization;
//import in.co.bel.olms.master.entity.SalesGroup;
//import in.co.bel.olms.master.repository.EmployeeRepository;
//import in.co.bel.olms.transaction.controller.OfferLetterController;
//import in.co.bel.olms.transaction.dto.INoteDto;
//import in.co.bel.olms.transaction.dto.OLDto;
//import in.co.bel.olms.transaction.dto.OfferLetterDto;
//import in.co.bel.olms.transaction.dto.OfferLetterStatusDto;
//import in.co.bel.olms.transaction.dto.OlAttachmentDto;
//import in.co.bel.olms.transaction.dto.OlLineItemDto;
//import in.co.bel.olms.transaction.entity.Category;
//import in.co.bel.olms.transaction.entity.OfferLetter;
//import in.co.bel.olms.transaction.entity.OlLineItem;
//import in.co.bel.olms.transaction.entity.Project;
//import in.co.bel.olms.transaction.entity.QinObsNcForm;
//import in.co.bel.olms.transaction.entity.SOLineItem;
//import in.co.bel.olms.transaction.entity.SaleOrder;
//import in.co.bel.olms.transaction.entity.Ship;
//import in.co.bel.olms.transaction.entity.SupplyOrder;
//import in.co.bel.olms.transaction.helper.ol.OlHelper;
//import in.co.bel.olms.transaction.repository.ClearanceLetterRepository;
//import in.co.bel.olms.transaction.repository.OfferLetterRepository;
//import in.co.bel.olms.transaction.repository.OlLineItemRepository;
//import in.co.bel.olms.transaction.service.NotificationService;
//import in.co.bel.olms.transaction.service.OfferLetterService;
//import in.co.bel.olms.transaction.service.check.NotifyUtil;
//import in.co.bel.olms.transaction.service.ol.OlAllotmentService;
//import in.co.bel.olms.websocket.WSService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsTranServicesApplication.class)
//class OfferLetterControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(OfferLetterControllerTest.class);
//	
//	@InjectMocks
//	OfferLetterController offerLetterController;
//	
//	@Mock
//	NotificationService notificationService;
//	
//	@Mock	NotifyUtil notifyUtil;
//	
//	@Mock
//	WSService wsService;
//	
//	@Mock
//	OfferLetterRepository offerLetterRepository;
//	
//	@Mock
//	ClearanceLetterRepository clearanceLetterRepository;
//	
//	@Mock
//	OlHelper olHelper;
//	
//	@Mock
//	OfferLetterService offerLetterService;
//
//	@Mock
//	OlLineItemRepository olLineItemRepository;
//	
//	@Mock
//	OlAllotmentService olAllotmentService;
//
//	@Mock
//	EmployeeRepository employeeRepository;
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
//	void getOfferLetterByIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOfferLetterByIdValidTest : Initiated");
//		try {
//			int id = 1;
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			OfferLetter ol = new OfferLetter();
//			SaleOrder saleOrder = new SaleOrder();
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			ol.setSaleOrder(saleOrder);
//			ol.setOlNumber("0405 OL 1");
//			when(offerLetterService.getOfferLetterById(anyInt())).thenReturn(ol);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			ResponseEntity<OfferLetterDto> result = offerLetterController.getOfferLetterById(id);
//			assertNotNull(result.toString());
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLetterByIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOfferLetterByIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLetterByIdValidTest : Finished");
//	}
//	
//	@Test
//	void saveOfferLetterValidTest() {
//		logger.info("OfferLetterControllerTest.saveOfferLetterValidTest : Initiated");
//		try {
//			Map<Boolean, String> response = new HashMap<>();
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(12);
//			offerLetterDto.setINoteTypeId(123);
//			String resp = gson.toJson(offerLetterDto);
//			response.put(true, resp);
//			when(offerLetterService.saveOfferLetter(any(OfferLetterDto.class))).thenReturn(response);
//			ResponseEntity<String> result = offerLetterController.saveOfferLetter(offerLetterDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveOfferLetterValidTest " + e.getMessage());
//			fail("Test failed due to an exception: saveOfferLetterValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.saveOfferLetterValidTest : Finished");
//	}
//	
//	@Test
//	void saveOfferLetterValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.saveOfferLetterValidSecScenarioTest : Initiated");
//		try {
//			Map<Boolean, String> response = new HashMap<>();
//			response.put(false, "False result");
//			when(offerLetterService.saveOfferLetter(any(OfferLetterDto.class))).thenReturn(response);
//			ResponseEntity<String> result = offerLetterController.saveOfferLetter(new OfferLetterDto());
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: saveOfferLetterValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: saveOfferLetterValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.saveOfferLetterValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getOLBySaleOrderIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOLBySaleOrderIdValidTest : Initiated");
//		try {
//			int saleOrderId = 199;
//			List<OfferLetter> lOL = new ArrayList<>();
//			OfferLetter ol = new OfferLetter();
//			ol.setActive(true);
//			lOL.add(ol);
//			lOL.add(ol);
//			when(offerLetterService.getOLBySaleOrderId(anyInt())).thenReturn(lOL);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOLBySaleOrderId(saleOrderId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLBySaleOrderIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOLBySaleOrderIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLBySaleOrderIdValidTest : Finished");
//	}
//	
//	@Test
//	void getOLBySaleOrderIdValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.getOLBySaleOrderIdValidSecScenarioTest : Initiated");
//		try {
//			int saleOrderId = 199;
//			List<OfferLetter> lOL = new ArrayList<>();
//			OfferLetter ol = new OfferLetter();
//			ol.setActive(false);
//			lOL.add(ol);
//			lOL.add(ol);
//			when(offerLetterService.getOLBySaleOrderId(anyInt())).thenReturn(lOL);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOLBySaleOrderId(saleOrderId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLBySaleOrderIdValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getOLBySaleOrderIdValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLBySaleOrderIdValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getLineItemsByOlValidTest() {
//		logger.info("OfferLetterControllerTest.getLineItemsByOlValidTest : Initiated");
//		try {
//			int olId = 213;
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			OlLineItem olLineItem = new OlLineItem();
//			SOLineItem soLineItem = new SOLineItem();
//			soLineItem.setStatus("Inactive");
//			soLineItem.setUnitCost(1000.00);
//			soLineItem.setTmrFileName("82_2_1680586714792.pdf");
//			soLineItem.setOrderQty(6);
//			soLineItem.setImported(true);
//			soLineItem.setId(656);
//			olLineItem.setTmrFileName("82_2_1680586714792.pdf");
//			olLineItem.setSoLineItem(soLineItem);
//			olLineItem.setOfferedQty(4);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			when(olLineItemRepository.getLineItemsByOl(anyInt())).thenReturn(olLineItems);
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			when(olLineItemRepository.getPendingQtyByLineItemNo(anyInt())).thenReturn(2);
//			ResponseEntity<List<OlLineItemDto>> result = offerLetterController.getLineItemsByOl(olId);
//			List<OlLineItemDto> retVal = offerLetterService.getLineItemsByOl(olId);
//			assertNotNull(result);
//			assertNotNull(retVal);
//			assertNotEquals("[]", retVal);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getLineItemsByOlValidTest " + e.getMessage());
//
//		}
//		logger.info("OfferLetterControllerTest.getLineItemsByOlValidTest : Finished");
//	}
//	
//	@Test
//	void getOLBySupplyOrderIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOLBySupplyOrderIdValidTest : Initiated");
//		try {
//			int supplyOrderId = 223;
//			List<OfferLetter> lOL = new ArrayList<>();
//			List<QinObsNcForm> lQInObs = new ArrayList<>();
//			QinObsNcForm qInObs = new QinObsNcForm();
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			OfferLetter ol = new OfferLetter();
//			SaleOrder saleOrder = new SaleOrder();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			cat.setId(1);
//			sh.setId(2);
//			pro.setActive(true);
//			qInObs.setId(12);
//			lQInObs.add(qInObs);
//			lQInObs.add(qInObs);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			ol.setSaleOrder(saleOrder);
//			ol.setActive(true);
//			ol.setQinObsNcForms(lQInObs);
//			ol.setOlNumber("0405 OL 1");
//			lOL.add(ol);
//			lOL.add(ol);
//			when(offerLetterService.getOLBySupplyOrderId(anyInt())).thenReturn(lOL);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOLBySupplyOrderId(supplyOrderId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLBySupplyOrderIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOLBySupplyOrderIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLBySupplyOrderIdValidTest : Finished");
//	}
//	
//	@Test
//	void getOLBySupplyOrderIdExceptionTest() {
//		logger.info("OfferLetterControllerTest.getOLBySupplyOrderIdExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOLBySupplyOrderId(anyInt())).thenThrow(new NullPointerException());
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOLBySupplyOrderId(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLBySupplyOrderIdExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLBySupplyOrderIdExceptionTest : Finished");
//	}
//	
//	
//	@Test
//	void getINoteOfOfferLetterValidTest() {
//		logger.info("OfferLetterControllerTest.getINoteOfOfferLetterValidTest : Initiated");
//		try {
//			int olId = 213;
//			INoteDto iNoteDto = new INoteDto();
//			iNoteDto.setIndentDate(new Date());
//			iNoteDto.setOlId(olId);
//			when(offerLetterService.getINoteByOlId(anyInt())).thenReturn(iNoteDto);
//			ResponseEntity<INoteDto> result = offerLetterController.getINoteOfOfferLetter(olId);
//			assertNotNull(result);
//			assertNotEquals("[]", result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getINoteOfOfferLetterValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getINoteOfOfferLetterValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getINoteOfOfferLetterValidTest : Finished");
//	}
//	
//	@Test
//	void getINoteOfOfferLetterExceptionTest() {
//		logger.info("OfferLetterControllerTest.getINoteOfOfferLetterExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getINoteByOlId(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<INoteDto> result = offerLetterController.getINoteOfOfferLetter(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getINoteOfOfferLetterExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getINoteOfOfferLetterExceptionTest : Finished");
//	}
//	
//	@Test
//	void getOlApprovalDetailsByOrgIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOlApprovalDetailsByOrgIdValidTest : Initiated");
//		try {
//			int orgId = 12;
//			List<Object[]> approvedOLs = Arrays.asList(new Object[] {84, "OL BB1", "2023-12-20", "MCE", "VRSAM MAIN EQUIPMENT", "2023-09-25", 3, "INote01"}, new Object[] {84, "OL BB1", "2023-12-20", "MCE", "VRSAM MAIN EQUIPMENT", "2023-09-25", 3, "INote01"});
//		    when(offerLetterRepository.getOlApprovalDetailsByOrgId(anyInt(), anyInt())).thenReturn(approvedOLs);
//		    when(clearanceLetterRepository.getByOlId(anyInt())).thenReturn(3);
//		    ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOlApprovalDetailsByOrgId(orgId);
//		    List<OfferLetterDto> retVal = offerLetterService.getOlApprovalDetailsByOrgId(orgId);
//		    assertNotNull(result);
//		    assertNotNull(retVal);
//	        assertNotEquals("[]", retVal);	
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlApprovalDetailsByOrgIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlApprovalDetailsByOrgIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOlApprovalDetailsByOrgIdValidTest : Finished");
//	}
//	
//	@Test
//	void updateOfferLetterValidTest() {
//		logger.info("OfferLetterControllerTest.updateOfferLetterValidTest : Initiated");
//		try {
//			Map<Boolean, String> response = new HashMap<>();
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(1);
//			String resp = gson.toJson(offerLetterDto);
//			response.put(true, resp);
//			when(offerLetterService.updateOfferLetter(any(OfferLetterDto.class))).thenReturn(response);
//			ResponseEntity<String> result = offerLetterController.updateOfferLetter(offerLetterDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOfferLetterValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOfferLetterValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOfferLetterValidTest : Finished");
//	}
//	
//	@Test
//	void updateOfferLetterValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.updateOfferLetterValidSecScenarioTest : Initiated");
//		try {
//			Map<Boolean, String> response = new HashMap<>();
//			response.put(false, "false result");
//			when(offerLetterService.updateOfferLetter(any(OfferLetterDto.class))).thenReturn(response);
//			ResponseEntity<String> result = offerLetterController.updateOfferLetter(null);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOfferLetterValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOfferLetterValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOfferLetterValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getAllotedOfferLettersValidTest() {
//		logger.info("OfferLetterControllerTest.getAllotedOfferLettersValidTest : Initiated");
//		try {
//			int empId = 7;
//			List<OfferLetter> lOL = new ArrayList<>();
//			List<QinObsNcForm> lQInObs = new ArrayList<>();
//			QinObsNcForm qInObs = new QinObsNcForm();
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			OfferLetter ol = new OfferLetter();
//			SaleOrder saleOrder = new SaleOrder();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			cat.setId(1);
//			sh.setId(2);
//			pro.setActive(true);
//			qInObs.setId(12);
//			lQInObs.add(qInObs);
//			lQInObs.add(qInObs);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			ol.setSaleOrder(saleOrder);
//			ol.setActive(true);
//			ol.setQinObsNcForms(lQInObs);
//			ol.setOlNumber("0405 OL 1");
//			lOL.add(ol);
//			lOL.add(ol);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			when(olHelper.isReturned(any(OfferLetter.class))).thenReturn(true);
//			when(olHelper.isScrutinyDone(any(OfferLetter.class))).thenReturn(true);
//			when(offerLetterService.getFileNoteStatusByOlId(anyInt())).thenReturn("Not Returned");
//			//when(olAllotmentService.getOLAllotedToEmployee(anyInt())).thenReturn(lOL);
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getAllotedOfferLetters(empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllotedOfferLettersValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllotedOfferLettersValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getAllotedOfferLettersValidTest : Finished");
//	}
//	
//	@Test
//	void getAllotedOfferLettersValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.getAllotedOfferLettersValidSecScenarioTest : Initiated");
//		try {
//			int empId = 7;
//			List<OfferLetter> lOL = new ArrayList<>();
//			List<QinObsNcForm> lQInObs = new ArrayList<>();
//			QinObsNcForm qInObs = new QinObsNcForm();
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			OfferLetter ol = new OfferLetter();
//			SaleOrder saleOrder = new SaleOrder();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			cat.setId(1);
//			sh.setId(2);
//			pro.setActive(true);
//			qInObs.setId(12);
//			lQInObs.add(qInObs);
//			lQInObs.add(qInObs);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			ol.setSaleOrder(saleOrder);
//			ol.setActive(false);
//			ol.setQinObsNcForms(lQInObs);
//			ol.setOlNumber("0405 OL 1");
//			lOL.add(ol);
//			lOL.add(ol);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			when(olHelper.isReturned(any(OfferLetter.class))).thenReturn(true);
//			when(olHelper.isScrutinyDone(any(OfferLetter.class))).thenReturn(true);
//			when(offerLetterService.getFileNoteStatusByOlId(anyInt())).thenReturn("Not Returned");
//			//when(olAllotmentService.getOLAllotedToEmployee(anyInt())).thenReturn(lOL);
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getAllotedOfferLetters(empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAllotedOfferLettersValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getAllotedOfferLettersValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getAllotedOfferLettersValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getApprovedOfferLettersValidTest() {
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersValidTest : Initiated");
//		try {
//			int empId = 7;
//			List<QinObsNcForm> lQInObs = new ArrayList<>();
//			QinObsNcForm qInObs = new QinObsNcForm();
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			SaleOrder saleOrder = new SaleOrder();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			cat.setId(1);
//			sh.setId(2);
//			pro.setActive(true);
//			qInObs.setId(12);
//			lQInObs.add(qInObs);
//			lQInObs.add(qInObs);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			List<Object[]> lOL = Arrays.asList(new Object[] { saleOrder, true, lQInObs, "0405 OL 1" },
//					new Object[] { saleOrder, true, lQInObs, "0405 OL 1" });
//			when(offerLetterService.getOlAllotedForApproval(anyInt())).thenReturn(lOL);
//			when(mapper.map(any(OfferLetter.class), eq(OLDto.class))).thenReturn(new OLDto());
//			when(olHelper.isReturned(any(OfferLetter.class))).thenReturn(true);
//			when(olHelper.isScrutinyDone(any(OfferLetter.class))).thenReturn(true);
//			when(offerLetterService.getFileNoteStatusByOlId(anyInt())).thenReturn("Not Returned");
//			ResponseEntity<List<OLDto>> result = offerLetterController.getApprovedOfferLetters(empId);
//			assertNotEquals("[]", result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getApprovedOfferLettersValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getApprovedOfferLettersValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersValidTest : Finished");
//	}
//	
//	@Test
//	void getApprovedOfferLettersValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersValidSecScenarioTest : Initiated");
//		try {
//			int empId = 7;
//			List<QinObsNcForm> lQInObs = new ArrayList<>();
//			QinObsNcForm qInObs = new QinObsNcForm();
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			SaleOrder saleOrder = new SaleOrder();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			cat.setId(1);
//			sh.setId(2);
//			pro.setActive(true);
//			qInObs.setId(12);
//			lQInObs.add(qInObs);
//			lQInObs.add(qInObs);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			List<Object[]> lOL = Arrays.asList(new Object[] { saleOrder, true, lQInObs, "0405 OL 1" },
//					new Object[] { saleOrder, true, lQInObs, "0405 OL 1" });
//			when(offerLetterService.getOlAllotedForApproval(anyInt())).thenReturn(lOL);
//			when(mapper.map(any(OfferLetter.class), eq(OLDto.class))).thenReturn(new OLDto());
//			when(olHelper.isReturned(any(OfferLetter.class))).thenReturn(true);
//			when(olHelper.isScrutinyDone(any(OfferLetter.class))).thenReturn(true);
//			when(offerLetterService.getFileNoteStatusByOlId(anyInt())).thenReturn("Not Returned");
//			ResponseEntity<List<OLDto>> result = offerLetterController.getApprovedOfferLetters(empId);
//			assertNotEquals("[]", result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getApprovedOfferLettersValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: getApprovedOfferLettersValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getApprovedOfferLettersExceptionTest() {
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOlAllotedForApproval(anyInt())).thenThrow(new NullPointerException());
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			when(olHelper.isReturned(any(OfferLetter.class))).thenReturn(true);
//			when(olHelper.isScrutinyDone(any(OfferLetter.class))).thenReturn(true);
//			when(offerLetterService.getFileNoteStatusByOlId(anyInt())).thenReturn("Not Returned");
//			ResponseEntity<List<OLDto>> result = offerLetterController.getApprovedOfferLetters(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getApprovedOfferLettersExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getApprovedOfferLettersExceptionTest : Finished");
//	}
//	
//	@Test
//	void getOlDetailsForPrintValidTest() {
//		logger.info("OfferLetterControllerTest.getOlDetailsForPrintValidTest : Initiated");
//		try {
//			int olId = 213;
//			Calendar cal = Calendar.getInstance();
//			cal.set(2023, 01, 02);
//			OfferLetterDto olDto = new OfferLetterDto();
//			List<OlLineItem> olLineItems = new ArrayList<>();
//			OlLineItem olLineItem = new OlLineItem();
//			SOLineItem soLineItem = new SOLineItem();
//			OfferLetter ol = new OfferLetter();
//			Organization org = new Organization();
//			SaleOrder saleOrder = new SaleOrder();
//			Consignee consignee = new Consignee();
//			Category cat = new Category();
//			Project pro = new Project();
//			Ship sh = new Ship();
//			soLineItem.setUnitCost(1000.00);
//			soLineItem.setOrderQty(6);
//			soLineItem.setId(656);
//			olLineItem.setSoLineItem(soLineItem);
//			olLineItem.setOfferedQty(11);
//			olLineItems.add(olLineItem);
//			olLineItems.add(olLineItem);
//			org.setId(50);
//			cat.setId(1);
//			sh.setId(2);
//			consignee.setAddress("The Project Director, SBC, Krishna Gate, Varuna Block,  Naval Base Post, Visakhapatnam - 530 014");
//			pro.setActive(true);
//			SupplyOrder supplyOrder = new SupplyOrder();
//			supplyOrder.setTotalQty(138);
//			supplyOrder.setFileNo("IS-000020");
//			supplyOrder.setValue(1040000.00);
//			supplyOrder.setOrderDate(cal.getTime());
//			supplyOrder.setOrderNo("New SO 2");
//			supplyOrder.setOrganization(org);
//			supplyOrder.getOrganization().setOrganizationName("BEL Bg Cx");
//			saleOrder.setSupplyOrder(supplyOrder);
//			saleOrder.setCategory(cat);
//			saleOrder.getCategory().setShip(sh);
//			saleOrder.getCategory().getShip().setProject(pro);
//			saleOrder.getCategory().getShip().getProject().setProjectName("VRSAM_MAIN EQUIPMENT");
//			ol.setSaleOrder(saleOrder);
//			ol.setActive(false);
//			ol.setConsignee(consignee);
//			ol.setOlLineItems(olLineItems);
//			olDto.setId(olId);
//			Optional<OfferLetter> optDataOfferLetter = Optional.of(ol);
//			when(offerLetterRepository.findById(anyInt())).thenReturn(optDataOfferLetter);
//			when(offerLetterService.getOlDetailsForPrint(anyInt())).thenReturn(olDto);
//			when(mapper.map(any(OfferLetter.class), eq(OfferLetterDto.class))).thenReturn(new OfferLetterDto());
//			when(mapper.map(any(OlLineItem.class), eq(OlLineItemDto.class))).thenReturn(new OlLineItemDto());
//			when(offerLetterRepository.getDlvrddQtyByOlIdNSoLineItemId(anyInt(), anyInt())).thenReturn(5);
//			ResponseEntity<OfferLetterDto> result = offerLetterController.getOlDetailsForPrint(olId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOlDetailsForPrintValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOlDetailsForPrintValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOlDetailsForPrintValidTest : Finished");
//	}
//	
//	@Test
//	void updateOlValidTest() {
//		logger.info("OfferLetterControllerTest.updateOlValidTest : Initiated");
//		try {
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			Map<Boolean, String> retMap = new HashMap<>();
//			OfferLetter offerLetter = new OfferLetter();
//			offerLetter.setId(84);
//			offerLetterDto.setId(1);
//			offerLetterDto.setStatus("Active");
//			String resp = gson.toJson(offerLetterDto);
//			retMap.put(true, resp);
//			Optional<OfferLetter> olOptional = Optional.of(offerLetter);
//			when(offerLetterService.updateOl(any(OfferLetterDto.class))).thenReturn(retMap);
//			when(offerLetterRepository.findById(anyInt())).thenReturn(olOptional);
//			when(offerLetterRepository.save(any())).thenReturn(new OfferLetter());
//			ResponseEntity<String> result = offerLetterController.updateOl(offerLetterDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlValidTest : Finished");
//	}
//	
//	@Test
//	void updateOlValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.updateOlValidSecScenarioTest : Initiated");
//		try {
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			Map<Boolean, String> retMap = new HashMap<>();
//			offerLetterDto.setId(1);
//			offerLetterDto.setStatus("Active");
//			retMap.put(false, "false Value");
//			when(offerLetterService.updateOl(any(OfferLetterDto.class))).thenReturn(retMap);
//			when(offerLetterRepository.findById(anyInt())).thenReturn(null);
//			when(offerLetterRepository.save(any())).thenReturn(new OfferLetter());
//			ResponseEntity<String> result = offerLetterController.updateOl(offerLetterDto);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlValidSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void getOfferLetterByCqaeIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOfferLetterByCqaeIdValidTest : Initiated");
//		try {
//			int id = 84;
//			List<OfferLetterDto> lOfferLetter = new ArrayList<>();
//			OfferLetterDto offerLetter = new OfferLetterDto();
//			offerLetter.setId(id);
//			lOfferLetter.add(offerLetter);
//			lOfferLetter.add(offerLetter);
//			when(offerLetterService.getOfferLetterByCqaeId(anyInt())).thenReturn(lOfferLetter);
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOfferLetterByCqaeId(id);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLetterByCqaeIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOfferLetterByCqaeIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLetterByCqaeIdValidTest : Finished");
//	}
//	
//	@Test
//	void notifyCqaeAndJcqaoValidTest() {
//		logger.info("OfferLetterControllerTest.notifyCqaeAndJcqaoValidTest : Initiated");
//		try {
//			int olId = 84;
//			int empId = 1;
//			List<Integer> receivers = new ArrayList<>();
//			OfferLetter offerLetter = new OfferLetter();
//			SaleOrder sOrder = new SaleOrder();
//			SupplyOrder supplyOrder = new SupplyOrder();
//			SalesGroup sGrp = new SalesGroup();
//			Organization org = new Organization();
//			sOrder.setId(199);
//			supplyOrder.setId(223);
//			org.setOrganizationName("BEL Bg Cx");
//			org.setId(1);
//			sGrp.setName("MCE");
//			receivers.add(1);
//			receivers.add(2);
//			offerLetter.setSaleOrder(sOrder);
//			offerLetter.setOlNumber("OL BB1");
//			offerLetter.getSaleOrder().setSupplyOrder(supplyOrder);
//			offerLetter.getSaleOrder().getSupplyOrder().setOrganization(org);
//			offerLetter.getSaleOrder().getSupplyOrder().setSalesGroup(sGrp);
//			when(offerLetterService.getOfferLetterById(anyInt())).thenReturn(offerLetter);
//			when(employeeRepository.getJCnAdminIdsByOrgId(anyInt())).thenReturn(receivers);
//			when(notifyUtil.notifyCqae(anyString(), anyInt(), anyString(), anyInt(), anyString(), anyList(), anyString())).thenReturn("success");
//			when(offerLetterService.updateOlStatus(anyInt(), anyInt())).thenReturn(true);
//			ResponseEntity<Boolean> result = offerLetterController.notifyCqaeAndJcqao(olId, empId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: notifyCqaeAndJcqaoValidTest " + e.getMessage());
//			fail("Test failed due to an exception: notifyCqaeAndJcqaoValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.notifyCqaeAndJcqaoValidTest : Finished");
//	}
//	
//	@Test
//	void notifyCqaeAndJcqaoExceptionTest() {
//		logger.info("OfferLetterControllerTest.notifyCqaeAndJcqaoExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOfferLetterById(anyInt())).thenThrow(new NullPointerException());
//			when(employeeRepository.getJCnAdminIdsByOrgId(anyInt())).thenReturn(null);
//			when(offerLetterService.updateOlStatus(anyInt(), anyInt())).thenReturn(true);
//			ResponseEntity<Boolean> result = offerLetterController.notifyCqaeAndJcqao(0, 0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: notifyCqaeAndJcqaoExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.notifyCqaeAndJcqaoExceptionTest : Finished");
//	}
//	
//	
//	@Test
//	void updateOlAttachmentsValidTest() {
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidTest : Initiated");
//		try {
//			byte[] fileContent = "Mock file content".getBytes();
//			MockMultipartFile[] attachments = new MockMultipartFile[] {
//					new MockMultipartFile("attachments", "filename1.txt", "text/plain", fileContent),
//					new MockMultipartFile("attachments", "filename2.txt", "text/plain", fileContent)
//			};
//			MockMultipartFile[] preInspectionReports = new MockMultipartFile[] {
//					new MockMultipartFile("preInspectionReports", "report1.pdf", "application/pdf", fileContent),
//					new MockMultipartFile("preInspectionReports", "report2.pdf", "application/pdf", fileContent)	
//			};
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(84);
//		    String StringOfferLetter = gson.toJson(offerLetterDto);
//		    when(offerLetterService.updateOlAttachments(any(OfferLetterDto.class), any(MultipartFile[].class), any(MultipartFile[].class), anyString())).thenReturn(offerLetterDto);
//		    ResponseEntity<OfferLetterDto> result = offerLetterController.updateOlAttachments(StringOfferLetter, attachments, preInspectionReports, StringOfferLetter);
//		    assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlAttachmentsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlAttachmentsValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidTest : Finished");
//	}
//	
//	@Test
//	void updateOlAttachmentsValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidSecScenarioTest : Initiated");
//		try {
//			byte[] fileContent = "Mock file content".getBytes();
//			MockMultipartFile[] attachments = new MockMultipartFile[] {
//					new MockMultipartFile("attachments", "filename1.txt", "text/plain", fileContent),
//					new MockMultipartFile("attachments", "filename2.txt", "text/plain", fileContent)
//			};
//			MockMultipartFile[] preInspectionReports = new MockMultipartFile[] {
//					new MockMultipartFile("preInspectionReports", "report1.pdf", "application/pdf", fileContent),
//					new MockMultipartFile("preInspectionReports", "report2.pdf", "application/pdf", fileContent)	
//			};
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(84);
//		    String StringOfferLetter = gson.toJson(offerLetterDto);
//		    when(offerLetterService.updateOlAttachments(any(OfferLetterDto.class), any(MultipartFile[].class), any(MultipartFile[].class), anyString())).thenReturn(null);
//		    ResponseEntity<OfferLetterDto> result = offerLetterController.updateOlAttachments(StringOfferLetter, attachments, preInspectionReports, StringOfferLetter);
//		    assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlAttachmentsValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlAttachmentsValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidSecScenarioTest : Finished");
//	}
//	@Test
//	void updateOlAttachmentsValidTest() {
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidTest : Initiated");
//		try {
//			byte[] fileContent = "Mock file content".getBytes();
//			MockMultipartFile[] attachments = new MockMultipartFile[] {
//					new MockMultipartFile("attachments", "filename1.txt", "text/plain", fileContent),
//					new MockMultipartFile("attachments", "filename2.txt", "text/plain", fileContent)
//			};
//			MockMultipartFile[] preInspectionReports = new MockMultipartFile[] {
//					new MockMultipartFile("preInspectionReports", "report1.pdf", "application/pdf", fileContent),
//					new MockMultipartFile("preInspectionReports", "report2.pdf", "application/pdf", fileContent)	
//			};
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(84);
//		    String StringOfferLetter = gson.toJson(offerLetterDto);
//		    when(offerLetterService.updateOlAttachments(any(OfferLetterDto.class), any(MultipartFile[].class), any(MultipartFile[].class), anyString())).thenReturn(offerLetterDto);
//		    ResponseEntity<OfferLetterDto> result = offerLetterController.updateOlAttachments(StringOfferLetter, attachments, preInspectionReports, StringOfferLetter);
//		    assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlAttachmentsValidTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlAttachmentsValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidTest : Finished");
//	}
//	
//	@Test
//	void updateOlAttachmentsValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidSecScenarioTest : Initiated");
//		try {
//			byte[] fileContent = "Mock file content".getBytes();
//			MockMultipartFile[] attachments = new MockMultipartFile[] {
//					new MockMultipartFile("attachments", "filename1.txt", "text/plain", fileContent),
//					new MockMultipartFile("attachments", "filename2.txt", "text/plain", fileContent)
//			};
//			MockMultipartFile[] preInspectionReports = new MockMultipartFile[] {
//					new MockMultipartFile("preInspectionReports", "report1.pdf", "application/pdf", fileContent),
//					new MockMultipartFile("preInspectionReports", "report2.pdf", "application/pdf", fileContent)	
//			};
//			OfferLetterDto offerLetterDto = new OfferLetterDto();
//			offerLetterDto.setId(84);
//		    String StringOfferLetter = gson.toJson(offerLetterDto);
//		    when(offerLetterService.updateOlAttachments(any(OfferLetterDto.class), any(MultipartFile[].class), any(MultipartFile[].class), anyString())).thenReturn(null);
//		    ResponseEntity<OfferLetterDto> result = offerLetterController.updateOlAttachments(StringOfferLetter, attachments, preInspectionReports, StringOfferLetter);
//		    assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: updateOlAttachmentsValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: updateOlAttachmentsValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.updateOlAttachmentsValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getAttachmentByOlIdValidTest() {
//		logger.info("OfferLetterControllerTest.getAttachmentByOlIdValidTest : Initiated");
//		try {
//			int olId = 84;
//			OlAttachmentDto olAttachDto = new OlAttachmentDto();
//			olAttachDto.setId(olId);
//			when(offerLetterService.getAttachmentsByOlId(anyInt())).thenReturn(olAttachDto);
//			ResponseEntity<OlAttachmentDto> result = offerLetterController.getAttachmentByOlId(olId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAttachmentByOlIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getAttachmentByOlIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getAttachmentByOlIdValidTest : Finished");
//	}
//	
//	@Test
//	void getAttachmentByOlIdExceptionTest() {
//		logger.info("OfferLetterControllerTest.getAttachmentByOlIdExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getAttachmentsByOlId(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<OlAttachmentDto> result = offerLetterController.getAttachmentByOlId(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getAttachmentByOlIdExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getAttachmentByOlIdExceptionTest : Finished");
//	}
//	
//	@Test
//	void deleteOlAttachmentValidTest() {
//		logger.info("OfferLetterControllerTest.deleteOlAttachmentValidTest : Initiated");
//		try {
//			KeyValuePair keyValue = new KeyValuePair();
//			keyValue.setId(1);
//			keyValue.setName("file1");
//			String resp = gson.toJson(keyValue);
//			when(offerLetterService.deleteOlAttachment(anyInt(), anyString())).thenReturn("success");
//			ResponseEntity<String> result = offerLetterController.deleteOlAttachment(resp);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: deleteOlAttachmentValidTest " + e.getMessage());
//			fail("Test failed due to an exception: deleteOlAttachmentValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.deleteOlAttachmentValidTest : Finished");
//	}
//	
//	@Test
//	void deleteOlAttachmentValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.deleteOlAttachmentValidSecScenarioTest : Initiated");
//		try {
//			KeyValuePair keyValue = new KeyValuePair();
//			keyValue.setId(1);
//			keyValue.setName("file1");
//			String resp = gson.toJson(keyValue);
//			when(offerLetterService.deleteOlAttachment(anyInt(), anyString())).thenReturn("failure");
//			ResponseEntity<String> result = offerLetterController.deleteOlAttachment(resp);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: deleteOlAttachmentValidSecScenarioTest " + e.getMessage());
//			fail("Test failed due to an exception: deleteOlAttachmentValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.deleteOlAttachmentValidSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void getOfferLettersByStatusValidTest() {
//		logger.info("OfferLetterControllerTest.getOfferLettersByStatusValidTest : Initiated");
//		try {
//			List<OfferLetterDto> lOfferLetter = new ArrayList<>();
//			OfferLetterDto offerLetter = new OfferLetterDto();
//			offerLetter.setId(12);
//			lOfferLetter.add(offerLetter);
//			lOfferLetter.add(offerLetter);
//			when(offerLetterService.getOfferLettersByStatus(anyInt())).thenReturn(lOfferLetter);
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOfferLettersByStatus( 100303);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLettersByStatusValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOfferLettersByStatusValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLettersByStatusValidTest : Finished");
//	}
//	
//	@Test
//	void getOfferLettersByStatusValidSecScenarioTest() {
//		logger.info("OfferLetterControllerTest.getOfferLettersByStatusValidSecScenarioTest : Initiated");
//		try {
//			when(offerLetterService.getOfferLettersByStatus( anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<List<OfferLetterDto>> result = offerLetterController.getOfferLettersByStatus(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLettersByStatusValidSecScenarioTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLettersByStatusValidSecScenarioTest : Finished");
//	}
//	
//	
//	@Test
//	void getOLCountValidTest() {
//		logger.info("OfferLetterControllerTest.getOLCountValidTest : Initiated");
//		try {
//			int orgId = 100303;
//			OfferLetterStatusDto offerLetterStatusDto = new OfferLetterStatusDto();
//			offerLetterStatusDto.setInProgress(5);
//			when(offerLetterService.getOLCount(anyInt())).thenReturn(offerLetterStatusDto);
//			ResponseEntity<OfferLetterStatusDto> result = offerLetterController.getOLCount(orgId);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLCountValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOLCountValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLCountValidTest : Finished");
//	}
//	
//	@Test
//	void getOLCountExceptionTest() {
//		logger.info("OfferLetterControllerTest.getOLCountExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOLCount(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<OfferLetterStatusDto> result = offerLetterController.getOLCount(0);
//			assertNotNull(result);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLCountExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLCountExceptionTest : Finished");
//	}
//	
//	@Test
//	void getOfferLettersStatusBySalesGrpIdValidTest() {
//		logger.info("OfferLetterControllerTest.getOfferLettersStatusBySalesGrpIdValidTest : Initiated");
//		try {
//			List<OfferLetterDto> lOfferLetter = new ArrayList<>();
//			OfferLetterDto offerLetter = new OfferLetterDto();
//			offerLetter.setId(12);
//			lOfferLetter.add(offerLetter);
//			lOfferLetter.add(offerLetter);
//			when(offerLetterService.getOfferLetterStatusBySalesGrpId( anyInt())).thenReturn(lOfferLetter);
//			ResponseEntity<List<OfferLetterDto>> res = offerLetterController.getOfferLettersStatusBySalesGrpId(100303);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLettersStatusBySalesGrpIdValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOfferLettersStatusBySalesGrpIdValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLettersStatusBySalesGrpIdValidTest : Finished");
//	}
//	
//	@Test
//	void getOfferLettersStatusBySalesGrpIdExceptionTest() {
//		logger.info("OfferLetterControllerTest.getOfferLettersStatusBySalesGrpIdExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOfferLetterStatusBySalesGrpId( anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<List<OfferLetterDto>> res = offerLetterController.getOfferLettersStatusBySalesGrpId( 0);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOfferLettersStatusBySalesGrpIdExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOfferLettersStatusBySalesGrpIdExceptionTest : Finished");
//	}
//	
//	@Test
//	void getOLCountBySalesGrpValidTest() {
//		logger.info("OfferLetterControllerTest.getOLCountBySalesGrpValidTest : Initiated");
//		try {
//			int salesGrpId = 213;
//			OfferLetterStatusDto offerLetterStatusDto = new OfferLetterStatusDto();
//			offerLetterStatusDto.setInProgress(5);
//			when(offerLetterService.getOLCountBySalesGrp(anyInt())).thenReturn(offerLetterStatusDto);
//			ResponseEntity<OfferLetterStatusDto> res = offerLetterController.getOLCountBySalesGrp(salesGrpId);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLCountBySalesGrpValidTest " + e.getMessage());
//			fail("Test failed due to an exception: getOLCountBySalesGrpValidTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLCountBySalesGrpValidTest : Finished");
//	}
//	
//	@Test
//	void getOLCountBySalesGrpExceptionTest() {
//		logger.info("OfferLetterControllerTest.getOLCountBySalesGrpExceptionTest : Initiated");
//		try {
//			when(offerLetterService.getOLCountBySalesGrp(anyInt())).thenThrow(new NullPointerException());
//			ResponseEntity<OfferLetterStatusDto> res = offerLetterController.getOLCountBySalesGrp(0);
//			assertNotNull(res);
//		} catch (Exception e) {
//			logger.error("Exception occurred: getOLCountBySalesGrpExceptionTest " + e.getMessage());
//		}
//		logger.info("OfferLetterControllerTest.getOLCountBySalesGrpExceptionTest : Finished");
//	}
//	
//}
