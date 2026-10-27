package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.OlApprovalDetails;

import java.util.Optional;

public interface OlApprovalDetailsRepository extends JpaRepository<OlApprovalDetails, Integer> {
	
	@Query(value = "SELECT NEXT_APPROVER_ID FROM TDMS.OL_APPROVAL_DETAILS WHERE OFFER_LETTER_ID = :olId ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	Integer getEligibleApprover(@Param("olId") int olId);

	@Query(value = "SELECT APPROVED_BY FROM TDMS.OL_APPROVAL_DETAILS WHERE OFFER_LETTER_ID = :olId ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	Integer getOriginatorByOlId(@Param("olId") int olId);
	
	@Query(value = "SELECT CASE "
			+ "         WHEN COUNT(OAD.ID) > 0 THEN TRUE "
			+ "         ELSE FALSE "
			+ "       END AS CQAO_APPROVAL_STATUS "
			+ "FROM   TDMS.OL_APPROVAL_DETAILS OAD "
			+ "       LEFT OUTER JOIN MDMS.EMPLOYEE E "
			+ "                    ON OAD.Created_BY = E.ID "
			+ "WHERE  OFFER_LETTER_ID = :olId "
			+ "       AND OAD.APPROVAL_STATUS = 1 "
			+ "       AND E.EMP_ROLE_ID = 3 "
			+ "LIMIT 1", nativeQuery = true)
	boolean getLatestApprovalStatusByOlId(@Param("olId") int olId);
	
	@Query(value = "SELECT CASE "
			+ "         WHEN COUNT(OAD.ID) > 0 THEN TRUE "
			+ "         ELSE FALSE "
			+ "       END AS CQAO_APPROVAL_STATUS "
			+ "FROM   TDMS.OL_APPROVAL_DETAILS OAD "
			+ "       LEFT OUTER JOIN MDMS.EMPLOYEE E "
			+ "                    ON OAD.Created_BY = E.ID "
			+ "WHERE  FILE_NOTE_Id= :fileNoteId "
			+ "       AND OAD.APPROVAL_STATUS = 1 "
			+ "       AND E.EMP_ROLE_ID = 3 "
			+ "LIMIT 1", nativeQuery = true)
	boolean getLatestApprovalStatusByFileNoteId(@Param("fileNoteId") int fileNoteId);
		
	
	@Query(value = "SELECT NEXT_APPROVER_ID FROM TDMS.OL_APPROVAL_DETAILS WHERE file_note_id = :fileNoteId  ORDER BY CREATED_DATETIME DESC LIMIT 1 ", nativeQuery = true)
	Integer getNextApprover(@Param("fileNoteId") int fileNoteId);
	
	@Query(value = "SELECT note_no FROM TDMS.OL_APPROVAL_DETAILS WHERE file_note_id = :fileNoteId ORDER BY CREATED_DATETIME DESC LIMIT 1 ", nativeQuery = true)
	Integer getLatestNoteNoByFileNote(@Param("fileNoteId") int fileNoteId);

	@Query(value = "select * from tdms.ol_approval_details " +
			"where offer_letter_id = :olId " +
			"order by created_datetime desc limit 1", nativeQuery = true)
	Optional<OlApprovalDetails> getLastRecordByOlId(int olId);
	
}
