package in.co.bel.olms.master.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;


/**
 * 
 * @author Manasa B
 * @author VizagTeam
 *
 */
@Data
public class OrganizationDto {

	private int id;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^[a-zA-Z0-9\\s@#,.&/()\\-_]+$", message = "Only alphanumeric characters and #@,&, /,- characters allowed")
	private String organizationName;
	private String email;
	private String mobile;
	private int organizationTypeId;
	private String organizationType;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp ="^[a-zA-Z0-9\\s\\-#./,()&]+$", message = "Only alphanumeric and #@,&, /,- characters allowed")
	private String organizationAddress;
	private boolean active;
	private Date createdDatetime;
	private Integer modifiedBy;
	private String stdCode;
	private int parentOrgId;
	private byte[] logo;
	private String logoStr;
}
