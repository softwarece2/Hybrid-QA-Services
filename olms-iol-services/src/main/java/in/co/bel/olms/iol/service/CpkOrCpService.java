package in.co.bel.olms.iol.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.dto.VoltDto;
import in.co.bel.olms.iol.procCapability.ProcessCapabilityService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CpkOrCpService {

	private static final Logger logger = LoggerFactory.getLogger(CpkOrCpService.class);
	
	private final ProcessCapabilityService processCapService;

	private double[] convertVoltDtosToArray(List<VoltDto> voltDtos) {
		if (voltDtos == null) {
			return new double[0];
		}
		try {
			return voltDtos.stream().filter(voltDto -> voltDto != null && voltDto.getValue() != null).mapToDouble(VoltDto::getValue).toArray();
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

	public double calculateCpkVolt(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 28.0;
		double lsl = 26.0;

		double cpks = processCapService.calculateCpkVolt(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;

	}

	public double calculateCpVolt(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 28.0;
		double lsl = 26.0;

		double cp = processCapService.calculateCpVolt(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}

	public Double calculateCpkPhCurrS(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_S_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cpks = processCapService.calculateCpkPhCurrS(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;
	}

	public Double calculateCpPhCurrS(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_S_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cp = processCapService.calculateCpPhCurrS(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}

	public Double calculateCpkPhCurrR(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_R_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cpks = processCapService.calculateCpkPhCurrR(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;
	}

	public Double calculateCpPhCurrR(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_R_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cp = processCapService.calculateCpPhCurrR(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}

	public Double calculateCpkPhCurrT(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cpks = processCapService.calculateCpkPhCurrT(data, usl, lsl);
		cpks = round(cpks, 2);
		
		return cpks;
	}

	public Double calculateCpPhCurrT(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		double usl = 2.0;
		double lsl = -2.0;

		double cp = processCapService.calculateCpPhCurrT(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}

	
	public double calculateMean(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		return processCapService.calculateMean(data);
	}
	
	public double calculateStandardDeviation(List<VoltDto> voltDtos, Double mean) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		return processCapService.calculateStandardDeviation(data, mean);
	}
	
	public double calculateMedian(List<VoltDto> voltDtos) {
		if (voltDtos == null || voltDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.PHASE_T_DATA_NULL);
		}
		double[] data = convertVoltDtosToArray(voltDtos);
		return processCapService.calculateMedian(data);
	}
}
