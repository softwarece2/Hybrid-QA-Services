package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CrossCheckTier2Dto {
	
	private Integer id;
    private String officerSqao;
    private String conductedAt;
    private String conductedDate;
    private String storeNomenclature;
    private String indentNo;
    private String indentDate;
    private String qtyOnOrder;
    private String totalCost;
    private String instructions;
    private String samplePickedFrom;
    private String challanNo;
    private String challanDate;
    private String noSamplesDrawn;
    private String storeDetails;
    private String scopeOfTests;
    private String reasonForOmit;
    private String results;
    private String compComments;
    private String inference;
    private String clarificationDetails;
    private String suggestion;
    private String recommendation;
    private String officerName;
    private String officerSign;
    private String checkDate;
    private Date submittedDate;
    private Integer submittedBy;
    private String submittedByStr;

}
