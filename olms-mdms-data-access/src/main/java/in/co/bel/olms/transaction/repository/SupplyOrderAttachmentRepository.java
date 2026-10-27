package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.SupplyOrderAttachment;
/**
* Repository interface for SupplyOrderAttachment entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   07-06-2023
*/

public interface SupplyOrderAttachmentRepository extends JpaRepository<SupplyOrderAttachment, Integer>{
	
	@Query(value = "select * from tdms.supply_order_attachment where supply_order_id = :soId and file_name = :fileName limit 1", nativeQuery = true)
	SupplyOrderAttachment getBySoIdNFileName(int soId, String fileName);
	
	@Query(value = "SELECT category, id, file_name "
			+ "FROM tdms.supply_order_attachment "
			+ "WHERE supply_order_id = :soId "
			+ "GROUP BY category, id", nativeQuery = true)
	List<Object[]> getBySoId(int soId);
}
