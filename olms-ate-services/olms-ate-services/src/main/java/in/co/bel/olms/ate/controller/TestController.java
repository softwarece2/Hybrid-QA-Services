package in.co.bel.olms.ate.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.TestDto;
import in.co.bel.olms.ate.service.TestService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	private final TestService testService;
	
	@GetMapping("/getAllTests")
	public ResponseEntity<?> getAllTests(){
		try {
			List<TestDto> res = testService.getAllTests();
			return ResponseEntity.ok().body(res);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("saveFai IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to retreive products", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to retreive tests");
		}
	}
	
	@GetMapping("/prod/{prodId}")
	public ResponseEntity<Object> getTests(@PathVariable Integer prodId){
		try {
			List<TestDto> res = testService.getTestsByProdId(prodId);
			return ResponseEntity.ok().body(res);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

}