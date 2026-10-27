package in.co.bel.olms.transaction.dto.insp;


import lombok.Data;

/**
 * 
 * @author Manasa B
 * @since 23-02-2023
 *
 */
@Data
public class AllotSoDto {
	private int id;
	private Integer salesGroupId;
	private Integer indenterId;	
	private String projectName;
	private Integer employeeId;
	private String inspectorName;
	private Integer secOfficerId;
	private String secOfficerName;
	private Integer dcqaoId;
	private String dcqaoName;
	private Integer jcqaoId;
	private String jcqaoName;
	private Integer cqaoId;
	private String cqaoName;
	private String orderDate;
	private String orderNo;
	private Integer subordinateId;
}
