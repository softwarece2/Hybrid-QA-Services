package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;


/**
 * 
 * @author Manasa
 * @since 08-02-3023
 */
@Data
public class FclDto {

	private Integer id;

	private Integer supplyOrderId;
	private String soOrderNo;
	private String soDate;
	private String opa;
	private String projectName;
	private String fileNo;
	private String salesGrpName;
	private Integer createdBy;
	private String submittedDate;
	private Date createdDate;
	private List<FclVigilanceDto> fclVigDtoList;
	private List<Integer> deletedVigList;
	private String signedBy;
}
