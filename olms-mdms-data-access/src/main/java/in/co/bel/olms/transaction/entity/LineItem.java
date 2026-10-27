package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LineItem entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2022-08-17
 */

@Entity
@Table(name = "line_item", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItem implements Serializable {

	private static final long serialVersionUID = 3394500124208836506L;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

}