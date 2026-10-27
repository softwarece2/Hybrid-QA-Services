package in.co.bel.olms.ate.entity;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.transaction.entity.OlLineItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "fai", schema = "ate")
public class FAI {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fai_id_seq")
	@SequenceGenerator(name = "fai_id_seq", sequenceName = "fai_id_seq", allocationSize = 1, schema = "ate")
	private Integer id;

    private String name;

    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Employee submittedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "submitted_datetime")
    private Date submittedDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "created_datetime")
    private Date createdDatetime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ol_line_item_id")
    @JsonIgnore
    private OlLineItem olLineItem;    

	@Column(name = "sub_qty")
	private Integer subQty;
	
	@Column(name = "act_qty")
	private Integer actQty;
	
	@Column(name = "rej_qty")
	private Integer rejQty;
	
	@Column(name = "remarks")
	private String remarks;

    @Column(name = "is_qa")
    private Boolean isQa;
	
}
