package in.co.bel.olms.transaction.controller.insp;

import java.util.List;

import in.co.bel.olms.transaction.dto.insp.FileNoteINoteDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.insp.InspectionFileNoteDto;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.insp.InspectionFileNoteService;
import in.co.bel.olms.transaction.service.ol.OlApprovalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Manasa B
 * @author Awadhesh Verma
 *
 */

@Slf4j
@RestController
@RequestMapping("/insp/filenote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InspectionFileNoteController {

	private final InspectionFileNoteService inspectionFileNoteService;

	private final OlApprovalDetailsService olApprovalService;

	private final OfferLetterService olService;

	@Value("${bel.jwt.secret}")
	private String jwtSecret;

	Gson gson = new Gson();

	private static final Logger logger = LoggerFactory.getLogger(InspectionFileNoteController.class);

	/**
	 * 
	 * @author Manasa B
	 * @param soid
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/so/{soid}")
	public ResponseEntity<InspectionFileNoteDto> getBySupplyOrder(@PathVariable int soid) throws DataNotFoundException {
		InspectionFileNoteDto fileNote = inspectionFileNoteService.getInspFileNoteBySoId(soid);
		return ResponseEntity.ok(fileNote);
	}

	/**
	 * @author Shubham Anand
	 * @param olId
	 * @return ResponseEntity<InspectionFileNoteDto>
	 */
	@GetMapping("/getByOfferLetterId/{olId}")
	public ResponseEntity<InspectionFileNoteDto> getByOfferLetterId(@PathVariable int olId) {
		return inspectionFileNoteService.getInspFileNoteByOlId(olId);
	}

	/**
	 * @author Shubham Anand
	 * @param inspectionFileNoteDtoStr
	 * @return ResponseEntity<String>
	 */

	@PreAuthorize("hasAnyAuthority('INSP','JCQAO','DCQAO','ACQAO','CQAO')")
	@PostMapping("/saveInspctionFileNote")
	public ResponseEntity<String> saveInspctionFileNote(
			@RequestParam(value = "inspectionFileNoteDtoStr", required = false) String inspectionFileNoteDtoStr,
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		String resposneString = null;
		OlApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
		}.getType());
		if (StringUtils.isNotBlank(inspectionFileNoteDtoStr)) {
			resposneString = inspectionFileNoteService.saveInspectionFileNote(inspectionFileNoteDtoStr);
			// Updating the OL Status on successful inspection file note save by inspector
			if (resposneString.equals(ApplicationConstants.SUCCESS)) {
				logger.info("Updating the OL Status");
				olService.updateOlStatus(olApprovalDto.getOfferLetterId(), ApplicationConstants.OL_COMPLETED);
			}
		}
		OlApprovalDto returnDto = olApprovalService.updateOlApproval(olApprovalDto, uploads);
		if ((resposneString == null || resposneString.equals(ApplicationConstants.SUCCESS)) && returnDto != null) {
			return ResponseEntity.ok(ApplicationConstants.SUCCESS);
		} else if (resposneString != null && resposneString.equals(ApplicationConstants.RETURN_INVALID_INPUT)) {
			return ResponseEntity.badRequest().body(ApplicationConstants.RETURN_INVALID_INPUT);
		}
		return ResponseEntity.internalServerError().body(ApplicationConstants.FAILURE);
	}

	@GetMapping("/getByOLIds")
	public ResponseEntity<InspectionFileNoteDto> getByOfferLetterId(@RequestParam List<Integer> olIds) {
		return inspectionFileNoteService.getInspFileNoteByOlIds(olIds);
	}

