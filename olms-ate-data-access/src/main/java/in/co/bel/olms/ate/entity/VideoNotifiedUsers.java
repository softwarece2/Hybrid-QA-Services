package in.co.bel.olms.ate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.VideoStatus;
import lombok.Data;

@Data
@Entity
@Table(name= "video_notified_users", schema = "ate")
public class VideoNotifiedUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_noti_id", nullable = false)
	private VideoNotification videoNotification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notified_user", nullable = false)
	private Employee notifiedUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	private VideoStatus status;
	
}