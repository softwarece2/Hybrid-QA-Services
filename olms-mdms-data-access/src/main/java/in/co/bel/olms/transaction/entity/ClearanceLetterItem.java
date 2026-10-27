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

import lombok.Data;

/**
 * ClearanceLetterItems entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-07-29
 */

@Entity
@Table(name = "clearance_letter_item", schema = "tdms")
@Data
public class ClearanceLetterItem implements Serializable {

	private static final long serialVersionUID = -54710872670004045L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clearance_letter_pt_id")
	private ClearanceLetterPoint clearanceLetterPoint;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_line_item_id", nullable = false)
	private OlLineItem olLineItem;

	@Column(name = "hologram_no", length = 20)
	private String hologramNo;
	
	@Column(name = "unit_sl_no", length = 20)
	private String unitSlNo;

}
