package in.co.bel.olms.master.dto;

import lombok.Data;

@Data
public class OrganizationTypeDto {
	
	private int id;
	private String typeName;
	private String createdDatetime;
	private String modifiedBy;
	private String modifiedDatetime;
	private Boolean active;
	

}
