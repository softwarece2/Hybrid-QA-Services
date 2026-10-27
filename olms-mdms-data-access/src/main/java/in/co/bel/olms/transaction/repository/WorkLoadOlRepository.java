package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.WorkLoadOl;

public interface WorkLoadOlRepository extends JpaRepository<WorkLoadOl, Integer> {
	
	@Query(value="FROM WorkLoadOl WHERE offerLetter= :offerLetter")
	WorkLoadOl getWorkLoadByOfferLetter(@Param("offerLetter")OfferLetter offerLetter);
	
	
	@Query(value = "SELECT * FROM TDMS.workload_ol wkld left join TDMS.OFFER_LETTER ol on wkld.offer_letter_id = ol.id  "
			+ " LEFT JOIN TDMS.SALE_ORDER SO ON ol.SALE_ORDER_ID = SO.ID WHERE SO.SUPPLY_ORDER_ID = :supplyOrderId" 	, nativeQuery = true)
	List<WorkLoadOl> getwrkLoadBySupplyOrder(@Param("supplyOrderId") Integer supplyOrderId);
	
	
	@Query(value = "SELECT insp_phase_activities_id, sl_no, activity, SUM( est_man_days) AS est , MAX( plan_completion ) AS plandate,   "
			+ "SUM (actual_man_days ) AS actual, MAX( actual_completion)  AS actdate, wkldOldet.remarks, ip.id AS insp_phase_id "
			+ "FROM tdms.workload_ol_det wkldOldet  "
			+ "LEFT JOIN mdms.insp_phase_activities ipa ON ipa.id= insp_phase_activities_id  "
			+ "LEFT JOIN mdms.insp_phase ip ON ip.id = ipa.insp_phase_id "
			+ "LEFT JOIN TDMS.workload_ol wkld  ON workload_ol_id = wkld.id  "
			+ "LEFT JOIN TDMS.OFFER_LETTER ol ON wkld.offer_letter_id = ol.id    "
			+ "LEFT JOIN TDMS.SALE_ORDER Sa ON ol.SALE_ORDER_ID = Sa.ID    "
			+ "LEFT JOIN tdms.supply_order so ON sa.supply_order_id = so.id   "
			+ "WHERE wkld.supply_order_id = :supplyOrderId "
			+ "GROUP BY ip.id, insp_phase_activities_id, sl_no, activity, wkldOldet.remarks" 	, nativeQuery = true)
	List<Object[]> getwrkLoadDataBySupplyOrder(@Param("supplyOrderId") Integer supplyOrderId);
	
	
	
	@Query(value = "SELECT  so.id, so.order_no, SUM( est_man_days) AS est , SUM (actual_man_days ) AS actual\r\n"
			+ "                         FROM tdms.workload_ol_det wkldOldet \r\n"
			+ "                         LEFT JOIN mdms.insp_phase_activities ipa ON ipa.id= insp_phase_activities_id  \r\n"
			+ "                         LEFT JOIN TDMS.workload_ol wkld  ON workload_ol_id = wkld.id \r\n"
			+ "                         LEFT JOIN TDMS.OFFER_LETTER ol ON wkld.offer_letter_id = ol.id   \r\n"
			+ "                         LEFT JOIN TDMS.SALE_ORDER Sa ON ol.SALE_ORDER_ID = Sa.ID  \r\n"
			+ "                         left join tdms.supply_order so on sa.supply_order_id = so.id\r\n"
			+ "                         where so.created_datetime > :fromDateVal and so.created_datetime <= :toDateVal \r\n"
			+ "                        GROUP BY  so.id, so.order_no" 	, nativeQuery = true)
	List<Object[]> getwrkLoadDataByDate(@Param("fromDateVal") Date fromDateVal,
			@Param("toDateVal") Date toDateVal  );
	
}
