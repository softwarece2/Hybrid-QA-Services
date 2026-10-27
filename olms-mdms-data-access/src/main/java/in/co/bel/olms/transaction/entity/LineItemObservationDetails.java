package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "line_item_observation_details", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItemObservationDetails implements Serializable {
	
	private static final long serialVersionUID = 1940072711511468897L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_item_observation_details_id_seq")
	@SequenceGenerator(name = "line_item_observation_details_id_seq", schema = "tdms", sequenceName = "line_item_observation_details_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "line_item_obs_id")
	private LineItemObservation lineItemObservation;
	
	@ManyToOne
	@JoinColumn(name = "so_line_item_id")
	private SOLineItem soLineItem;
	
	@Column(name = "serial_no")
	private String serialNo;
	
	@Column(name = "remark")
	private String remarks;	

}
