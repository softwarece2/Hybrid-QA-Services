package in.co.bel.olms.transaction.repository;

import in.co.bel.olms.transaction.entity.InternalINote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InternalINoteRepository extends JpaRepository<InternalINote, Integer> {

    @Query(value="SELECT * FROM tdms.internal_inote WHERE offer_letter_id = :olId ORDER BY created_datetime DESC LIMIT 1", nativeQuery=true)
    public InternalINote getByOlId(int olId);

    @Query(value="SELECT * FROM tdms.internal_inote WHERE merge_sslip_id = :mergeSslipId ORDER BY created_datetime DESC LIMIT 1", nativeQuery=true)
    public Optional<InternalINote> getBySslipIdId(int mergeSslipId);
    
    @Query(value="SELECT Count(id) "
            + "FROM   tdms.internal_inote "
            + "WHERE  inote_date >= "
            + "       ( "
            + "              SELECT "
            + "                     CASE "
            + "                            WHEN Extract(month FROM CURRENT_DATE) < 4 THEN date_trunc('year', CURRENT_DATE - interval '1 year') + interval '3 months' "
            + "                            ELSE date_trunc('year', CURRENT_DATE) + interval '3 months' "
            + "                     END)"
            + " AND department_id = :deptId", nativeQuery=true)
    public int getInoteCountInCurrentFY(int deptId);

    @Query(value="SELECT I.inote_no, "
            + "                   TO_CHAR(inote_date, 'dd-MM-yyyy') "
            + "            FROM   tdms.internal_inote I "
            + "                   LEFT JOIN tdms.inspection_note OL "
            + "                          ON I.file_note_id= OL.id "
            + "                   LEFT JOIN tdms.supply_order PO "
            + "                          ON OL.supply_order_id = PO.id "
            + "            WHERE  PO.id = :soId and inote_date is not null "
            + "            ORDER BY I.created_datetime DESC limit 1", nativeQuery = true)
    public List<Object[]> getLastInoteDetailsBySoId(int soId);
    
    @Query(value = "select count(lilr.id) from tdms.line_item_lots_register lilr  "
    		+ "left join tdms.ol_line_item oli on oli.id = lilr.ol_line_item_id  "
    		+ "left join tdms.so_line_item sli on sli.id = oli.so_line_item_id  "
    		+ "left join tdms.internal_inote ii on ii.sub_slip_id = oli.sub_slip_id "
    		+ "where lilr.status = 'accepted' and sli.id = :sliId and ii.id is not null", nativeQuery = true)
    public Integer getClearedQty(Integer sliId);
    
    @Query(value = "select count(sssn.id) from tdms.sub_slip_serial_no sssn "
    		+ " left join tdms.warrant_details wd on wd.id = sssn.warrant_details_id "
    		+ " left join tdms.line_item_serial_no lisn on lisn.id = wd.line_item_serial_no_id "
    		+ " left join tdms.so_line_item sli on sli.id = lisn.so_line_item_id  "
    		+ " left join tdms.internal_inote ii on ii.sub_slip_id = sssn.sub_slip_id "
    		+ " where sssn.final_status = 'accepted' and sli.id = :sliId and ii.id is not null", nativeQuery = true)
    public Integer getClearedQtySslip(Integer sliId);

    @Query(value= "SELECT * FROM tdms.internal_inote WHERE merge_sslip_id = :mergedSubSlipId ", nativeQuery=true)
	Optional<InternalINote> findByMergeSubSlipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);

    @Query(value = "SELECT sum(ili.accepted) FROM tdms.internal_inote_line_item ili "
    		+ "LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ili.sslip_line_item_id "
    		+ "WHERE ssli.so_line_item_id = :soLineItemId ", nativeQuery = true)
	public Integer getClearedQtyByLineItem(@Param("soLineItemId") Integer soLineItemId);


}
