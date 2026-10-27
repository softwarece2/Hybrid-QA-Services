package in.co.bel.olms.master.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class OpaDto {
	private int id;
	private String opaName;
	private String opashortName;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,& characters allowed")
	private String opaAddress;
	private Boolean opaStatus;
	
	
}
