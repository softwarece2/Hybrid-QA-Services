package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.ate.entity.InspectionRound;

public interface InspectionRoundRepository extends JpaRepository<InspectionRound, Integer> {
		
	@Query(value = "SELECT * FROM ate.insp_round WHERE ol_ser_no_id = :devId AND proudct_id = :prodId AND ol_line_item_id = :olLineItem AND is_qa = true order by round_id asc ", nativeQuery = true)
	List<InspectionRound> findRoundIdIsQa(Integer devId, Integer prodId, Integer olLineItem);
	
	@Query(value = "SELECT * FROM ate.insp_round WHERE ol_ser_no_id = :devId AND proudct_id = :prodId AND ol_line_item_id = :olLineItem  and (is_qa IS NULL or is_qa = false) order by round_id asc", nativeQuery = true)
	List<InspectionRound> findRoundId(Integer devId, Integer prodId, Integer olLineItem);

	@Query(value = "SELECT * FROM ate.insp_round WHERE sslip_serial_no_id = :devId AND proudct_id = :prodId AND sub_slip_line_item_id = :ssLiId AND is_qa = true order by round_id asc ", nativeQuery = true)
	List<InspectionRound> findRoundIdIsQaSsLiId(Integer devId, Integer prodId, Integer ssLiId);
	
	@Query(value = "SELECT * FROM ate.insp_round WHERE sslip_serial_no_id = :devId AND proudct_id = :prodId AND sub_slip_line_item_id = :ssLiId  and (is_qa IS NULL or is_qa = false) order by round_id asc", nativeQuery = true)
	List<InspectionRound> findRoundIdSsLiId(Integer devId, Integer prodId, Integer ssLiId);

	@Query(value = "SELECT DISTINCT pm.id ,pm.product_id ,pm.test_id, t.test_desc , ir.round_id ,itm.submitted_by ,ir.submitted_date, e.ename, pm.seq "
			+ "FROM ate.product_mapping pm  "
			+ "Left join ate.product p  on pm.product_id = p.id   "
			+ "Left join ate.test t on t.id = pm.test_id  "
			+ "Left join ate.insp_round ir ON ir.proudct_id = p.id   "
			+ "left join iol.insp_test_mapping itm on itm. insp_id = ir.id "
			+ " Left join mdms.employee e ON e.id = itm.submitted_by "
			+ "where  ir.proudct_id = :prodId and ir.ol_ser_no_id = :devId and ir.ol_line_item_id = :olLineItem  and ir.round_id = :roundId and ir.is_qa IS NULL "
			+ "order by pm.seq asc", nativeQuery = true)
	List<Object[]> getAllTestListByPrdId(Integer devId, Integer prodId, Integer olLineItem ,Integer roundId);

	@Query(value = "SELECT DISTINCT pm.id ,pm.product_id ,pm.test_id, t.test_desc , ir.round_id ,itm.submitted_by ,ir.submitted_date, e.ename, pm.seq "
			+ "FROM ate.product_mapping pm  "
			+ "Left join ate.product p  on pm.product_id = p.id   "
			+ "Left join ate.test t on t.id = pm.test_id  "
			+ "Left join ate.insp_round ir ON ir.proudct_id = p.id   "
			+ "left join iol.insp_test_mapping itm on itm. insp_id = ir.id "
			+ " Left join mdms.employee e ON e.id = itm.submitted_by "
			+ "where  ir.proudct_id = :prodId and ir.ol_ser_no_id = :devId and ir.ol_line_item_id = :olLineItem  and ir.round_id = :roundId and ir.is_qa = true "
			+ "order by pm.seq asc", nativeQuery = true)
	List<Object[]> getAllTestListByPrdIdIsQa(Integer devId, Integer prodId, Integer olLineItem,Integer roundId);
	
	@Query(value = 
		    "SELECT " +
		    " pm.id, " +
		    " pm.product_id, " +
		    " pm.test_id, " +
		    " t.test_desc, " +
		    " ir.round_id, " +
		    " itm.submitted_by, " +
		    " itm.submitted_date, " +
		    " e.ename, " +
		    " pm.seq " +
		    "FROM ate.product_mapping pm " +
		    "LEFT JOIN ate.product p ON p.id = pm.product_id " +
		    "LEFT JOIN ate.test t ON t.id = pm.test_id " +
		    "LEFT JOIN ate.insp_round ir ON ir.proudct_id = pm.product_id " +
		    " AND ir.sslip_serial_no_id = :devId " +
		    " AND ir.sub_slip_line_item_id = :ssLiId " +
		    " AND ir.round_id = :roundId " +
		    " AND (ir.is_qa IS NULL OR ir.is_qa = false) " +
		    "LEFT JOIN iol.insp_test_mapping itm ON itm.insp_id = ir.id " +
		    " AND itm.test_id = pm.test_id " +
		    "LEFT JOIN mdms.employee e ON e.id = itm.submitted_by " +
		    "WHERE pm.product_id = :prodId " +
		    "ORDER BY pm.seq ASC",
		    nativeQuery = true)
		List<Object[]> getAllTestListByPrdIdSsLiId( @Param("devId") Integer devId, @Param("prodId") Integer prodId, @Param("ssLiId") Integer ssLiId, @Param("roundId") Integer roundId);

