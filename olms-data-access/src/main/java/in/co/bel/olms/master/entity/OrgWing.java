package in.co.bel.olms.master.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Organization Wing entity class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 26-02-2024
 */

@Entity
@Table(name = "org_wing", schema = "mdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrgWing implements Serializable {

	private static final long serialVersionUID = 7493494257319599537L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "wing_name", nullable = false)
	@Size(min = 2, max = 100, message = "Wing name must be between 2 and 100 characters")
	private String wingName;
	
}
