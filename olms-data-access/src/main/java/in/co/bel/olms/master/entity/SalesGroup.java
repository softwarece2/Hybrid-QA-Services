package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* SalesGroup entity class
* This is used as sbu also
*
* @author  Awadhesh Verma
* @author Manasa B
* @version 1.0
* @since   2022-12-27 
*/
@Entity
@Table(name = "sales_group", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesGroup implements Serializable {
	
	private static final long serialVersionUID = 9185568432608533877L;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
//	private Integer id;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sales_group_id_seq")
	@SequenceGenerator(name="sales_group_id_seq",schema="mdms",sequenceName="sales_group_id_seq",allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "name")
	@NotNull(message = "Name can not be null")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s&(),/\\-]+$", message = "Only alphanumeric characters and symbols (&, (, ), ,, /, -) are allowed")
	private String name;

	@Column(name = "code", nullable = false)
	private String code;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "active", nullable = false)
	private Boolean active;

	// bi-directional many-to-one association to Organization
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "organization_id")
	private Organization organization;
	

	@Column(name = "address")
	private String address;
	
	
	public Date getcreatedDate() {
		return createdDate;
	}
	
	public void setcreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
