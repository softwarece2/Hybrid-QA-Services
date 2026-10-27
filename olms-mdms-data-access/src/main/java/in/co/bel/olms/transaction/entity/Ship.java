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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ship", schema = "tdms")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship implements Serializable {

	private static final long serialVersionUID = 898714556533333389L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ship_id_seq")
	@SequenceGenerator(name = "ship_id_seq", schema = "tdms", sequenceName = "ship_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "ship_name", nullable = false, length = 200)
	private String shipName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date")
	private Date modifiedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ship")
	private List<Category> categories;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "est_date")
	private Date estDate;
	
	@Column(name = "rev_no")
	private Integer revNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "so_line_item_id")
	private SOLineItem soLineItem;
	
}
