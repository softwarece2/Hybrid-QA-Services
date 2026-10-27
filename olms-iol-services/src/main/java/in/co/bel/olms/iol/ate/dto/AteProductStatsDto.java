package in.co.bel.olms.iol.ate.dto;

import lombok.Data;

@Data
public class AteProductStatsDto {
	
	private Integer testId;
	private String testName;
	private Integer total;
	private Integer passed;
	private Integer failed;
	
}