package in.co.bel.olms.transaction.entity;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="internal_inote_line_item", schema="tdms")
public class InternalINoteLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "internal_inote_line_item_id_seq")
    @SequenceGenerator(name = "internal_inote_line_item_id_seq", schema = "tdms", sequenceName = "internal_inote_line_item_id_seq", allocationSize = 1)
    @Column(name="id", unique = true, nullable = false)
    private Integer id;

    @Column(name="part_no")
    private String partNo;

    @Column(name="description")
    private String description;

    @Column(name="uom")
    private String uom;

    @Column(name="tendered")
    private BigDecimal tendered;

    @Column(name="accepted")
    private BigDecimal accepted;

    @Column(name="order_qty")
    private BigDecimal orderedQty;

    @Column(name="rejected")
    private BigDecimal rejected;

    @Column(name="rfr")
    private Integer rfr;

    @Column(name="ledger_folio")
    private BigDecimal ledgerFolio;

    @Column(name="qty_accepted_to_date")
    private BigDecimal qtyAcceptedToDate;

    @Column(name="remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="internal_inote_id")
    private InternalINote iNote;

    @Column(name="indent_no")
    private String indentNo;

    @Column(name="item_remarks")
    private String itemRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ol_line_item_id")
    private OlLineItem olLineItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sslip_line_item_id")
    private SubSlipLineItem sslipLineItem;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modified_datetime")
    private Date modifiedDatetime;

    @Column(name = "hologram_remarks", length = 100)
    private String hologramRemarks;

    @Column(name="ename")
    private String ename;
}
