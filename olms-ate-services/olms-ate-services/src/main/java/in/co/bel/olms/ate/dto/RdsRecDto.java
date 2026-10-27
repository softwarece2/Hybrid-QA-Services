package in.co.bel.olms.ate.dto;
import lombok.Data;

@Data
public class RdsRecDto {

	private Integer id;
	private Integer devId;
	private Integer olLineItemId;
	private Integer applicableTestId;
	private String submittedDate; 
	private String devName;
}
