package in.co.bel.olms.ate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "spec_test_type", schema = "ate")
public class SpecTestType implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spec_test_type_id_seq")
	@SequenceGenerator(name = "spec_test_type_id_seq", schema = "ate", sequenceName = "spec_test_type_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "test_type")
	private String testType;
}

