package in.co.bel.olms.master.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupplierDto {

	private int id;
	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp ="^$|^[a-zA-Z\\s]+$", message = "Only alphabet characters allowed")
	private String name;
	private Boolean active;
	private Boolean status;
	private int createdBy;
	private Date createdDate;

}
