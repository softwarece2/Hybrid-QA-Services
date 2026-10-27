package in.co.bel.olms.transaction.controller;

import java.util.Map;

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
import in.co.bel.olms.transaction.dto.ClearanceLetterDto;
import in.co.bel.olms.transaction.service.ClearanceLetterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clearanceLetter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CleranceLetterController {
	
	private final ClearanceLetterService clearanceLetterService;

	private static final Logger logger =LoggerFactory.getLogger( CleranceLetterController.class);

	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody ClearanceLetterDto clearanceLetterDto) {
		try {
		Map<Boolean, String> savedStatus  = clearanceLetterService.save(clearanceLetterDto);
		if (savedStatus.containsKey(true)) {
			String returnString = savedStatus.get(true);
			return new ResponseEntity<>(returnString, HttpStatus.CREATED);
		} else {
			String returnString = savedStatus.get(false);
			return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		} catch(DataNotValidException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + de.getMessage());
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
		}
	}
	
	@GetMapping("getByOlId/{id}")
	public ResponseEntity<ClearanceLetterDto> getClearanceLetter(@PathVariable int id) throws DataNotFoundException {		
     	ClearanceLetterDto clearanceLetterDto =clearanceLetterService.getClearanceLetter(id);
		return ResponseEntity.ok(clearanceLetterDto);
	}
	
	@GetMapping("getAddressDetails/getByOlId/{id}")
	public ResponseEntity<ClearanceLetterDto> getAddressDetails(@PathVariable int id) throws DataNotFoundException {		
     	ClearanceLetterDto clearanceLetterDto =clearanceLetterService.getAddress(id);
		return ResponseEntity.ok(clearanceLetterDto);
	}
}
