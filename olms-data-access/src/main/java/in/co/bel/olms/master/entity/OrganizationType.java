package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the organization_type database table.
 * @author Manasa B
 * @since 08-12-2022
 */
@Entity
@Table(name="organization_type", schema = "mdms")
public class OrganizationType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Boolean active;

	@Temporal(TemporalType.DATE)
	@Column(name="created_datetime")
	private Date createdDatetime;

	@Column(name="modified_by")
	private Integer modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="modified_datetime")
	private Date modifiedDatetime;

	@Column(name="type_name")
	private String typeName;

	//bi-directional many-to-one association to Organization
//	@OneToMany(mappedBy="organizationType")
//	private Set<Organization> organizations;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organizationType")
	private Set<EmpRole> roles = new HashSet<EmpRole>(0);

	private Integer level;
	
	public OrganizationType(int id, String typeName, Date createdDatetime, Date modifiedDatetime, Boolean active,
			Integer modifiedBy, Set<EmpRole> roles, 
			//Set<Organization> organizations,
			int level) {
		super();
		this.id = id;
		this.typeName = typeName;
		this.createdDatetime = createdDatetime;
		this.modifiedDatetime = modifiedDatetime;
		this.active = active;
		this.modifiedBy = modifiedBy;
		this.roles = roles;
		//this.organizations = organizations;
		this.level = level;
	}


	public OrganizationType() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedDatetime() {
		return this.createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Integer getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDatetime() {
		return this.modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

//	public Set<Organization> getOrganizations() {
//		return this.organizations;
//	}
//
//	public void setOrganizations(Set<Organization> organizations) {
//		this.organizations = organizations;
//	}

//	public Organization addOrganization(Organization organization) {
//		getOrganizations().add(organization);
//		organization.setOrganizationType(this);
//
//		return organization;
//	}
//
//	public Organization removeOrganization(Organization organization) {
//		getOrganizations().remove(organization);
//		organization.setOrganizationType(null);
//
//		return organization;
//	}
	
	
	public Set<EmpRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<EmpRole> roles) {
		this.roles = roles;
	}
	

	@Column(name = "level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	

}