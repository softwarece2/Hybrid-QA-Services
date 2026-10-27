package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.transaction.entity.SubmissionSlip;

public interface SubmissionSlipRepository extends JpaRepository<SubmissionSlip, Integer> {

	
	@Query(value = "select ol_number from tdms.submission_slip", nativeQuery = true)
	List<String> findOfferLetterNos();

   @Query(value ="SELECT OL.ID, "
		     + " OL.OL_NUMBER, "
		     + " Ol.OL_status_id, "
		     + " COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'),'') AS OFFER_DATE ,     "
		     + " SG.NAME AS SBU_NAME, "
		     + " PO.INOTE_TYPE_ID , "
		     + " ols.status,  "
		     + " il.level_of_inspection,  "
		     + " po.order_no,ol.is_notified,  "
		     + " po.is_gcc ,  "
		     + " po.id as supply_order_id ,  "
		     + " consignee_id,  "
		     + " c.id as cat_id, "
		     + " c.category_name,  "
		     + " pn.part_no, "
		     + " pn.material_description,  "
		     + " so.sale_order_no,"
		     + " og.organization_name   "
		     + "FROM TDMS.submission_slip OL      "
		     + " LEFT JOIN tdms.category c ON c.id = OL.warrant_id     "
		     + " LEFT JOIN tdms.so_line_item sli  on sli.id= c.so_line_item_id "
		     + " LEFT JOIN mdms.organization og ON og.id = sli.insp_auth "
		     + " LEFT JOIN TDMS.SUPPLY_ORDER PO ON sli.so_id = PO.ID     "
		     + " LEFT JOIN tdms.sale_order so ON so.id = sli.sale_order_id  "
		     + " LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id    "
		     + " LEFT JOIN mdms.inspection_level il ON ol.inspection_level_id  = il.id  "
		     + " Left Join mdms.ol_status ols on OL.ol_status_id = ols.id  "
		     + " left join mdms.part_number pn on pn.id = sli.part_number_id  "
		     + " left join tdms.ship s on s.so_line_item_id = sli.id "
		     + " left join tdms.project proj on proj.id = s.project_id "
		     + " WHERE  PO.sales_grp_id = :salesGrpId AND ol.ol_status_id in :statusIds "
		     + "GROUP BY  "
		     + " OL.id, "
		     + "    OL.OL_NUMBER,  "
		     + "    SG.NAME,  "
		     + "    OL.OL_status_id,    "
		     + " po.inote_type_id, "
		     + " ols.status , "
		     + " il.level_of_inspection ,  "
		     + " po.order_no, "
		     + " po.is_gcc ,  "
		     + " po.id ,  "
		     + " c.id, "
		     + " c.category_name, "
		     + " pn.part_no, "
		     + " pn.material_description,  "
		     + " so.sale_order_no,"
		     + " og.organization_name  "
		     + "order by  OL.id desc"
		  , nativeQuery = true)
	List<Object[]> getSubSlipBySalesGrpId(int salesGrpId, List<Integer> statusIds);
	
	
	@Query(value ="SELECT ss.ID, "
			   + " ss.OL_NUMBER, "
			   + " ss.OL_status_id, "
			   + " COALESCE(TO_CHAR(ss.OFFER_DATE, 'dd-mm-yyyy'),'') AS OFFER_DATE, "
			   + " SG.NAME AS SBU_NAME, "
			   + " PO.INOTE_TYPE_ID, "
			   + " ols.status, "
			   + " il.level_of_inspection, "
			   + " po.order_no,ss.is_notified, "
			   + " po.is_gcc, "
			   + " po.id as supply_order_id, "
			   + " consignee_id, "
			   + " c.id as cat_id, "
			   + " c.category_name, "
			   + " pn.part_no, "
			   + " pn.material_description, "
			   + " so.sale_order_no,"
			   + " og.organization_name "
			   + " FROM TDMS.submission_slip ss      "
			   + "  LEFT JOIN tdms.category c ON c.id = ss.warrant_id "
			   + "  LEFT JOIN tdms.so_line_item sli  on sli.id= c.so_line_item_id "
			   + "      LEFT JOIN mdms.organization og on og.id = sli.insp_auth "
			   + "  LEFT JOIN TDMS.SUPPLY_ORDER PO ON sli.so_id = PO.ID "
			   + "   LEFT JOIN tdms.sale_order so ON so.id = sli.sale_order_id "
			   + "  LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id "
			   + "  LEFT JOIN mdms.inspection_level il ON ss.inspection_level_id  = il.id "
			   + "  Left Join mdms.ol_status ols on ss.ol_status_id = ols.id "
			   + "  left join mdms.part_number pn on pn.id = sli.part_number_id "
			   + "   left join tdms.ship s on s.so_line_item_id = sli.id "
			   + "  left join tdms.project proj on proj.id = s.project_id "
			   + "  left join tdms.sub_slip_allotment ssa on ssa.sub_slip_id = ss.id "
			   + " WHERE  PO.sales_grp_id = :salesGrpId AND (ss.created_by = :empId OR ssa.alloted_to = :empId) AND ss.ol_status_id in (:statusIds)"
			   + " GROUP BY  "
			   + "  ss.id, "
			   + "     ss.OL_NUMBER,  "
			   + "     SG.NAME,  "
			   + "     ss.OL_status_id,    "
			   + "  po.inote_type_id, "
			   + "  ols.status , "
			   + "  il.level_of_inspection ,  "
			   + "  po.order_no, "
			   + "  po.is_gcc ,  "
			   + "  po.id ,  "
			   + "  c.id, "
			   + "  c.category_name, "
			   + "  pn.part_no, "
			   + "  pn.material_description,  "
			   + "     so.sale_order_no ,"
			   + "   og.organization_name  "
			   + " order by MAX(ssa.created_datetime) desc" , nativeQuery = true)
	List<Object[]> getSubSlipBySalesGrpIdEmpId(int salesGrpId, int empId, List<Integer> statusIds);

