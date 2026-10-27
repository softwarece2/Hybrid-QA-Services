//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyInt;
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
//import in.co.bel.olms.master.controller.AccountController;
//import in.co.bel.olms.master.dto.KeyValuePair;
//import in.co.bel.olms.master.entity.Account;
//import in.co.bel.olms.master.repository.AccountRepository;
//import in.co.bel.olms.master.service.AccountService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class AccountControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(AccountControllerTest.class);
//	
//	@InjectMocks
//	AccountController accountController;
//	
//	@Mock
//	AccountRepository accountRepository;
//	
//	@Mock
//	AccountService accountService;
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
//	void getAccountByIdValidTest() {
//		logger.info("AccountControllerTest.getAccountByIdValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			int id = 1;
//			Account account = new Account();
//			account.setId(1);
//			account.setAcctType("Govt");
//			Optional<Account> optData = Optional.of(account);
//			when(accountService.getAccountById(anyInt())).thenReturn(account);
//			when(accountRepository.findById(anyInt())).thenReturn(optData);
//			ResponseEntity<KeyValuePair> result = accountController.getAccountById(id);
//			assertNotNull(result);
//		}, "Test failed due to an exception: getAccountByIdValidTest ");
//		logger.info("AccountControllerTest.getAccountByIdValidTest : Finished");
//	}
//	
//	 @Test
//	 void getAllEqptGroupValidTest() {
//		logger.info("AccountControllerTest.getAllEqptGroupValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			List<Account> accounts = new ArrayList<>();
//			Account account = new Account();
//			account.setId(1);
//			account.setAcctType("Govt");
//			when(accountService.getAllAccounts()).thenReturn(accounts);
//			when(accountRepository.findAll()).thenReturn(accounts);
//			ResponseEntity<List<KeyValuePair>> result = accountController.getAllEqptGroup();
//			assertNotNull(result);
//			assertNotEquals("[]", result);
//			}, "Test failed due to an exception: getAllEqptGroupValidTest ");
//			logger.info("AccountControllerTest.getAllEqptGroupValidTest : Finished");
//	 }
//	 
//}
