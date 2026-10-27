package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the Employee table.
 * 
 * @author Manasa B
 * @since 08-12-2022
 * 
 */

@Entity
@Table(name = "employee", schema = "mdms")
public class Employee implements Serializable {

	private static final long serialVersionUID = -2844370566220417612L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_id_seq")
	@SequenceGenerator(name = "emp_id_seq", schema = "mdms", sequenceName = "emp_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	
	private Integer id;

	@Column(name = "ename", length = 150, nullable = false)
	private String ename;

	@Column(name = "gender", length = 1)
	private Character gender;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDateTime;

	@Column(name = "created_by")
	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDateTime;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@ManyToOne
	@JoinColumn(name = "emp_role_id")
	private EmpRole empRole;
	
	@Column(name="ename_hindi")
	private String enameHindi;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "sbu_id")
	private SalesGroup salesGroup;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="reporting_officer_id")
	private Employee reportingOfficer;
	
	@ManyToOne
	@JoinColumn(name = "designation_id")
	private EmpDesignation empDesignation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_type_id")
	private DepartmentType deptType;
	
	@Column(name="seal_no", length = 10)
	private String sealNo;
	
	@Column(name="active")
	private Boolean active;

	@Column(name = "staff_no")
	private String staffNo;
	
	public Integer getId() {
		return id;
	}

	public String getEname() {
		return ename;
	}

	public Character getGender() {
		return gender;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public EmpRole getEmpRole() {
		return empRole;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setEmpRole(EmpRole empRole) {
		this.empRole = empRole;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public SalesGroup getSalesGroup() {
		return salesGroup;
	}

	public void setSalesGroup(SalesGroup salesGroup) {
		this.salesGroup = salesGroup;
	}

	public Employee getReportingOfficer() {
		return reportingOfficer;
	}

	public void setReportingOfficer(Employee reportingOfficer) {
		this.reportingOfficer = reportingOfficer;
	}

	public String getEnameHindi() {
		return enameHindi;
	}

	public void setEnameHindi(String enameHindi) {
		this.enameHindi = enameHindi;
	}

	public EmpDesignation getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(EmpDesignation empDesignation) {
		this.empDesignation = empDesignation;
	}

	public String getSealNo() {
		return sealNo;
	}

	public void setSealNo(String sealNo) {
		this.sealNo = sealNo;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public DepartmentType getDeptType() {
		return deptType;
	}

	public void setDeptType(DepartmentType deptType) {
		this.deptType = deptType;
	}
	
	public String  getStaffNo() {
		return staffNo;
	}

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
}
