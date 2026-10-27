package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SoApprovalDetAttachment;

public interface SoApprovalDetAttachRepo extends JpaRepository<SoApprovalDetAttachment, Integer> {

	
	
	@Query(value = "SELECT * FROM tdms.so_approval_det_attachment where so_approval_det_id  = :id ", nativeQuery = true)
	List<Object[]> getBySoApprovalDetailsId(@Param("id") int id);
	

}
