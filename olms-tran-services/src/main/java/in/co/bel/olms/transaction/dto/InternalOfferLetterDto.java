package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class InternalOfferLetterDto {
	private int id;
	private Integer saleOrderNo;
	private Integer employeeId;
	private String olNumber;
	private String saleOrderNoStr;
	private String offerDate;
	private String employee;
	private List<OlLineItemDto> olLineItemDtos;
	
}
