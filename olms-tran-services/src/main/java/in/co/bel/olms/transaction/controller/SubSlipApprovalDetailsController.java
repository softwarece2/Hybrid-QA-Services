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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.dto.SubSlipApprovalDto;
import in.co.bel.olms.transaction.dto.SubSlipSerialNoDto;
import in.co.bel.olms.transaction.service.SubSlipApprovalDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/subSlipApproval")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SubSlipApprovalDetailsController {

	private final SubSlipApprovalDetailsService subSlipApprovalService;

	private static final Logger logger = LoggerFactory.getLogger(SubSlipApprovalDetailsController.class);

	Gson gson = new Gson();

	@PostMapping("/update")
	public ResponseEntity<SubSlipApprovalDto> updateSSlipApproval(
			@RequestParam(value = "subSlipApprovalDtoStr", required = true) String subSlipApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		SubSlipApprovalDto subSlipApprovalDto = gson.fromJson(subSlipApprovalDtoStr, new TypeToken<OlApprovalDto>() {
		}.getType());
		SubSlipApprovalDto updatedOlApprovalDto = subSlipApprovalService.updateFileNoteApproval(subSlipApprovalDto,
				uploads, null);
		if (updatedOlApprovalDto != null) {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * modified by : Shridhar R
	 * similar to updateInternalOlApproval 
	 */
	@PostMapping("/updateInternalSubSlipApproval")
	public ResponseEntity<Object> updateInternalOlApproval(
			@RequestParam(value = "olApprovalDtoStr", required = true) String olApprovalDtoStr,
			@RequestParam(value = "uploads", required = false) MultipartFile[] uploads) {
		try {
			SubSlipApprovalDto olApprovalDto = gson.fromJson(olApprovalDtoStr, new TypeToken<SubSlipApprovalDto>() {
			}.getType());
			SubSlipApprovalDto updatedOlApprovalDto = subSlipApprovalService.updateInternalOlApproval(olApprovalDto, uploads,
					null);
			if (updatedOlApprovalDto != null) {
				return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(updatedOlApprovalDto, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error("update internal olApproval Error {}",e);
			log.error("/olApproval/updateInternalSubSlipApproval Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}

	@GetMapping("/{subSlipId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsByOlId(@PathVariable Integer subSlipId, @PathVariable Integer empId)
			throws DataNotFoundException {

		SoApprovalDto soApprovalDto = subSlipApprovalService.getOlApprovalDetailsByOlId(subSlipId, empId);
		return ResponseEntity.ok(soApprovalDto);

	}

	@GetMapping("ByFileNoteId/{fileNoteId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsByFileNoteId(@PathVariable int fileNoteId,
			@PathVariable int empId) throws DataNotFoundException {

	SoApprovalDto soApprovalDto = subSlipApprovalService.getOlApprovalDetailsByFileNoteId(fileNoteId, empId);
		return ResponseEntity.ok(soApprovalDto);

	}
	
	@GetMapping("/getPassedRegNoBySubSlipId/{subSlipId}")
	public ResponseEntity<List<SubSlipSerialNoDto>> getPassedRegNoBySubSlipId(@PathVariable Integer subSlipId) throws DataNotFoundException{
		List<SubSlipSerialNoDto> dto = subSlipApprovalService.getPassedRegNoBySubSlipId(subSlipId);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/getRegNoBySubSlipId/{subSlipId}")
	public ResponseEntity<List<SubSlipSerialNoDto>> getRegNoBySubSlipId(@PathVariable Integer subSlipId) throws DataNotFoundException{
		List<SubSlipSerialNoDto> dto = subSlipApprovalService.getRegNoBySubSlipId(subSlipId);
		return ResponseEntity.ok(dto);
	}

}
