package in.co.bel.olms.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "rfr_approval_uploads", schema = "tdms")
public class RfrApprovalUploads implements Serializable {

    private static final long serialVersionUID = -4429423829510651652L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfr_approval_uploads_id_seq")
    @SequenceGenerator(name = "rfr_approval_uploads_id_seq", schema = "tdms", sequenceName = "rfr_approval_uploads_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "file_name", length = 200, nullable = false)
    private String fileName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfr_approval_id")
    private RfrApprovalDetails rfrApprovalDetails;

}
