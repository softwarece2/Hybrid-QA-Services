package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="inspection_note_dd_extn", schema="tdms")
public class InspectionNoteDdExtension implements Serializable {
	
	private static final long serialVersionUID = -8660834674394923794L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inspection_note_id")
	private InspectionFileNote inspectionFileNote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dd_extension_id")
	private DdExtension ddExtension;
	
	@Column(name="prov_dd")
	private Boolean provDd;

	@Column(name="formal_dd")
	private Boolean formalDd;	
}