	@Query(value = "SELECT DISTINCT pm.id ,pm.product_id ,pm.test_id, t.test_desc , ir.round_id ,itm.submitted_by ,ir.submitted_date, e.ename, pm.seq "
			+ "FROM ate.product_mapping pm  "
			+ "Left join ate.product p  on pm.product_id = p.id   "
			+ "Left join ate.test t on t.id = pm.test_id  "
			+ "Left join ate.insp_round ir ON ir.proudct_id = p.id   "
			+ "left join iol.insp_test_mapping itm on itm. insp_id = ir.id "
			+ " Left join mdms.employee e ON e.id = itm.submitted_by "
			+ "where  ir.proudct_id = :prodId and ir.sslip_serial_no_id = :devId and ir.sub_slip_line_item_id = :ssLiId  and ir.round_id = :roundId and ir.is_qa = true "
			+ "order by pm.seq asc", nativeQuery = true)
	List<Object[]> getAllTestListByPrdIdIsQaSsLiId(Integer devId, Integer prodId, Integer ssLiId,Integer roundId);
	
	@Query(value = "WITH RankedTests AS ( " + "    SELECT " + "pm.id, " + "pm.product_id, "
			+ " pm.test_id, " + " t.test_desc, " + " ir.round_id, " + "itm.submitted_by, "
			+ " ir.submitted_date, " + "e.ename, " + "pm.seq, "
			+ "ROW_NUMBER() OVER (PARTITION BY pm.product_id ORDER BY pm.seq) AS rn "
			+ "FROM ate.product_mapping pm " + "LEFT JOIN ate.product p ON pm.product_id = p.id "
			+ "LEFT JOIN ate.test t ON t.id = pm.test_id "
			+ "LEFT JOIN ate.insp_round ir ON ir.proudct_id= p.id  "
			+ "LEFT JOIN iol.insp_test_mapping itm ON itm.insp_id = ir.id "
			+ "LEFT JOIN mdms.employee e ON e.id = itm.submitted_by WHERE ir.proudct_id = :prodId "
			+ "AND ir.ol_ser_no_id = :devId  AND ir.ol_line_item_id = :olLineItem "
			+ "AND ir.round_id = :roundId AND ir.is_qa = true ) SELECT  id, "
			+ "product_id, " + "test_id, " + "test_desc, " + "round_id, " + "submitted_by, "
			+ "submitted_date, " + "ename," + "seq " + "FROM RankedTests "
			+ "WHERE rn IN (1, (SELECT COUNT(*) FROM RankedTests WHERE product_id = :prodId)) "
			+ "ORDER BY seq ASC", nativeQuery = true)
	List<Object[]> getAllTestListByPrdIdIsFai(Integer devId, Integer prodId, Integer olLineItem, Integer roundId);

  @Query(value = "WITH OrderedTests AS (" +
      "  SELECT pm.test_id, " +
      "  ROW_NUMBER() OVER (ORDER BY pm.seq ASC) as rn, " +
      "  COUNT(*) OVER () as total_count " +
      "  FROM ate.product_mapping pm " +
      "  INNER JOIN ate.product p ON pm.product_id = p.id " +
      "  WHERE p.id = :prodId) " +
      " SELECT test_id FROM OrderedTests " +
      " WHERE rn = 1 OR rn = total_count", nativeQuery = true)
  List<Integer> getRequiredTestIdsByFai(@Param("prodId") Integer prodId);

	@Query(value = "select * from ate.insp_round where dev_id =:devId and ol_line_item_id =:olLineItem and proudct_id =:prodId and  is_qa IS NULL order by created_datetime desc limit 1 ", nativeQuery = true)
	InspectionRound getNextRoundId(Integer devId, Integer prodId, Integer olLineItem);

	@Query(value = "select * from ate.insp_round where dev_id =:devId and ol_line_item_id =:olLineItem and proudct_id =:prodId and is_qa = true order by created_datetime desc limit 1 ", nativeQuery = true)
	InspectionRound getNextRoundIdIsQa(Integer devId, Integer prodId, Integer olLineItem);

