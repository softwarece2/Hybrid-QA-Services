package in.co.bel.olms.iol.ate.dto;


import java.util.Date;

import lombok.Data;

@Data
public class SubmittedTestsDto {

	private Integer prodId;
	private Integer testId;
	private String submittedBy;
	private Date submittedDate;
	private String submitDate;
	private String testStatus;
	private Integer sequence;
	

}
