//package in.co.bel.olms.transaction.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//import org.hibernate.annotations.CreationTimestamp;
//
//import in.co.bel.olms.master.entity.Employee;
//import lombok.Getter;
//import lombok.Setter;
//
///**
// * @author Shridhar R
// * @since 02-01-2026
// */
//
//@Entity
//@Setter
//@Getter
//@Table(name = "merge_inote", schema = "tdms")
//public class MergeINote {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merge_inote_seq")
//	@SequenceGenerator(name = "merge_inote_seq", schema = "tdms", sequenceName = "merge_inote_seq", allocationSize = 1)
//	@Column(name = "id", unique = true, nullable = false)
//	private Integer id;
//
//	@Column(name = "merge_inote_no")
//	private String mergeINoteNo;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "created_by", updatable = false)
//	private Employee createdBy;
//	
//	@CreationTimestamp
//	@Column(updatable = false)
//	private Date createdAt;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "modified_by")
//	private Employee modifiedBy;
//	
//	@Column(name = "modified_at")
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date updatedAt;
//	
//	private String status;
//	
//	private Boolean isNotified;
//	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "notified_to")
//	private Employee notifiedTo;
//	
//
//	
//	
//}
