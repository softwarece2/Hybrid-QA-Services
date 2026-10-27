package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.WorkloadOlDet;

public interface WorkloadOlDetRepository extends JpaRepository<WorkloadOlDet, Integer> {
	
	@Query(value =  " SELECT insp_phase_activities_id, sum (est_man_days), max( plan_completion) ,  sum( actual_man_days), max( actual_completion) "
			+ " FROM tdms.workload_ol_det left join tdms.workload_ol on workload_ol.id = workload_ol_id "
			+ " where     supply_order_id =:supplyOrderId     group by insp_phase_activities_id  ", nativeQuery = true)
	List<Object[]> getSummaryBySoId(@Param("supplyOrderId") int supplyOrderId);
	
	
}
