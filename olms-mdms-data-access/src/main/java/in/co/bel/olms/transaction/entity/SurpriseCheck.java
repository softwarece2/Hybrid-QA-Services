package in.co.bel.olms.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.co.bel.olms.master.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * SurpriseCheck entity class
 *
 * @author Awadhesh Verma
 * @since 2023-08-26
 */

@Entity
@Table(name = "surprise_check", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurpriseCheck implements Serializable {

	private static final long serialVersionUID = -1755720529542281391L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;

	@Column(name = "individual_responsible", length = 200)
	private String individualResponsible;

	@Column(name = "test_carried", length = 500)
	private String testCarried;

	@Column(name = "cross_verify_by", length = 200)
	private String crossVerifyBy;

	@Column(length = 200)
	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name = "cross_verify_date")
	private Date crossVerifyDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "inspection_date")
	private Date inspectionDate;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "line_item_id")
	private SOLineItem soLineItem;

}
