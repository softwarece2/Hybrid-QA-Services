package in.co.bel.olms.master.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ApplicableTestDto {

	private int id;
	private String testName;
	private Boolean active;
	private Integer salesGroupId;
}
