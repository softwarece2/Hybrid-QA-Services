package in.co.bel.olms.ate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LineItemLotsRegisterDto {
    private Integer id;
    private List<String> registerNo;
    private String status;
    private Integer lineItemLotsId;
    private Integer olLineItemId;
    private Boolean isRunning;
    private Boolean isSample;

    // for deletion and updates
    private String registerNumber;
    private List<Integer> ids;
    private Integer sampleQty;
    private Boolean isFaiSample;
}
