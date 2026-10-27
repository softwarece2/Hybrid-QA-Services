package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class QarAllotmentDto {
	private int id;
	private Integer offerLetterId;
	private String offerLetterName;
	private String offerLetterDate;
	private Integer allotedBy;
	private String allotedByName;
	private Integer allotedTo;
	private String allotedToName;
	private String projectName;
	private String section;
	private Boolean isAlloteBtnVisible;
	private String supplyOrderNo;
	private String salesGroup;
	private Boolean isAllotedToEmp;
}
