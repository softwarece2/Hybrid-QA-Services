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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;



@Entity
@Table(name = "covering_letter", schema = "tdms")
@Data

public class CoveringLetter  implements Serializable {
	private static final long serialVersionUID = 7720211000881066364L;
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
		private Integer id;
		
		@JsonIgnore
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "file_note_id", nullable = false)
		private InspectionFileNote fileNote;
		
		@Temporal(TemporalType.DATE)
		@Column(name = "covering_date", nullable = false)
		private Date coveringDate;
		
		@Temporal(TemporalType.DATE)
		@Column(name = "created_date", nullable = false)
		private Date createdDate;

		@Column(name = "addressed_to", length = 500)
		private String addressedTo;
		
		@Column(name = "copy_to", length = 1000)
		private String copyTo;
		
		
		@JsonIgnore
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="created_by")
		private Employee createdBy;

		@JsonIgnore
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="signatory")
		private Employee signatory;

		@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "coveringLetter")
		private List<CoveringLetterPoint> coveringLetterPoints;
		
	

}





