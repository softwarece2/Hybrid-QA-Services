package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.co.bel.olms.master.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "line_item_observation_file", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemObservationFile implements Serializable {
	
	private static final long serialVersionUID = 1940072711511468897L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_item_observation_file_id_seq")
	@SequenceGenerator(name = "line_item_observation_file_id_seq", schema = "tdms", sequenceName = "line_item_observation_file_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "created_by")
	private Employee createdBy;
	
	@Column(name="remarks")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name = "line_item_obs_id")
	private LineItemObservation lineItemObservation;
	
//	@JsonIgnore
	@OneToMany(mappedBy = "lineItemObservationFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<LineItemObservationAttachment> lineItemObservationAttachments;
	
	@ManyToOne
	@JoinColumn(name="send_to")
	private Employee sendTo;
	
	
}
