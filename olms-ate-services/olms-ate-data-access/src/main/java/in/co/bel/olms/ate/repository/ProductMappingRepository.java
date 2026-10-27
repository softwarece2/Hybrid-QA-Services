package in.co.bel.olms.ate.repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.ProductMapping;

@Repository
public interface ProductMappingRepository extends JpaRepository<ProductMapping, Integer> {

	@Query(value = "SELECT DISTINCT pm.product_id ,pm.group_id ,p.prod_name "
			+ "FROM ate.product_mapping pm "
			+ "Left join ate.product p  on pm.product_id = p.id "
			+ "where  pm.product_id =:prodId order By pm.group_id ", nativeQuery = true)
	List<Object[]> getProductTestDet(@Param("prodId") int prodId);

	
	@Query(value = "SELECT DISTINCT pm.id ,pm.product_id ,pm.test_id, t.test_desc, pm.seq "
			+ "	FROM ate.product_mapping pm"
			+ " Left join ate.product p  on pm.product_id = p.id "
			+ "	Left join ate.test t on t.id = pm.test_id"
			+ "	where  pm.product_id =:prodId "
			+ " order by pm.seq asc ", nativeQuery = true)
	List<Object[]> getTestsByProdId(@Param("prodId") int prodId);
	
	@Query(value = "SELECT DISTINCT pm.id ,t.test_desc"
			+ "	FROM ate.product_mapping pm"
			+ " Left join ate.product p  on pm.product_id = p.id "
			+ "	Left join ate.test t on t.id = pm.test_id"
			+ "	where  pm.product_id =:prodId  ", nativeQuery = true)
	List<Object[]> getTestListByProdId(@Param("prodId") int prodId);


	
	@Query(value = "SELECT pm.test_id "
			+ "	FROM ate.product_mapping pm"
			+ "	where  pm.product_id =:prodId ORDER BY pm.test_id ", nativeQuery = true)
	List<Integer> getTestIdsByProdId(@Param("prodId") int prodId);
	
	@Query(value = "SELECT t.id, t.test_desc, "
	        + " COUNT(CASE WHEN itm.test_status_id = 11 THEN 1 END) AS passed_count, "
	        + " COUNT(CASE WHEN itm.test_status_id = 12 THEN 1 END) AS failed_count, "
	        + " COUNT(itm.test_id) AS total_count " 
	        + " FROM ate.test t "
	        + " JOIN ate.product_mapping pm ON pm.test_id = t.id "
	        + " LEFT JOIN iol.insp_test_mapping itm ON itm.test_id = t.id "
	        + "   AND itm.submitted_date >= :from "
	        + "   AND itm.submitted_date < (cast(:to AS date) + 1) "
	        + " WHERE pm.product_id = :prodId "
	        + " GROUP BY t.test_desc, t.id", nativeQuery = true)
	List<Object[]> getStatsByProdId(@Param("prodId") int prodId, @Param("from") Date from, @Param("to") Date to);

	Optional<ProductMapping> findByProductIdAndTestId(Integer refProdId, Integer testId);


	boolean existsByProductId(Integer prodId);

}
