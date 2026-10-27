package in.co.bel.olms.transaction.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.MergeOlDto;
import in.co.bel.olms.transaction.dto.MergedOlDetDto;
import in.co.bel.olms.transaction.dto.MergedSubSlipDto;
import in.co.bel.olms.transaction.dto.OLDetailsDto;
import in.co.bel.olms.transaction.service.MergeOlService;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author Shridhar R
 * Merge Ol service	
 */

@RestController
@RequestMapping("/merge")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class MergeOlController {
	
	private static final Logger logger = LoggerFactory.getLogger(MergeOlController.class);
	
	private final MergeOlService mergeOlService;

	private final JwtUtils jwtUtils;
	
//	 only DO user allowed to save 
//	@PreAuthorize("hasAnyAuthority('DO')")  
	@PostMapping("/mergeOl/save")
	private ResponseEntity<String> mergeINote(@RequestBody MergeOlDto requestDto, @RequestHeader(name = "Authorization") String token) throws Exception {
		
		if(token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		try {
//			Map<String, Object> map = jwtUtils.parseToken(token);
			Map<String, Object> map = jwtUtils.parseToken(token);
			int empId = jwtUtils.getEmpIdFromJwtToken(map);
			requestDto.setCreatedBy(empId);
			return mergeOlService.saveMergeOl(requestDto);
		}catch (IllegalArgumentException e) {
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
    public ResponseEntity<List<MergedSubSlipDto>> getAllMergedOls() {
        try {
        	List<MergedSubSlipDto> olDto = mergeOlService.getAllMergedOls();

            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	 logger.error("Error occurred while fetching all merged offer letters: ", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
 //not added preAuthorize for now for get
    @GetMapping("/getMergedOlDetById/{mergedOlId}")
    public ResponseEntity<List<MergedOlDetDto>> getMergedOlDetById(@PathVariable("mergedOlId") Integer mergedOlId) {
        try {
        	List<MergedOlDetDto> olDto = mergeOlService.getMergedOlDetById(mergedOlId);

            return ResponseEntity.ok(olDto);
        }catch(Exception e) {
        	logger.error("error retrieving merged offer letter details for ID {}: ", mergedOlId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/getAllOlDetails/{olIdString}")
    public ResponseEntity<Object> getAllOlDetails(@PathVariable("olIdString") String olIdString) {
        if(olIdString == null || olIdString.isEmpty()) {
        	return ResponseEntity.badRequest().body(Collections.emptyList());
        }
       
    	try {
    		List<Integer> olIds = Arrays.stream(olIdString.split(","))
    				                .map(Integer::parseInt)
    				                .toList();
    		
        	List<OLDetailsDto> olDto = mergeOlService.getAllOlDetails(olIds);

            return ResponseEntity.ok(olDto);
        } catch(IllegalArgumentException e) {
        	logger.error("Business logic error for olIdString {}: {}", olIdString, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
        	logger.error("Unexpected error retrieving OL details for string: {}", olIdString, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    	
    }

}
