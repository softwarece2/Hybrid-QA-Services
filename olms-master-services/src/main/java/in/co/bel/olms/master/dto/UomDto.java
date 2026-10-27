package in.co.bel.olms.master.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class UomDto {

	private int id;
	// private String name;
	@Pattern(regexp = "^$|^[a-zA-Z\\s/\\\\]+$", message = "Only alphabets, spaces, forward slashes, and backward slashes are allowed")
	@NotBlank(message = "Value must not be Empty")
	private String unit;
	private Boolean active;
}
