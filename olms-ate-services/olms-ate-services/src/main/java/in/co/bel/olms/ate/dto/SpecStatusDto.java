package in.co.bel.olms.ate.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SpecStatusDto {

	private Integer id;
	
	@NotNull(message = "Spec ID cannot be null")
	private Integer specId;
	
	private String specValue;
	
	@NotNull(message = "Active cannot be null")
	private Boolean active;
	
	@NotNull(message = "Remark cannot be null")
	private String remark;
	
	@NotNull(message = "Created by Id cannot be null")
	private Integer createdById;
	
	private String createdByNameAndRoleAtCreation;
	
	private String createdByName;
	private String createdAt;

	private List<SpecStatusUpdateDto> updateDtos;
}
