package in.co.bel.olms.transaction.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * SurpriseCheckDto class
 *
 * @author Awadhesh Verma
 * @since 2023-08-28
 */

@Data
public class SurpriseCheckDto {

	private Integer id;
	private Date createdDate;
	private Integer createdBy;
	private String createdByName;
	private String individualResponsible;
	private String testCarried;
	private String crossVerifyBy;
	private String remarks;
	private String crossVerifyDate;
	private String inspectionDate;
	private String firm;
	private Integer soLineItemId;
	private String itemNo;
	private String itemDescription;
	private String supplyOrderNo;
	private String soDescription;
	private String supplier;
	private String firmAddress;
	private String supplyOrderDate;
	private List<PartListDto> items;
}
