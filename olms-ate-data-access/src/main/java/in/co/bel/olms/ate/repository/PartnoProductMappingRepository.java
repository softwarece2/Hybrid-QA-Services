package in.co.bel.olms.ate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import in.co.bel.olms.ate.entity.PartnoProductMapping;

@Repository
public interface PartnoProductMappingRepository extends JpaRepository<PartnoProductMapping, Integer> {
	 boolean existsByProductIdAndPartNumberId(Integer productId, Integer partNumberId);
	 boolean existsByProductId(Integer productId);
	 boolean existsByPartNumberId(Integer partNumberId);
	 
	@Query(value = "Select product_id from ate.partno_product_mapping where part_number_id=:id", nativeQuery = true)
	Integer findProductNoByPartNumberId(Integer id);
}