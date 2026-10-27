package in.co.bel.olms.iol.ate.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.repository.SpecRepository;
import in.co.bel.olms.iol.ate.dto.drilldown.AteDrilldownOverviewDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteFailedDeviceDetailDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteFailedParameterDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteSpecTrendGraphDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteTestFailureGroupDto;
import in.co.bel.olms.iol.ate.dto.drilldown.AteTrendDataPointDto;
import in.co.bel.olms.iol.ate.repository.Tvne1paDrilldownRepository;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AteDrilldownDashboardService {

	private static final Logger logger = LoggerFactory.getLogger(AteDrilldownDashboardService.class);

	private final Tvne1paDrilldownRepository tvne1paDrilldownRepo;
	private final SpecRepository specRepository;

	private SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
	}

	public AteDrilldownOverviewDto getOverview(Integer productId, Date fromDate, Date toDate, Boolean isQa) {
		List<Object[]> results = tvne1paDrilldownRepo.getOverallDeviceSummary(productId, fromDate, toDate, isQa);
		int total = 0;
		int passed = 0;
		int failed = 0;

		if (results != null && !results.isEmpty()) {
			Object[] row = results.get(0);
			total = row[0] != null ? ((Number) row[0]).intValue() : 0;
			passed = row[1] != null ? ((Number) row[1]).intValue() : 0;
			failed = row[2] != null ? ((Number) row[2]).intValue() : 0;
		}

		double passRate = total > 0 ? round((passed * 100.0) / total, 2) : 0.0;
		double failRate = total > 0 ? round((failed * 100.0) / total, 2) : 0.0;

		return AteDrilldownOverviewDto.builder()
				.totalTestedDevices(total)
				.passedDevices(passed)
				.failedDevices(failed)
				.passRate(passRate)
				.failRate(failRate)
				.build();
	}

	public List<AteTestFailureGroupDto> getFailuresGroupedByTest(Integer productId, Date fromDate, Date toDate, Boolean isQa) {
		List<Object[]> rows = tvne1paDrilldownRepo.getFailuresGroupedByTest(productId, fromDate, toDate, isQa);
		List<AteTestFailureGroupDto> list = new ArrayList<>();

		if (rows != null) {
			for (Object[] row : rows) {
				Integer testId = row[0] != null ? ((Number) row[0]).intValue() : null;
				String testName = row[1] != null ? row[1].toString() : "Unknown Test";
				int totalDevices = row[2] != null ? ((Number) row[2]).intValue() : 0;
				int failedCount = row[3] != null ? ((Number) row[3]).intValue() : 0;
				int passedCount = row[4] != null ? ((Number) row[4]).intValue() : 0;

				double failRate = totalDevices > 0 ? round((failedCount * 100.0) / totalDevices, 2) : 0.0;
				double passRate = totalDevices > 0 ? round((passedCount * 100.0) / totalDevices, 2) : 0.0;

				list.add(AteTestFailureGroupDto.builder()
						.testId(testId)
						.testName(testName)
						.totalTestedDevices(totalDevices)
						.failedDevicesCount(failedCount)
						.passedDevicesCount(passedCount)
						.failRate(failRate)
						.passRate(passRate)
						.build());
			}
		}
		return list;
	}

	public List<AteFailedDeviceDetailDto> getFailedDevicesForTest(Integer productId, Integer testId, Date fromDate, Date toDate, Boolean isQa) {
		List<Object[]> devRows = tvne1paDrilldownRepo.getFailedDevicesForTest(productId, testId, fromDate, toDate, isQa);
		List<AteFailedParameterDto> allFailedParams = getFailedParametersDetail(productId, testId, null, fromDate, toDate, isQa);

		Map<Integer, List<AteFailedParameterDto>> paramsByRoundMap = new HashMap<>();
		for (AteFailedParameterDto p : allFailedParams) {
			if (p.getInspRoundId() != null) {
				paramsByRoundMap.computeIfAbsent(p.getInspRoundId(), k -> new ArrayList<>()).add(p);
			}
		}

		SimpleDateFormat sdf = getDateFormat();
		List<AteFailedDeviceDetailDto> devices = new ArrayList<>();

		if (devRows != null) {
			for (Object[] row : devRows) {
				Integer inspRoundId = row[0] != null ? ((Number) row[0]).intValue() : null;
				String serialNo = row[1] != null ? row[1].toString() : "N/A";
				String dateStr = null;
				if (row[2] != null) {
					dateStr = row[2] instanceof Date ? sdf.format((Date) row[2]) : row[2].toString();
				}
				Integer roundId = row[3] != null ? ((Number) row[3]).intValue() : null;
				Boolean qaFlag = row[4] != null ? (Boolean) row[4] : false;
				String roundStatus = row[5] != null ? row[5].toString() : "Fail";
				String submittedBy = row[6] != null ? row[6].toString() : "";
				int failedCount = row[7] != null ? ((Number) row[7]).intValue() : 0;

				List<AteFailedParameterDto> matchedParams = paramsByRoundMap.getOrDefault(inspRoundId, Collections.emptyList());

				devices.add(AteFailedDeviceDetailDto.builder()
						.inspRoundId(inspRoundId)
						.serialNo(serialNo)
						.submittedDate(dateStr)
						.roundId(roundId)
						.isQa(qaFlag)
						.roundStatus(roundStatus)
						.submittedByName(submittedBy)
						.failedParamCount(failedCount > 0 ? failedCount : matchedParams.size())
						.failedParameters(matchedParams)
						.build());
			}
		}
		return devices;
	}

	public List<AteFailedParameterDto> getFailedParametersDetail(Integer productId, Integer testId, Integer inspRoundId, Date fromDate, Date toDate, Boolean isQa) {
		List<Object[]> rows = tvne1paDrilldownRepo.getFailedParametersDetail(productId, testId, inspRoundId, fromDate, toDate, isQa);
		List<AteFailedParameterDto> list = new ArrayList<>();
		SimpleDateFormat sdf = getDateFormat();

		if (rows != null) {
			for (Object[] row : rows) {
				Integer tvneId = row[0] != null ? ((Number) row[0]).intValue() : null;
				Integer roundIdVal = row[1] != null ? ((Number) row[1]).intValue() : null;
				String serialNo = row[2] != null ? row[2].toString() : "N/A";
				String dateStr = null;
				if (row[3] != null) {
					dateStr = row[3] instanceof Date ? sdf.format((Date) row[3]) : row[3].toString();
				}
				Integer roundNo = row[4] != null ? ((Number) row[4]).intValue() : null;
				Boolean qa = row[5] != null ? (Boolean) row[5] : false;
				Integer tId = row[6] != null ? ((Number) row[6]).intValue() : null;
				String tName = row[7] != null ? row[7].toString() : "";
				Integer sId = row[8] != null ? ((Number) row[8]).intValue() : null;
				String pName = row[9] != null ? row[9].toString() : "";
				String pCode = row[10] != null ? row[10].toString() : "";
				String clause = row[11] != null ? row[11].toString() : "";
				Double minVal = row[12] != null ? ((Number) row[12]).doubleValue() : null;
				Double maxVal = row[13] != null ? ((Number) row[13]).doubleValue() : null;
				String unit = row[14] != null ? row[14].toString() : "";
				Integer obsTypeId = row[15] != null ? ((Number) row[15]).intValue() : null;
				String obsVal = row[16] != null ? row[16].toString() : "";
				String pStatus = row[17] != null ? row[17].toString() : "Fail";
				String remarks = row[18] != null ? row[18].toString() : "";

				String failureReason = buildFailureReason(obsVal, minVal, maxVal, unit);

				list.add(AteFailedParameterDto.builder()
						.tvne1paId(tvneId)
						.inspRoundId(roundIdVal)
						.serialNo(serialNo)
						.submittedDate(dateStr)
						.roundId(roundNo)
						.isQa(qa)
						.testId(tId)
						.testName(tName)
						.specId(sId)
						.paramName(pName)
						.paramCode(pCode)
						.tyClauseNo(clause)
						.minLimit(minVal)
						.maxLimit(maxVal)
						.unit(unit)
						.obsTypeId(obsTypeId)
						.obsValue(obsVal)
						.paramStatus(pStatus)
						.remarks(remarks)
						.failureReason(failureReason)
						.build());
			}
		}
		return list;
	}

	public AteSpecTrendGraphDto getSpecTrendGraph(Integer specId, Integer productId, Integer testId, Date fromDate, Date toDate, Boolean isQa) {
		Optional<Spec> specOpt = specRepository.findById(specId);
		Double min = null;
		Double max = null;
		String paramName = "";
		String paramCode = "";
		String unit = "";

		if (specOpt.isPresent()) {
			Spec sp = specOpt.get();
			min = sp.getMin() != null ? sp.getMin().doubleValue() : null;
			max = sp.getMax() != null ? sp.getMax().doubleValue() : null;
			paramName = sp.getSpecVal() != null ? sp.getSpecVal() : "";
			paramCode = sp.getParamCode() != null ? sp.getParamCode() : "";
			unit = sp.getUnits() != null ? sp.getUnits().getUints() : "";
		}

		List<Object[]> rows = tvne1paDrilldownRepo.getSpecTrendGraphData(specId, productId, testId, fromDate, toDate, isQa);
		List<AteTrendDataPointDto> points = new ArrayList<>();
		List<Double> numericVals = new ArrayList<>();
		SimpleDateFormat sdf = getDateFormat();
		int passCount = 0;
		int failCount = 0;

		if (rows != null) {
			for (Object[] r : rows) {
				String serialNo = r[0] != null ? r[0].toString() : "N/A";
				String rawVal = r[1] != null ? r[1].toString() : "";
				String status = r[2] != null ? r[2].toString() : "";
				Date subDate = r[4] != null ? (Date) r[4] : null;
				Boolean qa = r[5] != null ? (Boolean) r[5] : false;
				Integer roundId = r[6] != null ? ((Number) r[6]).intValue() : null;

				Double numVal = null;
				if (rawVal != null && !rawVal.trim().isEmpty()) {
					try {
						numVal = Double.parseDouble(rawVal.trim());
						numericVals.add(numVal);
					} catch (NumberFormatException ignored) {
					}
				}

				if ("Pass".equalsIgnoreCase(status) || "1".equals(status)) {
					passCount++;
				} else if ("Fail".equalsIgnoreCase(status) || "0".equals(status)) {
					failCount++;
				}

				points.add(AteTrendDataPointDto.builder()
						.serialNo(serialNo)
						.value(numVal)
						.rawObsValue(rawVal)
						.status(status)
						.submittedDate(subDate != null ? sdf.format(subDate) : "")
						.isQa(qa)
						.roundId(roundId)
						.build());
			}
		}

		Double mean = null;
		Double stdDev = null;
		Double median = null;
		Double cp = null;
		Double cpk = null;
		Double ucl = null;
		Double lcl = null;

		if (!numericVals.isEmpty()) {
			double[] arr = numericVals.stream().mapToDouble(Double::doubleValue).toArray();
			mean = round(calcMean(arr), 4);
			stdDev = round(calcStdDev(arr, mean), 4);
			median = round(calcMedian(arr), 4);

			if (stdDev > 0) {
				ucl = round(mean + (3 * stdDev), 4);
				lcl = round(mean - (3 * stdDev), 4);

				if (min != null && max != null && max > min) {
					cp = round((max - min) / (6 * stdDev), 4);
					double cpu = (max - mean) / (3 * stdDev);
					double cpl = (mean - min) / (3 * stdDev);
					cpk = round(Math.min(cpu, cpl), 4);
				}
			}
		}

		return AteSpecTrendGraphDto.builder()
				.specId(specId)
				.paramName(paramName)
				.paramCode(paramCode)
				.minLimit(min)
				.maxLimit(max)
				.unit(unit)
				.mean(mean)
				.median(median)
				.stdDev(stdDev)
				.cp(cp)
				.cpk(cpk)
				.ucl(ucl)
				.lcl(lcl)
				.totalCount(points.size())
				.passCount(passCount)
				.failCount(failCount)
				.dataPoints(points)
				.build();
	}

	private String buildFailureReason(String obsVal, Double min, Double max, String unit) {
		String u = unit != null && !unit.isEmpty() ? " " + unit : "";
		if (obsVal == null || obsVal.trim().isEmpty()) {
			return "No observation value recorded";
		}
		try {
			double val = Double.parseDouble(obsVal.trim());
			if (max != null && val > max) {
				return String.format("Observed value %s exceeds maximum limit of %s%s", obsVal, max, u);
			}
			if (min != null && val < min) {
				return String.format("Observed value %s is below minimum limit of %s%s", obsVal, min, u);
			}
		} catch (NumberFormatException ignored) {
		}
		return "Non-conformance: Result marked as Fail";
	}

	private double calcMean(double[] data) {
		if (data.length == 0) return 0.0;
		double sum = 0.0;
		for (double d : data) sum += d;
		return sum / data.length;
	}

	private double calcStdDev(double[] data, double mean) {
		if (data.length < 2) return 0.0;
		double sum = 0.0;
		for (double d : data) sum += Math.pow(d - mean, 2);
		return Math.sqrt(sum / (data.length - 1));
	}

	private double calcMedian(double[] data) {
		if (data.length == 0) return 0.0;
		double[] copy = Arrays.copyOf(data, data.length);
		Arrays.sort(copy);
		int n = copy.length;
		if (n % 2 != 0) {
			return copy[n / 2];
		}
		return (copy[(n / 2) - 1] + copy[n / 2]) / 2.0;
	}

	private double round(double value, int places) {
		if (Double.isNaN(value) || Double.isInfinite(value)) return 0.0;
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
