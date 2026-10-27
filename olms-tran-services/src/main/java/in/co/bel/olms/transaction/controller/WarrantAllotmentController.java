package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.WarrantAllotmentDto;
import in.co.bel.olms.transaction.entity.WarrantAllotment;
import in.co.bel.olms.transaction.service.WarrantAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/warrant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class WarrantAllotmentController {

	private final WarrantAllotmentService warrantAllotmentService;
	
	private static final Logger logger =LoggerFactory.getLogger(WarrantAllotmentController.class);
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	@PostMapping("/allotSubordinate")
	public ResponseEntity<List<WarrantAllotmentDto>> allotSubordinate(@RequestBody @Valid WarrantAllotmentDto warrantAllotmentDto) {

		try {
			if (warrantAllotmentDto.getAllotedToIds() == null || warrantAllotmentDto.getAllotedToIds().isEmpty()) {
	            logger.error("allotedTo list cannot be null or empty.");
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	        }
			
			List<WarrantAllotment> olAllotment=warrantAllotmentService.allotSubordinate(warrantAllotmentDto);
			List<WarrantAllotmentDto> allotmentDtos = new ArrayList<>();
			for(WarrantAllotment allotment : olAllotment) {
				WarrantAllotmentDto dto = new WarrantAllotmentDto();
				warrantAllotmentDto.setId(allotment.getId());
				allotmentDtos.add(dto);
			}
			return new ResponseEntity<>(allotmentDtos, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save OlAllotment error: {} ", ex.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save OlAllotment error: {} ", e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/getByEmpId/{orgId}/{empId}")
	public ResponseEntity<List<WarrantAllotmentDto>> getByEmpId(@PathVariable int orgId,@PathVariable int empId){
		try {
			return ResponseEntity.ok(warrantAllotmentService.getWarrantAllotmentForEmpId(orgId,empId));
		}catch(DataNotValidException e) {
			logger.warn("Invalid data request for OrgId: {} and EmpId: {}. Reason: {}", orgId, empId, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("Error occurred while fetching warrant allotment for EmpId: {}", empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/{warrantId}")
	public ResponseEntity<String> getWarrantAllotmentsWithNextEmps(@PathVariable Integer warrantId,
			@RequestParam(name = "deptTypeId", required = false) Integer deptTypeId,
			@RequestHeader(name = "Authorization") String token){
		try {
			
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);
			
			return ResponseEntity.ok(warrantAllotmentService.getWarrantAllotmentsWithNextEmps(warrantId, deptTypeId, empId, roleId));
		}catch(DataNotValidException e) {
			 logger.warn("Validation failed for warrantId {}: {}", warrantId, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("Error retrieving next employees for warrantId {}: ", warrantId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
}
