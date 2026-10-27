package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
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
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * ClearanceLetterPoints entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-07-29
 */

@Entity
@Table(name = "clearance_letter_point", schema = "tdms")
@Data
public class ClearanceLetterPoint implements Serializable {

	private static final long serialVersionUID = 7720211000881066364L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clearance_letter_id")
	private ClearanceLetter clearanceLetter;

	@Column(name = "content_txt", length = 500)
	private String contentText;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "clearanceLetterPoint")
	private List<ClearanceLetterItem> clearanceLetterItems;

}
