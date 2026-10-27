package in.co.bel.olms.iol.reports.dto;

import lombok.Data;

@Data
public class ReportRequest {

	private Integer productTypeId;
	private Integer testTypeId;
	private String fromDate;
	private String toDate;
	
}
