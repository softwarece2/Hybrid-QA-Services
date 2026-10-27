package in.co.bel.olms.transaction.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.TmrResultDto;
import in.co.bel.olms.transaction.service.TmrResultService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tmrResult")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TmrResultController {

	private final TmrResultService resultService;
	
	private static final Logger logger = LoggerFactory.getLogger(TmrResultController.class);

	@PostMapping("/save")
	public ResponseEntity<String> saveTmrResult(@RequestBody @Valid List<TmrResultDto> dtos) {

		try {
			resultService.saveTmrResult(dtos);
			return new ResponseEntity<>(ApplicationConstants.SUCCESS, HttpStatus.CREATED);
		} catch (DataNotValidException e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getByLineItemId/{olLineItemId}")
	public ResponseEntity<List<TmrResultDto>> getTmrResultByOlLineItemId(@PathVariable int olLineItemId) throws DataNotFoundException {
		List<TmrResultDto> resultDtos = resultService.getByOlLineItemId(olLineItemId);
		return ResponseEntity.ok(resultDtos);
	}
	
	@GetMapping("/getByCreatedBy/{createdBy}/{olLineItemId}")
	public ResponseEntity<List<TmrResultDto>> getTmrResultByCreatedBy(@PathVariable String createdBy, @PathVariable int olLineItemId) throws DataNotFoundException {
		List<TmrResultDto> resultDtos = resultService.getByCreatedBy(createdBy, olLineItemId);
		return ResponseEntity.ok(resultDtos);
	}

}
