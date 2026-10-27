package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.TestBySpecMap;

@Repository
public interface TestBySpecMapRepository extends JpaRepository<TestBySpecMap, Integer>{
	
	@Query(value = "select tm.test_by_mst_id, tb.test_by "
			+ "from ate.test_by_spec_map tm "
			+ "left join ate.test_by_mst tb on tm.test_by_mst_id = tb.id "
			+ "left join ate.spec s on tm.spec_id = s.id "
			+ "where s.prod_id = :prodId and s.test_id = :testId and s.id = :specId order by tm.test_by_mst_id asc ", nativeQuery = true)
	List<Object[]> getTestBySpecMap(@Param("prodId") int prodId, @Param("testId") int testId, @Param("specId") int specId);
	
}
