package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the Employee Role table.
 * 
 * @author Manasa B
 * @since 08-12-2022
 */
@Entity
@Table(name = "emp_role", schema = "mdms")
public class EmpRole implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = -7633063408539918157L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="emp_role_id_seq")
	@SequenceGenerator(name="emp_role_id_seq",schema="mdms",sequenceName = "emp_role_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "emp_role", length = 50, nullable = false)
	private String empRole;

	@Column(name = "sec_emp_role_id", length = 50, nullable = false)
	private String secEmpRoleId;
	
	
	@Column(name = "seniority")
	private Integer seniority;
	
	@Column(name = "is_admin", nullable = true)
	private Boolean is_admin;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_type")
	private OrganizationType organizationType;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;
	
	@Column(name="emp_role_hindi")
	private String empRoleHindi;

	
	public boolean isIs_admin() {
		return is_admin;
	}

	public void setIs_admin(boolean is_admin) {
		this.is_admin = is_admin;
	}

	public OrganizationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public Integer getId() {
		return id;
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

	public String getEmpRoleHindi() {
		return empRoleHindi;
	}

	public void setEmpRoleHindi(String empRoleHindi) {
		this.empRoleHindi = empRoleHindi;
	}

	public String getSecEmpRoleId() {
		return secEmpRoleId;
	}

	public void setSecEmpRoleId(String secEmpRoleId) {
		this.secEmpRoleId = secEmpRoleId;
	}
	
	public Integer getSeniority() {
		return seniority;
	}

	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}

	
	
}
