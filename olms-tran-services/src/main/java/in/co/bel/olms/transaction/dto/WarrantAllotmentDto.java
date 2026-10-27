package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class WarrantAllotmentDto {
		private int id;
		private Integer warrantId;
		private String warrantName;
		private Integer allotedBy;
		private String allotedByName;
		private Integer allotedTo;
		private String allotedToName;
		private Boolean isAlloteBtnVisible;
		private Boolean isDeligation;
		private Boolean isAllotedToEmp;
		private String createdAt;
		private String date;
		private String warrantDate;
		
		private String allotedByNameAndStaffNo;
		private String allotedToNameAndStaffNo;

		private List<Integer> allotedToIds;	

}
