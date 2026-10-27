package in.co.bel.olms.iol.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fai_protocol", schema = "iol")
@Getter
@Setter
public class FaiProtocol {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fai_protocol_id_seq")
	@SequenceGenerator(name = "fai_protocol_id_seq", schema = "iol", sequenceName = "fai_protocol_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "sl_no")
	private Integer slNo;

	@Column(name = "param_code")
	private Integer paramCode;

	@Column(name = "main_type", length = 50)
	private String mainType;

	@Column(name = "param_type", length = 100, nullable = false)
	private String paramType;

	@Column(name = "param_desc", length = 250)
	private String paramDesc;

	@Column(name = "ty_clause_no", length = 100)
	private String tyClauseNo;

	@Column(name = "min")
	private Double min;

	@Column(name = "max")
	private Double max;

	@Column(name = "unit", length = 20)
	private String unit;

	@Column(name = "test_by", length = 100)
	private String testBy;

	@Column(name = "duration")
	private Integer duration;

	@ManyToOne
	@JoinColumn(name = "applicable_test_id")
	private LineItemApplicableTest lineItemApplTest;
}
