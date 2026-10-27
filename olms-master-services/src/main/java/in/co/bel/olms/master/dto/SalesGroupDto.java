package in.co.bel.olms.master.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SalesGroupDto {
	
	private int id;
	private String name;
	private String code;
	private int organizationId;
	private String organizationName;
	private Boolean active;
	private Date createdDate;

}
