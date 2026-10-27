package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;
import java.util.List;

public class AteSpecTrendGraphDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer specId;
	private String paramName;
	private String paramCode;
	private Double minLimit;
	private Double maxLimit;
	private String unit;
	private Double mean;
	private Double median;
	private Double stdDev;
	private Double cp;
	private Double cpk;
	private Double ucl;
	private Double lcl;
	private Integer totalCount;
	private Integer passCount;
	private Integer failCount;
	private List<AteTrendDataPointDto> dataPoints;

	public AteSpecTrendGraphDto() {
	}

	public AteSpecTrendGraphDto(Integer specId, String paramName, String paramCode, Double minLimit, Double maxLimit,
			String unit, Double mean, Double median, Double stdDev, Double cp, Double cpk, Double ucl, Double lcl,
			Integer totalCount, Integer passCount, Integer failCount, List<AteTrendDataPointDto> dataPoints) {
		this.specId = specId;
		this.paramName = paramName;
		this.paramCode = paramCode;
		this.minLimit = minLimit;
		this.maxLimit = maxLimit;
		this.unit = unit;
		this.mean = mean;
		this.median = median;
		this.stdDev = stdDev;
		this.cp = cp;
		this.cpk = cpk;
		this.ucl = ucl;
		this.lcl = lcl;
		this.totalCount = totalCount;
		this.passCount = passCount;
		this.failCount = failCount;
		this.dataPoints = dataPoints;
	}

	public Integer getSpecId() { return specId; }
	public void setSpecId(Integer specId) { this.specId = specId; }

	public String getParamName() { return paramName; }
	public void setParamName(String paramName) { this.paramName = paramName; }

	public String getParamCode() { return paramCode; }
	public void setParamCode(String paramCode) { this.paramCode = paramCode; }

	public Double getMinLimit() { return minLimit; }
	public void setMinLimit(Double minLimit) { this.minLimit = minLimit; }

	public Double getMaxLimit() { return maxLimit; }
	public void setMaxLimit(Double maxLimit) { this.maxLimit = maxLimit; }

	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }

	public Double getMean() { return mean; }
	public void setMean(Double mean) { this.mean = mean; }

	public Double getMedian() { return median; }
	public void setMedian(Double median) { this.median = median; }

	public Double getStdDev() { return stdDev; }
	public void setStdDev(Double stdDev) { this.stdDev = stdDev; }

	public Double getCp() { return cp; }
	public void setCp(Double cp) { this.cp = cp; }

	public Double getCpk() { return cpk; }
	public void setCpk(Double cpk) { this.cpk = cpk; }

	public Double getUcl() { return ucl; }
	public void setUcl(Double ucl) { this.ucl = ucl; }

	public Double getLcl() { return lcl; }
	public void setLcl(Double lcl) { this.lcl = lcl; }

	public Integer getTotalCount() { return totalCount; }
	public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

	public Integer getPassCount() { return passCount; }
	public void setPassCount(Integer passCount) { this.passCount = passCount; }

	public Integer getFailCount() { return failCount; }
	public void setFailCount(Integer failCount) { this.failCount = failCount; }

	public List<AteTrendDataPointDto> getDataPoints() { return dataPoints; }
	public void setDataPoints(List<AteTrendDataPointDto> dataPoints) { this.dataPoints = dataPoints; }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Integer specId;
		private String paramName;
		private String paramCode;
		private Double minLimit;
		private Double maxLimit;
		private String unit;
		private Double mean;
		private Double median;
		private Double stdDev;
		private Double cp;
		private Double cpk;
		private Double ucl;
		private Double lcl;
		private Integer totalCount;
		private Integer passCount;
		private Integer failCount;
		private List<AteTrendDataPointDto> dataPoints;

		public Builder specId(Integer val) { this.specId = val; return this; }
		public Builder paramName(String val) { this.paramName = val; return this; }
		public Builder paramCode(String val) { this.paramCode = val; return this; }
		public Builder minLimit(Double val) { this.minLimit = val; return this; }
		public Builder maxLimit(Double val) { this.maxLimit = val; return this; }
		public Builder unit(String val) { this.unit = val; return this; }
		public Builder mean(Double val) { this.mean = val; return this; }
		public Builder median(Double val) { this.median = val; return this; }
		public Builder stdDev(Double val) { this.stdDev = val; return this; }
		public Builder cp(Double val) { this.cp = val; return this; }
		public Builder cpk(Double val) { this.cpk = val; return this; }
		public Builder ucl(Double val) { this.ucl = val; return this; }
		public Builder lcl(Double val) { this.lcl = val; return this; }
		public Builder totalCount(Integer val) { this.totalCount = val; return this; }
		public Builder passCount(Integer val) { this.passCount = val; return this; }
		public Builder failCount(Integer val) { this.failCount = val; return this; }
		public Builder dataPoints(List<AteTrendDataPointDto> val) { this.dataPoints = val; return this; }

		public AteSpecTrendGraphDto build() {
			return new AteSpecTrendGraphDto(specId, paramName, paramCode, minLimit, maxLimit, unit, mean, median,
					stdDev, cp, cpk, ucl, lcl, totalCount, passCount, failCount, dataPoints);
		}
	}
}
