package in.co.bel.olms.master.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrgCqaeMappingDto {

	private Integer id;
	private Integer oldId;
	private String organizationName;
	private List<Integer> inspectionAgencyIds;
	private List<OrgCqaeMappingDto> inspectionAgencies;
	private String active;

}
