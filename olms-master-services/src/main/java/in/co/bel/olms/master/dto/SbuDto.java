package in.co.bel.olms.master.dto;

import lombok.Data;

@Data
public class SbuDto {
	
	private int id;
	private Integer sbuCode;
	private String sbuName;
	private int orgId;
	private String activeStatus;
	private boolean active;
	private Integer createdBy;
	
	private Integer modifiedBy;
}
