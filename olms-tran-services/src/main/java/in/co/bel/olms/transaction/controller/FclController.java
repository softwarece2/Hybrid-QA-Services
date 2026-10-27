package in.co.bel.olms.transaction.controller;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.FclDto;
import in.co.bel.olms.transaction.entity.Fcl;
import in.co.bel.olms.transaction.service.so.FclService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 * @since 14-02-2023
 *
 */

@RestController
@RequestMapping("/fcl")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FclController {

	private final FclService fclService;

	private static final Logger logger = LoggerFactory.getLogger(FclController.class);
	
	/**
	 * 
	 * @param fclDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/saveFcl")
	public ResponseEntity<FclDto> saveFcl(@RequestBody @Valid FclDto fclDto) {

		try {
			Fcl fcl=fclService.saveFcl(fclDto);
			fclDto=fclService.convertBotoDto(fcl);
			return new ResponseEntity<>(fclDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save fcl error: {} ", ex.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save fcl error: {} ", e.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @param fclDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/submitFcl")
	public ResponseEntity<FclDto> submitFcl(@RequestBody @Valid FclDto fclDto) {
		try {
			Fcl fcl=fclService.submitFcl(fclDto);
			fclDto=fclService.convertBotoDto(fcl);
			return new ResponseEntity<>(fclDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Submit fcl error: {} ", ex.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			logger.error("Submit fcl error: {} ", e.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 
	 * @author Manasa B
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<FclDto> getFclBySoId(@PathVariable int soid) throws DataNotFoundException {
		try {
			FclDto fclDto = fclService.getFclBySoId(soid);
			return ResponseEntity.ok(fclDto);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(new FclDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
