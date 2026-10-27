package in.co.bel.olms.transaction.dto.qar.sqae;

import java.util.Date;

import lombok.Data;

@Data
public class QarSqaePartCDto {
	
	private Integer id;
	private String location;
	private String date;
	private String testRepEncl;
	private String acceptanceDetEncl;
	private String srlNo;
	private String labDetEncl;
	private String reasons;
	private String deficiencesEncl;
	private String dispatchEncl;
	private String sectnOfficerSign;
	private String sectnOfficerName;
	private String sectnOfficerDesig;
	private String dateOfFilling;
	private Date submittedDate;
	private Integer submittedBy;
	private String submittedByStr;

}
