package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LineItemDetailsDto {
	private Integer id;
	private BigDecimal offeredQty;
    private String financialYear;
    private double unitPrice;
}
