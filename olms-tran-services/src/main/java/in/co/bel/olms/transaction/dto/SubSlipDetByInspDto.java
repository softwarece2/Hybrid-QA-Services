
package in.co.bel.olms.transaction.dto;
//import java.util.Date;
import lombok.Data;
@Data

public class SubSlipDetByInspDto {
	
	private String Grp;
	private String soNumber;
	private String soDate;
	private String olNo;
	private String olDate;

	private String workOrderNo;
	private Integer olId;
	private String epcode;
	private String nomenclature;
	private String inspAuth;

}

