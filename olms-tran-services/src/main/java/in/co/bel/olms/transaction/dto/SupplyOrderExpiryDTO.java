package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SupplyOrderExpiryDTO {
	private Long id;
	private String orderNo;
	private Date originalDd;
	private Date latestNewDd;
	private Date finalDd;
	private long daysRemaining;
	private String salesGrpName;
	
	public SupplyOrderExpiryDTO(Long id, String orderNo, Date originalDd, Date latestNewDd, Date finalDd,
			long daysRemaining) {
		this.id = id;
		this.orderNo = orderNo;
		this.originalDd = originalDd;
		this.latestNewDd = latestNewDd;
		this.finalDd = finalDd;
		this.daysRemaining = daysRemaining;
	}

	public SupplyOrderExpiryDTO(Long id, String orderNo, Date originalDd, Date latestNewDd, Date finalDd,
			long daysRemaining, String salesGrpName) {
		this.id = id;
		this.orderNo = orderNo;
		this.originalDd = originalDd;
		this.latestNewDd = latestNewDd;
		this.finalDd = finalDd;
		this.daysRemaining = daysRemaining;
		this.salesGrpName = salesGrpName;
	}
}