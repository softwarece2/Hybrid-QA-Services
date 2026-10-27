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
 * Indenter entity class
 *
 * @author Awadhesh Verma
 * @author Manasa B
 * @version 1.0
 * @since 24-11-2022
 */

@Entity
@Table(name = "indenter", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Indenter implements Serializable {

	private static final long serialVersionUID = 7493494257319599537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", nullable = false )
//	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	private String name;
	
	@Column(name = "short_name" )
//	@Size(min = 2, max = 100, message = "Shortname must be between 2 and 100 characters")
	private String shortName;
	
//	@Size(max = 255, message = "Address cannot exceed 255 characters")
//	@Pattern(regexp = "^[a-zA-Z0-9\\s.,'-]+$", message = "Invalid address format")
	@Column(name = "address" )
	private String address;	

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

	@Column(name="active")
	private Boolean active;
	
	
}
