package in.co.bel.olms.transaction.repository.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.InspOlIg;

public interface InspOlIgRepository extends JpaRepository<InspOlIg, Integer> {
	
	@Query(value = "FROM InspOlIg WHERE offerLetter = :offerLetter")
	InspOlIg getByInspOlIg(@Param ("offerLetter") OfferLetter offerLetter);

}
