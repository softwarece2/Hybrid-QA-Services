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
import in.co.bel.olms.transaction.dto.check.army.ScrutinyThreeTierDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyThreeTierDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyThreeTier;
import in.co.bel.olms.transaction.service.check.army.ScrutinyThreeTierService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyThreeTier")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyThreeTierController {

	private final ScrutinyThreeTierService scrutinyThreeTierService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	private static final Logger logger = LoggerFactory.getLogger(ScrutinyThreeTierController.class);

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method saveScrutinyThreeTier
	 * @param ScrutinyThreeTierDto
	 * @return ResponseEntity<ScrutinyThreeTierDto>
	 * @throws
	 * @created 11/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinyThreeTierDto> saveScrutinyThreeTier(
			@RequestBody @Valid ScrutinyThreeTierDto scrutinyThreeTierDto) {
		try {
			logger.info("scrutiny save {}", scrutinyThreeTierDto);
			ScrutinyThreeTier scrutinyThreeTier = scrutinyThreeTierService.saveScrutinyThreeTier(scrutinyThreeTierDto);
			scrutinyThreeTierDto = mapper.map(scrutinyThreeTier, ScrutinyThreeTierDto.class);
			 if (scrutinyThreeTierDto.getId() != 0) {
	                scrutinyThreeTierDto.setOfferLetterId(scrutinyThreeTier.getOfferLetter().getId());
	            }
			List<ScrutinyThreeTierDetDto> threeTierDetDtos = scrutinyThreeTierService
					.prepareThreeTierDets(scrutinyThreeTier.getScrutinyThreeTierDets());
			scrutinyThreeTierDto.setScrutinyThreeTierDetDtos(threeTierDetDtos);
			return new ResponseEntity<>(scrutinyThreeTierDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {

			logger.error("save ScrutinyThreeTier Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyThreeTierDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinyThreeTier: ", e);
			logger.error("save ScrutinyThreeTier Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyThreeTierDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method getByOfferLetter
	 * @param olid
	 * @return ResponseEntity<ScrutinyThreeTierDto>
	 * @throws DataNotFoundException
	 * @created 11/05/24
	 * @Mapping Get
	 */
	@GetMapping("/getThreeTier/{olid}")
	public ResponseEntity<ScrutinyThreeTierDto> getByOfferLetter(@PathVariable int olid) throws DataNotFoundException {

		ScrutinyThreeTierDto threeTier = scrutinyThreeTierService.getScrutinyThreeTierByOlId(olid);
		return ResponseEntity.ok(threeTier);
	}

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method submitScrutinyThreeTier
	 * @param ScrutinyThreeTierDto
	 * @return ResponseEntity<ScrutinyThreeTierDto>
	 * @throws
	 * @created 11/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinyThreeTierDto> submitScrutinyThreeTier(
			@RequestBody @Valid ScrutinyThreeTierDto scrutinyThreeTierDto) {
		try {
			ScrutinyThreeTier scrutinyThreeTier = scrutinyThreeTierService
					.submitScrutinyThreeTier(scrutinyThreeTierDto);
			scrutinyThreeTierDto = mapper.map(scrutinyThreeTier, ScrutinyThreeTierDto.class);
			scrutinyThreeTierDto.setSubmittedDate(dateFormat.format(scrutinyThreeTier.getSubmittedDate()));
			if (scrutinyThreeTierDto.getId() != 0) {
                scrutinyThreeTierDto.setOfferLetterId(scrutinyThreeTier.getOfferLetter().getId());
            }
			List<ScrutinyThreeTierDetDto> threeTierDetDtos = scrutinyThreeTierService
					.prepareThreeTierDets(scrutinyThreeTier.getScrutinyThreeTierDets());
			scrutinyThreeTierDto.setScrutinyThreeTierDetDtos(threeTierDetDtos);
			return new ResponseEntity<>(scrutinyThreeTierDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinyThreeTier Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyThreeTierDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinyThreeTier Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyThreeTierDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
