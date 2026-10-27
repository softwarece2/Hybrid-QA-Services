package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class EnvTestReviewDto {
	private Integer id;

	private String firmRef;

	private String qaInspAuth;

	private String dateOfInsp;

	private String firmName;

	private String orderNo;

	private String orderDate;

	private String description;

	private String prevRef;

	private String drgNo;

	private String dispatchTo;

	private String signOnBehalf;

	private String signature;

	private String positionHeld;

	private String reviewDate;

	private String remarks;

	private String inspCertNo;

}
