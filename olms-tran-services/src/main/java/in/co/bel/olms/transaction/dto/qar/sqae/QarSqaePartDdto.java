package in.co.bel.olms.transaction.dto.qar.sqae;

import java.util.Date;

import lombok.Data;

@Data
public class QarSqaePartDdto {
	
	private Integer id;
	private String obsEncl;
	private String vigilanceCheckDate;
	private String vigilanceCheckedBy;
	private String crossVerificationDate;
	private String isCleared;
	private String isAuthorised;
	private String obsCrossVerificationEncl;
	private String approverSign;
	private String approverName;
	private String approvedDate;
	private String inOrder;
	private String isCarriedOut;
	private String actionInitiated;
	private Date submittedDate;
	private Integer submittedBy;
	private String submittedByStr;

}
