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
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclDetDto;
import in.co.bel.olms.transaction.dto.check.army.ScrutinyQaclDto;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQacl;
import in.co.bel.olms.transaction.service.check.army.ScrutinyQaclService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/scrutinyQacl")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ScrutinyQaclController {
	
	private final ScrutinyQaclService qaclService;

	private final Mapper mapper;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final Logger logger = LoggerFactory.getLogger(ScrutinyQaclController.class);


	/**
	 * 
	 * @param ScrutinyQaclDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/save")
	public ResponseEntity<ScrutinyQaclDto> saveScrutinyQacl(@RequestBody @Valid ScrutinyQaclDto scrutinyQaclDto) {
		try {
			logger.info("scrutiny save {}", scrutinyQaclDto);
			ScrutinyQacl scrutinyQacl = qaclService.saveScrutinyQacl(scrutinyQaclDto);
			scrutinyQaclDto = mapper.map(scrutinyQacl, ScrutinyQaclDto.class);
			List<ScrutinyQaclDetDto> qaclDetDtos = qaclService.prepareQaclDets(scrutinyQacl.getScrutinyQaclDets());
			scrutinyQaclDto.setQaclDetDtos(qaclDetDtos);
			return new ResponseEntity<>(scrutinyQaclDto, HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error("save ScrutinyQacl Error: {}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyQaclDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving scrutinyQacl: ", e);
			logger.error("save ScrutinyQacl Error:{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyQaclDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param ScrutinyQaclDto
	 * @return
	 */
//	@PreAuthorize("hasAnyAuthority('INSP')")
	@PostMapping("/submit")
	public ResponseEntity<ScrutinyQaclDto> submitScrutinyQacl(@RequestBody @Valid ScrutinyQaclDto scrutinyQaclDto) {
		try {		
			ScrutinyQacl scrutinyQacl = qaclService.submitScrutinyQacl(scrutinyQaclDto);
			scrutinyQaclDto = mapper.map(scrutinyQacl, ScrutinyQaclDto.class);
			scrutinyQaclDto.setSubmittedDate(dateFormat.format(scrutinyQacl.getSubmittedDate()));
			List<ScrutinyQaclDetDto> qaclDetDtos = qaclService.prepareQaclDets(scrutinyQacl.getScrutinyQaclDets());
			scrutinyQaclDto.setQaclDetDtos(qaclDetDtos);
			return new ResponseEntity<>(scrutinyQaclDto, HttpStatus.CREATED);

		} catch (DataNotFoundException ex) {

			logger.error("submit ScrutinyQacl Error :{}", ex.getMessage());
			return new ResponseEntity<>(new ScrutinyQaclDto(), HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			logger.error("submit ScrutinyQacl Error :{}", e.getMessage());
			return new ResponseEntity<>(new ScrutinyQaclDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * 
	 * @param olid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/ol/{olid}")
	public ResponseEntity<ScrutinyQaclDto> getByOfferLetterId(@PathVariable int olid) throws DataNotFoundException {
		ScrutinyQaclDto dto = qaclService.getScrutinyQaclByOlId(olid);
		return ResponseEntity.ok(dto);
	}


	
	@PostMapping("/updateQaclByQAO")
	public ResponseEntity<Boolean> updateQaclByQAO(@RequestParam(value = "scrutinyQacl", required = true) String scrutinyQacl,
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
		
		Gson gson = new Gson();
		ScrutinyQaclDto scrutinyQaclDto = gson.fromJson(scrutinyQacl, new TypeToken<ScrutinyQaclDto>() {
		}.getType());
		
		Boolean returnStatus = qaclService.updateQaclByQAO(scrutinyQaclDto , attachments);
		if (returnStatus.booleanValue()) {

			return new ResponseEntity<>(returnStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(returnStatus, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
