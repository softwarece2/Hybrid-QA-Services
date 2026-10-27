package in.co.bel.olms.transaction.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.SoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* OlApprovalDetails entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-02-21
*/

@Entity
@Table(name = "so_approval_details", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoApprovalDetails implements Serializable {
	
	private static final long serialVersionUID = 350290179399100063L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "so_approval_details_id_seq")
	@SequenceGenerator(name = "so_approval_details_id_seq", schema = "tdms", sequenceName = "so_approval_details_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "remarks", length = 1000, nullable = false)
	private String remarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;
	
	
	@JsonIgnore
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
	private SoStatus soStatus;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supply_order_id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_so_approval_id")
	private SoApprovalDetails parentSoApprovalDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "next_approver_id")
	private Employee nextApprover;
	
	@Column(name = "qao_remarks", length = 1000, nullable = false)
	private String qaoRemarks;
	
}
