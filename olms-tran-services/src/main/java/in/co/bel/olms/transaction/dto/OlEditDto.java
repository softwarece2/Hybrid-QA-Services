package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OlEditDto {
	
	private int offerLetterId;
	private String consigneeId;
	private Date offerDate;
}
