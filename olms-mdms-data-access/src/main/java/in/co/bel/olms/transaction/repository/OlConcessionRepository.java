package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.OlConcession;

/**
 * OlConcessionRepository class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-07-06
 */


public interface OlConcessionRepository extends JpaRepository<OlConcession, Integer> {

	@Query(value = "select * from tdms.ol_concession where offer_letter_id in :offerLetterIds", nativeQuery = true)
	List<OlConcession> findByOfferLetterId(List<Integer> offerLetterIds);
	
}
