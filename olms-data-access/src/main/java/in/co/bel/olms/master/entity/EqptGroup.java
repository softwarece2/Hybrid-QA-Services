package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "eqpt_group", schema = "mdms")

@NoArgsConstructor
@AllArgsConstructor
public class EqptGroup implements Serializable {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 6703038347112788763L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
private Integer id;

@Column(name = "group", nullable = false)
private String group;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getGroup() {
	return group;
}

public void setGroup(String group) {
	this.group = group;
}


}
