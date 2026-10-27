package in.co.bel.olms.transaction.dto;


import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* SupplyOrderDto class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   21-02-2023
*/

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupplyOrderInputDto {
	
	private int id;
	private Integer orgId;
	private Integer empId;
	private Integer qaAgencyId;
	private List<SupplyOrderDto> supplyOrderDtos;

}
