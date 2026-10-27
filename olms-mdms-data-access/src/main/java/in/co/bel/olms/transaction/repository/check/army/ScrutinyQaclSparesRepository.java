package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyQaclSpares;

public interface ScrutinyQaclSparesRepository extends JpaRepository<ScrutinyQaclSpares, Integer>{
	
	
	@Query(value="FROM ScrutinyQaclSpares WHERE offerLetter = :offerLetter")
	ScrutinyQaclSpares getScrutinyQaclSparesByOfferLetter(@Param("offerLetter") OfferLetter offerLetter);
}
