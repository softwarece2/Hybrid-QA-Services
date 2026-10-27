package in.co.bel.olms.transaction.dto;


import lombok.Data;

@Data
public class WorkloadInspActDetDto {

	private Integer id;

	private Integer workLoadId;

	private Integer inspPhaseActId;
	
	private String inspPhaseActivity;
	
	private String slNo;
	
	private Integer inspPhaseId;
	
	private String inspPhase;
	
	private Double estManDays;

	private String planCompletion;

	private Double actualManDays;

	private String actualCompletion;

	private String remarks;
	
	private boolean isBold;

}
