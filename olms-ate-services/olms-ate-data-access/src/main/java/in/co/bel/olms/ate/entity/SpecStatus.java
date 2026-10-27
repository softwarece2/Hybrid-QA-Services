package in.co.bel.olms.ate.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;

@Data
@Entity
@Table(name = "spec_status", schema = "ate")
public class SpecStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spec_id", nullable = false)
	private Spec spec;
	
	@Column(nullable = false)
	private String remark;
	
	@Column(nullable = false)
	private Boolean active;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	private Employee createdBy;
	
	@Column(name = "created_by_name", updatable = false)
	private String createdByNameAndRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column( updatable = false)
	private Date createdAt;
	
}
