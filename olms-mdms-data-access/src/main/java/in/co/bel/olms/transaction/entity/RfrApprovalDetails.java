package in.co.bel.olms.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.OlStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rfr_approval_details", schema = "tdms")
public class RfrApprovalDetails implements Serializable {

    private static final long serialVersionUID = 350290179399100063L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfr_approval_details_id_seq")
    @SequenceGenerator(name = "rfr_approval_details_id_seq", schema = "tdms", sequenceName = "rfr_approval_details_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "remarks", nullable = false)
    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Employee createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private Employee modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_status")
    private OlStatus olStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_letter_id")
    private OfferLetter offerLetter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_rfr_approval_id")
    private RfrApprovalDetails parentRfrApprovalDetails;

    @Column(name = "note_no", length = 20)
    private String noteNo;


    @Column(name = "emp_desgn")
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_approver_id")
    private Employee nextApprover;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rfrApprovalDetails")
    private List<RfrApprovalUploads> rfrApprovalUploads;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_note_id")
    private InspectionFileNote inspectionFileNote;



}
