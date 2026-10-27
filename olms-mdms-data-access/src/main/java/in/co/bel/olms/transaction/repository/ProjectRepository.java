package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	@Query(value = "select project_name from tdms.project", nativeQuery = true)
	List<String> findProjectNames();

  @Query(value = "SELECT distinct P.PROJECT_NAME,"
      + " S.SHIP_NAME,"
      + " C.CATEGORY_NAME,"
      + " S.est_date, "
      + " S.rev_no, C.wt_iss_date "
      + " FROM TDMS.PROJECT P "
      + " LEFT JOIN TDMS.SHIP S ON P.ID = S.PROJECT_ID "
      + " LEFT JOIN TDMS.CATEGORY C ON S.ID = C.SHIP_ID"
//			+ " LEFT JOIN TDMS.SALE_ORDER SO ON"
//			+ " C.ID = SO.CATEGORY_ID"
			+ " WHERE P.SUPPLY_ORDER_ID IS NOT NULL  "
//			+ "AND SO.id  IS NOT NULL AND  SO.sale_order_no IS NOT NULL"
			+ "  AND P.SUPPLY_ORDER_ID =:soId", nativeQuery = true)
	List<Object[]> findProjectDetailsBySoId(@Param("soId") int soId);

	@Query(value = "SELECT P.PROJECT_NAME,S.SHIP_NAME,C.CATEGORY_NAME,SO.SALE_ORDER_NO, SO.ACTIVE, SO.ID , S.est_date, S.rev_no, C.wt_iss_date "
			+ "FROM TDMS.PROJECT P LEFT JOIN TDMS.SHIP S ON P.ID = S.PROJECT_ID LEFT JOIN TDMS.CATEGORY "
			+ "C ON S.ID = C.SHIP_ID LEFT JOIN TDMS.SALE_ORDER SO ON"
			+ " C.ID = SO.CATEGORY_ID WHERE P.SUPPLY_ORDER_ID IS NOT NULL AND SO.id  IS NOT NULL  AND  SO.sale_order_no IS NOT NULL", nativeQuery = true)
	List<Object[]> findAllProjectDetails();

	@Query(value = "SELECT P.PROJECT_NAME,S.SHIP_NAME,C.CATEGORY_NAME,SO.SALE_ORDER_NO FROM TDMS.PROJECT P "
			+ "LEFT JOIN TDMS.SHIP S ON P.ID = S.PROJECT_ID LEFT JOIN TDMS.CATEGORY C ON S.ID= C.SHIP_ID LEFT JOIN "
			+ "TDMS.SALE_ORDER SO ON C.ID = SO.CATEGORY_ID LEFT JOIN TDMS.SUPPLY_ORDER SP ON SP.PROJECT_ID = P.ID"
			+ " LEFT JOIN MDMS.EMPLOYEE E ON E.SBU_ID = SP.SALES_GRP_ID WHERE E.ID = :empId", nativeQuery = true)
	List<String[]> getProjectForSalesGrp(@Param("empId") int empId);

	@Query(value = "SELECT p.id, p.project_name FROM TDMS.PROJECT P LEFT JOIN TDMS.SUPPLY_ORDER SO ON P.SUPPLY_ORDER_ID = SO.ID "
			+ "WHERE (:orgId = -1 OR SO.ORGANIZATION_ID = :orgId) AND (:salesGrpId = -1 OR SO.SALES_GRP_ID = :salesGrpId)", nativeQuery = true)
	List<Object[]> getAllByOrgId(@Param("orgId") int orgId, @Param("salesGrpId") int salesGrpId);

	@Query(value = "select id, project_name from tdms.project", nativeQuery = true)
	List<Object[]> getAllProjects();

	@Query(value = "SELECT p.id, p.project_name FROM TDMS.PROJECT P LEFT JOIN TDMS.SUPPLY_ORDER SO ON P.SUPPLY_ORDER_ID = SO.ID LEFT JOIN mdms.sales_group sg ON so.sales_grp_id = sg.id LEFT JOIN mdms.organization org ON sg.organization_id = org.id "
			+ "WHERE (:orgId = -1 OR sg.ORGANIZATION_ID = :orgId)", nativeQuery = true)
	List<Object[]> getAllByFirmId(@Param("orgId") int orgId);

	@Query(value = "SELECT pro.id as id, TO_CHAR(pro.created_datetime, 'DD-MM-YYYY') as cr_dt, COALESCE(ind.name, 'Opa name not available') as opa_nm, so.value as pr_va, COALESCE(ol.offered_qty, 0) as cu_qty, COALESCE(ol.cleared_qty, 0) as cl_qty, COALESCE(ol.offered_qty, 0) + COALESCE(ol.cleared_qty, 0) as tot_qty, tmr.app_test_id as tmr_id, ofl.id as ofl_id, COUNT(tmr.app_test_id) OVER (PARTITION BY tmr.app_test_id) as tot_te, COALESCE(ofl.ol_number, 'QACL numbers not available') as qacl_nu, (SELECT COUNT(*) FROM tdms.ol_line_item oli WHERE oli.offer_letter_id = ofl.id) as nu_rfr FROM tdms.project as pro LEFT JOIN tdms.supply_order as so on pro.supply_order_id = so.id LEFT JOIN tdms.sale_order as sl on so.id = sl.supply_order_id LEFT JOIN tdms.offer_letter as ofl on sl.id = ofl.sale_order_id LEFT JOIN tdms.ol_line_item as ol on ofl.id = ol.offer_letter_id LEFT JOIN mdms.indenter as ind on so.indenter_id = ind.id LEFT JOIN tdms.tmr_result as tmr on ol.id = tmr.ol_line_item_id WHERE ( :poId  = -1 OR pro.id = :poId )  GROUP BY pro.id, cr_dt, opa_nm, pr_va, cu_qty, cl_qty, tmr_id, ofl_id, qacl_nu ORDER BY opa_nm ASC", nativeQuery = true)
	List<Object[]> getProjectSummaryByProjectId(@Param("poId") int poId);

}
