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
import in.co.bel.olms.master.entity.OlStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* OlApprovalDetails entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-02-18
*/

@Getter
@Setter
@Entity
@Table(name = "ol_approval_details", schema = "tdms")
@NoArgsConstructor
@AllArgsConstructor
public class OlApprovalDetails implements Serializable {
	
	private static final long serialVersionUID = 350290179399100063L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_approval_details_id_seq")
	@SequenceGenerator(name = "ol_approval_details_id_seq", schema = "tdms", sequenceName = "ol_approval_details_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "remarks", nullable = false)
	private String remarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by")
	private Employee modifiedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private Employee approvedBy;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status")
	private OlStatus olStatus;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_letter_id")
	private OfferLetter offerLetter;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_ol_approval_id")
	private OlApprovalDetails parentOlApprovalDetails;
	
	@Column(name = "note_no", length = 20)
	private String noteNo;
	
	
	@Column(name = "emp_desgn")
	private String designation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "next_approver_id")
	private Employee nextApprover;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "olApprovalDetails")
	private List<OlApprovalUploads> olApprovalUploads;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_note_id")
	private InspectionFileNote inspectionFileNote;

	@Column(name = "is_rfr")
	private Boolean isRfr;
}
