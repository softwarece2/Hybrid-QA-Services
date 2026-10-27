package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.PoObservation;
import in.co.bel.olms.transaction.entity.SupplyOrder;

/**
 * 
 * @author Manasa B
 * @since 08-04-2023
 * 
 *
 */
public interface PoObservationRepository extends JpaRepository<PoObservation, Integer> {

	@Query(value = "FROM PoObservation where supplyOrder = :supplyOrder")
	PoObservation getPoObservationBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);

	/**
	 * @author Manasa B
	 * @param opaId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id  "
			+ " where so.sales_grp_id =:opaId ORDER BY po.created_datetime DESC ", nativeQuery = true)
	List<PoObservation> getPoObservationByOpaId(@Param("opaId") int opaId);

	/**
	 * @author Manasa B
	 * @param opaId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id  "
			+ " where so.sales_grp_id =:salesGrpId ORDER BY po.created_datetime DESC ", nativeQuery = true)
	List<PoObservation> getPoObservationBySalesGrpId(@Param("salesGrpId") int salesGrpId);

	/**
	 * @author Manasa B
	 * @param inspId
	 * @return
	 */
	/**
	 * Alternate query is :
	 * 
	 * SELECT * from tdms.po_observation po where po.supply_order_id in ( SELECT
	 * so.id from tdms.supply_order so left join tdms.so_allotment soal on
	 * soal.supply_order_id = so.id where soal.alloted_to =7 ) ORDER BY
	 * po.created_datetime DESC
	 */
	@Query(value = "SELECT * FROM tdms.po_observation po  "
			+ "left join tdms.supply_order so  on po.supply_order_id =  so.id   "
			+ "left join tdms.so_allotment soal  on soal.supply_order_id = so.id " + "where soal.alloted_to =:inspId  "
			+ "ORDER BY po.created_datetime DESC  ", nativeQuery = true)
	List<PoObservation> getByInsp(@Param("inspId") int inspId);

	/**
	 * Todo : recursive by organization id of the cqae
	 * 
	 * @author Manasa B
	 * @param orgId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id  "
			+ " where so.sales_grp_id =:orgId ORDER BY po.created_datetime DESC ", nativeQuery = true)
	List<PoObservation> getPoObservationByCqaeId(@Param("orgId") int orgId);

//	/**
//	 * @author Manasa B
//	 * @param inspId
//	 * @return
//	 */
//	
//	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id"		
//			+ " where so.section_officer_id =:empId ORDER BY po.created_date DESC ", nativeQuery = true)
//	List<PoObservation> getByAcqao(@Param("empId") int empId);	
//	
//	/**
//	 * @author Manasa B
//	 * @param inspId
//	 * @return
//	 */
//	
//	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id"		
//			+ " where so.dcqao_id =:empId ORDER BY po.created_date DESC ", nativeQuery = true)
//	List<PoObservation> getByDcqao(@Param("empId") int empId);	
//
//	/**
//	 * @author Manasa B
//	 * @param inspId
//	 * @return
//	 */
//	
//	@Query(value = "SELECT * FROM tdms.po_observation po left join tdms.supply_order so  on po.supply_order_id =  so.id"		
//			+ " where so.jcqao_id =:empId ORDER BY po.created_date DESC ", nativeQuery = true)
//	List<PoObservation> getByJcqao(@Param("empId") int empId);	
//	

	@Query(value = "SELECT * FROM  (SELECT DISTINCT supply_order_id  FROM tdms.so_allotment soal  "
			   + "     LEFT JOIN mdms.employee e ON e.id = soal.alloted_to  "
			   + "     WHERE (alloted_to IN (  "
			   + "      WITH RECURSIVE reporting_officer_tree AS (  "
			   + "      SELECT id, reporting_officer_id   FROM mdms.employee   "
			   + "      WHERE reporting_officer_id = :empId  UNION ALL  "
			   + "      SELECT employee.id, employee.reporting_officer_id  "
			   + "      FROM mdms.employee  JOIN reporting_officer_tree ON   "
			   + "      employee.reporting_officer_id = reporting_officer_tree.id   ) "
			   + "     SELECT id FROM reporting_officer_tree)) OR alloted_to = :empId) t  "
			   + "    inner join tdms.po_observation po on t.supply_order_id =  po.supply_order_id  "
			   + " ORDER BY po.created_datetime DESC ", nativeQuery = true)
	List<PoObservation> getByEmpnSub(@Param("empId") int empId);
	
	@Query(value = "SELECT ID FROM TDMS.PO_OBSERVATION WHERE SUPPLY_ORDER_ID = :supplyOrderId",  nativeQuery = true)
	List<Integer> getPoObsIdsBySoId(@Param("supplyOrderId") int supplyOrderId);

}
