package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer>{

	@Query(value = "select * from ofd_mdms.test where prod_Id =:prodId order by id ASC", nativeQuery = true)
	List<Test> getTestsByProdID(@Param("prodId") int prodId);
	
	@Query(value="Select * from ate.test order by id asc", nativeQuery = true)
	List<Test> findAll();

	@Query(value = "select distinct(t.*) from ate.test t "
		+ " left join ate.product_mapping pm on pm.test_id = t.id "
		+ " where pm.product_id = :prodId order by t.id asc", nativeQuery = true)
	List<Test> findAllTestByProdId(@Param("prodId") int prodId); 
	
}
