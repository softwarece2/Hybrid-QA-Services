package in.co.bel.olms.transaction.dto;




import java.math.BigDecimal;

import lombok.Data;

@Data

public class InoteDetailsDto {
	
	private String Grp;
	private String internalInoteNo;
	private String internalInoteDate;
	private String inoteNo;
	private String inoteDate;
	private String warrantyNo;
	private String workOrderNo;
	private BigDecimal subQty;
	private BigDecimal acceptedQty;
	private BigDecimal rfrQty;
	private BigDecimal rejectedQty;
	private String Organization;
	private Integer olid;
	private String mergeSslipNo;
	private Integer mergeSslipId;
	private String Epcode;
	private String OfferLetterNumber;
	private String offerLetterDate;
    private Integer inoteId;
}
