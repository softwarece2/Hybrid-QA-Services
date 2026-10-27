
package in.co.bel.olms.transaction.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.transaction.entity.SupplyOrder;
/**
* Repository interface for SupplyOrder entity class
*
* @author  Awadhesh Verma
* @author Manasa B
* @version 1.0
* @since   24-09-2026
*/

@Repository
public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, Integer>{
	
	/**
	 * @author Manasa B
	 * @param cqaeId
	 * @return
	 */
	 
	@Query(value = "SELECT * FROM tdms.supply_order where organization_id =:cqaeId ORDER BY id ASC ", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByCqaeId(@Param("cqaeId") int cqaeId);
	
	@Query(value = "SELECT SO.* FROM TDMS.SUPPLY_ORDER SO LEFT JOIN MDMS.EMPLOYEE E ON E.SBU_ID = SO.SALES_GRP_ID WHERE E.ID= :salesGrpId", nativeQuery = true)
	List<SupplyOrder> getAllSupplyOrdersBySalesGrp(@Param("salesGrpId") int salesGrpId);

	@Query(value = "SELECT SO.* FROM TDMS.SUPPLY_ORDER SO where so.sales_grp_id = :salesGrpId", nativeQuery = true)
	List<SupplyOrder> getAllSupplyOrdersBySalesGrpId(int salesGrpId);
	
	@Query(value = "SELECT * FROM TDMS.SUPPLY_ORDER WHERE ORGANIZATION_ID= :orgId", nativeQuery = true)
	List<SupplyOrder> getAllSupplyOrdersByOrgId(@Param("orgId") int orgId);
	
	@Query(value = "SELECT order_no FROM TDMS.SUPPLY_ORDER", nativeQuery = true)
	List<String> getAllSupplyOrdersNos();

	@Query(value = "SELECT C.ID,C.NAME FROM MDMS.CONSIGNEE C LEFT JOIN TDMS.SO_CONSIGNEE_MAPPING SCM ON "
			+ "SCM.CONSIGNEE_ID = C.ID WHERE SCM.SUPPLY_ORDER_ID = :supplyOrderId", nativeQuery = true)
	List<Object[]> getConsigneesBySoId(@Param("supplyOrderId") int supplyOrderId);
	
	@Query(value = "SELECT "
					+ "COALESCE(SUM(VALID_DD),0) AS VALID_DD, "
					+ "COALESCE(SUM(EXPIRED_LESS_THAN_ONE),0) AS EXPIRED_LESS_THAN_ONE, "
					+ "COALESCE(SUM(EXPIRED_MORE_THAN_ONE),0) AS EXPIRED_MORE_THAN_ONE, "
					+ "COALESCE(SUM(EXPIRED_MORE_THAN_THREE),0) AS EXPIRED_MORE_THAN_THREE, "
					+ "COALESCE(SUM(EXPIRED_MORE_THAN_FIVE),0) AS EXPIRED_MORE_THAN_FIVE, "
					+ "COALESCE(SUM(COMPLETED_ON_DATE),0) AS COMPLETED_ON_DATE, "
					+ "COALESCE(SUM(EXPIRING_IN_CURRENT_FY),0) AS EXPIRING_IN_CURRENT_FY, "
					+ "COALESCE(SUM(EXPIRING_IN_THREE),0) AS EXPIRING_IN_THREE, "
					+ "COALESCE(SUM(EXPIRING_IN_SIX),0) AS EXPIRING_IN_SIX, "
					+ "COALESCE(SUM(EXPIRING_IN_TWELVE),0) AS EXPIRING_IN_TWELVE "
				+ "FROM "
				+ "("
					+ "SELECT "
						+ "(CASE WHEN SO.DD >= CURRENT_DATE THEN COUNT(SO.ID) ELSE 0 END) AS VALID_DD, "
						+ "(CASE WHEN DD BETWEEN (CURRENT_DATE - INTERVAL '1 YEARS') AND CURRENT_DATE THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_LESS_THAN_ONE, "
						+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 1 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_ONE, "
						+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 3 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_THREE, "
						+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 5 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_FIVE, "
						+ "(CASE WHEN SO.SO_STATUS_ID = 4 THEN COUNT(SO.ID) ELSE 0 END) AS COMPLETED_ON_DATE, "
						+ "(CASE WHEN SO.DD >= CURRENT_DATE AND SO.DD <= :fiscalYearEndDate THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_CURRENT_FY, "
						+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '3 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_THREE, "
						+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '6 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_SIX, "
						+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '12 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_TWELVE "
					+ "FROM TDMS.SUPPLY_ORDER SO "
					+ "WHERE "
						+ "(:orgId = -1 OR SO.ORGANIZATION_ID = :orgId) "
						+ "AND (:indentorId = -1 OR SO.INDENTER_ID = :indentorId) "
						+ "AND (:salesGrpId = -1 OR SO.SALES_GRP_ID = :salesGrpId) "
					+ "GROUP BY SO.DD, SO.SO_STATUS_ID"
				+ ") T", nativeQuery = true)
	List<Object[]> getPoDetailsForChart(@Param("orgId") int orgId, @Param("fiscalYearEndDate") LocalDate fiscalYearEndDate,
			  @Param("indentorId") int indentorId, @Param("salesGrpId") int salesGrpId);
	
	@Query(value = "SELECT "
			+ "COALESCE(SUM(VALID_DD),0) AS VALID_DD, "
			+ "COALESCE(SUM(EXPIRED_LESS_THAN_ONE),0) AS EXPIRED_LESS_THAN_ONE, "
			+ "COALESCE(SUM(EXPIRED_MORE_THAN_ONE),0) AS EXPIRED_MORE_THAN_ONE, "
			+ "COALESCE(SUM(EXPIRED_MORE_THAN_THREE),0) AS EXPIRED_MORE_THAN_THREE, "
			+ "COALESCE(SUM(EXPIRED_MORE_THAN_FIVE),0) AS EXPIRED_MORE_THAN_FIVE, "
			+ "COALESCE(SUM(COMPLETED_ON_DATE),0) AS COMPLETED_ON_DATE, "
			+ "COALESCE(SUM(EXPIRING_IN_CURRENT_FY),0) AS EXPIRING_IN_CURRENT_FY, "
			+ "COALESCE(SUM(EXPIRING_IN_THREE),0) AS EXPIRING_IN_THREE, "
			+ "COALESCE(SUM(EXPIRING_IN_SIX),0) AS EXPIRING_IN_SIX, "
			+ "COALESCE(SUM(EXPIRING_IN_TWELVE),0) AS EXPIRING_IN_TWELVE "
		+ "FROM "
		+ "("
			+ "SELECT "
				+ "(CASE WHEN SO.DD >= CURRENT_DATE THEN COUNT(SO.ID) ELSE 0 END) AS VALID_DD, "
				+ "(CASE WHEN DD BETWEEN (CURRENT_DATE - INTERVAL '1 YEARS') AND CURRENT_DATE THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_LESS_THAN_ONE, "
				+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 1 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_ONE, "
				+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 3 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_THREE, "
				+ "(CASE WHEN DATE_PART('YEAR', AGE(CURRENT_DATE, DD)) >= 5 THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRED_MORE_THAN_FIVE, "
				+ "(CASE WHEN SO.SO_STATUS_ID = 4 THEN COUNT(SO.ID) ELSE 0 END) AS COMPLETED_ON_DATE, "
				+ "(CASE WHEN SO.DD >= CURRENT_DATE AND SO.DD <= :fiscalYearEndDate THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_CURRENT_FY, "
				+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '3 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_THREE, "
				+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '6 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_SIX, "
				+ "(CASE WHEN DD BETWEEN CURRENT_DATE AND (CURRENT_DATE + INTERVAL '12 MONTHS') THEN COUNT(SO.ID) ELSE 0 END) AS EXPIRING_IN_TWELVE "
			+ "FROM TDMS.SUPPLY_ORDER SO "
			+ "WHERE "
				+ " (:orgId = -1 OR SO.ORGANIZATION_ID = :orgId) "
				+ "AND (:indentorId = -1 OR SO.INDENTER_ID = :indentorId) "
				+ "AND (:salesGrpId = -1 OR SO.SALES_GRP_ID = :salesGrpId) "
				+ "AND SO.ID IN (SELECT SUPPLY_ORDER_ID FROM TDMS.SO_CONSIGNEE_MAPPING WHERE CONSIGNEE_ID = :consigneeId)"
			+ "GROUP BY SO.DD, SO.SO_STATUS_ID"
		+ ") T", nativeQuery = true)
	List<Object[]> getPoDetailsForChartConsigneeWise(@Param("orgId") int orgId, @Param("fiscalYearEndDate") LocalDate fiscalYearEndDate,
	  @Param("indentorId") int indentorId,  @Param("consigneeId") int consigneeId, @Param("salesGrpId") int salesGrpId);
	
	@Query(value = "SELECT * FROM TDMS.SUPPLY_ORDER SO LEFT JOIN TDMS.SO_APPROVAL_DETAILS SAD ON SAD.SUPPLY_ORDER_ID = SO.ID WHERE SAD.NEXT_APPROVER_ID = :empId", nativeQuery = true)
	List<SupplyOrder> getAllotedSupplyOrders(@Param("empId") int empId);
	
	@Query(value="SELECT * FROM ( SELECT DISTINCT ON (so.id) \r\n"
			   + " so.id, so.order_no, so.file_no, so.no_of_items, so.total_qty, so.dd, so.order_date, so.status, so.is_notified,p.project_name,sg.name \r\n"
			   + " FROM tdms.supply_order so \r\n"
			   + " left join tdms.project p on p.id = so.project_id \r\n"
			   + " left join mdms.sales_group sg on sg.id = so.sales_grp_id \r\n"
			   + " left join tdms.so_line_item sli ON sli.so_id = so.id \r\n"
			   + " WHERE sli.insp_auth = :orgId \r\n"
			   + " ORDER BY so.id ) t\r\n"
			   + " ORDER BY t.order_date DESC", nativeQuery=true)
	List<Object[]> getNotifiedSupplyOrders(int orgId);
	

	// @author Manasa B
	// @since 23-09-2026
	@Query(value="SELECT   DISTINCT(so.id) , so.organization_id,    so.order_no, "
			+ "    so.order_date, so.sales_grp_id,   sales.name AS sales_grp_name, "
			+ "    so.file_no, so.received_date AS receipt_date, "
			+ "    so.created_datetime,    so.status,    so.is_notified,  so.remarks, "
			+ "    org.organization_name,     sost.status as SOStatus,    so.so_status_id as soStatusId, so.is_active "
			+ " FROM  tdms.supply_order so "
			+ " LEFT JOIN mdms.sales_group sales ON sales.id = so.sales_grp_id "
			+ " LEFT JOIN mdms.employee emp ON emp.sbu_id = sales.id "
			+ " LEFT JOIN mdms.organization org ON org.id = so.organization_id "
			+ " LEFT JOIN mdms.so_status sost ON so.so_status_id = sost.id  "
			+ " WHERE  emp.id = :empId  AND so.so_status_id IN (:statusIds)  and is_del IS NULL "
			+ " GROUP BY  so.id,    so.organization_id,  so.order_no,  so.order_date,     so.sales_grp_id,\r\n"
			+ "    sales.name,   so.received_date,    so.created_datetime,   so.status,\r\n"
			+ "    so.is_notified,    so.remarks,   org.organization_name,     sost.status ,    so.so_status_id "
			+ "   ORDER BY so.id ASC"	, nativeQuery=true)
	List<Object[]> getAllSupplyOrdersBySalesGrpOptimized(@Param("empId") int empId, List<Integer> statusIds);
	
	@Query(value="SELECT DISTINCT\r\n"
			+ "    so.id,\r\n"
			+ "    so.organization_id,\r\n"
			+ "    so.order_no,\r\n"
			+ "    so.order_date,\r\n"
			+ "    so.sales_grp_id,\r\n"
			+ "    sales.name AS sales_grp_name,\r\n"
			+ "    so.file_no,\r\n"
			+ "    so.received_date AS receipt_date,\r\n"
			+ "    so.created_datetime,\r\n"
			+ "    so.status,\r\n"
			+ "    so.is_notified,\r\n"
			+ "    so.remarks,\r\n"
			+ "    org.organization_name,\r\n"
			+ "    sost.status AS SOStatus\r\n"
			+ "FROM\r\n"
			+ "    tdms.supply_order so\r\n"
			+ "LEFT JOIN\r\n"
			+ "    mdms.sales_group sales ON sales.id = so.sales_grp_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    mdms.employee emp ON emp.sbu_id = sales.id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    mdms.organization org ON org.id = so.organization_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    mdms.so_status sost ON so.so_status_id = sost.id\r\n"
			+ "WHERE\r\n"
			+ "    emp.id = :empId  and so.created_by= :empId\r\n"
			+ "GROUP BY\r\n"
			+ "    so.id,\r\n"
			+ "    so.organization_id,\r\n"
			+ "    so.order_no,\r\n"
			+ "    so.order_date,\r\n"
			+ "    so.sales_grp_id,\r\n"
			+ "    sales.name,\r\n"
			+ "    so.file_no,\r\n"
			+ "    so.received_date,\r\n"
			+ "    so.created_datetime,\r\n"
			+ "    so.status,\r\n"
			+ "    so.is_notified,\r\n"
			+ "    so.remarks,\r\n"
			+ "    org.organization_name,\r\n"
			+ "    sost.status\r\n"
			+ "ORDER BY \r\n"
			+ "    so.id ASC;"
			, nativeQuery=true)
	List<Object[]> getAllSupplyOrdersBySalesGrpOptimizedByEmpId(@Param("empId") int empId);
	
	@Query(value = "SELECT E.id "
			+ " FROM   mdms.employee E "
			+ "       LEFT JOIN mdms.organization O   ON E.organization_id = O.id "
			+ "       LEFT JOIN mdms.sales_group SG    ON SG.organization_id = O.id "
			+ "       LEFT JOIN tdms.supply_order SO "
			+ "              ON SO.sales_grp_id = SG.id "
			+ "WHERE  E.emp_role_id = 9    AND E.sbu_id = SO.sales_grp_id "
			+ "       AND SO.id = :supplyOrderId", nativeQuery = true)
	List<Integer> getSbuAdminIdBySoId(@Param("supplyOrderId") int supplyOrderId);

	@Query(value = "select  * " +
			"from  tdms.supply_order so " +
			"where  TO_TIMESTAMP(cast(received_date as text), 'YYYY-MM-DD') >= TO_TIMESTAMP(:fromDate, 'YYYY-MM-DD') " +
			"and TO_TIMESTAMP(cast(received_date as text), 'YYYY-MM-DD') <= TO_TIMESTAMP(:toDate, 'YYYY-MM-DD') " +
			"and is_notified = true and so.organization_id = :orgId", nativeQuery = true)
	List<SupplyOrder> getSORegister(int orgId, String fromDate, String toDate);

	@Query(value = "SELECT id, file_no "
			+ "FROM tdms.supply_order so WHERE so.organization_id = :orgId AND so.is_notified = true", nativeQuery = true)
	List<Object[]> getNotifiedFileNos(int orgId);

	@Query(value = "SELECT sg.NAME AS firm_name, " +
			"       o.organization_name, " +
			"       o.address, " +
			"       so.order_no, " +
			"       so.order_date, " +
			"       so.description " +
			"FROM   tdms.supply_order so " +
			"       INNER JOIN mdms.sales_group sg " +
			"               ON so.sales_grp_id = sg.id " +
			"       INNER JOIN mdms.organization o " +
			"               ON sg.organization_id = o.id " +
			"WHERE  so.id = :supplyOrderId", nativeQuery = true)
	List<Object[]> getSODetailsForSurpriseCheck(int supplyOrderId);

	@Query(value = "select " +
			" o.organization_name, " +
			" count(so.id) as total_so " +
			"from " +
			" tdms.supply_order so " +
			"left join mdms.organization o on " +
			" so.organization_id = o.id " +
			"where " +
			" so.organization_id in (2, 21, 93) " +
			"group by " +
			" o.organization_name order by organization_name", nativeQuery = true)
	List<Object[]> getPoDetailsForDGQAChart();

	@Query(value = "select  distinct(so.*) " +
			"from  tdms.supply_order so  inner join tdms.sale_order so2 on  so2.supply_order_id = so.id " +
			"inner join tdms.offer_letter ol on  ol.sale_order_id = so2.id " +
			"where  ol.id in :olIds", nativeQuery = true)
	SupplyOrder getSupplyOrderByOlIds(List<Integer> olIds);

	@Query(value = "select   distinct(so.*) " +
			"from   tdms.supply_order so " +
			"  left join tdms.sale_order so2 on so2.supply_order_id = so.id " +
			"  left join tdms.offer_letter ol on ol.sale_order_id = so2.id " +
			"  left join tdms.file_note_ol_mapping fnom on fnom.offer_letter_id = ol.id " +
			"where " +
			"  fnom.file_note_id = :fileNoteId", nativeQuery = true)
	SupplyOrder getSupplyOrderByFileNoteId(Integer fileNoteId);
	
	@Query(value="select count(*) from tdms.so_allotment where supply_order_id =:soId ", nativeQuery = true)
	int getNotifiedCount(int soId);
	
	@Query(value="select exists ( "
			+ "select line_item_no from tdms.so_line_item soli "
			+ "where so_id = :soId and soli.line_item_no = :indentSlNo )", nativeQuery = true)
	boolean indentExists(int soId, String indentSlNo);
	
	@Query(value = "SELECT  COUNT(id) AS supply_order_count,SUM(value) AS total_order_value FROM tdms.supply_order ", nativeQuery = true)
	List<Object[]> getDashBoardSoDetails();
	
	@Query(value = "SELECT COUNT(*) " + "FROM tdms.supply_order so " + "LEFT JOIN ( "
			+ "    SELECT supply_order_id, MAX(new_dd_date) AS latest_new_dd " + "    FROM tdms.dd_extension "
			+ "    GROUP BY supply_order_id " + ") dde ON so.id = dde.supply_order_id "
			+ "LEFT JOIN mdms.sales_group sales ON sales.id = so.sales_grp_id "
			+ "LEFT JOIN mdms.employee emp ON emp.sbu_id = sales.id " + "WHERE emp.id = :empId "
			+ "AND COALESCE(dde.latest_new_dd, so.dd) "
			+ "BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '45 days'", nativeQuery = true)
	int countExpiringOrdersByEmpId(@Param("empId") int empId);

	@Query(value = "SELECT " + "    so.id, " + "    so.order_no, " + "    so.dd AS original_dd, "
			+ "    dde.latest_new_dd, " + "    COALESCE(dde.latest_new_dd, so.dd) AS final_dd "
			+ "FROM tdms.supply_order so " + "LEFT JOIN ( "
			+ "    SELECT supply_order_id, MAX(new_dd_date) AS latest_new_dd " + "    FROM tdms.dd_extension "
			+ "    GROUP BY supply_order_id " + ") dde ON so.id = dde.supply_order_id "
			+ "LEFT JOIN mdms.sales_group sales ON sales.id = so.sales_grp_id "
			+ "LEFT JOIN mdms.employee emp ON emp.sbu_id = sales.id " + "WHERE emp.id = :empId "
			+ "AND COALESCE(dde.latest_new_dd, so.dd) "
			+ "BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '45 days'", nativeQuery = true)
	List<Object[]> findExpiringOrdersByEmpId(@Param("empId") int empId);

	@Query(value = "SELECT " + "    so.id, " + "    so.order_no, " + "    so.dd AS original_dd, "
			   + "    dde.latest_new_dd, " + "    COALESCE(dde.latest_new_dd, so.dd) AS final_dd,"
			     + " sg.\"name\" as sales_grp_name "
			   + "FROM tdms.supply_order so inner join mdms.sales_group sg on so.sales_grp_id = sg.id " + "LEFT JOIN ( "
			   + "    SELECT supply_order_id, MAX(new_dd_date) AS latest_new_dd " + "    FROM tdms.dd_extension "
			   + "    GROUP BY supply_order_id " + ") dde ON so.id = dde.supply_order_id "
			   + "WHERE so.organization_id = :orgId " + "AND COALESCE(dde.latest_new_dd, so.dd) "
			   + "BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '45 days'", nativeQuery = true)
	List<Object[]> findExpiringOrdersByOrgId(@Param("orgId") int orgId);

	@Query(value = "SELECT COUNT(*) " + "FROM tdms.supply_order so " + "LEFT JOIN ( "
			+ "    SELECT supply_order_id, MAX(new_dd_date) AS latest_new_dd " + "    FROM tdms.dd_extension "
			+ "    GROUP BY supply_order_id " + ") dde ON so.id = dde.supply_order_id "
			+ "WHERE so.organization_id = :orgId " + "AND COALESCE(dde.latest_new_dd, so.dd) "
			+ "BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '45 days'", nativeQuery = true)
	int countExpiringOrdersByOrgId(@Param("orgId") int orgId);

	@Query(value = " SELECT DISTINCT so.id , so.order_no, so.order_date, so.received_date \r\n"
			   + "   FROM tdms.supply_order so \r\n"
			   + "   LEFT JOIN tdms.so_approval_details sad ON sad.supply_order_id = so.id \r\n"
			   + "   WHERE sad.approval_status = 1 AND so.sales_grp_id = :salesGrpId ", nativeQuery = true)
	List<Object[]> getApprovedSoBySalesGrp(@Param("salesGrpId") int salesGrpId);

	@Query(value = "SELECT so.status FROM TDMS.SUPPLY_ORDER so WHERE so.id = :soId" , nativeQuery = true)
	String getSupplyOrderStatus(@Param("soId") int soId);

	@Query(value = "SELECT * FROM TDMS.SUPPLY_ORDER so WHERE so.id = :soId" , nativeQuery = true)
	List<Object[]> getById(@Param("soId") int soId);
	@Query(value = "SELECT o.order_no,  o.order_date,  o.delivery_date, "
		      + " o.total_qty,  o.offered_qty,  o.lot_no,  a.accepted "
		      + "FROM (SELECT so.order_no,so.order_date,so.total_qty,so.lot_no,ino.delivery_date, "
		      + "    SUM(DISTINCT lil.offered_qty) AS offered_qty\r\n"
		      + "FROM  tdms.supply_order so\r\n"
		      + "JOIN tdms.inote ino ON ino.supply_order_no = so.order_no\r\n"
		      + "JOIN tdms.inote_line_item inoli ON inoli.inote_id = ino.id\r\n"
		      + "JOIN    tdms.so_line_item sli ON sli.so_id = so.id\r\n"
		      + "JOIN    tdms.line_item_lots lil ON lil.so_line_item_id = sli.id\r\n"
		      + "GROUP BY   so.order_no,so.order_date,so.total_qty,so.lot_no,ino.delivery_date  ) o "
		      + "LEFT JOIN (SELECT so.order_no, MIN(so.order_date) AS order_date,\r\n"
		      + "MIN(ino.delivery_date) AS delivery_date,\r\n"
		      + "MIN(so.total_qty) AS total_qty,\r\n"
		      + "MIN(so.lot_no) AS lot_no,\r\n"
		      + "SUM(inoli.accepted) AS accepted\r\n"
		      + "FROM tdms.supply_order so\r\n"
		      + "JOIN tdms.inote ino ON ino.supply_order_no = so.order_no\r\n"
		      + "JOIN tdms.inote_line_item inoli ON inoli.inote_id = ino.id\r\n"
		      + "GROUP BY so.order_no ) a ON o.order_no = a.order_no",
		     nativeQuery = true)
		List<Object[]> getAllTargetAcceptedQuantityofSo();
			
		@Query(value ="select  distinct  pn.part_no from mdms.part_number pn "
				+ "left join tdms.so_line_item sli on sli.part_number_id = pn.id "
				+ "left join tdms.supply_order so on so.id = sli.so_id "
				+ "where so.order_no = :soNumber limit 1", nativeQuery = true)
		public String getPartNoBySoNumber(@Param("soNumber") String soNumber);
		
		@Query(value = "Select Distinct org.organization_name from tdms.supply_order po     "
			    + " left join tdms.so_line_item sli on sli.so_id=po.id "
			    + " left join mdms.organization org on org.id=sli.insp_auth"
			    + " where po.order_no=:soNumber ", nativeQuery = true)
		public List<String> getInspAuthBySoNumber(@Param("soNumber") String soNumber);

		SupplyOrder findByOrderNo(String soNo);
		
		@Modifying
		@Query(value = "UPDATE tdms.supply_order SET so_status_id = :status WHERE order_no = :orderNo", nativeQuery = true)
		void updateStatusByOrderNo(@Param("orderNo") String orderNo, @Param("status") Integer status);

		@Modifying
		@Query(value = "UPDATE tdms.supply_order SET so_status_id = :status WHERE id = :id", nativeQuery = true)
		void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

		@Query(value ="select  distinct  pn.part_no, og.organization_name from mdms.part_number pn "
				+ " left join tdms.so_line_item sli on sli.part_number_id = pn.id "
				+ " left join mdms.organization og on og.id = sli.insp_auth "
				+ " LEFT JOIN tdms.category c on c.so_line_item_id = sli.id "
				+ " LEFT JOIN tdms.submission_slip ss on ss.warrant_id = c.id "
				+ " where ss.id = :categoryId ", nativeQuery = true)
		List<Object[]> getPartNumberBySubSlipId(Integer categoryId);

		@Query(value =" select  distinct  pn.part_no from mdms.part_number pn "
				+ " left join tdms.so_line_item sli on sli.part_number_id = pn.id "
				+ " LEFT JOIN tdms.category c on c.so_line_item_id = sli.id "
				+ " where c.id = :categoryId ", nativeQuery = true)
		String getPartNumberByWarrantId(Integer categoryId);

		@Query(value ="select  distinct  pn.part_no, og.organization_name from mdms.part_number pn "
				+ " left join tdms.so_line_item sli on sli.part_number_id = pn.id "
				+ " left join mdms.organization og on og.id = sli.insp_auth "
				+ " LEFT JOIN tdms.category c on c.so_line_item_id = sli.id "
				+ " LEFT JOIN tdms.submission_slip ss on ss.warrant_id = c.id "
				+ " LEFT JOIN tdms.merge_sslip_mapping msm on msm.sslip_id = ss.id "
				+ " LEFT JOIN tdms.merge_submission_slip mss on mss.id = msm.merge_sslip_id "
				+ " LEFT JOIN tdms.offer_letter ol on ol.merge_sslip_id = mss.id "
				+ " where ol.id = :olId ", nativeQuery = true)
		List<Object[]> getPartNumberByOlId(Integer olId);

		/**
		 * @author Chirag Swamy
		 * @since 29-09-26
		 * 
	     * Checks if any Lot (MergeSubmissionSlip) associated with this Supply Order 
	     * has already had an IntInote or INote generated (Lot Status 18 or 9).
	     */
	    @Query(value = "SELECT COUNT(mss) > 0 FROM tdms.merge_submission_slip mss \r\n"
	    		+ "	JOIN tdms.merge_sslip_mapping msm ON msm.merge_sslip_id = mss.id \r\n"
	    		+ " JOIN tdms.submission_slip ss ON ss.id = msm.sslip_id \r\n"
	    		+ "	JOIN tdms.category c ON c.id = ss.warrant_id \r\n"
	    		+ " JOIN tdms.so_line_item sln ON sln.id = c.so_line_item_id \r\n"
	    		+ "	JOIN tdms.supply_order so ON so.id = sln.so_id \r\n"
	    		+ "	WHERE so.id = :soId \r\n"
	    		+ "	AND mss.status IN (:IntInoteGeneratedStatus, :INoteGeneratedStatus) ", nativeQuery = true)
	    Boolean existsWithInoteStatus(Integer soId, Integer IntInoteGeneratedStatus, Integer INoteGeneratedStatus );
	    
	    /**
		 * @author Chirag Swamy
		 * @since 30-09-26
		 * 
	     */
	    @Query(value="SELECT so.id, so.organization_id, so.order_no, "
	    	    + " so.order_date, so.sales_grp_id, sg.name AS sales_grp_name, "
	    	    + " so.file_no, so.received_date AS receipt_date, "
	    	    + " so.created_datetime, so.status, so.is_notified, so.remarks, "
	    	    + " org.organization_name, sost.status as SOStatus, so.so_status_id as soStatusId, "
	    	    + " so.is_active, so.is_del, so.del_datetime " 
	    	    + " FROM tdms.supply_order so "
	    	    + " LEFT JOIN mdms.sales_group sg ON sg.id = so.sales_grp_id "
	    	    + " LEFT JOIN mdms.organization org ON org.id = so.organization_id "
	    	    + " LEFT JOIN mdms.so_status sost ON so.so_status_id = sost.id "
	    	    + " ORDER BY so.id DESC", nativeQuery=true)
	    	List<Object[]> getAllSupplyOrdersForSuperAdmin();

}
