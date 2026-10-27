package in.co.bel.olms.transaction.controller.insp;

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

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.insp.AllotOlDto;
import in.co.bel.olms.transaction.service.insp.OlInspService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */

@RestController
@RequestMapping("/olinsp")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlInspController {

	private final OlInspService olInspService;
	
	private static final Logger logger = LoggerFactory.getLogger(OlInspController.class);
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<String> getOlByInspId(@PathVariable int id) {	
		try {
			return ResponseEntity.ok( olInspService.getOlByInspId(id));
		}		
		catch (DataNotFoundException ex) {
			logger.error(" Get ol by insp id - Date not found Error: {} ", ex.getMessage());
			return new ResponseEntity<>(ApplicationConstants.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Get ol by insp id - Error: {} ", e.getMessage());
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{roleId}/{empIid}")
	public ResponseEntity<String> getolByInspId(@PathVariable int roleId, @PathVariable int empIid) throws DataNotFoundException {		
		try {
			return ResponseEntity.ok(  olInspService.getOlByRolenEmpId(roleId,empIid));
		}		
		catch (DataNotFoundException ex) {
			logger.error(" Get so by role and emp id - Date not found Error: {} ", ex.getMessage());
			return new ResponseEntity<>(ApplicationConstants.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Get so by role and emp id - Error: {} ", e.getMessage());
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/allotInsp")
	public ResponseEntity<OfferLetterDto> allotInsp(@RequestBody @Valid OfferLetterDto soDto) {
		try {			
			return new ResponseEntity<>(olInspService.allotInsp(soDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Allot Inspector  Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new OfferLetterDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" Allot Inspector Error: {} ", e.getMessage());
			return new ResponseEntity<>(new OfferLetterDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * @param soDto
	 * @return
	 */
	@PostMapping("/allot/secOfficer")
	public ResponseEntity<AllotOlDto> allotSecOfficer(@RequestBody @Valid OfferLetterDto soDto) {
		try {			
			return new ResponseEntity<>(olInspService.allotSecOfficer(soDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Allot Section officer Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" Allot Section officer Error: {} ", e.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * @param soDto
	 * @return
	 */
	@PostMapping("/allot/dcqao")
	public ResponseEntity<AllotOlDto> allotDcqao(@RequestBody @Valid OfferLetterDto soDto) {
		try {			
			return new ResponseEntity<>(olInspService.allotDcqao(soDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Allot Dcqao Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" Allot Dcqao Error: {} ", e.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * @param soDto
	 * @return
	 */
	@PostMapping("/allot/jcqao")
	public ResponseEntity<AllotOlDto> allotJcqao(@RequestBody @Valid OfferLetterDto soDto) {
		try {			
			return new ResponseEntity<>(olInspService.allotJcqao(soDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" Allot Jcqao Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" Allot Jcqao Error: {} ", e.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * 
	 * @param allotOlDto
	 * @return
	 */
	@PostMapping("/allot/subordinate")
	public ResponseEntity<AllotOlDto> allotSubordinate(@RequestBody @Valid AllotOlDto allotOlDto) {
		try {			
			return new ResponseEntity<>(olInspService.allotSubordinate(allotOlDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" OL Allot Subordinate Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" OL Allot Subordinate Error: {} ", e.getMessage());
			return new ResponseEntity<>(new AllotOlDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
