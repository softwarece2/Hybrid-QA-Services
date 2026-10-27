package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OlConcessionDto {
	
	private Integer id;
	private String concession;
	private String affectedItemRef;
	private String concessionDetails;
	private String authority;
	private Date createdDate;
	private String createdDateStr;
	private Integer createdBy;
	private Integer offerLetterId;
	private String olNumber;
}
