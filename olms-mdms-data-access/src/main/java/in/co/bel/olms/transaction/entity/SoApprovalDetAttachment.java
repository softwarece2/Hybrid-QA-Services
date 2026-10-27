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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "so_approval_det_attachment", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoApprovalDetAttachment implements Serializable {
	
	private static final long serialVersionUID = 350290179399100063L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "so_approval_det_attachment_id_seq")
	@SequenceGenerator(name = "so_approval_det_attachment_id_seq", schema = "tdms", sequenceName = "so_approval_det_attachment_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "so_approval_det_id")
	private SoApprovalDetails soApprovalDetails;

	@Column(name = "file_name", nullable = false)
	private String fileName;

}
