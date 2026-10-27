package in.co.bel.olms.iol.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inspection_stage", schema = "iol")
@Getter
@Setter
public class InspectionStage {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "stage_name")
	private String stageName;

	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	
	
}