	@Query(value ="SELECT OL.ID, "
		      + " OL.OL_NUMBER, "
		      + " Ol.OL_status_id, "
		      + " COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'),'') AS OFFER_DATE ,     "
		      + " SG.NAME AS SBU_NAME, "
		      + " PO.INOTE_TYPE_ID , "
		      + " ols.status,  "
		      + " il.level_of_inspection,  "
		      + " po.order_no,ol.is_notified,  "
		      + " po.is_gcc ,  "
		      + " po.id as supply_order_id ,  "
		      + " consignee_id,  "
		      + " c.id as cat_id, "
		      + " c.category_name,  "
		      + " pn.part_no, "
		      + " pn.material_description, "
		      + " so.sale_order_no ,"
		      + "og.organization_name  "
		      + "FROM TDMS.submission_slip OL      "
		      + " LEFT JOIN tdms.category c ON c.id = OL.warrant_id     "
		      + " LEFT JOIN tdms.so_line_item sli  on sli.id= c.so_line_item_id "
		    + " LEFT JOIN mdms.organization og on og.id = sli.insp_auth "
		      + " LEFT JOIN TDMS.SUPPLY_ORDER PO ON sli.so_id = PO.ID     "
		      + " LEFT JOIN tdms.sale_order so ON so.id = sli.sale_order_id  "
		      + " LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id    "
		      + " LEFT JOIN mdms.inspection_level il ON ol.inspection_level_id  = il.id  "
		      + " Left Join mdms.ol_status ols on OL.ol_status_id = ols.id  "
		      + " left join mdms.part_number pn on pn.id = sli.part_number_id  "
		      + " left join tdms.ship s on s.so_line_item_id = sli.id "
		      + " left join tdms.project proj on proj.id = s.project_id "
		      + " WHERE  PO.sales_grp_id = :salesGrpId AND c.id = :warrantId "
		      + "GROUP BY  "
		      + " OL.id, "
		      + "    OL.OL_NUMBER,  "
		      + "    SG.NAME,  "
		      + "    OL.OL_status_id,    "
		      + " po.inote_type_id, "
		      + " ols.status , "
		      + " il.level_of_inspection ,  "
		      + " po.order_no, "
		      + " po.is_gcc ,  "
		      + " po.id ,  "
		      + " c.id, "
		      + " c.category_name, "
		      + " pn.part_no, "
		      + " pn.material_description, "
		      + " so.sale_order_no,"
		      + " og.organization_name  "
		      + "order by  OL.id desc", nativeQuery = true)
	List<Object[]> findByWarrantIdAndSalesGrpId(Integer salesGrpId, Integer warrantId);

