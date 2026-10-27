package in.co.bel.olms.iol.entity;

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
@Table(name = "test_type", schema = "iol")
public class TestType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_type_id_seq")
	@SequenceGenerator(name = "test_type_id_seq", schema = "iol", sequenceName = "test_type_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "test_type")
	private String testType;
	
}
