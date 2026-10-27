package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class SampleSelectionDto {
	private int regId;
	private String regNo;
	private int olLineItemId;
	private int soLineItemId;
	private String soLineItemNo;	
	private Integer productId;
	private String status;
	private Integer qaStatusId;
	private String qaStatus;
	private String faiStatus;
	
}
