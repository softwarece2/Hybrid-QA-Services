package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LineItemLotsDto {
	private Integer soLineItemId;
	private List<LineItemDetailsDto> lineItems;
	private Date createdDatetime;
	private Date modifiedDatetime;
}
