package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "so_part_list", schema = "tdms")
public class SoPartList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2313096891213277527L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "so_part_list_seq_id")
	@SequenceGenerator(name = "so_part_list_seq_id", schema = "tdms", sequenceName = "so_part_list_seq_id", allocationSize = 1)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDateTime;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDateTime;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "part_name", length = 200)
	private String partName;
	
	@Column(name = "line_item_id")
	private Integer lineItemId;

	public Integer getId() {
		return id;
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

	public String getPartName() {
		return partName;
	}

	public Integer getLineItemId() {
		return lineItemId;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public void setLineItemId(Integer lineItemId) {
		this.lineItemId = lineItemId;
	}
	
	
	

}
