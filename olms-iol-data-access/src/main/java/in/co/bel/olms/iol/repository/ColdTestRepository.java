package in.co.bel.olms.iol.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.entity.ColdTest;
@Repository
public interface ColdTestRepository extends JpaRepository<ColdTest, Integer> {

	@Query(value = "SELECT * FROM iol.cold_test WHERE applicable_test_id = :testId AND ol_line_item_id = :olLineItemId", nativeQuery = true)
	List<ColdTest> findByApplicableTestAndOlLineItemId(@Param("testId") int testId,
			@Param("olLineItemId") int olLineItemId);

	
	@Query(value = "SELECT * FROM iol.cold_test WHERE insp_round_id = :inspRoundId", nativeQuery = true)
	List<ColdTest> findByInspRoundId(@Param("inspRoundId") int inspRoundId);

	@Query(value = "SELECT * FROM iol.cold_test WHERE insp_round_id = :inspRoundId and  hot_cold = :hotCold ", nativeQuery = true)
	ColdTest findByInspRndHotCold(@Param("inspRoundId") int inspRoundId, @Param("hotCold") int hotCold);

	
	@Query(value = "SELECT * FROM iol.cold_test WHERE id = :Id", nativeQuery = true)
	ColdTest findByApplicableTestId(@Param("Id") int Id);

	@Query(value = "SELECT * FROM iol.cold_test ct 	left join tdms.ol_line_item oli on oli.id = ct.ol_line_item_id "
			+ "	left join tdms.offer_letter ol on ol.id= oli.offer_letter_id 	where ol.id=:olId", nativeQuery = true)
	List<ColdTest> findByOfferLetterId(@Param("olId") int olId);

	@Query(value = "SELECT ct.device_no AS label, ct.volt AS value FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findAllVoltDev(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT ct.device_no AS label, ct.ph_curr_r AS value FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findPhCurrR(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT ct.device_no AS label, ct.ph_curr_s AS value FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findPhCurrS(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT ct.device_no AS label, ct.ph_curr_s AS value FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findPhCurrT(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT ct.device_no AS label, ct.mech_sp_9v_forw AS mechSp9vFor, ct.mech_sp_9v_backw AS mechSp9vBack FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findMechSp9v(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT ct.device_no AS label, ct.mech_sp_14v_forw AS mechSp14vFor, ct.mech_sp_14v_backw AS mechSp14vBack FROM iol.cold_test AS ct LEFT JOIN iol.product_type AS pt ON ct.product_type_id = pt.id WHERE ct.product_type_id = :productTypeId AND ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.id ASC", nativeQuery = true)
	List<Object[]> findMechSp14v(@Param("productTypeId") int prodductTypeId, @Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT id as dev_id, device_no as dev_no, status as st, remarks as rem, test_date as te_dt , test_type from iol.cold_test "
			+ " WHERE applicable_test_id = :testId AND ol_line_item_id = :olLineItemId ORDER BY id ASC", nativeQuery = true)
	List<Object[]> getDevDet(@Param("testId") int testId, @Param("olLineItemId") int olLineItemId);

	@Query(value = "SELECT * from iol.cold_test where hot_cold = :coldTestTypeId", nativeQuery = true)
	List<ColdTest> getDeviceByHotOrColdId(@Param("coldTestTypeId") int coldTestTypeId);

	@Query(value = "SELECT *  FROM iol.cold_test ORDER BY created_datetime DESC LIMIT 1", nativeQuery = true)
	ColdTest getColdTestByCreatedDate();

	@Query(value = "SELECT ct.id AS dev_id, ct.device_no AS dev_no, ct.status AS st, ct.remarks AS rem, ct.test_date AS te_dt, ct.test_type, sc.accepted_count, sc.rejected_count, CASE WHEN sc.total_count = 0 THEN 0 ELSE ROUND((sc.accepted_count * 100.0 / sc.total_count), 2) END AS success_rate FROM iol.cold_test ct LEFT JOIN (SELECT COUNT(*) AS total_count, COUNT(*) FILTER (WHERE status = 1) AS accepted_count, COUNT(*) FILTER (WHERE status = 0) AS rejected_count FROM iol.cold_test) sc ON true LEFT JOIN iol.hot_cold_test_type AS ht ON ct.hot_cold = ht.id WHERE ct.hot_cold = :testTypeId AND ct.submitted_date BETWEEN :fromDate AND :toDate ORDER BY ct.submitted_date ASC", nativeQuery = true)
	List<Object[]> getAllColdTestsDevDetailsByCreatedDate(@Param("testTypeId") int testTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query(value = "SELECT \r\n"
			+ "            SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS accepted_count,\r\n"
			+ "            SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS rejected_count\r\n"
			+ "        FROM \r\n"
			+ "            iol.cold_test", nativeQuery = true)
	Object getTheAcceptOrRejectCount();

	@Query(value = "SELECT tv.dev_name AS label, lt.leak_test AS value FROM iol.tvne AS tv LEFT JOIN iol.leak_test AS lt ON tv.id = lt.tvne_id LEFT JOIN iol.product_type AS pt ON tv.product_type_id = pt.id WHERE tv.product_type_id = :productTypeId AND lt.submitted_date BETWEEN :fromDate AND :toDate ORDER BY tv.id ASC", nativeQuery = true)
	List<Object[]> findAllLeakTest(@Param("productTypeId") int productTypeId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query(value = "select * from iol.cold_test where device_no = :devId limit 1", nativeQuery = true)
	ColdTest getColdTestByDevId(@Param("devId") String devId);

	

	@Query(value = "SELECT DISTINCT llr.register_no, ct.volt FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductPhDetVolt(Date fromDate, Date toDate);
	
	@Query(value = "SELECT DISTINCT llr.register_no, ct.ph_curr_s FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductPhCurrS(Date fromDate, Date toDate);
	
	@Query(value = "SELECT DISTINCT llr.register_no, ct.ph_curr_r FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductPhCurrR(Date fromDate, Date toDate);
	
	
	@Query(value = "SELECT DISTINCT llr.register_no, ct.ph_curr_t FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductPhCurrT(Date fromDate, Date toDate);
	
	@Query(value = "SELECT DISTINCT llr.register_no, ct.mech_sp_9v_forw ,ct.mech_sp_9v_backw FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductMechSp9v(Date fromDate, Date toDate);
	
	@Query(value = "SELECT DISTINCT llr.register_no, ct.mech_sp_14v_forw ,ct.mech_sp_14v_backw FROM iol.cold_test ct LEFT JOIN ate.insp_round ir ON ir.id = ct.insp_round_id "
			+ "LEFT JOIN tdms.line_item_lots_register llr ON llr.id = ir.dev_id "
			+ "AND ct.submitted_date BETWEEN :fromDate AND :toDate ", nativeQuery = true)
	List<Object[]> findAteProductMechSp14v(Date fromDate, Date toDate);
}
