package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * The persistent entity class for the inspection_level table.
 * 
 * @author Awadhesh Verma
 * @since 12-04-2023
 */

@Entity
@Table(name = "inspection_level", schema = "mdms")
@Data
public class InspectionLevel implements Serializable {

	private static final long serialVersionUID = 2966189397162131404L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "level_of_inspection", length = 100, nullable = false)
	private String levelOfInspection;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by")
	private Employee modifiedBy;
	
}
