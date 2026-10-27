package in.co.bel.olms.transaction.repository;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OlAllotment;

public interface OlAllotmentRepository extends JpaRepository<OlAllotment,Integer> {
	
	
	@Query(value="SELECT "
			   + "       t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date,    "
			   + "       STRING_AGG(t.ename, ', ' order by t.created_datetime DESC) AS alloted_to,     "
			   + "       STRING_AGG(CAST(t.alloted_to AS TEXT), ',' order by t.created_datetime DESC) AS alloted_to_id,      "
			   + "       STRING_AGG(CAST(t.alloted_by AS TEXT), ',' order by t.created_datetime DESC) AS alloted_by,         "
			   + "       proj.project_name, "
			   + "       so.order_no, "
			   + "       salesGrp.name AS sales_grp, "
			   + "             STRING_AGG(TO_CHAR(t.created_datetime, 'DD-MM-YYYY HH:MI:SS AM'), ',' order by t.created_datetime DESC)AS created_datetime,"
			   + "          pn.part_no,                   "
			   + "    pn.material_description,"
			   + "       CASE WHEN EXISTS (    "
			   + "           SELECT 1    "
			   + "           FROM tdms.ol_allotment olal2    "
			   + "           WHERE olal2.offer_letter_id = t.offer_letter_id    "
			   + "             AND olal2.alloted_to = :empId "
			   + "             AND olal2.is_deligation IS FALSE    "
			   + "       ) THEN true ELSE false END AS is_alloted_em ,"
			   + "    itc.category   "
			   + "   FROM (     "
			   + "       SELECT DISTINCT "
			   + "           offer_letter_id, "
			   + "           olal.alloted_to, "
			   + "           e.ename, "
			   + "           olal.alloted_by,    "
			   + "                 olal.created_datetime "
			   + "       FROM tdms.ol_allotment olal     "
			   + "       LEFT JOIN mdms.employee e ON e.id = olal.alloted_to  "
			   + "       WHERE alloted_to IN ("
			   + "                   WITH RECURSIVE reporting_officer_tree AS (     "
			   + "                       SELECT "
			   + "                           id, "
			   + "                           reporting_officer_id      "
			   + "                       FROM mdms.employee      "
			   + "                       WHERE id = :empId"
			   + "        and organization_id= :orgId"
			   + "                       UNION ALL     "
			   + "                       SELECT "
			   + "                           employee.id, "
			   + "                           employee.reporting_officer_id      "
			   + "                       FROM mdms.employee "
			   + "                       JOIN reporting_officer_tree "
			   + "                       ON employee.reporting_officer_id = reporting_officer_tree.id  "
			   + "        where employee.organization_id= :orgId   "
			   + "                   )     "
			   + "                   SELECT id FROM reporting_officer_tree"
			   + "               )"
			   + "               AND olal.is_deligation IS FALSE"
			   + "               "
			   + "   ) t  "
			   + "    LEFT JOIN tdms.offer_letter ol ON ol.id = t.offer_letter_id     "
			   + "    LEFT JOIN tdms.ol_line_item olli ON olli.offer_letter_id = ol.id    "
			   + "          LEFT JOIN tdms.so_line_item sli ON sli.id = olli.so_line_item_id  "
			   + "    LEFT JOIN tdms.supply_order so ON so.id = sli.so_id    "
			   + "    LEFT JOIN tdms.category c ON c.so_line_item_id = sli.id    "
			   + "    LEFT JOIN tdms.ship s ON s.id = c.ship_id     "
			   + "    LEFT JOIN tdms.project proj ON proj.id = s.project_id    "
			   + "    LEFT JOIN mdms.sales_group salesGrp ON salesGrp.id = so.sales_grp_id   "
			   + "       LEFT JOIN mdms.part_number   pn ON pn.id=sli.part_number_id    "
			   + "    LEFT JOIN mdms.item_category  itc ON itc.id=pn.item_category_id "
			   + "    GROUP BY "
			   + "       t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date, "
			   + "       proj.project_name, "
			   + "       so.order_no, "
			   + "       salesGrp.name, "
			   + "    pn.part_no, "
			   + "    pn.material_description, "
			   + "    itc.category,"
			   + "             ol.created_datetime "
			//   + "             t.created_datetime "
			   + "             order by MAX(t.created_datetime) desc", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsByEmpId(int orgId,int empId);
	
	
	@Query(value="SELECT "
			   + "       t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date,    "
			   + "       STRING_AGG(t.ename, ', ' order by t.created_datetime DESC) AS alloted_to,     "
			   + "       STRING_AGG(CAST(t.alloted_to AS TEXT), ',' order by t.created_datetime DESC) AS alloted_to_id,      "
			   + "       STRING_AGG(CAST(t.alloted_by AS TEXT), ',' order by t.created_datetime DESC) AS alloted_by,    "
			   + "       pn.material_description, "
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
			   + "          LEFT JOIN tdms.so_line_item soli ON soli.sale_order_id = saleOrder.id  "
			   + "    LEFT JOIN tdms.supply_order so ON so.id = soli.so_id    "
			   + "          LEFT JOIN mdms.part_number pn ON pn.id = soli.part_number_id  "
			   + "    LEFT JOIN tdms.category c ON c.id = saleOrder.category_id    "
			   + "    LEFT JOIN tdms.ship s ON s.id = c.ship_id     "
			   + "    LEFT JOIN tdms.project proj ON proj.id = s.project_id    "
			   + "    LEFT JOIN mdms.sales_group salesGrp ON salesGrp.id = so.sales_grp_id   "
			   + "    GROUP BY "
			   + "       t.offer_letter_id, "
			   + "       ol.ol_number, "
			   + "       ol.offer_date, "
			   + "       pn.material_description, "
			   + "       so.order_no, "
			   + "       salesGrp.name ORDER BY MAX(t.created_datetime) DESC", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsForEmpId(int empId);
	
	@ Query(value="SELECT * FROM tdms.ol_allotment WHERE offer_letter_id=:olId AND alloted_to = :empId AND alloted_by = :allotedBy "
			+ "order by created_datetime desc limit 1 ",nativeQuery=true)
	public OlAllotment getAllotedOlToEmp(int olId,int empId, int allotedBy);
	
	@Query(value="SELECT alloted_by,alloted_to FROM tdms.ol_allotment WHERE offer_letter_id = :olId",nativeQuery=true)
	public List<Object[]> getAllotedByNToOfOl(int olId);
	

	@Query(value="SELECT * FROM tdms.ol_allotment WHERE alloted_to = :empId", nativeQuery=true)
	public List<OlAllotment> getAllotedOlToEmployee(int empId);
	
	
	
//	@Query(value="select * FROM tdms.ol_allotment ola "
//			+ "left join tdms_check.insp_ol_ib OLIB on  olib.offer_letter_id = ola.offer_letter_id "
//			+ "left join tdms.offer_letter ol2 on  ola.offer_letter_id = ol2.id "
//			+ "where ola.alloted_to = :empId "
//			+ "order by  case   when olib.submitted_date is not null then ol2.offer_date  when olib.submitted_date is null then olib.submitted_date end", nativeQuery=true)
//	public List<OlAllotment> getAllotedOlToEmployee1(int empId);

	@Query(value = "SELECT ol2.id, ol2.ol_number, "
	        + "COALESCE(TO_CHAR(ol2.offer_date, 'dd-mm-yyyy'), '') AS offer_date, "
	        + "po.order_no, sg.name, "
	        + "STRING_AGG(DISTINCT pn.material_description, ', ') AS project_name, "
	        + "ol2.ol_status_id, sol.submitted_date, "
	        + "COUNT(oli.*) AS total_items, SUM(oli.offered_qty) AS total_qty, "
	        + "SUM(oli.offered_qty * sli.unit_cost) AS ol_value, "
	        + "COALESCE(TO_CHAR(po.order_date, 'dd-mm-yyyy'), '') AS order_date, "
			+ "ol2.remarks, ol2.stores_description, po.id AS po_id, ol2.consignee_id, po.file_no, st.so_type, "
			+ "case when count(osca.*) > 0 then true else false end as surprise_check, "
			+ "case when count(occa.*) > 0 then true else false end as cross_check, "
			+ " org.organization_name, "
			+ "STRING_AGG(distinct cons.name, ', ') AS consignees "
	        + "FROM tdms.ol_allotment ola "
	        + "LEFT JOIN tdms_check.insp_ol_ib olib ON olib.offer_letter_id = ola.offer_letter_id "
	        + "LEFT JOIN tdms.offer_letter ol2 ON ola.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.ol_surprise_check_allotment osca ON ol2.id = osca.offer_letter_id "
	        + "LEFT JOIN tdms.ol_cross_check_allotment occa ON ol2.id = occa.offer_letter_id "
	        + "LEFT JOIN tdms.sale_order so ON ol2.sale_order_id = so.id "
	        + "LEFT JOIN tdms.supply_order po ON so.supply_order_id = po.id "
	        + "LEFT JOIN mdms.sales_group sg ON po.sales_grp_id = sg.id "
//	        + "LEFT JOIN tdms.project pj ON pj.supply_order_id = po.id "
	        + "LEFT JOIN tdms_check.insp_ol_ib sol ON sol.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.ol_line_item oli ON oli.offer_letter_id = ol2.id "
	        + "LEFT JOIN tdms.so_line_item sli ON oli.so_line_item_id = sli.id "
	        + "LEFT JOIN mdms.part_number pn ON pn.id = sli.part_number_id "
	        + "LEFT JOIN mdms.so_type st ON po.so_type_id = st.id "
	        +" LEFT JOIN mdms.organization org ON org.id= sli.insp_auth "
		    +" LEFT JOIN tdms.so_consignee_mapping scm ON scm.supply_order_id=po.id "
			+" LEFT JOIN mdms.consignee cons on cons.id=scm.consignee_id "
	        + "WHERE ola.alloted_to = :empId AND ola.is_deligation = FALSE "
	        + "GROUP BY olib.submitted_date, sol.submitted_date, ol2.id, ol2.ol_number, ol2.offer_date, "
	        + "po.order_no, sg.name, po.order_date, po.id, ol2.consignee_id, st.so_type, org.organization_name, ol2.created_datetime  "
			+ "ORDER BY ol2.created_datetime DESC", nativeQuery = true)
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
		
	
		
		@Query(value="select * from tdms.ol_allotment where offer_letter_id = :offerLetterId order by created_datetime desc ", nativeQuery = true)
	    public List<OlAllotment> getByOlIdOrderByCreatedAtDesc(Integer offerLetterId);
		
	
}