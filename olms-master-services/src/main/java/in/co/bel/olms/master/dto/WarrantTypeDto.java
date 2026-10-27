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
public class WarrantTypeDto {
	private int id;
    private String warrantType;

}
