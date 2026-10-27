


package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author Shridhar R
 * Dto for merge OL service	
 */

@Data
public class MergeSubSlipDto {
  private Integer id;
  private String mergeOlNo;
  private Integer createdBy;
  private Integer notifiedTo;
  private Date createdAt;
  private Boolean isNotified;
  private Integer statusId;
  private String status;
  private Integer modifiedBy;
  private Date modifiedAt;
  private List<Integer> olIds;

}


