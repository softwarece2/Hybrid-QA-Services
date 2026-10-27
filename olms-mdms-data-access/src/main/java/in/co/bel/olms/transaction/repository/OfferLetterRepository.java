package in.co.bel.olms.transaction.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;


/**
 * @author Manasa B
 * @since 20-01-2023
 */
public interface OfferLetterRepository extends JpaRepository<OfferLetter, Integer> {

	
	/**
	 * @author Manasa B
	 * @param cqaeId
	 * @return
	 */
	//Todo: to be checked after foreign key in database
	@Query(value = "SELECT * FROM tdms.offer_letter ol left join tdms.sale_order sa on ol.sale_order_id = sa.id left join tdms.supply_order so on  sa.supply_order_id = so.id where so.organization_id =:cqaeId ORDER BY ol.id ASC ", nativeQuery = true)
	List<OfferLetter> getOfferLetterByCqaeId(@Param("cqaeId") int cqaeId);
	
	@Query(value = "select ol_number from tdms.offer_letter", nativeQuery = true)
	List<String> findOfferLetterNos();
	
	@Query(value = "SELECT * FROM tdms.offer_letter ol where ol.sale_order_id =:saleOrderId", nativeQuery = true)
	List<OfferLetter> getOLBySaleOrderId(@Param("saleOrderId") int saleOrderId);
	
	@Query(value = "SELECT * FROM TDMS.OFFER_LETTER OL LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID WHERE SO.SUPPLY_ORDER_ID = :supplyOrderId", nativeQuery = true)
	List<OfferLetter> getOLBySupplyOrderId(@Param("supplyOrderId") int supplyOrderId);
	
	@Query(value = "SELECT * FROM tdms.offer_letter ol where ol.id IN :idList", nativeQuery = true)
	List<OfferLetter> getOLByIdlist(@Param("idList") Set<Integer> idList);
	
	@Query(value = "SELECT PO.ORGANIZATION_ID FROM TDMS.SUPPLY_ORDER PO LEFT JOIN TDMS.PROJECT P ON "
			+ "PO.PROJECT_ID = P.ID LEFT JOIN TDMS.SHIP S ON P.ID = S.PROJECT_ID LEFT JOIN "
			+ "TDMS.CATEGORY C ON C.SHIP_ID = S.ID LEFT JOIN TDMS.SALE_ORDER SO ON "
			+ "SO.CATEGORY_ID = C.ID LEFT JOIN TDMS.OFFER_LETTER OL ON OL.SALE_ORDER_ID = SO.ID WHERE OL.ID = :olId", nativeQuery = true)
	Integer getCqaeOrgIdByOlId(@Param("olId") int olId);
	
