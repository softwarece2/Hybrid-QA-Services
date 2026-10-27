package in.co.bel.olms.transaction.dto;


import lombok.Data;

import java.util.Date;

/**
* Dto class for OL register
*
* @author  Awadhesh Verma
* @since   26-08-2023
*/

@Data
public class OLRegisterDto {

	private String olNumber;
	private Date offerDate;
	private String indentNo;
	private Date indentDate;
	private Date orderDate;
	private String projectDescription;
	private Integer noOfItems;
	private Integer totalQty;
	private Double value;
	private Date dd;

}
