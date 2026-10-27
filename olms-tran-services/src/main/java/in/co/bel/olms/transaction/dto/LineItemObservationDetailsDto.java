package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class LineItemObservationDetailsDto {
	private Integer soLineItemId;
    private String serialNo;
    private String remarks;

    private Integer lineItemObsId;
    private String lineItemNo;
    private String partNo;
    private String description;
    private Date  createdDate;

}
