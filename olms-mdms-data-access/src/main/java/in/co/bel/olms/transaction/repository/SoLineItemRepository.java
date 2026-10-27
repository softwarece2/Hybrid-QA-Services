package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SOLineItem;

public interface SoLineItemRepository extends JpaRepository<SOLineItem, Integer> {

	@Query(value = "SELECT * FROM TDMS.SO_LINE_ITEM WHERE SO_ID= :soId order by id desc ", nativeQuery = true)
	List<SOLineItem> getSoLineItemsBySoId(@Param("soId") Integer soId);
	
	@Query(value = "SELECT * FROM TDMS.SO_LINE_ITEM WHERE line_item_no IN :lineItemsNos", nativeQuery = true)
	List<SOLineItem> getLineItemsByNos(@Param("lineItemsNos") List<String> lineItemsNos);

	@Query(value = "SELECT * FROM TDMS.SO_LINE_ITEM WHERE sale_order_id = :saleOrderId", nativeQuery = true)
	List<SOLineItem> getSoLineItemsBySaleOrderId(@Param("saleOrderId") int saleOrderId);
	
	@Query(value = "SELECT * FROM TDMS.SO_LINE_ITEM sli "
			+ " left join tdms.category c on c.so_line_item_id = sli.id "
			+ " WHERE c.id = :categoryId", nativeQuery = true)
	SOLineItem getSoLineItemsByCategoryId(@Param("categoryId") int categoryId);

	@Query(value="SELECT uom.unit, SUM(so.order_qty) FROM tdms.so_line_item so "
			+ "LEFT JOIN mdms.uom uom ON uom.id = so.uom_id "
			+ "WHERE so.so_id = :soId "
			+ "GROUP BY so.uom_id, uom.unit", nativeQuery=true)
	List<Object[]> getfLineItemsPoQtyGroupByUom(int soId);
	
	@Query(value="WITH c AS  "
			+ "(SELECT SUM(ol.offered_qty) AS offered_qty_sum, STRING_AGG(CAST(ol.id AS TEXT), ',') AS ol_id, "
			+ "so.order_qty AS so_order_qty, so.id AS so_id  "
			+ "FROM tdms.ol_line_item ol "
			+ "LEFT JOIN tdms.so_line_item so ON ol.so_line_item_id = so.id  "
			+ "WHERE so.so_id = :soId "
			+ "GROUP BY so.id) "
			+ "SELECT c.so_id FROM c WHERE c.offered_qty_sum = c.so_order_qty", nativeQuery=true)
	List<Integer> getSuppliedLineItemListBySoId(int soId);
	
