package in.co.bel.olms.master.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * INoteChartValuePair DTO class for INote dashboard
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-05-13
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class INoteChartValuePair {
	
	private String key;
	private int iNote;
	private int formIV;

}
