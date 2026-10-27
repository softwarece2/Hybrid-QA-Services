package in.co.bel.olms.iol.ate.dto;

import lombok.Data;

@Data
public class FileDetailDto {

	private Integer fileUploadId;
    private String fileName;
    private String origFileName;
    private String fileDownloadUrl;
    private Integer version;

}
