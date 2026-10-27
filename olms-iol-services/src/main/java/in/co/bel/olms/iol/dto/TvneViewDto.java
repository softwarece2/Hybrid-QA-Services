package in.co.bel.olms.iol.dto;

import lombok.Data;

@Data
public class TvneViewDto {

	private Integer id;
	private String devName;
	private Boolean allSubmitted;
	private String leakSubmittedDate;
	private String vibrSubmittedDate;
	private String impactSubmittedDate;
	private String heatSubmittedDate;
	private String coldSubmittedDate;
}
