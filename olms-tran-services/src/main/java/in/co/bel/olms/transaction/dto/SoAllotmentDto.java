package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class SoAllotmentDto {

	private int id;
	private Integer supplyOrderId;
	private String supplyOrderName;
	private String supplyOrderDate;
	private Integer allotedBy;
	private String allotedByName;
	private Integer allotedTo;
	private String allotedToName;
	private Boolean isAlloteBtnVisible;
	private String date;
	private String epcode;
	private String itemCategory;

	private String createdDate;
	private String orgType;
	private String allotedByNameAndStaffNo;
	private String allotedToNameAndStaffNo;
}
