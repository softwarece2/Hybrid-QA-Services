package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Fcm;
import in.co.bel.olms.transaction.entity.SupplyOrder;

public interface FcmRepository extends JpaRepository<Fcm, Integer> {

	
	@Query(value="FROM Fcm WHERE supplyOrder = :supplyOrder")
	Fcm getFcmBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);
}
