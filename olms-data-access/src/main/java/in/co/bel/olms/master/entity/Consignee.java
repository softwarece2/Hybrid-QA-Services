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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Consignee entity class
*
* @author Awadhesh Verma
* @author Manasa B
* @version 1.0
* @since   21-10-2022
*/

@Entity
@Table(name = "consignee", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consignee implements Serializable{


	private static final long serialVersionUID = 1940072711511468897L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	@Size(min = 2, max = 100, message = "Name name must be between 2 and 100 characters")
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
	
	
	@Column(name = "fax")
	private String fax;
	
	@Column(name = "phone")	
	@Size(min = 10, max = 10, message = "Phone Number must be 10 digits")
	private String phone;	
	
	@Size(max = 300, message = "Address cannot exceed 300 characters")
	@Pattern(regexp =  "^[a-zA-Z0-9\\s.,'#\\-/()&]+$", message = "Invalid address format")
	@Column(name = "address")
	private String address;	
	
	@Column(name = "active")
	private Boolean active;

}


