package in.co.bel.olms.transaction.entity;  

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Table(name = "amendments_letter_attachment", schema = "tdms")
@Data
public class AmendmentsLetterAttachment implements Serializable {

    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amendments_letter_attachment_id_seq")
    @SequenceGenerator(name = "amendments_letter_attachment_id_seq", schema = "tdms", sequenceName = "amendments_letter_attachment_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @JsonIgnore 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amendments_letter_id")
    private AmendmentsLetter amendmentsLetter; 

}