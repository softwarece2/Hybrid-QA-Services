package in.co.bel.olms.ate.entity;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import lombok.Data;

@Data
@Entity
@Table(name = "fai_details", schema = "ate")
public class FaiDetails implements Serializable {

    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fai_details_id_seq")  
    @SequenceGenerator(name = "fai_details_id_seq", sequenceName = "fai_details_id_seq", allocationSize = 1, schema = "ate") 
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fai_id")
    @JsonIgnore
    private FAI fai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_no")
    @JsonIgnore
    private LineItemLotsRegister regNo;

    @Column(name = "page_no")
    private Integer pageNo;

    
    @Column(name = "remarks")
	private String remarks;
   }