package in.co.bel.olms.master.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ChartValuePair DTO class for dashboard
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-04-03
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ChartValuePair {
	
	private String label;
	private int value;

}
