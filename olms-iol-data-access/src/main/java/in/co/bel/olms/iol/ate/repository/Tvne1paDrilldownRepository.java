package in.co.bel.olms.iol.ate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.ate.entity.Tvne1pa;

/**
 * ATE drilldown analytics queries.
 * <p>
 * All queries exclude WITHOUT_REGISTRATION_NUMBER (mdms.registration_no_type id = 3),
 * which generates placeholder serials like WRN0001 for backend-only testing.
 */
@Repository
public interface Tvne1paDrilldownRepository extends JpaRepository<Tvne1pa, Integer> {

	// Shared exclusion: registration_no type 3 OR serial starting with WRN
	// Joins resolve SO line item via serial / OL line item / sub-slip line item paths.

	@Query(value = "SELECT "
			+ "    COUNT(DISTINCT dev_key) AS total_devices, "
			+ "    COUNT(DISTINCT CASE WHEN has_failure = 0 THEN dev_key END) AS passed_devices, "
			+ "    COUNT(DISTINCT CASE WHEN has_failure > 0 THEN dev_key END) AS failed_devices "
			+ " FROM ( "
			+ "    SELECT "
			+ "        COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) AS dev_key, "
			+ "        SUM(CASE WHEN tvp.status ILIKE 'fail' OR tvp.status = '0' OR ir.status ILIKE 'fail' THEN 1 ELSE 0 END) AS has_failure "
			+ "    FROM iol.tvne1pa tvp "
			+ "    JOIN ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ "    LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ "    LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ "    LEFT JOIN tdms.line_item_serial_no lsn_sub ON lsn_sub.id = wd.line_item_serial_no_id "
			+ "    LEFT JOIN tdms.line_item_serial_no lsn_qa ON lsn_qa.id = ir.ol_ser_no_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ser ON soli_ser.id = COALESCE(lsn_sub.so_line_item_id, lsn_qa.so_line_item_id) "
			+ "    LEFT JOIN tdms.ol_line_item oli ON oli.id = ir.ol_line_item_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ol ON soli_ol.id = oli.so_line_item_id "
			+ "    LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ir.sub_slip_line_item_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ss ON soli_ss.id = ssli.so_line_item_id "
			+ "    WHERE ir.proudct_id = :productId "
			+ "      AND ir.submitted_date BETWEEN :fromDate AND :toDate "
			+ "      AND (:isQa IS NULL OR ir.is_qa = :isQa) "
			+ "      AND (tvp.active_status IS NULL OR tvp.active_status = true) "
			+ "      AND COALESCE(soli_ser.registration_no, soli_ol.registration_no, soli_ss.registration_no, -1) <> 3 "
			+ "      AND COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, '') NOT ILIKE 'WRN%' "
			+ "    GROUP BY COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) "
			+ " ) dev_summary", nativeQuery = true)
	List<Object[]> getOverallDeviceSummary(@Param("productId") Integer productId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("isQa") Boolean isQa);

	@Query(value = "SELECT "
			+ "    t_summary.test_id, "
			+ "    t_summary.test_desc AS test_name, "
			+ "    COUNT(DISTINCT dev_key) AS total_tested_devices, "
			+ "    COUNT(DISTINCT CASE WHEN test_failures > 0 THEN dev_key END) AS failed_devices_count, "
			+ "    COUNT(DISTINCT CASE WHEN test_failures = 0 THEN dev_key END) AS passed_devices_count "
			+ " FROM ( "
			+ "    SELECT "
			+ "        t.id AS test_id, "
			+ "        t.test_desc, "
			+ "        COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) AS dev_key, "
			+ "        SUM(CASE WHEN tvp.status ILIKE 'fail' OR tvp.status = '0' OR ir.status ILIKE 'fail' THEN 1 ELSE 0 END) AS test_failures "
			+ "    FROM iol.tvne1pa tvp "
			+ "    JOIN ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ "    JOIN ate.test t ON t.id = tvp.test_id "
			+ "    LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ "    LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ "    LEFT JOIN tdms.line_item_serial_no lsn_sub ON lsn_sub.id = wd.line_item_serial_no_id "
			+ "    LEFT JOIN tdms.line_item_serial_no lsn_qa ON lsn_qa.id = ir.ol_ser_no_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ser ON soli_ser.id = COALESCE(lsn_sub.so_line_item_id, lsn_qa.so_line_item_id) "
			+ "    LEFT JOIN tdms.ol_line_item oli ON oli.id = ir.ol_line_item_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ol ON soli_ol.id = oli.so_line_item_id "
			+ "    LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ir.sub_slip_line_item_id "
			+ "    LEFT JOIN tdms.so_line_item soli_ss ON soli_ss.id = ssli.so_line_item_id "
			+ "    WHERE ir.proudct_id = :productId "
			+ "      AND ir.submitted_date BETWEEN :fromDate AND :toDate "
			+ "      AND (:isQa IS NULL OR ir.is_qa = :isQa) "
			+ "      AND (tvp.active_status IS NULL OR tvp.active_status = true) "
			+ "      AND COALESCE(soli_ser.registration_no, soli_ol.registration_no, soli_ss.registration_no, -1) <> 3 "
			+ "      AND COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, '') NOT ILIKE 'WRN%' "
			+ "    GROUP BY t.id, t.test_desc, COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) "
			+ " ) t_summary "
			+ " GROUP BY t_summary.test_id, t_summary.test_desc "
			+ " ORDER BY failed_devices_count DESC, t_summary.test_desc ASC", nativeQuery = true)
	List<Object[]> getFailuresGroupedByTest(@Param("productId") Integer productId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("isQa") Boolean isQa);

