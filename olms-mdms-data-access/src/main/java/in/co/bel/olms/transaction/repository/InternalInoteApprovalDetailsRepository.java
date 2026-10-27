package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.InternalInoteApprovalDetails;

public interface InternalInoteApprovalDetailsRepository extends JpaRepository<InternalInoteApprovalDetails, Integer> {
	
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
	
	
	@Query(value = "SELECT "
			+ "  i.id,i.offer_letter_no, i.offer_letter_id "
			+ "FROM tdms.internal_inote AS i "
			+ "LEFT JOIN tdms.ol_allotment AS ol "
			+ "  ON i.offer_letter_id = ol.offer_letter_id  "
			+ "WHERE"
			+ "  ol.alloted_to = :empId ", nativeQuery = true)
	List<Object[]> getAllRecordsByEmpId(int empId);
	
}
