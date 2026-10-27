package in.co.bel.olms.transaction.entity.check.army;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.transaction.entity.SupplyOrder;


/**
 * Checklist remarks for the scrutiny of the Gi Form
 * @author Prateek Dwivedi
 * @since 09-05-2024
 */
@Entity
@Table(name="scrutiny_gi_form", schema="tdms_check_army")
public class ScrutinyGiForm implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_gi_form_id_seq")
	@SequenceGenerator(name = "scrutiny_gi_form_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_gi_form_id_seq", allocationSize = 1)
	@Column (name="id",unique=true,nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_datetime",nullable=false)
	private Date createdDatetime;
	
	@OneToOne
	@JoinColumn(name = "supply_order_id", nullable = false)	
	private SupplyOrder supplyOrder;
	
	
	@Column(name="section_officer_id")
	private Integer sectionOfficerId;
	
	@Column(name="submitted_date")
	private Date submittedDate;
	
	@Column(name="modified_datetime")
	private Date modifiedDatetime;

	@Column(name="last_updated_by")
	private Integer lastUpdatedBy;

	@JsonIgnore
	@OneToMany(mappedBy = "scrutinyGiForm")
	private List<ScrutinyGiFormDet> scrutinyGiFormDets;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}

	public Integer getSectionOfficerId() {
		return sectionOfficerId;
	}

	public void setSectionOfficerId(Integer sectionOfficerId) {
		this.sectionOfficerId = sectionOfficerId;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public List<ScrutinyGiFormDet> getScrutinyGiFormDets() {
		return scrutinyGiFormDets;
	}

	public void setScrutinyGiFormDets(List<ScrutinyGiFormDet> scrutinyGiFormDets) {
		this.scrutinyGiFormDets = scrutinyGiFormDets;
	}
}
