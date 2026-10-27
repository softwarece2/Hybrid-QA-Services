package in.co.bel.olms.transaction.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.service.ol.OlApprovalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/olApproval")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlApprovalDetailsController {

	private final OlApprovalDetailsService olApprovalService;
	
	private static final Logger logger =LoggerFactory.getLogger(OlApprovalDetailsController .class);

	private final Gson gson = new Gson();

	@PostMapping("/updateOlApproval")
	public ResponseEntity<OlApprovalDto> updateOlApproval(
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		OlApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
		}.getType());
        OlApprovalDto updatedOlApprovalDto = olApprovalService.updateFileNoteApproval(olApprovalDto, uploads, null);
		if (updatedOlApprovalDto != null) {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/updateInternalOlApproval")
	public ResponseEntity<Object> updateInternalOlApproval(
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		try {
			OlApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
			}.getType());
			OlApprovalDto updatedOlApprovalDto = olApprovalService.updateInternalOlApproval(olApprovalDto, uploads, null);
			if (updatedOlApprovalDto != null) {
				return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Update failed: {}", e.getMessage());
			log.error("/olApproval/updateInternalOlApproval Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}
	
	
	@GetMapping("/{olId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsByOlId(@PathVariable int olId, @PathVariable int empId) throws DataNotFoundException {

		SoApprovalDto soApprovalDto = olApprovalService.getOlApprovalDetailsByOlId(olId, empId);
		return ResponseEntity.ok(soApprovalDto);

	}
	@GetMapping("ByFileNoteId/{fileNoteId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsByFileNoteId(@PathVariable int fileNoteId, @PathVariable int empId) throws DataNotFoundException {

		SoApprovalDto soApprovalDto = olApprovalService.getOlApprovalDetailsByFileNoteId(fileNoteId, empId);
		return ResponseEntity.ok(soApprovalDto);

	}

	@PostMapping("/updateOlApprovalNew")  //Temp soln for ol approval
	public ResponseEntity<OlApprovalDto> updateOlApprovalNew(
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		OlApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
		}.getType());
        OlApprovalDto updatedOlApprovalDto = olApprovalService.updateFileNoteApprovalNew(olApprovalDto, uploads);
		if (updatedOlApprovalDto != null) {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
