package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ol_part_list", schema = "tdms")
public class PartList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4539742916838922187L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_part_list_seq_id")
	@SequenceGenerator(name = "ol_part_list_seq_id", schema = "tdms", sequenceName = "ol_part_list_seq_id", allocationSize = 1)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
