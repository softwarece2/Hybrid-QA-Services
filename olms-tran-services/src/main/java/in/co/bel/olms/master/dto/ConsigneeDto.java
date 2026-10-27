package in.co.bel.olms.master.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
	private String name;
	private String phone;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,& characters allowed")
	private String address;
	private String fax;
	private boolean active;
}
