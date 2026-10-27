package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.entity.ColdTestUpload;


public interface ColdTestUploadRepository extends JpaRepository<ColdTestUpload, Integer> {

	@Query(value = "SELECT * FROM iol.cold_test_upload where cold_test_id = :testId", nativeQuery = true)
	List<ColdTestUpload> getUploadImagesByTestId(@Param("testId") int testId);

}
