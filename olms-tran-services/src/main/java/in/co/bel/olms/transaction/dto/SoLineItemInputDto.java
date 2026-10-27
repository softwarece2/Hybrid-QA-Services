package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class SoLineItemInputDto {
	
	private Integer saleOrderId;
	private Integer supplyOrderId;
	private String saleOrderNo;
	
	private List<SoLineItemDto> lineItemDtos;
	private Integer createdBy;

}
