package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "item_category", schema = "mdms")
public class ItemCategory implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -3218440424615297825L;
	
	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator = "item_category_id_seq")
	@SequenceGenerator(name="item_category_id_seq",schema="mdms",sequenceName="item_category_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
		
	@Column(name = "category", length = 50, nullable = false)
	private String category;

	@Column(name="active")
	private Boolean active;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}	
	

}
