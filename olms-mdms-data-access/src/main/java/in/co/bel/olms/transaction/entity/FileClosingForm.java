package in.co.bel.olms.transaction.entity;

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

@Entity
@Table(name="file_closing_form",schema="tdms")

public class FileClosingForm implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5845224186171288562L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_closing_form_id_seq")
	@SequenceGenerator(name = "file_closing_form_id_seq", schema = "tdms", sequenceName = "file_closing_form_id_seq", allocationSize = 1)

	@Column(name="id",unique=true, nullable=false)
	private Integer id;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "supply_order_id")
	private SupplyOrder supplyOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDatetime;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="modified_by")
	private Integer modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "fileClosingForm")
	private List<FileClosingFormDet> fileclosingFormDets;
	public List<FileClosingFormDet> getfileclosingFormDets() {
		return fileclosingFormDets;
	}

	public void setfileclosingFormDets(List<FileClosingFormDet> fileclosingFormDets) {
		this.fileclosingFormDets = fileclosingFormDets;
	}
	
	public SupplyOrder getSupplyOrder() {
		return this.supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime", nullable = false)
	private Date modifiedDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
