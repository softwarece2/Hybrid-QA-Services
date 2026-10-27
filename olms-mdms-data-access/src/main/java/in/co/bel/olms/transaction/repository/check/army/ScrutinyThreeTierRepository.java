package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyThreeTier;

public interface ScrutinyThreeTierRepository extends JpaRepository<ScrutinyThreeTier, Integer>{

	@Query(value = "FROM ScrutinyThreeTier WHERE offerLetter = :offerLetter")
	ScrutinyThreeTier getScrutinyThreeTierByOfferLetter(@Param ("offerLetter") OfferLetter offerLetter);
}
