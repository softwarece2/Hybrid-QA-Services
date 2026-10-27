package in.co.bel.olms.ate.entity;

import in.co.bel.olms.master.entity.PartNumber;
import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "partno_product_mapping", schema = "ate")
public class PartnoProductMapping implements Serializable {

    private static final long serialVersionUID = 9223372036854775807L; 

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partno_product_id_seq")
    @SequenceGenerator(name = "partno_product_id_seq", schema = "ate", sequenceName = "partno_product_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    
    @JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")   
    private Products product;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_number_id")
    private PartNumber partNumber;
}