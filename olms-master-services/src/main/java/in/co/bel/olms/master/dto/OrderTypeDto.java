package in.co.bel.olms.master.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@ToString
@EqualsAndHashCode
@Data
public class OrderTypeDto {
	private int id;
    private String orderType;
}
