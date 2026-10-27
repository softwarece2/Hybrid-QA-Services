package in.co.bel.olms.transaction.controller.check.army;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyTenderDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyTenderDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTender;
import in.co.bel.olms.transaction.service.check.army.ScrutinyTenderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyTender")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyTenderController {

	private final ScrutinyTenderService scrutinyTenderService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyTenderController.class);

	/**
	 * 
	 * @param scrutinyTenderDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinyTenderDto> saveScrutinyTender(
			@RequestBody @Valid ScrutinyTenderDto scrutinyTenderDto) {
		try {
			logger.info("scrutiny save {}", scrutinyTenderDto);
			ScrutinyTender scrutinyTender = scrutinyTenderService.saveScrutinyTender(scrutinyTenderDto);
			scrutinyTenderDto = mapper.map(scrutinyTender, ScrutinyTenderDto.class);
			List<ScrutinyTenderDetDto> tenderDetDtos = scrutinyTenderService
					.prepareTenderDets(scrutinyTender.getScrutinyTenderDets());
			scrutinyTenderDto.setScrutinyTenderDetDtos(tenderDetDtos);
			return new ResponseEntity<>(scrutinyTenderDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {

			logger.error("save ScrutinyTender Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyTenderDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinyTender: ", e);
			logger.error("save ScrutinyTender Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyTenderDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/tender/{soid}")
	public ResponseEntity<ScrutinyTenderDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		ScrutinyTenderDto result = scrutinyTenderService.getScrutinyTenderBySoId(soid);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/submittedtender/{soid}")
	public ResponseEntity<ScrutinyTenderDto> getSubmittedtenderBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		ScrutinyTenderDto result = scrutinyTenderService.getSubmittedScrutinyTenderBySoId(soid);
		return ResponseEntity.ok(result);
	}

	/**
	 * 
	 * @param scrutinyTenderDto
	 * @return
	 */
//	@PreAuthorize("hasAnyAuthority('EXAMINER', 'INSPECTOR')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinyTenderDto> submitScrutinyTender(
			@RequestBody @Valid ScrutinyTenderDto scrutinyTenderDto) {
		try {
			ScrutinyTender scrutinyTender = scrutinyTenderService.submitScrutinyTender(scrutinyTenderDto);
			scrutinyTenderDto = mapper.map(scrutinyTender, ScrutinyTenderDto.class);
			scrutinyTenderDto.setSubmittedDatetime(dateFormat.format(scrutinyTender.getSubmittedDatetime()));
			List<ScrutinyTenderDetDto> tenderDetDtos = scrutinyTenderService
					.prepareTenderDets(scrutinyTender.getScrutinyTenderDets());
			scrutinyTenderDto.setScrutinyTenderDetDtos(tenderDetDtos);
			return new ResponseEntity<>(scrutinyTenderDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinyTender Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyTenderDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinyTender Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyTenderDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
