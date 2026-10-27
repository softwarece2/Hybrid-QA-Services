package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SaleOrder;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Integer> {
	
	@Query(value = "SELECT SALE_ORDER_NO FROM TDMS.SALE_ORDER", nativeQuery = true)
	List<String> findSaleOrderNos();
	
	SaleOrder findBySaleOrderNo(String saleOrderNo);
	
	@Query(value = "SELECT * FROM TDMS.SALE_ORDER WHERE supply_order_id= :soId", nativeQuery = true)
	List<SaleOrder> getSaleOrdersBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT sale_order_no FROM TDMS.SALE_ORDER WHERE supply_order_id= :soId", nativeQuery = true)
	List<String> getSaleOrderNosBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT * FROM TDMS.SALE_ORDER WHERE SALE_ORDER_NO IN :saleOrderNos", nativeQuery = true)
	List<SaleOrder> findSaleOrderIds(@Param("saleOrderNos") List<String> saleOrderNos);

}
