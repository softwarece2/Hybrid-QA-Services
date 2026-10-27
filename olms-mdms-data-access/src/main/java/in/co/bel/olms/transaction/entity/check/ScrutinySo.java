package in.co.bel.olms.transaction.entity.check;

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
 * Checklist header for the scrutiny of the supply order
 * @author Manasa B
 * @since 09-01-2023
 */

@Entity
@Table(name="scrutiny_so",schema="tdms_check")
public class ScrutinySo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1079489416350336331L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_so_id_seq")
	@SequenceGenerator(name = "scrutiny_so_id_seq", schema = "tdms_check", sequenceName = "scrutiny_so_id_seq", allocationSize = 1)

	@Column (name="id",unique=true,nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_datetime",nullable=false)
	private Date createdDatetime;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date")
	private Date submittedDatetime;
	
	@OneToOne
	@JoinColumn(name = "supply_order_id", nullable = false)	
	private SupplyOrder supplyOrder;
	
	
	@Column(name="section_officer_id")
	private Integer sectionOfficerId;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "scrutinySo")
	private List<ScrutinySoDet> scrutinySoDets;
	
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

	public List<ScrutinySoDet> getScrutinySoDets() {
		return scrutinySoDets;
	}

	public void setScrutinySoDets(List<ScrutinySoDet> scrutinySoDets) {
		this.scrutinySoDets = scrutinySoDets;
	}

	public Date getSubmittedDatetime() {
		return submittedDatetime;
	}

	public void setSubmittedDatetime(Date submittedDatetime) {
		this.submittedDatetime = submittedDatetime;
	}
	
	

}
