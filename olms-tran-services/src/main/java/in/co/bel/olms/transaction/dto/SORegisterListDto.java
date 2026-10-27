package in.co.bel.olms.transaction.dto;
import java.util.List;

import lombok.Data;
@Data
public class SORegisterListDto {
	List<SORegisterDto> soregisters;
	private Double totalValue;
	private int totalSO;

}
