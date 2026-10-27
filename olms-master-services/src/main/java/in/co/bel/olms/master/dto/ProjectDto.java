package in.co.bel.olms.master.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProjectDto {

	private int id;
	private String name;
	private String shipNo;
	private String saleOrderNo;
	private String itemCategory;

	private Date createdDate;

	private Date modifiedDate;

	private Integer createdBy;

	private Integer modifiedBy;

}
