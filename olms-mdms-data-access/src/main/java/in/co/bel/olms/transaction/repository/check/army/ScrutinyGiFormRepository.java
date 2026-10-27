package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyGiForm;

public interface ScrutinyGiFormRepository extends JpaRepository<ScrutinyGiForm, Integer>{

	@Query(value = "FROM ScrutinyGiForm WHERE supplyOrder = :supplyOrder")
	ScrutinyGiForm getScrutinyGiFormBySupplyOrder(@Param ("supplyOrder") SupplyOrder supplyOrder);
}
