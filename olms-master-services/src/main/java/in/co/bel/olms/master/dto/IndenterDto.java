package in.co.bel.olms.master.dto;

import lombok.Data;

@Data
public class IndenterDto {
	
	private int id;
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z\\s]+$", message = "Only alphabets characters allowed")
	private String name;
	private String shortName;
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,& characters allowed")
	private String address;
	private Boolean active;

}
