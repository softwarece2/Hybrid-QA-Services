package in.co.bel.olms.master.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sbu_camera_ip_mapping", schema = "mdms")
public class SbuCameraIpMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sbu_camera_ip_mapping_id_seq")
	@SequenceGenerator(name = "sbu_camera_ip_mapping_id_seq", schema = "mdms", sequenceName = "sbu_camera_ip_mapping_id_seq", allocationSize = 1)
	@Column(name="id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="camera_ip")
	private String cameraIp;
	
	@Column(name="sbu_name")
	private String sbuName;
	
	@Column(name="sales_grp_id")
	private Integer salesGrpId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_datetime")
	private Date createdDatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modified_datetime")
	private Date modifiedDatetime;
}
