package in.co.bel.olms.transaction.repository.check.army;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclAttachment;

public interface ScrutinyQaclAttachmentRepo extends JpaRepository<ScrutinyQaclAttachment, Integer>{

	
	@Query(value = "SELECT * FROM tdms_check_army.scrutiny_qacl_attachment where scrutiny_qacl_id  = :id ", nativeQuery = true)
	List<Object[]> getByQaclIdId(@Param("id") int id);
	
	

}
