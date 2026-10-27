package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* InspectionAuthority entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

@Entity
@Table(name = "inspection_authority", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionAuthority implements Serializable {


	private static final long serialVersionUID = -118416574839598767L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", length = 150, nullable = false)
	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;

}
