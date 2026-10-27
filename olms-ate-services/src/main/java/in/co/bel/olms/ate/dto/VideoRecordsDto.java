package in.co.bel.olms.ate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VideoRecordsDto {

	private Integer id;
	private Integer olId; 
	private String olNumber; 
	private Integer olLiId;
	private Integer devId;
	private String devName;
	private Integer testId;
	private String test;
	private Integer createdById;
	private String createdBy;
	private String createdAt;
	private String path;

    private Boolean isOl;
    
    @JsonProperty("sSlipId")
    private Integer sSlipId;

    private String sSlipNum;

    @JsonProperty("sSlipLiId")
    private Integer sSlipLiId;

    @JsonProperty("sSlipDevId")
    private Integer sSlipDevId;

    private String sSlipDevName;
}
