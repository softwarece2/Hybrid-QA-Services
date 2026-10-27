package in.co.bel.olms.ate.entity;

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
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubmissionSlip;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import lombok.Data;

@Data
@Entity
@Table(name= "video_records_dtl", schema = "ate")
public class VideoRecords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ol_id")
	private OfferLetter ol; 
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ol_li_id")
	private OlLineItem olLineItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_id")
	private OLLineItemSerialNo dev;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private Test test;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", nullable = false)
	private Employee createdBy;

	@CreationTimestamp
	private Date createdAt;
	
	private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_slip_id")
    private SubmissionSlip sSlip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_slip_li_id")
    private SubSlipLineItem sSlipLineItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_slip_serial_no_id")
    private SubSlipSerialNo sSlipSerialNo;

}