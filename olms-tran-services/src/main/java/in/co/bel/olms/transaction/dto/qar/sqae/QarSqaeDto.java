package in.co.bel.olms.transaction.dto.qar.sqae;

import java.util.Date;

import in.co.bel.olms.transaction.dto.CrossCheckTier2Dto;
import in.co.bel.olms.transaction.dto.CrossCheckTier3Dto;
import lombok.Data;

@Data
public class QarSqaeDto {

	private Integer id;
	private Integer qarPartAId;
	private Integer qarPartBId;
	private Integer qarPartDId;
	private Integer qarPartCId;
	private Integer checkTier2Id;
	private Integer checkTier3Id;
	private Integer qarCheckReportId;
	private Integer olId;
	private Integer createdBy;
	private Date submittedDatetime;
	private QarSqaePartADto qarSqaePartADto;
	private QarSqaePartBDto qarSqaePartBDto;
	private QarSqaePartCDto qarSqaePartCDto;
	private QarSqaePartDdto qarSqaePartDdto;
	private CrossCheckTier2Dto crossCheckTier2Dto;
	private CrossCheckTier3Dto crossCheckTier3Dto;
	private QarCheckReportDto checkReportDto;
	private String qaclNo;
	private String qaclDate;
	private String firmName;
	private String indentNo;
	private String supplyOrderDate;
	private String indentor;
	private String consignee;
	private boolean partaAllottedStatus = false;
	private boolean partBAllottedStatus = false;
	private boolean partCAllottedStatus = false;
	private boolean partDAllottedStatus = false;
	private boolean tierIIAllottedStatus = false;
	private boolean tierIIIAllottedStatus = false;

}
