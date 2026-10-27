package in.co.bel.olms.iol.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.dto.ColdTestDto;
import in.co.bel.olms.iol.dto.DevDetDto;
import in.co.bel.olms.iol.entity.DeviceStatusDto;
import in.co.bel.olms.iol.service.IOLColdTestService;
import in.co.bel.olms.master.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/iolColdTest")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class IOLColdTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(IOLColdTestController.class);

	private final IOLColdTestService  iolColdTestService;
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/saveColdTest")
	public ResponseEntity<String> saveColdTest(@RequestBody ColdTestDto dto) throws Exception{
		try {
			if (dto.getOlLineItemId() == null || dto.getApplicableTestId() == null) {
				throw new IllegalArgumentException("The id value can not be null");
			}
			String result = iolColdTestService.saveColdTest(dto);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			logger.error("saveColdTest IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to save cold test details: {}", e.getMessage());
			logger.error("Failed to save ", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to save cold test details");
		}
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@PutMapping("/editColdTest")
	public ResponseEntity<String> editColdTest(@RequestBody ColdTestDto dto) {
		try {
			if (dto.getOlLineItemId() == null || dto.getApplicableTestId() == null) {
				throw new IllegalArgumentException("The id value can not be null");
			}
			String result = iolColdTestService.editColdTest(dto);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			logger.error("editColdTest IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to edit cold test details: {}", e.getMessage());
			logger.error("Failed to edit ", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to edit cold test details");
		}
	}
	
	/**
	 * 
	 * @param Id
	 * @return
	 * @throws DataNotFoundException
	 * @throws ParseException 
	 */
	@GetMapping("/getColdTestByDeviceId/{Id}")
    public ResponseEntity<List<ColdTestDto>> getColdTestByDeviceId(@PathVariable int id)
                    throws DataNotFoundException, ParseException {
            List<ColdTestDto> dto = iolColdTestService.getColdTestByDeviceId(id);
            return ResponseEntity.ok(dto);

    }
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@PostMapping("/submitColdTest")
	public ResponseEntity<String> submitColdTest(@RequestBody ColdTestDto dto) {	
		try {
			String result = iolColdTestService.submitColdTest(dto);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			logger.error("submitColdTest IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to submit cold test details: {}", e.getMessage());
			logger.error("Failed to submit ", e);
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Failed to submit cold test details");
		}
	}
	
	/**
	 * 
	 * @param Id
	 * @return
	 * @throws DataNotFoundException
	 */
		@GetMapping("getDeviceByHotOrColdId/{Id}")
	public ResponseEntity<List<DeviceStatusDto>> getDeviceByHotOrColdId(@PathVariable int id)throws DataNotFoundException {
		List<DeviceStatusDto> dto = iolColdTestService.getDeviceByHotOrColdId(id);
		return ResponseEntity.ok(dto);

	}
		
		/**
		 * 
		 * @param olId
		 * @return
		 * @throws DataNotFoundException
		 */
	@GetMapping("getSubmittedDetailsByOlId/{olId}")
	public ResponseEntity<List<DeviceStatusDto>> getSubmittedDetailsByOlId(@PathVariable int olId)
			throws DataNotFoundException {
		List<DeviceStatusDto> dto = iolColdTestService.getSubmittedDetailsByOlId(olId);
		return ResponseEntity.ok(dto);

	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 * @throws DataNotFoundException
	 */
	@PostMapping("validateDto")
	public ResponseEntity<DeviceStatusDto> validateDto(@RequestBody ColdTestDto dto) throws DataNotFoundException {
		DeviceStatusDto dtos = iolColdTestService.validateDto(dto);
		return ResponseEntity.ok(dtos);
	}
	
	/**
	 * 
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/getColdTestByCreatedDate")
	public ResponseEntity<String> getColdTestByCreatedDate() throws DataNotFoundException {
		String dto = iolColdTestService.getColdTestByCreatedDate();
		return ResponseEntity.ok(dto);
	}

	/**
	 * 
	 * @return
	 * @throws DataNotFoundException
	 * @throws ParseException 
	 */
	@GetMapping("/getAllColdTestsDevDetails")
	public ResponseEntity<DevDetDto> getAllColdTestsDevDetails(@RequestParam int productTypeId,
			@RequestParam int testTypeId, @RequestParam String fromDate, @RequestParam String toDate)
			throws DataNotFoundException, ParseException {
		DevDetDto dto = new DevDetDto();
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.IST_DATE_FORMAT);
		Date from;
		Date to;
		from = sdf.parse(fromDate);
		to = sdf.parse(toDate);
		if (productTypeId == 1) {
		 dto = iolColdTestService.getAllColdTestsDevDetails(testTypeId, from, to);
		}
		return ResponseEntity.ok(dto);

	}
}
