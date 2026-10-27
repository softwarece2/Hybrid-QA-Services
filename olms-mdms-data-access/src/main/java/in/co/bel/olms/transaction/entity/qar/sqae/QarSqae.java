package in.co.bel.olms.transaction.entity.qar.sqae;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.co.bel.olms.transaction.entity.CrossCheckTier2;
import in.co.bel.olms.transaction.entity.CrossCheckTier3;
import in.co.bel.olms.transaction.entity.OfferLetter;
import lombok.Data;

@Entity
@Table(name = "qar", schema = "tdms_army")
@Data
public class QarSqae {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qar_id_seq")
	@SequenceGenerator(name = "qar_id_seq", schema = "tdms_army", sequenceName = "qar_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "qar_part_a", nullable = false)
    private QarSqaePartA qarPartA;

	@OneToOne
    @JoinColumn(name = "qar_part_b", nullable = false)
    private QarSqaePartB qarPartB;

	@OneToOne
    @JoinColumn(name = "qar_part_c", nullable = false)
    private QarSqaePartC qarPartC;

	@OneToOne
    @JoinColumn(name = "qar_part_d", nullable = false)
    private QarSqaePartD qarPartD;

	@OneToOne
    @JoinColumn(name = "check_tier_2", nullable = false)
    private CrossCheckTier2 checkTier2;

	@OneToOne
    @JoinColumn(name = "check_tier_3", nullable = false)
    private CrossCheckTier3 checkTier3;
	
	@OneToOne
    @JoinColumn(name = "qar_check_report")
    private QarCheckReport qarCheckReport;
  
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_datetime")
    private Date submittedDatetime;

    @Column(name = "created_by")
    private Integer createdBy;

	@OneToOne
    @JoinColumn(name = "ol_id", nullable = false)
    private OfferLetter offerLetter;
   
}
