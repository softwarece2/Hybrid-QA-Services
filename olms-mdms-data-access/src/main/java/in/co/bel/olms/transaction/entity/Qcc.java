package in.co.bel.olms.transaction.entity;

import java.math.BigDecimal;
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

import org.hibernate.annotations.CreationTimestamp;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.QccStatus;
import lombok.Data;

@Entity
@Table(name = "qcc", schema = "tdms")
@Data
public class Qcc {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "inote_date")
	private Date inoteDate;

	@Column(name = "nomenclature")
	private String nomenclature;
	
	@Column(name = "reg_no")
	private String regNo;
	
	@Column(name = "qty_tendered")
	private BigDecimal qtyTendered;
	
	@Column(name = "swo_no")
	private String swoNo;
	
	@Column(name = "warrant_no")
	private String warrantNo;
	
	@Column(name = "so_no")
	private String soNo;
	
	@Column(name = "ifd_no")
	private String ifdNo;
	
	@Column(name = "drawing_no")
	private String drawingNo;
	
	@Column(name = "deviation_concession")
	private String deviationConcession;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "ins_date")
	private Date insDate;
	
	@Column(name = "inote_id")
	private Integer inoteId;
	
	@Column(name = "inote_no")
	private String inoteNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private Employee createdBy;  // HOSA generates the QCC and notifies DOA
	
	@CreationTimestamp
	private Date createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_approved_by_id")
	private Employee prodApprovedBy;  // DO Assembly (for Prod)
	
	private Date prodApprovedAt;
	
	private String prodApprovedByDept;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qc_approved_by_id")
	private Employee qcApprovedBy;   // DO QC (for Insp)
	
	private Date qcApprovedAt;
	
	private String qcApprovedByDept;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qcc_status")
	private QccStatus qccStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merged_sslip_id")
	private MergeSubSlip mergedSubSlip;
	
	@Column(name = "merged_sslip_no")
	private String mergedSslipNo;
}
