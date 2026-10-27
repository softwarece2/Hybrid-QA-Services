package in.co.bel.olms.ate.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.SbuCameraTestMapping;

@Repository
public interface SbuCameraTestMappingRepository extends JpaRepository<SbuCameraTestMapping, Integer> {
	
	List<SbuCameraTestMapping> findByTestId(Integer testId);
	
	
	@Query(value = "SELECT DISTINCT t.test_id FROM ate.sbu_camera_test_mapping t JOIN mdms.sbu_camera_ip_mapping c ON t.cam_id = c.id WHERE  t.cam_id = :camId", nativeQuery = true)
	    List<Object[]> findByCamId( Integer camId);
	    
	    
	 @Modifying
	 @Transactional
	 @Query(value = "DELETE FROM ate.sbu_camera_test_mapping t WHERE t.cam_id = :camId", nativeQuery = true)
	 void deleteAllByCamId(Integer camId);
	
}
