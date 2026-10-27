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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyPreInspectionDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyPreInspectionDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyPreInspection;
import in.co.bel.olms.transaction.service.check.army.ScrutinyPreInspectionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyPreInspection")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyPreInspectionController {

	private final ScrutinyPreInspectionService scrutinyPreInspectionService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyPreInspectionController.class);
	
	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method saveScrutinyPreInspection
	 * @param ScrutinyPreInspectionDto
	 * @return ResponseEntity<ScrutinyPreInspectionDto>
	 * @throws
	 * @created 11/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinyPreInspectionDto> saveScrutinyPreInspection(@RequestBody @Valid ScrutinyPreInspectionDto scrutinyPreInspectionDto) {
		try {
			logger.info("scrutiny save {}", scrutinyPreInspectionDto);
			ScrutinyPreInspection scrutinyPreInspection = scrutinyPreInspectionService.saveScrutinyPreInspection(scrutinyPreInspectionDto);
			scrutinyPreInspectionDto = mapper.map(scrutinyPreInspection, ScrutinyPreInspectionDto.class);
			 if (scrutinyPreInspectionDto.getId() != 0) {
				 scrutinyPreInspectionDto.setOfferLetterId(scrutinyPreInspection.getOfferLetter().getId());
	            }
			List<ScrutinyPreInspectionDetDto> preInspectionDetDtos = scrutinyPreInspectionService.preparePreInspectionDets(scrutinyPreInspection.getScrutinyPreInspectionDets());
			scrutinyPreInspectionDto.setScrutinyPreInspectionDetDtos(preInspectionDetDtos);
			return new ResponseEntity<>(scrutinyPreInspectionDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {

			logger.error("save ScrutinyPreInspection Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyPreInspectionDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinyPreInspection: ", e);
			logger.error("save ScrutinyPreInspection Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyPreInspectionDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method getBySupplyOrder
	 * @param soid
	 * @return ResponseEntity<ScrutinyPreInspectionDto>
	 * @throws DataNotFoundException
	 * @created 11/05/24
	 * @Mapping Get
	 */
	@GetMapping("/getPreInspection/{olId}")
	public ResponseEntity<ScrutinyPreInspectionDto> getByOfferLetter(@PathVariable int olId) throws DataNotFoundException {
		
		
		ScrutinyPreInspectionDto preInspection = scrutinyPreInspectionService.getScrutinyPreInspectionByOlId(olId);
		return ResponseEntity.ok(preInspection);
	}

	/**
	 * 
	 * @author Prateek Dwivedi
	 * @method submitScrutinyPreInspection
	 * @param ScrutinyPreInspectionDto
	 * @return ResponseEntity<ScrutinyPreInspectionDto>
	 * @throws
	 * @created 11/05/24
	 * @Mapping Post
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinyPreInspectionDto> submitScrutinyPreInspection(@RequestBody @Valid ScrutinyPreInspectionDto scrutinyPreInspectionDto) {
		try {		
			ScrutinyPreInspection scrutinyPreInspection = scrutinyPreInspectionService.submitScrutinyPreInspection(scrutinyPreInspectionDto);
			scrutinyPreInspectionDto = mapper.map(scrutinyPreInspection, ScrutinyPreInspectionDto.class);
			scrutinyPreInspectionDto.setSubmittedDate(dateFormat.format(scrutinyPreInspection.getSubmittedDate()));
			if (scrutinyPreInspectionDto.getId() != 0) {
				scrutinyPreInspectionDto.setOfferLetterId(scrutinyPreInspection.getOfferLetter().getId());
            }
			List<ScrutinyPreInspectionDetDto> preInspectionDetDtos = scrutinyPreInspectionService.preparePreInspectionDets(scrutinyPreInspection.getScrutinyPreInspectionDets());
			scrutinyPreInspectionDto.setScrutinyPreInspectionDetDtos(preInspectionDetDtos);
			return new ResponseEntity<>(scrutinyPreInspectionDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinyPreInspection Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyPreInspectionDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinyPreInspection Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyPreInspectionDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/updateByQAO")
	public ResponseEntity<Boolean> updateByQAO(@RequestParam(value = "scrutinyQacl", required = true) String scrutinyQacl,
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
		
		Gson gson = new Gson();
		ScrutinyPreInspectionDto scrutinyPreInspectionDto = gson.fromJson(scrutinyQacl, new TypeToken<ScrutinyPreInspectionDto>() {
		}.getType());
		
		Boolean returnStatus = scrutinyPreInspectionService.updatePreinspectionByQAO(scrutinyPreInspectionDto , attachments);
		if (returnStatus.booleanValue()) {

			return new ResponseEntity<>(returnStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(returnStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	
}
