package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.FileClosingForm;
import in.co.bel.olms.transaction.entity.SupplyOrder;

public interface FileClosingFormRepository extends JpaRepository<FileClosingForm,Integer > {

	@Query(value="FROM FileClosingForm WHERE supplyOrder = :supplyOrder")
	FileClosingForm getFileCloseFormBySupplyOrder(@Param("supplyOrder") SupplyOrder supplyOrder);

}
