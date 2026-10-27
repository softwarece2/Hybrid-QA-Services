package in.co.bel.olms.transaction.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "line_item_serial_no_history", schema = "tdms")
public class LineItemSerialNoHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Integer id;
	
	private Integer lineItemSerialNoId;
	private Integer statusId;
	private String status;
	private String remark;
	private Integer createdById;
	private String createdBy;
	
	@CreationTimestamp
	private Date createdAt;

	private Integer fkId;
	private String fkName;
	
}
