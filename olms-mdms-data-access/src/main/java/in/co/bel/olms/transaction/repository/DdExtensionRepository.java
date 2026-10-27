package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.co.bel.olms.transaction.entity.DdExtension;

public interface DdExtensionRepository extends JpaRepository<DdExtension, Integer>{
	@Query(value = "SELECT * FROM tdms.dd_extension where supply_order_id =:soId ORDER BY id DESC ", nativeQuery = true)
	List<DdExtension> getBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT * FROM tdms.dd_extension where so_line_item_id =:soLiId ORDER BY id DESC ", nativeQuery = true)
	List<DdExtension> getBySoLiId(@Param("soLiId") int soLiId);
	
	@Query(value = "SELECT * FROM tdms.dd_extension WHERE supply_order_id =:soId ORDER BY id DESC LIMIT 2 ", nativeQuery = true)
	List<DdExtension> getLast2BySoId(@Param("soId") int soId);

	@Query(value = "SELECT * FROM tdms.dd_extension WHERE so_line_item_id =:soLiId ORDER BY id DESC LIMIT 2 ", nativeQuery = true)
	List<DdExtension> getLast2BySoLiId(@Param("soLiId") int soLiId);

	// Modified by: Shridhar R
	// Date: 13-10-2025
	boolean existsByLetterNoAndLetterDateAndNewDdDateAndSupplyOrderId(String letterNo, Date letterDate, Date newDdDate, Integer supplyOrderId);

	boolean existsByLetterNoAndLetterDateAndNewDdDateAndSoLineItemId(String letterNo, Date letterDate, Date newDdDate, Integer soLineItemId);

}