	@Query(value="SELECT * FROM TDMS.SO_LINE_ITEM WHERE so_id = :soId and is_gcs is true", nativeQuery=true)
	List<SOLineItem> getGcsLineItemsBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT ol.ol_number FROM tdms.so_line_item  sli "
		    + "            JOIN tdms.ol_line_item oli ON sli.id = oli.so_line_item_id "
		    + "           JOIN tdms.offer_letter ol ON oli.offer_letter_id = ol.id "
		    + "            WHERE sli.id = :soLineItemId", nativeQuery=true)
	 String findOlNumberBySoLineItemId(@Param("soLineItemId") int soLineItemId);
	
	@Query(value = "SELECT p.part_no,slo.sale_order_no,sli.order_master_no,sli.line_item_no,sli.insp_auth, \r\n"
			   + "      sli.supply_type,p.item_category_id,p.drawing_no,p.cat_part_no,p.nsn_part_no,p.uom_id,p.material_description, \r\n"
			   + "      sli.unit_cost,sli.delivery_date,sli.order_qty,\r\n"
			   + "      STRING_AGG(c.name, ', '),sli.ref_no,sli.order_type_id,sli.order_type_no,sli.order_type_date, \r\n"
			   + "      sli.gem,sli.self_certification,sli.remarks,sli.is_imported, \r\n"
			   + "      sli.is_gcs,sli.status, string_agg(cast(c.id AS text),  ',') as consignee_ids,sli.registration_no FROM tdms.so_line_item sli  \r\n"
			   + "      join mdms.part_number p on p.id=sli.part_number_id \r\n"
			   + "      Left join tdms.so_consignee_mapping cm on cm.so_line_item_id=sli.id \r\n"
			   + "      left join tdms.sale_order slo on slo.id=sli.sale_order_id\r\n"
			   + "      left join mdms.consignee c on c.id=cm.consignee_id\r\n   where sli.id=:sliId "
			   + "      group by p.part_no,slo.sale_order_no,sli.order_master_no,sli.line_item_no,sli.insp_auth, \r\n"
			   + "      sli.supply_type,p.item_category_id,p.drawing_no,p.cat_part_no,p.nsn_part_no,p.uom_id,p.material_description, \r\n"
			   + "      sli.unit_cost,sli.delivery_date,sli.order_qty,sli.ref_no,sli.order_type_id,sli.order_type_no,sli.order_type_date, \r\n"
			   + "      sli.gem,sli.self_certification,sli.remarks,sli.is_imported, \r\n"
			   + "      sli.is_gcs,sli.status,sli.registration_no ", nativeQuery=true)
	List<Object[]> getSoLineItemDetails(int sliId);
	
	  
	@Query(value = "SELECT string_agg(DISTINCT con.name, ', ') AS consignee_names\r\n"
			+ "FROM tdms.so_consignee_mapping scm\r\n"
			+ "JOIN tdms.so_line_item sli ON scm.supply_order_id = sli.so_id\r\n"
			+ "Join mdms.consignee con on con.id=scm.consignee_id\r\n" + "WHERE sli.so_id = :soId\r\n"
			+ "GROUP BY sli.so_id", nativeQuery = true)
	String getConsigneesBySoId(int soId);
	
	@Query(value = "SELECT string_agg(DISTINCT con.name, ', ') AS consignee_names "
			   + "  FROM tdms.so_consignee_mapping scm "
			   + "   JOIN tdms.so_line_item sli ON scm.so_line_item_id = sli.id "
			   + "   Join mdms.consignee con on con.id=scm.consignee_id WHERE sli.id =:sliId "
			   + "   GROUP BY sli.id", nativeQuery = true)
	String getConsigneesBySliId(int sliId);
	
	@Query(value = "SELECT con.id AS consignee_names \r\n"
			   + "      FROM tdms.so_consignee_mapping scm \r\n"
			   + "       JOIN tdms.so_line_item sli ON scm.so_line_item_id = sli.id \r\n"
			   + "       Join mdms.consignee con on con.id=scm.consignee_id WHERE sli.id =:sliId\r\n"
			   + "       GROUP BY sli.id,con.id", nativeQuery = true)
	List<Integer> getConsigneesIdBySliId(int sliId);
	
	@Query(value = "SELECT Count(*) FROM TDMS.SO_LINE_ITEM WHERE SO_ID= :soId group by so_id ", nativeQuery = true)
	Integer getSoLineItemCount(@Param("soId") Integer soId);

	@Modifying
	@Query(value = "UPDATE tdms.so_line_item SET status_id = :status WHERE id = :id", nativeQuery = true)
	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

	@Modifying
	@Query(value = "UPDATE tdms.so_line_item SET status_id = :status WHERE so_id = :id", nativeQuery = true)
	void updateStatusBySupplyOrderId(@Param("id") Integer id, @Param("status") Integer status);

  @Query(value = "select distinct sli.* from tdms.so_line_item sli " +
      "join tdms.sslip_line_item ssli on ssli.so_line_item_id = sli.id " +
      "join tdms.merge_sslip_mapping msm on msm.sslip_id  = ssli.sub_slip_id  " +
      "where msm.merge_sslip_id = :lotId ", nativeQuery = true)
  SOLineItem getSoLineItemByLotId(@Param("lotId") Integer lotId);
  
  //Checks if a line item with the same number already exists for a specific Supply Order
  boolean existsBySupplyOrderIdAndLineItemNo(Integer supplyOrderId, String lineItemNo);
	
}
