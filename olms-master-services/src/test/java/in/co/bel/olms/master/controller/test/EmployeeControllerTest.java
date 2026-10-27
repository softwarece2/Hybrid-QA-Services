//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
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
//import in.co.bel.olms.master.controller.EmployeeController;
//import in.co.bel.olms.master.dto.EmployeeDto;
//import in.co.bel.olms.master.entity.Employee;
//import in.co.bel.olms.master.repository.EmployeeRepository;
//import in.co.bel.olms.master.service.EmployeeService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class EmployeeControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(EmployeeControllerTest.class);
//	
//	@InjectMocks
//	EmployeeController employeeController;
//	
//	@Mock
//	EmployeeService employeeService;
//	
//	@Mock
//	EmployeeRepository employeeRepository;
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
//	void getEmployeeByIdValidTest() {
//		logger.info("EmployeeControllerTest.getEmployeeByIdValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			int id = 1;
//			Employee employee = new Employee();
//			employee.setCreatedBy(2);
//			employee.setId(1);
//			Optional<Employee> optDataEmp = Optional.of(employee);
//			when(employeeService.getEmployeeById(anyInt())).thenReturn(employee);
//			when(employeeRepository.findById(anyInt())).thenReturn(optDataEmp);
//			when(mapper.map(any(Employee.class), eq(EmployeeDto.class))).thenReturn(new EmployeeDto());
//			ResponseEntity<EmployeeDto> result = employeeController.getEmployeeById(id);
//			assertNotNull(result);
//		}, "Test failed due to an exception: getEmployeeByIdValidTest ");
//		logger.info("EmployeeControllerTest.getEmployeeByIdValidTest : Finished");
//	}
//	
//}
