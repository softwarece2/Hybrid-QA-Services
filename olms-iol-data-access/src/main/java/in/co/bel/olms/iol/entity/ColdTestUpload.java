package in.co.bel.olms.iol.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cold_test_upload", schema = "iol")
public class ColdTestUpload implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cold_test_upload_id_seq")
	@SequenceGenerator(name = "cold_test_upload_id_seq", schema = "iol", sequenceName = "cold_test_upload_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cold_test_id")
	private ColdTest coldTest;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDateTime;
	
	@JsonIgnore
	@OneToMany(mappedBy = "coldTestFileUpload")
	private List<ColdTestUploadMapping> coldTestUploadMappings;

}
