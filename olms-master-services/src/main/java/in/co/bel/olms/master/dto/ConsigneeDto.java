package in.co.bel.olms.master.dto;

import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ConsigneeDto {

	private int id;
	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z\\s]+$", message = "Only alphabet characters allowed")
	private String name;
//	@Pattern(regexp = "^$|^[0-9]+$", message = "Only number characters allowed")
	private String phone;
	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,& characters allowed")
	private String address;
//	@Pattern(regexp = "^$|^[0-9]+$", message = "Only number characters allowed")
	private String fax;
	private Boolean active;
}
