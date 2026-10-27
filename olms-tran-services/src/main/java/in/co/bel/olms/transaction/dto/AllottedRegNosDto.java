package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class AllottedRegNosDto {
	
	private Integer olId;
	private String olNumber;
	
	private Integer olLineItemId;
	private String olLineItemDate;

	private Integer soLineItemId;
	private String soLineItemNo;
	
	private Integer registerNoId;
	private String registerNo;
	private String status;
	
	private Integer productId;
	private String productName;
	
	private String partNo;
	
}
