package in.co.bel.olms.transaction.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;


@Entity
@Table(name = "aol_aod", schema = "tdms")
@Data
public class AolAod implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8093823147950078844L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="aol_aod_id_seq")
    @SequenceGenerator(name="aol_aod_id_seq",schema="tdms",sequenceName="aol_aod_id_seq",allocationSize=1)
    @Column(name="id")
	private Integer id;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aol_emp_id")
	private Employee aolEmp;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asn_emp_id")
	private Employee asnEmp;
	
	@Column(name = "asn_desg")
	private String asnDesg;
	
	@Column(name = "type")
	private String type;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "from_date")
	private Date fromDate;	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "to_date")
	private Date toDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Column(name = "created_by")
	private Integer createdBy;
	
	



}
