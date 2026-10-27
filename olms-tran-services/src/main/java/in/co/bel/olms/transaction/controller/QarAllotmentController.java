package in.co.bel.olms.transaction.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.QarAllotmentDto;
import in.co.bel.olms.transaction.entity.QarAllotment;
import in.co.bel.olms.transaction.service.QarAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/qar")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class QarAllotmentController {

	private final QarAllotmentService qarAllotmentService;

	private static final Logger logger = LoggerFactory.getLogger(QarAllotmentController.class);

	@PostMapping("/allotSubordinate")
	public ResponseEntity<QarAllotmentDto> allotSubordinate(@RequestBody @Valid QarAllotmentDto qarAllotmentDto) {

		try {
			QarAllotment olAllotment=qarAllotmentService.allotSubordinate(qarAllotmentDto);
			qarAllotmentDto=qarAllotmentService.convertBotoDto(olAllotment);
			return new ResponseEntity<>(qarAllotmentDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save OlAllotment error: {} ", ex.getMessage());
			return new ResponseEntity<>(new QarAllotmentDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save OlAllotment error: {} ", e.getMessage());
			return new ResponseEntity<>(new QarAllotmentDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
