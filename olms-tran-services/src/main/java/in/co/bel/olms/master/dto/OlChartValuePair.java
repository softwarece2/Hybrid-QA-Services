package in.co.bel.olms.master.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ChartValuePair DTO class for OL dashboard
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-04-11
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OlChartValuePair {
	
	private String month;
	private int cleared;
	private int returned;

}
