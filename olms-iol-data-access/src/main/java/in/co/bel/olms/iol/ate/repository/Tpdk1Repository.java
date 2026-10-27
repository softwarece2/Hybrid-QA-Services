// tpdk1
package in.co.bel.olms.iol.ate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.ate.entity.Tpdk1;

@Repository
public interface Tpdk1Repository extends JpaRepository<Tpdk1, Integer>{

	
	
	@Query(value = "select ir.id as insp_round_Id ,ir.dev_id , ir.insp_date,ir.submitted_by, ir.approved_by,ir.ol_line_item_id, ir.proudct_id, ir.submitted_date,ir.status as insp_round_status , "
			+ "			tp.id as tpdk1_id,tp.spec_id ,tp.obs_type_id,tp.remarks, tp.obs_value,tp.status as tpdk1_status ,s.test_id,e.ename,ir.is_qa "
			+ "			from ate.insp_round ir "
			+ "			left join iol.tpdk1 tp on tp.insp_round_id = ir.id "
			+ "			left join ate.spec s on s.id = tp.spec_id "
			+ "         left join mdms.employee e on e.id = ir.submitted_by "
			+ "			where ir.dev_id = :devId and ir.proudct_id = :prodId and tp.test_id = :testId and ir.round_id = :roundId  and (:isQa is null or ir.is_qa = :isQa) order by tp.id", nativeQuery = true)
	List<Object[]> getTpdk1ByDevId(@Param("devId") int devId,@Param("prodId") int prodId, @Param("testId") int testId, @Param("roundId") int roundId, @Param("isQa") Boolean isQa);

	@Query(value = "SELECT "
			+ "    ir.submitted_date, ir.submitted_by, "
			+ "    tp.status "
			+ " FROM iol.tpdk1 tp "
			+ " left join ate.insp_round ir ON ir.id = tp.insp_round_id "
			+ " WHERE ir.dev_id = :devId and ir.proudct_id = :prodId and tp.test_id = :testId", nativeQuery = true) 
	List<Object[]> getTestInfo(@Param("prodId") int prodId,@Param("devId") int devId, @Param("testId") int testId);

	@Query(value = " SELECT lisn.serial_no, cr.obs_value, cr.status " + "FROM iol.tpdk1 cr "
			+ "LEFT JOIN ate.insp_round ir ON ir.id = cr.insp_round_id "
			+ "LEFT JOIN ate.spec sp ON sp.id = cr.spec_id "
			+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ "LEFT JOIN tdms.warrant_details wd ON wd.id = sssn.warrant_details_id "
			+ "LEFT JOIN tdms.line_item_serial_no lisn ON lisn.id = wd.line_item_serial_no_id "
			+ "WHERE cr.spec_id = :specId AND ir.submitted_date BETWEEN :fromDate AND :toDate and ir.submitted_date is not null  "
			+ " ORDER BY cr.id", nativeQuery = true)
	List<Object[]> getGraphData(@Param("specId") int specId, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate);

	@Query(value = ""
		    + "SELECT tp.insp_round_id, "
		    + "       ir.round_id, "
		    + "       ir.proudct_id, "
		    + "       tp.test_id, "
		    + "       ts.test_desc, "
		    + "       tp.spec_id, "
		    + "       sp.spec_val, "
		    + "       sp.min, "
		    + "       sp.max, "
		    + "       sp.obs_value, "
		    + "       sp.obs_type_id, "
		    + "       tp.obs_value AS observation, "
		    + "       tp.status, "
		    + "       sp.param_code, "
		    + "       sp.param_type as param_type_id, "
		    + "       sp.ty_clause_no, "
		    + "       pt.param_type, "
		    + "       u.units, "
		    + "       em.ename "
		    + "FROM iol.tpdk1 tp "
		    + "LEFT JOIN ate.test ts ON tp.test_id = ts.id "
		    + "LEFT JOIN ate.insp_round ir ON tp.insp_round_id = ir.id "
		    + "LEFT JOIN ate.spec sp ON tp.spec_id = sp.id "
		    + "LEFT JOIN ate.param_type pt on sp.param_type = pt.id "
		    + "LEFT JOIN ate.units u on sp.unit = u.id "
		    + "LEFT JOIN mdms.employee em on ir.submitted_by = em.id "
		    + "WHERE ir.proudct_id = :prodId "
		    + "  AND ir.ol_ser_no_id = :devId "
		    + "  AND ir.round_id = :roundId "
		    + "  AND tp.active_status = true"
		    + "  AND ir.is_qa = true "
		    + "ORDER BY tp.test_id " ,
		    nativeQuery = true)
		List<Object[]> getAllTestListByPrdIdIsQa(@Param("devId") Integer devId, @Param("prodId") Integer prodId, @Param("roundId") Integer roundId);

