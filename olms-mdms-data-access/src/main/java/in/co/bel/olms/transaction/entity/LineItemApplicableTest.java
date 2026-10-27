package in.co.bel.olms.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.co.bel.olms.master.entity.ApplicableTest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * LineItemApplicableTest entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since   17-10-2023
 */
@Entity
@Table(name = "line_item_applicable_test", schema = "tdms")
@Data
@NoArgsConstructor
public class LineItemApplicableTest implements Serializable {

	private static final long serialVersionUID = -5728389923888039571L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_item_applicable_test_seq")
	@SequenceGenerator(name = "line_item_applicable_test_seq", schema = "tdms", sequenceName = "line_item_applicable_test_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ol_line_item_id")
	private OlLineItem olLineItem;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "applicable_test_id")
	private ApplicableTest applicableTest;

	@Column(name = "remarks", length = 500)
	private String remarks;

	@JsonIgnore
	@OneToMany(mappedBy = "lineItemApplicableTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OlLineItemAttachment> lineItemAttachments;
	
	@Column(name = "is_ate")
	private Boolean isAte;
	
	@Column(name = "is_rmt")
	private Boolean isRmt;

}
