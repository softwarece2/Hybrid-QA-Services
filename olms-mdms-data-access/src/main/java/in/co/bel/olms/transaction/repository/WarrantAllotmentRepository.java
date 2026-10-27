package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.WarrantAllotment;

public interface WarrantAllotmentRepository extends JpaRepository<WarrantAllotment, Integer>{
	@Query(value="SELECT t.offer_letter_id,  ol.ol_number,  ol.offer_date, STRING_AGG(t.ename, ', ' order by t.created_datetime DESC) AS alloted_to,     \"\r\n"
			+ "   + \"       STRING_AGG(CAST(t.alloted_to AS TEXT), ',' order by t.created_datetime DESC) AS alloted_to_id,      \"\r\n"
			+ "   + \"       STRING_AGG(CAST(t.alloted_by AS TEXT), ',' order by t.created_datetime DESC) AS alloted_by,         \"\r\n"
			+ "   + \"       proj.project_name, \"\r\n"
			+ "   + \"       so.order_no, \"\r\n"
			+ "   + \"       salesGrp.name AS sales_grp, \"\r\n"
			+ "   + \"             STRING_AGG(TO_CHAR(t.created_datetime, 'DD-MM-YYYY HH:MI:SS AM'), ',' order by t.created_datetime DESC)AS created_datetime,      \"\r\n"
			+ "   + \"       CASE WHEN EXISTS (    \"\r\n"
			+ "   + \"           SELECT 1    \"\r\n"
			+ "   + \"           FROM tdms.ol_allotment olal2    \"\r\n"
			+ "   + \"           WHERE olal2.offer_letter_id = t.offer_letter_id    \"\r\n"
			+ "   + \"             AND olal2.alloted_to = :empId \"\r\n"
			+ "   + \"             AND olal2.is_deligation IS FALSE    \"\r\n"
			+ "   + \"       ) THEN true ELSE false END AS is_alloted_em    \"\r\n"
			+ "   + \"   FROM (      SELECT DISTINCT \"\r\n"
			+ "   + \"           offer_letter_id, \"\r\n"
			+ "   + \"           olal.alloted_to, \"\r\n"
			+ "   + \"           e.ename, \"\r\n"
			+ "   + \"           olal.alloted_by,    \"\r\n"
			+ "   + \"                 olal.created_datetime \"\r\n"
			+ "   + \"       FROM tdms.ol_allotment olal     \"\r\n"
			+ "   + \"       LEFT JOIN mdms.employee e ON e.id = olal.alloted_to  \"\r\n"
			+ "   + \"       WHERE alloted_to IN (\"\r\n"
			+ "   + \"                   WITH RECURSIVE reporting_officer_tree AS (     \"\r\n"
			+ "   + \"                       SELECT \"\r\n"
			+ "   + \"                           id, \"\r\n"
			+ "   + \"                           reporting_officer_id      \"\r\n"
			+ "   + \"                       FROM mdms.employee      \"desc\r\n"
			+ "   + \"                       WHERE id = :empId\"\r\n"
			+ "   + \"        and organization_id= :orgId\"\r\n"
			+ "   + \"                       UNION ALL     \"\r\n"
			+ "   + \"                       SELECT \"\r\n"
			+ "   + \"                           employee.id, \"\r\n"
			+ "   + \"                           employee.reporting_officer_id      \"\r\n"
			+ "   + \"                       FROM mdms.employee \"\r\n"
			+ "   + \"                       JOIN reporting_officer_tree \"\r\n"
			+ "   + \"                       ON employee.reporting_officer_id = reporting_officer_tree.id  \"\r\n"
			+ "   + \"        where employee.organization_id= :orgId   \"\r\n"
			+ "   + \"                   )     \"\r\n"
			+ "   + \"                   SELECT id FROM reporting_officer_tree\"\r\n"
			+ "   + \"               )\"\r\n"
			+ "   + \"               AND olal.is_deligation IS FALSE\"\r\n"
			+ "   + \"               \"\r\n"
			+ "   + \"   ) t  \"\r\n"
			+ "   + \"    LEFT JOIN tdms.offer_letter ol ON ol.id = t.offer_letter_id     \"\r\n"
			+ "   + \"    LEFT JOIN tdms.sale_order saleOrder ON saleOrder.id = ol.sale_order_id    \"\r\n"
			+ "   + \"    LEFT JOIN tdms.supply_order so ON so.id = saleOrder.supply_order_id    \"\r\n"
			+ "   + \"    LEFT JOIN tdms.category c ON c.id = saleOrder.category_id    \"\r\n"
			+ "   + \"    LEFT JOIN tdms.ship s ON s.id = c.ship_id     \"\r\n"
			+ "   + \"    LEFT JOIN tdms.project proj ON proj.id = s.project_id    \"\r\n"
			+ "   + \"    LEFT JOIN mdms.sales_group salesGrp ON salesGrp.id = so.sales_grp_id   \"\r\n"
			+ "   + \"    GROUP BY t.offer_letter_id,  ol.ol_number, \"\r\n"
			+ "   + \"       ol.offer_date, \"\r\n"
			+ "   + \"       proj.project_name, \"\r\n"
			+ "   + \"       so.order_no, \"\r\n"
			+ "   + \"       salesGrp.name \"\r\n"
			+ "   + \"             order by ol.offer_date desc", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsByEmpId(int orgId,int empId);
	
	
	@Query(value="SELECT  t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date,    "
			   + "       STRING_AGG(t.ename, ', ' order by t.created_datetime DESC) AS alloted_to,     "
			   + "       STRING_AGG(CAST(t.alloted_to AS TEXT), ',' order by t.created_datetime DESC) AS alloted_to_id,      "
			   + "       STRING_AGG(CAST(t.alloted_by AS TEXT), ',' order by t.created_datetime DESC) AS alloted_by,    "
			   + "       proj.project_name, "
			   + "       so.order_no, "
			   + "       salesGrp.name AS sales_grp,    "
			   + "             STRING_AGG(TO_CHAR(t.created_datetime, 'DD-MM-YYYY HH:MI:SS AM'), ',' order by t.created_datetime DESC)AS created_datetime,      "
			   + "       CASE WHEN EXISTS (    "
			   + "           SELECT 1    "
			   + "           FROM tdms.ol_allotment olal2    "
			   + "           WHERE olal2.offer_letter_id = t.offer_letter_id    "
			   + "             AND olal2.alloted_to = :empId "
			   + "             AND olal2.is_deligation IS FALSE    "
			   + "       ) THEN true ELSE false END AS is_alloted_em    "
			   + "      FROM (     "
			   + "          SELECT DISTINCT "
			   + "              offer_letter_id, "
			   + "              olal.alloted_to, "
			   + "              e.ename, "
			   + "              olal.alloted_by,    "
			   + "                    olal.created_datetime "
			   + "          FROM tdms.ol_allotment olal     "
			   + "          LEFT JOIN mdms.employee e ON e.id = olal.alloted_to  "
			   + "         WHERE alloted_to IN ("
			   + "                      WITH RECURSIVE reporting_officer_tree AS (   "
			   + "                         SELECT "
			   + "                             id, "
			   + "                             reporting_officer_id      "
			   + "                          FROM mdms.employee     "
			   + "                          WHERE id = :empId "
			   + "                          UNION ALL     "
			   + "                          SELECT "
			   + "                              employee.id, "
			   + "                              employee.reporting_officer_id     "
			   + "                         FROM mdms.employee "
			   + "                          JOIN reporting_officer_tree "
			   + "                         ON employee.reporting_officer_id = reporting_officer_tree.id  "
			   + "       "
			   + "                      )     "
			   + "                    SELECT id FROM reporting_officer_tree"
			   + "                )"
			   + "                AND olal.is_deligation IS FALSE "
			   + "               "
			   + "     ) t "
			   + "    LEFT JOIN tdms.offer_letter ol ON ol.id = t.offer_letter_id     "
			   + "    LEFT JOIN tdms.sale_order saleOrder ON saleOrder.id = ol.sale_order_id    "
			   + "    LEFT JOIN tdms.supply_order so ON so.id = saleOrder.supply_order_id    "
			   + "    LEFT JOIN tdms.category c ON c.id = saleOrder.category_id    "
			   + "    LEFT JOIN tdms.ship s ON s.id = c.ship_id     "
			   + "    LEFT JOIN tdms.project proj ON proj.id = s.project_id    "
			   + "    LEFT JOIN mdms.sales_group salesGrp ON salesGrp.id = so.sales_grp_id   "
			   + "    GROUP BY "
			   + "       t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date, "
			   + "       proj.project_name, "
			   + "       so.order_no, "
			   + "       salesGrp.name order by ol.offer_date desc", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsForEmpId(int empId);
	
	@ Query(value="SELECT * FROM tdms.warrant_allotment WHERE offer_letter_id=:olId AND alloted_to = :empId "
			+ "order by created_datetime desc limit 1 ",nativeQuery=true)
	public WarrantAllotment getAllotedOlToEmp(int olId,int empId);
	
	@Query(value="SELECT alloted_by,alloted_to FROM tdms.warrant_allotment WHERE offer_letter_id = :olId",nativeQuery=true)
	public List<Object[]> getAllotedByNToOfOl(int olId);
	

	@Query(value="SELECT * FROM tdms.warrant_allotment WHERE alloted_to = :empId", nativeQuery=true)
	public List<WarrantAllotment> getAllotedOlToEmployee(int empId);
	
	@Query(value = "SELECT ol2.id, ol2.ol_number, "
	        + "COALESCE(TO_CHAR(ol2.offer_date, 'dd-mm-yyyy'), '') AS offer_date, "
	        + "po.order_no, sg.name, "
	        + "STRING_AGG(DISTINCT pj.project_name, ',') AS project_name, "
	        + "ol2.ol_status_id, sol.submitted_date, "
	        + "COUNT(oli.*) AS total_items, SUM(oli.offered_qty) AS total_qty, "
	        + "SUM(oli.offered_qty * sli.unit_cost) AS ol_value, "
	        + "COALESCE(TO_CHAR(po.order_date, 'dd-mm-yyyy'), '') AS order_date, "
			+ "ol2.remarks, ol2.stores_description, po.id AS po_id, ol2.consignee_id, po.file_no, st.so_type, "
			+ "case when count(osca.*) > 0 then true else false end as surprise_check, "
			+ "case when count(occa.*) > 0 then true else false end as cross_check "
	        + "FROM tdms.ol_allotment ola "
	        + "LEFT JOIN tdms_check.insp_ol_ib olib ON olib.offer_letter_id = ola.offer_letter_id "
	        + "LEFT JOIN tdms.offer_letter ol2 ON ola.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.ol_surprise_check_allotment osca ON ol2.id = osca.offer_letter_id "
	        + "LEFT JOIN tdms.ol_cross_check_allotment occa ON ol2.id = occa.offer_letter_id "
	        + "LEFT JOIN tdms.sale_order so ON ol2.sale_order_id = so.id "
	        + "LEFT JOIN tdms.supply_order po ON so.supply_order_id = po.id "
	        + "LEFT JOIN mdms.sales_group sg ON po.sales_grp_id = sg.id "
	        + "LEFT JOIN tdms.project pj ON pj.supply_order_id = po.id "
	        + "LEFT JOIN tdms_check.insp_ol_ib sol ON sol.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.ol_line_item oli ON oli.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.so_line_item sli ON oli.so_line_item_id = sli.id "
	        + "LEFT JOIN mdms.so_type st ON po.so_type_id = st.id "
	        + "WHERE ola.alloted_to = :empId AND ola.is_deligation = FALSE "
	        + "GROUP BY olib.submitted_date, sol.submitted_date, ol2.id, ol2.ol_number, ol2.offer_date, "
	        + "po.order_no, sg.name, po.order_date, po.id, ol2.consignee_id, st.so_type "
			+ "ORDER BY ol2.offer_date DESC", nativeQuery = true)
	public List<Object[]> getAllotedOlToEmployee1(int empId);

	
	  @Modifying
	    @Transactional
	    @Query(value = "UPDATE tdms.ol_allotment " +
	                   "SET is_deligation = true " +
	                   "WHERE offer_letter_id = :olId " +
	                   "  AND created_datetime < :createdDate " +
	                   "  AND alloted_by = :empId", nativeQuery = true)
	    void updateDeligationStatus(@Param("empId") Integer empId,
	                                @Param("olId") Integer olId,
	                                @Param("createdDate") Date createdDate);
	  
	  
	  @Query(value="select distinct alloted_by, alloted_to "
				+ "from tdms.ol_allotment "
				+ "where offer_letter_id = :olId", nativeQuery = true)
		public List<Object[]> getEmployeesByOlId(int olId);
		
		@Query(value = "SELECT STRING_AGG( DISTINCT   emp.ename,',') " +
				" FROM tdms.ol_allotment ol " +
				" left join mdms.employee emp  on emp.id = ol.alloted_to " +
				" WHERE ol.offer_letter_id = :olId "
				, nativeQuery = true)
		public String getAllotedOlToEmployeeName(int olId);

		public List<WarrantAllotment> findByCategoryIdOrderByCreatedAtDesc(Integer warrantId);
}
