package in.co.bel.olms.transaction.dto.insp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import in.co.bel.olms.transaction.dto.OfferLetterDto;
import in.co.bel.olms.transaction.dto.SupplyOrderDto;
import lombok.Data;

@Data
public class InspectionFileNoteDto {
	
	private SupplyOrderDto soDto;
	private OfferLetterDto olDto;
	private Integer id;
	private Integer offerLetterId;
	private Integer createdBy;
	private Date createdDatetime;
	private String olNumber;
	private Date olDate;
	private String projectDescription;
	private String fileNumber;
	private String noteNo;
	private String supplyOrderNo;
	private Date supplyOrderDate;
	private Date originalDeliverydate;
	private String totalLineItemsPo;
	private String totalValueLineItemsPo;
	private String totalInotesCcIssued;
	private String completedLotSize;
	private String noLineItemsSupplied;
	private String noLineItemsOl;
	private String valueOfferedOl;
	private String noLineItemsClrdOl;
	private String valueClrdOl;
	private String inspectorName;
	private String inspectorDesignation;
	private String qapRef;
	private Boolean inspDocVerify;
	private Boolean inspElec;
	private Boolean inspFats;
	private Boolean inspPdi;
	private Boolean inspPhysical;
	private Boolean inspEss;
	private Boolean inspEmiEmc;
	private Boolean inspEndurance;
	private Boolean inspEt;	
	private Date inspectionStartDate;
	private Date inspectionCompleteDate;
	private String inspectorRemrks;
	private String acqaoRemarks;
	private String dcqaoRemarks;
	private String jcqaoRemarks;
	private Date extendedDd1;
	private Date extendedDd2;
	private Boolean isFormalDd1;
	private Boolean isFormalDd2;
	private String latestDdExtendedVide;
	private String lotSizeOriginal;
	private String lotSizeAmmended1;
	private String lotSizeAmmended2;
	private String typeOfDelivery;
	private Boolean isSubmitted;
	private String fileNoteStatus;
	private Date cqaoApprovalDate;
	private Integer supplyOrderId;
	private String salesGrpName;
	private String iNoteDate;
	// to cater single file noting for multiple OLs of same PO
	private List<Integer> offerLetterIds;
	private String olDateStr;
	private Integer iNoteTypeId;
	private String inoteStatus;
	private Boolean clearanceStatus;	
	private Boolean isQinCompleted;
	private Boolean isFileNoteIdPresent;
	private List<OfferLetterDto> ols;
	private String olDescription;
	private String consigneeName;
	private String consigneeAddr;
	private Integer inoteId;
	private String soType;
	private Integer internalInoteId;
	private String inspectionAgency;
	private BigDecimal acceptedQty;
	private BigDecimal rejectedQty;
	private Integer rfr;

}