	List<SubmissionSlip> findByWarrantId(Integer warrantId);

//	@Query(value = "with AllotmentCombined as ( select sub_slip_id, alloted_to, alloted_by from tdms.sub_slip_allotment ssa )    "
//			+ "select ss.ID, "
//			+ " ss.OL_NUMBER, "
//			+ " ss.OL_status_id, "
//			+ " coalesce(TO_CHAR(ss.OFFER_DATE,'dd-mm-yyyy'),'') as OFFER_DATE, "
//			+ "	SG.NAME as SBU_NAME, "
//			+ "	P.PROJECT_NAME, "
//			+ "	PO.INOTE_TYPE_ID, "
//			+ "	OLS.status, "
////			+ "	SO.sale_order_no, "
//			+ "	PO.order_no, "
//			+ "	pn.part_no, "
//			+ "	c.category_name, "
//			+ "	case "
//			+ "	   when COUNT(AC.sub_slip_id) > 0 then true else false end as is_alloted, "
//			+ "	st.so_type "
//			+ "from TDMS.submission_slip ss "
//			+ "left join tdms.sslip_line_item ssli on ssli.sub_slip_id  = ss.id "
//			+ "left join ate.insp_round ir on ir.sub_slip_line_item_id = ssli.id "
//			+ "left join tdms.sub_slip_serial_no sssn on sssn.id = ir.sslip_serial_no_id  "
//			+ "left join tdms.so_line_item sli on sli.id = ssli.so_line_item_id "
//			+ "left join tdms.supply_order PO on PO.ID = sli.so_id "
//			+ "left join tdms.sale_order SO on SO.supply_order_id = po.id "
//			+ "left join mdms.sales_group SG on PO.sales_grp_id = SG.id "
//			+ "left join tdms.inote I on I.offer_letter_id = ss.id "
//			+ "left join tdms.category C on C.id = SO.category_id "
//			+ "left join tdms.ship S on S.so_line_item_id = sli.id "
//			+ "left join tdms.project P on P.id = S.project_id "
//			+ "left join mdms.part_number pn on pn.id = sli.part_number_id "
//			+ "left join mdms.ol_status OLS on ss.ol_status_id = OLS.id "
//			+ "left join AllotmentCombined AC on ss.ID = AC.sub_slip_id "
//			+ "left join mdms.employee e on e.id = :empId "
//			+ "left join mdms.emp_role er on er.id = e.emp_role_id "
//			+ "left join mdms.so_type st on po.so_type_id = st.id "
//			+ "where     "
//			+ "	SG.ORGANIZATION_ID = :orgId and ( AC.alloted_to = :empId or AC.alloted_by = :empId) "
//			+ "group by     "
//			+ "	ss.ID,    "
//			+ "	ss.OL_NUMBER,    "
//			+ "	ss.OL_status_id,    "
//			+ "	ss.OFFER_DATE,    "
//			+ "	SG.NAME,    "
//			+ "	P.PROJECT_NAME,    "
//			+ "	PO.INOTE_TYPE_ID,    "
//			+ "	OLS.status,    "
//			+ "	SO.sale_order_no,    "
//			+ "	PO.order_no,    "
//			+ "	st.so_type ,  "
//			+ "	pn.part_no,  "
//			+ "	c.category_name  "
////			+ "	so.sale_order_no "
//			+ "HAVING  "
//			+ "    COUNT(CASE WHEN sssn.final_status <> 'pending' THEN 1 END) > 0 "
//			+ "order by  "
//			+ "	ss.offer_date desc", nativeQuery = true)
//
//	List<Object[]> getAllottedSubSlipsByStatusForFirm(@Param("orgId") Integer orgId, @Param("empId") Integer empId);
	
	// previous fetch all sub slip query
	@Query(value = "with AllotmentCombined as ( select sub_slip_id, alloted_to, alloted_by from tdms.sub_slip_allotment ssa )    "
			   + "select     "
			   + "    ss.ID, ss.OL_NUMBER, ss.OL_status_id,    "
			   + "    coalesce(TO_CHAR(ss.OFFER_DATE,'dd-mm-yyyy'),'') as OFFER_DATE,    "
			   + " SG.NAME as SBU_NAME,    "
			   + " P.PROJECT_NAME,    "
			   + " PO.INOTE_TYPE_ID,    "
			   + " OLS.status,    "
			   + " SO.sale_order_no,    "
			   + " PO.order_no,  "
			   + " pn.part_no,   "
			   + " c.category_name, "
			   + " case     "
			   + "  when COUNT(AC.sub_slip_id) > 0 then true else false end as is_alloted,    "
			   + " st.so_type "
			   + "from TDMS.submission_slip ss "
			   + "left join tdms.sslip_line_item ssli on ssli.sub_slip_id  = ss.id "
			   + "left join ate.insp_round ir on ir.sub_slip_line_item_id = ssli.id "
			   + "left join tdms.sub_slip_serial_no sssn on sssn.id = ir.sslip_serial_no_id  "
			   + "left join tdms.so_line_item sli on sli.id = ssli.so_line_item_id "
			   + "left join tdms.supply_order PO on PO.ID = sli.so_id "
			   + "left join tdms.sale_order SO on SO.id = sli.sale_order_id "
			   + "left join mdms.sales_group SG on PO.sales_grp_id = SG.id "
			   + "left join tdms.inote I on I.offer_letter_id = ss.id "
			   + "left join tdms.category C on C.id = SO.category_id "
			   + "left join tdms.ship S on S.so_line_item_id = sli.id "
			   + "left join tdms.project P on P.id = S.project_id "
			   + "left join mdms.part_number pn on pn.id = sli.part_number_id "
			   + "left join mdms.ol_status OLS on ss.ol_status_id = OLS.id "
			   + "left join AllotmentCombined AC on ss.ID = AC.sub_slip_id "
			   + "left join mdms.employee e on e.id = :empId "
			   + "left join mdms.emp_role er on er.id = e.emp_role_id "
			   + "left join mdms.so_type st on po.so_type_id = st.id "
			   + "where     "
			   + " SG.ORGANIZATION_ID = :orgId and ( AC.alloted_to = :empId or AC.alloted_by = :empId)   "
			   + "group by     "
			   + " ss.ID,    "
			   + " ss.OL_NUMBER,    "
			   + " ss.OL_status_id,    "
			   + " ss.OFFER_DATE,    "
			   + " SG.NAME,    "
			   + " P.PROJECT_NAME,    "
			   + " PO.INOTE_TYPE_ID,    "
			   + " OLS.status,    "
			   + " SO.sale_order_no,    "
			   + " PO.order_no,    "
			   + " st.so_type ,  "
			   + " pn.part_no,  "
			   + " c.category_name,  "
			   + " so.sale_order_no "
			   + "HAVING  "
			   + "    COUNT(CASE WHEN sssn.final_status <> 'in-process' THEN 1 END) > 0 "
			   + "order by  "
			   + " ss.offer_date desc", nativeQuery = true)
	List<Object[]> getAllottedSubSlipsApprovalByStatusForFirmOld(@Param("orgId") Integer orgId, @Param("empId") Integer empId);
	
