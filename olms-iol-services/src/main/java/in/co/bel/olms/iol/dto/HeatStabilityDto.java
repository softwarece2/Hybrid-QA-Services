package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HeatStabilityDto {

	private Integer id;
	private Integer heatMovSmooth;
	private Integer heatMovOpen;
	private Integer heatLeak;
	private String heatMovRem;
	private String heatLeakRem;
	private Integer tvneId;
	private String submittedDate;
	private Date createdDatetime;
	
}
