package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SubSlipApprovalDetails;

public interface SubSlipApprovalDetailsRepository extends JpaRepository<SubSlipApprovalDetails, Integer> {
	
	@Query(value = "SELECT NEXT_APPROVER_ID FROM TDMS.SSLIP_APPROVAL_DETAILS WHERE SSLIP_ID = :subSlipId ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	Integer getEligibleApprover(@Param("subSlipId") int subSlipId);

	@Query(value = "SELECT APPROVED_BY FROM TDMS.OL_APPROVAL_DETAILS WHERE OFFER_LETTER_ID = :olId ORDER BY CREATED_DATETIME DESC LIMIT 1", nativeQuery = true)
	Integer getOriginatorByOlId(@Param("olId") int olId);
	
	@Query(value = "SELECT CASE "
			+ "         WHEN COUNT(OAD.ID) > 0 THEN TRUE "
			+ "         ELSE FALSE "
			+ "       END AS CQAO_APPROVAL_STATUS "
			+ "FROM   TDMS.SSLIP_APPROVAL_DETAILS OAD "
			+ "       LEFT OUTER JOIN MDMS.EMPLOYEE E "
			+ "                    ON OAD.Created_BY = E.ID "
			+ "WHERE  SSLIP_ID = :subSlipId "
			+ "       AND OAD.APPROVAL_STATUS = 1 "
			+ "       AND E.EMP_ROLE_ID = 3 "
			+ "LIMIT 1", nativeQuery = true)
	boolean getLatestApprovalStatusByOlId(@Param("subSlipId") Integer subSlipId);
	
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


	
	@Query(value = "select * from tdms.SSLIP_APPROVAL_DETAILS " +
			"where SSLIP_ID = :subSlipId " +
			"order by created_datetime desc limit 1", nativeQuery = true)
	Optional<SubSlipApprovalDetails> getLastRecordBySubSlipId(Integer subSlipId);
	
	@Query(value = "select Distinct sss.id, lis.serial_no, sss.final_status from tdms.submission_slip ss "
			   + "    left join tdms.sub_slip_serial_no  sss on sss.sub_slip_id=ss.id "
			   + "    left join tdms.warrant_details wd ON wd.id = sss.warrant_details_id  "
			   + "    left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id  "
			   + "     where sss.final_status='pass' and ss.id = :subSlipId ORDER BY lis.serial_no ", nativeQuery = true)
	List<Object[]> getPassedRegNoBySubSlipId(Integer subSlipId);
	
	@Query(value = "select Distinct sss.id, lis.serial_no, sss.final_status, rt.id as regNoId from tdms.submission_slip ss "
			   + "    left join tdms.sub_slip_serial_no  sss on sss.sub_slip_id=ss.id "
			   + "    left join tdms.warrant_details wd ON wd.id = sss.warrant_details_id  "
			   + "    left join tdms.line_item_serial_no lis on lis.id = wd.line_item_serial_no_id  "
			   + "    left join tdms.so_line_item soli on soli.id = lis.so_line_item_id"
			   + "    left join mdms.registration_no_type rt on rt.id = soli.registration_no "
			   + "     where sss.final_status <> 'in-process' and ss.id = :subSlipId ORDER BY lis.serial_no ", nativeQuery = true)
	List<Object[]> getRegNoBySubSlipId(Integer subSlipId);
	
	@Query(value = "SELECT sad.sslip_id " +
		       "FROM tdms.sslip_approval_details sad " +
		       "WHERE sad.sslip_id IN (:subSlipIds)", nativeQuery = true)
		List<Integer> findExistingSubSlipIds(@Param("subSlipIds") List<Integer> subSlipIds);

	@Query(value = "SELECT sad.sslip_id " +
		       "FROM tdms.sslip_approval_details sad " +
		       "WHERE sad.sslip_id IN (:subSlipIds) AND sad.next_approver_id = :empId ", nativeQuery = true)	
	List<Integer> findAllotedSubSlipsIds(List<Integer> subSlipIds, int empId);
}
