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

@Entity
@Table(name = "fcm_vendor", schema = "tdms")
public class FcmVendor implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcm_vendor_id_seq")
	@SequenceGenerator(name = "fcm_vendor_id_seq", schema = "tdms", sequenceName = "fcm_vendor_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	
	@ManyToOne
	@JoinColumn(name = "fcm_id")	
	private Fcm fcm;	
	
	@Column(name="vendor")
	private String vendor;
	
	@Column(name="item_desc")
	private String itemDesc;
	
	@Column(name="agency")
	private String agency;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Fcm getFcm() {
		return fcm;
	}

	public void setFcm(Fcm fcm) {
		this.fcm = fcm;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}
		
}
