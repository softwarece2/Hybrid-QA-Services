package in.co.bel.olms.transaction.dto;


import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SubSlipApprovalDto {
	
	private Integer id;
	private Integer subSlipId;
	private Integer supplyOrderId;
	private String inspectorName;
	private Date createdDate;
	private Date modifiedDate;
	private Integer createdBy;
	private String createdByStr;
	private Integer approvedBy;
	private String approvedByStr;
	private Integer modifiedBy;
	private String modifiedByStr;
	private String remarks;
	private String noteNo;
	private String offerLetterNo;
	private Integer olStatus;
	private Integer soStatus;
	private String olStatusStr;
	private String soStatusStr;
	private String projectName;
	private String designation;
	private String empRole;
	private Integer nextApproverId;
	private String nextApproverName;
	private Integer parentOlApprovalId;
	private Integer parentSoApprovalId;
	private List<String> uploadedFiles;
	private Integer inspectionFileNoteId;
	private Date cqaoApprovalDate;
	private String approverDesignation;
	private String qaoRemarks;
	private Boolean isRfr;
	private Integer internalInoteId;
	private Integer approvalStatus;
}
