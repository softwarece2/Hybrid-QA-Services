package in.co.bel.olms.transaction.dto.check.army;
import lombok.Data;

@Data
public class ScrutinyQaclSparesDetDto {
		
		private Integer id;	    
		private Integer scrutinyQaclSparesId;
		private Integer checklistScrutinyQaclSparesId; 
		private String slNo;
		private String reference;		   
		private String obsSts;
		private String obsType;
		private String remarks;	 
}
