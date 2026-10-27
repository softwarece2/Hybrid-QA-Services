package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class SoAttachmentDetailDto {
	private int id;
    private String url;
    private String fileName;
    
    public SoAttachmentDetailDto(int id, String fileName, String url) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
    }
}
