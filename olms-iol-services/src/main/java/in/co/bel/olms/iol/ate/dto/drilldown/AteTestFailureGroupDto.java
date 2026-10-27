package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;

public class AteTestFailureGroupDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer testId;
	private String testName;
	private Integer totalTestedDevices;
	private Integer failedDevicesCount;
	private Integer passedDevicesCount;
	private Double failRate;
	private Double passRate;

	public AteTestFailureGroupDto() {
	}

	public AteTestFailureGroupDto(Integer testId, String testName, Integer totalTestedDevices,
			Integer failedDevicesCount, Integer passedDevicesCount, Double failRate, Double passRate) {
		this.testId = testId;
		this.testName = testName;
		this.totalTestedDevices = totalTestedDevices;
		this.failedDevicesCount = failedDevicesCount;
		this.passedDevicesCount = passedDevicesCount;
		this.failRate = failRate;
		this.passRate = passRate;
	}

	public Integer getTestId() { return testId; }
	public void setTestId(Integer testId) { this.testId = testId; }

	public String getTestName() { return testName; }
	public void setTestName(String testName) { this.testName = testName; }

	public Integer getTotalTestedDevices() { return totalTestedDevices; }
	public void setTotalTestedDevices(Integer totalTestedDevices) { this.totalTestedDevices = totalTestedDevices; }

	public Integer getFailedDevicesCount() { return failedDevicesCount; }
	public void setFailedDevicesCount(Integer failedDevicesCount) { this.failedDevicesCount = failedDevicesCount; }

	public Integer getPassedDevicesCount() { return passedDevicesCount; }
	public void setPassedDevicesCount(Integer passedDevicesCount) { this.passedDevicesCount = passedDevicesCount; }

	public Double getFailRate() { return failRate; }
	public void setFailRate(Double failRate) { this.failRate = failRate; }

	public Double getPassRate() { return passRate; }
	public void setPassRate(Double passRate) { this.passRate = passRate; }

	public static Builder builder() { return new Builder(); }

	public static class Builder {
		private Integer testId;
		private String testName;
		private Integer totalTestedDevices;
		private Integer failedDevicesCount;
		private Integer passedDevicesCount;
		private Double failRate;
		private Double passRate;

		public Builder testId(Integer val) { this.testId = val; return this; }
		public Builder testName(String val) { this.testName = val; return this; }
		public Builder totalTestedDevices(Integer val) { this.totalTestedDevices = val; return this; }
		public Builder failedDevicesCount(Integer val) { this.failedDevicesCount = val; return this; }
		public Builder passedDevicesCount(Integer val) { this.passedDevicesCount = val; return this; }
		public Builder failRate(Double val) { this.failRate = val; return this; }
		public Builder passRate(Double val) { this.passRate = val; return this; }

		public AteTestFailureGroupDto build() {
			return new AteTestFailureGroupDto(testId, testName, totalTestedDevices, failedDevicesCount, passedDevicesCount, failRate, passRate);
		}
	}
}
