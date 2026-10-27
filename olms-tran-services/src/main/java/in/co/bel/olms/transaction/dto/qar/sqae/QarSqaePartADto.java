package in.co.bel.olms.transaction.dto.qar.sqae;

import java.util.Date;

import lombok.Data;

@Data
public class QarSqaePartADto {
	
	private Integer id;
	private String firmsName;
	private String firmsAddress;
	private String soNo;
	private String atNo;
	private String soDate;
	private String indentor;
	private String indentNo;
	private String consignee;
	private String qaAssuredDate;
	private String callLetterNo;
	private String callLetterDate;
	private String clsOfDeviation;
	private String priceReduction;
	private String deliveryDate;
	private String clReceivedDate;
	private String itemNo;
	private String eqptDesc;
	private String accUnit;
	private String qtyUnit;
	private String tenderQty;
	private String acceptedQty;
	private String broughtToLedger;
	private String rejected;
	private String remarks;
	private Date submittedDate;
	private Integer submittedBy;
	private String submittedByStr;

}
