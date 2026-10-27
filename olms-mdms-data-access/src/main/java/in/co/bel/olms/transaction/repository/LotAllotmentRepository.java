package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.LotAllotment;

public interface LotAllotmentRepository extends JpaRepository<LotAllotment, Integer>{

	@Query(value= "Select alloted_to from tdms.lot_allotment where merge_sslip_id = :mergeSubSlipId", nativeQuery = true)
	List<Integer> findExisitngIdByMergeSubSlipId(int mergeSubSlipId);

	List<LotAllotment> findByMergeSubSlipIdOrderByCreatedAtDesc(int mergeSubSlipId);

}
