package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.Ship;

public interface ShipRepository extends JpaRepository<Ship, Integer> {
	
	@Query(value = "select ship_name from tdms.ship", nativeQuery = true)
	List<String> findShipNames();
	
	Ship findByShipName(String shipName);

	@Query(value = "SELECT s.* from tdms.ship s "
			+ " left join tdms.project p on p.id = s.project_id "
			+ " left join tdms.supply_order so on so.id = p.supply_order_id"
			+ " left join tdms.so_line_item sli on sli.so_id = so.id "
			+ " where sli.id = :soLiId order by s.id asc", nativeQuery = true)
	List<Ship> findBySoLiId(@Param("soLiId") Integer soLiId);
	
	@Modifying
	@Query(value = "DELETE FROM tdms.ship  WHERE so_line_item_id =:lineItemId", nativeQuery = true)
	void deleteBySoLineitemId(int lineItemId);

}
