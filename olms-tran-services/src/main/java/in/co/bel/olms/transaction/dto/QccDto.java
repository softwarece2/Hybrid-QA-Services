package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class QccDto {

	private Integer id;
	private String inoteDate;
	private String nomenclature;
	private String regNo;
	private BigDecimal qtyTendered;
	private String swoNo;
	private String warrantNo;
	private String soNo;
	private String ifdNo;
	private String ifdDate;
	private String drawingNo;
	private String deviationConcession;
	private String remarks;
	private Integer inoteId;
	private String inoteNo;
	private Integer createdById;
	private String createdBy;
	private String createdAt;
	private Integer mergedSubSlipId;
	private String mergeSubSlipNo;
	private List<Map<String, Object>> consignees;
	private String OlNo;
	private String OfferDate;
	private Integer prodApprovedById;
	private String prodApprovedBy;
	private String prodApprovedByDept;
	private String prodApprovedAt;
	private Integer qcApprovedById;
	private String qcApprovedBy;
	private String qcApprovedByDept;
	private String qcApprovedAt;
	private Integer qccStatusId;
	private String qccStatus;
	private String soDate;

	private String consignee;
	private Boolean isOlGenerated;
	private String catPartNo;
	private String nsnPartNo;
	private String itemCategory;
	private Integer externalInoteId;
	private String externalInoteNo;
	private String externalInoteDate;
	private String section;
	private Integer registrationNo;
	private String epCode;
	private String olRemarks;
}
