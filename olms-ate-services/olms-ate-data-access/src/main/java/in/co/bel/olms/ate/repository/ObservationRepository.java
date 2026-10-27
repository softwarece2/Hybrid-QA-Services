package in.co.bel.olms.ate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Observation;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Integer> {

	@Query(value = "SELECT * FROM ate.observation r WHERE r.dev_id = :devId order by id", nativeQuery = true)
	List<Observation> findRdsByDevId(@Param("devId") Integer devId);

	@Query(value = "SELECT * FROM ate.observation WHERE spec_id = :specId AND dev_id = :devId LIMIT 1", nativeQuery = true)
	Optional<Observation> findBySpecIdAndDevId(@Param("specId") int specId, @Param("devId") Integer devId);

	@Query(value = "SELECT * FROM ate.observation r WHERE  r.dev_id = :devId AND r.prod_id = 1 order by id", nativeQuery = true)
	List<Observation> getBoardArtilleryByDevId(@Param("devId") Integer devId);

	@Query(value = "SELECT MAX(o.id) AS id, o.dev_id, MAX(o.ol_line_item_id) AS ol_line_item_id, MAX(o.applicable_test_id) AS applicable_test_id, MAX(o.submitted_date) AS submitted_date ,os.serial_no "
			+ "FROM ate.observation o "
			+ "Left join tdms.ol_line_item_serial_no os on os.id = o.dev_id "
			+ "WHERE prod_id = :prodId GROUP BY dev_id ,os.serial_no ORDER BY dev_id ASC ", nativeQuery = true)
	List<Object[]> getRdsRecDashDet(@Param("prodId") int prodId);

	@Query(value = "SELECT * FROM ate.observation as obs WHERE  obs.ol_line_item_id =:olLineItemId AND obs.applicable_test_id =:testId AND obs.prod_id =:prodId", nativeQuery = true)
	List<Observation> findByApplicableTestAndOlLineItemId(@Param("testId") int testId,
			@Param("olLineItemId") int olLineItemId, @Param("prodId") int prodId);
	
	@Query(value = "SELECT ob.id ,ob.obs ,ob.remarks,ob.spec_id,ob.ol_line_item_id,ob.created_datetime,ob.submitted_date,ob.prod_id,ob.obs_value,ob.dev_id, lilr.register_no,ob.result "
			+ "			FROM ate.observation ob "
			+ "			Left join ate.spec sp on sp.id = ob.spec_id "
			+ "			Left join ate.product_mapping pm on pm.test_id =sp.test_id and pm.product_id = sp.prod_id "
			+ "			Left join tdms.line_item_lots_register lilr on lilr.id = ob.dev_id  "
			+ "WHERE  ob.dev_id =:devId AND ob.prod_id = :productId and pm.group_id = :loginId order by id ", nativeQuery = true)
	List<Object[]> getSepcDetByDevIdAndLogId(Integer devId, Integer productId, Integer loginId);

	
	@Query(value = "SELECT * FROM ate.observation r WHERE  page_no = :pageNo order by id", nativeQuery = true)
	List<Observation> getObsByPageNo(@Param("pageNo") Integer pageNo);


	@Query(value = "SELECT * FROM ate.observation r WHERE  page_no = :pageNo and fai_id=:faiId order by id", nativeQuery = true)
	List<Observation> getObsByFaiIdPageNo(Integer faiId, Integer pageNo);

}
