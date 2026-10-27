package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class SubSlipSerialNoDto {

	private Integer id;
	private Integer warrantDetailsId;
	private Integer serialNoId;
	private String serialNo;
	private Integer subSlipId;
	private String subSlip;
	private Integer subSlipLineItemId;
	private String finalStatus;
	private Integer createdById;
	private String createdBy;
	private String createdAt;
    private Integer registrationNo;
}
