package in.co.bel.olms.iol.dto;

import lombok.Data;

@Data
public class TvneDto {
	private Integer id;
	private String devName;
	private Boolean allSubmitted;
	private Integer olLineItemId;
	private Integer productTypeId;
	private Integer applicableTestId;
	private LeakTestDto leakTestDto;
	private VibrationDto vibrationDto;
	private ImpactDto impactDto;
	private HeatStabilityDto heatStabilityDto;
	private ColdResistanceDto coldResistanceDto;
	private Integer inspStageId;
}
