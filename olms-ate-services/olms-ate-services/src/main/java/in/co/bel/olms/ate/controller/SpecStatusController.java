package in.co.bel.olms.ate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.SpecStatusDto;
import in.co.bel.olms.ate.service.SpecStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spec/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SpecStatusController {

	private final SpecStatusService specStatusService;
	
	private static final String MESSAGE = "Error Message: ";

	// Update a single spec by Id
	@PostMapping()
	ResponseEntity<String> updateSpecStatus(@RequestBody @Valid SpecStatusDto specStatusDto) {

		try {
			String response = specStatusService.updateSpecStatus(specStatusDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			return ResponseEntity.badRequest().body(ae.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}
	
	// Update multiple specs
	@PostMapping("/update")
	ResponseEntity<String> updateSpecStatusMany(@RequestBody SpecStatusDto specStatusDto) {

		try {
			String response = specStatusService.updateSpecStatusMany(specStatusDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			return ResponseEntity.badRequest().body(ae.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}

	// fetches sepc status updates about all specs that has been updated for a test Id
	@GetMapping("/history")
	ResponseEntity<Map<String, Object>> getAllSpecUpdateHistory(@RequestParam(required = false) Integer testId) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<SpecStatusDto> getAllSpecStatus = specStatusService.getAllSpecUpdateHistory(testId);
			response.put("specStatusDtos", getAllSpecStatus);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			response.put(MESSAGE, ae.getMessage());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	
	// fetches sepc-status updates about a single specId
	@GetMapping("/{specId}")
	ResponseEntity<Map<String, Object>> getAllSpecStatus(@PathVariable("specId") @Valid Integer specId) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<SpecStatusDto> getAllSpecStatus = specStatusService.getAllSpecStatus(specId);
			response.put("specStatusDtos", getAllSpecStatus);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			response.put(MESSAGE, ae.getMessage());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.put(MESSAGE, e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}

	}

}
