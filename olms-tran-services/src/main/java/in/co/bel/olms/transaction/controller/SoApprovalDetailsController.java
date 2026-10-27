package in.co.bel.olms.transaction.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.OlApprovalDto;
import in.co.bel.olms.transaction.dto.SoApprovalDto;
import in.co.bel.olms.transaction.entity.SoApprovalDetails;
import in.co.bel.olms.transaction.service.so.SoApprovalDetailsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soApproval")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoApprovalDetailsController {

	private final SoApprovalDetailsService soApprovalService;

	Gson gson = new Gson();
	
	@PostMapping("/updateSoApproval")
	public ResponseEntity<Boolean> updateSoApproval(@RequestParam(value = "soApprovalDetailsStr", required = true) String soApprovalDetailsStr,
			@RequestParam(value = "attachments", required = false) MultipartFile[] attachments) {
		
		Gson gson = new Gson();
		OlApprovalDto soApprovalDto = gson.fromJson(soApprovalDetailsStr, new TypeToken<OlApprovalDto>() {
		}.getType());
		
		Boolean returnStatus = soApprovalService.updateSoApproval(soApprovalDto , attachments);
		if (returnStatus.booleanValue()) {

			return new ResponseEntity<>(returnStatus, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(returnStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{soId}/{empId}")
	public ResponseEntity<SoApprovalDto> getOlApprovalDetailsBySoId(@PathVariable int soId, @PathVariable int empId) throws DataNotFoundException {
		SoApprovalDto soApprovalDto = soApprovalService.getSoApprovalDetailsBySoId(soId, empId);
		return ResponseEntity.ok(soApprovalDto);
	}

	@PostMapping("/feedBackUpdateSo")
	public ResponseEntity<SoApprovalDetails> feedBackUpdateSo(@RequestBody OlApprovalDto soApprovalDto)  throws DataNotFoundException {
		SoApprovalDetails soApprovalDetails = soApprovalService.feedBackUpdateSo(soApprovalDto);
		return ResponseEntity.ok(soApprovalDetails);
	}
}
