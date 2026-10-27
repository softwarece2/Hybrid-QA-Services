package in.co.bel.olms.transaction.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* SupplyOrderDto class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-01-30
*/

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SaleOrderDto {
	
	private int id;
	private String saleOrderNo;

}
