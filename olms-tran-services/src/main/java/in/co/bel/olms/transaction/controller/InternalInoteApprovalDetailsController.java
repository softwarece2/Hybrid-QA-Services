package in.co.bel.olms.transaction.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.service.InternalInoteApprovalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/internalInote/approval")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InternalInoteApprovalDetailsController {

	private final InternalInoteApprovalService  olApprovalService;
	
	private static final Logger logger = LoggerFactory.getLogger(InternalInoteApprovalDetailsController.class);

	private final Gson gson = new Gson();

	@PostMapping("/updateOlApproval")
	public ResponseEntity<OlApprovalDto> updateOlApproval(
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		OlApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<OlApprovalDto>() {
		}.getType());
        OlApprovalDto updatedOlApprovalDto = olApprovalService.updateOlApproval(olApprovalDto, uploads);
		if (updatedOlApprovalDto != null) {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{internalInoteId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsByOlId(@PathVariable int internalInoteId, @PathVariable int empId) throws DataNotFoundException {
		SoApprovalDto soApprovalDto = olApprovalService.getInternalInoteApprovalDetailsByOlId(internalInoteId, empId);
		return ResponseEntity.ok(soApprovalDto);
	}
	
	@GetMapping("getAllInternalInotesByEmpId/{empId}")
	public ResponseEntity<List<SoApprovalDto>> getAllInternalInotesByEmpId( @PathVariable int empId) throws DataNotFoundException {
		List<SoApprovalDto> soApprovalDto = olApprovalService.getAllInternalInotesByEmpId( empId);
		return ResponseEntity.ok(soApprovalDto);
	}
}
