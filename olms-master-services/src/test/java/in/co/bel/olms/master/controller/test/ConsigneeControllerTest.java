//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
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
//import org.springframework.test.context.ContextConfiguration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//
//import in.co.bel.olms.OlmsMasterServicesApplication;
//import in.co.bel.olms.master.controller.ConsigneeController;
//import in.co.bel.olms.master.dto.ConsigneeDto;
//import in.co.bel.olms.master.entity.Consignee;
//import in.co.bel.olms.master.repository.ConsigneeRepository;
//import in.co.bel.olms.master.service.ConsigneeService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class ConsigneeControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(ConsigneeControllerTest.class);
//	
//	@InjectMocks
//	ConsigneeController consigneeController;
//	
//	@Mock
//	ConsigneeRepository consigneeRepository;
//	
//	@Mock
//	ConsigneeService consigneeService;
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
//	void getAllConsigneesValidTest() {
//		logger.info("ConsigneeControllerTest.getAllConsigneesValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			List<Consignee> lConsignee = new ArrayList<>();
//			Consignee consignee = new Consignee();
//			consignee.setActive(true);
//			consignee.setId(1);
//			lConsignee.add(consignee);
//			lConsignee.add(consignee);
//			when(consigneeRepository.findAll()).thenReturn(lConsignee);
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(new ConsigneeDto());
//			ResponseEntity<List<ConsigneeDto>> result = consigneeController.getAllConsignees();
//			assertNotNull(result);
//		}, "Test failed due to an exception: getAllConsigneesValidTest ");
//		logger.info("ConsigneeControllerTest.getAllConsigneesValidTest : Finished");
//	}
//	
//	@Test
//	void saveConsigneeConflictTest() {
//		logger.info("ConsigneeControllerTest.saveConsigneeConflictTest : Initiated");
//		assertDoesNotThrow(() -> {
//			ConsigneeDto consigneeDto = new ConsigneeDto();
//			consigneeDto.setName("name1");
//			consigneeDto.setPhone("0123456789");
//			when(consigneeService.existsByname(anyString())).thenReturn(true);
//			when(consigneeRepository.existsByname(anyString())).thenReturn(true);
//			when(consigneeService.existsByphone(anyString())).thenReturn(false);
//			when(consigneeRepository.existsByphone(anyString())).thenReturn(false);
//			when(consigneeService.saveConsignee(any(ConsigneeDto.class))).thenReturn(new Consignee());
//			when(mapper.map(any(ConsigneeDto.class), eq(Consignee.class))).thenReturn(new Consignee());
//			when(consigneeRepository.save(any())).thenReturn(new Consignee());
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(new ConsigneeDto());
//			ResponseEntity<String> result = consigneeController.saveConsignee(consigneeDto);
//			assertNotNull(result);
//			assertEquals("<409 CONFLICT Conflict,Name is already exists,[]>", result.toString());
//		}, "Test failed due to an exception: saveConsigneeConflictTest ");
//		logger.info("ConsigneeControllerTest.saveConsigneeConflictTest : Finished");
//	}
//	
//	@Test
//	void saveConsigneeConflictSecScenarioTest() {
//		logger.info("ConsigneeControllerTest.saveConsigneeConflictSecScenarioTest : Initiated");
//		assertDoesNotThrow(() -> {
//			ConsigneeDto consigneeDto = new ConsigneeDto();
//			consigneeDto.setName("name1");
//			consigneeDto.setPhone("0123456789");
//			when(consigneeService.existsByname(anyString())).thenReturn(false);
//			when(consigneeRepository.existsByname(anyString())).thenReturn(false);
//			when(consigneeService.existsByphone(anyString())).thenReturn(true);
//			when(consigneeRepository.existsByphone(anyString())).thenReturn(true);
//			when(consigneeService.saveConsignee(any(ConsigneeDto.class))).thenReturn(new Consignee());
//			when(mapper.map(any(ConsigneeDto.class), eq(Consignee.class))).thenReturn(new Consignee());
//			when(consigneeRepository.save(any())).thenReturn(new Consignee());
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(new ConsigneeDto());
//			ResponseEntity<String> result = consigneeController.saveConsignee(consigneeDto);
//			assertNotNull(result);
//			assertEquals("<409 CONFLICT Conflict,Phone number is already exists,[]>", result.toString());
//		}, "Test failed due to an exception: saveConsigneeConflictSecScenarioTest ");
//		logger.info("ConsigneeControllerTest.saveConsigneeConflictSecScenarioTest : Finished");
//	}
//	
//	@Test
//	void saveConsigneeValidTest() {
//		logger.info("ConsigneeControllerTest.saveConsigneeValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			ConsigneeDto consigneeDto = new ConsigneeDto();
//			consigneeDto.setName("name1");
//			consigneeDto.setPhone("0123456789");
//			when(consigneeService.existsByname(anyString())).thenReturn(false);
//			when(consigneeRepository.existsByname(anyString())).thenReturn(false);
//			when(consigneeService.existsByphone(anyString())).thenReturn(false);
//			when(consigneeRepository.existsByphone(anyString())).thenReturn(false);
//			when(consigneeService.saveConsignee(any(ConsigneeDto.class))).thenReturn(new Consignee());
//			when(mapper.map(any(ConsigneeDto.class), eq(Consignee.class))).thenReturn(new Consignee());
//			when(consigneeRepository.save(any())).thenReturn(new Consignee());
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(new ConsigneeDto());
//			ResponseEntity<String> result = consigneeController.saveConsignee(consigneeDto);
//			assertNotNull(result);
//		}, "Test failed due to an exception: saveConsigneeValidTest ");
//		logger.info("ConsigneeControllerTest.saveConsigneeValidTest : Finished");
//	}
//	
//	@Test
//	void getConsigneeValidTest() {
//		logger.info("ConsigneeControllerTest.getConsigneeValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			int id = 1;
//			Consignee consignee = new Consignee();
//			consignee.setActive(true);
//			Optional<Consignee> consigneeOptional = Optional.of(consignee);
//			when(consigneeService.getConsignee(anyInt())).thenReturn(consignee);
//			when(consigneeRepository.findById(anyInt())).thenReturn(consigneeOptional);
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(new ConsigneeDto());
//			ResponseEntity<ConsigneeDto> result = consigneeController.getConsignee(id);
//			assertNotNull(result);
//		}, "Test failed due to an exception: getConsigneeValidTest ");
//		logger.info("ConsigneeControllerTest.getConsigneeValidTest : Finished");
//	}
//	
//	@Test
//	void updateConsigneeValidTest() {
//		logger.info("ConsigneeControllerTest.updateConsigneeValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			ConsigneeDto consigneeDto = new ConsigneeDto();
//			consigneeDto.setId(2);
//			consigneeDto.setActive(true);
//			consigneeDto.setAddress("The Project Director, SBC, Krishna Gate, Varuna Block,  Naval Base Post, Visakhapatnam - 530 014");
//			consigneeDto.setFax("021641-157520");
//			consigneeDto.setPhone("8754534343");
//			consigneeDto.setName("SBC-Krishna5");
//			Consignee consignee = new Consignee();
//			consignee.setId(2);
//			consignee.setActive(true);
//			Optional<Consignee> consigneeOptional = Optional.of(consignee);
//			when(consigneeService.getConsignee(anyInt())).thenReturn(consignee);
//			when(consigneeRepository.findById(anyInt())).thenReturn(consigneeOptional);
//			when(consigneeRepository.save(any(Consignee.class))).thenReturn(consignee);
//			when(mapper.map(any(ConsigneeDto.class), eq(Consignee.class))).thenReturn(consignee);
//			when(consigneeService.updateConsignee(any(ConsigneeDto.class))).thenReturn(consignee);
//			when(mapper.map(any(Consignee.class), eq(ConsigneeDto.class))).thenReturn(consigneeDto);
//			ResponseEntity<String> result = consigneeController.updateConsignee(consigneeDto);
//			assertNotEquals("[]", result);
//			assertNotEquals(Exception.class, result.getClass());
//		}, "Test failed due to an exception: updateConsigneeValidTest ");
//		logger.info("ConsigneeControllerTest.updateConsigneeValidTest : Finished");
//	}
//		
//}
