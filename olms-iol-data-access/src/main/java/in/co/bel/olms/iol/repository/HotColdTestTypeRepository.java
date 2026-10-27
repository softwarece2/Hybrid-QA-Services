package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.entity.HotColdTestType;


@Repository
public interface HotColdTestTypeRepository extends JpaRepository<HotColdTestType, Integer>{
	
	@Query(value = "SELECT hct.id AS id, hct.test_type FROM iol.hot_cold_test_type AS hct LEFT JOIN iol.product_type AS pt ON hct.product_type_id = pt.id WHERE hct.product_type_id = :productTypeId ORDER BY hct.id ASC", nativeQuery = true)
	List<HotColdTestType> findAllHotColdTestTypes(@Param("productTypeId")int productTypeId);

}
