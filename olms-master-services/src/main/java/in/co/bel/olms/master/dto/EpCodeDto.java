package in.co.bel.olms.master.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EpCodeDto {
	private String partNo;
	private String Description;
	private String orderDetails;
	private BigDecimal totalQty;
	private String indenterName;
	private String consigneeName;
	private String inspectionAuthorityName;
	private String lineItemNo;
	private String OrderNo;
}
