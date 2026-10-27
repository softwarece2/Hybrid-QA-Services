package in.co.bel.olms.ate.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import lombok.Data;

@Data
@Entity
@Table(name = "spec", schema = "ate")
public class Spec implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "spec_val")
	private String specVal;
	
	@Column(name = "min")
	private BigDecimal min;
	
	@Column(name = "max")
	private BigDecimal max;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id")
	private Products product;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id")
	private Test test;
	
	@Column(name = "param_code")
	private String paramCode;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "param_type")
	private ParamType paramType;
	
	
	@Column(name = "ty_clause_no")
	private String tyClauseNo;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit")
	private Units units;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "obs_type_id")
	private ObsType obsType;
	
	@Column(name = "obs_value")
	private String obsValue;
	
	private Integer slNo;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_type_id")
	private SpecTestType testType;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "main_type_id")
	private MainType mainType;
	
	private Boolean active;
	
}