		@Query(value = ""
			    + "SELECT tp.insp_round_id, "
			    + "       ir.round_id, "
			    + "       ir.proudct_id, "
			    + "       tp.test_id, "
			    + "       ts.test_desc, "
			    + "       tp.spec_id, "
			    + "       sp.spec_val, "
			    + "       sp.min, "
			    + "       sp.max, "
			    + "       sp.obs_value, "
			    + "       sp.obs_type_id, "
			    + "       tp.obs_value AS observation, "
			    + "       tp.status, "
			    + "       sp.param_code, "
			    + "       sp.param_type as param_type_id, "
			    + "       sp.ty_clause_no, "
			    + "       pt.param_type, "
			    + "       u.units, "
			    + "       em.ename "
			    + "FROM iol.tpdk1 tp "
			    + "LEFT JOIN ate.test ts ON tp.test_id = ts.id "
			    + "LEFT JOIN ate.insp_round ir ON tp.insp_round_id = ir.id "
			    + "LEFT JOIN ate.spec sp ON tp.spec_id = sp.id "
			    + "LEFT JOIN ate.param_type pt on sp.param_type = pt.id "
			    + "LEFT JOIN ate.units u on sp.unit = u.id "
			    + "LEFT JOIN mdms.employee em on ir.submitted_by = em.id "
			    + "WHERE ir.proudct_id = :prodId "
			    + "  AND ir.sslip_serial_no_id = :devId "
			    + "  AND ir.round_id = :roundId "
			    + "  AND ir.is_qa IS false "
			    + " AND tp.active_status = true "
			    + " ORDER BY tvp.test_id " ,
			    nativeQuery = true)
			List<Object[]> getAllTestListByPrdId(@Param("devId") Integer devId, @Param("prodId") Integer prodId, @Param("roundId") Integer roundId);

			
			@Query(value = "select * from  iol.tpdk1 tp  "
					+ "			left join ate.insp_round ir on tp.insp_round_id = ir.id "
					+ "			left join ate.spec s on s.id = tp.spec_id "
					+ "			left join mdms.employee e on e.id = ir.submitted_by "
					+ "			where ir.sslip_serial_no_id = :devId and ir.proudct_id = :prodId and tp.test_id = :testId  and ir.round_id = :roundId  and (:isQa is null or ir.is_qa = :isQa) order by tp.id ", nativeQuery = true)
			List<Tpdk1> getByDevIdProdTestRndIsQa(@Param("devId") int devId, @Param("prodId") int prodId,
					@Param("testId") int testId, @Param("roundId") int roundId, @Param("isQa") Boolean isQa);

			@Query(value = "select * from  iol.tpdk1 tp  "
					+ "			left join ate.insp_round ir on tp.insp_round_id = ir.id "
					+ "			left join ate.spec s on s.id = tp.spec_id "
					+ "			left join mdms.employee e on e.id = ir.submitted_by "
					+ "			where ir.ol_ser_no_id = :devId and ir.proudct_id = :prodId and tp.test_id = :testId  and ir.round_id = :roundId  and  ir.is_qa = :isQa  order by tp.id ", nativeQuery = true)
			List<Tpdk1> getByDevIdProdTestRndIsQa1(@Param("devId") int devId, @Param("prodId") int prodId,
					@Param("testId") int testId, @Param("roundId") int roundId, @Param("isQa") Boolean isQa);

}
