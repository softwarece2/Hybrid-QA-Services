package in.co.bel.olms.transaction.dto;

import in.co.bel.olms.transaction.entity.SOLineItem;
import lombok.Data;

@Data
public class OlLineItemDto {
	
	private int id;
	private SOLineItem soLineItem;

}
