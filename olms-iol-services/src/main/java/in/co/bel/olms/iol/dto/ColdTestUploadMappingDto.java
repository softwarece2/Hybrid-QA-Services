package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ColdTestUploadMappingDto {
	private Integer id;
	private String fileName;
    private String filePath;
    private Date createdDateTime;

}
