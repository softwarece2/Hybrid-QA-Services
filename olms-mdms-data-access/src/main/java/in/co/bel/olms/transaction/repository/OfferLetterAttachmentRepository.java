package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.OfferLetterAttachment;



public interface OfferLetterAttachmentRepository extends JpaRepository<OfferLetterAttachment, Integer>{
	@Query(value = "select * from tdms.offer_letter_attachment where offer_letter_id = :olId and file_name = :fileName limit 1", nativeQuery = true)
	OfferLetterAttachment getByOlIdNFileName(int olId, String fileName);
	
	@Query(value = "SELECT category, STRING_AGG(file_name, ',') AS file_names , COALESCE(TO_CHAR(created_datetime, 'dd-mm-yyyy'), '') AS created_datetime,id "
			+ "FROM tdms.offer_letter_attachment "
			+ "WHERE offer_letter_id = :olId "
			+"GROUP BY category ,created_datetime ,id", nativeQuery = true)
	List<Object[]> getByOlId(int olId);
}
