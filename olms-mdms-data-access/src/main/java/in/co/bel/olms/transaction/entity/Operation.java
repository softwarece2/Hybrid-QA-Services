package in.co.bel.olms.transaction.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.DepartmentType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "operation", schema = "tdms")
@Getter
@Setter
public class Operation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "op_no", length = 100, nullable = false)
	private String opNo;

	private String description;
	
	private Boolean isFinal;
	
	private Integer seq;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_type")
	private DepartmentType deptType;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warrant_id")
	private Category warrant;
	
	@CreationTimestamp
	private Date createdDate;
	
}
