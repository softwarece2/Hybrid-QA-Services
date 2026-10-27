package in.co.bel.olms.iol.ate.dto.drilldown;

import java.io.Serializable;

public class AteDrilldownOverviewDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer totalTestedDevices;
	private Integer passedDevices;
	private Integer failedDevices;
	private Double passRate;
	private Double failRate;
	private Integer totalTestsConducted;

	public AteDrilldownOverviewDto() {
	}

	public AteDrilldownOverviewDto(Integer totalTestedDevices, Integer passedDevices, Integer failedDevices,
			Double passRate, Double failRate, Integer totalTestsConducted) {
		this.totalTestedDevices = totalTestedDevices;
		this.passedDevices = passedDevices;
		this.failedDevices = failedDevices;
		this.passRate = passRate;
		this.failRate = failRate;
		this.totalTestsConducted = totalTestsConducted;
	}

	public Integer getTotalTestedDevices() { return totalTestedDevices; }
	public void setTotalTestedDevices(Integer totalTestedDevices) { this.totalTestedDevices = totalTestedDevices; }

	public Integer getPassedDevices() { return passedDevices; }
	public void setPassedDevices(Integer passedDevices) { this.passedDevices = passedDevices; }

	public Integer getFailedDevices() { return failedDevices; }
	public void setFailedDevices(Integer failedDevices) { this.failedDevices = failedDevices; }

	public Double getPassRate() { return passRate; }
	public void setPassRate(Double passRate) { this.passRate = passRate; }

	public Double getFailRate() { return failRate; }
	public void setFailRate(Double failRate) { this.failRate = failRate; }

	public Integer getTotalTestsConducted() { return totalTestsConducted; }
	public void setTotalTestsConducted(Integer totalTestsConducted) { this.totalTestsConducted = totalTestsConducted; }

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Integer totalTestedDevices;
		private Integer passedDevices;
		private Integer failedDevices;
		private Double passRate;
		private Double failRate;
		private Integer totalTestsConducted;

		public Builder totalTestedDevices(Integer val) { this.totalTestedDevices = val; return this; }
		public Builder passedDevices(Integer val) { this.passedDevices = val; return this; }
		public Builder failedDevices(Integer val) { this.failedDevices = val; return this; }
		public Builder passRate(Double val) { this.passRate = val; return this; }
		public Builder failRate(Double val) { this.failRate = val; return this; }
		public Builder totalTestsConducted(Integer val) { this.totalTestsConducted = val; return this; }

		public AteDrilldownOverviewDto build() {
			return new AteDrilldownOverviewDto(totalTestedDevices, passedDevices, failedDevices, passRate, failRate, totalTestsConducted);
		}
	}
}
