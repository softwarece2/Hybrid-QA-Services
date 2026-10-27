package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class INoteRegisterItemDto {
	private String iNoteNo;
	private String fileNo;
	private String orderNo;
	private String orderDate;
	private String Indenter;
	private Integer noOfItems;
	private Integer qty;
	private Double value;
	private String deliveryType;
	private String description;
	private String section;
	private String division;
	private String olNumber;
	private String olDate;
	private String dd;
	private Integer sectionId;
	private String inoteDate;
	
}
