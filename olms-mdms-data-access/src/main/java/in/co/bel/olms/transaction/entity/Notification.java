package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification", schema = "tdms")
public class Notification implements Serializable {

	private static final long serialVersionUID = 6651967335564687518L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_id_seq")
	@SequenceGenerator(name = "notifications_id_seq", schema = "tdms", sequenceName = "notifications_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "notification_name", length = 250)
	private String notificationName;

	@Column(name = "created_datetime", nullable = false)
	private Date createdDateTime;

	@Column(name = "read_status")
	private boolean readStatus;

	@Column(name = "read_time")
	private Date readTime;

	@Column(name = "category_id", nullable = false)
	private Integer categoryId;

	@Column(name = "read_by")
	private Integer readBy;
	
	@ManyToOne
	@JoinColumn(name = "sender")
	private Employee sender;
	
	@ManyToOne
	@JoinColumn(name = "receiver")
	private Employee receiver;

	@Column(name = "notification_category", length = 100)
	private String notificationCategory;

	
	@Column(name = "sbu_name", length = 100)
	private String sbuName;
	
	private Boolean isLatest;
}
