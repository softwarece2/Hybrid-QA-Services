package in.co.bel.olms.iol.ate.dto;

import lombok.Data;

@Data
public class AteTestReportUploadDto {

	private Integer id;
	private String origFileName;
	private String fileName;
	private String fileDownloadUrl;
	private Integer uploadedById;
	private String uploadedBy;
	private String uploadedAt;
	private Integer devId;
	private Integer prodId;
	private Integer testId;
	private Integer roundId;
	private Integer specId;
	private Integer ollisnId;
	
}