package in.co.bel.olms.dgqa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.dgqa.dto.INoteDashBoardDto;
import in.co.bel.olms.service.INoteDashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class INoteDashBoardController {

	private final INoteDashboardService iNoteDashBoardService;

	private static final Logger logger = LoggerFactory.getLogger(INoteDashBoardController.class);

	@GetMapping("/Inotedetails")
	public ResponseEntity<INoteDashBoardDto> getINoteDashBoard() {
		try {
			INoteDashBoardDto iNoteDashBoardDto = iNoteDashBoardService.getINoteDashBoard();
			return ResponseEntity.ok(iNoteDashBoardDto);
		} catch (Exception e) {
			logger.error("Get i-note deatils error:{}", e.getMessage());
			return new ResponseEntity<>(new INoteDashBoardDto(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
