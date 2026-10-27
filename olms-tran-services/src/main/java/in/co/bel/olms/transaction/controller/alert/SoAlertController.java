package in.co.bel.olms.transaction.controller.alert;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.transaction.dto.alert.AlertDto;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */
@RestController
@RequestMapping("/alert/so")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoAlertController {
	
	@GetMapping("/getAlerts/{empid}")
	public ResponseEntity<AlertDto> getAlerts(@PathVariable int empid) {
		
		AlertDto alertDto = new  AlertDto();
		return ResponseEntity.ok(alertDto);
	}
	
}
