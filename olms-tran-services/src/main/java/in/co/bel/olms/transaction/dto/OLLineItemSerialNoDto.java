package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data

public class OLLineItemSerialNoDto {

  private Integer id;
  private Integer olLineItemId;
  private Integer serialNoId;
  private String serialNo;
  private Boolean active;
  private String status;
  private String lineItemNo;
  private Integer sampleQty;
  private Integer olId;
  private List<Integer> ids;
  private Boolean isSample;
  private Boolean isFaiSample;
  private Integer iNoteLineItemId;
  private Integer productId;
  private Date createdAt;
}