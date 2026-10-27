package in.co.bel.olms.ate.dto;

import java.util.Date;
import java.util.List;

import in.co.bel.olms.ate.entity.Test;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SbuCameraIpMappingDto {
	private Integer id;
	private String cameraIp;
	private String sbuName;
	private Integer salesGrpId;
	private Date createdDatetime;
	private String testName; 
	private List<Test> tests;
	
}
