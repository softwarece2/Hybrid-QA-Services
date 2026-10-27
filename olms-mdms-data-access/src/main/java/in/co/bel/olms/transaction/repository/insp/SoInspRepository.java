package in.co.bel.olms.transaction.repository.insp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;

/**
 * 
 * @author Manasa B
 * @since 22-02-2023
 */

public interface SoInspRepository extends JpaRepository<SupplyOrder, Integer>{

	
	@Query(value="select * FROM tdms.supply_order WHERE employee_id = :inspectorId", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByInspectionAuthId(@Param("inspectorId") Integer inspectorId);
	
	
	@Query(value="select * FROM tdms.supply_order WHERE section_officer_id = :acqaoId", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByAcqaoId(@Param("acqaoId") Integer acqaoId);

	
	@Query(value="select * FROM tdms.supply_order WHERE dcqao_id = :dcqaoId", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByDcqaoId(@Param("dcqaoId") Integer dcqaoId);

	
	@Query(value="select * FROM tdms.supply_order WHERE jcqao_id = :jcqaoId", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByJcqaoId(@Param("jcqaoId") Integer jcqaoId);

	@Query(value="select * FROM tdms.supply_order WHERE ccqao_id = :cqaoId", nativeQuery = true)
	List<SupplyOrder> getSupplyOrderByCqaoId(@Param("cqaoId") Integer cqaoId);

}
