package in.co.bel.olms.iol.ate.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.iol.ate.dto.drilldown.AteDrilldownOverviewDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteFailedDeviceDetailDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteFailedParameterDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteSpecTrendGraphDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteTestFailureGroupDto;
import in.co.bel.olms.iol.ate.service.AteDrilldownDashboardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ateDrilldownDash")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AteDrilldownDashboardController {

	private final AteDrilldownDashboardService drilldownService;

	private static final String[] SUPPORTED_DATE_FORMATS = new String[] {
		"yyyy-MM-dd",
		"dd-MM-yyyy",
		"yyyy-MM-dd'T'HH:mm:ss.SSSX",
		"yyyy-MM-dd'T'HH:mm:ss.SSSZ",
		"yyyy-MM-dd'T'HH:mm:ss",
		"yyyy-MM-dd HH:mm:ss",
		"dd-MM-yyyy HH:mm:ss",
		"yyyy/MM/dd",
		"dd/MM/yyyy"
	};

	private Date parseDate(String text) {
		if (text == null || text.trim().isEmpty()) {
			return null;
		}
		String s = text.trim();
		for (String fmt : SUPPORTED_DATE_FORMATS) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(fmt);
				sdf.setLenient(false);
				return sdf.parse(s);
			} catch (ParseException ignored) {
			}
		}
		try {
			long millis = Long.parseLong(s);
			return new Date(millis);
		} catch (NumberFormatException ignored) {
		}
		throw new IllegalArgumentException("Invalid date format: '" + text + "'. Supported formats: yyyy-MM-dd, dd-MM-yyyy, ISO 8601");
	}

	private Date parseFromDate(String text) {
		Date d = parseDate(text);
		if (d == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	private Date parseToDate(String text) {
		Date d = parseDate(text);
		if (d == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	@GetMapping({"/getOverview", "/overview"})
	public ResponseEntity<AteDrilldownOverviewDto> getOverview(
			@RequestParam(required = true) Integer productId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = false) Boolean isQa) {
		Date from = parseFromDate(fromDate);
		Date to = parseToDate(toDate);
		AteDrilldownOverviewDto dto = drilldownService.getOverview(productId, from, to, isQa);
		return ResponseEntity.ok(dto);
	}

	@GetMapping({"/getFailuresByTest", "/failures-by-test"})
	public ResponseEntity<List<AteTestFailureGroupDto>> getFailuresByTest(
			@RequestParam(required = true) Integer productId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = false) Boolean isQa) {
		Date from = parseFromDate(fromDate);
		Date to = parseToDate(toDate);
		List<AteTestFailureGroupDto> list = drilldownService.getFailuresGroupedByTest(productId, from, to, isQa);
		return ResponseEntity.ok(list);
	}

	@GetMapping({"/getFailedDevicesForTest", "/failed-devices"})
	public ResponseEntity<List<AteFailedDeviceDetailDto>> getFailedDevicesForTest(
			@RequestParam(required = true) Integer productId,
			@RequestParam(required = true) Integer testId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = false) Boolean isQa) {
		Date from = parseFromDate(fromDate);
		Date to = parseToDate(toDate);
		List<AteFailedDeviceDetailDto> list = drilldownService.getFailedDevicesForTest(productId, testId, from, to, isQa);
		return ResponseEntity.ok(list);
	}

	@GetMapping({"/getFailedParameters", "/parameter-root-cause", "/failed-parameters"})
	public ResponseEntity<List<AteFailedParameterDto>> getFailedParameters(
			@RequestParam(required = true) Integer productId,
			@RequestParam(required = false) Integer testId,
			@RequestParam(required = false) Integer inspRoundId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = false) Boolean isQa) {
		Date from = parseFromDate(fromDate);
		Date to = parseToDate(toDate);
		List<AteFailedParameterDto> list = drilldownService.getFailedParametersDetail(productId, testId, inspRoundId, from, to, isQa);
		return ResponseEntity.ok(list);
	}

	@GetMapping({"/getSpecTrend", "/spec-trend"})
	public ResponseEntity<AteSpecTrendGraphDto> getSpecTrend(
			@RequestParam(required = true) Integer specId,
			@RequestParam(required = false) Integer productId,
			@RequestParam(required = false) Integer testId,
			@RequestParam(required = true) String fromDate,
			@RequestParam(required = true) String toDate,
			@RequestParam(required = false) Boolean isQa) {
		Date from = parseFromDate(fromDate);
		Date to = parseToDate(toDate);
		AteSpecTrendGraphDto dto = drilldownService.getSpecTrendGraph(specId, productId, testId, from, to, isQa);
		return ResponseEntity.ok(dto);
	}
}