	// "Retrieves all submission slips currently assigned to the logged-in employee for approval,
	// filtering by organization and ensuring the slip has completed at least one inspection stage."
	// Author: Shridhar R
	@Query(value = "SELECT "
			+ "    ss.ID,  "
			+ "    ss.OL_NUMBER,  "
			+ "    ss.OL_status_id, "
			+ "    COALESCE(TO_CHAR(ss.OFFER_DATE,'dd-mm-yyyy'),'') as OFFER_DATE, "
			+ "    SG.NAME as SBU_NAME, "
			+ "    P.PROJECT_NAME, "
			+ "    PO.INOTE_TYPE_ID, "
			+ "    OLS.status, "
			+ "    SO.sale_order_no, "
			+ "    PO.order_no, "
			+ "    pn.part_no, "
			+ "    c.category_name, "
			+ "    TRUE as is_alloted,  "
			+ "    st.so_type "
			+ "FROM TDMS.submission_slip ss "
			+ "LEFT JOIN tdms.sslip_line_item ssli ON ssli.sub_slip_id = ss.id "
			+ "LEFT JOIN ate.insp_round ir ON ir.sub_slip_line_item_id = ssli.id "
			+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ "LEFT JOIN tdms.so_line_item sli ON sli.id = ssli.so_line_item_id "
			+ "LEFT JOIN tdms.supply_order PO ON PO.ID = sli.so_id "
			+ "LEFT JOIN tdms.sale_order SO ON SO.id = sli.sale_order_id  "
			+ "LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id "
			+ "LEFT JOIN tdms.inote I ON I.offer_letter_id = ss.id "
			+ "LEFT JOIN tdms.category C ON C.id = SO.category_id "
			+ "LEFT JOIN tdms.ship S ON S.so_line_item_id = sli.id "
			+ "LEFT JOIN tdms.project P ON P.id = S.project_id "
			+ "LEFT JOIN mdms.part_number pn ON pn.id = sli.part_number_id "
			+ "LEFT JOIN mdms.ol_status OLS ON ss.ol_status_id = OLS.id "
			+ "LEFT JOIN mdms.so_type st ON po.so_type_id = st.id "
			+ "LEFT JOIN LATERAL ( "
			+ "    SELECT ssa.alloted_to, ssa.alloted_by  "
			+ "    FROM tdms.sub_slip_allotment ssa "
			+ "    WHERE ssa.sub_slip_id = ss.id "
			+ "    ORDER BY ssa.created_datetime DESC "
			+ "    LIMIT 1 "
			+ ") LatestAC ON TRUE "
			+ "LEFT JOIN mdms.employee e ON e.id = :empId "
			+ "WHERE "
			+ "    SG.ORGANIZATION_ID = :orgId  "
			+ "    AND LatestAC.alloted_to = :empId "
			+ " GROUP BY "
			+ "    ss.ID, "
			+ "    ss.OL_NUMBER, "
			+ "    ss.OL_status_id, "
			+ "    ss.OFFER_DATE, "
			+ "    SG.NAME, "
			+ "    P.PROJECT_NAME, "
			+ "    PO.INOTE_TYPE_ID, "
			+ "    OLS.status, "
			+ "    SO.sale_order_no, "
			+ "    PO.order_no, "
			+ "    st.so_type, "
			+ "    pn.part_no, "
			+ "    c.category_name, "
			+ "    so.sale_order_no "
			+ "HAVING "
			+ "    COUNT(CASE WHEN sssn.final_status <> 'in-process' THEN 1 END) > 0 "
			+ "ORDER BY "
			+ "    ss.ol_number DESC;", nativeQuery = true)
	List<Object[]> getAllottedSubSlipsApprovalByStatusForFirm(@Param("orgId") Integer orgId, @Param("empId") Integer empId);
	
	
	@Query(value =
	        "WITH AllotmentCombined AS ( " +
	        "    SELECT sub_slip_id, alloted_to, alloted_by  " +
	        "    FROM tdms.sub_slip_allotment ssa " +
	        ") " +
	        "SELECT " +
	        "    OL.ID, " +
	        "    OL.OL_NUMBER, " +
	        "    OL.OL_status_id, " +
	        "    COALESCE(TO_CHAR(OL.OFFER_DATE, 'dd-mm-yyyy'), '') AS OFFER_DATE, " +
	        "    SG.NAME AS SBU_NAME, " +
	        "    P.PROJECT_NAME, " +
	        "    PO.INOTE_TYPE_ID, " +
	        "    OLS.status, " +
	        "    SO.sale_order_no, " +
	        "    PO.order_no, " +
	        "    pn.part_no, " +
	        "    cat.category_name, " +
	        "    CASE " +
	        "        WHEN COUNT(AC.sub_slip_id) > 0 THEN TRUE " +
	        "        ELSE FALSE " +
	        "    END AS is_alloted, " +
	        "    st.so_type " +
	        "FROM TDMS.submission_slip OL " +
	        "LEFT JOIN TDMS.category cat ON OL.warrant_id = cat.ID " +
	        "LEFT JOIN tdms.so_line_item sli ON sli.id = cat.so_line_item_id " +
	        "LEFT JOIN TDMS.SUPPLY_ORDER PO ON PO.ID = sli.so_id " +
	        "LEFT JOIN TDMS.SALE_ORDER SO ON SO.id = sli.sale_order_id " +
	        "LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id " +
	        "LEFT JOIN tdms.inote I ON I.offer_letter_id = OL.id " +
	        "LEFT JOIN tdms.ship S ON S.so_line_item_id = sli.id " +
	        "LEFT JOIN tdms.project P ON P.id = S.project_id " +
	        "LEFT JOIN mdms.part_number pn ON pn.id = sli.part_number_id " +
	        "LEFT JOIN mdms.ol_status OLS ON OL.ol_status_id = OLS.id " +
	        "LEFT JOIN AllotmentCombined AC ON OL.ID = AC.sub_slip_id " +
	        "LEFT JOIN mdms.employee e ON e.id = :empId " +
	        "LEFT JOIN mdms.emp_role er ON er.id = e.emp_role_id " +
	        "LEFT JOIN mdms.so_type st ON PO.so_type_id = st.id " +
	        "WHERE SG.ORGANIZATION_ID = :orgId " +
	        "AND (AC.alloted_to = :empId OR AC.alloted_by = :empId) " +
	        " GROUP BY " +
	        "    OL.ID, " +
	        "    OL.OL_NUMBER, " +
	        "    OL.OL_status_id, " +
	        "    OL.OFFER_DATE, " +
	        "    SG.NAME, " +
	        "    P.PROJECT_NAME, " +
	        "    PO.INOTE_TYPE_ID, " +
	        "    OLS.status, " +
	        "    SO.sale_order_no, " +
	        "    PO.order_no, " +
	        "    st.so_type, " +
	        "    pn.part_no, " +
	        "    cat.category_name " +
	        "ORDER BY OL.OFFER_DATE DESC",
	        nativeQuery = true)
	List<Object[]> getAllottedSubSlipsByStatusForFirm(@Param("orgId") Integer orgId,  @Param("empId") Integer empId);
	
