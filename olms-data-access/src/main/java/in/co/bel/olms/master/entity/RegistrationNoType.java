package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="registration_no_type", schema = "mdms")
public class RegistrationNoType implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;

	@Column(name="registration_no_type")
	private String regType;
}
