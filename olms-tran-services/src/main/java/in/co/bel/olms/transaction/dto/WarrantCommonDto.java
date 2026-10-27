package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class WarrantCommonDto {

	private Integer soLineItemId;
	private String soLineItemNo;
	private String partNumber;
	private String materialDescription;
	private BigDecimal remQuantity;
	private List<WarrantDetailsDto> warrantDetailsDtos;
	private String olNumber;
	private String offerDate;
	private BigDecimal offeredQty;
	private List<String> regNosList;
	private  List<Map<String, Object>> regNos;
	private  List<Map<String, Object>> allRegNos;
	private Integer registrationNo;
}
