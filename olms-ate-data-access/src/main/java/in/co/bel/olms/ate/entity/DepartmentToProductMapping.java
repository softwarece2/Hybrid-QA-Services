package in.co.bel.olms.ate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Department;
import lombok.Data;

@Data
@Entity
@Table(name ="department_product_mapping", schema ="ate")
public class DepartmentToProductMapping implements Serializable {
	private static final long serialVersionUID = -8455362752612688624L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator ="department_product_mapping_id_seq")
	@SequenceGenerator(name ="department_product_mapping_id_seq", schema = "ate", sequenceName = "department_product_mapping_id_seq", allocationSize =1 )
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;
	
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")   
    private Products product;
	
}
