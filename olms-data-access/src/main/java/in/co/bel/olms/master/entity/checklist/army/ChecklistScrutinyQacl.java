package in.co.bel.olms.master.entity.checklist.army;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "checklist_scrutiny_qacl", schema = "mdms_army")
public class ChecklistScrutinyQacl implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_scrutiny_qacl_id_seq")
	@SequenceGenerator(name = "checklist_scrutiny_qacl_id_seq", schema = "mdms_army", sequenceName = "checklist_scrutiny_qacl_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "split")
	private Boolean split;

	@Column(name = "sl_no", nullable = false, length = 5)
	private String slNo;

	@Column(name = "reference", length = 300)
	private String reference;

	@Column(name = "obs_type", length = 20)
	private String obsType;

	@Column(name = "part")
	private Integer part;

}
