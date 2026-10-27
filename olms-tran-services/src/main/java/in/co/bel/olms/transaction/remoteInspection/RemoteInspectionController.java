package in.co.bel.olms.transaction.remoteInspection;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.websocket.WSService;
import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/remoteInspection")
@CrossOrigin(origins="*",allowedHeaders="*")
@RequiredArgsConstructor
public class RemoteInspectionController {

	private final WSService wsService;
	
	@PostMapping("/sendLink")
	public ResponseEntity<String> sendVideoConsultationLink(@RequestBody List<Integer> userIdList, @RequestParam String meetingId, @RequestParam String empName){
		try {
			MessageBean messageBean = new MessageBean(empName + " has invited you to a meeting. Do you want to join?");
			messageBean.setMeetingId(meetingId);
			messageBean.setReceivers(userIdList);
			messageBean.setMessageType("ri-link");
			wsService.notifyCqae(messageBean);
			return ResponseEntity.ok(ApplicationConstants.SUCCESS);
		} catch(Exception e) {
			log.error("RemoteInspectionController sendVideoConsultationLink Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}
	
	@PostMapping("/sendWebcamLink")
	public ResponseEntity<String> sendWebcamVideoConsultationLink(@RequestBody List<Integer> userIdList, @RequestParam(required = false) String meetingId, @RequestParam String empName, @RequestParam Integer olId,
			@RequestParam String olNumber, @RequestParam String lineItemNo){
		try {
			MessageBean messageBean = new MessageBean(empName + " has invited you for Remote Inspection. Do you want to join?");
			messageBean.setMeetingId(meetingId);
			messageBean.setReceivers(userIdList);
			messageBean.setMessageType("webcam-link");
			messageBean.setOlId(olId);
			messageBean.setOlNumber(olNumber);
			messageBean.setLineItemNo(lineItemNo);
			wsService.notifyCqae(messageBean);
			return ResponseEntity.ok(ApplicationConstants.SUCCESS);
		} catch(Exception e) {
			log.error("RemoteInspectionController sendVideoConsultationLink Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body("Internal Server Error");
		}
	}
}
