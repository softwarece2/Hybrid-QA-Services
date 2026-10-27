package in.co.bel.olms.transaction.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OlCrossCheckAllotment;

public interface OlCrossCheckAllotmentRepository extends JpaRepository<OlCrossCheckAllotment, Integer> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE tdms.ol_cross_check_allotment " + "SET is_deligation = true "
			+ "WHERE offer_letter_id = :olId " + "  AND created_datetime < :createdDate "
			+ "  AND alloted_by = :empId", nativeQuery = true)
	void updateDeligationStatus(@Param("empId") Integer empId, @Param("olId") Integer olId,
			@Param("createdDate") Date createdDate);

}