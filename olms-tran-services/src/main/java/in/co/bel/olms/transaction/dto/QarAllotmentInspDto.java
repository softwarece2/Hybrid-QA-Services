package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class QarAllotmentInspDto {
	private int id;
	private Integer offerLetterId;
	private String offerLetterName;
	private String offerLetterDate;
	private Integer allotedBy;
	private String allotedByName;
	private List<Integer> allotedTo;
	private String allotedToName;
	private String projectName;
	private String section;
	private Boolean isAlloteBtnVisible;
	private String supplyOrderNo;
	private String salesGroup;
	private Boolean isAllotedToEmp;
}
