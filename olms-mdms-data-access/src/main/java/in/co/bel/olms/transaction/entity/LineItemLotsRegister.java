package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.OlStatus;
import lombok.Data;

// Currently it is used for mapping the registration numbers with the submission slip i.e Offer letter

@Entity
@Table(name="line_item_lots_register",schema="tdms")
@Data
public class LineItemLotsRegister implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="register_no")
	private String registerNo;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="line_item_lot_id")
	private LineItemLots lineItemLotsId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ol_line_item_id")
	private OlLineItem olLineItemId;
	
    @Column(name="status")
    private String status;
    
    @Column(name = "is_running")
    private Boolean isRunning;
    
    @Column(name = "is_sample")
    private Boolean isSample;
    
    
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="qa_status")
	private OlStatus qaStatus ;
    
    @Column(name = "is_faisample")
    private Boolean isFaiSample;
    
    
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="serial_no_id")
	private LineItemSerialNo lineItemSerialNo;
}
