package in.co.bel.olms.ate.dto;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class RdsFieldsDto {
		private String label;
		private Double value;
		private BigDecimal minVal;
		private BigDecimal maxVal;
}
