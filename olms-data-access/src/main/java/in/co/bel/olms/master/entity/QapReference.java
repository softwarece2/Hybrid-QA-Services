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

import lombok.Data;

/**
 * The persistent entity class for the qap_reference table.
 * 
 * @author Awadhesh Verma
 * @since 17-04-2023
 */

@Entity
@Table(name = "qap_reference", schema = "mdms")
@Data
public class QapReference implements Serializable {

	private static final long serialVersionUID = 2078420815981496859L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "system_name", length = 200, nullable = false)
	private String systemName;
	
	@Column(name = "qap_no", length = 200)
	private String qapNo;
	
	@Column(name = "approval_letter_ref", length = 200)
	private String approvalLetterRef;
	
	@Column(name = "remarks", length = 200)
	private String remarks;
	
	@Column(name = "rack")
	private Integer rack;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by")
	private Employee modifiedBy;
	
	@Column(name = "status")
	private Boolean status;
	
}
