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

@Entity
@Table(name = "covering_letter_point", schema = "tdms")
@Data
public class CoveringLetterPoint implements Serializable{
	private static final long serialVersionUID = 7720211000881066364L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "covering_letter_id")
	private CoveringLetter coveringLetter;

	@Column(name = "content_txt", length = 500)
	private String contentText;
	
	
	
}


