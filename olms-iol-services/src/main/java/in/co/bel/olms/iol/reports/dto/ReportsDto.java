package in.co.bel.olms.iol.reports.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReportsDto {

	private Integer seNo;
	private String devName;
	private String testedDt;
	private String status;
	private String remarks;
	
}
