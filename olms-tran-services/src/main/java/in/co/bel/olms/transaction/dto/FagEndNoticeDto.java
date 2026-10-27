package in.co.bel.olms.transaction.dto;

import in.co.bel.olms.master.dto.OrganizationDto;
import lombok.Data;

import java.util.Date;

@Data
public class FagEndNoticeDto {

    private Integer id;
    private Date createdDatetime;
    private String modifiedDateTime;
    private Integer lastUpdatedBy;
    private Integer supplyOrderId;
    private Integer offerLetterId;
    private String issuerSignature;
    private String issuerName;
    private String issuerDesignation;
    private String orderNo;
    private String orderDate;
    private String olNumber;
    private String offerDate;
    private String recievedDate;
    private String description;
    private Date dd;
    private Integer empId;
    private Date submittedDate;
    private Integer emplId;
    private Integer createdBy;
    private OrganizationDto soOrganizationDto;
    private OrganizationDto sgOrganizationDto;
    private String categoryName;
    private String projName;

}
