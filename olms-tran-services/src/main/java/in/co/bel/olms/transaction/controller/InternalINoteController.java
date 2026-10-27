package in.co.bel.olms.transaction.controller;

import java.util.List;
import java.util.Map;

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

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.InoteDetailsDto;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.inote.InternalInoteService;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internalInote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InternalINoteController {

    private final OfferLetterService offerLetterService;
    
    private final InternalInoteService internalInoteService;
    
    private final JwtUtils jwtUtils;
    
    private static final Logger logger = LoggerFactory.getLogger(InternalINoteController.class);
    
    /**
     * @author Shubham Anand
     * @param olId
     * @return
     *
     * Get I-Note details by OL id
     */
    @GetMapping("/getINoteByOlId/{olId}")
    public ResponseEntity<INoteDto> getINoteOfOfferLetter(@PathVariable int olId) {
        try {
            INoteDto iNoteDto = internalInoteService.getInternalINoteByOlId(olId);
            return ResponseEntity.ok(iNoteDto);
        }catch(Exception e) {
        	 logger.error("An unexpected error occurred while fetching iNote for olId: {}", olId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    /**
     * @param iNoteDto
     * @return
     *
     * Get I-Note details by Sub slip id
     */
    @GetMapping("/getINoteBySslipId/{mergeSubSlipId}")
    public ResponseEntity<Object> getINoteOfSubSlip(@PathVariable Integer mergeSubSlipId) {
        try {
            INoteDto iNoteDto = internalInoteService.getInternalINoteBySubSlipId(mergeSubSlipId);
            return ResponseEntity.ok(iNoteDto);
        }catch(IllegalArgumentException e) {
        	 logger.warn("Invalid argument provided for SubSlipId: {}. Error: {}", mergeSubSlipId, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e) {
        	logger.error("Unexpected error retrieving iNote for SubSlipId: {}", mergeSubSlipId, e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

//	@PreAuthorize("hasAnyAuthority('CQAO')")
    @PostMapping("/saveINote")
    public ResponseEntity<String> saveINote(@RequestBody INoteDto iNoteDto , @RequestHeader(name = "Authorization") String token) {
    	
// author: Shridhar R  
// taking empId from jwt token for digital signature in QCC
    	if(token != null && token.startsWith("Bearer")) {
    		token = token.substring(7);
    	}
    	Map<String, Object>map = jwtUtils.parseToken(token);
    	Integer empId = jwtUtils.getEmpIdFromJwtToken(map);
        try {
            String response = internalInoteService.saveInternalINote(iNoteDto , empId);
            if(response.equals(ApplicationConstants.SUCCESS)) {
                return ResponseEntity.ok().body(response);
            }
            else {
                return ResponseEntity.internalServerError().body(response);
            }
        } catch(DataNotValidException de) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + de.getMessage());
        } catch(Exception e) {
        	logger.error("Critical error during saveINote for EmpId: {}", empId, e);
            return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
        }
    }
    
    @GetMapping("/getInternalInotesByProdId/{partNumberId}")
    public ResponseEntity<List<InoteDetailsDto>> getInternalInotesByProdId(@PathVariable int partNumberId){
    	try {
        	List<InoteDetailsDto> iNoteDto = offerLetterService.getInternalInotesByProdId(partNumberId);
            return ResponseEntity.ok(iNoteDto);
        }catch(Exception e) {
        	logger.error("Exception occurred while fetching internal iNotes for PartNumberId: {}", partNumberId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/getAllInotesList")
    public ResponseEntity<List<InoteDetailsDto>> getAllInotesList() {
        try {
        	List<InoteDetailsDto> iNoteDto = offerLetterService.getAllInotesList();
            return ResponseEntity.ok(iNoteDto);
        }catch(Exception e) {
        	logger.error("Unexpected error occurred while fetching all iNotes list", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<List<InoteDetailsDto>> getAllInternalInoteList() {
        try {
        	List<InoteDetailsDto> iNoteDto = offerLetterService.getAllInternalInoteList();
            return ResponseEntity.ok(iNoteDto);
        }catch(Exception e) {
        	logger.error("Critical error while fetching all internal iNotes list", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
