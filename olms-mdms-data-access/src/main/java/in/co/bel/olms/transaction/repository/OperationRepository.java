package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.Category;
import in.co.bel.olms.transaction.entity.Operation;

public interface OperationRepository extends JpaRepository<Operation, Integer>{

	Operation findByWarrant(Category existingCategory);
	
	@Modifying
	@Query(value = "DELETE FROM tdms.operation WHERE warrant_id=:warrantId", nativeQuery = true)
	void deleteByWarrantId(int warrantId);

}
