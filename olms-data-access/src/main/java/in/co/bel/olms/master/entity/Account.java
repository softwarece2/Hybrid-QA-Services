package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the Account Role table.
 * 
 * @author Awadhesh Verma
 * @since 08-06-2023
 */

@Entity
@Table(name = "account", schema = "mdms")
@Data
public class Account implements Serializable {

	private static final long serialVersionUID = -934600628779571914L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "acct_type", length = 20, nullable = false)
	private String acctType;

}