//	@PreAuthorize("hasAnyAuthority('INSP','JCQAO','DCQAO','ACQAO','CQAO')")
	@Transactional
	@PostMapping("/saveInspctionFileNoteV2")
	public ResponseEntity<OlApprovalDto> saveInspctionFileNoteV2(
			@RequestParam(value = "inspectionFileNoteDtoStr", required = false) String inspectionFileNoteDtoStr,
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads,
			@RequestHeader("Authorization") String token) {
		try {
//		String resposneString = null;
			// if (StringUtils.isNotBlank(inspectionFileNoteDtoStr)) {
			String jwtToken = token.substring(7);
			Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken).getBody();
			String role = claims.get("roles", String.class);
			
			int empId = claims.get("empId", Integer.class);
			OlApprovalDto olApprovalDtos = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
			}.getType());
			if(olApprovalDtos.getCreatedBy() != empId) {
				logger.warn("Unauthorized access attempt");
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); 
			}
			OlApprovalDto olApprovalDto = inspectionFileNoteService.saveInspectionFileNoteV2(inspectionFileNoteDtoStr,
					olApprovalDtoStr, uploads, role);
			// }
			if (olApprovalDto != null) {
				return ResponseEntity.ok(olApprovalDto);
			} else {
				return ResponseEntity.badRequest().body(olApprovalDto);
			}
		} catch (DataNotValidException de) {
			logger.error("Invalid characters or empty string");
			 logger.error("Error occurred while saving plan: {}", de.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception e) {
			logger.info("Something went wrong");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

	@GetMapping("/getAllFileNotes/{orgId}")
	public ResponseEntity<List<InspectionFileNoteDto>> getAllFileNotes(@PathVariable int orgId) {
		List<InspectionFileNoteDto> fileNotes = inspectionFileNoteService.getAllFileNotes(orgId);
		return ResponseEntity.ok(fileNotes);

	}

	@GetMapping("/getFileNoteById/{fileNoteId}")
	public ResponseEntity<InspectionFileNoteDto> getFileNote(@PathVariable int fileNoteId) {
		InspectionFileNoteDto fileNote = inspectionFileNoteService.getFileNoteById(fileNoteId);
		return ResponseEntity.ok(fileNote);

	}

	@PostMapping("/updateInspctionFileNoteV2")
	public ResponseEntity<OlApprovalDto> updateInspctionFileNoteV2(
			@RequestParam(value = "inspectionFileNoteDtoStr", required = false) String inspectionFileNoteDtoStr,
			@RequestParam(value = "olApprovalDtoStr", required = false) String olApprovalDtoStr) {
//		String resposneString = null;
		// if (StringUtils.isNotBlank(inspectionFileNoteDtoStr)) {
		OlApprovalDto olApprovalDto = inspectionFileNoteService.updateInspectionFileNoteV2(inspectionFileNoteDtoStr,
				olApprovalDtoStr);
		// }
		if (olApprovalDto != null) {
			return ResponseEntity.ok(olApprovalDto);
		} else {
			return ResponseEntity.badRequest().body(olApprovalDto);

		}
	}

	@PostMapping("/updateFileNoteOlDesc")
	public ResponseEntity<Object> updateFileNoteOlDesc(@RequestBody InspectionFileNoteDto inspFileNoteDto) {
		try {
			String response = inspectionFileNoteService.updateFileNoteOlDesc(inspFileNoteDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			log.error("/filenote/updateFileNoteOlDesc IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			log.error("/filenote/updateLineItemsQty Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/getINoteNos/{fileNoteId}")
	public ResponseEntity<List<FileNoteINoteDto>> getINoteNos(@PathVariable int fileNoteId) {

		List<FileNoteINoteDto> fileNoteINoteDtos = inspectionFileNoteService.getINoteNoByFileNoteId(fileNoteId);
		return ResponseEntity.ok(fileNoteINoteDtos);

	}

	@PostMapping("/updateINoteNo")
	public ResponseEntity<String> updateINoteNo(@RequestBody List<FileNoteINoteDto> fileNoteINoteDtos) {
		String status = inspectionFileNoteService.updateINoteNo(fileNoteINoteDtos);
		if (status.equals(ApplicationConstants.SUCCESS)) {
			return ResponseEntity.ok(status);
		} else {
			return ResponseEntity.internalServerError().body(status);
		}
	}

}