	// fetch sub slip only if all reg. nos. in it is tested i.e. either pass/fail
	//author: Shridhar
	@Query(
			  value  = "WITH AllotmentCombined AS( "
			  		+ "    SELECT sub_slip_id, alloted_to, alloted_by, created_datetime "
			  		+ "    FROM tdms.sub_slip_allotment ssa) "
			  		+ "SELECT "
			  		+ "    ss.ID, "
			  		+ "    ss.OL_NUMBER, "
			  		+ "    ss.OL_status_id, "
			  		+ "    COALESCE(TO_CHAR(ss.OFFER_DATE, 'dd-mm-yyyy'), '') AS OFFER_DATE, "
			  		+ "    SG.NAME AS SBU_NAME, "
			  		+ "    P.PROJECT_NAME, "
			  		+ "    PO.INOTE_TYPE_ID, "
			  		+ "    OLS.status, "
			  		+ "    SO.sale_order_no, "
			  		+ "    PO.order_no, "
			  		+ "    pn.part_no, "
			  		+ "    C.category_name, "
			  		+ "    CASE "
			  		+ "        WHEN COUNT(AC.sub_slip_id) > 0 THEN TRUE "
			  		+ "        ELSE FALSE "
			  		+ "    END AS is_alloted, "
			  		+ "    st.so_type, "
			  		+ "    AC.created_datetime "
			  		+ "FROM TDMS.submission_slip ss "
			  		+ "LEFT JOIN tdms.sslip_line_item ssli ON ssli.sub_slip_id = ss.id "
			  		+ "LEFT JOIN ate.insp_round ir ON ir.sub_slip_line_item_id = ssli.id "
			  		+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			  		+ "LEFT JOIN tdms.so_line_item sli ON sli.id = ssli.so_line_item_id "
			  		+ "LEFT JOIN tdms.supply_order PO ON PO.ID = sli.so_id "
			  		+ "LEFT JOIN tdms.sale_order SO ON SO.id = sli.sale_order_id "
			  		+ "LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id "
			  		+ "LEFT JOIN tdms.inote I ON I.offer_letter_id = ss.id "
			  		+ "LEFT JOIN tdms.category C ON C.id = SO.category_id "
			  		+ "LEFT JOIN tdms.ship S ON S.so_line_item_id = sli.id "
			  		+ "LEFT JOIN tdms.project P ON P.id = S.project_id "
			  		+ "LEFT JOIN mdms.part_number pn ON pn.id = sli.part_number_id "
			  		+ "LEFT JOIN mdms.ol_status OLS ON ss.ol_status_id = OLS.id "
			  		+ "LEFT JOIN AllotmentCombined AC ON ss.ID = AC.sub_slip_id "
			  		+ "LEFT JOIN mdms.so_type st ON PO.so_type_id = st.id "
			  		+ "WHERE SG.ORGANIZATION_ID = :orgId  "
			  		+ "  AND AC.alloted_to = :empId "
			  		+ "  AND ss.id NOT IN ( "
			  		+ "      SELECT ssli_sub.sub_slip_id "
			  		+ "      FROM tdms.sslip_line_item ssli_sub "
			  		+ "      INNER JOIN tdms.sub_slip_serial_no sssn_sub ON sssn_sub.sub_slip_line_item_id = ssli_sub.id "
			  		+ "      WHERE TRIM(LOWER(sssn_sub.final_status)) = 'in-process' "
			  		+ "  ) "
			  		+ "GROUP BY "
			  		+ "    ss.ID, "
			  		+ "    ss.OL_NUMBER, "
			  		+ "    ss.OL_status_id, "
			  		+ "    ss.OFFER_DATE, "
			  		+ "    SG.NAME, "
			  		+ "    P.PROJECT_NAME, "
			  		+ "    PO.INOTE_TYPE_ID, "
			  		+ "    OLS.status, "
			  		+ "    SO.sale_order_no, "
			  		+ "    PO.order_no, "
			  		+ "    st.so_type, "
			  		+ "    pn.part_no, "
			  		+ "    C.category_name, "
			  		+ "    AC.created_datetime "
			  		+ "ORDER BY AC.created_datetime DESC; ", nativeQuery = true )
	List<Object[]> getAllottedSubSlipsApprovalForExaminerInspector(@Param("orgId") Integer orgId,  @Param("empId") Integer empId);
	
	
	@Query(
    	    value = "SELECT DISTINCT ol.id as olId, "
//    	    		+ "co.category AS Grp, "
    	    		+ "so.order_no as So_Number, "
    	    		+ " TO_CHAR(so.order_date, 'dd-mm-yyy') AS So_Date, "
    	    		+ "ol.ol_number as ol_no, "
    	    		+ " TO_CHAR(ol.offer_date, 'dd-mm-yyyy') AS ol_date, "
    	    		+ "sal.sale_order_no AS WorkOrderNo "
    	    		+ "FROM "
    	    		+ "tdms.submission_slip ol "
    	    		+ "JOIN tdms.sslip_line_item olt ON olt.sub_slip_id = ol.id "
    	    		+ "JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id "
    	    		+ "JOIN tdms.supply_order so ON so.id = sl.so_id "
//    	    		+ "JOIN mdms.item_category co ON co.id = sl.item_category_id "
    	    		+ "JOIN tdms.sale_order sal ON sal.supply_order_id = so.id "
//    	    		+ "JOIN mdms.organization o ON o.id = so.organization_id "
//    	    		+ "JOIN mdms.organization_type ot ON ot.id = o.organization_type_id "
    	    		+ "WHERE "
//    	    		+ " ot.id = 1 AND "
    	    		+ "ol.is_merged = false "
    	    		+ "ORDER BY olId DESC ", nativeQuery = true		)
	List<Object[]> getAllSubSlipListByInspOrgId();
	
