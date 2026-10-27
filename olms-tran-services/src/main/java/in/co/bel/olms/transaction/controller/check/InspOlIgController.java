package in.co.bel.olms.transaction.controller.check;

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

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.check.InspOlIgDetDto;
import in.co.bel.olms.transaction.dto.check.InspOlIgDto;
import in.co.bel.olms.transaction.entity.check.InspOlIg;
import in.co.bel.olms.transaction.service.check.InspOlIgService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inspOlIg")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspOlIgController {

	private final InspOlIgService inspOlIgService;

	private final Mapper mapper;
	
	private static final Logger logger = LoggerFactory.getLogger(InspOlIg.class);

	/**
	 * 
	 * @param inspOlIgDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/saveInspOlIg")
	public ResponseEntity<InspOlIgDto> saveInspOlIg(@RequestBody @Valid InspOlIgDto inspOlIgDto) {
		try {
			InspOlIg inspOlIg = inspOlIgService.saveInspOlIg(inspOlIgDto);
			inspOlIgDto = mapper.map(inspOlIg, InspOlIgDto.class);
			List<InspOlIgDetDto> detDto = inspOlIgService.prepareDetBos(inspOlIg.getInspOlIgDets());
			inspOlIgDto.setInspOlIgDetDtos(detDto);
			return new ResponseEntity<>(inspOlIgDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Save Inspection OlIg Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new InspOlIgDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Save Inspection OlIg Error: {} ", e.getMessage());
			return new ResponseEntity<>(new InspOlIgDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param inspOlIgDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/submitInspOlIg")
	public ResponseEntity<String> submitInspOlIg(@RequestBody @Valid InspOlIgDto inspOlIgDto) {
		try {
			inspOlIgService.submitInspOlIg(inspOlIgDto);
			return new ResponseEntity<>(ApplicationConstants.SUCCESS, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Submit Inspection OlIg Error: {} ", ex.getMessage());
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Submit Inspection OlIg Error: {} ", e.getMessage());
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param olid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/ol/{olid}")
	public ResponseEntity<InspOlIgDto> getByOfferLetter(@PathVariable int olid) throws DataNotFoundException {

		InspOlIgDto fcm = inspOlIgService.getByOlId(olid);
		return ResponseEntity.ok(fcm);

	}

}
