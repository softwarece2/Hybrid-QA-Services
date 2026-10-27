package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.PoObservationDto;
import in.co.bel.olms.transaction.entity.PoObservation;
import in.co.bel.olms.transaction.service.so.PoObservationService;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */

@RestController
@RequestMapping("/poObservation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PoObservationController {

	private final PoObservationService poObservationService;

	private static final Logger logger =LoggerFactory.getLogger(PoObservationController.class);
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PoObservationDto> getPoObservationById(@PathVariable int id) throws DataNotFoundException {
		PoObservation poObservation = poObservationService.getPoObservationById(id);
		PoObservationDto poObservationDto = poObservationService.convertBoToDto(poObservation);
		return ResponseEntity.ok(poObservationDto);
	}

	/**
	 * @since 13-02-2023
	 * @param poObservationDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/createPoObservation")
	public ResponseEntity<PoObservationDto> createPoObservation(@RequestBody @Valid PoObservationDto poObservationDto) {
		try {
			PoObservation poObservation=poObservationService.createPoObservation(poObservationDto);
			poObservationDto = poObservationService.convertBoToDto(poObservation);
			return new ResponseEntity<>(poObservationDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("create po observation error: {} ", ex.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("create po observation error: {} ", e.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @since 09-03-2023
	 * @param poObservationDto
	 * @return SBU_ADMIN
	 */
  @PreAuthorize("hasAnyAuthority('SBU_ADMIN', 'SALES_ADMIN', 'DO')")
	@PostMapping("/saveRemActPoObservation")
	public ResponseEntity<PoObservationDto> saveRemActPoObservation(@RequestBody @Valid PoObservationDto poObservationDto) {
		try {
			PoObservation poObservation=poObservationService.saveRemActPoObservation(poObservationDto);
			poObservationDto = poObservationService.convertBoToDto(poObservation);
			return new ResponseEntity<>(poObservationDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save po observation rem ac error: {} ", ex.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save po observation rem ac error: {} ", e.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	/**
	 * @since 09-03-2023
	 * @param poObservationDto
	 * @return
	 */
  @PreAuthorize("hasAnyAuthority('SBU_ADMIN', 'SALES_ADMIN', 'DO')")
	@PostMapping("/submitRemActPoObservation")
	public ResponseEntity<PoObservationDto> submitRemActPoObservation(@RequestBody @Valid PoObservationDto poObservationDto) {
		try {
			PoObservation poObservation=poObservationService.submitRemActPoObservation(poObservationDto);
			poObservationDto = poObservationService.convertBoToDto(poObservation);
			return new ResponseEntity<>(poObservationDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Submit po observation rem ac error: {} ", ex.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Submit po observation rem ac error: {} ", e.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	/**
	 * @since 09-03-2023
	 * @param poObservationDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/saveRemarkPoObservation")
	public ResponseEntity<PoObservationDto> saveRemarkPoObservation(@RequestBody @Valid PoObservationDto poObservationDto) {
		try {
			PoObservation poObservation = poObservationService.saveRemarkPoObservation(poObservationDto);
			poObservationDto = poObservationService.convertBoToDto(poObservation);
			return new ResponseEntity<>(poObservationDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save po observation remarks error: {} ", ex.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save po observation remarks error: {} ", e.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	/**
	 * @since 09-03-2023
	 * @param poObservationDto
	 * @return
	 */
	@PreAuthorize("hasAuthority('INSP')")
	@PostMapping("/submitRemarkPoObservation")
	public ResponseEntity<PoObservationDto> submitRemarkPoObservation(@RequestBody @Valid PoObservationDto poObservationDto) {
		try {
			PoObservation poObservation = poObservationService.submitRemarkPoObservation(poObservationDto);
			poObservationDto = poObservationService.convertBoToDto(poObservation);
			return new ResponseEntity<>(poObservationDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Submit po observation remarks error: {} ", ex.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Submit po observation remarks error: {} ", e.getMessage());
			return new ResponseEntity<>(new PoObservationDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	/**
	 * 
	 * @param soId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soId}")
	public ResponseEntity<PoObservationDto> getBySupplyOrder(@PathVariable int soId) throws DataNotFoundException {
		PoObservationDto poObservationDto = poObservationService.getPoObservationBySoId(soId);
		return ResponseEntity.ok(poObservationDto);
	}

	/**
	 * Get the PO observation list by the OPA id 
	 * @author Manasa B
	 * @since 11-02-2023
	 * @param orgId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/opa/{orgId}")
	public ResponseEntity<List<PoObservationDto>> getByOpa(@PathVariable int orgId) {
		try {
			List<PoObservationDto> poObservationDto = poObservationService.getPoObservationByOpaId(orgId);
			return ResponseEntity.ok(poObservationDto);
		} catch (DataNotValidException ex) {
			return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	/**
	 * Get the PO observation list by the sales grp id 
	 * @author Manasa B
	 * @since 08-04-2023
	 * @param orgId
	 * @return
	 * @throws DataNotFoundException
	 */

  @PreAuthorize("hasAnyAuthority('SBU_ADMIN','SALES_ADMIN','DO')")
	@GetMapping("/salesGrp/{salesGrpId}")
	public ResponseEntity<List<PoObservationDto>> getBySalesGrp(@PathVariable int salesGrpId) {
		try {
			List<PoObservationDto> poObservationDto = poObservationService.getPoObservationBySalesGrpId(salesGrpId);
			return ResponseEntity.ok(poObservationDto);
		} catch (DataNotValidException ex) {
			return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Get the PO observation list by the org Id
	 * @author Manasa B
	 * @since 13-02-2023
	 * @param orgId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/cqae/{orgId}")
	public ResponseEntity<List<PoObservationDto>> getByCqae(@PathVariable int orgId)
			throws DataNotFoundException {
		List<PoObservationDto> poObservationDto = poObservationService.getByCqae(orgId);
		return ResponseEntity.ok(poObservationDto);
	}
	

	/**
	 * 
	 * @param roleId
	 * @param empId
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('INSP','JCQAO','DCQAO','ACQAO')")
	@GetMapping("/getByEmpnSub/{empId}")
	public ResponseEntity<List<PoObservationDto>> getByEmpnSub( @PathVariable int empId) {
		List<PoObservationDto> olDtos = poObservationService.getByEmpnSub(empId);
		return ResponseEntity.ok(olDtos);
	}
	
	
	@GetMapping("/getByIds")
	public ResponseEntity<List<PoObservationDto>> getPoObservationById(
			@RequestParam(required = true) List<Integer> ids) throws DataNotFoundException {

		List<PoObservationDto> returnList = new ArrayList<>();
		for (Integer obsId : ids) {
			PoObservation poObservation = poObservationService.getPoObservationById(obsId);
			PoObservationDto poObservationDto = poObservationService.convertBoToDto(poObservation);
			returnList.add(poObservationDto);
		}
		return ResponseEntity.ok(returnList);
	}
	
}
