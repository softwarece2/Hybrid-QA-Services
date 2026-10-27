
package in.co.bel.olms.transaction.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "merge_sslip_mapping", schema="tdms")
public class MergeSubSlipMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "merge_sslip_mapping_seq")
	@SequenceGenerator(name = "merge_sslip_mapping_seq", schema = "tdms", sequenceName = "merge_sslip_mapping_seq", allocationSize = 1)
	@Column(nullable = false, unique = true)
	private Integer id;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sslip_id")
	private SubmissionSlip subSlip;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merge_sslip_id")
	private MergeSubSlip mergeOlNoId;
	
	@Column(name = "is_merged")
	private Boolean isMerged;
	
	private String status;
	
	
		
}
