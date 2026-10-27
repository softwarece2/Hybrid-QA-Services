package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.MergeSubSlipMapping;

public interface MergeSubSlipMappingRepository extends JpaRepository<MergeSubSlipMapping, Integer>{

	List<MergeSubSlipMapping>  findByMergeOlNoIdId(Integer mergedSubSlipId);

	@Query(value = "SELECT msm.sslip_id FROM tdms.merge_sslip_mapping msm WHERE msm.sslip_id IN (:subSlipIds) " , nativeQuery = true)
	List<Integer> findExistingSubSlipIds(List<Integer> subSlipIds);

}
