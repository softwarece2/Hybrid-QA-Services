package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.iol.entity.TestType;


public interface TestTypeRepository extends JpaRepository<TestType, Integer> {
	
	@Query(value = "SELECT id as te_id, test_type as te_ty FROM iol.test_type ty WHERE ty.id = :id", nativeQuery = true)
	TestType findTestTypeById(@Param("id") Integer id);

	@Query(value = "SELECT * FROM iol.test_type ORDER BY id ASC", nativeQuery = true)
	List<TestType> findAllTestTypes();
	
	@Query(value="Select * from iol.test_type where product_type_id =:productTypeId",nativeQuery=true)
	List<TestType> getTestByProductType(@Param("productTypeId")int productTypeId);

}
