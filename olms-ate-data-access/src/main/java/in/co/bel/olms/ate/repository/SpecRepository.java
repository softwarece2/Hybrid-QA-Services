package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Spec;

@Repository
public interface SpecRepository extends JpaRepository<Spec, Integer>{
	
	@Query(value = "select * from ate.spec where prod_id =:prodId order by id ASC", nativeQuery = true)
	List<Spec> getSpecByProdID(@Param("prodId") int prodId);

	@Query(value = "SELECT  DISTINCT s.id as spec_id, s.spec_val, s.min,s.max,s.prod_id, s.test_id,pm.group_id,t.test_desc "
			+ "FROM ate.spec s "
			+ "Left JOIN ate.product_mapping pm ON  s.prod_id =pm.product_id and s.test_id = pm.test_id "
			+ "Left join ate.test t on t.id = s.test_id "
			+ "WHERE  s.prod_id = :prodId  "
			+ "ORDER BY s.id  ASC ", nativeQuery = true)	
	List<Object[]> getSpecsByProdIdGpId(@Param("prodId") int prodId);

	
	@Query(value = "select * from ate.spec where prod_id = :prodId and test_id = :testId order by id ASC ", nativeQuery = true)	
	List<Spec> getAllSpecsByProdIdTestId(@Param("prodId") int prodId ,@Param("testId") int testId);
	
	@Query(value = "select * from ate.spec where prod_id = :prodId order by id ASC ", nativeQuery = true)	
	List<Spec> getAllSpecsByProdId(@Param("prodId") int prodId );
	
	@Query(value = "select * from ate.spec where prod_id = :prodId and test_id = :testId and active = true order by id ASC ", nativeQuery = true)	
	List<Spec> getActiveSpecsByProdIdTestId(@Param("prodId") int prodId ,@Param("testId") int testId);
	
	@Query(value = "SELECT s.* FROM ate.spec s "
			+ " INNER JOIN ( "
			+ "    SELECT spec_id, MAX(created_at) AS latest_created_at "
			+ "    FROM ate.spec_status GROUP BY spec_id "
			+ " ) latest_status ON s.id = latest_status.spec_id "
			+ " INNER JOIN ate.spec_status ss ON s.id = ss.spec_id AND ss.created_at = latest_status.latest_created_at "
			+ " WHERE s.prod_id = :prodId AND s.test_id = :testId AND ss.active = :active ", nativeQuery = true)
		List<Spec> getAllActiveSpecsByProdIdTestId(@Param("prodId") int prodId, @Param("testId") int testId, @Param("active") Boolean active);
	
	@Query(value = "SELECT s.* FROM ate.spec s "
			+ " INNER JOIN ( "
			+ "    SELECT spec_id, MAX(created_at) AS latest_created_at "
			+ "    FROM ate.spec_status GROUP BY spec_id "
			+ " ) latest_status ON s.id = latest_status.spec_id "
			+ " INNER JOIN ate.spec_status ss ON s.id = ss.spec_id AND ss.created_at = latest_status.latest_created_at "
			+ " WHERE s.prod_id = :prodId  AND ss.active = :active ", nativeQuery = true)
		List<Spec> getAllActiveSpecsByProdId(@Param("prodId") int prodId, @Param("active") Boolean active);
	
	@Query(value = "SELECT DISTINCT sp.id, "
	        + "sp.spec_val as title, "
	        + "u.units as unit, "
	        + "sp.min as min_val, "
	        + "sp.max as max_val, "
	        + "sp.obs_type_id "
	        + "FROM ate.spec sp "
	        + "LEFT JOIN ate.units u ON sp.unit = u.id "
	        + "LEFT JOIN ate.obs_type AS o ON sp.obs_type_id = o.id "
	        + "WHERE sp.prod_id = :prodId "
	        + "AND sp.test_id = :testId "
//	        + "AND sp.obs_type_id = 2 "
	        + "ORDER BY sp.id ASC " , nativeQuery = true)
		List<Object[]> getAllSpecsFrGrByProdIdTestId(@Param("prodId") int prodId ,@Param("testId") int testId);

	@Query(value = "SELECT COUNT(*) "
			+ "FROM ate.spec sp "
			+ "LEFT JOIN ate.units u ON sp.unit = u.id "
			+ "LEFT JOIN ate.obs_type AS o ON sp.obs_type_id = o.id "
			+ "WHERE sp.prod_id = :productId "
			+ "AND sp.test_id = :testId " , nativeQuery = true)
//			+ "AND sp.obs_type_id = 2;"
	Object getGraphCountByProdIdTestId(@Param("productId")int productId, @Param("testId")int testId);

	List<Spec> findByProductIdAndTestIdOrderBySlNo(Integer prodId, Integer testId);

	List<Spec> findByProductIdAndTestId(Integer prodId, Integer testId);


  @Query(value = "SELECT STRING_AGG(lisn.serial_no || ' - ' || s.spec_val || ': ' || t.obs_value , ', ') FROM ate.spec s \r\n"
			+ "JOIN iol.tvne1pa t ON s.id = t.spec_id \r\n"
			+ "JOIN ate.insp_round ir ON t.insp_round_id = ir.id \r\n"
			+ " JOIN tdms.line_item_serial_no lisn on lisn.id = ir.ol_ser_no_id "
			+ "WHERE t.status = 'Fail' AND ir.ol_line_item_id = :olLineItemId AND ir.status = 'fail' \r\n" , nativeQuery = true)
	String getFailedSpecForInote(Integer olLineItemId);
  
  @Query(value = "SELECT STRING_AGG(serial_data, E'\n') " +
	        "FROM ( " +
	        "    SELECT lisn.serial_no || ' - ' || " +
	        "           STRING_AGG(s.spec_val || ': ' || t.obs_value, ', ' ORDER BY s.spec_val) AS serial_data " +
	        "    FROM ate.spec s " +
	        "    JOIN iol.tvne1pa t ON s.id = t.spec_id " +
	        "    JOIN ate.insp_round ir ON t.insp_round_id = ir.id " +
	        "    JOIN tdms.sub_slip_serial_no ssn ON ssn.id = ir.sslip_serial_no_id " +
	        "    JOIN tdms.warrant_details wd ON wd.id = ssn.warrant_details_id " +
	        "    JOIN tdms.line_item_serial_no lisn ON lisn.id = wd.line_item_serial_no_id " +
	        "    WHERE t.status = 'Fail' " +
	        "      AND ir.sub_slip_line_item_id = :subSlipLineItemId " +
	        "      AND ir.status = 'fail' " +
	        "    GROUP BY lisn.serial_no " +
	        ") AS failed",
	        nativeQuery = true)
	String getFailedSpecForInternalInote(@Param("subSlipLineItemId") Integer subSlipLineItemId);
	
}
