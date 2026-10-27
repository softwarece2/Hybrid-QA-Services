package in.co.bel.olms.transaction.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.SoAllotmentDto;
import in.co.bel.olms.transaction.entity.SoAllotment;
import in.co.bel.olms.transaction.service.so.SoAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/so")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoAllotmentController {

	private final SoAllotmentService soAllotmentService;
	
	private static final Logger logger =LoggerFactory.getLogger(SoAllotmentController.class);
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	/**
	 * 
	 * @param fclDto
	 * @return
	 */
	//@PreAuthorize("hasAnyAuthority('JCQAO','DCQAO','ACQAO')")
	@PostMapping("/allotSubordinate")
	public ResponseEntity<SoAllotmentDto> allotSubordinate(@RequestBody @Valid SoAllotmentDto soAllotmentDto) {

		try {
			SoAllotment soAllotment=soAllotmentService.allotSubordinate(soAllotmentDto);
			soAllotmentDto=soAllotmentService.convertBotoDto(soAllotment);
			return new ResponseEntity<>(soAllotmentDto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save SoAllotment error: {} ", ex.getMessage());
			return new ResponseEntity<>(new SoAllotmentDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save SoAllotment error: {} ", e.getMessage());
			return new ResponseEntity<>(new SoAllotmentDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/getByEmpId/{empId}")
	public ResponseEntity<List<SoAllotmentDto>> getByEmpId(@PathVariable int empId){
		try {
			return ResponseEntity.ok(soAllotmentService.getSoAllotmentForEmpId(empId));
		}catch(DataNotValidException e) {
			 logger.warn("Invalid data requested for EmpId: {}. Error: {}", empId, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("An unexpected error occurred while fetching allotment for EmpId: {}", empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/{soId}")
  public ResponseEntity<Object> getWarrantAllotmentsWithNextEmps(@PathVariable Integer soId,
			@RequestHeader(name = "Authorization") String token) {
		try {

			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);

      return ResponseEntity.ok(soAllotmentService.getSoAllotmentsWithNextEmps(soId, empId, roleId));
		} catch (DataNotValidException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
}
