package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrgCqaeMapping entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 15-02-2023
 */

@Entity
@Table(name = "org_cqae_mapping", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgCqaeMapping implements Serializable {

	private static final long serialVersionUID = 7493494257319599537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "org_id", nullable = false)
	private Organization organization;

	@ManyToOne
	@JoinColumn(name = "cqae_id", nullable = false)
	private Organization cqaeOrg;

}
