package in.co.bel.olms.transaction.dto.qar.sqae;

import java.util.Date;

import lombok.Data;

@Data
public class QarSqaePartBDto {

	private Integer id;
	private String specDet;
	private String drawingDet;
	private String testScheduleDet;
	private String acceStoresCost;
	private String preInspEncl;
	private String firmLNo;
	private String notRcvdRsnEncl;
	private String samplingDetEncl;
	private String literatureEncl;
	private String litAprvEncl;
	private String litSubEncl;
	private String litVerfEncl;
	private String qtyDue;
	private String nosBulkSupply;
	private String nosCopiesSupplied;
	private String fullQtyLtrEncl;
	private String nosExamined;
	private String rawMtrlAcpEncl;
	private String nosFullyWorking;
	private String detailedWorkingEncl;
	private String specEgptDetEncl;
	private String warranteeEncl;
	private String nosPackages;
	private String markingDetQty;
	private String markingDetEncl;
	private String firmsCertAsEncl;
	private String bondDetEncl;
	private String smpDrawnEncl;
	private String shortCmgEncl;
	private String rsnsForDelay;
	private String regdslNo;
	private String regdslGrp;
	private String regdslMonth;
	private String inspSign;
	private String inspName;
	private String inspDesig;
	private String inspMarkDet;
	private String dateOfInsp;
	private Date submittedDate;
	private Integer submittedBy;
	private String submittedByStr;

}
