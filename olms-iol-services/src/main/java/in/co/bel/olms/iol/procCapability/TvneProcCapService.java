package in.co.bel.olms.iol.procCapability;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.constants.StringLiteralConstants;
import in.co.bel.olms.iol.dto.TvneFieldsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TvneProcCapService {

	private static final Logger logger = LoggerFactory.getLogger(TvneProcCapService.class);

	private final ProcessCapabilityService processCapService;
	
	public double calculateCpkColdCur(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 3.0;
		double lsl = 2.0;

		double cpks = processCapService.calculateCpkVolt(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;

	}
	
	public double calculateCpColdCur(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 3.0;
		double lsl = 2.0;

		double cp = processCapService.calculateCpVolt(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}
	
	public double calculateCpkColdCurPrism(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 1.50;
		double lsl = 1.00;

		double cpks = processCapService.calculateCpkVolt(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;

	}
	
	public double calculateCpColdCurPrism(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 1.50;
		double lsl = 1.00;

		double cp = processCapService.calculateCpVolt(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}
	
	
	public double calculateCpkColdCurAuto(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 150;
		double lsl = 0;

		double cpks = processCapService.calculateCpkVolt(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;

	}
	
	public double calculateCpColdCurAuto(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 150;
		double lsl = 0;

		double cp = processCapService.calculateCpVolt(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}
	
	public double calculateCpkLeakTest(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 0.20;
		double lsl = 0;

		double cpks = processCapService.calculateCpkVolt(data, usl, lsl);
		cpks = round(cpks, 2);
		return cpks;

	}
	
	public double calculateCpLeakTest(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null || coldCurDtos.isEmpty()) {
			throw new IllegalArgumentException(StringLiteralConstants.NULL_VOLT_DATA_MESSAGE);
		}
		double[] data = convertColdCurDtosToArray(coldCurDtos);
		double usl = 0.20;
		double lsl = 0;

		double cp = processCapService.calculateCpVolt(data, usl, lsl);
		cp = round(cp, 2);
		return cp;

	}
	
	
	private double[] convertColdCurDtosToArray(List<TvneFieldsDto> coldCurDtos) {
		if (coldCurDtos == null) {
			return new double[0];
		}
		try {
			return coldCurDtos.stream().filter(coldCurDto -> coldCurDto != null).mapToDouble(TvneFieldsDto::getValue).toArray();
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

}
