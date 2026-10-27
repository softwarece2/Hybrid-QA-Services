package in.co.bel.olms.transaction.controller.dashboard;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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

import in.co.bel.olms.master.dto.ChartValuePair;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.service.OfferLetterService;
import in.co.bel.olms.transaction.service.SupplyOrderService;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for Dashboard APIs
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-03-31
 */

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class DashboardController {

	private final SupplyOrderService poService;

	private final OfferLetterService olService;

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@GetMapping("/getPoDetailsForChart")
	public ResponseEntity<List<ChartValuePair>> getPoDetailsForChart(@RequestParam(required = true) Integer orgId,
			@RequestParam(required = false) Integer consigneeId, @RequestParam Integer indentorId, @RequestParam Integer salesGrpId) throws DataNotFoundException {
		
		List<ChartValuePair> chartDataList = null;
		try {
			chartDataList = poService.getPoDetailsForChart(orgId, consigneeId,
					indentorId, salesGrpId);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Error occurred in getting poDetails for Chart: ", e);
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getActiveOlsForChart")
	public ResponseEntity<List<ChartValuePair>> getActiveOlsForChart(@RequestParam(required = true) Integer orgId,
			@RequestParam(required = false) Integer projectId, @RequestParam(required = false) Integer inspectorId) throws DataNotFoundException {
		
		List<ChartValuePair> chartDataList = null;
		try {
			chartDataList = olService.getActiveOlForDashboard(orgId, projectId,
					inspectorId);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getOLChartData")
	public ResponseEntity<List<Map<String, Object>>> getReturnedNClearedOlsDashboard(@RequestParam(required = true) Integer orgId,
			@RequestParam(required = true) Integer category, @RequestParam(required = true) Integer categoryValue) throws DataNotFoundException {

		List<Map<String, Object>> chartDataList = null;
		try {
			chartDataList = olService.getReturnedNClearedOlsDashboard(orgId, category, categoryValue);
			return ResponseEntity.ok(chartDataList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getLastFiveFY")
	public ResponseEntity<List<String>> getLastFiveFY() {

		List<String> lastFiveYears = new ArrayList<>();
		try {
			LocalDate now = LocalDate.now();
			int currentYear = now.getYear();
			if (now.getMonthValue() > Month.MARCH.getValue()) {
				currentYear++;
			}
			// Get the start year for the list
			int startYear = currentYear - 4;

			for (int i = startYear; i <= currentYear; i++) {
				// Get the financial year start date
				LocalDate fyStartDate = LocalDate.of(i, 4, 1);
				String yearString = fyStartDate.format(DateTimeFormatter.ofPattern("yyyy"));
				lastFiveYears.add(yearString);
			}
			Collections.reverse(lastFiveYears);
		} catch (Exception e) {
			logger.error("Could not get the last 5 FY due to: {}", e.getMessage());
		}
		return ResponseEntity.ok(lastFiveYears);
	}
	
}