	@Query(value = "SELECT DISTINCT "
			+ "    ir.id AS insp_round_id, "
			+ "    COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) AS serial_no, "
			+ "    ir.submitted_date, "
			+ "    ir.round_id, "
			+ "    ir.is_qa, "
			+ "    ir.status AS round_status, "
			+ "    e.ename AS submitted_by_name, "
			+ "    COUNT(tvp.id) AS failed_param_count "
			+ " FROM iol.tvne1pa tvp "
			+ " JOIN ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ " LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ " LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_sub ON lsn_sub.id = wd.line_item_serial_no_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_qa ON lsn_qa.id = ir.ol_ser_no_id "
			+ " LEFT JOIN tdms.so_line_item soli_ser ON soli_ser.id = COALESCE(lsn_sub.so_line_item_id, lsn_qa.so_line_item_id) "
			+ " LEFT JOIN tdms.ol_line_item oli ON oli.id = ir.ol_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ol ON soli_ol.id = oli.so_line_item_id "
			+ " LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ir.sub_slip_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ss ON soli_ss.id = ssli.so_line_item_id "
			+ " LEFT JOIN mdms.employee e ON e.id = ir.submitted_by "
			+ " WHERE ir.proudct_id = :productId "
			+ "   AND tvp.test_id = :testId "
			+ "   AND (tvp.status ILIKE 'fail' OR tvp.status = '0' OR ir.status ILIKE 'fail') "
			+ "   AND ir.submitted_date BETWEEN :fromDate AND :toDate "
			+ "   AND (:isQa IS NULL OR ir.is_qa = :isQa) "
			+ "   AND (tvp.active_status IS NULL OR tvp.active_status = true) "
			+ "   AND COALESCE(soli_ser.registration_no, soli_ol.registration_no, soli_ss.registration_no, -1) <> 3 "
			+ "   AND COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, '') NOT ILIKE 'WRN%' "
			+ " GROUP BY ir.id, COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)), ir.submitted_date, ir.round_id, ir.is_qa, ir.status, e.ename "
			+ " ORDER BY ir.submitted_date DESC, serial_no ASC", nativeQuery = true)
	List<Object[]> getFailedDevicesForTest(@Param("productId") Integer productId, @Param("testId") Integer testId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("isQa") Boolean isQa);

