package in.co.bel.olms.transaction.dto;



import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class WarrantCertificateDto {
	private Integer id;
	private String certificateNo;
	private String itemCode;
	private String nomenclature;
	private String drawingNo;
	private String contractNo;
	private String contractDate;
	private Integer qty;
	private List<String> regNos;
	private List<String> passedRegNos;
	private Integer createdById;
	private String createdBy;
	private String createdDate;
    private LocalDate startDate;
    private LocalDate endDate;

	private Integer warrantType;
	
	@JsonProperty("inoteId")
	private Integer inoteId;
	
	private Integer authorityId;
	private String authorityName;
	private String approvalDate;
	private String inoteNo;
	private String inoteDate;
	private String regNos1;
	private String warrantTypeName;
	private String eName;

    private Boolean isDoaApproved;
    private Integer doaApproverId;
    private String doaApproverName;
    private Integer warrantyPeriodQty;
//    private String warrPeriodType;	//need to remove, Chirag , 24-10-26
    private Integer registrationNo;
}