	@Query(
    	    value = "SELECT DISTINCT ss.id as ssId, "
    	    		+ " so.order_no as So_Number, "
    	    		+ "  TO_CHAR(so.order_date, 'dd-mm-yyyy') AS So_Date, "
    	    		+ " ss.ol_number as ol_no, "
    	    		+ "  TO_CHAR(ss.offer_date, 'dd-mm-yyyy') AS ol_date,  "
    	    		+ " sal.sale_order_no AS WorkOrderNo,  "
    	    		+ " pn.part_no,  "
    	    		+" pn.material_description, "
    	    		+ " og.organization_name "
    	    		+ " FROM tdms.submission_slip ss "
    	    		+ " JOIN tdms.sslip_line_item olt ON olt.sub_slip_id = ss.id "
    	    		+ " JOIN tdms.so_line_item sl ON sl.id = olt.so_line_item_id "
    	    		+ " JOIN tdms.supply_order so ON so.id = sl.so_id "
    	    		+ " JOIN tdms.sale_order sal ON sal.id = sl.sale_order_id  "
    	    		+ " join tdms.sub_slip_serial_no sssn on sssn.sub_slip_id = ss.id  "
    	    		+ " join tdms.warrant_details wd on wd.id = sssn.warrant_details_id  "
    	    		+ " join tdms.line_item_serial_no lisn on lisn.id = wd.line_item_serial_no_id  "
    	    		+ " join tdms.sub_slip_allotment ssa on ssa.sub_slip_id = ss.id "
    	    		+ " join mdms.part_number pn on pn.id = sl.part_number_id"
    	    		+ " join mdms.organization og on og.id = sl.insp_auth "
    	    		+ " WHERE ss.is_merged = false and lisn.sub_slip_id = ss.id and sssn.final_status = 'pass' and ssa.alloted_to = :empId ORDER BY ssId DESC ", nativeQuery = true		)
	List<Object[]> getAllPassedSubSlipListByInspOrgId(int empId);

