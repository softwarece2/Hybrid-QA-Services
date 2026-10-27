package in.co.bel.olms.transaction.dto;
import java.util.List;

import in.co.bel.olms.master.dto.EmpRoleDto;
import lombok.Data;

@Data

public class OlAllotmentDto {
	private int id;
	private Integer offerLetterId;
	private String offerLetterName;
	private String offerLetterDate;
	private Integer allotedBy;
	private String allotedByName;
	private Integer allotedTo;
	private String allotedToName;
	private String projectName;
	private String supplyOrderNo;
	private String salesGroup;
	private Boolean isAlloteBtnVisible;
	private Boolean isDeligation;
	private Boolean isAllotedToEmp;
	private String date;
	private String createdDate;

	private String epcode;
	private String materialDescription;
	private String itemCategory;
	private List<EmpRoleDto> empLists;
	
	private String allotedByNameAndStaffNo;
	private String allotedToNameAndStaffNo;

}
