package in.co.bel.olms.ate.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ObsRemDto {

	private Integer specId;
	private String obs;
	private String rem;
	private Double obsValue;
	private String result;
	private Integer resultId;
	private String specVal;
	private Integer testId;
	private String testName;
	private BigDecimal  min;
	private BigDecimal  max;
	
}
