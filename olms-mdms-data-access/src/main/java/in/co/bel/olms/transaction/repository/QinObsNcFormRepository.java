package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.QinObsNcForm;

public interface QinObsNcFormRepository extends JpaRepository<QinObsNcForm, Integer> {
	
	@Query(value="SELECT * FROM TDMS.QIN_OBS_NC_FORM WHERE OFFER_LETTER_ID = :offerLetterId LIMIT 1",nativeQuery=true)
	public QinObsNcForm getQinDetailsByOlId(@Param("offerLetterId") int offerLetterId);

}
