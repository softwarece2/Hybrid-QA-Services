package in.co.bel.olms.ate.dto;

import lombok.Data;
@Data
public class PartnoProductMappingDto {
	private Integer id;

    private Integer productId;  

    private Integer partNumberId;
    
    private String productName;
    
    private String partNumberName;
}
