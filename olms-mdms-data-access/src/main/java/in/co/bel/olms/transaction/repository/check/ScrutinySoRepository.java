package in.co.bel.olms.transaction.repository.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.ScrutinySo;

/**
 * 
 * @author Manasa B
 *
 */
public interface ScrutinySoRepository extends JpaRepository <ScrutinySo,Integer>{
	
	@Query(value="FROM ScrutinySo WHERE supplyOrder = :supplyOrder")
	ScrutinySo getScrutinySoBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);
}
