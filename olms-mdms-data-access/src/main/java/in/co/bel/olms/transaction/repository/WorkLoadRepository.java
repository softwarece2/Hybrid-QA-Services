package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.WorkLoad;

public interface WorkLoadRepository extends JpaRepository<WorkLoad, Integer> {
	
	@Query(value="FROM WorkLoad WHERE supplyOrder= :supplyOrder")
	WorkLoad getWorkLoadBySupplyOrder(@Param("supplyOrder")SupplyOrder supplyOrder);
	
	
	@Query(value = "SELECT  so.id, so.order_no, SUM( est_man_days) AS est , SUM (actual_man_days ) AS actual          FROM tdms.workload_insp_act_det wkldOldet        LEFT JOIN mdms.insp_phase_activities ipa ON ipa.id= insp_phase_activities_id   \r\n"
			   + "  LEFT JOIN tdms.workload wkld  ON workload_id = wkld.id   left join tdms.supply_order so on wkld.supply_order_id = so.id  "
			   + "  where so.created_datetime > :fromDateVal  and so.created_datetime <= :toDateVal  GROUP BY  so.id, so.order_no"  , nativeQuery = true)
	List<Object[]> getwrkLoadDataByDate(@Param("fromDateVal") Date fromDateVal,
			@Param("toDateVal") Date toDateVal  );
	
	
	
}
