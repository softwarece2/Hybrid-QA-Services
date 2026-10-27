package in.co.bel.olms.ate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.ate.entity.SpecStatus;

public interface SpecStatusRepository extends JpaRepository<SpecStatus, Integer> {

	Optional<SpecStatus> findTopBySpecIdOrderByCreatedAtDesc(Integer specId);

	List<SpecStatus> findBySpecIdOrderByCreatedAtDesc(Integer specId);

	@Query("Select ss from SpecStatus ss LEFT JOIN ss.spec s WHERE s.test.id = :testId ORDER BY ss.id ASC")
	List<SpecStatus> findByTestId(@Param("testId") Integer testId);
	
	List<SpecStatus> findByActiveOrderByCreatedAtDesc(Boolean active);
}
