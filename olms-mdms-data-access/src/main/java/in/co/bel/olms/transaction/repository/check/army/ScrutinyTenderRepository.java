package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTender;

public interface ScrutinyTenderRepository extends JpaRepository<ScrutinyTender, Integer> {

	@Query(value = "FROM ScrutinyTender WHERE supplyOrder = :supplyOrder")
	ScrutinyTender getScrutinyTendeBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);
}
