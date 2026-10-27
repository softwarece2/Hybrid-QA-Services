package in.co.bel.olms.transaction.dto.qar.sqae;

import lombok.Data;

@Data
public class QarCheckReportDto {
	
	private Integer id;
    private String qaCallLetter;
    private String scrutinyOfQacl;
    private String preInspectionReport;
    private String guaranteeCertificate;
    private String calibrationReport;
    private String sqaeTestResults;
    private String qaReport;
    private String confirmatoryReport;
    private String vigilanceReport;


}
