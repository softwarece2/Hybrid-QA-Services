package in.co.bel.olms.ate.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class FaiDto {
    private Integer id;
    private String name;
    private Integer status;
    private Integer createdBy; 
    private Integer submittedBy;
    private Date submittedDatetime;
    private Date createdDatetime;
    private Integer olLineItemId;
    private Integer subQty;
    private Integer actQty;
    private Integer rejQty;
    private Boolean isQa;
    private String remarks;
    private List<FaiDetailsDto> faiDetails;    
}