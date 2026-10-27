package in.co.bel.olms.dgqa.controller;

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

import in.co.bel.olms.dgqa.dto.SoDashBoardDto;
import in.co.bel.olms.dgqa.dto.SoDetDto;
import in.co.bel.olms.dgqa.service.SoDashBoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/soDashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class SoDashBoardController {
	
	private final SoDashBoardService soDashBoardService;

	private static final Logger logger = LoggerFactory.getLogger(SoDashBoardController.class);

	@GetMapping("/getsodetails")
	public ResponseEntity<SoDashBoardDto> getSoDashboard() {
		try {
			SoDashBoardDto soDashBoardDto = soDashBoardService.getSoDashboard();
			return ResponseEntity.ok(soDashBoardDto);
		} catch (Exception e) {
			logger.error("Get so deatils error:{}", e.getMessage());
			return new ResponseEntity<>(new SoDashBoardDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getSoDet")
    public List<SoDetDto> getSoDet(
            @RequestParam int month, @RequestParam int year) { 
	    return soDashBoardService.getSoDets(month, year);
    }

}
