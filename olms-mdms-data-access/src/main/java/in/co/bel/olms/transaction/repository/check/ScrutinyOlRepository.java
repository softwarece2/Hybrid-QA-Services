package in.co.bel.olms.transaction.repository.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.ScrutinyOl;

public interface ScrutinyOlRepository extends JpaRepository<ScrutinyOl, Integer> {
	
	@Query(value = "FROM ScrutinyOl WHERE offerLetter = :offerLetter")
	ScrutinyOl getByOfferLetter(@Param ("offerLetter") OfferLetter offerLetter);

}
