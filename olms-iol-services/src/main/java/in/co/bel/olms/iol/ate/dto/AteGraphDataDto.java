package in.co.bel.olms.iol.ate.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class AteGraphDataDto {
	private String regNol;
	private String obsValue ;
	private String status;
	
	private BigDecimal value2;
	private Double value;
	private BigDecimal cpk;
	private BigDecimal cp;
	private BigDecimal mean;
	private BigDecimal stdDev;
	private BigDecimal median;
	
	
	


}
