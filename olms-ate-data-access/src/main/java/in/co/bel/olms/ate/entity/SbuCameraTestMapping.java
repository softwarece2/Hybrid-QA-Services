package in.co.bel.olms.ate.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.SbuCameraIpMapping;
//import in.co.bel.olms.master.entity.SbuCameraIpMapping;
import lombok.Data;


@Data
@Entity
@Table(name = "sbu_camera_test_mapping", schema = "ate")
public class SbuCameraTestMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sbu_camera_test_mapping_id_seq")
	@SequenceGenerator(name = "sbu_camera_test_mapping_id_seq", schema = "ate", sequenceName = "sbu_camera_test_mapping_id_seq", allocationSize = 1)
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="test_id")
	private Test testId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="cam_id")
	private SbuCameraIpMapping camId;
	
	
	

}
