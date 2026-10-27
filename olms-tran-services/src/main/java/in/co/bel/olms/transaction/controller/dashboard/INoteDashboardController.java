package in.co.bel.olms.transaction.controller.dashboard;

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

import in.co.bel.olms.master.dto.INoteChartValuePair;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.service.INoteDashboardService;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for INote Dashboard APIs
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-05-12
 */

@RestController
@RequestMapping("/dashboard/inote/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class INoteDashboardController {

	private final INoteDashboardService iNoteDashboardService;

	private static final Logger logger = LoggerFactory.getLogger(INoteDashboardController.class);

	@GetMapping("/getINoteDetailsYearWise")
	public ResponseEntity<List<INoteChartValuePair>> getINoteDetailsForChart(
			@RequestParam(required = true) Integer selectedFY, @RequestParam(required = false) Integer term)
			throws DataNotFoundException {

		List<INoteChartValuePair> chartDataList = null;
		try {
			chartDataList = iNoteDashboardService.getINoteDetailsForChart(selectedFY, term);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getINoteDetailsProjectWise")
	public ResponseEntity<List<INoteChartValuePair>> getINoteDetailsProjectWise(
			@RequestParam(required = true) Integer projectId) throws DataNotFoundException {

		List<INoteChartValuePair> chartDataList = null;
		try {
			chartDataList = iNoteDashboardService.getINoteDetailsProjectWise(projectId);
			return ResponseEntity.ok(chartDataList);
		} catch (DataNotValidException ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new ResponseEntity<>(chartDataList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
