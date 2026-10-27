package in.co.bel.olms.transaction.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OlLineItem;

public interface OlLineItemRepository extends JpaRepository<OlLineItem, Integer>{
	
	@Query(value = "SELECT * FROM tdms.ol_line_item where offer_letter_id =:olId", nativeQuery = true)
	List<OlLineItem> getLineItemsByOl(@Param("olId") int olId);
	
	@Query(value = "SELECT\r\n"
			   + "    oln.id AS ol_line_item_id,\r\n"
			   + "    COUNT(CASE WHEN olsn.is_sample = TRUE  THEN 1 ELSE NULL END) AS sample_selected_count, \r\n"
			   + "    COUNT(CASE WHEN olsn.is_fai_sample = TRUE THEN 1 ELSE NULL END) AS fai_sample_selected_count, \r\n"
			   + "    COUNT(CASE WHEN olsn.is_sample = TRUE AND (olsn.status = 'pass' OR olsn.status = 'fail')  THEN 1 ELSE NULL END) AS sample_tested_count, \r\n"
			   + "    COUNT(CASE WHEN olsn.is_fai_sample = TRUE AND (olsn.status = 'pass' OR olsn.status = 'fail') THEN 1 ELSE NULL END) AS fai_sample_tested_count \r\n"
			   + " FROM \r\n"
			   + "    tdms.ol_line_item oln\r\n"
			   + " LEFT JOIN \r\n"
			   + "    tdms.ol_line_item_serial_no olsn ON olsn.ol_line_item_id = oln.id \r\n"
			   + " WHERE \r\n"
			   + "    oln.id = :olLineItemId \r\n"
			   + " GROUP BY \r\n"
			   + "    oln.id; ", nativeQuery = true)
	List<Object[]> getOLLineItemsSampleCountByOl(@Param("olLineItemId") int olLineItemId);
	
	@Query(value = "SELECT  offered_qty - cleared_qty as pending "
			+ "FROM tdms.ol_line_item "
			+ "where id =:lineItemNo order by created_datetime desc limit 1", nativeQuery = true)
	BigDecimal getPendingQtyByLineItemNo(@Param("lineItemNo") int lineItemNo);
	
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
	
//	@Query(value="WITH c AS  "
//			+ "( "
//			+ "SELECT ol_line_item.offer_letter_id, STRING_AGG(DISTINCT CAST(ol_line_item.id AS TEXT), ',') AS line_item_ids, SUM(ol_line_item.offered_qty) AS total_offered_qty  "
//			+ "FROM tdms.ol_line_item ol_line_item "
//			+ "WHERE ol_line_item.offer_letter_id = 1 "
//			+ "GROUP BY ol_line_item.offer_letter_id "
//			+ ") "
//			+ "SELECT c.line_item_ids FROM c  "
//			+ "LEFT JOIN tdms.offer_letter ol ON ol.id = c.offer_letter_id "
//			+ "WHERE c.total_offered_qty = ol.order_qty", nativeQuery=true)
//	String getClearedLineItemsByOlId(int olId);
	
	@Query(value="SELECT olli.id "
			+ "FROM tdms.ol_line_item olli "
			+ "LEFT JOIN tdms.so_line_item soli ON soli.id = olli.so_line_item_id "
			+ "WHERE olli.id IN :olLiIdList AND soli.order_qty = olli.cleared_qty", nativeQuery = true)
	List<Integer> getClearedLineItemsByOlLiIdList(Set<Integer> olLiIdList);
	
	@Query(value = "SELECT SLI.ORDER_QTY - "
			+ "(CASE WHEN SUM(OLI.CLEARED_QTY) IS NULL THEN 0 ELSE SUM(OLI.CLEARED_QTY) END) AS REMAINING_QTY "
			+ "FROM TDMS.OL_LINE_ITEM OLI LEFT JOIN TDMS.SO_LINE_ITEM SLI ON OLI.SO_LINE_ITEM_ID = SLI.ID WHERE"
			+ " OLI.SO_LINE_ITEM_ID = :soLineItemId GROUP BY SLI.ORDER_QTY", nativeQuery=true)
	BigDecimal getRemainingQtyBySoLineItemId(@Param("soLineItemId") int soLineItemId);

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

	@Query(value="select ppm.product_id  from tdms.so_line_item sli join ate.partno_product_mapping ppm on \r\n"
			+ "sli.part_number_id = ppm.part_number_id where sli.id=:soLineItemId",nativeQuery=true)
	Integer getBysoLineItemId(Integer soLineItemId);
	
	@Query(value = "select soli.delivery_date,soli.order_type_no,p.drawing_no,p.material_description,soli.order_type_date, p.cat_part_no,p.nsn_part_no,ic.category, soli.line_item_no  from tdms.ol_line_item olli join tdms.so_line_item soli on soli.id = olli.so_line_item_id\r\n"
			+ "join mdms.part_number p on p.id=soli.part_number_id \r\n"
			+ " join mdms.item_category ic on ic.id = p.item_category_id "
			+ " where offer_letter_id =:olId", nativeQuery = true)
	List<Object[]> getSoLiNoDetailsByOlId(@Param("olId") int olId);
	
	@Query(value="select distinct(sssn.id) as serialNo,sssn.sub_slip_id,sssn.sub_slip_line_item_id,ir.round_id from tdms.line_item_serial_no lisn join tdms.ol_line_item_serial_no olsn on olsn.serial_no_id=lisn.id \r\n"
			+ "join tdms.sub_slip_serial_no sssn on sssn.sub_slip_id=lisn.sub_slip_id\r\n"
			+ "join ate.insp_round ir on ir.sub_slip_line_item_id=sssn.sub_slip_line_item_id\r\n"
			+ "where olsn.ol_line_item_id= :olId AND sssn.final_status = 'pass'",nativeQuery=true)
	List<Object[]> getSubSlipItemSerialNoRoundId(@Param("olId") int olId);

}
