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

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyTender;
import lombok.Data;

@Data
@Entity
@Table(name = "scrutiny_tender_det", schema = "tdms_check_army")
public class ScrutinyTenderDet implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_tender_det_seq")
	@SequenceGenerator(name = "scrutiny_tender_det_seq", schema = "tdms_check_army", sequenceName = "scrutiny_tender_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "remarks")
	private String remarks;

	@ManyToOne
	@JoinColumn(name = "scrutnity_tender_id")
	private ScrutinyTender scrutinyTender;

	@ManyToOne
	@JoinColumn(name = "checklist_scrutiny_tender_id")
	private ChecklistScrutinyTender checklistScrutinyTender;

	@Column(name = "obs_sts")
	private String obsSts;
	
	@Column(name = "action_and_ref")
	private String actionAndRef;

}
