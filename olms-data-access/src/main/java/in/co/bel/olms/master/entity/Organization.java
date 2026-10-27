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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * @author Manasa b, Awadhesh Verma
 * @author VizagTeam
 * @since 08-12-2022
 */

@Entity
@Table(name = "organization", schema = "mdms")
public class Organization implements Serializable {

	private static final long serialVersionUID = 1213997170751651196L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "organization_id_seq")
	@SequenceGenerator(name="organization_id_seq",schema="mdms",sequenceName="organization_id_seq",allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	

	@Column(name = "organization_name",unique=true)
	@NotNull(message = "Organization name can not be null")
	@Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
	private String organizationName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime", nullable = false)
	private Date modifiedDatetime;
	
	@Column(name = "active")
	private Boolean active;
	
	@Column(name = "last_updated_by")
	private Integer lastUpdatedBy;
	
	@Column(name = "email")
	@NotNull(message = "Email can not be null")
	@Size(min = 2, max = 100, message = "Email must be between 2 and 100 characters")
	private String email;
	
	@Column(name = "mobile_no")
	@NotNull(message = "Mobile Number can not be null")
	@Size(min = 10, max = 10, message = "Mobile Number must be 10 digits")
	private String mobile;

	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "organization_type_id")
	private OrganizationType organizationType;
	
	@Column(name = "std_code", length = 7)
	private String stdCode;

	@Column(name = "inote_code", length = 5)
	private String inoteCode;
	
	@Column(name = "inote_serial_strt")
	private Integer inoteSerialStrt;
	
	@Column(name = "address")
	private String address;

	@Column(name = "phone_no")
	private String phoneNO;

	@Column(name = "fax_no")
	private String faxNo;


	@Column(name = "organization_name_hi")
	private String  organizationNameHi;;

	@Column(name = "address_hi")
	private String addressHi;




	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "parent_org_id")
	private Organization parentOrganization;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "org_wing_id")
	private OrgWing orgWing;

	@Column(name = "logo")
	private byte[] logo;

	public String getOrganizationNameHi() {
		return organizationNameHi;
	}

	public void setOrganizationNameHi(String organizationNameHi) {
		this.organizationNameHi = organizationNameHi;
	}

	public String getPhoneNO() {
		return phoneNO;
	}

	public void setPhoneNO(String phoneNO) {
		this.phoneNO = phoneNO;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}


	public String getAddressHi() {
		return addressHi;
	}

	public void setAddressHi(String addressHi) {
		this.addressHi = addressHi;
	}

	public Integer getId() {
		return id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public Boolean getActive() {
		return active;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public OrganizationType getOrganizationType() {
		return this.organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStdCode() {
		return stdCode;
	}

	public void setStdCode(String stdCode) {
		this.stdCode = stdCode;
	}

	public String getInoteCode() {
		return inoteCode;
	}

	public void setInoteCode(String inoteCode) {
		this.inoteCode = inoteCode;
	}

	public Integer getInoteSerialStrt() {
		return inoteSerialStrt;
	}

	public void setInoteSerialStrt(Integer inoteSerialStrt) {
		this.inoteSerialStrt = inoteSerialStrt;
	}

	public Organization getParentOrganization() {
		return parentOrganization;
	}

	public void setParentOrganization(Organization parentOrganization) {
		this.parentOrganization = parentOrganization;
	}

	public OrgWing getOrgWing() {
		return orgWing;
	}

	public void setOrgWing(OrgWing orgWing) {
		this.orgWing = orgWing;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
}
