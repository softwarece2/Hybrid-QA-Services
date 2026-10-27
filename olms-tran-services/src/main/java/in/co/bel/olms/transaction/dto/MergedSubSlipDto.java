

package in.co.bel.olms.transaction.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergedSubSlipDto {

	 private String mergedOlNo;
	 private String olNos;
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
	 private String InspectionAuthority;
	 private Integer organizationTypeId;

	 private String Epcode;
  private Integer statusId;
  private String status;
	 

}
