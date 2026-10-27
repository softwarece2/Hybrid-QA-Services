package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class LotAllotmentDto {

	private int id;
	private Integer mergeSubSlipId;
	private Integer allotedById;

	private String allotedToName;
	private String allotedByName;

	private Boolean isAllotedToEmp;
	private String createdAt;
	private Integer allotedToId;
	
	private String allotedByNameAndStaffNo;
	private String allotedToNameAndStaffNo;

}
