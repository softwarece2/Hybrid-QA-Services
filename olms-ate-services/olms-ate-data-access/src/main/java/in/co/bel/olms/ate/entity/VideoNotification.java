package in.co.bel.olms.ate.entity;

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
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.VideoStatus;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "video_notification", schema = "ate")
@Setter
@Getter
public class VideoNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requested_by")
	private Employee requestedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private Employee approvedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	private VideoStatus status;

	@CreationTimestamp
	private Date dateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_id", nullable = false)
	private OfferLetter ol; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_li_id", nullable = false)
	private OlLineItem olLineItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_id", nullable = false)
	private OLLineItemSerialNo dev;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private Test test;
	
	@JsonIgnore
	@OneToMany(mappedBy = "videoNotification", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VideoNotifiedUsers> notifiedUsers;
	
}
