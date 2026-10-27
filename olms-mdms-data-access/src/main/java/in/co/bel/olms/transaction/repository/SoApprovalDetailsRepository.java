package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SoApprovalDetails;

public interface SoApprovalDetailsRepository extends JpaRepository<SoApprovalDetails, Integer> {

//	@Query(value = "SELECT NEXT_APPROVER_ID FROM TDMS.SO_APPROVAL_DETAILS WHERE SUPPLY_ORDER_ID = :soId || approval_status = 2 ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
//	Integer getEligibleApprover(@Param("soId") int soId);
	
	@Query(value = "SELECT APPROVED_BY FROM TDMS.SO_APPROVAL_DETAILS WHERE SUPPLY_ORDER_ID = :soId ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	Integer getOriginatorBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT * FROM TDMS.SO_APPROVAL_DETAILS WHERE  SUPPLY_ORDER_ID = :soId and approval_status = 2   ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	SoApprovalDetails getEligibleApprover(@Param("soId") int soId);
	
}
