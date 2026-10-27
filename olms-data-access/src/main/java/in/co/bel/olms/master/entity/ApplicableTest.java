package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**oll
* ApplicableTest entity class
*
* @author Awadhesh Verma
* @version 1.0
* @since   17-10-2023
*/

@Entity
@Table(name = "applicable_test", schema = "mdms")
@Data
@NoArgsConstructor
public class ApplicableTest implements Serializable{

	private static final long serialVersionUID = -934600628779571914L;

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator = "applicable_test_id_seq")
	@SequenceGenerator(name="applicable_test_id_seq",schema="mdms",sequenceName="applicable_test_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "test_name", nullable = false, length = 200)
	@Size(min = 2, max = 100, message = "Name name must be between 2 and 100 characters")
	private String testName;

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
	
	@Column(name = "active")
	private Boolean active;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sales_group_id" )
	private SalesGroup salesGroup;

}
