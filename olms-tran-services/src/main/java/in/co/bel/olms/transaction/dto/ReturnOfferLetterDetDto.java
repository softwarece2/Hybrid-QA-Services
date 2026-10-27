package in.co.bel.olms.transaction.dto;


import lombok.Data;

@Data
public class ReturnOfferLetterDetDto {

	private Integer id;

	private Integer returnId;
	
	private String observation;

	private String createdDate;

	private Integer createdBy;

}
