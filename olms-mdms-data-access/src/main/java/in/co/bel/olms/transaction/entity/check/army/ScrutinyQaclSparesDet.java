package in.co.bel.olms.transaction.entity.check.army;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyQaclSpares;
import lombok.Data;

@Data
@Entity
@Table(name = "scrutiny_qacl_spares_det", schema = "tdms_check_army")
public class ScrutinyQaclSparesDet implements Serializable{
	private static final long serialVersionUID = -2620922020388944938L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_qacl_spares_det_id_seq")
    @SequenceGenerator(name = "scrutiny_qacl_spares_det_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_qacl_spares_det_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "scrutiny_qacl_spares")
    private ScrutinyQaclSpares scrutinyQaclSpares;

    @Column(name = "remarks")
    private String remarks;
    
    @ManyToOne
    @JoinColumn(name = "checklist_scrutiny_qacl_spares_id")
    private ChecklistScrutinyQaclSpares checklistScrutinyQaclSpares;

    @Column(name = "obs_sts")
    private String obsSts;
}
