package in.co.bel.olms.transaction.repository.insp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OfferLetter;

/**
 * 
 * @author Manasa B
 * @since 24-02-2023
 */

public interface OlInspRepository extends JpaRepository<OfferLetter, Integer>{

	
	@Query(value="select * FROM tdms.offer_letter ol WHERE ol.employee_id = :inspectorId", nativeQuery = true)
	List<OfferLetter> getOfferLetterByInspectorId(@Param("inspectorId") Integer inspectorId);
	
	
	@Query(value="select * FROM tdms.offer_letter WHERE section_officer_id = :acqaoId", nativeQuery = true)
	List<OfferLetter> getOfferLetterByAcqaoId(@Param("acqaoId") Integer acqaoId);

	
	@Query(value="select * FROM tdms.offer_letter WHERE dcqao_id = :dcqaoId", nativeQuery = true)
	List<OfferLetter> getOfferLetterByDcqaoId(@Param("dcqaoId") Integer dcqaoId);

	
	@Query(value="select * FROM tdms.offer_letter WHERE jcqao_id = :jcqaoId", nativeQuery = true)
	List<OfferLetter> getOfferLetterByJcqaoId(@Param("jcqaoId") Integer jcqaoId);

	@Query(value="select * FROM tdms.offer_letter WHERE ccqao_id = :cqaoId", nativeQuery = true)
	List<OfferLetter> getOfferLetterByCqaoId(@Param("cqaoId") Integer cqaoId);
	
}
