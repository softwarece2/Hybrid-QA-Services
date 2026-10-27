package in.co.bel.olms.ate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "units", schema = "ate")
public class Units {
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "units")
	private String uints;

}
