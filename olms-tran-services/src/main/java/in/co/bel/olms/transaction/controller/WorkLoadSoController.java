package in.co.bel.olms.transaction.controller;

import javax.validation.Valid;

import org.dozer.Mapper;
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
import in.co.bel.olms.transaction.dto.WorkLoadDto;
import in.co.bel.olms.transaction.entity.WorkLoad;
import in.co.bel.olms.transaction.service.WorkLoadSoService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 14-02-2023
 */

@RestController
@RequestMapping("/workloadso")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WorkLoadSoController {

	/**
	 * 
	 */
	private final WorkLoadSoService workLoadService;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(WorkLoadSoController.class);
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WorkLoadDto> getWorkLoadByID(@PathVariable int id) throws DataNotFoundException {
		WorkLoad workLoad = workLoadService.getWorkLoadById(id);
		WorkLoadDto workLoadDto = mapper.map(workLoad, WorkLoadDto.class);
		return ResponseEntity.ok(workLoadDto);

	}

	/**
	 * 
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<WorkLoadDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		WorkLoadDto workLoad = workLoadService.getWorkLoadBySoId(soid);
		return ResponseEntity.ok(workLoad);
	}

	/**
	 * Save the work load details for the supply order
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/plan/save")
	public ResponseEntity<WorkLoadDto> savePlan(@RequestBody @Valid WorkLoadDto workLoadDto) {
		try {
			WorkLoad workLoad = workLoadService.savePlan(workLoadDto);
			WorkLoadDto savedworkLoadDto = workLoadService.convertBoToDto(workLoad);		
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving plan: ", e);
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Todo: submit the work load details for the supply order
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/plan/submit")
	public ResponseEntity<WorkLoadDto> submitPlan(@RequestBody @Valid WorkLoadDto workLoadDto) {
		try {
			WorkLoad workLoad = workLoadService.submitPlan(workLoadDto);
			WorkLoadDto savedworkLoadDto = workLoadService.convertBoToDto(workLoad);		
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Save the work load details for the supply order
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/act/save")
	public ResponseEntity<WorkLoadDto> saveActual(@RequestBody @Valid WorkLoadDto workLoadDto) {
		try {
			WorkLoad workLoad = workLoadService.saveAct(workLoadDto);
			WorkLoadDto savedworkLoadDto = workLoadService.convertBoToDto(workLoad);		
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			 logger.error("Error occurred while saving actual: ", e);
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Todo: submit the work load details for the supply order
	 * 
	 * @param workLoadDto
	 * @return
	 */
	@PostMapping("/act/submit")
	public ResponseEntity<WorkLoadDto> submitActual(@RequestBody @Valid WorkLoadDto workLoadDto) {
		try {
			WorkLoad workLoad = workLoadService.submitAct(workLoadDto);
			WorkLoadDto savedworkLoadDto = workLoadService.convertBoToDto(workLoad);		
			return new ResponseEntity<>(savedworkLoadDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new WorkLoadDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
