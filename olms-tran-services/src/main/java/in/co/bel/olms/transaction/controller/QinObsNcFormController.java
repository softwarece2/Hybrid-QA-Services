package in.co.bel.olms.transaction.controller;

import java.util.Map;

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
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.QinObsNcFormDto;
import in.co.bel.olms.transaction.service.QinObsNcFormService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/qinObsNcForm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class QinObsNcFormController {

	private final QinObsNcFormService qinObsNcFormService;

	private static final Logger logger =LoggerFactory.getLogger(QinObsNcFormController.class);
	/**
	 * 
	 * @param qinObsNcFormDto
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<String> saveQinObsNcForm(@RequestBody @Valid QinObsNcFormDto qinObsNcFormDto) {
		try {
		Map<Boolean, String> response = qinObsNcFormService.saveQinObsNcForm(qinObsNcFormDto);
		if(response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);	
		}
		else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		} catch(DataNotValidException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error ocurred: " + de.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error ocurred: " + e.getMessage());
		}
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<String> updateQinObsNcFormAction(@RequestBody @Valid QinObsNcFormDto qinObsNcFormDto) {
		
		Map<Boolean, String> response = qinObsNcFormService.updateQinObsNcFormAction(qinObsNcFormDto);
		if(response.containsKey(true)) {
			String returnString = response.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);	
		}
		else {
			String returnString = response.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@GetMapping("/offerLetter/{offerLetterId}")
	public ResponseEntity<QinObsNcFormDto> getQinNcFormDetails(@PathVariable int offerLetterId) throws DataNotFoundException {

		QinObsNcFormDto qinObsNcFormDto = qinObsNcFormService.getQinNcFormDetails(offerLetterId);
		return ResponseEntity.ok(qinObsNcFormDto);

	}
	
}
