package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "vendor_rating", schema = "tdms")
public class VendorRating implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6129325098832444647L;
	@Id

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vendor_rating_id_seq")
	@SequenceGenerator(name = "vendor_rating_id_seq", schema = "tdms", sequenceName = "vendor_rating_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "vendor_id")
	private Integer vendorId;

	@Column(name = "qr", precision = 2)
	private Double qr;

	@Column(name = "dr", precision = 2)
	private Double dr;

	@Column(name = "er", precision = 2)
	private Double er;

	@Column(name = "kd", precision = 2)
	private Double kd;

	@Column(name = "kr", precision = 2)
	private Double kr;

	@Column(name = "na", precision = 2)
	private Double na;

	@Column(name = "ni", precision = 2)
	private Double ni;

	@Column(name = "q1", precision = 2)
	private Double q1;

	@Column(name = "dr_q", precision = 2)
	private Double drQ;

	@Column(name = "t", precision = 2)
	private Double t;

	@Column(name = "t1", precision = 2)
	private Double t1;

	@Column(name = "p", precision = 2)
	private Double p;

	@Column(name = "q", precision = 2)
	private Double q;

	@Column(name = "promptness")
	private Integer promptness;

	@Column(name = "attitude")
	private Integer attitude;

	@Column(name = "prepared_by")
	private Integer preparedBy;

	@Column(name = "vetted_by")
	private Integer vettedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Double getQr() {
		return qr;
	}

	public void setQr(Double qr) {
		this.qr = qr;
	}

	public Double getDr() {
		return dr;
	}

	public void setDr(Double dr) {
		this.dr = dr;
	}

	public Double getEr() {
		return er;
	}

	public void setEr(Double er) {
		this.er = er;
	}

	public Double getKd() {
		return kd;
	}

	public void setKd(Double kd) {
		this.kd = kd;
	}

	public Double getKr() {
		return kr;
	}

	public void setKr(Double kr) {
		this.kr = kr;
	}

	public Double getNa() {
		return na;
	}

	public void setNa(Double na) {
		this.na = na;
	}

	public Double getNi() {
		return ni;
	}

	public void setNi(Double ni) {
		this.ni = ni;
	}

	public Double getQ1() {
		return q1;
	}

	public void setQ1(Double q1) {
		this.q1 = q1;
	}

	public Double getDrQ() {
		return drQ;
	}

	public void setDrQ(Double drQ) {
		this.drQ = drQ;
	}

	public Double getT() {
		return t;
	}

	public void setT(Double t) {
		this.t = t;
	}

	public Double getT1() {
		return t1;
	}

	public void setT1(Double t1) {
		this.t1 = t1;
	}

	public Double getP() {
		return p;
	}

	public void setP(Double p) {
		this.p = p;
	}

	public Double getQ() {
		return q;
	}

	public void setQ(Double q) {
		this.q = q;
	}

	public Integer getPromptness() {
		return promptness;
	}

	public void setPromptness(Integer promptness) {
		this.promptness = promptness;
	}

	public Integer getAttitude() {
		return attitude;
	}

	public void setAttitude(Integer attitude) {
		this.attitude = attitude;
	}

	public Integer getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(Integer preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Integer getVettedBy() {
		return vettedBy;
	}

	public void setVettedBy(Integer vettedBy) {
		this.vettedBy = vettedBy;
	}

}