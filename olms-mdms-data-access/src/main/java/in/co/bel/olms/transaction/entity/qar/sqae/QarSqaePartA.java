package in.co.bel.olms.transaction.entity.qar.sqae;

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

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;

@Entity
@Table(name = "qar_sqae_part_a", schema = "tdms_army")
@Data
public class QarSqaePartA {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="qar_sqae_part_a_id_seq")
    @SequenceGenerator(name="qar_sqae_part_a_id_seq",schema="tdms_army",sequenceName="qar_sqae_part_a_id_seq",allocationSize=1)
    @Column(name="id")
	private Integer id;

	@Column(name = "firms_name")
	private String firmsName;

	@Column(name = "firms_address")
	private String firmsAddress;

	@Column(name = "so_no")
	private String soNo;

	@Column(name = "at_no")
	private String atNo;

	@Column(name = "so_date")
	private String soDate;

	@Column(name = "indentor")
	private String indentor;

	@Column(name = "indent_no")
	private String indentNo;

	@Column(name = "consignee")
	private String consignee;

	@Column(name = "qa_assured_date")
	private String qaAssuredDate;

	@Column(name = "call_letter_no")
	private String callLetterNo;

	@Column(name = "call_letter_date")
	private String callLetterDate;

	@Column(name = "cls_of_deviation")
	private String clsOfDeviation;

	@Column(name = "price_reductn")
	private String priceReduction;

	@Column(name = "delivery_date")
	private String deliveryDate;

	@Column(name = "cl_received_date")
	private String clReceivedDate;
	
	@Column(name = "item_no")
	private String itemNo;

	@Column(name = "eqpt_desc")
	private String eqptDesc;

	@Column(name = "acc_unit")
	private String accUnit;

	@Column(name = "qty_unit")
	private String qtyUnit;

	@Column(name = "tender_qty")
	private String tenderQty;

	@Column(name = "accepted_qty")
	private String acceptedQty;

	@Column(name = "brought_to_ledger")
	private String broughtToLedger;
	
	@Column(name = "rejected")
	private String rejected;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date", nullable=false)
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by")
    private Employee submittedBy;
}
