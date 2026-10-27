package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "amendments_letter", schema = "tdms")
@Data
public class AmendmentsLetter implements Serializable{
	
	/**
	 * 	
	 */
	private static final long serialVersionUID = -8093823147950078844L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="amendments_letter_id_seq")
    @SequenceGenerator(name="amendments_letter_id_seq",schema="tdms",sequenceName="amendments_letter_id_seq",allocationSize=1)
    @Column(name="id")
	private Integer id;

	@Column(name = "letter_no", length = 500)
	private String letterNo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "letter_date")
	private Date letterDate;
	
	
	@Column(name = "amendments")
	private String amendments;
	
	@Column(name = "amendments_of")
	private String amendmentsOf;
	
	@Column(name = "remarks")
	private String remarks;
	

	@Column(name = "subject")
	private String subject;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supply_order_id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "amendmentsLetter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AmendmentsLetterAttachment> attachments;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="so_line_item_id")
	private SOLineItem soLineItem;
	
	private Integer type;

}
