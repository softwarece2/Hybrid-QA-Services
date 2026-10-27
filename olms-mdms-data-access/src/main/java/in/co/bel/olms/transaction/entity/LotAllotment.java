package in.co.bel.olms.transaction.entity;

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
import lombok.Data;

@Data
@Entity
@Table(name = "lot_allotment", schema = "tdms")
public class LotAllotment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="merge_sslip_id", nullable=false)
	private MergeSubSlip mergeSubSlip ; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="alloted_by", nullable=false)
	private Employee allotedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="alloted_to", nullable=false)
	private Employee allotedTo;
	
	@CreationTimestamp
	private Date createdAt;


}
