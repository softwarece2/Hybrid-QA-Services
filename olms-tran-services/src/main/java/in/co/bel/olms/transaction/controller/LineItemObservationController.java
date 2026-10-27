package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.transaction.dto.CheckOffListMainDto;
import in.co.bel.olms.transaction.dto.LineItemObservationDto;
import in.co.bel.olms.transaction.service.LineItemObservationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lineItemObservation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class LineItemObservationController {

	private static final Logger logger = LoggerFactory.getLogger(LineItemObservationController.class);

	private final LineItemObservationService lineItemObservationService;

	@PostMapping("/save")
	public ResponseEntity<?> saveLineItemObservation(
			@RequestParam(value = "lineItemObservationDtoStr", required = false) String lineItemObservationDtoStr,
			@RequestParam(value = "checkOffListStr", required = false) String checkOffListStr,
			@RequestParam(value = "checkofflist", required = false) MultipartFile[] uploads) {

		Gson gson = new Gson();

		// Parse the incoming DTOs from JSON strings
		LineItemObservationDto lineItemObservationDto = gson.fromJson(lineItemObservationDtoStr,
				new TypeToken<LineItemObservationDto>() {
				}.getType());
		CheckOffListMainDto checkOffListMainDto = gson.fromJson(checkOffListStr, new TypeToken<CheckOffListMainDto>() {
		}.getType());

		if (lineItemObservationDto.getSupplyOrderId() == null) {
			String errorMessage = "supplyOrderId cannot be null or empty";
			return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
		}
		try {
			LineItemObservationDto updatedOrderDto = lineItemObservationService
					.saveLineItemObservation(lineItemObservationDto, checkOffListMainDto, uploads);
			return new ResponseEntity<>(updatedOrderDto, HttpStatus.ACCEPTED);
		} catch (IllegalArgumentException ex) {
			String errorMessage = ex.getMessage();

			if (errorMessage.contains("not found")) {
				return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND); // Supply order not found
			} else {
				return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // Invalid input
			}
		} catch (Exception e) {
			logger.error("Unexpected error in saveLineItemObservation: ", e);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllSubmittedSOsByempId/{sentToId}")
	public ResponseEntity<List<LineItemObservationDto>> getAllSubmittedSOs(@PathVariable int sentToId) {
		try {
			List<LineItemObservationDto> dtos = lineItemObservationService.getAllSubmittedSOs(sentToId);
			return ResponseEntity.ok(dtos);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getByLineItemObservationId/{id}")
	public ResponseEntity<LineItemObservationDto> getByLineItemObservationId(@PathVariable int id) {
		try {
			LineItemObservationDto dto = lineItemObservationService.getByLineItemObservationId(id);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/getInstageByLineItemIds")
	public ResponseEntity<LineItemObservationDto> getInstageByLineItemIds(@RequestParam List<Integer> ids) {
		LineItemObservationDto dto = lineItemObservationService.getInstageByLineItemIds(ids);
		return ResponseEntity.ok(dto);
	}
}
