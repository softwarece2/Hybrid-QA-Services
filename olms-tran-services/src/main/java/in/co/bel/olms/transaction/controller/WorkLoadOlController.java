package in.co.bel.olms.transaction.controller;
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
import in.co.bel.olms.transaction.dto.WorkLoadOlDto;
import in.co.bel.olms.transaction.entity.WorkLoadOl;
import in.co.bel.olms.transaction.service.WorkLoadServiceOl;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 17-02-2023
 */

@RestController
@RequestMapping("/workloadol")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WorkLoadOlController {

	
	/**
	 * 
	 */
	private final WorkLoadServiceOl workLoadOlService;
		
	private static final Logger logger = LoggerFactory.getLogger(WorkLoadOlController.class);
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WorkLoadOlDto> getWorkLoadByID(@PathVariable int id )throws DataNotFoundException{
		WorkLoadOl workLoad = workLoadOlService.getWorkLoadById(id);
		WorkLoadOlDto workLoadDto =workLoadOlService.convertBoToDto(workLoad);
		return ResponseEntity.ok(workLoadDto);

	}
	
	/**
	 * 
	 * @param olid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/ol/{olid}")
	public ResponseEntity<WorkLoadOlDto> getByOfferLetter(@PathVariable int olid) throws DataNotFoundException {
		try {
			WorkLoadOlDto workLoad = workLoadOlService.getWorkLoadByOlId(olid);
			return ResponseEntity.ok(workLoad);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());			
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.BAD_REQUEST);
		}	catch (Exception e) {
			 logger.error("Unexpected error retrieving workload for Offer Letter ID {}: ", olid, e);
			logger.error(e.getMessage());			
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	/**
	 * Save the work load details for the offer letter
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/plan/save")
	public ResponseEntity<WorkLoadOlDto> savePlan(@RequestBody @Valid WorkLoadOlDto workLoadDto){
		try {
			WorkLoadOl workLoad =workLoadOlService.savePlan(workLoadDto);
			WorkLoadOlDto savedworkLoadDto = workLoadOlService.convertBoToDto(workLoad);
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	
	/**Todo:
	 * submit the work load details for the offer letter
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/plan/submit")
	public ResponseEntity<WorkLoadOlDto> submitPlan(@RequestBody @Valid WorkLoadOlDto workLoadDto){

		
		try {
			WorkLoadOl workLoad =workLoadOlService.submitPlan(workLoadDto);
			WorkLoadOlDto savedworkLoadDto = workLoadOlService.convertBoToDto(workLoad);
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
		/**
	 * Save the work load details for the offer letter
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/act/save")
	public ResponseEntity<WorkLoadOlDto> saveActual(@RequestBody @Valid WorkLoadOlDto workLoadDto){		
		try {
			WorkLoadOl workLoad =workLoadOlService.saveAct(workLoadDto);
			WorkLoadOlDto savedworkLoadDto = workLoadOlService.convertBoToDto(workLoad);
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	
	/**Todo:
	 * submit the work load details for the offer letter
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/act/submit")
	public ResponseEntity<WorkLoadOlDto> submitAct(@RequestBody @Valid WorkLoadOlDto workLoadDto){
		try {
			WorkLoadOl workLoad =workLoadOlService.submitAct(workLoadDto);
			WorkLoadOlDto savedworkLoadDto = workLoadOlService.convertBoToDto(workLoad);
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadOlDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}	
	
}
