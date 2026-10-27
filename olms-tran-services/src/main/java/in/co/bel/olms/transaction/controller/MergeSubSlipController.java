
package in.co.bel.olms.transaction.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import in.co.bel.olms.master.dto.EqptGroupDto;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.LotAllotmentDto;
import in.co.bel.olms.transaction.dto.MergeSubSlipDto;
import in.co.bel.olms.transaction.dto.MergedOlDetDto;
import in.co.bel.olms.transaction.dto.MergedSubSlipDto;
import in.co.bel.olms.transaction.dto.OlDetailDto;
import in.co.bel.olms.transaction.dto.WarrantAllotmentDto;
import in.co.bel.olms.transaction.entity.LotAllotment;
import in.co.bel.olms.transaction.entity.WarrantAllotment;
import in.co.bel.olms.transaction.service.MergeSubSlipService;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author Shridhar R
 * Merge Submission Slip service	
 */

@RestController
@RequestMapping("/merge/sslip")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class MergeSubSlipController {

	private final MergeSubSlipService mergeSubSlipService;
	
	private final JwtUtils jwtUtils;

	private final HmisAppSecurity appSecurity = new HmisAppSecurity();
  
	private static final Logger logger =LoggerFactory.getLogger(WarrantAllotmentController.class);
	
//	 only DO user allowed to save 
//	@PreAuthorize("hasAnyAuthority('DO')")  
	@PostMapping("/save")
	private ResponseEntity<String> mergeINote(@RequestBody MergeSubSlipDto requestDto, @RequestHeader(name = "Authorization") String token) throws Exception {
		try {
      int empId = appSecurity.getEmpIdFromJwtToken(token);
			requestDto.setCreatedBy(empId);
      String res = mergeSubSlipService.saveMergeOl(requestDto);
      return ResponseEntity.ok(res);
    } catch (IllegalArgumentException e) {
    	logger.error("Invalid argument: {}", e.getMessage(), e);
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
    	logger.error("Failed to save: {}", e.getMessage(), e);
      return ResponseEntity.internalServerError().body("Failed to save ");
    }
	}
	
//	for now, get access given to all
//	@PreAuthorize("hasAnyAuthority('DO')") 
    @GetMapping("/getAllMergedOls")
    public ResponseEntity<List<MergedSubSlipDto>> getAllMergedOls(@RequestHeader(name = "Authorization") String token, @RequestParam(required = false) List<Integer> statusIds) throws Exception {
        try {
        	
        	Integer empId = appSecurity.getEmpIdFromJwtToken(token);
        	Integer roleId = appSecurity.getRoleIdFromJwtToken(token);
          List<MergedSubSlipDto> olDto = mergeSubSlipService.getAllMergedOls(empId, roleId, statusIds);
            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("error in getAllMergedOls for user context: ", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
 //not added preAuthorize for now for get
    @GetMapping("/getDetById/{mergedOlId}")
    public ResponseEntity<List<MergedOlDetDto>> getMergedOlDetById(@PathVariable("mergedOlId") Integer mergedOlId) {
        try {
        	List<MergedOlDetDto> olDto = mergeSubSlipService.getMergedOlDetById(mergedOlId);

            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	 logger.error("Error retrieving merged offer letter details for mergedOlId {}: ", mergedOlId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/getAllOlDetails/{olIdString}")
    public ResponseEntity<List<OlDetailDto>> getAllOlDetails(@PathVariable("olIdString") String olIdString) {
        if(olIdString == null || olIdString.isEmpty()) {
        	return ResponseEntity.badRequest().body(Collections.emptyList());
        }
       
    	try {
    		List<Integer> olIds = Arrays.stream(olIdString.split(","))
    				                .map(Integer::parseInt)
    				                .toList();
    		
        	List<OlDetailDto> olDto = mergeSubSlipService.getAllOlDetails(olIds);

            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("Critical error retrieving OL details for IDs: {}", olIdString, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/{id}/getRegs")
    public ResponseEntity<Object> getRegsByMergeSslipId(@PathVariable(name = "id") Integer mergeSslipId ){
    	try {
    		List<Map<String, Object>> regsByMergeSslipId = mergeSubSlipService.getRegsByMergeSslipId(mergeSslipId);
			return ResponseEntity.ok(regsByMergeSslipId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
    }
    
}
