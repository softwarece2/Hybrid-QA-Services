package in.co.bel.olms.transaction.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* OlApprovalUploads entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-02-18
*/

@Entity
@Table(name = "ol_approval_uploads", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OlApprovalUploads implements Serializable {

	private static final long serialVersionUID = -4429423829510651652L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_approval_uploads_id_seq")
	@SequenceGenerator(name = "ol_approval_uploads_id_seq", schema = "tdms", sequenceName = "ol_approval_uploads_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "file_name", length = 200, nullable = false)
	private String fileName;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_approval_id")
	private OlApprovalDetails olApprovalDetails;

}
