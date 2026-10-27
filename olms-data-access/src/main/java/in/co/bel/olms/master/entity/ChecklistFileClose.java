package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
/**
 * The persistent class for the Checklist_File_Close table.
 * 
 * @author Sulochana B
 * @since 14-03-2023
 * 
 */
@Entity
@Table(name="checklist_file_close",schema = "mdms")
public class ChecklistFileClose implements Serializable {

		/**
	 * 
	 */
	private static final long serialVersionUID = 7499770469433802046L;

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_file_close_id_seq")
		@SequenceGenerator(name = "checklist_file_close_id_seq", schema = "mdms", sequenceName = "checklist_file_close_id_seq", allocationSize = 1)
		@Column(name = "id", unique = true, nullable = false)
		private int id;
		
		@Column(name = "sl_no")
		private int slNo;
		
		public String getObsType() {
			return obsType;
		}

		public void setObsType(String obsType) {
			this.obsType = obsType;
		}

		@Column(name= "reference")
		private String reference;
		
		@Column(name = "split")
		private Boolean split;
		
		@Column(name = "obs_type")
		private String obsType;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getSlNo() {
			return slNo;
		}

		public void setSlNo(int slNo) {
			this.slNo = slNo;
		}

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public Boolean getSplit() {
			return split;
		}

		public void setSplit(Boolean split) {
			this.split = split;
		}
		

}
