package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.TmrResult;

public interface TmrResultRepository extends JpaRepository<TmrResult, Integer> {
	
	@Query(value = "select * from tdms.tmr_result where ol_line_item_id=:olLineItemId", nativeQuery = true)
	List<TmrResult> findByOllineItemId(@Param("olLineItemId") int olLineItemId);

	@Query(value = "select * from tdms.tmr_result where app_test_id=:testId", nativeQuery = true)
	List<TmrResult> findByTestId(@Param("testId") int testId);
	
	@Query(value= "select * from tdms.tmr_result where created_by=:createdBy and ol_line_item_id=:olLineItemId", nativeQuery = true)
	List<TmrResult> findByCreatedBy(@Param("createdBy") String createdBy, @Param("olLineItemId") int olLineItemId);
}

