package in.co.bel.olms.transaction.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
* SoTreeViewDto class to store the data in hierarchy structure
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-02-27
*/

@Data
public class SoTreeViewDto {
	
	private int id;
	private String name;
	private Map<String, Object> attributes;
	private List<SoTreeViewDto> children;

}
