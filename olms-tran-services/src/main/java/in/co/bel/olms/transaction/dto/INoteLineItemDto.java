package in.co.bel.olms.transaction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class INoteLineItemDto {

  private Integer id;
	private String partNo;
	private String description;
	private String uom;
	private BigDecimal tendered;
	private BigDecimal accepted;
	private BigDecimal rejected;
	private BigDecimal rrQty;
	private BigDecimal ledgerFolio;
	private BigDecimal qtyAcceptedToDate;
	private BigDecimal cumulativePassed;

	private String remarks;
	private int olLineItemId;
	private Integer sslipLineItemId;
	private BigDecimal orderedQty;
	private String hologramRemarks;
	private String indentNo;
	private String itemRemarks;

	private Integer partNumberId;
	private String partNumberName;
	private String materialDescription;
	private List<String> regNos;
	private List<Map<String, Object>> regs;
	private List<Map<String, Object>> failedRegs;

	private Integer rfr;
	private String otherInspAuthority;
	public String ename;
	private String catPartNo;
    private String nsnPartNo;
    private String lineItemNo;
    private String orderTypeNo;
    private String itemCategory;
    private String failedParamDesc;
	private String nomenclature;
	private Integer registrationNo;
}
