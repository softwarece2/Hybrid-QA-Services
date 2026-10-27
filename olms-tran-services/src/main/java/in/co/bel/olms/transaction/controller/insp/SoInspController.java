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
import in.co.bel.olms.transaction.dto.insp.AllotSoDto;
import in.co.bel.olms.transaction.service.insp.SoInspService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 08-04-2023
 */

@RestController
@RequestMapping("/soinsp")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoInspController {

	private final SoInspService soInspService;

	private static final Logger logger = LoggerFactory.getLogger(SoInspController.class);
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getSoByInspId(@PathVariable int id) throws DataNotFoundException {		
		try {
			return ResponseEntity.ok(  soInspService.getSoByInspId(id));
		}		
		catch (DataNotFoundException ex) {
			logger.error(" Get so by insp id - Date not found Error: {} ", ex.getMessage());
			return new ResponseEntity<>(ApplicationConstants.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Get so by insp id - Error: {} ", e.getMessage());
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
	public ResponseEntity<String> getSoByInspId(@PathVariable int roleId, @PathVariable int empIid) throws DataNotFoundException {		
		try {
			return ResponseEntity.ok(  soInspService.getSoByRolenEmpId(roleId,empIid));
		}		
		catch (DataNotFoundException ex) {
			logger.error(" Get so by role and emp id - Date not found Error: {} ", ex.getMessage());
			return new ResponseEntity<>(ApplicationConstants.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(" Get so by role and emp id - Error: {} ", e.getMessage());
			return new ResponseEntity<>(ApplicationConstants.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
//	/**
//	 * 
//	 * @param soDto
//	 * @return
//	 */
//	@PostMapping("/allotInsp")
//	public ResponseEntity<AllotSoDto> allotInsp(@RequestBody @Valid SupplyOrderDto soDto) {
//		try {			
//			return new ResponseEntity<>(soInspService.allotInsp(soDto), HttpStatus.CREATED);
//		} catch (DataNotFoundException ex) {
//			logger.error("SO  Allot Inspector  Error: {} ", ex.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.BAD_REQUEST);
//			
//		} catch (Exception e) {
//			logger.error(" SO Allot Inspector Error: {} ", e.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
//	/**
//	 * 
//	 * @param soDto
//	 * @return
//	 */
//	@PostMapping("/allot/secOfficer")
//	public ResponseEntity<AllotSoDto> allotSecOfficer(@RequestBody @Valid SupplyOrderDto soDto) {
//		try {			
//			return new ResponseEntity<>(soInspService.allotSecOfficer(soDto), HttpStatus.CREATED);
//		} catch (DataNotFoundException ex) {
//			logger.error("SO Allot Section officer Error: {}  ", ex.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.BAD_REQUEST);
//			
//		} catch (Exception e) {
//			logger.error(" SO Allot Section officer Error: {} ", e.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
//	/**
//	 * 
//	 * @param soDto
//	 * @return
//	 */
//	//@PreAuthorize("hasAuthority('JCQAO')")
//	@PostMapping("/allot/dcqao")
//	public ResponseEntity<AllotSoDto> allotDcqao(@RequestBody @Valid SupplyOrderDto soDto) {
//		try {			
//			return new ResponseEntity<>(soInspService.allotDcqao(soDto), HttpStatus.CREATED);
//		} catch (DataNotFoundException ex) {
//			logger.error("SO Allot DCQAO Error: {} ", ex.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.BAD_REQUEST);
//			
//		} catch (Exception e) {
//			logger.error(" SO Allot DCQAO Error: {} ", e.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	
//	/**
//	 * 
//	 * @param soDto
//	 * @return
//	 */
//	@PostMapping("/allot/jcqao")
//	public ResponseEntity<AllotSoDto> allotJcqao(@RequestBody @Valid SupplyOrderDto soDto) {
//		try {			
//			return new ResponseEntity<>(soInspService.allotJcqao(soDto), HttpStatus.CREATED);
//		} catch (DataNotFoundException ex) {
//			logger.error("SO Allot JCQAO officer Error: {} ", ex.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.BAD_REQUEST);
//			
//		} catch (Exception e) {
//			logger.error(" SO Allot JCQAO officer Error: {} ", e.getMessage());
//			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	
	/**
	 * 
	 * @param allotOlDto
	 * @return
	 */
	@PostMapping("/allot/subordinate")
	public ResponseEntity<AllotSoDto> allotSubordinate(@RequestBody @Valid AllotSoDto allotOlDto) {
		try {			
			return new ResponseEntity<>(soInspService.allotSubordinate(allotOlDto), HttpStatus.CREATED);
		} catch (DataNotFoundException ex) {
			logger.error(" SO Allot Subordinate Error: {} ", ex.getMessage());
			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error(" SO Allot Subordinate Error: {} ", e.getMessage());
			return new ResponseEntity<>(new AllotSoDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
