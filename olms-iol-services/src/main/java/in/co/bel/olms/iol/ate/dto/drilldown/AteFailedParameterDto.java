package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;

public class AteFailedParameterDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer tvne1paId;
	private Integer inspRoundId;
	private String serialNo;
	private String submittedDate;
	private Integer roundId;
	private Boolean isQa;
	private Integer testId;
	private String testName;
	private Integer specId;
	private String paramName;
	private String paramCode;
	private String tyClauseNo;
	private Double minLimit;
	private Double maxLimit;
	private String unit;
	private Integer obsTypeId;
	private String obsValue;
	private String paramStatus;
	private String remarks;
	private String failureReason;

	public AteFailedParameterDto() {
	}

	public AteFailedParameterDto(Integer tvne1paId, Integer inspRoundId, String serialNo, String submittedDate,
			Integer roundId, Boolean isQa, Integer testId, String testName, Integer specId, String paramName,
			String paramCode, String tyClauseNo, Double minLimit, Double maxLimit, String unit, Integer obsTypeId,
			String obsValue, String paramStatus, String remarks, String failureReason) {
		this.tvne1paId = tvne1paId;
		this.inspRoundId = inspRoundId;
		this.serialNo = serialNo;
		this.submittedDate = submittedDate;
		this.roundId = roundId;
		this.isQa = isQa;
		this.testId = testId;
		this.testName = testName;
		this.specId = specId;
		this.paramName = paramName;
		this.paramCode = paramCode;
		this.tyClauseNo = tyClauseNo;
		this.minLimit = minLimit;
		this.maxLimit = maxLimit;
		this.unit = unit;
		this.obsTypeId = obsTypeId;
		this.obsValue = obsValue;
		this.paramStatus = paramStatus;
		this.remarks = remarks;
		this.failureReason = failureReason;
	}

	public Integer getTvne1paId() { return tvne1paId; }
	public void setTvne1paId(Integer tvne1paId) { this.tvne1paId = tvne1paId; }

	public Integer getInspRoundId() { return inspRoundId; }
	public void setInspRoundId(Integer inspRoundId) { this.inspRoundId = inspRoundId; }

	public String getSerialNo() { return serialNo; }
	public void setSerialNo(String serialNo) { this.serialNo = serialNo; }

	public String getSubmittedDate() { return submittedDate; }
	public void setSubmittedDate(String submittedDate) { this.submittedDate = submittedDate; }

	public Integer getRoundId() { return roundId; }
	public void setRoundId(Integer roundId) { this.roundId = roundId; }

	public Boolean getIsQa() { return isQa; }
	public void setIsQa(Boolean isQa) { this.isQa = isQa; }

	public Integer getTestId() { return testId; }
	public void setTestId(Integer testId) { this.testId = testId; }

	public String getTestName() { return testName; }
	public void setTestName(String testName) { this.testName = testName; }

	public Integer getSpecId() { return specId; }
	public void setSpecId(Integer specId) { this.specId = specId; }

	public String getParamName() { return paramName; }
	public void setParamName(String paramName) { this.paramName = paramName; }

	public String getParamCode() { return paramCode; }
	public void setParamCode(String paramCode) { this.paramCode = paramCode; }

	public String getTyClauseNo() { return tyClauseNo; }
	public void setTyClauseNo(String tyClauseNo) { this.tyClauseNo = tyClauseNo; }

	public Double getMinLimit() { return minLimit; }
	public void setMinLimit(Double minLimit) { this.minLimit = minLimit; }

	public Double getMaxLimit() { return maxLimit; }
	public void setMaxLimit(Double maxLimit) { this.maxLimit = maxLimit; }

	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }

	public Integer getObsTypeId() { return obsTypeId; }
	public void setObsTypeId(Integer obsTypeId) { this.obsTypeId = obsTypeId; }

	public String getObsValue() { return obsValue; }
	public void setObsValue(String obsValue) { this.obsValue = obsValue; }

	public String getParamStatus() { return paramStatus; }
	public void setParamStatus(String paramStatus) { this.paramStatus = paramStatus; }

	public String getRemarks() { return remarks; }
	public void setRemarks(String remarks) { this.remarks = remarks; }

	public String getFailureReason() { return failureReason; }
	public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Integer tvne1paId;
		private Integer inspRoundId;
		private String serialNo;
		private String submittedDate;
		private Integer roundId;
		private Boolean isQa;
		private Integer testId;
		private String testName;
		private Integer specId;
		private String paramName;
		private String paramCode;
		private String tyClauseNo;
		private Double minLimit;
		private Double maxLimit;
		private String unit;
		private Integer obsTypeId;
		private String obsValue;
		private String paramStatus;
		private String remarks;
		private String failureReason;

		public Builder tvne1paId(Integer val) { this.tvne1paId = val; return this; }
		public Builder inspRoundId(Integer val) { this.inspRoundId = val; return this; }
		public Builder serialNo(String val) { this.serialNo = val; return this; }
		public Builder submittedDate(String val) { this.submittedDate = val; return this; }
		public Builder roundId(Integer val) { this.roundId = val; return this; }
		public Builder isQa(Boolean val) { this.isQa = val; return this; }
		public Builder testId(Integer val) { this.testId = val; return this; }
		public Builder testName(String val) { this.testName = val; return this; }
		public Builder specId(Integer val) { this.specId = val; return this; }
		public Builder paramName(String val) { this.paramName = val; return this; }
		public Builder paramCode(String val) { this.paramCode = val; return this; }
		public Builder tyClauseNo(String val) { this.tyClauseNo = val; return this; }
		public Builder minLimit(Double val) { this.minLimit = val; return this; }
		public Builder maxLimit(Double val) { this.maxLimit = val; return this; }
		public Builder unit(String val) { this.unit = val; return this; }
		public Builder obsTypeId(Integer val) { this.obsTypeId = val; return this; }
		public Builder obsValue(String val) { this.obsValue = val; return this; }
		public Builder paramStatus(String val) { this.paramStatus = val; return this; }
		public Builder remarks(String val) { this.remarks = val; return this; }
		public Builder failureReason(String val) { this.failureReason = val; return this; }

		public AteFailedParameterDto build() {
			return new AteFailedParameterDto(tvne1paId, inspRoundId, serialNo, submittedDate, roundId, isQa, testId,
					testName, specId, paramName, paramCode, tyClauseNo, minLimit, maxLimit, unit, obsTypeId, obsValue,
					paramStatus, remarks, failureReason);
		}
	}
}
