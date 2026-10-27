package in.co.bel.olms.transaction.repository.insp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.InspectionFileNote;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface InspectionFileNoteRepository extends JpaRepository<InspectionFileNote, Integer> {

    @Query(value = "SELECT * FROM tdms.inspection_note WHERE offer_letter_id=:offerLetterId ORDER BY created_datetime DESC LIMIT 1", nativeQuery = true)
    InspectionFileNote findByOfferLetterId(Integer offerLetterId);

    @Query(value = "select" +
            " in2.*" +
            " from" +
            " tdms.inspection_note in2" +
            " inner join tdms.file_note_ol_mapping fnom on" +
            " in2.offer_letter_id = fnom.offer_letter_id" +
            " where" +
            " fnom.offer_letter_id in :offerLetterIds limit 1", nativeQuery = true)
    InspectionFileNote findByOfferLetterIds(List<Integer> offerLetterIds);


    @Query(value = " SELECT fln.id, STRING_AGG(DISTINCT ol.ol_number, ',') AS ol_numbers,so.order_no ,sg.name ,STRING_AGG( DISTINCT   pj.project_name,',') as project_name ,fln.ol_date, "
            + " (SELECT CASE    "
            + "                       WHEN ( oad.approval_status = 1 And OAD.created_by=3 ) THEN 'Approved by CQAO'    "
            + "                       ELSE 'Held with '    "
            + "                            || (SELECT r.emp_role    "
            + "                                FROM   mdms.emp_role r    "
            + "                                       LEFT JOIN mdms.employee ee    "
            + "                                              ON ee.emp_role_id = r.id    "
            + "                                WHERE  ee.id = oad.next_approver_id)    "
            + "                     end AS OL_STATUS    "
            + "              FROM   tdms.ol_approval_details oad    "
            + "                     LEFT JOIN mdms.employee e    "
            + "                            ON oad.approved_by = e.id    "
            + "                     LEFT JOIN mdms.emp_role er    "
            + "                            ON e.emp_role_id = er.id    "
            + "              WHERE  oad.file_note_id = fln.id    "
            + "              GROUP  BY oad.id,    "
            + "                        er.emp_role,    "
            + "                        e.emp_role_id    "
            + "              ORDER  BY oad.id DESC     "
            + "              LIMIT  1)                                         AS ol_status,so.id  as so_id, fln.ol_description "
            + " FROM tdms.inspection_note fln"
            + " LEFT JOIN tdms.file_note_ol_mapping fm ON fln.id = fm.file_note_id"
            + " LEFT JOIN tdms.offer_letter ol ON fm.offer_letter_id = ol.id"
            + " left join tdms.supply_order so  on fln.supply_order_id  = so.id "
            + " left join mdms.sales_group  sg  on so.sales_grp_id= sg.id "
            + " left join tdms.project pj  on so.id=pj.supply_order_id "
            + " WHERE   fm.file_note_id IS NOt NULL and so.organization_id= 151"
            + " GROUP BY fln.id ,so.order_no,sg.name ,so.id "
            + " order by fln.ol_date desc", nativeQuery = true)
    List<Object[]> findFileNotes();


    @Query(value = "select supply_order_id from tdms.inspection_note where id =:fileNoteId ", nativeQuery = true)
    Integer findSoId(int fileNoteId);

    @Query(value = "select " +
            "   in2.* " +
            "from " +
            "   tdms.inspection_note in2 " +
            "inner join tdms.file_note_ol_mapping fnom on " +
            "   in2.id = fnom.file_note_id " +
            "where " +
            "   fnom.offer_letter_id in :offerLetterIds " +
            "limit 1", nativeQuery = true)
    InspectionFileNote findFileNoteByOlIds(List<Integer> offerLetterIds);

    @Query(value = "select " +
            "fnom.offer_letter_id," +
            " ol.consignee_id " +
            "from tdms.file_note_ol_mapping fnom " +
            "join tdms.offer_letter ol on " +
            "fnom.offer_letter_id = ol.id " +
            "where fnom.file_note_id  = :fileNoteId", nativeQuery = true)
    List<Object[]> getOlNConsigneeByFileNoteId(int fileNoteId);
    
    @Query(value = "select " +
            "fnom.offer_letter_id," +
            " ol.consignee_id " +
            "from tdms.file_note_ol_mapping fnom " +
            "join tdms.offer_letter ol on " +
            "fnom.offer_letter_id = ol.id " +
            "where fnom.file_note_id  = :olId", nativeQuery = true)
    List<Object[]> getOlNConsigneeByOlId(int olId);

    @Modifying
    @Transactional
    @Query(value = "update tdms.file_note_ol_mapping set inote_no =:inoteNo where file_note_id=:fileNoteId and offer_letter_id =:offerLetterId", nativeQuery = true)
    void updateINoteNo(@Param("inoteNo") String inoteNo, @Param("fileNoteId") int fileNoteId, @Param("offerLetterId") int offerLetterId);

    @Query(value = "SELECT fnom.file_note_id, " +
            "       STRING_AGG(TO_CHAR(ol.id, 'FM999999999'), ', ') AS ol_ids," +
            "       STRING_AGG(ol.ol_number, ', ') AS ol_numbers," +
            "       ol.consignee_id," +
            "       c.name as consignee_name," +
            "       fnom.inote_no " +
            "FROM   tdms.file_note_ol_mapping fnom " +
            "       JOIN tdms.offer_letter ol " +
            "         ON fnom.offer_letter_id = ol.id " +
            "       JOIN mdms.consignee c " +
            "         ON ol.consignee_id = c.id " +
            "WHERE  fnom.file_note_id = :fileNoteId " +
            "GROUP  BY fnom.file_note_id," +
            "          ol.consignee_id," +
            "          c.name," +
            "          fnom.inote_no", nativeQuery = true)
    List<Object[]> getINoteNoByFileNoteId(@Param("fileNoteId") int fileNoteId);

    @Query(value = "SELECT Count(DISTINCT(fnom.inote_no)) " +
            "FROM   tdms.file_note_ol_mapping fnom " +
            "JOIN   tdms.ol_approval_details oad " +
            "ON     fnom.file_note_id = oad.file_note_id " +
            "JOIN   mdms.employee e " +
            "ON     oad.approved_by = e.id " +
            "WHERE  inote_no IS NOT NULL and inote_no <> '' " +
            "AND    oad.created_datetime >= " +
            "       (SELECT " +
            "                     CASE " +
            "                            WHEN Extract(month FROM CURRENT_DATE) < 4 THEN date_trunc('year', CURRENT_DATE - interval '1 year') + interval '3 months' " +
            "                            ELSE date_trunc('year', CURRENT_DATE) + interval '3 months' " +
            "                     END)", nativeQuery = true)
    public int getInoteCountInCurrentFY(int approvalRole);
    
    @Query(value = " SELECT COUNT(*) FROM tdms.file_note_ol_mapping WHERE inote_no = :inoteNo "
    		+ " AND file_note_id IN (SELECT file_note_id FROM tdms.ol_approval_details "
    		+ " WHERE created_datetime >= "
    		+ " (SELECT CASE WHEN EXTRACT(MONTH FROM CURRENT_DATE) < 4 "
    		+ " THEN date_trunc('year', CURRENT_DATE - interval '1 year') + interval '3 months' ELSE date_trunc('year', CURRENT_DATE) + interval '3 months' END)) ", nativeQuery = true)
    int countExistingINoteNos(@Param("inoteNo") String inoteNo);
    
    
    @Query(value = " SELECT MAX(CAST(SUBSTRING(inote_no FROM 6 FOR 4) AS INTEGER)) AS max_number "
    		+ " FROM tdms.file_note_ol_mapping "
    		+ " WHERE inote_no IS NOT NULL "
    		+ " AND inote_no <> '' "
    		+ " AND SUBSTRING(inote_no FROM 6 FOR 4) ~ '^\\d{4}$'  "
    		+ " AND file_note_id IN ( "
    		+ " SELECT file_note_id "
    		+ " FROM tdms.ol_approval_details "
    		+ " WHERE created_datetime >= ( "
    		+ " SELECT CASE "
    		+ " WHEN EXTRACT(MONTH FROM CURRENT_DATE) < 4 "
    		+ " THEN date_trunc('year', CURRENT_DATE - interval '1 year') + interval '3 months' "
    		+ " ELSE date_trunc('year', CURRENT_DATE) + interval '3 months' "
    		+ " END "
    		+ " ) "
    		+ " ) " , nativeQuery = true)
    Integer checkMaxValue();
    
  


    @Query(value = "select inote_no from tdms.file_note_ol_mapping where offer_letter_id in (:offerLetterIds) limit 1", nativeQuery = true)
    String getINoteNoByOlIds(List<Integer> offerLetterIds);

    @Modifying
    @Transactional
    @Query(value = "update tdms.file_note_ol_mapping set inote_no =:inoteNo where file_note_id=:fileNoteId and offer_letter_id in (:offerLetterIds)", nativeQuery = true)
    void changeINoteNo(@Param("inoteNo") String inoteNo, @Param("fileNoteId") int fileNoteId, @Param("offerLetterIds") List<Integer> offerLetterIds);

    @Query(value = "SELECT CASE " +
            "         WHEN Count(CASE " +
            "                      WHEN inote_no <> '' THEN 1 " +
            "                    END) > 0 THEN true " +
            "         ELSE false " +
            "       END AS inote_no_exists " +
            "FROM   tdms.file_note_ol_mapping " +
            "WHERE  file_note_id = :fileNoteId", nativeQuery = true)
    boolean checkINoteNoStatusByFileNoteId(Integer fileNoteId);
}
