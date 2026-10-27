package in.co.bel.olms.iol.ate.dto;



import java.util.Date;

import lombok.Data;

@Data
public class InspTestMappingDto {
private Integer id;
private Integer inspId;
private Integer testId;
private Integer submittedById;
private Date submittedDate;
private Integer testStatusId;
//private Integer modifiedBy;
//private Date modifiedDate;
}
