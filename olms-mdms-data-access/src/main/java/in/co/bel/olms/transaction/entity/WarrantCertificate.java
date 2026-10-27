package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.WarrantType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "warrant_certificate", schema = "tdms")
public class WarrantCertificate implements Serializable {

	private static final long serialVersionUID = -2313096891213277527L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;

	private String certificateNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column(name="start_date")
    private LocalDate startDate;

	@Column(name="end_date")
    private LocalDate endDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="warrant_type")
	private WarrantType warrantType;

    private Integer inoteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authority_id")
	private Employee authority;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="approval_date")
	private Date approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doa_approver_id")
    private Employee doaApprover;

    private Boolean isDoaApproved;
    
    private Integer warrantyPeriodQty;

}
