package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.ReturnOfferLetter;

/**
 * 
 * @author Manasa B
 * @since 25-05-2023
 */
public interface ReturnOfferLetterRepository extends JpaRepository<ReturnOfferLetter, Integer> {

	/**
	 * @author Manasa B
	 * @param olId
	 * @return
	 */
	@Query(value = "SELECT * FROM tdms.return_offer_letter  where offer_letter_id =:olId  ", nativeQuery = true)
	ReturnOfferLetter getByOlId(@Param("olId") int olId);

	/**
	 * @author Manasa B
	 * @param salesGrpId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.return_offer_letter rol  left join tdms.offer_letter ol  on rol.offer_letter_id = ol.id "
			+ "  left join tdms.sale_order sale on ol.sale_order_id = sale.id  "
			+ "  left join tdms.supply_order so  on sale.supply_order_id =  so.id  "
			+ " where so.sales_grp_id =:salesGrpId ORDER BY rol.created_date DESC ", nativeQuery = true)
	List<ReturnOfferLetter> getBySalesGrpId(@Param("salesGrpId") int salesGrpId);

	/**
	 * @author Manasa B
	 * @param inspId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.return_offer_letter rol  left join tdms.offer_letter ol  on rol.offer_letter_id = ol.id "
			+ " where ol.section_officer_id =:empId ORDER BY rol.created_date DESC ", nativeQuery = true)
	List<ReturnOfferLetter> getByAcqao(@Param("empId") int empId);

	/**
	 * @author Manasa B
	 * @param empId
	 * @return
	 */

	@Query(value = "SELECT * FROM  (SELECT DISTINCT offer_letter_id " + "    FROM tdms.ol_allotment olal  "
			   + "    LEFT JOIN mdms.employee e ON e.id = olal.alloted_to  " + "    WHERE (alloted_to IN (  "
			   + "     WITH RECURSIVE reporting_officer_tree AS (  "
			   + "     SELECT id, reporting_officer_id   " + "     FROM mdms.employee   "
			   + "     WHERE reporting_officer_id = :empId " + "     UNION ALL  "
			   + "     SELECT employee.id, employee.reporting_officer_id  "
			   + "     FROM mdms.employee    " + "     JOIN reporting_officer_tree ON   "
			   + "     employee.reporting_officer_id = reporting_officer_tree.id  )  "
			   + "    SELECT id FROM reporting_officer_tree)) OR alloted_to = :empId ) t  "
			   + "   inner join tdms.return_offer_letter rol on t.offer_letter_id =  rol.offer_letter_id  "
			   + "  ORDER BY rol.created_date DESC ", nativeQuery = true)
	List<ReturnOfferLetter> getByEmpnSub(@Param("empId") int empId);

	/**
	 * @author Manasa B
	 * @param inspId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.return_offer_letter rol  left join tdms.offer_letter ol  on rol.offer_letter_id = ol.id "
			+ " where ol.dcqao_id =:empId ORDER BY rol.created_date DESC ", nativeQuery = true)
	List<ReturnOfferLetter> getByDcqao(@Param("empId") int empId);

	/**
	 * @author Manasa B
	 * @param inspId
	 * @return
	 */

	@Query(value = "SELECT * FROM tdms.return_offer_letter rol  left join tdms.offer_letter ol  on rol.offer_letter_id = ol.id "
			+ " where ol.jcqao_id =:empId ORDER BY rol.created_date DESC ", nativeQuery = true)
	List<ReturnOfferLetter> getByJcqao(@Param("empId") int empId);

}
