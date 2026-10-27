package in.co.bel.olms.transaction.entity.check.army;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import lombok.Data;

@Data
@Entity
@Table(name = "scrutiny_tender", schema = "tdms_check_army")
public class ScrutinyTender implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_tender_seq")
	@SequenceGenerator(name = "scrutiny_tender_seq", schema = "tdms_check_army", sequenceName = "scrutiny_tender_id_seq", allocationSize = 1)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDatetime;

	@OneToOne
	@JoinColumn(name = "supply_order_id", nullable = false)
	private SupplyOrder supplyOrder;

	@Column(name = "section_officer_id")
	private Integer sectionOfficerId;

	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDatetime;

	@JsonIgnore
	@OneToMany(mappedBy = "scrutinyTender")
	private List<ScrutinyTenderDet> scrutinyTenderDets;

}
