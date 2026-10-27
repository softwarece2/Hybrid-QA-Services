package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class LineItemObservationDto {
	private Integer id;
	private Integer supplyOrderId;
	private String supplyOrderNo;
    private Integer createdBy;
    private String createdByName;
    private String sendToName;
    private String tittle;
    private Integer sendTo;
    private List<LineItemObservationDetailsDto> lineItemObservationDetails;

    private List<LineItemObservationFileDto> lineItemObservationFileDtos;
    private String inputRemarks;

}
