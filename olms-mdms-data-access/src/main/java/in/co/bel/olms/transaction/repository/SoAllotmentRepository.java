package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SoAllotment;

public interface SoAllotmentRepository  extends JpaRepository<SoAllotment, Integer> {

	@Query(value="SELECT t.supply_order_id, so.order_no, so.order_date,  "
			   + "STRING_AGG(t.ename, ', ' ORDER BY t.created_datetime DESC) AS alloted_to, "
			   + "STRING_AGG(CAST(t.alloted_to AS TEXT), ','ORDER BY t.created_datetime DESC) AS alloted_to_id,  "
			   + "STRING_AGG(CAST(t.alloted_by AS TEXT), ',' order by t.created_datetime desc) AS alloted_by, "
			    +" pn.part_no,itc.category, "
			   + "STRING_AGG(TO_CHAR(t.created_datetime,'DD-MM-YYYY HH:MI:SS AM' ), ','ORDER BY t.created_datetime DESC) AS created_datetime  "
			   + "FROM ( "
			   + " SELECT DISTINCT supply_order_id, soal.alloted_to, e.ename, soal.alloted_by, soal.created_datetime "
			   + " FROM tdms.so_allotment soal "
			   + " LEFT JOIN mdms.employee e ON e.id = soal.alloted_to "
			   + " WHERE (alloted_to IN ( "
			   + "  WITH RECURSIVE reporting_officer_tree AS ( "
			   + "  SELECT id, reporting_officer_id  "
			   + "  FROM mdms.employee  "
			   + "  WHERE reporting_officer_id = :empId "
			   + "  UNION ALL "
			   + "  SELECT employee.id, employee.reporting_officer_id  "
			   + "  FROM mdms.employee   "
			   + "  JOIN reporting_officer_tree ON  "
			   + "  employee.reporting_officer_id = reporting_officer_tree.id "
			   + " ) "
			   + " SELECT id FROM reporting_officer_tree)) OR alloted_to = :empId  or alloted_by = :empId "
			   + " ) t "
			   + "LEFT JOIN tdms.supply_order so ON so.id= t.supply_order_id "
			   + " LEFT JOIN tdms.so_line_item sli ON sli.so_id= so.id "
			   + "      LEFT JOIN mdms.part_number   pn ON pn.id=sli.part_number_id "
			   + "      LEFT JOIN mdms.item_category  itc ON itc.id=pn.item_category_id "
			   + "GROUP BY supply_order_id, so.order_date, so.order_no,pn.part_no,itc.category order by so.order_date desc", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsByEmpId(int empId);
	
	@Query(value="SELECT * FROM tdms.so_allotment WHERE supply_order_id = :soId AND alloted_to = :empId order by created_datetime desc", nativeQuery=true)
	public SoAllotment getAllotedSoToEmp(int soId, int empId);
	
	@Query(value="SELECT alloted_by, alloted_to FROM tdms.so_allotment WHERE supply_order_id = :soId order by created_datetime desc", nativeQuery=true)
	public List<Object[]> getAllotedByNToOfSo(int soId);
	
	@Query(value="SELECT * FROM tdms.so_allotment WHERE alloted_to = :empId order by created_datetime desc", nativeQuery=true)
	public List<SoAllotment> getSoAllotmentByEmpId(int empId);
	
	@Query(value="SELECT so.id, so.order_no, so.order_date, sales.name AS sales_grp_name, project.project_name, so.status, "
			+ "scrutinySo.submitted_date AS scrutiny_submitted_date, fcl.submitted_date AS fcl_submitted_date, "
			+ "fcm.submitted_date AS fcm_subnitted_date, fileClosing.submitted_date AS file_closing_submitted_date, so.description ,scrutinyTender.submitted_date as scrutinyTenderSub, st.so_type "
			+ "FROM tdms.so_allotment soall  "
			+ "LEFT JOIN tdms.supply_order so ON so.id = soall.supply_order_id "
			+ "LEFT JOIN mdms.sales_group sales ON sales.id = so.sales_grp_id "
			+ "LEFT JOIN tdms.project project ON project.id = so.project_id "
			+ "LEFT JOIN tdms_check.scrutiny_so scrutinySo ON scrutinySo.supply_order_id = so.id "
			+ "LEFT JOIN tdms.fcl fcl ON fcl.supply_order_id = so.id "
			+ "LEFT JOIN tdms.fcm fcm ON fcm.supply_order_id = so.id "
			+ "LEFT JOIN tdms.file_closing_form fileClosing ON fileClosing.supply_order_id = so.id "
            + "LEFT JOIN tdms_check_army.scrutiny_tender scrutinyTender ON scrutinyTender.supply_order_id =  so.id "
            + "LEFT JOIN mdms.so_type st ON st.id =  so.so_type_id "
			+ "WHERE alloted_to = :empId", nativeQuery=true)
	public List<Object[]> getAllotedSoDetailsByEmpId(int empId);
	
	@Query(value = " SELECT t.supply_order_id, so.order_no, so.order_date,so.received_date, so.file_no,   COALESCE(sl.part_no, '') AS part_no, "
      + " STRING_AGG(t.ename, ', ') AS alloted_to, "
      + " STRING_AGG(distinct CAST(t.alloted_to AS TEXT), ',') AS alloted_to_id, "
      + " STRING_AGG(distinct CAST(t.alloted_by AS TEXT), ',') AS alloted_by, "
      + " org.organization_name ,"
      + " so.is_notified, "
      + " sost.status,"
      + " so.so_status_id "
      + " FROM ( "
      + " SELECT DISTINCT supply_order_id, soal.alloted_to, e.ename, soal.alloted_by, soal.created_datetime "
      + " FROM tdms.so_allotment soal "
      + " LEFT JOIN mdms.employee e ON e.id = soal.alloted_to "
      + " WHERE (alloted_to IN ( "
      + " WITH RECURSIVE reporting_officer_tree AS ( "
      + " SELECT id, reporting_officer_id "
      + " FROM mdms.employee "
      + " WHERE reporting_officer_id = :empId "
      + " UNION ALL "
      + " SELECT employee.id, employee.reporting_officer_id "
      + " FROM mdms.employee "
      + " JOIN reporting_officer_tree ON "
      + " employee.reporting_officer_id = reporting_officer_tree.id "
      + " ) "
      + " SELECT id FROM reporting_officer_tree)) OR alloted_to = :empId or alloted_by = :empId "
      + " ) t "
      + " LEFT JOIN tdms.supply_order so ON so.id= t.supply_order_id "
      + " left join tdms.so_line_item sl on sl.so_id=so.id "
      + " LEFT JOIN mdms.organization org ON org.id = sl.insp_auth "
      + " LEFT JOIN mdms.so_status sost ON sost.id = so.so_status_id "
      + " WHERE so.sales_grp_id = :salesGrpId "
      + " AND so.so_status_id IN (:statusIds)"
      + " GROUP BY supply_order_id, so.order_date, so.order_no, so.received_date, so.file_no, COALESCE(sl.part_no, ''), org.organization_name, so.is_notified, t.created_datetime, sost.status, so.so_status_id order by t.created_datetime desc", nativeQuery = true)
	public List<Object[]> getAllotmentDetailsByEmpIdAndSalesGrp(@Param("empId") int empId, @Param("salesGrpId") int salesGrpId, List<Integer> statusIds);

  @EntityGraph(attributePaths = {"allotedTo"})
	public List<SoAllotment> findBySupplyOrderIdOrderByCreatedDateDesc(Integer soId);
}
