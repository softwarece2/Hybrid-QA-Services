package in.co.bel.olms.master.dto;

import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
@EqualsAndHashCode
@Data
public class ItemCategoryDto {
	
	private int id;	
	private String category;
	private Boolean active;

}
