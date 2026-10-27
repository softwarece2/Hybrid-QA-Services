package in.co.bel.olms.master.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "part_number", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartNumber implements Serializable {

    private static final long serialVersionUID = -2147483648L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "part_number_id_seq")
    @SequenceGenerator(name = "part_number_id_seq", schema = "mdms", sequenceName = "part_number_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "part_no", nullable = false, length = 50)
    @Size(min = 1, max = 50, message = "Part Number must be between 1 and 50 characters")
    private String partNo;

    @Column(name = "material_description", length = 50)
    private String materialDescription;

    @Column(name = "cat_part_no", length = 50) 
    private String catPartNo;

    @Column(name = "nsn_part_no", length = 50) 
    private String nsnPartNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "active")
    private Boolean active;
    
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_id")
    private Uom uom;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;
	
	private String drawingNo;

}