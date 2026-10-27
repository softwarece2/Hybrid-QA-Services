package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WarrantDetailsDto {
	
	private Integer id;
	private Date createdDate;
	private Integer createdById;
	private String createdBy;
	private Integer modifiedBy;
	private Date modifiedDate;
	private Integer categoryId;
	private Integer lineItemSerialNoId; 
	private String lineItemSerialNo; 
	private String lineItemSerialNoStatus; 
	private Integer lineItemSerialNoStatusId; 
	private List<Integer> lineItemSerialNoIds; 
	private Integer soLineItemId;
	private String workOrderNo;
	private String warrantNo;
	private Date wtIssueDate;
	private String estNo;
	private String revNo;
	private Date estDate;
	private String drawingNo;
	private String epCode;
	private String ordNo;
	private Integer wtQty;
	
}
