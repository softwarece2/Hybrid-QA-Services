package in.co.bel.olms.master.entity;

import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;

import lombok.Data;


@Data
@Entity
@Table(name = "serial_no_status", schema = "mdms")
public class SerialNoStatus {
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "serial_no_status_seq")
//	@SequenceGenerator(name = "serial_no_status_seq", schema = "tdms", sequenceName = "serial_no_status_seq", allocationSize = 1)
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="status")
	private String status;
	
}
