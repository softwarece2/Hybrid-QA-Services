
package in.co.bel.olms.transaction.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergedOlDetDto {

	 private String mergedOlNo;

	 private String olNo;
	 private Integer olId;
	 private String submissionSlipNo;
	 private String olDate;
	 private String mergedDate;
	 private String soNumber;
	 private String soDate;
	 private String grp;
	 private Integer mergedOlId;
	 private List<String> regNos = new ArrayList<>();
	 private String Epcode;
	 private String nomenclature;
	 private Integer registrationNo;

}
