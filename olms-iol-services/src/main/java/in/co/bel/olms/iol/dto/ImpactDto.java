package in.co.bel.olms.iol.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ImpactDto implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L; 
	
	private Integer id;
	private Integer impactFlick;
	private Integer impactBreakdown;
	private String impactFlickBreakdRem;
	private Integer tvneId;
	private String submittedDate;
	private Date createdDatetime;
	private Integer impactCleanOk;
	private String impactCleanRem;
	
}
