//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.dozer.Mapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsMasterServicesApplication;
//import in.co.bel.olms.master.controller.ChecklistFileCloseController;
//import in.co.bel.olms.master.dto.ChecklistFileCloseDto;
//import in.co.bel.olms.master.entity.ChecklistFileClose;
//import in.co.bel.olms.master.repository.ChecklistFileCloseRepository;
//import in.co.bel.olms.master.service.ChecklistFileCloseService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class ChecklistFileCloseControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(ChecklistFileCloseControllerTest.class);
//	
//	@InjectMocks
//	ChecklistFileCloseController checklistFileCloseController;
//	
//	@Mock
//	ChecklistFileCloseService fileService;
//	
//	@Mock
//	ChecklistFileCloseRepository checklistfilecloseRepository;
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
//	void getAllreferencesValidTest() {
//		logger.info("ChecklistFileCloseControllerTest.getAllreferencesValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			List<ChecklistFileClose> lchListFClose = new ArrayList<>();
//			ChecklistFileClose chListFClose = new ChecklistFileClose();
//			chListFClose.setId(1);
//			lchListFClose.add(chListFClose);
//			lchListFClose.add(chListFClose);
//			when(fileService.getAll()).thenReturn(lchListFClose);
//			when(checklistfilecloseRepository.findAll()).thenReturn(lchListFClose);
//			when(mapper.map(any(ChecklistFileClose.class), eq(ChecklistFileCloseDto.class))).thenReturn(new ChecklistFileCloseDto());
//			ResponseEntity<List<ChecklistFileCloseDto>> result = checklistFileCloseController.getAllreferences();
//			assertNotNull(result);
//			assertNotEquals("[]", result);
//		}, "Test failed due to an exception: getAllreferencesValidTest ");
//		logger.info("ChecklistFileCloseControllerTest.getAllreferencesValidTest : Finished");
//	}
//		
//}
