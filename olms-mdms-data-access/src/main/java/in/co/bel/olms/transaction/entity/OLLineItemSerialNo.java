package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Table(name = "ol_line_item_serial_no", schema = "tdms")

public class OLLineItemSerialNo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_line_item_serial_no_id_seq")
  @SequenceGenerator(name = "ol_line_item_serial_no_id_seq", schema = "tdms", sequenceName = "ol_line_item_serial_no_id_seq", allocationSize = 1)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ol_line_item_id")
  private OlLineItem olLineItem;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "serial_no_id")
  private LineItemSerialNo serialNo;

  @Column(name = "active")
  private Boolean active;


  @Column(name = "status")
  private String status;

  @Column(name = "is_sample")
  private Boolean isSample;

  @Column(name = "is_fai_sample")
  private Boolean isFaiSample;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "inote_line_item_id")
  private INoteLineItem iNoteLineItem;

  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  private Date createdAt;

}
