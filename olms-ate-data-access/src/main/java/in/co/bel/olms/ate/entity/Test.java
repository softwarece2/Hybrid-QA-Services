package in.co.bel.olms.ate.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "test", schema = "ate")
public class Test implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "test_desc")
	private String testDesc;
	
	@Column(name = "is_fai")
	private Boolean isFai;
	
	@JsonIgnore
	@OneToMany(mappedBy = "test")
	private List<Spec> specs;
	
}