	@Query(value = "SELECT * FROM TDMS.OFFER_LETTER WHERE employee_id = :empId", nativeQuery = true)
	List<OfferLetter> getAllOfferLettersByInspectorId(@Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.OFFER_LETTER WHERE section_officer_id = :empId", nativeQuery = true)
	List<OfferLetter> getAllOfferLettersBySectionOffcrId(@Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.OFFER_LETTER WHERE dcqao_id = :empId", nativeQuery = true)
	List<OfferLetter> getAllOfferLettersByDcqaoId(@Param("empId") int empId);
	
	@Query(value = "SELECT * FROM TDMS.OFFER_LETTER WHERE jcqao_id = :empId", nativeQuery = true)
	List<OfferLetter> getAllOfferLettersByJcQaoId(@Param("empId") int empId);
	
	@Query(value = "SELECT OL.* FROM TDMS.OFFER_LETTER OL LEFT JOIN TDMS.SALE_ORDER SO ON"
			+ " OL.SALE_ORDER_ID = SO.ID LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID "
			+ "LEFT JOIN MDMS.EMPLOYEE E ON E.ORGANIZATION_ID = PO.ORGANIZATION_ID WHERE E.ID = :empId", nativeQuery = true)
	List<OfferLetter> getAllOfferLettersByCqaoId(@Param("empId") int empId);
	
	@Query(value =" SELECT OL.id, i.id as inote_id ,"
			   + "          OL.offer_letter_no,"
			   + "          COALESCE(To_char(OL.offer_letter_date, 'dd-mm-yyyy'), '') AS offer_date,"
			   + "       SG.NAME AS sbu_name, "
			   + "          P.project_name, "
			   + "          COALESCE(To_char(I.inote_date, 'dd-mm-yyyy'), '')  AS inote_date,"
			   + "          po.inote_type_id , "
			   + "          CASE "
			   + "            WHEN Count(I.id) > 0 THEN 'Issued' "
			   + "            ELSE 'Pending' "
			   + "          END AS INOTE_STATUS ,po.order_no ,i.consignee, OL.offer_letter_id as olid,po.id as supplyOrderId,org.organization_name"
			   + "       "
			   + "   FROM   tdms.internal_inote OL "
			   + "          LEFT JOIN  tdms.internal_inote_approval_details OAD "
			   + "                 ON OAD.internal_inote_id = OL.id "
			   + "          LEFT JOIN mdms.employee E "
			   + "                 ON OAD.approved_by = E.id"
			   + "        left join tdms.offer_letter oll"
			   + "        on ol.offer_letter_id=oll.id"
			   + "          LEFT JOIN tdms.sale_order SO "
			   + "                 ON OlL.sale_order_id = SO.id "
			   + "          LEFT JOIN tdms.supply_order PO "
			   + "                 ON SO.supply_order_id = PO.id "
			   + "          LEFT JOIN mdms.sales_group SG "
			   + "                 ON PO.sales_grp_id = SG.id "
			   + "          LEFT JOIN tdms.inote I "
			   + "                 ON I.offer_letter_id = OL.offer_letter_id "
			   + "          LEFT JOIN tdms.category c"
			   + "               ON c.id = SO.category_id "
			   + "          LEFT JOIN tdms.ship s"
			   + "                  ON s.id = c.ship_id  "
			   + "         LEFT JOIN tdms.project p "
			   + "                 ON p.id = s.project_id "
			   + "                   LEFT JOIN   mdms.organization org ON org.id=PO.organization_id"
			   + "   WHERE  OAD.approval_status = 1 "
			   + "          AND PO.organization_id = :orgId "
			   + "          AND E.emp_role_id = 2 "
			   + "   GROUP  BY OL.id,i.id, "
			   + "             SG.NAME, "
			   + "             P.project_name, "
			   + "             I.inote_date,   "
			   + "             po.inote_type_id ,po.order_no,po.id,org.organization_name"
			   + "   ORDER  BY inote_status DESC"
			   , nativeQuery = true)
	List<Object[]> getOlApprovalDetailsByOrgId(@Param("orgId") int orgId);
	
	@Query(value =" SELECT OL.id, i.id as inote_id , "
			   + " OL.ol_number , "
			   + " COALESCE(To_char(OL.offer_date , 'dd-mm-yyyy'), '') AS offer_date, "
			   + " SG.NAME AS sbu_name,  "
			   + " P.project_name,  "
			   + " COALESCE(To_char(I.inote_date, 'dd-mm-yyyy'), '')  AS inote_date, "
			   + " po.inote_type_id ,  "
			   + " CASE  "
			   + "   WHEN Count(I.id) > 0 THEN 'Issued'  "
			   + "   ELSE 'Pending'  "
			   + " END AS INOTE_STATUS ,po.order_no ,i.consignee, OL.id as olid,po.id as supplyOrderId,org.organization_name ,"
			            + "CAST(COUNT(DISTINCT CASE WHEN OLSN.status = 'pass' THEN OLSN.id END) AS INTEGER) AS accepted,\r\n"
			            + "CAST(COUNT(DISTINCT CASE WHEN OLSN.status = 'fail' THEN OLSN.id END) AS INTEGER) AS rejected,\r\n"
			            + "CAST(COUNT(DISTINCT CASE WHEN OLSN.status = 'rfr' THEN OLSN.id END) AS INTEGER) AS rfr "
			   + " FROM tdms.offer_letter OL  "
			   + " LEFT JOIN  tdms.ol_approval_details oad ON OAD.offer_letter_id  = OL.id  "
			   + " LEFT JOIN mdms.employee E ON OAD.approved_by = E.id "
			   + " LEFT JOIN tdms.sale_order SO ON ol.sale_order_id  = SO.id  "
			   + " LEFT JOIN tdms.supply_order PO ON SO.supply_order_id = PO.id  "
			   + " left join tdms.so_line_item sli on sli.so_id = PO.id "
			   + " LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id  "
			   + " LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id  "
			   +"  LEFT JOIN tdms.inote_line_item IL ON IL.inote_id=I.id "
			   + " LEFT JOIN tdms.category c ON c.id = SO.category_id  "
			   + " LEFT JOIN tdms.ship s ON s.id = c.ship_id   "
			   + " LEFT JOIN tdms.project p ON p.id = s.project_id  "
			   + " LEFT JOIN mdms.organization org ON org.id=sli.insp_auth "
			   + " LEFT JOIN tdms.ol_line_item olt on olt.offer_letter_id = OL.id "
			   + " LEFT JOIN tdms.ol_line_item_serial_no olsn ON olsn.ol_line_item_id = olt.id "
			   + " WHERE OAD.approval_status = 1 AND sli.insp_auth  = :orgId "
			//   + "AND E.emp_role_id = 2  "
			   + " GROUP BY OL.id,i.id, SG.NAME, P.project_name, I.inote_date, po.inote_type_id , po.order_no, po.id, org.organization_name "
			   + " ORDER BY inote_status DESC ", nativeQuery = true)
	List<Object[]> getOlApprovalDetailsByOrgIdV2(@Param("orgId") Integer orgId);

	@Query(value = "  SELECT OL.id, i.id as inote_id ,   \r\n"
		    + "       OL.ol_number ,   \r\n"
		    + "       COALESCE(To_char(OL.offer_date , 'dd-mm-yyyy'), '') AS offer_date,   \r\n"
		    + "       SG.NAME AS sbu_name,    \r\n"
		    + "       P.project_name,    \r\n"
		    + "       COALESCE(To_char(I.inote_date, 'dd-mm-yyyy'), '')  AS inote_date,   \r\n"
		    + "       po.inote_type_id ,    \r\n"
		    + "       CASE    \r\n"
		    + "         WHEN Count(I.id) > 0 THEN 'Issued'    \r\n"
		    + "         ELSE 'Pending'    \r\n"
		    + "       END AS INOTE_STATUS ,\r\n"
		    + "    po.order_no , cn.name ,   OL.id as olid, "
		    + "    po.id as supplyOrderId,\r\n"
		    + "    org.organization_name ,  \r\n"
//		    + "           CAST(COUNT(DISTINCT CASE WHEN lisn.ser_no_sts_id <> 5 THEN OLSN.id END) AS INTEGER) AS accepted,  "
//		    + "           CAST(COUNT(DISTINCT CASE WHEN lisn.ser_no_sts_id = 5  THEN OLSN.id END) AS INTEGER) AS rejected  "
            + "    IL.accepted AS accepted, \r\n "
            + "    IL.rejected AS rejected \r\n "
		    + "       FROM tdms.offer_letter OL    \r\n"
		    + "       LEFT JOIN  tdms.ol_approval_details oad ON OAD.offer_letter_id  = OL.id   "
		    + "       LEFT JOIN tdms.sale_order SO ON ol.sale_order_id  = SO.id    "
		    + "       LEFT JOIN tdms.so_line_item sli ON sli.sale_order_id = SO.id    "
		    + "       LEFT JOIN tdms.supply_order po ON po.id = sli.so_id        "
		    + "       left join mdms.consignee cn on cn.id = OL.consignee_id      "
		    + "    LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id    "
		    + "       LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id    "
		    + "       LEFT JOIN tdms.inote_line_item IL ON IL.inote_id=I.id   "
		    + "       LEFT JOIN tdms.category c ON c.id = SO.category_id    "
		    + "       LEFT JOIN tdms.ship s ON s.id = c.ship_id     "
		    + "       LEFT JOIN tdms.project p ON p.id = s.project_id    "
		    + "       LEFT JOIN mdms.organization org ON org.id=sli.insp_auth   "
		    + "       LEFT JOIN tdms.ol_line_item olt on olt.offer_letter_id = OL.id   "
		    + "       LEFT JOIN tdms.ol_line_item_serial_no olsn ON olsn.ol_line_item_id = olt.id  "
		    + "       left join tdms.line_item_serial_no lisn on lisn.id = olsn.serial_no_id   "
		    + "       WHERE OAD.approval_status = 1 AND sli.insp_auth  = :orgId   "
		    + "       GROUP BY OL.id,i.id, SG.NAME, P.project_name, I.inote_date, po.inote_type_id , po.order_no, po.id, org.organization_name, cn.name, IL.accepted, IL.rejected  "
		    + "       ORDER BY OL.created_datetime DESC   ", nativeQuery = true)
  List<Object[]> getOlApprovalDetailsByOrgIdV3(@Param("orgId") Integer orgId);
	
  @Query(value = "SELECT OL.id ,"
		   + "       OL.ol_number,"
		   + "       COALESCE(To_char(OL.offer_date, 'dd-mm-yyyy'), '') AS offer_date,"
		   + "       SG.NAME AS sbu_name, "
		   + "       P.project_name, "
		   + "       COALESCE(To_char(I.inote_date, 'dd-mm-yyyy'), '')  AS inote_date,"
		   + "       po.inote_type_id, cl.id as clearance_id , "
		   + "       CASE "
		   + "         WHEN Count(I.id) > 0 THEN 'Issued' "
		   + "         ELSE 'Pending' "
		   + "       END AS INOTE_STATUS "
		   + "FROM   tdms.offer_letter OL "
		   + "       LEFT JOIN tdms.ol_approval_details OAD "
		   + "              ON OAD.offer_letter_id = OL.id "
		   + " Left join tdms.clearance_letter cl " 
		   + " on cl.offer_letter_id= ol.id "
		   + "       LEFT JOIN mdms.employee E "
		   + "              ON OAD.approved_by = E.id "
		   + "       LEFT JOIN tdms.sale_order SO "
		   + "              ON OL.sale_order_id = SO.id "
		   + "       LEFT JOIN tdms.supply_order PO "
		   + "              ON SO.supply_order_id = PO.id "
		   + "       LEFT JOIN mdms.sales_group SG "
		   + "              ON PO.sales_grp_id = SG.id "
		   + "       LEFT JOIN tdms.inote I "
		   + "              ON I.offer_letter_id = OL.id "
		   + "       LEFT JOIN tdms.category c"
		   + "            ON c.id = SO.category_id "
		   + "       LEFT JOIN tdms.ship s"
		   + "               ON s.id = c.ship_id " 
		   + "      LEFT JOIN tdms.project p "
		   + "              ON p.id = s.project_id "
		   + "WHERE  OAD.approval_status = 1 "
		   + "       AND PO.organization_id = :orgId "
		   + "       AND E.emp_role_id = :empRoleId "
		   + "GROUP  BY OL.id, "
		   + "          SG.NAME, "
		   + "          P.project_name, "
		   + "          I.inote_date,  clearance_id , "
		   + "          po.inote_type_id "
		   + "ORDER  BY inote_status DESC"
		   , nativeQuery = true)
	List<Object[]> getOlApprovalDetailsByOrgIdAndEmpId(@Param("orgId") int orgId, @Param("empRoleId") int empRoleId);
	
	
	
	@Query(value = "SELECT "
			+ "  COALESCE(SUM(TOTAL_ACTIVE_OL), 0) AS TOTAL_ACTIVE_OL "
			+ "FROM "
			+ "  ("
			+ "    SELECT "
			+ "      ("
			+ "        CASE WHEN OL.OL_STATUS_ID = 1 THEN COUNT(OL.ID) ELSE 0 END"
			+ "      ) AS TOTAL_ACTIVE_OL "
			+ "    FROM "
			+ "      TDMS.OFFER_LETTER OL "
			+ "      LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID "
			+ "      LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID "
			+ "      LEFT JOIN TDMS.PROJECT P ON P.SUPPLY_ORDER_ID = SO.ID "
			+ "      LEFT JOIN MDMS.EMPLOYEE E ON OL.EMPLOYEE_ID = E.ID "
			+ "    WHERE "
			+ "      ( :inspectorId = -1 OR OL.EMPLOYEE_ID = :inspectorId) "
			+ "      AND (:projectId = -1 OR P.ID = :projectId) "
			+ "      AND PO.ORGANIZATION_ID = :orgId "
			+ "    GROUP BY "
			+ "      OL.OL_STATUS_ID "
			+ "  ) T"
			+ "", nativeQuery = true)
	Integer getActiveOlForDashboard(@Param("orgId") int orgId, @Param("projectId") int projectId,
			@Param("inspectorId") int inspectorId);
	
	@Query(value = "SELECT "
			+ "  MONTH_NAME, "
			+ "  SUM(RETURNED_OL) AS RETURNED_OL, "
			+ "  SUM(CLEARED_OL) AS CLEARED_OL,"
			+ "  SUM(TOTAL_OL) AS TOTAL_OL "
			+ "FROM "
			+ "  ("
			+ "    SELECT "
			+ "      TO_CHAR(OL.CREATED_DATETIME, 'MON') AS MONTH_NAME,"
			+ "   COUNT(OL.ID) AS TOTAL_OL,"
			+ "      CASE WHEN OL.OL_STATUS_ID = 6 THEN COUNT(OL.ID) ELSE 0 END AS RETURNED_OL,"
			+ "   CASE WHEN OL.OL_STATUS_ID = 3 THEN COUNT(OL.ID) ELSE 0 END AS CLEARED_OL"
			+ "    FROM "
			+ "      TDMS.OFFER_LETTER OL "
			+ "   LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID "
			+ "   LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID"
			+ "   WHERE :orgId = -1 OR PO.ORGANIZATION_ID = :orgId"
			+ "   AND OL.OFFER_DATE >= :startDate AND OL.OFFER_DATE <= :endDate"
			+ "    GROUP BY "
			+ "      OL.CREATED_DATETIME, "
			+ "      MONTH_NAME, OL.OL_STATUS_ID"
			+ "  ) T "
			+ "GROUP BY "
			+ "  MONTH_NAME", nativeQuery = true)
	List<Object[]> getReturnedNClearedOlsDashboard(@Param("orgId") int orgId, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
	
	
	@Query(value="SELECT COALESCE(SUM(olLi.cleared_qty),0) FROM tdms.ol_line_item olLi "
			+ "WHERE olLi.so_line_item_id = :soLiId", nativeQuery=true)
	BigDecimal getCountOfCompletedLineItemsBySoLiId(int soLiId);

	@Query(value= "SELECT ol.id    AS offer_letter_id, "

   +  "       ol.ol_number, "

   +  "       Coalesce(To_char(po.order_date, 'dd-mm-yyyy'), '') AS order_date, "

   +  "       po.order_no, "

   +  "       p.project_name, "

   +  "       CASE "

   +  "         WHEN rol.id != 0 THEN 'Returned' "

   +  "         ELSE 'Not Returned' "

   +  "       end                                                AS is_returned, "

   +  "       (SELECT CASE "

   +  "                 WHEN ( oad.approval_status = 1 "

   +  "                        AND OAD.approved_by=3 ) THEN 'Approved by CQAO' "

   +  "                 ELSE 'Held with ' "

   +  "                      || (SELECT r.emp_role "

   +  "                          FROM   mdms.emp_role r "

   +  "                                 LEFT JOIN mdms.employee ee "

   +  "                                        ON ee.emp_role_id = r.id "

   +  "                          WHERE  ee.id = oad.next_approver_id) "

   +  "               end AS OL_STATUS "

   +  "        FROM   tdms.ol_approval_details oad    "

   +  "               LEFT JOIN mdms.employee ee "

   +  "                      ON oad.approved_by = ee.id "

   +  "               LEFT JOIN mdms.emp_role r "

   +  "                      ON ee.emp_role_id = r.id "

   +  "    WHERE  oad.offer_letter_id = ol.id "

   +  "        GROUP  BY oad.id, "

   +  "                  r.emp_role, "

   +  "                  ee.emp_role_id "

   +  "        ORDER  BY oad.id DESC "

   +  "        LIMIT  1)                                         AS ol_status, "

   +  "       Coalesce(To_char(ol.offer_date, 'dd-mm-yyyy'), '') AS ol_date, "

   +  "       sg.name                                            AS sales_grp "

   +  "FROM   tdms.offer_letter ol "

   +  "       LEFT JOIN tdms.ol_approval_details oad "

   +  "              ON oad.offer_letter_id = ol.id "

   +  "       LEFT JOIN tdms.return_offer_letter rol "

   +  "              ON rol.offer_letter_id = ol.id "

   +  "       LEFT JOIN tdms.sale_order SO "

   +  "              ON OL.sale_order_id = SO.id "

   +  "       LEFT JOIN tdms.category c "

   +  "              ON so.category_id = c.id "

   +  "       LEFT JOIN tdms.ship s "

   +  "              ON c.ship_id = s.id "

   +  "       LEFT JOIN tdms.project p "

   +  "              ON s.project_id = p.id "

   +  "       LEFT JOIN tdms.supply_order PO "

   +  "              ON SO.supply_order_id = PO.id  "

   +  "       LEFT JOIN mdms.sales_group sg "

   +  "              ON sg.id = po.sales_grp_id  "

   +  "WHERE  oad.next_approver_id = :empId  "

   +  "UNION "

   +  "SELECT ol.id                                              AS offer_letter_id, "

   +  "       ol.ol_number, "

   +  "       Coalesce(To_char(po.order_date, 'dd-mm-yyyy'), '') AS order_date, "

   +  "       po.order_no, "

   +  "       p.project_name, "

   +  "       CASE "

   +  "         WHEN rol.id != 0 THEN 'Returned' "

   +  "         ELSE 'Not Returned' "

   +  "       end                                                AS is_returned, "

   +  "       (SELECT CASE "

   +  "                 WHEN ( oad.approval_status = 1 And OAD.approved_by=3 ) THEN 'Approved by CQAO' "

   +  "                 ELSE 'Held with ' "

   +  "                      || (SELECT r.emp_role "

   +  "                          FROM   mdms.emp_role r "

   +  "                                 LEFT JOIN mdms.employee ee "

   +  "                                        ON ee.emp_role_id = r.id "

   +  "                          WHERE  ee.id = oad.next_approver_id) "

   +  "               end AS OL_STATUS "

   +  "        FROM   tdms.ol_approval_details oad "

   +  "               LEFT JOIN mdms.employee e "

   +  "                      ON oad.approved_by = e.id "

   +  "               LEFT JOIN mdms.emp_role er "

   +  "                      ON e.emp_role_id = er.id "

   +  "        WHERE  oad.offer_letter_id = ol.id "

   +  "        GROUP  BY oad.id, "

   +  "                  er.emp_role, "

   +  "                  e.emp_role_id "

   +  "        ORDER  BY oad.id DESC  "

   +  "        LIMIT  1)                                         AS ol_status,  "

   +  "       Coalesce(To_char(ol.offer_date, 'dd-mm-yyyy'), '') AS ol_date,  "

   +  "       sg.name                                            AS sales_grp  "

   +  "FROM   tdms.offer_letter ol "

   +  "       LEFT JOIN tdms.ol_allotment oa  "

   +  "              ON oa.offer_letter_id = ol.id  "

   +  "       LEFT JOIN tdms.return_offer_letter rol  "

   +  "              ON rol.offer_letter_id = ol.id  "

   +  "       LEFT JOIN tdms.sale_order SO  "

   +  "              ON OL.sale_order_id = SO.id  "

   +  "       LEFT JOIN tdms.category c  "

   +  "              ON so.category_id = c.id  "

   +  "       LEFT JOIN tdms.ship s  "

   +  "              ON c.ship_id = s.id  "

   +  "       LEFT JOIN tdms.project p  "

   +  "              ON s.project_id = p.id  "

   +  "       LEFT JOIN tdms.supply_order PO  "

   +  "              ON SO.supply_order_id = PO.id  "

   +  "       LEFT JOIN mdms.sales_group sg  "

   +  "              ON sg.id = po.sales_grp_id  "

   +  "WHERE  oa.alloted_to = :empId  "

   , nativeQuery=true)
	List<Object[]> getOlAllotedForApproval(int empId);
	
	@Query(value="SELECT COALESCE(SUM(OLI.CLEARED_QTY),0) AS DELIVERED_QTY FROM TDMS.OL_LINE_ITEM OLI "
			+ "LEFT JOIN TDMS.OFFER_LETTER OL ON OLI.OFFER_LETTER_ID = OL.ID WHERE OL.ID = :olId "
			+ "AND OLI.SO_LINE_ITEM_ID = :soLineItemId", nativeQuery=true)
	int getDlvrddQtyByOlIdNSoLineItemId(int olId, int soLineItemId);

	@Query(value = "SELECT COALESCE(Sum(active_ol), 0)  AS ACTIVE_OL "
//			+ "       COALESCE(Sum(pending_ol), 0) AS PENDING_OL "
			+ "FROM   (SELECT ( CASE "
			+ "                   WHEN OL.ol_status_id in (1,4,5,7) THEN Count(OL.id) "
			+ "                   ELSE 0 "
			+ "                 END ) AS ACTIVE_OL "
//			+ "               ( CASE "
//			+ "                   WHEN OL.ol_status_id = 4 THEN Count(OL.id) "
//			+ "                   ELSE 0 "
//			+ "                 END ) AS PENDING_OL "
			+ "        FROM   tdms.offer_letter OL "
			+ "               LEFT JOIN tdms.sale_order SO "
			+ "                      ON OL.sale_order_id = SO.id "
			+ "               LEFT JOIN tdms.supply_order PO "
			+ "                      ON SO.supply_order_id = PO.id "
			+ "               LEFT JOIN tdms.project P "
			+ "                      ON P.supply_order_id = PO.id "
			+ "        WHERE  ( :projectId = -1 "
			+ "                  OR P.id = :projectId ) "
			+ "               AND ( :orgId = -1 "
			+ "                      OR PO.organization_id = :orgId ) "
			+ "        GROUP  BY ol_status_id)T ", nativeQuery = true)
	List<Object[]> getProjectWiseOls(int orgId, int projectId);
	
	@Query(value = "SELECT COALESCE(Sum(IN_PROGRESS), 0)  AS IN_PROGRESS, "
			+ "       COALESCE(Sum(PENDING_OL), 0) AS PENDING_OL "
			+ "FROM   (SELECT ( CASE "
			+ "                   WHEN OL.ol_status_id = 5 THEN Count(OL.id) "
			+ "                   ELSE 0 "
			+ "                 END ) AS IN_PROGRESS, "
			+ "               ( CASE "
			+ "                   WHEN OL.ol_status_id = 4 THEN Count(OL.id) "
			+ "                   ELSE 0 "
			+ "                 END ) AS PENDING_OL "
			+ "        FROM   tdms.offer_letter OL "
			+ "               LEFT JOIN tdms.sale_order SO "
			+ "                      ON OL.sale_order_id = SO.id "
			+ "               LEFT JOIN tdms.supply_order PO "
			+ "                      ON SO.supply_order_id = PO.id "
			+ "               LEFT JOIN tdms.ol_allotment OA "
			+ "                      ON OA.offer_letter_id = OL.id "
			+ "               LEFT JOIN mdms.employee E "
			+ "                      ON OA.alloted_to = E.id "
			+ "        WHERE  ( :orgId = -1 "
			+ "                  OR PO.organization_id = :orgId ) "
			+ "               AND ( ( :inspectorId = -1 "
			+ "                        OR OA.alloted_to = :inspectorId ) "
			+ "                     AND E.emp_role_id = 7 ) "
			+ "        GROUP  BY ol_status_id)T ", nativeQuery = true)
	List<Object[]> getInspectorWiseOls(int orgId, int inspectorId);
	
	
	/**
	 * NOTE: Since 'warrant_id' is no longer stored directly in the Offer Letter table, 
	 * this query uses 'merge_sslip_id' to bridge through the merge_sslip_mapping
	 * 
	 * @since 26-05-2026
	 * 
	 * @param offerLetterId
	 * @return
	 */
	@Query(value="SELECT DISTINCT po.order_no \r\n"
			+ "FROM tdms.supply_order po \r\n"
			+ "JOIN tdms.so_line_item sli ON sli.so_id = po.id \r\n"
			+ "JOIN tdms.category c ON c.so_line_item_id = sli.id \r\n"
			+ "JOIN tdms.submission_slip ss ON ss.warrant_id = c.id \r\n"
			+ "JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = ss.id \r\n"
			+ "JOIN tdms.offer_letter ol ON ol.merge_sslip_id = msm.merge_sslip_id \r\n"
			+ "WHERE ol.id = :offerLetterId ", nativeQuery=true)
	String getSoNumberByOlId(int offerLetterId);
	
	@Query(value="SELECT CASE "
			+ "         WHEN COUNT(oad.*) = 0 THEN 'File Note not generated' "
			+ "         WHEN ( oad.approval_status = 1 "
			+ "                AND emp_role_id = 3 ) THEN 'Approved by CQAO' "
			+ "         ELSE 'Held with ' "
			+ "              || (SELECT r.emp_role "
			+ "                  FROM   mdms.emp_role r "
			+ "                         LEFT JOIN mdms.employee ee "
			+ "                                ON ee.emp_role_id = r.id "
			+ "                  WHERE  ee.id = oad.next_approver_id) "
			+ "       end AS OL_STATUS "
			+ "FROM   tdms.ol_approval_details oad "
			+ "       LEFT JOIN tdms.offer_letter ol "
			+ "              ON oad.offer_letter_id = ol.id "
			+ "       LEFT JOIN mdms.employee e "
			+ "              ON oad.approved_by = e.id "
			+ "       LEFT JOIN mdms.emp_role er "
			+ "              ON e.emp_role_id = er.id "
			+ "WHERE  oad.offer_letter_id = :olId "
			+ "GROUP  BY oad.id, "
			+ "          er.emp_role, "
			+ "          e.emp_role_id "
			+ "ORDER  BY oad.id DESC "
			+ "LIMIT  1", nativeQuery=true)
	String getFileNoteStatusByOlId(int olId);

	@Query(value = "select " +
			"ol.ol_number, " +
			"ol.offer_date, " +
			"po.order_no, " +
			"po.order_date as indent_date, " +
			"po.order_date, " +
			"STRING_AGG(distinct p.project_name, " +
			"', ') as project_names, " +
			"COUNT(oli.*) as total_items, " +
			"SUM(oli.offered_qty) as total_qty, " +
			"SUM(oli.offered_qty * sli.unit_cost) as ol_value, " +
			"po.dd " +
			"from " +
			"tdms.offer_letter ol " +
			"left join tdms.sale_order so  " +
			"              on " +
			"ol.sale_order_id = so.id " +
			"left join tdms.supply_order po on " +
			"so.supply_order_id = po.id " +
			"left join tdms.ol_line_item oli on " +
			"oli.offer_letter_id = ol.id " +
			"left join tdms.so_line_item sli on " +
			"oli.so_line_item_id = sli.id " +
			"left join tdms.project p on " +
			"p.supply_order_id = po.id " +
			"where " +
			"To_timestamp(cast(ol.offer_date as text), " +
			"'YYYY-MM-DD') >= To_timestamp(:fromDate, " +
			"'YYYY-MM-DD') " +
			"and To_timestamp(cast(ol.offer_date as text), " +
			"'YYYY-MM-DD') <= To_timestamp(:toDate, " +
			"'YYYY-MM-DD') " +
			"and po.organization_id = :orgId " +
			"group by " +
			"ol.ol_number, " +
			"ol.offer_date, " +
			"po.order_no, " +
			"po.order_date, " +
			"po.dd", nativeQuery = true)
	List<Object[]> getOLRegister(int orgId, String fromDate, String toDate);


	@Query(value = "with EarliestNotification as ( " +
			   "select " +
			   " nf.category_id," +
			   " nf.created_datetime," +
			   " nf.receiver," +
			   " row_number() over (partition by nf.category_id " +
			   "order by " +
			   " nf.created_datetime asc) as rn " +
			   "from " +
			   " tdms.notification nf " +
			   "where " +
			   " nf.notification_category = 'OL' " +
			   "), "
			   + " AllotmentCombined as ( "
			   + "  SELECT offer_letter_id, alloted_to, alloted_by "
			   + "  FROM tdms.ol_allotment "
			   + "  UNION "
			   + "  SELECT offer_letter_id, alloted_to, alloted_by "
			   + "  FROM tdms.qar_allotment "
			   + ")" +
			   "select " +
			   " OL.ID," +
			   " OL.OL_NUMBER," +
			   " OL.OL_status_id," +
			   " coalesce(TO_CHAR(OL.OFFER_DATE," +
			   " 'dd-mm-yyyy')," +
			   " '') as OFFER_DATE," +
			   " SG.NAME as SBU_NAME," +
			   " P.PROJECT_NAME," +
			   " PO.INOTE_TYPE_ID," +
			   " OLS.status," +
			   " SO.sale_order_no," +
			   " PO.order_no," +
			   " NF.category_id," +
			   " coalesce(TO_CHAR(NF.created_datetime," +
			   " 'DD-MM-YYYY HH24:MI')," +
			   " 'NA') as notified_time," +
			   " NF.receiver," +
			   " case " +
			   "  when COUNT(AC.offer_letter_id) > 0 then true " +
			   "  else false " +
			   " end as is_alloted, st.so_type, " +
			   " case when count(osca.*) > 0 then true else false end as surprise_check, " +
			   " case when count(occa.*) > 0 then true else false end as cross_check " +
			   " from " +
			   " TDMS.OFFER_LETTER OL " +
			   "left join ( " +
			   " select " +
			   "  category_id," +
			   "  created_datetime," +
			   "  receiver " +
			   " from " +
			   "  EarliestNotification " +
			   " where " +
			   "  rn = 1 " +
			   ") NF on " +
			   " OL.id = NF.category_id " +
			   "LEFT JOIN tdms.ol_surprise_check_allotment osca ON ol.id = osca.offer_letter_id " +
			         "LEFT JOIN tdms.ol_cross_check_allotment occa ON ol.id = occa.offer_letter_id " +
			   "left join TDMS.SALE_ORDER SO on " +
			   " OL.SALE_ORDER_ID = SO.ID " +
			   "left join TDMS.SUPPLY_ORDER PO on " +
			   " SO.SUPPLY_ORDER_ID = PO.ID " +
			   "left join mdms.sales_group SG on " +
			   " PO.sales_grp_id = SG.id " +
			   "left join tdms.inote I on " +
			   " I.offer_letter_id = OL.id " +
			   "left join tdms.category C on " +
			   " C.id = SO.category_id " +
			   "left join tdms.ship S on " +
			   " S.id = C.ship_id " +
			   "left join tdms.project P on " +
			   " P.id = S.project_id " +
			   "left join mdms.ol_status OLS on " +
			   " OL.ol_status_id = OLS.id " +
			   " LEFT JOIN AllotmentCombined AC on "+
			    "    OL.ID = AC.offer_letter_id " +
			   "left join mdms.employee e on " +
			   " e.id = :empId " +
			   "left join mdms.emp_role er on " +
			   " er.id = e.emp_role_id " +
			   "left join mdms.so_type st on " +
			   " po.so_type_id = st.id " +
			   "where " +
			   " PO.ORGANIZATION_ID = :orgId " +
			   " and ( " +
			   "      e.emp_role_id = 2 " +
			   "  or " +
			   "      (NF.receiver = :empId " +
			   "  or AC.alloted_to = :empId " +
			   "  or AC.alloted_by = :empId) " +
			   "  ) " +
			   "group by " +
			   " OL.ID," +
			   " OL.OL_NUMBER," +
			   " OL.OL_status_id," +
			   " OL.OFFER_DATE," +
			   " SG.NAME," +
			   " P.PROJECT_NAME," +
			   " PO.INOTE_TYPE_ID," +
			   " OLS.status," +
			   " SO.sale_order_no," +
			   " PO.order_no," +
			   " NF.category_id," +
			   " NF.created_datetime," +
			   " NF.receiver, st.so_type " +
			   "order by " +
			   " OL.OFFER_DATE desc", nativeQuery = true)
	List<Object[]> getOLsByStatus(int orgId, int empId);

	@Query(value = "with EarliestNotification as ( " +
			"select  " +
			" nf.category_id, " +
			" nf.created_datetime, " +
			" nf.receiver, " +
			" row_number() over (partition by nf.category_id, nf.receiver " +
			"order by  " +
			" nf.created_datetime asc) as rn " +
			"from  " +
			" tdms.notification nf " +
			"where  " +
			" nf.notification_category = 'OL'  " +
			"),  " +
			" AllotmentCombined as ( " +
			"select " +
			" offer_letter_id, " +
			" alloted_to, " +
			" alloted_by " +
			"from " +
			" tdms.ol_allotment " +
			"union " +
			"select " +
			" offer_letter_id, " +
			" alloted_to, " +
			" alloted_by " +
			"from " +
			" tdms.qar_allotment  " +
			") " +
			"select  " +
			" OL.ID, " +
			" OL.OL_NUMBER, " +
			" OL.OL_status_id, " +
			" coalesce(TO_CHAR(OL.OFFER_DATE, " +
			" 'dd-mm-yyyy'), " +
			" '') as OFFER_DATE, " +
			" SG.NAME as SBU_NAME, " +
			" P.PROJECT_NAME, " +
			" PO.INOTE_TYPE_ID, " +
			" OLS.status, " +
			" SO.sale_order_no, " +
			" PO.order_no, " +
			" NF.category_id, " +
			" coalesce(TO_CHAR(NF.created_datetime, " +
			" 'DD-MM-YYYY HH24:MI'), " +
			" 'NA') as notified_time, " +
			" NF.receiver, " +
			" case  " +
			"  when COUNT(AC.offer_letter_id) > 0 then true " +
			"  else false " +
			" end as is_alloted, " +
			" st.so_type, " +
			" case " +
			"  when count(osca.*) > 0 then true " +
			"  else false " +
			" end as surprise_check, " +
			" case " +
			"  when count(occa.*) > 0 then true " +
			"  else false " +
			" end as cross_check " +
			"from  " +
			" TDMS.OFFER_LETTER OL " +
			"left join ( " +
			" select  " +
			"  category_id, " +
			"  created_datetime, " +
			"  receiver " +
			" from  " +
			"  EarliestNotification " +
			" where  " +
			"  rn = 1  " +
			") NF on  " +
			" OL.id = NF.category_id " +
			"left join tdms.ol_surprise_check_allotment osca on " +
			" ol.id = osca.offer_letter_id " +
			"left join tdms.ol_cross_check_allotment occa on " +
			" ol.id = occa.offer_letter_id " +
			"left join TDMS.SALE_ORDER SO on  " +
			" OL.SALE_ORDER_ID = SO.ID " +
			"left join TDMS.SUPPLY_ORDER PO on  " +
			" SO.SUPPLY_ORDER_ID = PO.ID " +
			"left join mdms.sales_group SG on  " +
			" PO.sales_grp_id = SG.id " +
			"left join tdms.inote I on  " +
			" I.offer_letter_id = OL.id " +
			"left join tdms.category C on  " +
			" C.id = SO.category_id " +
			"left join tdms.ship S on  " +
			" S.id = C.ship_id " +
			"left join tdms.project P on  " +
			" P.id = S.project_id " +
			"left join mdms.ol_status OLS on  " +
			" OL.ol_status_id = OLS.id " +
			"left join AllotmentCombined AC on " +
			" OL.ID = AC.offer_letter_id " +
			"left join mdms.employee e on  " +
			" e.id = :empId " +
			"left join mdms.emp_role er on  " +
			" er.id = e.emp_role_id " +
			"left join mdms.so_type st on  " +
			" po.so_type_id = st.id " +
			"where  " +
			" SG.ORGANIZATION_ID = :orgId " +
			" and  " +
			"      (NF.receiver = :empId " +
			"  or AC.alloted_to = :empId " +
			"  or AC.alloted_by = :empId)  " +
			"   " +
			"group by  " +
			" OL.ID, " +
			" OL.OL_NUMBER, " +
			" OL.OL_status_id, " +
			" OL.OFFER_DATE, " +
			" SG.NAME, " +
			" P.PROJECT_NAME, " +
			" PO.INOTE_TYPE_ID, " +
			" OLS.status, " +
			" SO.sale_order_no, " +
			" PO.order_no, " +
			" NF.category_id, " +
			" NF.created_datetime, " +
			" NF.receiver, " +
			" st.so_type " +
			"order by  " +
			" OL.OFFER_DATE desc", nativeQuery = true)
	List<Object[]> getOLsByStatusForFirm(int orgId, int empId);


  @Query(value = "with " +
      " AllotmentCombined as ( " +
      " select " +
      " offer_letter_id, " +
      " alloted_to, " +
      " alloted_by, " +
      " created_datetime " +
      " from tdms.ol_allotment ) " +
      " select " +
      " OL.ID, " +
      " OL.OL_NUMBER, " +
      " OL.OL_status_id, " +
      " coalesce(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'), '') as OFFER_DATE, " +
      " SG.NAME as SBU_NAME, " +
      " P.PROJECT_NAME, " +
      " PO.INOTE_TYPE_ID, " +
      " OLS.status, " +
      " SO.sale_order_no, " +
      " PO.order_no, " +
      " pn.part_no, " +
      " string_agg(distinct cat.category_name, ', '), " +
      " case " +
      "  when COUNT(AC.offer_letter_id) > 0 then true " +
      "   else false " +
      "  end as is_alloted, " +
      " st.so_type " +
      " from TDMS.OFFER_LETTER OL " +
      " left join tdms.sale_order so on so.id = ol.sale_order_id " +
      " left join tdms.so_line_item sli on sli.sale_order_id = so.id " +
      " left join tdms.supply_order po on po.id = sli.so_id " +
      " left join tdms.category cat on cat.so_line_item_id = sli.id " +
      " left join mdms.sales_group SG on PO.sales_grp_id = SG.id " +
      " left join tdms.inote I on I.offer_letter_id = OL.id " +
      " left join tdms.category C on C.id = SO.category_id " +
      " left join tdms.ship S on S.so_line_item_id = sli.id " +
      " left join tdms.project P on P.id = S.project_id " +
      " left join mdms.part_number pn on pn.id = sli.part_number_id " +
      " left join mdms.ol_status OLS on OL.ol_status_id = OLS.id " +
      " left join AllotmentCombined AC on OL.ID = AC.offer_letter_id " +
      " left join mdms.employee e on e.id = :empId " +
      " left join mdms.emp_role er on er.id = e.emp_role_id " +
      " left join mdms.so_type st on po.so_type_id = st.id " +
      " where (AC.alloted_to = :empId or AC.alloted_by = :empId) " +
      " group by " +
      " OL.ID, " +
      " OL.OL_NUMBER, " +
      " OL.OL_status_id, " +
      " OL.OFFER_DATE, " +
      " SG.NAME, " +
      " P.PROJECT_NAME, " +
      " PO.INOTE_TYPE_ID, " +
      " OLS.status, " +
      " SO.sale_order_no, " +
      " PO.order_no, " +
      " st.so_type , " +
      " pn.part_no, " +
      " so.sale_order_no " +
      " order by MAX(AC.created_datetime) desc", nativeQuery = true)
	List<Object[]> getAllottedOLsByStatusFroFirm( @Param("empId") Integer empId);
	
	// "Retrieves all offer letter currently assigned to the logged-in employee for approval,
	// filtering by organization and ensuring the offer letter has completed at least one inspection stage."
	// Author: Shridhar R
		@Query(value = "SELECT\r\n"
				+ "     OL.ID,\r\n"
				+ "     OL.OL_NUMBER,\r\n"
				+ "     OL.OL_status_id,\r\n"
				+ "     COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'), '') as OFFER_DATE,\r\n"
				+ "     SG.NAME as SBU_NAME,\r\n"
				+ "     P.PROJECT_NAME,\r\n"
				+ "     PO.INOTE_TYPE_ID,\r\n"
				+ "     OLS.status,\r\n"
				+ "     SO.sale_order_no,\r\n"
				+ "     PO.order_no,\r\n"
				+ "     pn.part_no,\r\n"
				+ "     C.category_name,\r\n"
				+ "     TRUE as is_alloted,\r\n"
				+ "     st.so_type\r\n"
				+ "FROM TDMS.OFFER_LETTER OL\r\n"
				+ "LEFT JOIN tdms.ol_line_item oli ON oli.offer_letter_id = OL.id\r\n"
				+ "LEFT JOIN ate.insp_round ir ON ir.ol_line_item_id = oli.id\r\n"
				+ "LEFT JOIN tdms.line_item_serial_no lisn ON lisn.id = ir.ol_ser_no_id\r\n"
				+ "LEFT JOIN tdms.ol_line_item_serial_no olisn ON olisn.serial_no_id = lisn.id\r\n"
				+ "LEFT JOIN tdms.so_line_item sli ON sli.id = oli.so_line_item_id\r\n"
				+ "LEFT JOIN tdms.supply_order PO ON PO.ID = sli.so_id\r\n"
				+ "LEFT JOIN tdms.sale_order SO ON SO.id = sli.sale_order_id\r\n"
				+ "LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id\r\n"
				+ "LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id\r\n"
				+ "LEFT JOIN tdms.category C ON C.id = SO.category_id\r\n"
				+ "LEFT JOIN tdms.ship S ON S.so_line_item_id = sli.id\r\n"
				+ "LEFT JOIN tdms.project P ON P.id = S.project_id\r\n"
				+ "LEFT JOIN mdms.part_number pn ON pn.id = sli.part_number_id\r\n"
				+ "LEFT JOIN mdms.ol_status OLS ON OL.ol_status_id = OLS.id\r\n"
				+ "LEFT JOIN LATERAL (\r\n"
				+ "     SELECT ac.alloted_to, ac.alloted_by, ac.created_datetime \r\n"
				+ "     FROM tdms.ol_allotment ac\r\n"
				+ "     WHERE ac.offer_letter_id = OL.id\r\n"
				+ "     ORDER BY ac.created_datetime DESC\r\n"
				+ "     LIMIT 1\r\n"
				+ ") LatestAC ON TRUE\r\n"
				+ "LEFT JOIN mdms.employee e ON e.id = :empId\r\n"
				+ "LEFT JOIN mdms.emp_role er ON er.id = e.emp_role_id\r\n"
				+ "LEFT JOIN mdms.so_type st ON po.so_type_id = st.id\r\n"
				+ "WHERE \r\n"
//				+ "    SG.ORGANIZATION_ID = 2 \r\n"
//				+ "	AND \r\n" 
				+ "     LatestAC.alloted_to = :empId\r\n"
				+ "GROUP BY\r\n"
				+ "     OL.ID, \r\n"
				+ "     OL.OL_NUMBER, \r\n"
				+ "     OL.OL_status_id, \r\n"
				+ "     OL.OFFER_DATE, \r\n"
				+ "     SG.NAME,\r\n"
				+ "     P.PROJECT_NAME, \r\n"
				+ "     PO.INOTE_TYPE_ID, \r\n"
				+ "     OLS.status, \r\n"
				+ "     SO.sale_order_no,\r\n"
				+ "     PO.order_no, \r\n"
				+ "     st.so_type, \r\n"
				+ "     pn.part_no, \r\n"
				+ "     C.category_name, \r\n"
				+ "     so.sale_order_no,"
				+ "    LatestAC.created_datetime \r\n"
				+ "HAVING  \r\n"
				+ "    COUNT(CASE WHEN olisn.status <> 'in-process' THEN 1 END) > 0\r\n"
				+ "ORDER BY\r\n"
				+ "     LatestAC.created_datetime DESC;", nativeQuery = true)
		List<Object[]> getAllottedOLsApprovalByStatusFroFirm( @Param("empId") Integer empId);
		

	@Query(value = "with EarliestNotification as (  " +
			"select   " +
			" nf.category_id,  " +
			" nf.created_datetime,  " +
			" nf.receiver,  " +
			" row_number() over (partition by nf.category_id, nf.receiver " +
			"order by   " +
			" nf.created_datetime asc) as rn  " +
			"from   " +
			" tdms.notification nf  " +
			"where   " +
			" nf.notification_category = 'OL'  " +
			")  " +
			"select   " +
			" OL.ID,  " +
			" OL.OL_NUMBER,  " +
			" OL.OL_status_id,  " +
			" coalesce(TO_CHAR(OL.OFFER_DATE,  " +
			" 'dd-mm-yyyy'),  " +
			" '') as OFFER_DATE,  " +
			" SG.NAME as SBU_NAME,  " +
			" P.PROJECT_NAME,  " +
			" PO.INOTE_TYPE_ID,  " +
			" OLS.status,  " +
			" SO.sale_order_no,  " +
			" PO.order_no,  " +
			" NF.category_id,  " +
			" coalesce(TO_CHAR(NF.created_datetime,  " +
			" 'DD-MM-YYYY HH24:MI'),  " +
			" 'NA') as notified_time,  " +
			" NF.receiver,  " +
			"   " +
			" st.so_type,  " +
			" case  " +
			"  when count(osca.*) > 0 then true  " +
			"  else false  " +
			" end as surprise_check,  " +
			" case  " +
			"  when count(occa.*) > 0 then true  " +
			"  else false  " +
			" end as cross_check  " +
			"from   " +
			" TDMS.OFFER_LETTER OL  " +
			"left join (  " +
			" select   " +
			"  category_id,  " +
			"  created_datetime,  " +
			"  receiver  " +
			" from   " +
			"  EarliestNotification  " +
			"  where rn = 1 " +
			") NF on   " +
			" OL.id = NF.category_id  " +
			"left join tdms.ol_surprise_check_allotment osca on  " +
			" ol.id = osca.offer_letter_id  " +
			"left join tdms.ol_cross_check_allotment occa on  " +
			" ol.id = occa.offer_letter_id  " +
			"left join TDMS.SALE_ORDER SO on   " +
			" OL.SALE_ORDER_ID = SO.ID  " +
			"left join TDMS.SUPPLY_ORDER PO on   " +
			" SO.SUPPLY_ORDER_ID = PO.ID  " +
			"left join mdms.sales_group SG on   " +
			" PO.sales_grp_id = SG.id  " +
			"left join tdms.inote I on   " +
			" I.offer_letter_id = OL.id  " +
			"left join tdms.category C on   " +
			" C.id = SO.category_id  " +
			"left join tdms.ship S on   " +
			" S.id = C.ship_id  " +
			"left join tdms.project P on   " +
			" P.id = S.project_id  " +
			"left join mdms.ol_status OLS on   " +
			" OL.ol_status_id = OLS.id  " +
			"left join mdms.employee e on   " +
			" e.id = :empId  " +
			"left join mdms.emp_role er on   " +
			" er.id = e.emp_role_id  " +
			"left join mdms.so_type st on   " +
			" po.so_type_id = st.id  " +
			"where   " +
			" SG.ORGANIZATION_ID = :orgId  " +
			" and   " +
			"      (NF.receiver = :empId  " +
			"  )  " +
			"group by   " +
			" OL.ID,  " +
			" OL.OL_NUMBER,  " +
			" OL.OL_status_id,  " +
			" OL.OFFER_DATE,  " +
			" SG.NAME,  " +
			" P.PROJECT_NAME,  " +
			" PO.INOTE_TYPE_ID,  " +
			" OLS.status,  " +
			" SO.sale_order_no,  " +
			" PO.order_no,  " +
			" NF.category_id,  " +
			" NF.created_datetime,  " +
			" NF.receiver,  " +
			" st.so_type  " +
			"order by  " +
			" OL.OFFER_DATE desc", nativeQuery = true)
	List<Object[]> getNotifiedOLsByStatusForFirm(int orgId, int empId);

	@Query(value ="WITH EarliestNotification AS (       "
			   + "          SELECT       "
			   + "              nf.category_id,       "
			   + "              nf.created_datetime,       "
			   + "              nf.receiver,       "
			   + "              ROW_NUMBER() OVER (PARTITION BY nf.category_id ORDER BY nf.created_datetime ASC) AS rn       "
			   + "          FROM tdms.notification nf       "
			   + "       )       "
			   + "       SELECT       "
			   + "          OL.ID,       "
			   + "          OL.OL_NUMBER,       "
			   + "          OL.OL_status_id,       "
			   + "          COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'), '') AS OFFER_DATE,       "
			   + "          SG.NAME AS SBU_NAME,       "
			   + "          P.PROJECT_NAME,       "
			   + "          PO.INOTE_TYPE_ID,       "
			   + "          OLS.status,       "
			   + "          SO.sale_order_no,       "
			   + "          PO.order_no,       "
			   + "          NF.category_id,       "
			   + "          TO_CHAR(NF.created_datetime, 'DD-MM-YYYY HH24:MI:SS.FF3') AS formatted_datetime_ms,       "
			   + "          NF.receiver,   "
			   + "          CASE WHEN COUNT(OLA.offer_letter_id) > 0 THEN true ELSE false END AS is_alloted, st.so_type ,ol.inote_approval_remarks,ol.inote_approval    "
			   + "       FROM TDMS.OFFER_LETTER OL       "
			   + "       LEFT JOIN EarliestNotification NF ON OL.id = NF.category_id AND NF.rn = 1       "
			   + "       LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID       "
			   + "       LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID       "
			   + "       LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id       "
			   + "       LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id       "
			   + "       LEFT JOIN tdms.category C ON C.id = SO.category_id       "
			   + "       LEFT JOIN tdms.ship S ON S.id = C.ship_id       "
			   + "       LEFT JOIN tdms.project P ON P.id = S.project_id       "
			   + "       LEFT JOIN mdms.ol_status OLS ON OL.ol_status_id = OLS.id       "
			   + "       LEFT JOIN tdms.ol_allotment OLA ON OL.ID = OLA.offer_letter_id   "
			   + "    LEFT JOIN mdms.so_type st ON PO.so_type_id = st.id   "
			   + "       WHERE PO.ORGANIZATION_ID = :orgId  and OLA.offer_letter_id is not null"
			   + "       GROUP BY    "
			   + "          OL.ID, OL.OL_NUMBER, OL.OL_status_id, OL.OFFER_DATE, SG.NAME, P.PROJECT_NAME, PO.INOTE_TYPE_ID,    "
			   + "          OLS.status, SO.sale_order_no, PO.order_no, NF.category_id, NF.created_datetime, NF.receiver, st.so_type    "
			   + "       ORDER BY NF.created_datetime DESC; ", nativeQuery = true)
				List<Object[]> getOfferLettersForCqao(int orgId);
		
	
	
    @Query(value = " SELECT OL_STATUS_ID , "
		    + " COUNT(*) "
			+ " FROM TDMS.OFFER_LETTER OL "
		    + " LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID "
			+ " LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID "
		    + " WHERE PO.ORGANIZATION_ID = :orgId and  OL.is_notified is true"
			+ " GROUP BY OL_STATUS_ID ", nativeQuery = true)
    List<Object[]> getOLCount(int orgId );
	
	
    @Query(value ="SELECT OL.ID,OL.OL_NUMBER,Ol.OL_status_id,COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'),'') AS OFFER_DATE ,    "
    		   + "           SG.NAME AS SBU_NAME,    "
    		   + "           P.PROJECT_NAME,    "
    		   + "           PO.INOTE_TYPE_ID ,"
    		   + "           ols.status, il.level_of_inspection  , po.order_no,so.id as sale_order_id ,so.sale_order_no  ,ol.is_notified , po.is_gcc , po.id as supply_order_id , consignee_id, st.so_type "
    		   + "          FROM TDMS.OFFER_LETTER OL     "
    		   + "           LEFT JOIN tdms.ol_approval_details OAD ON OAD.offer_letter_id = OL.id     "
    		   + "            LEFT JOIN mdms.employee E ON OAD.approved_by = E.id     "
    		   + "          LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID     "
    		   + "          LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID     "
    		   + "           LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id    "
    		   + "            LEFT JOIN mdms.inspection_level il ON ol.inspection_level_id  = il.id   "
    		   + "             LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id     "
    		   + "                   LEFT JOIN tdms.category c     "
    		   + "                       ON c.id = SO.category_id     "
    		   + "                  LEFT JOIN tdms.ship s     "
    		   + "                          ON s.id = c.ship_id     "
    		   + "                LEFT JOIN tdms.project p     "
    		   + "                         ON p.id = s.project_id       "
    		   + "              Left Join mdms.ol_status ols on OL.ol_status_id = ols.id    "
    		   + "      LEFT JOIN mdms.so_type st ON PO.so_type_id = st.id    "
    		   + "             WHERE  PO.sales_grp_id = :salesGrpId"
    		   + "          GROUP  BY OL.id,OL.OL_NUMBER, SG.NAME, OL.OL_status_id,  P.project_name,     "
    		   + "                    po.inote_type_id ,ols.status ,il.level_of_inspection , po.order_no,so.sale_order_no,so.id  ,po.is_gcc , po.id , st.so_type      "
    		   + "           order by  OL.id desc "
    		  , nativeQuery = true)
			List<Object[]>	getOLStatusBySalesGrpId(int salesGrpId );


  @Query(value = "select  " +
      "  OL.ID,    " +
      "  OL.OL_NUMBER,    " +
      "  Ol.OL_status_id,    " +
      "  coalesce(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'), '') as OFFER_DATE ,        " +
      "  SG.NAME as SBU_NAME,    " +
      "  PO.INOTE_TYPE_ID ,    " +
      "  ols.status,     " +
      "  il.level_of_inspection,     " +
      "  po.order_no,  " +
      "  ol.is_notified,     " +
      "  po.is_gcc ,     " +
      "  po.id as supply_order_id ,     " +
      "  consignee_id,     " +
      "  string_agg(distinct c.category_name, ', '),     " +
      "  pn.part_no,    " +
      "  proj.project_name,    " +
      "  so.sale_order_no  " +
      " from TDMS.OFFER_LETTER OL  " +
      " left join tdms.sale_order so on so.id = ol.sale_order_id   " +
      " left join tdms.so_line_item sli on sli.sale_order_id = so.id   " +
      " left join tdms.supply_order po on po.id = sli.so_id   " +
      " left join tdms.category c on c.so_line_item_id = sli.id   " +
      " left join mdms.sales_group SG on SG.id = po.sales_grp_id  " +
      " left join mdms.inspection_level il on ol.inspection_level_id = il.id  " +
      " left join mdms.ol_status ols on OL.ol_status_id = ols.id  " +
      " left join mdms.part_number pn on pn.id = sli.part_number_id  " +
      " left join tdms.ship s on s.so_line_item_id = sli.id  " +
      " left join tdms.project proj on proj.id = s.project_id  " +
      " where  " +
      "  PO.sales_grp_id = :salesGrpId  " +
      " group by     " +
      "     OL.id,    " +
      "    OL.OL_NUMBER,     " +
      "    SG.NAME,     " +
      "    OL.OL_status_id,       " +
      "     po.inote_type_id,    " +
      "     ols.status ,    " +
      "     il.level_of_inspection ,     " +
      "     po.order_no,    " +
      "     po.is_gcc ,     " +
      "     po.id ,     " +
      "     pn.part_no,    " +
      "     proj.project_name,    " +
      "    so.sale_order_no  " +
      " order by  " +
      "  OL.id desc", nativeQuery = true)
	List<Object[]>	getOLBySalesGrpId(@Param("salesGrpId") Integer salesGrpId );
						
	@Query(value = " SELECT OL_STATUS_ID , "
			   + " COUNT(*) "
			   + " FROM TDMS.OFFER_LETTER OL "
			   + " LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID "
			   + " LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID "
			   + " WHERE PO.sales_grp_id = :salesGrpId "
			   + " GROUP BY OL_STATUS_ID ", nativeQuery = true)
    List<Object[]> getOLCountBySalesGrpId(int salesGrpId );
	
	 @Query(value="SELECT i.id,i.offer_letter_id,i.created_by,i.created_datetime,i.modified_by, i.modified_datetime,i.inspected_by, i.inspected_date,  i.signed_by, i.signed_date,"
             + " COALESCE(i.submitted_date, ol.offer_date) AS offer_date "
             + "FROM tdms_check.insp_ol_ib AS i"
             + "LEFT JOIN tdms.offer_letter AS ol ON i.offer_letter_id = ol.id order by offer_date", nativeQuery = true)
List<OfferLetter> getOfferLetter(@Param("id") OfferLetter id);
	 
	 
	 
	 @Query(value = "SELECT o.id,so.id as so_id,so.order_no,o.OL_NUmber,o.offer_date,sg.name ,STRING_AGG( DISTINCT   pj.project_name,',') " +
				" FROM tdms.offer_letter o " +
				" LEFT JOIN tdms.file_note_ol_mapping f ON o.id = f.offer_letter_id " +
				" left join tdms.sale_order sa on  o.sale_order_id=sa.id " +
				" left join  tdms.supply_order so on  sa.supply_order_id=so.id " +
				" left join mdms.sales_group sg on so.sales_grp_id =sg.id " +
				" left join tdms.project pj  on so.id=pj.supply_order_id " +
				" WHERE f.offer_letter_id IS NULL " +
				"  group by o.id, so_id,sg.name   order by o.offer_date desc  "
				, nativeQuery = true)
	 List<Object[]> getOLs();
	 
	 @Query(value = "select offer_letter_id from tdms.file_note_ol_mapping where file_note_id= :fileNoteId ", nativeQuery = true)
	 List<Integer> getOLIds(int fileNoteId );


	// Below query fetches the Offerings for which FileNoting has been approved by CQAO.
	// It returns separate rows if consignees are different for a merged file noting.
	@Query(value = "SELECT "
			+ "    OL.ID AS offer_letter_id, "
			+ "    I.ID AS inote_id, "
			+ "    OL.OL_NUMBER, "
			+ "    OL.offer_date, "
			+ "    SG.NAME AS SBU_NAME, "
			+ "    STRING_AGG(DISTINCT P.PROJECT_NAME, ', ') AS PROJECT_NAMES, "
			+ "    CASE "
			+ "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN TO_CHAR(I.INOTE_DATE, 'dd-mm-yyyy') "
			+ "        ELSE 'NA' "
			+ "    END AS INOTE_DATE, "
			+ "    PO.INOTE_TYPE_ID, "
			+ "    CASE "
			+ "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued' "
			+ "        ELSE 'Pending' "
			+ "    END AS INOTE_STATUS, "
			+ "    PO.ORDER_NO, "
			+ "    C.NAME AS CONSIGNEE_NAME, "
			+ "    C.ADDRESS AS CONSIGNEE_ADDR "
			+ "FROM "
			+ "    TDMS.offer_letter OL "
			+ "LEFT JOIN "
			+ "    TDMS.inote I ON I.offer_letter_id = OL.ID "
			+ "LEFT JOIN "
			+ "    TDMS.sale_order SO ON OL.sale_order_id = SO.ID "
			+ "LEFT JOIN "
			+ "    TDMS.supply_order PO ON SO.supply_order_id = PO.ID "
			+ "LEFT JOIN "
			+ "    MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID "
			+ "LEFT JOIN "
			+ "    TDMS.PROJECT P ON PO.ID = P.SUPPLY_ORDER_ID "
			+ "LEFT JOIN "
			+ "    MDMS.CONSIGNEE C ON OL.CONSIGNEE_ID = C.ID "
			+ "WHERE "
			+ "    OL.inote_approval = TRUE AND PO.organization_id = :orgId "
			+ "GROUP BY "
			+ "    OL.ID, "
			+ "    I.ID, "
			+ "    SG.NAME, "
			+ "    I.INOTE_DATE, "
			+ "    PO.ORDER_NO, "
			+ "    PO.INOTE_TYPE_ID, "
			+ "    C.ID "
			+ "ORDER BY "
			+ "    CASE "
			+ "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued' "
			+ "        ELSE 'Pending' "
			+ "    END DESC,"
			+ "    OL.offer_date DESC", nativeQuery = true)
	List<Object[]> getINotesByOrgId(int orgId);


	@Query(value = "SELECT  " +
			"    OL.ID AS offer_letter_id,  " +
			"    I.ID AS inote_id,  " +
			"    OL.OL_NUMBER,  " +
			"    OL.offer_date,  " +
			"    SG.NAME AS SBU_NAME,  " +
			"    STRING_AGG(DISTINCT P.PROJECT_NAME, ', ') AS PROJECT_NAMES,  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN TO_CHAR(I.INOTE_DATE, 'dd-mm-yyyy')  " +
			"        ELSE 'NA'  " +
			"    END AS INOTE_DATE,  " +
			"    PO.INOTE_TYPE_ID,  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued'  " +
			"        ELSE 'Pending'  " +
			"    END AS INOTE_STATUS,  " +
			"    PO.ORDER_NO,  " +
			"    C.NAME AS CONSIGNEE_NAME,  " +
			"    C.ADDRESS AS CONSIGNEE_ADDR  " +
			"FROM  " +
			"    TDMS.offer_letter OL  " +
			"LEFT JOIN  " +
			"    TDMS.internal_inote I ON I.offer_letter_id = OL.ID  " +
			"LEFT JOIN  " +
			"    TDMS.sale_order SO ON OL.sale_order_id = SO.ID  " +
			"LEFT JOIN  " +
			"    TDMS.supply_order PO ON SO.supply_order_id = PO.ID  " +
			"LEFT JOIN  " +
			"    MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID  " +
			"LEFT JOIN  " +
			"    TDMS.PROJECT P ON PO.ID = P.SUPPLY_ORDER_ID  " +
			"LEFT JOIN  " +
			"    MDMS.CONSIGNEE C ON OL.CONSIGNEE_ID = C.ID  " +
			"WHERE  " +
			"    SG.organization_id = :orgId   " +
			"GROUP BY  " +
			"    OL.ID,  " +
			"    I.ID,  " +
			"    SG.NAME,  " +
			"    I.INOTE_DATE,  " +
			"    PO.ORDER_NO,  " +
			"    PO.INOTE_TYPE_ID,  " +
			"    C.ID  " +
			"ORDER BY  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued'  " +
			"        ELSE 'Pending'  " +
			"    END DESC, " +
			"    OL.offer_date DESC", nativeQuery = true)
	List<Object[]> getInotesForFirm(int orgId);




	@Query(value = "SELECT  "
			   + "    OL.ID AS offer_letter_id, "
			   + "    I.ID AS inote_id, "
			   + "    OL.OL_NUMBER, "
			   + "    OL.offer_date, "
			   + "    SG.NAME AS SBU_NAME,"
			   + "    STRING_AGG(DISTINCT P.PROJECT_NAME, ', ') AS PROJECT_NAME, "
			   + "    CASE"
			   + "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN TO_CHAR(I.INOTE_DATE, 'dd-mm-yyyy') "
			   + "        ELSE 'NA' "
			   + "    END AS INOTE_DATE, "
			   + "    PO.INOTE_TYPE_ID, "
			   + "    CASE "
			   + "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued' "
			   + "        ELSE 'Pending' "
			   + "    END AS INOTE_STATUS, "
			   + "    PO.ORDER_NO, "
			   + "    C.NAME AS CONSIGNEE_NAME, "
			   + "    C.ADDRESS AS CONSIGNEE_ADDR, "
			   + "    PO.ID,ORG.ORGANIZATION_NAME "
			   + "FROM "
			   + "    TDMS.offer_letter OL "
			   + "LEFT JOIN "
			   + "    tdms.ol_allotment oa ON oa.offer_letter_id = ol.id "
			   + "LEFT JOIN "
			   + "    TDMS.internal_inote I ON I.offer_letter_id = OL.ID "
			   + "LEFT JOIN "
			   + "    TDMS.sale_order SO ON OL.sale_order_id = SO.ID "
			   + "LEFT JOIN "
			   + "    TDMS.supply_order PO ON SO.supply_order_id = PO.ID "
			   + "LEFT JOIN "
			   + "    MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID "
			   + "LEFT JOIN "
			   + "    TDMS.PROJECT P ON PO.ID = P.SUPPLY_ORDER_ID "
			   + "LEFT JOIN "
			   + "    MDMS.CONSIGNEE C ON OL.CONSIGNEE_ID = C.ID "
			   + "LEFT JOIN MDMS.ORGANIZATION ORG ON ORG.ID=PO.ORGANIZATION_ID "
			   + "WHERE "
			   + "    SG.organization_id = :orgId  "
			   + "    and oa.alloted_to = :empId "
			   + "GROUP BY"
			   + "    OL.ID,"
			   + "    I.ID,"
			   + "    SG.NAME,"
			   + "    PO.ORDER_NO,"
			   + "    PO.INOTE_TYPE_ID,"
			   + "    PO.ID,"
			   + "    C.ID,"
			   + " ORG.ORGANIZATION_NAME "
			   + "ORDER BY "
			   + "    CASE "
			   + "        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued' "
			   + "        ELSE 'Pending' "
			   + "    END DESC ,"
			   + "    OL.offer_date DESC; ", nativeQuery = true)
	List<Object[]> getAllottedInotesForFirm(int orgId, int empId);




	@Query(value = "SELECT  " +
			"    OL.ID AS offer_letter_id,  " +
			"    I.ID AS inote_id,  " +
			"    OL.OL_NUMBER,  " +
			"    OL.offer_date, SOT.SO_TYPE, " +
			"    SG.NAME AS SBU_NAME,  " +
			"    STRING_AGG(DISTINCT P.PROJECT_NAME, ', ') AS PROJECT_NAMES,  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN TO_CHAR(I.INOTE_DATE, 'dd-mm-yyyy')  " +
			"        ELSE 'NA'  " +
			"    END AS INOTE_DATE,  " +
			"    PO.INOTE_TYPE_ID,  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued'  " +
			"        ELSE 'Pending'  " +
			"    END AS INOTE_STATUS,  " +
			"    PO.ORDER_NO,  " +
			"    C.NAME AS CONSIGNEE_NAME,  " +
			"    C.ADDRESS AS CONSIGNEE_ADDR  " +
			"FROM  " +
			"    TDMS.offer_letter OL  " +
			"left join tdms.ol_allotment oa on oa.offer_letter_id = ol.id " +
			"LEFT JOIN  " +
			"    TDMS.internal_inote I ON I.offer_letter_id = OL.ID  " +
			"LEFT JOIN  " +
			"    TDMS.sale_order SO ON OL.sale_order_id = SO.ID  " +
			"LEFT JOIN  " +
			"    TDMS.supply_order PO ON SO.supply_order_id = PO.ID  " +
			"LEFT JOIN  " +
			"    MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID  " +
			"LEFT JOIN  " +
			"    TDMS.PROJECT P ON PO.ID = P.SUPPLY_ORDER_ID  " +
			"LEFT JOIN  " +
			"    MDMS.CONSIGNEE C ON OL.CONSIGNEE_ID = C.ID  " +
			 " LEFT JOIN MDMS.SO_TYPE SOT on PO.SO_TYPE_ID = SOT.ID " +
			"WHERE  " +
			"    PO.organization_id = :orgId   " +
			"    and oa.alloted_to = :empId " +
			"GROUP BY  " +
			"    OL.ID,  " +
			"    I.ID,  " +
			"    SG.NAME,  " +
			"    I.INOTE_DATE,  " +
			"    PO.ORDER_NO,  " +
			"    PO.INOTE_TYPE_ID,  " +
			"    C.ID, SOT.ID  " +
			"ORDER BY  " +
			"    CASE  " +
			"        WHEN COUNT(I.ID) FILTER (WHERE OL.OL_NUMBER = I.OFFER_LETTER_NO) > 0 THEN 'Issued'  " +
			"        ELSE 'Pending'  " +
			"    END DESC, " +
			"    OL.offer_date DESC", nativeQuery = true)
	List<Object[]> getAllottedInotes(int orgId, int empId);


 
	 @Query(value = "SELECT * FROM TDMS.OFFER_LETTER OL LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID WHERE SO.SUPPLY_ORDER_ID = :supplyOrderId  and ol.is_notified is true", nativeQuery = true)
		List<OfferLetter> getNotifiedOLs(@Param("supplyOrderId") int supplyOrderId);
	 
	 
	 @Query(value= " SELECT fln.id, "
	 		+ "      String_agg(DISTINCT CASE "
	 		+ "                              WHEN flm.offer_letter_id IS NOT NULL THEN ol.ol_number "
	 		+ "                          END, ', ') AS ol_numbers, "
	 		+ "      COALESCE(To_char(fln.ol_date, 'dd-mm-yyyy'), '') AS offer_date, "
	 		+ "      SG.NAME AS sbu_name, "
	 		+ "      String_agg(DISTINCT P.project_name, ', ') AS project_names, "
	 		+ "      CASE "
	 		+ "          WHEN COUNT(I.id) FILTER ( "
	 		+ "                                   WHERE I.offer_letter_no ilike '%' || ol.ol_number || '%') > 0 THEN To_char(I.inote_date, 'dd-mm-yyyy') "
	 		+ "          ELSE 'NA' "
	 		+ "      END AS INOTE_DATE, "
	 		+ "      po.inote_type_id, "
	 		+ "      cl.id AS clearance_id, "
	 		+ "      String_agg(DISTINCT CAST(flm.offer_letter_id AS TEXT), ',') AS OL_IDS, "
	 		+ "      CASE "
	 		+ "          WHEN COUNT(I.id) FILTER ( "
	 		+ "                                   WHERE I.offer_letter_no ILIKE '%' || ol.ol_number || '%') > 0 THEN 'Issued' "
	 		+ "          ELSE 'Pending' "
	 		+ "      END AS INOTE_STATUS, "
	 		+ "      po.order_no, "
	 		+ "      c.id AS consignee_id, "
	 		+ "      c.NAME AS consignee_name, "
	 		+ "      c.address AS consignee_addr "
	 		+ "FROM tdms.inspection_note fln "
	 		+ "LEFT JOIN tdms.ol_approval_details OAD ON OAD.file_note_id = fln.id "
	 		+ "LEFT JOIN tdms.clearance_letter cl ON cl.offer_letter_id = fln.id "
	 		+ "LEFT JOIN mdms.employee E ON OAD.created_by = E.id "
	 		+ "LEFT JOIN tdms.supply_order PO ON fln.supply_order_id = PO.id "
	 		+ "LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id "
	 		+ "LEFT JOIN tdms.project P ON po.id = p.supply_order_id "
	 		+ "LEFT JOIN tdms.inote I ON I.file_note_id = fln.id "
	 		+ "LEFT JOIN tdms.file_note_ol_mapping flm ON fln.id = flm.file_note_id "
	 		+ "LEFT JOIN tdms.offer_letter ol ON flm.offer_letter_id = ol.id "
	 		+ "LEFT JOIN mdms.consignee c ON ol.consignee_id = c.id "
	 		+ "WHERE ol.id IS NOT NULL "
	 		+ " AND OAD.approval_status = 1 "
	 		+ "AND PO.sales_grp_id =:salesGrpId and i.created_datetime is not null "
	 		+ " AND (NOT EXISTS "
	 		+ "        (SELECT 1 "
	 		+ "         FROM tdms.inote sub_i "
	 		+ "         WHERE sub_i.file_note_id = fln.id) "
	 		+ "      OR ol.ol_number = ANY "
	 		+ "        (SELECT unnest(string_to_array(I.offer_letter_no, ',')))) "
	 		+ "GROUP BY fln.id, "
	 		+ "        SG.NAME, "
	 		+ "        I.inote_date, "
	 		+ "        clearance_id, "
	 		+ "        po.order_no, "
	 		+ "        po.inote_type_id, "
	 		+ "        c.id  , I.created_datetime "
	 		+ "ORDER BY  I.created_datetime DESC,Inote_status DESC ", nativeQuery = true)
	 List<Object[]> getINotesBySalesGrpId(@Param("salesGrpId") int salesGrpId);
	 
	 
	 @Query(value="select ol.id, ol.ol_number,sg.name ,p.project_name,ol.offer_date,i.inote_date from tdms.offer_letter ol "
	 		+ "left join tdms.sale_order so on ol.sale_order_id=so.id "
	 		+ "left join tdms.supply_order po on so.supply_order_id=po.id "
	 		+ "left join mdms.sales_group sg on po.sales_grp_id=sg.id "
	 		+ "left join tdms.project p on po.id=p.supply_order_id "
	 		+ "left join tdms.inote i on ol.id=i.offer_letter_id "
	 		+ "where po.is_gcc is true "
	 		+ "order by ol.created_datetime " ,nativeQuery=true)
	 List<Object[]> getGccOLsBySalesGrpId(@Param("salesGrpId") int salesGrpId);
	 
	 @Modifying
	 @Transactional
	 @Query(value = "delete from tdms.ol_line_item where id in (:lineItemIds)", nativeQuery = true)
	 void deleteLineItems(@Param("lineItemIds") List<Integer> lineItemIds);

	@Modifying
	@Transactional
	@Query(value = "UPDATE tdms.inote i " +
			"SET description = :description, " +
			"    remarks = :hologramRemarks " +
			"FROM tdms.file_note_ol_mapping fnom " +
			"JOIN tdms.offer_letter ol ON fnom.offer_letter_id = ol.id " +
			"WHERE i.file_note_id = fnom.file_note_id " +
			"  AND fnom.offer_letter_id = ol.id " +
			"  AND fnom.offer_letter_id = :olId " +
			"  AND i.offer_letter_no ILIKE '%' || ol.ol_number || '%'", nativeQuery = true)
	void updateINoteDetails(int olId, String description, String hologramRemarks);
	
	@Query(value = "SELECT  COUNT(ol.id) AS ol_count, SUM(sli. item_value) AS ol_value"
			   + "  FROM tdms.offer_letter ol "
			   + "  LEFT JOIN tdms.ol_line_item oli ON oli.offer_letter_id = ol.id "
			   + "  LEFT JOIN tdms.so_line_item sli ON sli.id = oli.so_line_item_id ", nativeQuery = true)
	List<Object[]> getDashBoardOlDetails();
	
	
	 @Query(value = "SELECT o.id,so.id as so_id,so.order_no,o.OL_NUmber,o.offer_date,sg.name ,pj.project_name  " +
				" FROM tdms.offer_letter o " +
				" LEFT JOIN tdms.file_note_ol_mapping f ON o.id = f.offer_letter_id " +
				" left join tdms.sale_order sa on  o.sale_order_id=sa.id " +
				" left join  tdms.supply_order so on  sa.supply_order_id=so.id " +
				" left join mdms.sales_group sg on so.sales_grp_id =sg.id " +
				" left join tdms.project pj  on so.id=pj.supply_order_id " +
				" WHERE f.offer_letter_id IS NULL and so.organization_id = :orgId", nativeQuery = true)
	 List<Object[]> getOLs(@Param("orgId") int orgId);


	@Query(value = "SELECT * FROM tdms.offer_letter where is_internal = true and created_by =:empId", nativeQuery = true)
	List<OfferLetter> findInternalOlByEmpId(@Param("empId") int empId);
	
	@Query(value = "SELECT ppm.product_id FROM ate.partno_product_mapping ppm WHERE ppm.part_number_id = :partNumberId", nativeQuery = true)
	Integer findProductNoByPartNumberId(@Param("partNumberId") Integer partNumberId);
	
	@Query(value = "SELECT  "
			   + "   OL.ID,  "
			   + "   OL.OL_NUMBER,  "
			   + "   soLi.id as so_li_id,  "
			   + "   soLi.line_item_no as so_line_item_no,  "
			   + "   olLi.id as ol_li_id,  "
			   + "   coalesce(TO_CHAR(olLi.created_datetime, 'dd-mm-yyyy HH:mm'), '') as ol_date,  "
			   + "   pn.part_no as part_no,  "
			   + "   reg.id as reg_id,  "
			   + "   reg.register_no,  "
			   + "   reg.status as reg_status,  "
			   + "   prod.id as prod_id,  "
			   + "   prod.prod_name  "
			   + "    FROM  "
			   + "   TDMS.OFFER_LETTER OL  "
			   + "  LEFT JOIN TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID  "
			   + "  LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID  "
			   + "  LEFT JOIN tdms.so_line_item soLi ON soLi.so_id = PO.id  "
			   + "  LEFT JOIN tdms.ol_line_item olLi ON olLi.so_line_item_id = soLi.id  "
			   + "  LEFT JOIN mdms.part_number pn ON pn.id = soLi.part_number_id  "
			   + "  LEFT JOIN tdms.line_item_lots_register reg ON reg.ol_line_item_id = olLi.id  "
			   + "  LEFT JOIN ate.partno_product_mapping ppm ON ppm.part_number_id = soLi.part_number_id  "
			   + "  LEFT JOIN ate.product prod ON prod.id = ppm.product_id  "
			   + "  LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id  "
			   + "  LEFT JOIN tdms.ol_allotment AC ON OL.ID = AC.offer_letter_id  "
			   + "  LEFT JOIN mdms.employee e ON e.id = :empId  "
			   + "  LEFT JOIN mdms.emp_role er ON er.id = e.emp_role_id  "
			   + "  LEFT JOIN mdms.so_type st ON po.so_type_id = st.id  "
			   + "  WHERE  "
			   + "  SG.ORGANIZATION_ID = :orgId  "
			   + "  AND (AC.alloted_to = :empId OR AC.alloted_by = :empId) "
			   + "  group by "
			   + " OL.ID, "
			   + " OL.OL_NUMBER, "
			   + " soLi.id, "
			   + " soLi.line_item_no, "
			   + " olLi.id, "
			   + " olLi.created_datetime, "
			   + " pn.part_no, "
			   + " reg.id, "
			   + " reg.register_no, "
			   + " reg.status, "
			   + " prod.id, "
			   + " prod.prod_name "
			   + " ORDER BY OL.ID DESC" , nativeQuery = true)
	List<Object[]> getAllottedOLsByStatusForFirm2(int orgId, int empId);
	
	@Query(value = "SELECT\n" +
            "OL.ID,\n" +
            "OL.OL_NUMBER,\n" +
            "soLi.id AS so_li_id,\n" +
            "soLi.line_item_no AS so_line_item_no,\n" +
            "olLi.id AS ol_li_id,\n" +
            "coalesce(TO_CHAR(olLi.created_datetime, 'dd-mm-yyyy HH:mm'), '') as ol_date,\n" +
            "pn.part_no AS part_no,\n" +
            "reg.id AS reg_id,\n" +
            "reg.register_no,\n" +
            "reg.status AS reg_status,\n" +
            "prod.id AS prod_id,\n" +
            "prod.prod_name\n" +
            "FROM\n" +
            "TDMS.OFFER_LETTER OL\n" +
            "LEFT JOIN\n" +
            "TDMS.SALE_ORDER SO ON OL.SALE_ORDER_ID = SO.ID\n" +
            "LEFT JOIN\n" +
            "TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID\n" +
            "LEFT JOIN\n" +
            "tdms.so_line_item soLi ON soLi.so_id = PO.id\n" +
            "LEFT JOIN\n" +
            "tdms.ol_line_item olLi ON olLi.so_line_item_id = soLi.id\n" +
            "LEFT JOIN\n" +
            "mdms.part_number pn ON pn.id = soLi.part_number_id\n" +
            "LEFT JOIN\n" +
            "tdms.line_item_lots_register reg ON reg.ol_line_item_id = olLi.id\n" +
            "LEFT JOIN\n" +
            "ate.partno_product_mapping ppm ON ppm.part_number_id = soLi.part_number_id\n" +
            "LEFT JOIN\n" +
            "ate.product prod ON prod.id = ppm.product_id\n" +
            "LEFT JOIN\n" +
            "mdms.sales_group SG ON PO.sales_grp_id = SG.id\n" +
            "LEFT JOIN\n" +
            "tdms.ol_allotment AC ON OL.ID = AC.offer_letter_id\n" +
            "LEFT JOIN\n" +
            "mdms.employee e ON e.id = :empId\n" +
            "LEFT JOIN\n" +
            "mdms.emp_role er ON er.id = e.emp_role_id\n" +
            "LEFT JOIN\n" +
            "mdms.so_type st ON po.so_type_id = st.id\n" +
            "WHERE\n" +
            "SG.ORGANIZATION_ID = :orgId\n" +
            "AND (AC.alloted_to = :empId OR AC.alloted_by = :empId)\n" +
            "AND OL.ol_status_id IN (:olStatusIds)\n" +
            "GROUP BY\n" +
            "OL.ID,\n" +
            "OL.OL_NUMBER,\n" +
            "soLi.id,\n" +
            "soLi.line_item_no,\n" +
            "olLi.id,\n" +
            "olLi.created_datetime,\n" +
            "pn.part_no,\n" +
            "reg.id,\n" +
            "reg.register_no,\n" +
            "reg.status,\n" +
            "prod.id,\n" +
            "prod.prod_name\n" +
            "ORDER BY\n" +
            "OL.ID DESC",
	        nativeQuery = true)
	public List<Object[]> getAllottedOLsByStatusForFirmByStatusV2(int orgId,int empId,List<Integer> olStatusIds);
	
	
	@Query(value = "select d.id, d.department_name, d.section_code  from tdms.offer_letter ol "
			+ "left join tdms.ol_line_item oli on ol.id = oli.offer_letter_id  "
			+ " left join tdms.so_line_item so on oli.so_line_item_id = so.id "
			+ "left join mdms.part_number pn on so.part_number_id = pn.id  "
			+ "left join ate.partno_product_mapping ppm  ON so.part_number_id =  ppm.part_number_id "
			+ "left join ate.department_product_mapping dpm on dpm.product_id= ppm.product_id "
			+ " left join mdms.department d on dpm.department_id = d.id  "
			+ "where ol.id = :offerLetterId ", nativeQuery = true)
	List<Object[]> getDepartSecCode(int offerLetterId);

	@Query(value = "select d.id, d.department_name, d.section_code  from tdms.submission_slip ss "
			+ "left join tdms.category c on c.id =ss.warrant_id  "
			+ "left join tdms.so_line_item so on c.so_line_item_id = so.id  "
			+ "left join mdms.part_number pn on so.part_number_id = pn.id   "
			+ "left join ate.partno_product_mapping ppm  ON so.part_number_id =  ppm.part_number_id  "
			+ "left join ate.department_product_mapping dpm on dpm.product_id= ppm.product_id  "
			+ "left join mdms.department d on dpm.department_id = d.id   "
			+ "where ss.id=:offerLetterId ", nativeQuery = true)
	List<Object[]> getDepartSecCodeBySubSlipId(int offerLetterId);

  @Query(value = "SELECT  " +
      " co.category AS Grp, " +
      " ine.inote_no AS InoteNo, " +
      " TO_CHAR(ine.inote_date, 'dd-mm-yyyy') AS InoteDate, " +
      " ol.ol_number as OfferLetterNo, " +
      " TO_CHAR(ol.offer_date, 'dd-mm-yyyy') AS OfferLetterDate, " +
      " so.sale_order_no AS WorkOrderNo, " +
      " string_agg(c.category_name,  ', ') AS WTNo, " +
      " inli.tendered AS SubQty, " +
      " inli.accepted AS AcceptedQty, " +
      " inli.rejected AS RejectedQty, " +
      " og.organization_name AS InspBy, " +
      " mss.merge_ol_no, mss.id, " +
      " ol.id as olId, " +
      " ine.id as inote_id " +
      " from tdms.inote ine " +
      "    left JOIN tdms.inote_line_item inli ON inli.inote_id = ine.id " +
      "    left JOIN tdms.offer_letter ol ON ine.offer_letter_id = ol.iD " +
      "    left join tdms.sale_order so on so.id = ol.sale_order_id " +
      "    left join tdms.supply_order po on po.id = so.supply_order_id " +
      "    left join tdms.so_line_item sli on sli.so_id = po.id " +
      "    left join tdms.category c on c.so_line_item_id = sli.id " +
      "    left JOIN tdms.merge_submission_slip mss on mss.id = ol.merge_sslip_id " +
      "    left JOIN tdms.project pr ON pr.supply_order_id = so.id " +
      "    left JOIN tdms.ship sh ON sh.project_id = pr.id " +
      "    left Join mdms.part_number p on p.id=sli.part_number_id " +
      "    left JOIN mdms.item_category co ON co.id = p.item_category_id " +
      "    left JOIN mdms.organization og ON og.id = sli.insp_auth " +
      "    left JOIN ate.partno_product_mapping ppm ON sli.part_number_id = ppm.part_number_id " +
      "    WHERE ppm.part_number_id = :partNumberId" +
      "    GROUP BY " +
      "    co.category, " +
      "    ine.inote_no, " +
      "    ine.inote_date, " +
      "    ol.ol_number,   " +
      "    ol.offer_date,   " +
      "    so.sale_order_no,   " +
      "    inli.tendered,   " +
      "    inli.accepted,   " +
      "    inli.rejected,   " +
      "    og.organization_name,   " +
      "    mss.merge_ol_no,   " +
      "    mss.id,   " +
      "    ol.id,   " +
      "    ine.id ORDER BY ine.id DESC; ", nativeQuery = true)
	public List<Object[]> getInotesByProdId(@Param("partNumberId") int partNumberId);


	@Query(value = "SELECT DISTINCT co.category AS Grp,ine.inote_no AS InoteNo,TO_CHAR(ine.inote_date, 'dd-mm-yyyy') AS InoteDate,ol.ol_number as OfferLetterNo,\r\n  "
			   + "     TO_CHAR(ol.offer_date, 'dd-mm-yyyy') AS OfferLetterDate,  sal.sale_order_no AS WorkOrderNo,\r\n"
			   + "     c.category_name AS WTNo,   inli.tendered AS SubQty,   inli.accepted AS AcceptedQty,\r\n"
			   + "     inli.rejected AS RejectedQty,og.organization_name AS InspBy, mo.merge_ol_no, mo.id,ine.id as inoteId  from\r\n"
			   + "     tdms.merge_submission_slip mo \r\n"
			   + "    JOIN  tdms.merge_sslip_mapping olm ON olm.merge_sslip_id = mo.id\r\n"
			   + "     JOIN tdms.submission_slip oll ON oll.id = olm.sslip_id\r\n"
			   + "    join tdms.internal_inote ine on ine.sub_slip_id = oll.id   \r\n"
			   + "     JOIN tdms.internal_inote_line_item inli ON inli.internal_inote_id = ine.id  \r\n"
			   + "     JOIN tdms.submission_slip ol ON ine.sub_slip_id = ol.id  \r\n"
			   + "     \r\n"
			   + "--     right JOIN  tdms.merge_sslip_mapping olm ON olm.sslip_id = ol.id\r\n"
			   + "--     right JOIN tdms.merge_submission_slip mss on mss.id = olm.sslip_id\r\n"
			   + "--      JOIN tdms.merge_submission_slip mss on mss.id = ol.merge_sslip_id \r\n"
			   + "     JOIN tdms.category c on ol.warrant_id = c.id  \r\n"
			   + "     JOIN tdms.so_line_item sli on sli.id=c.so_line_item_id  \r\n"
			   + "     JOIN tdms.supply_order so ON so.id = sli.so_id   \r\n"
			   + "     JOIN tdms.sale_order sal ON sal.id = sli.sale_order_id      \r\n"
			   + "     JOIN tdms.project pr ON pr.supply_order_id = so.id      \r\n"
			   + "     JOIN tdms.ship sh ON sh.project_id = pr.id     \r\n"
			   + "     Join mdms.part_number p on p.id=sli.part_number_id    \r\n"
			   + "     JOIN mdms.item_category co ON co.id = p.item_category_id      \r\n"
			   + "     JOIN mdms.organization og ON og.id = sli.insp_auth    \r\n"
			   + "     JOIN ate.partno_product_mapping ppm ON sli.part_number_id = ppm.part_number_id      \r\n"
			   + "     WHERE ppm.part_number_id =:partNumberId ORDER BY ine.id DESC ", nativeQuery = true)
	List<Object[]> getInternalInotesByProdId(int partNumberId);
	  
    @Query(
            value = "SELECT co.category AS Grp, " +
                    "       ine.inote_no AS InoteNo, " +
                    "      TO_CHAR(ine.inote_date, 'dd-mm-yyyy') AS InoteDate, " +
                    "       ino.inote_no AS InternalInoteNo, " +
                    "       TO_CHAR(ino.inote_date, 'dd-mm-yyyy') AS InternalInoteDate, " +
                    "       sal.sale_order_no AS WorkOrderNo, " +
                    "       c.category_name AS WTNo, " +
                    "       inol.tendered AS SubQty, " +
                    "       inol.accepted AS AcceptedQty, " +
                    "       inol.rejected AS RejectedQty, " +
                    "       inol.rfr AS RfrQty, " +
                    "       og.organization_name AS InspBy, " +
                    "       ino.offer_letter_id "+
                    " FROM tdms.internal_inote_line_item inol " +
                    " JOIN tdms.internal_inote ino ON ino.id = inol.internal_inote_id " +
                    " JOIN tdms.inote ine ON ine.offer_letter_id = ino.offer_letter_id " +
                    " JOIN tdms.ol_line_item ol ON inol.ol_line_item_id = ol.id " +
                    " JOIN tdms.so_line_item sl ON sl.id = ol.so_line_item_id " +
                    " JOIN tdms.supply_order so ON so.id = sl.so_id " +
                    " JOIN tdms.sale_order sal ON sal.supply_order_id = so.id " +
                    " JOIN tdms.project pr ON pr.supply_order_id = so.id " +
                    " JOIN tdms.ship sh ON sh.project_id = pr.id " +
                    " JOIN tdms.category c ON c.ship_id = sh.id " +
                    " JOIN mdms.item_category co ON co.id = sl.item_category_id " +
                    " JOIN mdms.organization og ON og.id = so.organization_id " +
                    " JOIN ate.partno_product_mapping ppm ON sl.part_number_id = ppm.part_number_id " ,
            nativeQuery = true
    )
    public  List<Object[]> getAllInotesList();

  @Query(value = "SELECT " +
      "    ic.category AS grp, " +
      "    ii.inote_no AS internal_inote_no, " +
      "    TO_CHAR(ii.inote_date, 'dd-mm-yyyy') AS internal_inote_date, " +
      "    iili.tendered, " +
      "    iili.accepted, " +
      "    iili.rejected, " +
      "    iili.rfr, " +
      "    STRING_AGG(distinct c.category_name, ', ' ORDER BY c.category_name) AS category_names, " +
      "    so.sale_order_no, " +
      "    o.organization_name, " +
      "    mss.merge_ol_no, " +
      "    mss.id AS merge_sslip_id, " +
      "    pn.part_no " +
      "FROM tdms.internal_inote_line_item iili " +
      "LEFT JOIN tdms.internal_inote ii ON ii.id = iili.internal_inote_id " +
      "LEFT JOIN tdms.merge_submission_slip mss ON mss.id = ii.merge_sslip_id " +
      "LEFT JOIN tdms.merge_sslip_mapping msm ON msm.merge_sslip_id = mss.id " +
      "LEFT JOIN tdms.submission_slip ss ON ss.id = msm.sslip_id " +
      "LEFT JOIN tdms.category c ON c.id = ss.warrant_id " +
      "LEFT JOIN tdms.so_line_item sli ON sli.id = c.so_line_item_id " +
      "LEFT JOIN tdms.sale_order so ON so.id = sli.sale_order_id " +
      "LEFT JOIN tdms.supply_order po ON po.id = so.supply_order_id " +
      "LEFT JOIN mdms.organization o ON o.id = po.organization_id " +
      "LEFT JOIN ate.partno_product_mapping ppm ON sli.part_number_id = ppm.part_number_id " +
      "LEFT JOIN mdms.part_number pn ON pn.id = ppm.part_number_id " +
      "LEFT JOIN mdms.item_category ic ON ic.id = pn.item_category_id " +
      "GROUP BY  " +
      "    ic.category, " +
      "    ii.inote_no, " +
      
      "    TO_CHAR(ii.inote_date, 'dd-mm-yyyy'), " +
      "    iili.tendered, " +
      "    iili.accepted, " +
      "    iili.rejected, " +
      "    iili.rfr, " +
      "    so.sale_order_no, " +
      "    o.organization_name, " +
      "    mss.merge_ol_no, " +
      "    mss.id, " +
      "    pn.part_no, ii.id  ORDER BY ii.id DESC", nativeQuery = true)
    public List<Object[]> getAllInternalInotesList();
    


    
    @Query( value = "SELECT DISTINCT ol.id as olId, "
//    	    		+ "co.category AS Grp,\r\n"
    	    		+ "so.order_no as So_Number, "
    	    		+ " TO_CHAR(so.order_date, 'dd-mm-yyy') AS So_Date, "
    	    		+ "ol.ol_number as ol_no,\r\n"
    	    		+ " TO_CHAR(ol.offer_date, 'dd-mm-yyyy') AS ol_date, "
    	    		+ "sal.sale_order_no AS WorkOrderNo "
    	    		+ "FROM tdms.offer_letter ol "
    	    		+ "JOIN tdms.ol_line_item olt ON olt.offer_letter_id = ol.id "
    	    		+ "JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id "
    	    		+ "JOIN tdms.supply_order so ON so.id = sl.so_id\r\n"
//    	    		+ "JOIN mdms.item_category co ON co.id = sl.item_category_id\r\n"
    	    		+ "JOIN tdms.sale_order sal ON sal.supply_order_id = so.id\r\n"
    	    		+ "WHERE "
    	    		+ "ol.is_merged = false ", nativeQuery = true		)
    		List<Object[]> getAllOlListByInspOrgId();
    		
	@Query(value ="SELECT count(*) FROM tdms.offer_letter where department_id =:deptId GROUP BY department_id ",nativeQuery = true )
	Integer getOlCountRows(Integer deptId);

	OfferLetter findByLotId(Integer mergedSubSlipId);
	
	@Query(value = "SELECT DISTINCT ol.id as olId, lisn.id as lisnId, lisn.serial_no, "
		+ " oli.id AS olLiId, ir.round_id, ir.proudct_id FROM tdms.offer_letter ol "
		+ " JOIN tdms.ol_line_item oli ON oli.offer_letter_id = ol.id "
		+ " JOIN tdms.ol_line_item_serial_no olisn ON olisn.ol_line_item_id = oli.id "
		+ " JOIN tdms.line_item_serial_no lisn ON lisn.id = olisn.serial_no_id "
		+ " JOIN ate.insp_round ir ON ir.ol_ser_no_id = lisn.id AND ir.ol_id = ol.id "
		+ " WHERE ol.id = :olId  AND ir.is_qa = TRUE;", nativeQuery = true)
	public List<Object[]> getRegsByOlIdQa(Integer olId);

	@Query(value = "SELECT COALESCE(SUM(inl.accepted), 0) AS total_accepted "
			+ "FROM tdms.ol_line_item oli_input "
			+ "JOIN tdms.ol_line_item oli "
			+ "    ON oli.so_line_item_id = oli_input.so_line_item_id "
			+ "JOIN tdms.inote_line_item inl "
			+ "    ON inl.ol_line_item_id = oli.id "
			+ "WHERE oli_input.offer_letter_id = :olId ; ", nativeQuery = true)
	Integer getAllPassRegsForLineItem(Integer olId);

  @Transactional
  @Modifying
  @Query(value = "UPDATE tdms.offer_letter SET ol_status_id = :status WHERE id = :id", nativeQuery = true)
  void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

}
