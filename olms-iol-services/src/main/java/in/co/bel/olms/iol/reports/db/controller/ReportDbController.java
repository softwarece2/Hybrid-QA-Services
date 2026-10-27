package in.co.bel.olms.iol.reports.db.controller;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import in.co.bel.olms.iol.reports.service.ReportService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/reportMst")
@RequiredArgsConstructor
public class ReportDbController {

	@Value("${RESOURCE_LOCATION}")
	private String resourceLocation;

	private final ReportService reportService;

	private static final Logger logger = LoggerFactory.getLogger(ReportDbController.class);

	Gson gson = new Gson();

	@GetMapping("/getAllProductTypes")
	public ResponseEntity<String> getAllProductTypes() {
		try {
			String result = reportService.getAllProductTypes();
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			logger.error("getAllProductTypes IllegalArgumentException:{}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("failed to getAllProductTypes: {} ", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/getTestByProductType/{productTypeId}")
	public ResponseEntity<String> getTestByProductType(@PathVariable int productTypeId) {
		try {
			String result = reportService.getTestByProductType(productTypeId);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			logger.error("getTestByProductType IllegalArgumentException:{}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("failed to getTestByProductType by Id: {} ", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
