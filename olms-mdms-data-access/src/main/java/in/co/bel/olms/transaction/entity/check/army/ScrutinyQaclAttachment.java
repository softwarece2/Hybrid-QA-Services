package in.co.bel.olms.transaction.entity.check.army;

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
@Table(name = "scrutiny_qacl_attachment", schema = "tdms_check_army")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrutinyQaclAttachment implements Serializable {
	
	private static final long serialVersionUID = 350290179399100063L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_qacl_attachment_id_seq")
	@SequenceGenerator(name = "scrutiny_qacl_attachment_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_qacl_attachment_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "scrutiny_qacl_id")
	private ScrutinyQacl scrutinyQacl;

	@Column(name = "file_name", nullable = false)
	private String fileName;
}
