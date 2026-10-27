package in.co.bel.olms.transaction.dto;


import lombok.Data;

import java.util.Date;

/**
* Dto class for SO/PO register
*
* @author  Awadhesh Verma
* @since   25-08-2023
*/

@Data
public class SORegisterDto {

	private String indentorName;
	private String supplierName;
	private String description;
	private String orderNo;
	private Date orderDate;
	private Date receiptDate;
	private Integer noOfItems;
	private Integer totalQty;
	private Double value;
	private Date dd;
	private String section;
	private String fileNo;

}