	@Query(value = "SELECT ir.proudct_id, im.test_id, st.status, em.ename, im.submitted_date, pm.seq "
			+ "FROM ate.insp_round ir "
			+ "left join iol.insp_test_mapping im on im.insp_id = ir.id "
			+ "left join mdms.ol_status st on st.id = im.test_status_id "
			+ "left join mdms.employee em on em.id = im.submitted_by "
			+ "left join ate.product_mapping pm on( pm.product_id = :prodId AND pm.test_id = im.test_id) "
			+ "where ir.proudct_id = :prodId AND "
			+ "ir.ol_ser_no_id = :devId AND "
			+ "ir.ol_line_item_id = :olLineItem AND "
			+ "ir.round_id = :roundId AND "
			+ "is_qa = true "
			+ " order by im.test_id"  , nativeQuery = true)
	List<Object[]> getAllSubmittedTestStatusByProdIdDevIdIsQa(Integer devId, Integer prodId, Integer olLineItem, Integer roundId);


	@Query(value = "SELECT ir.proudct_id, im.test_id, st.status, em.ename, im.submitted_date, pm.seq "
			+ "FROM ate.insp_round ir "
			+ "left join iol.insp_test_mapping im on im.insp_id = ir.id "
			+ "left join mdms.ol_status st on st.id = im.test_status_id "
			+ "left join mdms.employee em on em.id = im.submitted_by "
			+ "left join ate.product_mapping pm on( pm.product_id = :prodId AND pm.test_id = im.test_id) "
			+ "where ir.proudct_id = :prodId AND "
			+ "ir.sslip_serial_no_id = :devId AND "
			+ "ir.sub_slip_line_item_id = :olLineItem AND "
			+ "ir.round_id = :roundId AND "
			+ "(ir.is_qa IS NULL or ir.is_qa = false) "
			+ "order by im.test_id"  , nativeQuery = true)
	List<Object[]> getAllSubmittedTestStatusByProdIdDevId(Integer devId, Integer prodId, Integer olLineItem, Integer roundId);
	

	
	

	@Query(value = "SELECT * FROM ate.insp_round WHERE dev_id = :devId ", nativeQuery = true)
	List<InspectionRound> findInspectionRoundByDeviceId(Integer devId);
	
	@Query(value = "SELECT itm.test_id FROM ate.insp_round ir "
			+ " JOIN iol.insp_test_mapping itm ON itm.insp_id = ir.id "
			+ " WHERE ir.sslip_serial_no_id = :devId ORDER BY itm.test_id " , nativeQuery = true)
	List<Integer> findTestListByDevId(Integer devId);
	
	@Query(value = "SELECT itm.test_id FROM ate.insp_round ir "
			+ " LEFT JOIN iol.insp_test_mapping itm ON itm.insp_id = ir.id "
			+ " LEFT JOIN tdms.ol_line_item_serial_no olisn on olisn.serial_no_id = ir.ol_ser_no_id "
			+ "	LEFT JOIN tdms.line_item_serial_no lisn on lisn.id = olisn.serial_no_id "
			+ " WHERE ir.ol_ser_no_id = :olSerNoId ORDER BY itm.test_id " , nativeQuery = true)
	List<Integer> findTestListBySerialNoId(Integer olSerNoId);

	
	@Query(value = "SELECT itm.test_id FROM ate.insp_round ir "
			+ " LEFT JOIN iol.insp_test_mapping itm ON itm.insp_id = ir.id "
			+ " LEFT JOIN tdms.line_item_serial_no lisn on lisn.id = ir.ol_ser_no_id "
      + " LEFT JOIN tdms.ol_line_item_serial_no olisn on olisn.serial_no_id = lisn.id and olisn.ol_line_item_id = :olLiId "
      + " WHERE olisn.id = :devId AND itm.test_id IS NOT NULL AND ( "
			+ "      (olisn.is_sample IS NOT NULL AND olisn.is_sample = true)"
			+ "   OR (olisn.is_fai_sample IS NOT NULL AND olisn.is_fai_sample = true)"
			+ ") ORDER BY itm.test_id ", nativeQuery = true)
  List<Integer> findTestListByDevIdQa(Integer devId, Integer olLiId);

	
	@Modifying
	@Query(value = "DELETE FROM ate.insp_round i WHERE i.sslip_serial_no_id IN :serialNoIds ", nativeQuery = true)
	void deleteBySubSlipSlNoIdIn(List<Integer> serialNoIds);

}
