package in.co.bel.olms.transaction.dto;


import lombok.Data;

@Data
public class LineItemObservationAttachmentDto {
	private Integer id;
	private String fileName;
    private String category;
    private String docName;
    private Integer lineItemObsFileId;
}
