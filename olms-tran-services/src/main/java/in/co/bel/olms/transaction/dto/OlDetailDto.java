
package in.co.bel.olms.transaction.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OlDetailDto {
     private String supplyOrderNo;
     private String supplyOrderDate;
	 private String olDate;
	 private String olNo;
	 private List<String> regNos = new ArrayList<>();
}
