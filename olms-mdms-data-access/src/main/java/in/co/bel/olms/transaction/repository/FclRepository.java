package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Fcl;
import in.co.bel.olms.transaction.entity.SupplyOrder;


/**
 * 
 * @author Manasa B
 * @since 10-01-2023
 *
 */
public interface FclRepository extends JpaRepository<Fcl, Integer> {

	
	@Query(value="FROM Fcl WHERE supplyOrder = :supplyOrder")
	Fcl getFclBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);
	
}
