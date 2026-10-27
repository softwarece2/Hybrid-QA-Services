package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;



public interface ReportsRepository extends JpaRepository<SupplyOrder, Integer>{
	
	@Query(value="SELECT so.id,so.order_no, STRING_AGG(DISTINCT e.ename, ',') AS inspector_name, STRING_AGG(DISTINCT con.name, ',') AS consignee_name,  "
			+ "STRING_AGG(DISTINCT indenter.name, ',') AS indenter_name, proj.project_name "
			+ "FROM tdms.so_allotment sa "
			+ "LEFT JOIN mdms.employee e ON e.id = sa.alloted_to "
			+ "LEFT JOIN tdms.supply_order so ON so.id = sa.supply_order_id "
			+ "LEFT JOIN tdms.so_consignee_mapping scmap ON so.id = scmap.supply_order_id "
			+ "LEFT JOIN mdms.consignee con ON con.id = scmap.consignee_id "
			+ "LEFT JOIN mdms.indenter indenter ON indenter.id = so.indenter_id "
			+ "LEFT JOIN tdms.project proj ON proj.id = so.project_id "
			+ "WHERE sa.supply_order_id IN (SELECT id FROM tdms.supply_order WHERE order_date BETWEEN :fromDateVal AND :toDateVal) AND "
			+ "sa.alloted_to IN (SELECT ID FROM mdms.employee e WHERE e.emp_role_id = :emproleId)  "
			+ "GROUP BY so.order_no, so.id, proj.project_name", nativeQuery = true)
	List<Object[]> getReportsDataByDate(@Param("fromDateVal") Date fromDateVal,
			@Param("toDateVal") Date toDateVal, int emproleId );
	
}
