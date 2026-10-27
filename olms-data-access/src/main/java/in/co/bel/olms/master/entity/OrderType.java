package in.co.bel.olms.master.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
@Entity
@Table(name="order_type", schema = "mdms")
public class OrderType implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;

	@Column(name="type")
	private String orderType;
	
}
