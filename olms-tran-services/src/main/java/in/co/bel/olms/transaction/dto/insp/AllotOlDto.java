package in.co.bel.olms.transaction.dto.insp;


import lombok.Data;

/**
 * 
 * @author Manasa B
 * @since 24-02-2023
 *
 */
@Data
public class AllotOlDto {
	private int id;
	private Integer salesGroupId;
	private String name;
	private String soNumber;
	private String olNumber;
	private String offerDate;
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
