package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQacl;

public interface ScrutinyQaclRepository extends JpaRepository<ScrutinyQacl, Integer> {

	
	@Query(value="FROM ScrutinyQacl WHERE offerLetter = :offerLetter")
	ScrutinyQacl getScrutinyQaclByOfferLetter(@Param("offerLetter") OfferLetter offerLetter);
}
