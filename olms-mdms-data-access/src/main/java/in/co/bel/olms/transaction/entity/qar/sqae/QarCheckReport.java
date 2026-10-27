package in.co.bel.olms.transaction.entity.qar.sqae;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "qar_check_report", schema = "tdms_army")
@Data
public class QarCheckReport {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="qar_check_report_id_seq")
    @SequenceGenerator(name="qar_check_report_id_seq",schema="tdms_army",sequenceName="qar_check_report_id_seq",allocationSize=1)
    @Column(name="id")
	private Integer id;
	
	@Column(name = "qa_call_ltr")
    private String qaCallLetter;

    @Column(name = "scrutiny_of_qacl")
    private String scrutinyOfQacl;

    @Column(name = "pre_insp_report")
    private String preInspectionReport;

    @Column(name = "guarantee_cert")
    private String guaranteeCertificate;

    @Column(name = "calibration_report")
    private String calibrationReport;

    @Column(name = "sqae_test_results")
    private String sqaeTestResults;

    @Column(name = "qa_report")
    private String qaReport;

    @Column(name = "confirmatory_report")
    private String confirmatoryReport;

    @Column(name = "vigilance_report")
    private String vigilanceReport;

}
