package in.co.bel.olms.iol.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_type", schema = "iol")
public class ProductType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_type_seq")
	@SequenceGenerator(name = "product_type_seq", schema = "iol", sequenceName = "product_type_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "product")
	private String product;
	
}
