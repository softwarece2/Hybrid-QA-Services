package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.MergeSubSlipAllotmentDto;
import in.co.bel.olms.transaction.entity.SubSlipAllotment;
import in.co.bel.olms.transaction.service.MergedSubSlipAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/mergeSubSlip")
@RequiredArgsConstructor
public class MergeSubSlipAllotmentController {
	
	private final MergedSubSlipAllotmentService mergedSubSlipAllotmentService;
	
	private static final Logger logger = LoggerFactory.getLogger(MergeSubSlipAllotmentController.class);
	
	@PostMapping("/allotSubordinate")
	public ResponseEntity<List<MergeSubSlipAllotmentDto>> allotSubordinate(@RequestBody @Valid MergeSubSlipAllotmentDto olAllotmentDto) {
		try {
			if (olAllotmentDto.getAllotedTo() == null || olAllotmentDto.getAllotedTo().isEmpty()) {
	            logger.error("AllotedTo list cannot be null or empty in Submission slip allotment.");
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	        }
			List<SubSlipAllotment> olAllotment=mergedSubSlipAllotmentService.allotSubordinate(olAllotmentDto);
			List< MergeSubSlipAllotmentDto> allotmentDtos = new ArrayList<>();
			for(SubSlipAllotment allotment : olAllotment) {
				 MergeSubSlipAllotmentDto dto = new  MergeSubSlipAllotmentDto();
				olAllotmentDto.setId(allotment.getId());
				allotmentDtos.add(dto);
			}
			return new ResponseEntity<>(allotmentDtos, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save SubSlipAllotment error - DataNotValidException: {} ", ex.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save SubSlipAllotment error - Exception: {} ", e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
