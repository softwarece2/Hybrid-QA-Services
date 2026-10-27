package in.co.bel.olms.iol.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "insp_stage_mapping", schema = "iol")
public class InspStageMapping implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_stage_mapping_id_seq")
	@SequenceGenerator(name = "insp_stage_mapping_id_seq", schema = "iol", sequenceName = "insp_stage_mapping_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "applicable_test_id")
	private LineItemApplicableTest applicableTest;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

}
