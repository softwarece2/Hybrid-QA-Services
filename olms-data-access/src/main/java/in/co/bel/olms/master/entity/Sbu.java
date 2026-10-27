package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Manasa B
 * @since 08-12-2022
 */
@Entity
@Table(name = "sbu", schema = "mdms")
public class Sbu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6911477106439946567L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sbu_id_seq")
	@SequenceGenerator(name="sbu_id_seq",schema="mdms",sequenceName = "sbu_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "sbu_code", nullable = false)
	private Integer sbuCode;

	@Column(name = "sbu_name", nullable = false)
	private String sbuName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Column(name = "created_by")
	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "active", nullable = false)
	private Boolean active;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JoinColumn(name = "organization_id")
	private Organization organization;

	public Integer getId() {
		return id;
	}

	public Integer getSbuCode() {
		return sbuCode;
	}

	public String getSbuName() {
		return sbuName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSbuCode(Integer sbuCode) {
		this.sbuCode = sbuCode;
	}

	public void setSbuName(String sbuName) {
		this.sbuName = sbuName;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public Boolean getActive() {
		return active;
	}

	
	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
