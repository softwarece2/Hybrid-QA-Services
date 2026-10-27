package in.co.bel.olms.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OLDetailsDto {

     private String supplyOrderNo;
     private String supplyOrderDate;
  	 private String olDate;
	 private String olNo;
	 private String  regNos;
	 private String LotNo;
	 private Integer subSlipId;
	 private String epCode;
	 private String nomenclature;
	 private Integer registrationNo;
	 
}