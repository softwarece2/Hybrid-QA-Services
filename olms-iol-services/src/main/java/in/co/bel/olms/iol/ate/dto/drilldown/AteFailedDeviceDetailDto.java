package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;
import java.util.List;

public class AteFailedDeviceDetailDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer inspRoundId;
	private String serialNo;
	private String submittedDate;
	private Integer roundId;
	private Boolean isQa;
	private String roundStatus;
	private String submittedByName;
	private Integer failedParamCount;
	private List<AteFailedParameterDto> failedParameters;

	public AteFailedDeviceDetailDto() {
	}

	public AteFailedDeviceDetailDto(Integer inspRoundId, String serialNo, String submittedDate, Integer roundId,
			Boolean isQa, String roundStatus, String submittedByName, Integer failedParamCount,
			List<AteFailedParameterDto> failedParameters) {
		this.inspRoundId = inspRoundId;
		this.serialNo = serialNo;
		this.submittedDate = submittedDate;
		this.roundId = roundId;
		this.isQa = isQa;
		this.roundStatus = roundStatus;
		this.submittedByName = submittedByName;
		this.failedParamCount = failedParamCount;
		this.failedParameters = failedParameters;
	}

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

	public String getRoundStatus() { return roundStatus; }
	public void setRoundStatus(String roundStatus) { this.roundStatus = roundStatus; }

	public String getSubmittedByName() { return submittedByName; }
	public void setSubmittedByName(String submittedByName) { this.submittedByName = submittedByName; }

	public Integer getFailedParamCount() { return failedParamCount; }
	public void setFailedParamCount(Integer failedParamCount) { this.failedParamCount = failedParamCount; }

	public List<AteFailedParameterDto> getFailedParameters() { return failedParameters; }
	public void setFailedParameters(List<AteFailedParameterDto> failedParameters) { this.failedParameters = failedParameters; }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Integer inspRoundId;
		private String serialNo;
		private String submittedDate;
		private Integer roundId;
		private Boolean isQa;
		private String roundStatus;
		private String submittedByName;
		private Integer failedParamCount;
		private List<AteFailedParameterDto> failedParameters;

		public Builder inspRoundId(Integer val) { this.inspRoundId = val; return this; }
		public Builder serialNo(String val) { this.serialNo = val; return this; }
		public Builder submittedDate(String val) { this.submittedDate = val; return this; }
		public Builder roundId(Integer val) { this.roundId = val; return this; }
		public Builder isQa(Boolean val) { this.isQa = val; return this; }
		public Builder roundStatus(String val) { this.roundStatus = val; return this; }
		public Builder submittedByName(String val) { this.submittedByName = val; return this; }
		public Builder failedParamCount(Integer val) { this.failedParamCount = val; return this; }
		public Builder failedParameters(List<AteFailedParameterDto> val) { this.failedParameters = val; return this; }

		public AteFailedDeviceDetailDto build() {
			return new AteFailedDeviceDetailDto(inspRoundId, serialNo, submittedDate, roundId, isQa, roundStatus,
					submittedByName, failedParamCount, failedParameters);
		}
	}
}
