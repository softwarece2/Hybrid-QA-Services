package in.co.bel.olms.master.dto;

import java.util.List;

import lombok.Data;

@Data
public class InspPhaseDto {
	private List<InspPhaseAcitivitiesDto> prePhase;
	private List<InspPhaseAcitivitiesDto> inPhase;
	private List<InspPhaseAcitivitiesDto> postPhase;
}
