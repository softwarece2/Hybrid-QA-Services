package in.co.bel.olms.master.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;


@Data
public class PartNumberDto {

    private Integer id;
    private String partNo;
    private String materialDescription;
    private String catPartNo;
    private String nsnPartNo;
    private Date createdDate;
    private Date modifiedDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Boolean active;
    private List <Integer>sectionCode;
    private Integer uomId;
    private String uom;
    private Integer itemCategoryId;
    private String itemCategory;
    private String drawingNo;
    private List<DeptDropDto> departments;
    private String partNoMatDesc;
    private Boolean isMapped;

}