	@Query(value = "SELECT "
			+ "    tvp.id AS tvne1pa_id, "
			+ "    ir.id AS insp_round_id, "
			+ "    COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) AS serial_no, "
			+ "    ir.submitted_date, "
			+ "    ir.round_id, "
			+ "    ir.is_qa, "
			+ "    t.id AS test_id, "
			+ "    t.test_desc AS test_name, "
			+ "    sp.id AS spec_id, "
			+ "    sp.spec_val AS param_name, "
			+ "    sp.param_code, "
			+ "    sp.ty_clause_no, "
			+ "    sp.min AS min_limit, "
			+ "    sp.max AS max_limit, "
			+ "    u.units AS unit, "
			+ "    sp.obs_type_id, "
			+ "    tvp.obs_value, "
			+ "    tvp.status AS param_status, "
			+ "    tvp.remarks "
			+ " FROM iol.tvne1pa tvp "
			+ " JOIN ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ " JOIN ate.spec sp ON sp.id = tvp.spec_id "
			+ " JOIN ate.test t ON t.id = tvp.test_id "
			+ " LEFT JOIN ate.units u ON u.id = sp.unit "
			+ " LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ " LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_sub ON lsn_sub.id = wd.line_item_serial_no_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_qa ON lsn_qa.id = ir.ol_ser_no_id "
			+ " LEFT JOIN tdms.so_line_item soli_ser ON soli_ser.id = COALESCE(lsn_sub.so_line_item_id, lsn_qa.so_line_item_id) "
			+ " LEFT JOIN tdms.ol_line_item oli ON oli.id = ir.ol_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ol ON soli_ol.id = oli.so_line_item_id "
			+ " LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ir.sub_slip_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ss ON soli_ss.id = ssli.so_line_item_id "
			+ " WHERE ir.proudct_id = :productId "
			+ "   AND (:testId IS NULL OR tvp.test_id = :testId) "
			+ "   AND (:inspRoundId IS NULL OR ir.id = :inspRoundId) "
			+ "   AND (tvp.status ILIKE 'fail' OR tvp.status = '0') "
			+ "   AND ir.submitted_date BETWEEN :fromDate AND :toDate "
			+ "   AND (:isQa IS NULL OR ir.is_qa = :isQa) "
			+ "   AND (tvp.active_status IS NULL OR tvp.active_status = true) "
			+ "   AND COALESCE(soli_ser.registration_no, soli_ol.registration_no, soli_ss.registration_no, -1) <> 3 "
			+ "   AND COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, '') NOT ILIKE 'WRN%' "
			+ " ORDER BY ir.submitted_date DESC, serial_no ASC, sp.id ASC", nativeQuery = true)
	List<Object[]> getFailedParametersDetail(@Param("productId") Integer productId,
			@Param("testId") Integer testId, @Param("inspRoundId") Integer inspRoundId,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("isQa") Boolean isQa);

	@Query(value = "SELECT "
			+ "    COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, CAST(ir.dev_id AS VARCHAR)) AS serial_no, "
			+ "    tvp.obs_value, "
			+ "    tvp.status, "
			+ "    sp.obs_type_id, "
			+ "    ir.submitted_date, "
			+ "    ir.is_qa, "
			+ "    ir.round_id "
			+ " FROM iol.tvne1pa tvp "
			+ " JOIN ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ " JOIN ate.spec sp ON sp.id = tvp.spec_id "
			+ " LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ " LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_sub ON lsn_sub.id = wd.line_item_serial_no_id "
			+ " LEFT JOIN tdms.line_item_serial_no lsn_qa ON lsn_qa.id = ir.ol_ser_no_id "
			+ " LEFT JOIN tdms.so_line_item soli_ser ON soli_ser.id = COALESCE(lsn_sub.so_line_item_id, lsn_qa.so_line_item_id) "
			+ " LEFT JOIN tdms.ol_line_item oli ON oli.id = ir.ol_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ol ON soli_ol.id = oli.so_line_item_id "
			+ " LEFT JOIN tdms.sslip_line_item ssli ON ssli.id = ir.sub_slip_line_item_id "
			+ " LEFT JOIN tdms.so_line_item soli_ss ON soli_ss.id = ssli.so_line_item_id "
			+ " WHERE tvp.spec_id = :specId "
			+ "   AND (:productId IS NULL OR ir.proudct_id = :productId) "
			+ "   AND (:testId IS NULL OR tvp.test_id = :testId) "
			+ "   AND ir.submitted_date BETWEEN :fromDate AND :toDate "
			+ "   AND (:isQa IS NULL OR ir.is_qa = :isQa) "
			+ "   AND (tvp.active_status IS NULL OR tvp.active_status = true) "
			+ "   AND COALESCE(soli_ser.registration_no, soli_ol.registration_no, soli_ss.registration_no, -1) <> 3 "
			+ "   AND COALESCE(lsn_sub.serial_no, lsn_qa.serial_no, '') NOT ILIKE 'WRN%' "
			+ " ORDER BY ir.submitted_date ASC, tvp.id ASC", nativeQuery = true)
	List<Object[]> getSpecTrendGraphData(@Param("specId") Integer specId, @Param("productId") Integer productId,
			@Param("testId") Integer testId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("isQa") Boolean isQa);
}
