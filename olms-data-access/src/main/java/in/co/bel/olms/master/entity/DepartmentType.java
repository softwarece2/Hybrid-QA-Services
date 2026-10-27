package in.co.bel.olms.master.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name= "department_type", schema = "mdms")

public class DepartmentType {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_type_id_seq")
	@SequenceGenerator(name = "department_type_id_seq", schema = "mdms", sequenceName = "department_type_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column
	private String departmentType;
	
	@Column
	private Boolean isApprovalCycle;

}
