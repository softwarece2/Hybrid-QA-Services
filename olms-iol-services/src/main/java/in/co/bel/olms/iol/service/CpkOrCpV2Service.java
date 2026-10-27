package in.co.bel.olms.iol.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.ate.dto.AteGraphDataDto;
import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.procCapability.ProcessCapabilityServiceV2;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CpkOrCpV2Service {
	
	private static final Logger logger = LoggerFactory.getLogger(CpkOrCpV2Service.class);	

	private final ProcessCapabilityServiceV2 processCapServiceV2;

	private double[] convertVoltDtosToArray(List<AteGraphDataDto> dtos) {
		if (dtos == null) {
			return new double[0];
		}
		try {
			return dtos.stream().filter(dto -> dto != null).mapToDouble(AteGraphDataDto::getValue).toArray();
		} catch (Exception e) {
			return new double[0];
		}
	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public Double calculateCpkValue(List<AteGraphDataDto> dtos, Double min, Double max) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		Double usl = max;
		Double lsl = min;

		double[] data = convertVoltDtosToArray(dtos);
		
		double cpks = processCapServiceV2.calculateCpkValue(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;
	}
	public double calculateCpValue(List<AteGraphDataDto> dtos, Double min, Double max) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertVoltDtosToArray(dtos);
		double usl = max;
		double lsl = min;

		double cp = processCapServiceV2.calculateCpValue(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}
	
	public double calculateMean(List<AteGraphDataDto> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(dtos);
		return processCapServiceV2.calculateMean(data);
	}
	
	public double calculateStandardDeviation(List<AteGraphDataDto> dtos, Double mean) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(dtos);
		return processCapServiceV2.calculateStandardDeviation(data, mean);
	}
	
	public double calculateMedian(List<AteGraphDataDto> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(dtos);
		return processCapServiceV2.calculateMedian(data);
	}
	private BigDecimal[] convertDtosToBIgDecArray(List<AteGraphDataDto> dtos) {
		if (dtos == null) {
			return new BigDecimal[0];
		}
		try {
			logger.debug("Value 1: {} " , dtos.get(0).getValue2());
			logger.debug("Value 2: {} " , dtos.get(1).getValue2());
			logger.debug("Value 3: {} " , dtos.get(2).getValue2());
			return dtos.stream().map(AteGraphDataDto :: getValue2).filter(Objects :: nonNull).toArray(BigDecimal[]:: new);
		} catch (Exception e) {
			return new BigDecimal[0];
		}
	}
	public BigDecimal calculateBigDecCpkValue(List<AteGraphDataDto> dtos, BigDecimal min, BigDecimal max) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		BigDecimal usl = max;
		BigDecimal lsl = min;

		BigDecimal[] data = convertDtosToBIgDecArray(dtos);
		
		BigDecimal cpks = processCapServiceV2.calculateBigDecCpkValue(data, usl, lsl);
//		cpks = round(cpks, 2);
		return cpks.setScale(5,RoundingMode.HALF_UP);
	}
	
	public BigDecimal calculateBigDecCpValue(List<AteGraphDataDto> dtos, BigDecimal min, BigDecimal max) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		BigDecimal[] data = convertDtosToBIgDecArray(dtos);
		BigDecimal usl = max;
		BigDecimal lsl = min;

		BigDecimal cp = processCapServiceV2.calculateBigDecCpValue(data, usl, lsl);
//		cp = round(cp, 2);
		return cp.setScale(5,RoundingMode.HALF_UP);

	}
	
	public BigDecimal calculateBigDecMean(List<AteGraphDataDto> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		BigDecimal[] data = convertDtosToBIgDecArray(dtos);
		return processCapServiceV2.calculateBigDecimalMean(data);
	}
	
	public BigDecimal calculateBigDecStandardDeviation(List<AteGraphDataDto> dtos, BigDecimal mean) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		BigDecimal[] data = convertDtosToBIgDecArray(dtos);
		return processCapServiceV2.calculateBigDecStandardDeviation(data, mean);
	}
	
	public BigDecimal calculateBigDecMedian(List<AteGraphDataDto> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		BigDecimal[] data = convertDtosToBIgDecArray(dtos);
		return processCapServiceV2.calculateBigDecMedian(data);
	}

}
