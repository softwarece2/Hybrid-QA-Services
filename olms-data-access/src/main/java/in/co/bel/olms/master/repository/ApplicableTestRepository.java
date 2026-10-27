package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.ApplicableTest;

/**
* Repository interface for ApplicableTest entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-10-18
*/

public interface ApplicableTestRepository extends JpaRepository<ApplicableTest, Integer>{
	
	@Query(value = "SELECT * from mdms.applicable_test ORDER BY test_name ASC", nativeQuery = true)
	public List<ApplicableTest> findAll();
	
	@Query(value = "SELECT * from mdms.applicable_test WHERE active = true", nativeQuery = true)
	public List<ApplicableTest> findAllActiveAppTest();
	
	Boolean existsBytestName(String name);
	
	List<ApplicableTest> findBySalesGroupId(int salesGroupId);
	
	@Query(value="SELECT * from mdms.applicable_test WHERE active = true and sales_group_id = :salesGroupId", nativeQuery = true)
	public List<ApplicableTest> findAllActiveAppTestBySalesGrpId(int salesGroupId);
	
	@Query(value="SELECT *\r\n"
			+ "FROM mdms.applicable_test\r\n"
			+ "WHERE id NOT IN (SELECT applicable_test_id FROM tdms.line_item_applicable_test where ol_line_item_id=:lineItemId)",nativeQuery = true)
	List<ApplicableTest> getAllTests(int lineItemId);
	
}
