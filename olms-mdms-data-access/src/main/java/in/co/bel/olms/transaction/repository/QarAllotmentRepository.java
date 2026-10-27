package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.QarAllotment;

public interface QarAllotmentRepository extends JpaRepository<QarAllotment, Integer>{
	
	
	@Query(value = "select section from tdms.qar_allotment qa where alloted_to = :empId and offer_letter_id = :qaclId", nativeQuery = true)
	List<String> getAllottedQARSections(int empId, int qaclId);

}
