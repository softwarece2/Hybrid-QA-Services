package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.OLLineItemSerialNoDto;
import in.co.bel.olms.transaction.dto.SampleSelectionDto;
import in.co.bel.olms.transaction.service.OLLineItemSerialNoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/registration-numbers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OLLineItemSerialNoController {

    private final OLLineItemSerialNoService serialNoService;
    
    private static final Logger logger = LoggerFactory.getLogger(OLLineItemSerialNoController.class);

    @GetMapping("/generate")
    public List<String> generateSerialNumbers(
            @RequestParam(required = true) String prefix,
            @RequestParam(required = true) int startNumber,
            @RequestParam(required = true) int endNumber,
            @RequestParam(defaultValue = "") String postfix) { 
        return serialNoService.generateSerialNumbers(prefix, startNumber, endNumber, postfix);
    }
    @PostMapping("/save") 
    public ResponseEntity<List<OLLineItemSerialNoDto>> saveSerialDetails(
        @RequestBody List<OLLineItemSerialNoDto> dtoList) { 

        List<OLLineItemSerialNoDto> savedDetails = serialNoService.saveOlLineItemSerialDetails(dtoList);
        return ResponseEntity.ok(savedDetails);
    }
    
    @GetMapping("/getByOlLineItemId/{olLineItemId}")
	public ResponseEntity<List<OLLineItemSerialNoDto>> getOlLineItemSerialDetailsByLineItemId(@PathVariable Integer olLineItemId) {
	    List<OLLineItemSerialNoDto> response = serialNoService.getOlLineItemSerialDetailsByLineItemId(olLineItemId);
	    return ResponseEntity.ok(response);
	}
    
    
    @GetMapping("/getByOlLineItemIdForFirm/{olLineItemId}")
	public ResponseEntity<List<OLLineItemSerialNoDto>> getByOlLineItemIdForFirm(@PathVariable Integer olLineItemId) {
	    List<OLLineItemSerialNoDto> response = serialNoService.getOlLineItemSerialDetailsByLineItemIdForFirm(olLineItemId);
	    return ResponseEntity.ok(response);
	}
  
    @GetMapping("/serialDetails/{olLineItemId}")
    public ResponseEntity<List<OLLineItemSerialNoDto>> getSerialDetails(
    		@PathVariable Integer olLineItemId,
    		@RequestParam(required = false) String type) {

    	List<OLLineItemSerialNoDto> response;

    	switch (type != null ? type.toLowerCase() : "") {
	    	case "sampled": response = serialNoService.getSampledOlLineItemSerialDetailsByLineItemId(olLineItemId);
	    	break;
	    	case "unsampled": response = serialNoService.getUnSampledOlLineItemSerialDetailsByLineItemId(olLineItemId);
	    	break;
	    	case "sampledfai": response = serialNoService.getFaiOlLineItemSerialDetailsByLineItemId(olLineItemId);
	    	break;
	    	case "unsampledfai": response = serialNoService.getUnSampledForFaiOlLineItemSerialDetailsByLineItemId(olLineItemId);
	    	break;
	    	default: return ResponseEntity.badRequest().build();
    	}
    	return ResponseEntity.ok(response);
    }

    @PostMapping("/updateSampleStatus")
	public ResponseEntity<String> updateSampleStatus(@RequestBody OLLineItemSerialNoDto requestDto) {
    	try {
    		String response = serialNoService.updateSampleStatus(requestDto);
    		return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	    	return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	    	return ResponseEntity.internalServerError().body(e.getMessage());
	    }
    }
    
	@GetMapping("/getProductAndRegNoByOlId/{olId}")
	public List<SampleSelectionDto> getProductAndRegNoByOlId(@PathVariable Integer olId) {
		return serialNoService.getProductAndRegNoByOlId(olId);
	}
}