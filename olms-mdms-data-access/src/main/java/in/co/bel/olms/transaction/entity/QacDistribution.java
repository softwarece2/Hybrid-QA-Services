package in.co.bel.olms.transaction.entity;

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

import lombok.Data;

@Data
@Entity
@Table (name="qac_distribution",schema="tdms")
public class QacDistribution {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qac_distribution_seq_id")
	@SequenceGenerator(name = "qac_distribution_seq_id", schema = "tdms", sequenceName = "qac_distribution_seq_id", allocationSize = 1)
	
	@Column(name="id",nullable=false,unique = true)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="inote_id")
	private INote inoteId;
	
	@Column(name="distribution_name")
	private String distributionName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at",nullable = false)
	private Date createdAt;

}
