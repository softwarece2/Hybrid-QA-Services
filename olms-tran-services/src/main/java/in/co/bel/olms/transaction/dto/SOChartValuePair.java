package in.co.bel.olms.transaction.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SOChartValuePair {
	
	private String key;
	private int order;
	private int approved;

}
