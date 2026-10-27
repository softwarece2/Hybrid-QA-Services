package in.co.bel.olms.transaction.controller.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.transaction.dto.dashboard.DashFirmDto;
import in.co.bel.olms.transaction.service.dashboard.DashFirmService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashFirm")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DashFirmController {

	private final DashFirmService dashFirmService;
	
	private static final Logger logger = LoggerFactory.getLogger(DashFirmController.class);
	
	@GetMapping("/getAllDashFirms")
	public ResponseEntity<List<DashFirmDto>> getAllDashFirms() throws DataNotFoundException{
		
		return ResponseEntity.ok(dashFirmService.getAllDashFirms());
		
	}
}
