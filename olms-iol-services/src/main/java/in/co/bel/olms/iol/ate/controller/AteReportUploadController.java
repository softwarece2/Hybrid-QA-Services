package in.co.bel.olms.iol.ate.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.iol.ate.dto.AteTestReportUploadDto;
import in.co.bel.olms.iol.ate.service.AteReportUploadService;
import in.co.bel.olms.iol.ate.service.InspectionRoundService;
import lombok.RequiredArgsConstructor;

/**
 * @author Naveen B
 * @since 27-10-22
 */
@RestController
@RequestMapping("/ateReport")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequiredArgsConstructor
public class AteReportUploadController {
	
	private static final Logger logger = LoggerFactory.getLogger(AteReportUploadController.class);
	
	private final HmisAppSecurity app = new HmisAppSecurity();

	Gson gson = new Gson();

	private final AteReportUploadService ateUploadService;

	private final InspectionRoundService inspectionRoundService;
	
	/**
	 * Upload the ATE report data
	 */
	@PostMapping("/upload")
	public ResponseEntity<Object> ateReportUpload(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "ateTestReportUploadDtoStr", required = true) String ateTestReportUploadDtoStr,
			@RequestHeader(name = "Authorization") String token) {
		
		Integer empId = app.getEmpIdFromJwtToken(token);
		Boolean isQa = inspectionRoundService.isQaEmployee(empId);
		AteTestReportUploadDto ateTestReportDto = gson.fromJson(ateTestReportUploadDtoStr, new TypeToken<AteTestReportUploadDto>() {
		}.getType());
		
		try {
			List<AteTestReportUploadDto> response = ateUploadService.uploadAteReportData(ateTestReportDto, files, isQa);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			 logger.error("Error occurred during ateReportUpload: ", e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@PostMapping("/delete/{fileUploadId}")
	public ResponseEntity<Object> ateReportUploadDelete(@PathVariable(name = "fileUploadId") Integer fileUploadId) {
		try {
			if(fileUploadId == null) 
				throw new IllegalArgumentException("File-upload ID is null.");
			
			String response = ateUploadService.deleteFile(fileUploadId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error occurred while deleting file with ID: {}", fileUploadId, e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}
	
	@GetMapping("/fetch-files")
	public ResponseEntity<Object> getAteReportsBySpec(
	        @RequestParam Integer devId,
	        @RequestParam Integer prodId,
	        @RequestParam Integer testId,
	        @RequestParam Integer roundId,
	        @RequestParam Integer specId,
	        @RequestHeader(name = "Authorization") String token) {
	    
	    Integer empId = app.getEmpIdFromJwtToken(token);
	    Boolean isQa = inspectionRoundService.isQaEmployee(empId);

	    try {
	        List<AteTestReportUploadDto> reports = ateUploadService.getAteReportsBySpec(devId, prodId, testId, roundId, specId, isQa);
	        return ResponseEntity.ok(reports);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body(e.getMessage());
	    }
	}
	
}
