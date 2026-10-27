package in.co.bel.olms.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.OlStatus;

public interface OlStatusRepository extends JpaRepository<OlStatus, Integer>  {

	
	@Query(value = "SELECT * FROM mdms.ol_status WHERE LOWER(status) = :status ", nativeQuery = true)
	Optional<OlStatus> findStatus(String status);
	
	

}
