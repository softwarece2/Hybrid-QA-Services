package in.co.bel.olms.iol.ate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.ate.entity.Products;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ate_test_report_uploads", schema = "iol")
@Getter
@Setter
public class AteTestReportUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Integer id;
	
	private String origFileName;

	private String fileName;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploaded_by")
	private Employee uploadedBy;
	
	@CreationTimestamp
	private Date uploadedAt;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_id")
	private SubSlipSerialNo dev;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id")
	private Products prod;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id")
	private Test test;
	
	private Integer roundId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spec_id")
	private Spec spec;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_line_item_ser_no_id")
	private OLLineItemSerialNo ollisn;
	
	@Column(name = "file_hash")
    private String fileHash; // To store MD5/SHA-256 hash

}
