package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;

public interface SubSlipLineItemRepository extends JpaRepository<SubSlipLineItem, Integer>{
	
	@Query(value = "SELECT * FROM tdms.sslip_line_item where sub_slip_id =:subSlipId", nativeQuery = true)
	List<SubSlipLineItem> getLineItemsByOl(@Param("subSlipId") int subSlipId);
	
	@Query(value = "SELECT  offered_qty - cleared_qty as pending "
			+ "FROM tdms.ol_line_item "
			+ "where id =:lineItemNo order by created_datetime desc limit 1", nativeQuery = true)
	Integer getPendingQtyByLineItemNo(@Param("lineItemNo") int lineItemNo);
	
	/**
	 * @author Shubham Anand
	 */
	@Query(value="SELECT * FROM tdms.ol_line_item WHERE id IN :idList", nativeQuery=true)
	List<OlLineItem> getbyIdList(Set<Integer> idList);

	@Query(value = "SELECT * FROM tdms.ol_line_item WHERE offer_letter_id IN :olIdList", nativeQuery = true)
	List<OlLineItem> getbyOlIdList(List<Integer> olIdList);
	
	@Query(value="SELECT uom.unit, SUM(ol.offered_qty) FROM tdms.ol_line_item ol "
			+ "LEFT JOIN tdms.so_line_item so ON so.id = ol.so_line_item_id "
			+ "LEFT JOIN mdms.uom uom ON uom.id = so.uom_id "
			+ "WHERE ol.offer_letter_id = :olId "
			+ "GROUP BY so.uom_id, uom.unit", nativeQuery=true)
	List<Object[]> getLineItemQtyGroupByUom(int olId);
	
	@Query(value="SELECT uom.unit, SUM(so.order_qty) FROM tdms.so_line_item so  "
			+ "LEFT JOIN mdms.uom uom ON uom.id = so.uom_id "
			+ "WHERE so.id IN :soLiIdList "
			+ "GROUP BY so.uom_id, uom.unit", nativeQuery=true)
	List<Object[]> getSoLineItemQtyWithListGroupByUom(List<Integer> soLiIdList);
	
	@Query(value="SELECT uom.unit, SUM(soli.order_qty) AS order_qty "
			+ "FROM tdms.ol_line_item olli "
			+ "LEFT JOIN tdms.so_line_item soli ON soli.id = olli.so_line_item_id "
			+ "LEFT JOIN mdms.uom uom ON uom.id = soli.uom_id "
			+ "WHERE olli.id IN :clearedOlLiIdList "
			+ "GROUP BY soli.uom_id, uom.unit", nativeQuery=true)
	List<Object[]> getClearedLineItemQtyGroupByUom(Set<Integer> clearedOlLiIdList);
	
	@Query(value="SELECT olli.id "
			+ "FROM tdms.ol_line_item olli "
			+ "LEFT JOIN tdms.so_line_item soli ON soli.id = olli.so_line_item_id "
			+ "WHERE olli.id IN :olLiIdList AND soli.order_qty = olli.cleared_qty", nativeQuery = true)
	List<Integer> getClearedLineItemsByOlLiIdList(Set<Integer> olLiIdList);
	
	@Query(value = "SELECT SLI.ORDER_QTY - "
			+ "(CASE WHEN SUM(OLI.CLEARED_QTY) IS NULL THEN 0 ELSE SUM(OLI.CLEARED_QTY) END) AS REMAINING_QTY "
			+ "FROM TDMS.OL_LINE_ITEM OLI LEFT JOIN TDMS.SO_LINE_ITEM SLI ON OLI.SO_LINE_ITEM_ID = SLI.ID WHERE"
			+ " OLI.SO_LINE_ITEM_ID = :soLineItemId GROUP BY SLI.ORDER_QTY", nativeQuery=true)
	Integer getRemainingQtyBySoLineItemId(@Param("soLineItemId") int soLineItemId);

	@Query(value = "SELECT uom.unit, SUM(ol.offered_qty) FROM tdms.ol_line_item ol "
			+ "LEFT JOIN tdms.so_line_item so ON so.id = ol.so_line_item_id "
			+ "LEFT JOIN mdms.uom uom ON uom.id = so.uom_id "
			+ "WHERE ol.offer_letter_id in (:olIds) "
			+ "GROUP BY so.uom_id, uom.unit", nativeQuery = true)
	List<Object[]> getLineItemQtyNUomByOlIds(List<Integer> olIds);

	@Query(value = "SELECT * FROM tdms.ol_line_item olli " +
			"left join tdms.offer_letter ol on olli.offer_letter_id = ol.id " +
			"where ol.is_internal = true and olli.offer_letter_id in :olIdList", nativeQuery = true)
	List<OlLineItem> findByInternalOlIdList(List<Integer> olIdList);
	
	@Query(value = "select soli.line_item_no from tdms.ol_line_item olli join tdms.so_line_item soli on soli.id = olli.so_line_item_id\r\n"
			+ "where offer_letter_id = :olId", nativeQuery = true)
	String getSoLiNoByOlId(@Param("olId") int olId);

	@Query(value = "SELECT * FROM tdms.sslip_line_item "
			+ " left join tdms.merge_sslip_mapping msm on msm.sslip_id = sub_slip_id "
			+ " where msm.merge_sslip_id= :mergedSubSlipId", nativeQuery = true)
	List<SubSlipLineItem> getLineItemsByMergedSslipId(Integer mergedSubSlipId);

	@Query(value= "Select * from tdms.sslip_line_item ssl left join tdms.ol_line_item olt ON olt.so_line_item_id = ssl.so_line_item_id WHERE olt.id = :olLineItem ", nativeQuery = true)
	Integer findSslipLineItemByOlLineItemId(Integer olLineItem);

}
