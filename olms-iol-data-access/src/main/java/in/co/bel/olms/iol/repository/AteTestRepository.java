package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.entity.AteTest;


public interface AteTestRepository extends JpaRepository<AteTest, Integer> {

	@Query(value = "SELECT id, applicable_test_id FROM iol.ate_test where applicable_test_id =:testId", nativeQuery = true)
	List<AteTest> findByApplicableTestId(@Param("testId") int testId);

}
