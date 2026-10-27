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

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.AolAodDto;
import in.co.bel.olms.transaction.service.AolAodService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/aolaod")
@CrossOrigin(origins="*",allowedHeaders="*")
@RequiredArgsConstructor
public class AolAodController {
	
	private final AolAodService aolAodService;
	
	private static final Logger logger =LoggerFactory.getLogger( AolAodController.class);

	@PostMapping("/save")
	public ResponseEntity<Boolean> saveAmendmentsLetterData(@RequestBody  @Valid AolAodDto aolAodDto){
		Boolean savedStatus=aolAodService.saveData(aolAodDto);
		if(savedStatus.booleanValue()) {
			return new ResponseEntity<>(savedStatus,HttpStatus.CREATED);			
		}
		else {			
			return new ResponseEntity<>(savedStatus,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@GetMapping("/getAllByOrgId/{orgId}")
	public ResponseEntity< List<AolAodDto>> getBySoId(@PathVariable int orgId)throws DataNotFoundException {	
		List<AolAodDto> aolAodDtos=aolAodService.getAllByOrgId(orgId);
		return ResponseEntity.ok(aolAodDtos);	
	}
}
