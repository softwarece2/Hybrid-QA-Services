
package in.co.bel.olms.ate.dto;

import lombok.Data;

@Data
public class DepartmentToProductMappingDto {
	private Integer id;

	private Integer departmentId;

	private Integer productId;

	private String departmentName;

	private String productName;
	private String partNo;
	private String materialDescription;
	private String catPartNo;
	private String nsnPartNo;
	private Boolean active;

}
