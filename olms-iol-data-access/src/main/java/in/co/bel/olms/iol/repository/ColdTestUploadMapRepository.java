package in.co.bel.olms.iol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.entity.ColdTestUploadMapping;


public interface ColdTestUploadMapRepository extends JpaRepository<ColdTestUploadMapping, Integer> {

	@Query(value = "SELECT * FROM iol.cold_test_upload_mapping WHERE id = :Id", nativeQuery = true)
	ColdTestUploadMapping getUploadImagesById(@Param("Id") int Id);

}
