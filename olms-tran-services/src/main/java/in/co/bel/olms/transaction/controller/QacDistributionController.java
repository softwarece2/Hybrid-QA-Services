package in.co.bel.olms.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.QacDistributionDto;
import in.co.bel.olms.transaction.service.QacDistributionService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/qac")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class QacDistributionController {

	private final QacDistributionService qacDistributionService;
	private static final Logger logger = LoggerFactory.getLogger(QacDistributionController.class);
	@PostMapping("/distribution/save")
	public ResponseEntity<String> saveQac(@RequestBody QacDistributionDto qacDistributionDto) throws Exception{
		try {
			ResponseEntity<String> response = qacDistributionService.saveQacDistribution(qacDistributionDto);
			return response;
		}catch(IllegalArgumentException e) {
			logger.error("Invalid argument: {}", e.getMessage(), e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		catch (Exception e) {
		   logger.error("Failed to save: {}", e.getMessage(), e);
			return ResponseEntity.internalServerError().body("Failed to save");
		}
	}
}
