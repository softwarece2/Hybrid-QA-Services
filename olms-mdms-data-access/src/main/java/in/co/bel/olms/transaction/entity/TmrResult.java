package in.co.bel.olms.transaction.entity;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tmr_result", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmrResult {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tmr_result_id_seq")
	@SequenceGenerator(name = "tmr_result_id_seq", schema = "tdms", sequenceName = "tmr_result_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "parameter")
	private String parameter;

	@Column(name = "exp_min")
	private Double expectedMin;

	@Column(name = "exp_max")
	private Double expectedMax;

	@Column(name = "result")
	private String result;

	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "created_by")
	private String createdBy;
	

	@Column(name = "selected_datetime")
	private Date selectedDatetime;
	
	@Column(name = "unit")
	private String unit;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;

	@ManyToOne
	@JoinColumn(name = "ol_line_item_id")
	private OlLineItem olLineItem;

	@ManyToOne
	@JoinColumn(name = "app_test_id")
	private LineItemApplicableTest appTest;
}
