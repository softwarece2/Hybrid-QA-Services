//package in.co.bel.olms.master.controller.test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
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
//import in.co.bel.olms.master.controller.CaptchaController;
//import in.co.bel.olms.master.dto.CaptchaDto;
//import in.co.bel.olms.master.entity.LoginCaptcha;
//import in.co.bel.olms.master.repository.CaptchaRepository;
//import in.co.bel.olms.master.service.CaptchaService;
//
//@SpringBootTest
//@ContextConfiguration(classes = OlmsMasterServicesApplication.class)
//class CaptchaControllerTest {
//
//	private static final Logger logger = LoggerFactory.getLogger(CaptchaControllerTest.class);
//	
//	@InjectMocks
//	CaptchaController captchaController;
//	
//	@Mock
//	CaptchaService captchaService;
//	
//	@Mock
//	CaptchaRepository captchaRepository;
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
//	void getCaptchaValValidTest() {
//		logger.info("CaptchaControllerTest.getCaptchaValValidTest : Initiated");
//		assertDoesNotThrow(() -> {
//			LoginCaptcha savedCaptcha = new LoginCaptcha();
//			savedCaptcha.setCaptchaId(1);
//			savedCaptcha.setCaptchaVal("2mexp");
//			when(captchaService.saveCaptcha(any(LoginCaptcha.class))).thenReturn(savedCaptcha);
//			when(captchaRepository.save(any())).thenReturn(savedCaptcha);
//			ResponseEntity<CaptchaDto> result = captchaController.getCaptchaVal();
//			assertNotNull(result);
//		}, "Test failed due to an exception: getCaptchaValValidTest ");
//		logger.info("CaptchaControllerTest.getCaptchaValValidTest : Finished");
//	}
//	 	
//}
