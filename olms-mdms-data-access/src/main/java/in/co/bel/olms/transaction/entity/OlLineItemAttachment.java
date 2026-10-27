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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OlLineItemAttachment entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since   13-02-2024
 */
@Entity
@Table(name = "ol_line_item_attachment", schema = "tdms")
@Data
@NoArgsConstructor
public class OlLineItemAttachment implements Serializable {

	private static final long serialVersionUID = -5728389923888039471L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_line_test_attachment_seq")
	@SequenceGenerator(name = "ol_line_test_attachment_seq", schema = "tdms", sequenceName = "ol_line_test_attachment_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "test_id")
	private LineItemApplicableTest lineItemApplicableTest;

	@Column(name = "file_name", length = 500)
	private String fileName;

	@Column(name = "created_by", length = 100)
	private String createdBy;

}
