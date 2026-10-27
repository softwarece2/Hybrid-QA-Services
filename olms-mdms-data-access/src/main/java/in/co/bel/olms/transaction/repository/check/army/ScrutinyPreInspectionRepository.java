package in.co.bel.olms.transaction.repository.check.army;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyPreInspection;

public interface ScrutinyPreInspectionRepository extends JpaRepository<ScrutinyPreInspection, Integer>{

	@Query(value = "FROM ScrutinyPreInspection WHERE offerLetter = :offerLetter")
	ScrutinyPreInspection getScrutinyPreInspectionByOfferLetter(@Param ("offerLetter") OfferLetter offerLetter);
	
}
