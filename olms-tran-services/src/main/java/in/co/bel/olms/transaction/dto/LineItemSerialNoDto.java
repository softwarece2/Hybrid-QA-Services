package in.co.bel.olms.transaction.dto;



import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LineItemSerialNoDto {

	private Integer id;
	private String serialNo;
	private Integer createdById;
	private String createdBy;
	private Date createdAt;
	private Integer modifiedById;
	private String modifiedBy;
	private Date modifiedAt;
	private Integer statusId;
	private String status;
	private String lineItemNo;
	private List<Integer> ids;
	private Integer soLineItemId;
}
