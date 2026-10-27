package in.co.bel.olms.iol.procCapability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class ProcessCapabilityServiceV2 {

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
		double sum = 0.0;
		for (double num : data) {
			sum += Math.pow(num - mean, 2);
		}
		return Math.sqrt(sum / (data.length - 1));
	}
    
	public double calculateMedian(double[] data) {
		double[] sortedArr = Arrays.copyOf(data, data.length); 
	        Arrays.sort(sortedArr); 
	        int n = sortedArr.length;
	       
	        int value = n % 2;
	       
	        if(value!=0){
	            return sortedArr[n/2];
	        }
	        if(value == 0) {
	        	return 0;
	        }
	        else{
	            return (sortedArr[(n/2)-1]+sortedArr[(n/2)])/2.0;
	        }
	}
	
	public BigDecimal calculateBigDecimalMean(BigDecimal[] data) {
		BigDecimal sum = BigDecimal.ZERO;
		int count = 0;
		for (BigDecimal value : data) {
			if(value != null) {
				sum = sum.add(value);
				count++;
			}
			
		}
		return count == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count),10,RoundingMode.HALF_UP);
	}

	public BigDecimal calculateBigDecStandardDeviation(BigDecimal[] data, BigDecimal mean) {
		 if (data == null || mean == null) {
		        return BigDecimal.ZERO;
		    }
		BigDecimal sum = BigDecimal.ZERO;
		int count = 0;
		for (BigDecimal num : data) {
			if(num != null) {
				BigDecimal diff = num.subtract(mean);
				BigDecimal sqrDiff = diff.multiply(diff);
				sum = sum.add(sqrDiff);
				count++;
			}
		}
		 if (count < 2) {
		        return BigDecimal.ZERO;
		    }
		 BigDecimal variance = sum.divide(
		            BigDecimal.valueOf(count - 1),
		            10,
		            RoundingMode.HALF_UP
		    );

		    return variance.sqrt(
		            new MathContext(10, RoundingMode.HALF_UP)
		    );
	}
    
	public BigDecimal calculateBigDecMedian(BigDecimal[] data) {
		if(data == null || data.length ==0) {
			
			return BigDecimal.ZERO;
		}
		BigDecimal[] sortedArr = Arrays.copyOf(data, data.length); 
	        Arrays.sort(sortedArr); 
	        int n = sortedArr.length;
	       
	        int value = n % 2;
	       
	        if(value!=0){
	            return sortedArr[n/2];
	        }
	        else{
	        	BigDecimal sum = sortedArr[(n/2)-1].add(sortedArr[n/2]);
	        	return sum.divide(BigDecimal.valueOf(2),10,RoundingMode.HALF_UP);
//	        	BigDecimal medianvalue= (sorted_arr[(n/2)-1]+sorted_arr[(n/2)])/2.0;
//	            return medianvalue;
	        }
	}
	public double calculateCpkValue(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);

		double cpu = (usl - mean) / (3 * stdDev);
		double cpl = (mean - lsl) / (3 * stdDev);

		return Math.min(cpu, cpl);
	}

	public double calculateCpValue(double[] data, double usl, double lsl) {
		double mean = calculateMean(data);
		double stdDev = calculateStandardDeviation(data, mean);

		return (usl - lsl) / (6 * stdDev);

	}
	
	public BigDecimal calculateBigDecCpkValue(BigDecimal[] data, BigDecimal usl, BigDecimal lsl) {
		BigDecimal mean = calculateBigDecimalMean(data);
		BigDecimal stdDev = calculateBigDecStandardDeviation(data, mean);
       
		if(stdDev == null || stdDev.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal cpu = usl.subtract(mean).divide(BigDecimal.valueOf(3).multiply(stdDev),10,RoundingMode.HALF_UP);
		BigDecimal cpl = mean.subtract(lsl).divide(BigDecimal.valueOf(3).multiply(stdDev),10,RoundingMode.HALF_UP);

		return cpu.min(cpl);
	}

	public BigDecimal calculateBigDecCpValue(BigDecimal[] data, BigDecimal usl, BigDecimal lsl) {
		
		 if (data == null || data.length == 0 || usl == null || lsl == null) {
		        return BigDecimal.ZERO;
		    }
		 
		BigDecimal mean = calculateBigDecimalMean(data);
		BigDecimal stdDev = calculateBigDecStandardDeviation(data, mean);
		
		 // Prevent null or zero divisor
	    if (stdDev == null || stdDev.compareTo(BigDecimal.ZERO) == 0) {
	        return BigDecimal.ZERO;
	    }
	    
	    BigDecimal denominator = BigDecimal.valueOf(6).multiply(stdDev);

	    if (denominator.compareTo(BigDecimal.ZERO) == 0) {
	        return BigDecimal.ZERO;
	    }

	    return usl.subtract(lsl)
	            .divide(denominator, 10, RoundingMode.HALF_UP);

	}
}
