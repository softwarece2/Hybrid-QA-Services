package in.co.bel.olms.transaction.controller;

import java.util.Map;

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
import in.co.bel.olms.transaction.dto.SurpriseCheckDto;
import in.co.bel.olms.transaction.service.SurpriseCheckService;
import lombok.RequiredArgsConstructor;

/**
 * @author Awadhesh Verma
 * @since 28-08-2023
 */

@RestController
@RequestMapping("/surpriseCheck")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SurpriseCheckController {

    private final SurpriseCheckService surpriseCheckService;
    
    private static final Logger logger = LoggerFactory.getLogger(SurpriseCheckController.class);

    /**
     * @param surpriseCheckDto
     * @return String
     */

    @PostMapping("/save")
    public ResponseEntity<String> saveSurpriseCheck(@RequestBody @Valid SurpriseCheckDto surpriseCheckDto) {
    	try {
        Map<Boolean, String> response = surpriseCheckService.saveSurpriseCheck(surpriseCheckDto);
	        if (response.containsKey(true)) {
	            String returnString = response.get(true);
	            return new ResponseEntity<>(returnString, HttpStatus.CREATED);
	        } else {
	            String returnString = response.get(false);
	            return new ResponseEntity<>(returnString, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    } catch(DataNotValidException de) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + de.getMessage());
	    } catch(Exception e) {
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
	    }
    }

}
