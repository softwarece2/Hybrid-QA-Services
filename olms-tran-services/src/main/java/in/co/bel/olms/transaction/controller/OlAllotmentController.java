package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.OlAllotmentDto;
import in.co.bel.olms.transaction.dto.OlAllotmentInspDto;
import in.co.bel.olms.transaction.entity.OlAllotment;
import in.co.bel.olms.transaction.entity.OlCrossCheckAllotment;
import in.co.bel.olms.transaction.entity.OlSurpriseCheckAllotment;
import in.co.bel.olms.transaction.service.ol.OlAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/ol")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlAllotmentController {

	private final OlAllotmentService olAllotmentService;
	
	private static final Logger logger =LoggerFactory.getLogger(OlAllotmentController.class);
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	/**
	 * Jan 2026
	 * @param fclDto
	 * @return
	 */
	//@PreAuthorize("hasAnyAuthority('JCQAO','DCQAO','ACQAO')")
	@PostMapping("/allotSubordinate")
	public ResponseEntity<List<OlAllotmentDto>> allotSubordinate(@RequestBody @Valid OlAllotmentInspDto olAllotmentDto) {

		try {
			if (olAllotmentDto.getAllotedTo() == null || olAllotmentDto.getAllotedTo().isEmpty()) {
	            logger.error("AllotedTo list cannot be null or empty in Submission slip allotment.");
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	        }
			
			List<OlAllotment> olAllotment=olAllotmentService.allotSubordinate(olAllotmentDto);
			List<OlAllotmentDto> allotmentDtos = new ArrayList<>();
			for(OlAllotment allotment : olAllotment) {
				OlAllotmentDto dto = new OlAllotmentDto();
				olAllotmentDto.setId(allotment.getId());
				allotmentDtos.add(dto);
			}
			return new ResponseEntity<>(allotmentDtos, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save OlAllotment error - DataNotValidException: {} ", ex.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save OlAllotment error - Exception: {} ", e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/getByEmpId/{orgId}/{empId}")
	public ResponseEntity<List<OlAllotmentDto>> getByEmpId(@PathVariable int orgId,@PathVariable int empId){
		try {
			return ResponseEntity.ok(olAllotmentService.getOlAllotmentForEmpId(orgId,empId));
		}catch(DataNotValidException e) {
			 logger.error("Invalid data provided for OrgId: {} and EmpId: {}. Error: {}", orgId, empId, e);
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
		       logger.error("Unexpected error retrieving allotments for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	
	@GetMapping("/getToEmpId/{empId}")
	public ResponseEntity<List<OlAllotmentDto>> getToEmpId(@PathVariable int empId){
		try {
			return ResponseEntity.ok(olAllotmentService.getOlAllotmentToEmpId(empId));
		}catch(DataNotValidException e) {
			logger.error("Invalid data provided for EmpId: {}. Error: {}", empId, e);
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("Unexpected error retrieving allotments for EmpId: {}", empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getEmployeesByOlId/{olId}")
	public ResponseEntity<Object> getEmployeesByOlId(@PathVariable int olId){
		try {
			List<Map<Integer, String>> res = olAllotmentService.getEmployeesByOlId(olId);
			return ResponseEntity.ok(res);
		} catch(Exception e) {
			logger.error("/allot/ol getEmployeesByOlId Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
		}
	}
	
	@PostMapping("/allotForCrossCheck")
	public ResponseEntity<OlAllotmentDto> allotForCrossCheck(@RequestBody @Valid OlAllotmentDto olAllotmentDto) {

		try {
			OlCrossCheckAllotment olCrossCheckAllotment = olAllotmentService.allotForCrossCheck(olAllotmentDto);
			olAllotmentDto =  new OlAllotmentDto();
			olAllotmentDto.setId(olCrossCheckAllotment.getId());
			
			return new ResponseEntity<>(olAllotmentDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save OlAllotment Cross Check error - DataNotValidException: {} ", ex.getMessage());
			return new ResponseEntity<>(new OlAllotmentDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save OlAllotment Cross Check  error - Exception: {} ", e.getMessage());
			return new ResponseEntity<>(new OlAllotmentDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/allotForSurpriseCheck")
	public ResponseEntity<OlAllotmentDto> allotForSurpriseCheck(@RequestBody @Valid OlAllotmentDto olAllotmentDto) {

		try {
			OlSurpriseCheckAllotment olSurpriseCheckAllotment = olAllotmentService.allotForSurpriseCheck(olAllotmentDto);
			olAllotmentDto =  new OlAllotmentDto();
			olAllotmentDto.setId(olSurpriseCheckAllotment.getId());
			
			return new ResponseEntity<>(olAllotmentDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save OlAllotment surprise check error - DataNotValidException: {} ", ex.getMessage());
			return new ResponseEntity<>(new OlAllotmentDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save OlAllotment surprise check  error - Exception: {} ", e.getMessage());
			return new ResponseEntity<>(new OlAllotmentDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{olId}")
	public ResponseEntity<String> getOlAllotmentsWithNextEmps(@PathVariable Integer olId,
			@RequestParam(name = "deptTypeId", required = false) Integer deptTypeId,
			@RequestHeader(name = "Authorization") String token){
		try {
			
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);
			
			return ResponseEntity.ok(olAllotmentService.getOlAllotmentsWithNextEmps(olId, deptTypeId, empId, roleId));
		}catch(DataNotValidException e) {
			logger.error("Invalid request parameters for olId: {}. Error: {}", olId, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("error retrieving olallotments for olId: {}", olId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

}
