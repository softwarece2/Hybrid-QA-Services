package in.co.bel.olms.iol.ate.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.ate.entity.Tvne1pa;


@Repository
public interface Tvne1paRepository extends JpaRepository<Tvne1pa, Integer>{

	
	
	@Query(value = "select * from  iol.tvne1pa tvp  " + "  left join ate.insp_round ir on tvp.insp_round_id = ir.id "
			+ "  left join ate.spec s on s.id = tvp.spec_id " + "  left join mdms.employee e on e.id = ir.submitted_by "
			+ "  where ir.sslip_serial_no_id = :devId and ir.proudct_id = :prodId and tvp.test_id = :testId  and ir.round_id = :roundId  and (:isQa is null or ir.is_qa = :isQa) order by tvp.id ", nativeQuery = true)
	List<Tvne1pa> getByDevIdProdTestRndIsQa(@Param("devId") int devId, @Param("prodId") int prodId,
			@Param("testId") int testId, @Param("roundId") int roundId, @Param("isQa") Boolean isQa);

	@Query(value = "select * from  iol.tvne1pa tvp  " + "  left join ate.insp_round ir on tvp.insp_round_id = ir.id "
			+ "  left join ate.spec s on s.id = tvp.spec_id " + "  left join mdms.employee e on e.id = ir.submitted_by "
      + "  where ir.ol_ser_no_id = :devId and ir.proudct_id = :prodId and tvp.test_id = :testId  and ir.round_id = :roundId  and  ir.is_qa = :isQa  and ir.ol_line_item_id = :olLiId order by tvp.id ", nativeQuery = true)
	List<Tvne1pa> getByDevIdProdTestRndIsQa1(@Param("devId") int devId, @Param("prodId") int prodId,
                                           @Param("testId") int testId, @Param("roundId") int roundId, @Param("isQa") Boolean isQa, @Param("olLiId") Integer olLiId);

	
	@Query(value = "SELECT "
			+ "    lsn.serial_no, "
			+ "    tvp.obs_value, "
			+ "    tvp.status, "
			+ "    sp.obs_type_id"
			+ " FROM iol.tvne1pa tvp "
			+ " left join ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ " left join ate.spec sp ON sp.id = tvp.spec_id "
			+ " left join tdms.sub_slip_serial_no sssn ON sssn.id = ir.sslip_serial_no_id "
			+ " left join tdms.warrant_details wd ON wd.id = sssn.warrant_details_id  "
			+ " left join tdms.line_item_serial_no lsn ON lsn.id = wd.line_item_serial_no_id  "
			+ " WHERE tvp.spec_id = :specId AND ir.submitted_date BETWEEN :fromDate AND :toDate and ir.submitted_date is not null "
			+ " ORDER BY tvp.id", nativeQuery = true)
	List<Object[]> getGraphData(@Param("specId") int specId ,@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query(value = "SELECT "
			+ "    ir.submitted_date, ir.submitted_by, "
			+ "    tvp.status "
			+ " FROM iol.tvne1pa tvp "
			+ " left join ate.insp_round ir ON ir.id = tvp.insp_round_id "
			+ " WHERE ir.dev_id = :devId and ir.proudct_id = :prodId and tvp.test_id = :testId", nativeQuery = true) 
	List<Object[]> getTestInfo(@Param("prodId") int prodId,@Param("devId") int devId, @Param("testId") int testId);

	@Query(value = ""
		    + "SELECT tvp.insp_round_id, "
		    + "       ir.round_id, "
		    + "       ir.proudct_id, "
		    + "       tvp.test_id, "
		    + "       ts.test_desc, "
		    + "       tvp.spec_id, "
		    + "       sp.spec_val, "
		    + "       sp.min, "
		    + "       sp.max, "
		    + "       sp.obs_value, "
		    + "       sp.obs_type_id, "
		    + "       tvp.obs_value AS observation, "
		    + "       tvp.status, "
		    + "       sp.param_code, "
		    + "       sp.param_type as param_type_id, "
		    + "       sp.ty_clause_no, "
		    + "       pt.param_type, "
		    + "       u.units, "
		    + "       em.ename "
		    + "FROM iol.tvne1pa tvp "
		    + "LEFT JOIN ate.test ts ON tvp.test_id = ts.id "
		    + "LEFT JOIN ate.insp_round ir ON tvp.insp_round_id = ir.id "
		    + "LEFT JOIN ate.spec sp ON tvp.spec_id = sp.id "
		    + "LEFT JOIN ate.param_type pt on sp.param_type = pt.id "
		    + "LEFT JOIN ate.units u on sp.unit = u.id "
		    + "LEFT JOIN mdms.employee em on ir.submitted_by = em.id "
		    + "WHERE ir.proudct_id = :prodId "
		    + "  AND ir.ol_ser_no_id = :devId "
		    + "  AND ir.round_id = :roundId "
		    + "  AND ir.is_qa = true "
		    + "  AND tvp.active_status = true "
		    + "ORDER BY tvp.test_id " ,
		    nativeQuery = true)
		List<Object[]> getAllTestListByPrdIdIsQa(@Param("devId") Integer devId, @Param("prodId") Integer prodId, @Param("roundId") Integer roundId);

		@Query(value = ""
			    + "SELECT tvp.insp_round_id, "
			    + "       ir.round_id, "
			    + "       ir.proudct_id, "
			    + "       tvp.test_id, "
			    + "       ts.test_desc, "
			    + "       tvp.spec_id, "
			    + "       sp.spec_val, "
			    + "       sp.min, "
			    + "       sp.max, "
			    + "       sp.obs_value, "
			    + "       sp.obs_type_id, "
			    + "       tvp.obs_value AS observation, "
			    + "       tvp.status, "
			    + "       sp.param_code, "
			    + "       sp.param_type as param_type_id, "
			    + "       sp.ty_clause_no, "
			    + "       pt.param_type, "
			    + "       u.units, "
			    + "       em.ename "
			    + "FROM iol.tvne1pa tvp "
			    + "LEFT JOIN ate.test ts ON tvp.test_id = ts.id "
			    + "LEFT JOIN ate.insp_round ir ON tvp.insp_round_id = ir.id "
			    + "LEFT JOIN ate.spec sp ON tvp.spec_id = sp.id "
			    + "LEFT JOIN ate.param_type pt on sp.param_type = pt.id "
			    + "LEFT JOIN ate.units u on sp.unit = u.id "
			    + "LEFT JOIN mdms.employee em on ir.submitted_by = em.id "
			    + "WHERE ir.proudct_id = :prodId "
			    + "  AND ir.sslip_serial_no_id = :devId "
			    + "  AND ir.round_id = :roundId "
			    + "  AND ir.is_qa IS false "
			    + " AND tvp.active_status = true "
			    + " ORDER BY tvp.test_id " ,
			    nativeQuery = true)
			List<Object[]> getAllTestListByPrdId(@Param("devId") Integer devId, @Param("prodId") Integer prodId, @Param("roundId") Integer roundId);


	

}
