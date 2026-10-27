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
import in.co.bel.olms.transaction.dto.check.army.ScrutinyGiFormDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyGiFormDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyGiForm;
import in.co.bel.olms.transaction.service.check.army.ScrutinyGiFormService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyGiForm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyGiFormController {

	private final ScrutinyGiFormService scrutinyGiFormService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyGiFormController.class);
	
	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method saveScrutinyGiForm
	 * @param ScrutinyGiFormDto
	 * @return ResponseEntity<ScrutinyGiFormDto>
	 * @throws
	 * @created 09/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinyGiFormDto> saveScrutinyGiForm(@RequestBody @Valid ScrutinyGiFormDto scrutinyGiFormDto) {
		try {
			logger.info("scrutiny save {}", scrutinyGiFormDto);
			ScrutinyGiForm scrutinyGiForm = scrutinyGiFormService.saveScrutinyGiForm(scrutinyGiFormDto);
			scrutinyGiFormDto = mapper.map(scrutinyGiForm, ScrutinyGiFormDto.class);
			List<ScrutinyGiFormDetDto> giFormDetDtos = scrutinyGiFormService.prepareGiFormDets(scrutinyGiForm.getScrutinyGiFormDets());
			scrutinyGiFormDto.setScrutinyGiFormDetDtos(giFormDetDtos);
			return new ResponseEntity<>(scrutinyGiFormDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {

			logger.error("save ScrutinyGiForm Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyGiFormDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinyGiForm: ", e);
			logger.error("save ScrutinyGiForm Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyGiFormDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method getBySupplyOrder
	 * @param soid
	 * @return ResponseEntity<ScrutinyGiFormDto>
	 * @throws DataNotFoundException
	 * @created 09/05/24
	 * @Mapping Get
	 */
	@GetMapping("/getGiForm/{soid}")
	public ResponseEntity<ScrutinyGiFormDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		
		
		ScrutinyGiFormDto giForm = scrutinyGiFormService.getScrutinyGiFormBySoId(soid);
		return ResponseEntity.ok(giForm);
	}

	
	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method submitScrutinyGiForm
	 * @param ScrutinyGiFormDto
	 * @return ResponseEntity<ScrutinyGiFormDto>
	 * @throws
	 * @created 09/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinyGiFormDto> submitScrutinyGiForm(@RequestBody @Valid ScrutinyGiFormDto scrutinyGiFormDto) {
		try {		
			ScrutinyGiForm scrutinyGiForm = scrutinyGiFormService.submitScrutinyGiForm(scrutinyGiFormDto);
			scrutinyGiFormDto = mapper.map(scrutinyGiForm, ScrutinyGiFormDto.class);
			scrutinyGiFormDto.setSubmittedDate(dateFormat.format(scrutinyGiForm.getSubmittedDate()));
			List<ScrutinyGiFormDetDto> giFormDetDtos = scrutinyGiFormService.prepareGiFormDets(scrutinyGiForm.getScrutinyGiFormDets());
			scrutinyGiFormDto.setScrutinyGiFormDetDtos(giFormDetDtos);
			return new ResponseEntity<>(scrutinyGiFormDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinyGiForm Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyGiFormDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinyGiForm Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyGiFormDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
