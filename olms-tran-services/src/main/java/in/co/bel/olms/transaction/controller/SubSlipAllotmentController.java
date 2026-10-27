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
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.SubSlipAllotmentDto;
import in.co.bel.olms.transaction.entity.SubSlipAllotment;
import in.co.bel.olms.transaction.service.SubSlipAllotmentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/allot/subSlip")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SubSlipAllotmentController {

	private final SubSlipAllotmentService subSlipAllotmentService;
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	private static final Logger logger =LoggerFactory.getLogger(SubSlipAllotmentController.class);
	
	@PostMapping("/allotSubordinate")
	public ResponseEntity<List<SubSlipAllotmentDto>> allotSubordinate(@RequestBody @Valid SubSlipAllotmentDto olAllotmentDto) {

		try {
			if (olAllotmentDto.getAllotedTo() == null || olAllotmentDto.getAllotedTo().isEmpty()) {
	            logger.error("AllotedTo list cannot be null or empty in Submission slip allotment.");
	            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	        }
			
			List<SubSlipAllotment> olAllotment=subSlipAllotmentService.allotSubordinate(olAllotmentDto);
			List<SubSlipAllotmentDto> allotmentDtos = new ArrayList<>();
			for(SubSlipAllotment allotment : olAllotment) {
				SubSlipAllotmentDto dto = new SubSlipAllotmentDto();
				olAllotmentDto.setId(allotment.getId());
				allotmentDtos.add(dto);
			}
			return new ResponseEntity<>(allotmentDtos, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save SubSlipAllotment Data not valid error - DataNotValidException: {} ", ex.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save SubSlipAllotment error - Exception: {} ", e.getMessage());
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{offerLetterId}")
	public ResponseEntity<String> getWarrantAllotmentsWithNextEmps(@PathVariable Integer offerLetterId,
			@RequestHeader(name = "Authorization") String token){
		try {
			
			Integer empId = appSecurity.getEmpIdFromJwtToken(token);
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);
			
			return ResponseEntity.ok(subSlipAllotmentService.getSubSlipAllotmentsWithNextEmps(offerLetterId, empId, roleId));
		}catch(DataNotValidException e) {
			 logger.warn("Validation failed for offerLetterId {}: {}", offerLetterId, e.getMessage());
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("Unexpected error retrieving warrant allotments for offerLetterId {}", offerLetterId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/{offerLetterId}/{seniorityId}/{deptId}")
	public ResponseEntity<String> getWarrantAllotmentsWithNextEmps1(@PathVariable Integer offerLetterId,@PathVariable Integer seniorityId,@PathVariable Integer deptId){
		try {
			return ResponseEntity.ok(subSlipAllotmentService.getSubSlipAllotmentsWithNextEmps1(offerLetterId,seniorityId,deptId));
		}catch(DataNotValidException e) {
			logger.error("Validation failed for offerLetterId {}: {}", offerLetterId, e);
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			logger.error("Error processing warrant allotments for offerLetterId: {}", offerLetterId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
}
