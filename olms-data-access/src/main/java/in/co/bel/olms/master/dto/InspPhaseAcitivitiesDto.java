package in.co.bel.olms.master.dto;

import lombok.Data;

@Data
public class InspPhaseAcitivitiesDto {
	private int id;
	private int inspPhaseId;
	private String inspPhaseName;
	private String activity;
	private int order;
	private String slNo;
}
