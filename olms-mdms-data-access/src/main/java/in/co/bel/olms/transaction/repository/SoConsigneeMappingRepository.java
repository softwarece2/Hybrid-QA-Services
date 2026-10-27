package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.transaction.entity.SoConsigneeMapping;

public interface SoConsigneeMappingRepository extends JpaRepository<SoConsigneeMapping, Integer> {
	
	@Query(value = "SELECT * FROM TDMS.so_consignee_mapping WHERE supply_order_id= :soId", nativeQuery = true)
	List<SoConsigneeMapping> getSoConsigneeMappingsBySoId(@Param("soId") int soId);

	List<SoConsigneeMapping> findBySupplyOrderIdAndSoLineitemId(Integer id, Integer id2);

	 @Transactional
	@Modifying
	 @Query(value = "DELETE FROM tdms.so_consignee_mapping  WHERE so_line_item_id =:lineItemId", nativeQuery = true)
	void deleteBySoLineitemId(int lineItemId);
	

	@Query(value = "SELECT * FROM TDMS.so_consignee_mapping WHERE so_line_item_id= :soId", nativeQuery = true)	
	List<SoConsigneeMapping> findBySoLineItem(@Param("soId") int soId);

}
