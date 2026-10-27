package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * QinObsNcFormDet Entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-03-27
 */

@Entity
@Table(name = "qin_obs_nc_form_det", schema = "tdms")
public class QinObsNcFormDet implements Serializable {

	private static final long serialVersionUID = -657183304448479713L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qin_obs_nc_form_det_id_seq")
	@SequenceGenerator(name = "qin_obs_nc_form_det_id_seq", schema = "tdms", sequenceName = "qin_obs_nc_form_det_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "observation")
	private String observation;
	
	@Column(name = "rework_action")
	private String reworkAction;
	
	@Column(name = "remarks")
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name ="ol_line_item_id", nullable = false)
	private OlLineItem olLineItem;
	
	@ManyToOne
	@JoinColumn(name="qin_obs_nc_form_id", nullable = false)
	private QinObsNcForm qinObsNcForm;
	
	@Column(name = "closure_status")
	private Boolean closureStatus;


	@Column(name = "subpartno", length = 25)
	private String subPartNo;
	
	
	public String getSubPartNo() {
		return subPartNo;
	}
	
	public void setSubPartNo(String subPartNo) {
		this.subPartNo=subPartNo;
		}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getReworkAction() {
		return reworkAction;
	}

	public void setReworkAction(String reworkAction) {
		this.reworkAction = reworkAction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public OlLineItem getOlLineItem() {
		return olLineItem;
	}

	public void setOlLineItem(OlLineItem olLineItem) {
		this.olLineItem = olLineItem;
	}

	public QinObsNcForm getQinObsNcForm() {
		return qinObsNcForm;
	}

	public void setQinObsNcForm(QinObsNcForm qinObsNcForm) {
		this.qinObsNcForm = qinObsNcForm;
	}

	public Boolean getClosureStatus() {
		return closureStatus;
	}

	public void setClosureStatus(Boolean closureStatus) {
		this.closureStatus = closureStatus;
	}
	
}
