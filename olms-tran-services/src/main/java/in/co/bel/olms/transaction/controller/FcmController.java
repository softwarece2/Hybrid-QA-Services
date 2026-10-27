package in.co.bel.olms.transaction.controller;

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
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.FcmDto;
import in.co.bel.olms.transaction.entity.Fcm;
import in.co.bel.olms.transaction.service.so.FcmService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */

@RestController
@RequestMapping("/fcm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class FcmController {

	private final FcmService fcmService;

	private final Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(FcmController.class);
	
	/**
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<FcmDto> getFcmById(@PathVariable int id) throws DataNotFoundException {
		Fcm fcm = fcmService.getFcmById(id);
		FcmDto fcmdto = mapper.map(fcm, FcmDto.class);
		return ResponseEntity.ok(fcmdto);
	}

	/**
	 * To save as Draft FCM by the inspector
	 * @author Manasa B
	 * @since 13-03-2023
	 * @param fcmDto
	 * @return
	 */
	
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/saveFcm")
	public ResponseEntity<FcmDto> saveFcm(@RequestBody @Valid FcmDto fcmDto) {
		try {
			Fcm fcm = fcmService.saveFcm(fcmDto);	
			FcmDto fcmdto =  fcmService.convertBoToDto(fcm);
			return new ResponseEntity<>(fcmdto, HttpStatus.CREATED);
		}catch (DataNotValidException ex) {
			logger.error("Save FCM error:  {}", ex.getMessage());
			return new ResponseEntity<>(new FcmDto(),HttpStatus.BAD_REQUEST);
		}		
		catch (Exception e) {
			logger.error("Unexpected error during Save FCM: {}", e.getMessage());
			logger.error("Save FCM error:  {}",e.getMessage());
			return new ResponseEntity<>(new FcmDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}			
	}

	
	/**
	 * To submit FCM by the inspector, then only its visible to the other stakeholder
	 * @author Manasa B
	 * @since 13-03-2023
	 * @param fcmDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/submitFcm")
	public ResponseEntity<FcmDto> submitFcm(@RequestBody @Valid FcmDto fcmDto) {
		try {
			Fcm fcm = fcmService.submitFcm(fcmDto);		
			FcmDto fcmdto =  fcmService.convertBoToDto(fcm);
			return new ResponseEntity<>(fcmdto, HttpStatus.CREATED);
		} catch (DataNotValidException e) {
			logger.error("Submit FCM error:  {}", e.getMessage());
			return new ResponseEntity<>(new FcmDto(),HttpStatus.BAD_REQUEST);
		}	
		catch (Exception e) {
			logger.error("Submit FCM error:  {}", e.getMessage());
			return new ResponseEntity<>(new FcmDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

	/**
	 * @author Manasa B
	 * @since 10-02-2023
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<FcmDto> getFcmBySoId(@PathVariable int soid) {
		try {
			FcmDto fcm = fcmService.getFcmBySoId(soid);	
			return ResponseEntity.ok(fcm);
		}		
		catch (DataNotValidException e) {
			logger.error("Get FCM by Supply order error:  {}", e.getMessage());
			return new ResponseEntity<>(new FcmDto(),HttpStatus.BAD_REQUEST);
		}			
		catch (Exception e) {			
			logger.error("Get FCM by Supply order error:  {}", e.getMessage());			
			return new ResponseEntity<>(new FcmDto(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
