package in.co.bel.olms.rpt.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.service.INoteDashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class INoteDashboardController {

	private final INoteDashboardService iNoteDashboardService;

	private static final Logger logger = LoggerFactory.getLogger(INoteDashboardController.class);

	@GetMapping("/getINoteValueBySalesGrpId")
	public ResponseEntity<List<Map<String, String>>> getINoteValueBySalesGrpId(
			@RequestParam(required = true) Integer salesGrpId, @RequestParam(required = true) Integer selectedFY)
			throws DataNotFoundException {

		List<Map<String, String>> chartDataList = null;
		try {
			chartDataList = iNoteDashboardService.getINoteValueBySalesGrpId(salesGrpId, selectedFY);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getINoteValueByFY")
	public ResponseEntity<List<Map<String, String>>> getINoteValueByFY(@RequestParam(required = true) Integer orgId,
			@RequestParam(required = true) Integer selectedFY) throws DataNotFoundException {

		List<Map<String, String>> chartDataList = null;
		try {
			chartDataList = iNoteDashboardService.getINoteValueByFY(orgId, selectedFY);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
