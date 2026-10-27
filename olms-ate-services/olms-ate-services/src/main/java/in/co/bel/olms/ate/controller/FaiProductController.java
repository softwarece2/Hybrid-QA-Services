package in.co.bel.olms.ate.controller;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.ate.dto.FaiProductDto;
import in.co.bel.olms.ate.dto.ObservationDto;
import in.co.bel.olms.ate.service.DateUtils;
import in.co.bel.olms.ate.service.FaiProductService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/fai")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FaiProductController {
	
	private final Logger logger = LoggerFactory.getLogger(FaiProductController.class);

	private final FaiProductService faiProductService;
	
	Gson gson = new Gson();
	
	@PostMapping("/saveFai")
	public ResponseEntity<String> saveFai(@RequestBody FaiProductDto dto) throws Exception {
		logger.debug("Inside saveFai: {}" , dto.toString());
		try {
			boolean isSubmit = false;
			ResponseEntity<String> res = faiProductService.saveAteObs(dto, isSubmit);
			return res;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			logger.error("saveFai IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to save director fire D7 details", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to save  FAI details");
		}
	}


	@PostMapping("/submitFai")
	public ResponseEntity<String> submitFai(@RequestBody  FaiProductDto dto ) throws Exception {
		try {
			boolean isSubmit = true;
			for (ObservationDto obDto : dto.getDtoList()) {
				obDto.setSubmittedDate(DateUtils.formatDate(new Date()).toString());
			}
			ResponseEntity<String> res = faiProductService.submitAteObs(dto, isSubmit);
			return res;
		} catch (IllegalArgumentException e) {
			logger.error("submitFai IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to submit director fire D7 records", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to submit  FAI records");
		}
	}
	
	@GetMapping("getObsByPageNo/{pageNo}")
	public ResponseEntity<String> getObsByPageNo(@PathVariable Integer pageNo) throws DataNotFoundException {
		List<ObservationDto> dtos = faiProductService.getObsByPageNo(pageNo);
		return ResponseEntity.ok(gson.toJson(dtos));
	}


	@GetMapping("getObsByFaiId/{faiId}/{pageNo}")
	public ResponseEntity<String> getObsByPageNo(@PathVariable Integer faiId, @PathVariable Integer pageNo) throws DataNotFoundException {
		List<ObservationDto> dtos = faiProductService.getObsByFaiId(faiId, pageNo);
		return ResponseEntity.ok(gson.toJson(dtos));
	}


}
