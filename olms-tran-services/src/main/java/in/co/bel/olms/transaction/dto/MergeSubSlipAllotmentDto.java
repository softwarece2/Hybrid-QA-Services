package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MergeSubSlipAllotmentDto {
	private int id;
	private Integer mergedOlId;
	private String offerLetterName;
	private String offerLetterDate;
	private Integer allotedBy;
	private String allotedByName;
	private List<Integer> allotedTo;
	private String allotedToName;
	private String projectName;
	private String supplyOrderNo;
	private String salesGroup;
	private Boolean isAlloteBtnVisible;
	private Boolean isDeligation;
	private Boolean isAllotedToEmp;
}
