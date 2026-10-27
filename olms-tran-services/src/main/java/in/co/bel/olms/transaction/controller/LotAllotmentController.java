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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.LotAllotmentDto;
import in.co.bel.olms.transaction.dto.LotAllotmentEmpDto;
import in.co.bel.olms.transaction.entity.LotAllotment;
import in.co.bel.olms.transaction.service.MergeSubSlipService;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lot")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class LotAllotmentController {

	private final MergeSubSlipService mergeSubSlipService;
	
	private final JwtUtils jwtUtils;
	
	private static final Logger logger =LoggerFactory.getLogger(LotAllotmentController.class);
	
	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
	
	@GetMapping("/getAllActiveDoBydepTypeIdLotId/{deptTypeId}/{mergeSubSlipId}")
	public ResponseEntity<LotAllotmentEmpDto> getAllActiveDoBydepTypeIdLotId(@PathVariable int deptTypeId, @PathVariable int mergeSubSlipId, @RequestHeader(name = "Authorization") String token) {
		Integer empId = appSecurity.getEmpIdFromJwtToken(token);
		return ResponseEntity.ok(mergeSubSlipService.getAllActiveDoBydepTypeIdLotId(deptTypeId, mergeSubSlipId, empId));
	}		
	
	@PostMapping("/approve")
	public ResponseEntity<LotAllotmentDto> allotLot(@RequestBody @Valid LotAllotmentDto lotAllotmentDto) {
		try {
			if (lotAllotmentDto.getAllotedToId() == null ) {
	            logger.error("allotedTo list cannot be null or empty.");
	            return new ResponseEntity<>(new LotAllotmentDto(), HttpStatus.BAD_REQUEST);
	        }	
			LotAllotment lotAllotment=mergeSubSlipService.allotLot(lotAllotmentDto);
				LotAllotmentDto dto = new LotAllotmentDto();
				lotAllotmentDto.setId(lotAllotment.getId());
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		} catch (DataNotValidException ex) {
			logger.error("Save LotAllotment error: {} ", ex.getMessage());
			return new ResponseEntity<>(new LotAllotmentDto(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Save LotAllotment error: {} ", e.getMessage());
			return new ResponseEntity<>(new LotAllotmentDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
