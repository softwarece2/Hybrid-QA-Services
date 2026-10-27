package in.co.bel.olms.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.InoteDetailsDto;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.insp.InspectionFileNoteDto;
import in.co.bel.olms.transaction.service.INoteService;
import in.co.bel.olms.transaction.service.OfferLetterService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class INoteController {
	
	private final OfferLetterService offerLetterService;

	private final INoteService iNoteService;
	
	private static final Logger logger = LoggerFactory.getLogger(INoteController.class);
	
	/**
	 * @author Shubham Anand
	 * @param iNoteDto
	 * @return
	 * 
	 * Get I-Note details by OL id
	 */
	
//	@PreAuthorize("hasAnyAuthority('CQAO')")
	@PostMapping("/saveINote")
	public ResponseEntity<String> saveINote(@RequestBody INoteDto iNoteDto) {	
		try {
			String response = offerLetterService.saveINote(iNoteDto );
			if(response.equals(ApplicationConstants.SUCCESS)) {
				return ResponseEntity.ok().body(response);
			}
			else {
				return ResponseEntity.internalServerError().body(response);			
			}
		} catch(DataNotValidException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error occurred: " + de.getMessage());
		} catch(Exception e) {
			logger.error("Save Inote error:  {}", e.getMessage());
			return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
		}
	}
	
	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 * change required to offer letter
	 * Get I-Note details by OL id
	 */
	@GetMapping("/getINoteByOlId/{olId}")
	public ResponseEntity<INoteDto> getINoteOfOfferLetter(@PathVariable int olId) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByOlId(olId);
			
			return ResponseEntity.ok(iNoteDto);
		}catch(Exception e) {
			logger.error("Error retrieving INote for Offer Letter ID: {}", olId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	//Modified 13-08-26 for inote number generation
	@GetMapping("/getINoteByOlIdV2/{olId}")
	public ResponseEntity<INoteDto> getINoteOfOfferLetterV2(@PathVariable Integer olId) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByOlIdV2(olId);			
			return ResponseEntity.ok(iNoteDto);
		}catch(Exception e) {
			logger.error("Error retrieving INote for Offer Letter ID: {}", olId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return
	 * 
	 * Get I-Note details by OL id
	 */
	@GetMapping("/getForm4DetailsOfOfferLetter/{olId}")
	public ResponseEntity<INoteDto> getForm4DetailsOfOfferLetter(@PathVariable int olId) {
		try {
			INoteDto iNoteDto = offerLetterService.getNewINote(olId);
			
			return ResponseEntity.ok(iNoteDto);
		}catch(Exception e) {
			logger.error("Error retrieving Form4 for Offer Letter ID: {}", olId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	/**
	 * @param fileNoteId
	 * @return INoteDto
	 * Get I-Note details by file Note Id
	 * @author Awadhesh Verma
	 */
	@GetMapping("/getINoteByFileNoteId/{fileNoteId}")
	public ResponseEntity<INoteDto> getINoteByFileNoteId(@PathVariable int fileNoteId) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByFileNoteId(fileNoteId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving INote for FileNoteId: {}", fileNoteId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllINotes/{orgId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllINoteByFileNoteId(@PathVariable int orgId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllINoteByOrgId(orgId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving all INotes for OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllINotesV2/{orgId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllINoteByFileNoteIdV2(@PathVariable int orgId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllINoteByOrgIdV2(orgId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving all INotes (V2) for OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getAllExternalINotesForFirm/{orgId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllExternalINotesForFirm(@PathVariable int orgId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllINoteForFirm(orgId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving External INotes for Firm OrgId: {}", orgId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getAllottedExternalINotesForFirm/{orgId}/{empId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllottedExternalINotesForFirm(@PathVariable int orgId, @PathVariable int empId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllottedINoteForFirm(orgId, empId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving allotted External INotes for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getAllottedINotes/{orgId}/{empId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllottedINotes(@PathVariable int orgId, @PathVariable int empId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllottedINote(orgId, empId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			 logger.error("Error retrieving allotted iNotes for OrgId: {} and EmpId: {}", orgId, empId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getAllINotesBySalesGrpId/{salesGrpId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllINotesBySalesGrpId(@PathVariable int salesGrpId) {
		try {
			List<InspectionFileNoteDto> iNoteDto = offerLetterService.getAllINotesBySalesGrpId(salesGrpId);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			 logger.error("Exception occurred while fetching iNotes for Sales Group ID: {}", salesGrpId, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	/**
	 * @param olIds
	 * @return INoteDto
	 * Get I-Note details by file Note Id
	 * @author Awadhesh Verma
	 */
	@GetMapping("/getINoteByOLIds")
	public ResponseEntity<INoteDto> getINoteByOLIds(@RequestParam(value = "olIds", required = true) List<Integer> olIds) {
		try {
			INoteDto iNoteDto = offerLetterService.getINoteByOLIds(olIds);
			return ResponseEntity.ok(iNoteDto);
		} catch (Exception e) {
			logger.error("Error retrieving iNote for provided OL IDs: {}", olIds, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	/**
	 * @param iNoteDto
	 * @return status
	 * @author Awadhesh Verma
	 */
	@PostMapping("/updateINote")
	public ResponseEntity<String> updateINote(@RequestBody INoteDto iNoteDto) {
		try {
			String response = iNoteService.updateINote(iNoteDto);
			if (response.equals(ApplicationConstants.SUCCESS)) {
				return ResponseEntity.ok().body(response);
			} else {
				return ResponseEntity.internalServerError().body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
		}
	}
	
	/**
	 * @param offerLetterDto
	 * @return status
	 * @author Awadhesh Verma
	 */
	@PostMapping("/updateQaclForInote")
	public ResponseEntity<String> updateQaclForInote(@RequestBody OfferLetterDto offerLetterDto) {
		try {
			String response = offerLetterService.updateQaclForInote(offerLetterDto);
			if (response.equals(ApplicationConstants.SUCCESS)) {
				return ResponseEntity.ok().body(response);
			} else {
				return ResponseEntity.internalServerError().body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
		}
	}
	
    @GetMapping("/getInotesByProdId/{partNumberId}")
    public ResponseEntity<List<InoteDetailsDto>> getInotesByProdId(@PathVariable int  partNumberId) {
        try {
        	List<InoteDetailsDto> iNoteDto = offerLetterService.getInotesByProdId(partNumberId);
            return ResponseEntity.ok(iNoteDto);
        }catch(Exception e) {
        	 logger.error("Error retrieving iNotes for PartNumberId: {}", partNumberId, e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
