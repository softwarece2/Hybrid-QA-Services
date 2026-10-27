package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "signature_det", schema = "mdms")
public class SignatureDet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8249170266294324547L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="signature_det_id_seq")
	@SequenceGenerator(name="signature_det_id_seq",schema="mdms",sequenceName = "signature_det_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true)
	private Integer id;
	
	@Column(name = "signatory_name")
	private String signatoryName;
	
	@Column(name = "signatory_rank")
	private String signatoryRank;
	
	@Column(name = "signatory_desig")
	private String signatoryDesig;
	
	@Column(name = "hindi_name")
	private String hindiName;
	
	@Column(name = "hindi_rank")
	private String hindiRank;
	
	@Column(name = "hindi_desig")
	private String hindiDesig;

	public Integer getId() {
		return id;
	}

	public String getSignatoryName() {
		return signatoryName;
	}

	public String getSignatoryRank() {
		return signatoryRank;
	}

	public String getSignatoryDesig() {
		return signatoryDesig;
	}

	public String getHindiName() {
		return hindiName;
	}

	public String getHindiRank() {
		return hindiRank;
	}

	public String getHindiDesig() {
		return hindiDesig;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSignatoryName(String signatoryName) {
		this.signatoryName = signatoryName;
	}

	public void setSignatoryRank(String signatoryRank) {
		this.signatoryRank = signatoryRank;
	}

	public void setSignatoryDesig(String signatoryDesig) {
		this.signatoryDesig = signatoryDesig;
	}

	public void setHindiName(String hindiName) {
		this.hindiName = hindiName;
	}

	public void setHindiRank(String hindiRank) {
		this.hindiRank = hindiRank;
	}

	public void setHindiDesig(String hindiDesig) {
		this.hindiDesig = hindiDesig;
	}
	
	
	
	

}
