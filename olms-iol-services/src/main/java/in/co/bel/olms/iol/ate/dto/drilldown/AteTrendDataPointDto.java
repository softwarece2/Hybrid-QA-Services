package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;

public class AteTrendDataPointDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String serialNo;
	private Double value;
	private String rawObsValue;
	private String status;
	private String submittedDate;
	private Boolean isQa;
	private Integer roundId;

	public AteTrendDataPointDto() {
	}

	public AteTrendDataPointDto(String serialNo, Double value, String rawObsValue, String status, String submittedDate,
			Boolean isQa, Integer roundId) {
		this.serialNo = serialNo;
		this.value = value;
		this.rawObsValue = rawObsValue;
		this.status = status;
		this.submittedDate = submittedDate;
		this.isQa = isQa;
		this.roundId = roundId;
	}

	public String getSerialNo() { return serialNo; }
	public void setSerialNo(String serialNo) { this.serialNo = serialNo; }

	public Double getValue() { return value; }
	public void setValue(Double value) { this.value = value; }

	public String getRawObsValue() { return rawObsValue; }
	public void setRawObsValue(String rawObsValue) { this.rawObsValue = rawObsValue; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getSubmittedDate() { return submittedDate; }
	public void setSubmittedDate(String submittedDate) { this.submittedDate = submittedDate; }

	public Boolean getIsQa() { return isQa; }
	public void setIsQa(Boolean isQa) { this.isQa = isQa; }

	public Integer getRoundId() { return roundId; }
	public void setRoundId(Integer roundId) { this.roundId = roundId; }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private String serialNo;
		private Double value;
		private String rawObsValue;
		private String status;
		private String submittedDate;
		private Boolean isQa;
		private Integer roundId;

		public Builder serialNo(String val) { this.serialNo = val; return this; }
		public Builder value(Double val) { this.value = val; return this; }
		public Builder rawObsValue(String val) { this.rawObsValue = val; return this; }
		public Builder status(String val) { this.status = val; return this; }
		public Builder submittedDate(String val) { this.submittedDate = val; return this; }
		public Builder isQa(Boolean val) { this.isQa = val; return this; }
		public Builder roundId(Integer val) { this.roundId = val; return this; }

		public AteTrendDataPointDto build() {
			return new AteTrendDataPointDto(serialNo, value, rawObsValue, status, submittedDate, isQa, roundId);
		}
	}
}
