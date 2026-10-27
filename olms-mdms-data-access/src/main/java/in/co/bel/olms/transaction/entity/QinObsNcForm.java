package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;

/**
 * QinObsNcForm Entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-03-27
 */

@Entity
@Table(name = "qin_obs_nc_form", schema = "tdms")
public class QinObsNcForm implements Serializable {

	private static final long serialVersionUID = 5039028773894440402L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qin_obs_nc_form_id_seq")
	@SequenceGenerator(name = "qin_obs_nc_form_id_seq", schema = "tdms", sequenceName = "qin_obs_nc_form_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "offer_letter_id", nullable = false)
	private OfferLetter offerLetter;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private Employee createdBy;

	@ManyToOne
	@JoinColumn(name = "modified_by")
	private Employee modifiedBy;
	
	@Column(name = "is_rework_submitted")
	private Boolean isReworkSubmitted;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "qinObsNcForm", cascade = CascadeType.ALL)
	private List<QinObsNcFormDet> qinObsNcFormDets;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Employee getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}

	public Employee getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Employee modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<QinObsNcFormDet> getQinObsNcFormDets() {
		return qinObsNcFormDets;
	}

	public void setQinObsNcFormDets(List<QinObsNcFormDet> qinObsNcFormDets) {
		this.qinObsNcFormDets = qinObsNcFormDets;
	}

	public Boolean getIsReworkSubmitted() {
		return isReworkSubmitted;
	}

	public void setIsReworkSubmitted(Boolean isReworkSubmitted) {
		this.isReworkSubmitted = isReworkSubmitted;
	}

}
