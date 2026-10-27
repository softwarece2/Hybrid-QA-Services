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
@Table(name = "ate_test", schema = "iol")
@Getter
@Setter
public class AteTest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ate_test_id_seq")
	@SequenceGenerator(name = "ate_test_id_seq", schema = "iol", sequenceName = "ate_test_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "applicable_test_id")
	private LineItemApplicableTest lineItemApplTest;
}
