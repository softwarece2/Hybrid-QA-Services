package in.co.bel.olms.transaction.dto;


import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* SoApprovalDto class
*
* @author  Awadhesh Verma
* @since   28-02-2023
*/

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SoApprovalDto {
	
	private Boolean isEditAllowed;
	private Date cqaoApprovalDate;
	private int id; //InternalInoteNoId
	private String internalNo;
	private Integer offerLetterId;
	
	private List<OlApprovalDto> soApprovalDtos;
	private List<SubSlipApprovalDto> sSlipApprovalDtos;

}
