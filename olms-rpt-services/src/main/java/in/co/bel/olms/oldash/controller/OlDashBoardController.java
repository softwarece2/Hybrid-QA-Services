package in.co.bel.olms.oldash.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.dgqa.dto.OlDashBoardDto;
import in.co.bel.olms.dgqa.dto.OlDetDto;
import in.co.bel.olms.dgqa.service.OlDashBoardService;
import in.co.bel.olms.dgqa.service.OlDetService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/olDashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class OlDashBoardController {

	private final OlDashBoardService olDashBoardService;

	private final OlDetService olDetService;
	
	private static final Logger logger = LoggerFactory.getLogger(OlDashBoardController.class);
	
	@GetMapping("/getoldetails")
	public ResponseEntity<OlDashBoardDto> getOlDashboard() {
		try {
			OlDashBoardDto olDashBoardDto = olDashBoardService.getOlDashBoard();
			return ResponseEntity.ok(olDashBoardDto);
		} catch (Exception e) {
			logger.error("Get ol deatils error:{}", e.getMessage());
			return new ResponseEntity<>(new OlDashBoardDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @GetMapping("/getOlDet")
	    public List<OlDetDto> getOlDet(
	            @RequestParam int month, @RequestParam int year) { 
		    return olDetService.getOlDets(month, year);
	    }
	

}
