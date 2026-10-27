package in.co.bel.olms.ate.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.ate.dto.VideoRecordsDto;
import in.co.bel.olms.ate.service.VideoService;
import lombok.RequiredArgsConstructor;
import in.co.bel.olms.ate.dto.VideoNotificationDto;

@RestController
@RequestMapping("/video")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class VideoController {

	private final VideoService videoService;

	@GetMapping("/records")
	public ResponseEntity<Object> getRecordings(@RequestParam(required = false) Integer olId,
			@RequestParam(required = false) Integer olLiId, @RequestParam(required = false) Integer devId,
                                                @RequestParam(required = false) Integer sSlipId, @RequestParam(required = false) Integer sSlipLiId,
                                                @RequestParam(required = false) Integer sSlipDevId, @RequestParam(required = false) Integer testId,
                                                @RequestParam(required = false) Integer empId) {
		try {
            List<VideoRecordsDto> resp = videoService.getVideoRecords(olId, olLiId, devId, sSlipId, sSlipLiId, sSlipDevId, testId, empId);
			return ResponseEntity.ok(resp);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PostMapping("/records/save")
	public ResponseEntity<String> saveRecording(@RequestBody VideoRecordsDto dto) {
		try {
			String response = videoService.saveVideoRecord(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@GetMapping("/notifications/getAll")
	public ResponseEntity<Object> getAllVideoNotification(@RequestParam(required = false) Integer olId,
			@RequestParam(required = false) Integer olLiId, @RequestParam(required = false) Integer devId,
			@RequestParam(required = false) Integer testId, @RequestParam(required = false) Integer empId) {
		try {
			List<VideoNotificationDto> videoNotificationDtlDtoList = videoService.getAllVideoNotifications(olId, olLiId, devId, testId, empId);
			return ResponseEntity.ok(videoNotificationDtlDtoList);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
		}
	}

	@PostMapping("/notifications/save")
	public ResponseEntity<String> saveVideoNotification(
			@RequestBody @Valid VideoNotificationDto videoNotificationDtlDto) {

		try {
			String response = videoService.saveVideoNotification(videoNotificationDtlDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			return ResponseEntity.badRequest().body(ae.getLocalizedMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
		}
	}

	@PostMapping("/notifications/updateStatus")
	public ResponseEntity<String> updateVideoNotificationStatus(
			@RequestBody @Valid VideoNotificationDto videoNotificationDtlDto) {

		try {
			String response = videoService.updateVideoNotificationStatus(videoNotificationDtlDto);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException ae) {
			return ResponseEntity.badRequest().body(ae.getLocalizedMessage());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
		}
	}

}
