package in.co.bel.olms.iol.procCapability;

import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class ProcessCapabilityService {

	public double calculateMean(double[] data) {
		double sum = 0.0;
		int count = 0;
		for (double voltDto : data) {
			sum += voltDto;
			count++;
		}
		return count == 0 ? 0 : sum / count;
	}

	public double calculateStandardDeviation(double[] data, double mean) {
		
	    if (data == null || data.length <= 1) {
	        return 0.0; 
	    }
		double sum = 0.0;
		for (double num : data) {
			sum += Math.pow(num - mean, 2);
		}
		return Math.sqrt(sum / (data.length - 1));
	}
    
	public double calculateMedian(double[] data) {
	    if (data == null || data.length == 0) {
	        return 0.0; // Handle empty array case
	    }

	    double[] sortedArr = Arrays.copyOf(data, data.length);
	    Arrays.sort(sortedArr);
	    int n = sortedArr.length;

	    // Check if n is odd
	    if (n % 2 != 0) {
	        return sortedArr[n / 2];
	    } 
	    // If n is even, calculate the average of the two middle elements
	    else {
	        return (sortedArr[(n / 2) - 1] + sortedArr[n / 2]) / 2.0;
	    }
	}
	
	public double calculateCpkVolt(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		double cpu = (usl - mean) / (3 * stdDev);
		double cpl = (mean - lsl) / (3 * stdDev);

		return Math.min(cpu, cpl);
	}

	public double calculateCpVolt(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		return (usl - lsl) / (6 * stdDev);

	}

	public double calculateCpkPhCurrR(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		double cpu = (usl - mean) / (3 * stdDev);
		double cpl = (mean - lsl) / (3 * stdDev);

		return Math.min(cpu, cpl);
	}

	public double calculateCpPhCurrR(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		return (usl - lsl) / (6 * stdDev);
	}
	
	public double calculateCpkPhCurrS(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		double cpu = (usl - mean) / (3 * stdDev);
		double cpl = (mean - lsl) / (3 * stdDev);

		return Math.min(cpu, cpl);
	}

	public double calculateCpPhCurrS(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		return (usl - lsl) / (6 * stdDev);
	}
	
	public double calculateCpkPhCurrT(double[] data, double usl, double lsl) {
		
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);

		if (stdDev <= 0.0) {
	        return 0.0; 
	    }

		double cpu = (usl - mean) / (3 * stdDev);
		double cpl = (mean - lsl) / (3 * stdDev);
		
		return Math.min(cpu, cpl);
	}

	public double calculateCpPhCurrT(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);
		if (stdDev <= 0.0) {
	        return 0.0; 
	    }
		return (usl - lsl) / (6 * stdDev);
	}
}