	@Query(value = "Select ss.* from tdms.submission_slip ss "
			   + " left join tdms.merge_sslip_mapping msm on msm.sslip_id = ss.id "
			   + " where msm.merge_sslip_id= :mergedSubSlipId ", nativeQuery = true)
	List<SubmissionSlip> findByMergedSslipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);
    
	@Query(value = "Select product_id from ate.partno_product_mapping where part_number_id=:id", nativeQuery = true)
	Integer findProductNoByPartNumberId(Integer id);

	@Query(value = "Select ss.* from tdms.submission_slip ss "
			   + " left join tdms.merge_sslip_mapping msm on msm.sslip_id = ss.id "
			   + " where msm.merge_sslip_id= :mergedSubSlipId limit 1", nativeQuery = true)
	SubmissionSlip findOneByMergedSslipId(@Param("mergedSubSlipId") Integer mergedSubSlipId);
	
	@Query(value = "select sslip.ol_number as olNumber, "
			+ "sslip.offer_date as offerDate, "
			+ "sli.offered_qty as offeredQty, "
			+ " cat.qty "
			+ "from tdms.submission_slip sslip "
			+ "left join tdms.sslip_line_item sli on sli.sub_slip_id = sslip.id "
			+ "left join tdms.category cat on cat.id = sslip.warrant_id "
			+ "where sslip.id = :sSlipId ", nativeQuery = true)
	List<Object[]> getSubSlipDetails(@Param("sSlipId") Integer sSlipId);
	

	@Query(value = "SELECT sslip.id AS SubSlipId,  "
			   + "    sslip.ol_number AS SubSlipNumber, "
			   + "    sslip.offer_date AS offerDate, "
			   + "    string_agg(lisn.serial_no, ','), "
			   + "    slitem.offered_qty  "
			   + "    FROM tdms.submission_slip sslip "
			   + "    LEFT JOIN tdms.sslip_line_item slitem ON sslip.id = slitem.sub_slip_id "
			   + "    LEFT JOIN tdms.line_item_serial_no lisn ON lisn.sub_slip_id = sslip.id "
			   + "    WHERE sslip.warrant_id = :warrantId"
			   + "    GROUP BY sslip.id, sslip.ol_number, sslip.offer_date, slitem.offered_qty, slitem.so_line_item_id", nativeQuery = true)
	List<Object[]> getSubSlipDetailsByWarrantId( @Param("warrantId") Integer warrantId);	
	
	@Query(value = "select distinct id,serial_no   from tdms.line_item_serial_no  where sub_slip_id=:sSlipId ", nativeQuery = true)
	List<Object[]> getSerialNosBySubSlipId(@Param("sSlipId") Integer sSlipId);
	
	@Query(value = "select distinct lisn.id,lisn.serial_no  "
			   + " from tdms.submission_slip ss "
			   + " left join tdms.category c on c.id = ss.warrant_id "
			   + " left join tdms.warrant_details  wd on c.id = wd.category_id "
			   + " left join tdms.line_item_serial_no lisn  on lisn.id=wd.line_item_serial_no_id "
			   + " where ss.id=:sSlipId ", nativeQuery = true)
	List<Object[]> getAllSerialNosBySubSlipId(@Param("sSlipId") Integer sSlipId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tdms.submission_slip SET ol_status_id = :status WHERE id = :id", nativeQuery = true)
	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

	@Query(value="Select po.order_no from tdms.supply_order po  "
			+ "left join tdms.so_line_item sli on sli.so_id=po.id  "
			+ "left join tdms.category c on c.so_line_item_id=sli.id  "
			+ "left join tdms.submission_slip ss on ss.warrant_id = c.id  "
			+ "where ss.id= :subSlipId ", nativeQuery=true)
	String getSoNumberBySubSlipId(int subSlipId);

	@Query(value = "select distinct ss.* from tdms.submission_slip ss " 
			+ "left join tdms.merge_sslip_mapping msm on msm.sslip_id = ss.id " 
			+ "left join tdms.offer_letter ol on ol.merge_sslip_id = msm.merge_sslip_id " 
			+ "where ol.id = :olId order by ss.id asc ", nativeQuery = true)
	List<SubmissionSlip> findByOlId(@Param("olId") int olId);

	@Query(value = " SELECT ss.ID, "
			   + "ss.OL_NUMBER, "
			   + "ss.OL_status_id, "
			   + "COALESCE(TO_CHAR(ss.OFFER_DATE, 'dd-mm-yyyy'),'') AS OFFER_DATE, "
			   + "SG.NAME AS SBU_NAME, "
			   + "PO.INOTE_TYPE_ID, "
			   + "ols.status, "
			   + "il.level_of_inspection, "
			   + "po.order_no,ss.is_notified, "
			   + "po.is_gcc, "
			   + "po.id as supply_order_id, "
			   + "consignee_id, "
			   + "c.id as cat_id, "
			   + "c.category_name, "
			   + "pn.part_no, "
			   + "pn.material_description, "
			   + "so.sale_order_no,"
			   + "og.organization_name "
			   + "FROM TDMS.submission_slip ss "
			   + " LEFT JOIN tdms.category c ON c.id = ss.warrant_id "
			   + " LEFT JOIN tdms.so_line_item sli  on sli.id= c.so_line_item_id "
			   + " LEFT JOIN mdms.organization og on og.id = sli.insp_auth "
			   + " LEFT JOIN TDMS.SUPPLY_ORDER PO ON sli.so_id = PO.ID "
			   + "  LEFT JOIN tdms.sale_order so ON so.id = sli.sale_order_id "
			   + " LEFT JOIN mdms.sales_group SG ON PO.sales_grp_id = SG.id "
			   + " LEFT JOIN mdms.inspection_level il ON ss.inspection_level_id  = il.id "
			   + " Left Join mdms.ol_status ols on ss.ol_status_id = ols.id "
			   + " left join mdms.part_number pn on pn.id = sli.part_number_id "
			   + "  left join tdms.ship s on s.so_line_item_id = sli.id "
			   + " left join tdms.project proj on proj.id = s.project_id "
			   + " left join tdms.sub_slip_allotment ssa on ssa.sub_slip_id = ss.id "
			   + "WHERE  PO.sales_grp_id = :salesGrpId AND ss.ol_status_id in (:statusIds) "
			   + "GROUP BY  "
			   + " ss.id, "
			   + "    ss.OL_NUMBER,  "
			   + "    SG.NAME, "
			   + "    ss.OL_status_id, "
			   + " po.inote_type_id, "
			   + " ols.status , "
			   + " il.level_of_inspection ,  "
			   + " po.order_no, "
			   + " po.is_gcc ,  "
			   + " po.id ,  "
			   + " c.id, "
			   + " c.category_name, "
			   + " pn.part_no, "
			   + " pn.material_description,  "
			   + "    so.sale_order_no,"
			   + " og.organization_name "
			   + "order by ss.id desc", nativeQuery = true)
	List<Object[]> getSubSlipBySalesGrpIdEmpId( @Param("salesGrpId")Integer salesGrpId, @Param("statusIds") List<Integer> statusIds);	
}
