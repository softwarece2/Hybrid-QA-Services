package in.co.bel.olms.master.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "department", schema = "mdms")
public class Department implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dept_id_seq")
    @SequenceGenerator(name = "dept_id_seq", schema = "mdms", sequenceName = "dept_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "department_name")
    private String departmentName;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_grp_id")
    private SalesGroup salesGroup;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_type", nullable = false)
    private DepartmentType departmentType;
    
    @Column(name = "section_code")
    private Integer sectionCode;
    
}
