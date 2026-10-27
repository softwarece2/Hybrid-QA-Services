package in.co.bel.olms.transaction.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.ItemLots;



public interface ItemLotsRepository   extends JpaRepository<ItemLots, Integer>{
	
	@Query(value = "SELECT * FROM tdms.item_lots where supply_order_id =:soId order by id DESC", nativeQuery = true)
	List<ItemLots> getSoItemLotsBySoId(@Param("soId") int soId);
	
	@Query(value = "SELECT * FROM tdms.item_lots where so_line_item_id =:soLiId order by id DESC", nativeQuery = true)
	List<ItemLots> getSoItemLotsBySoLiId(@Param("soLiId") int soLiId);
	
	@Query(value = "select lot_no from tdms.item_lots where supply_order_id = :supplyOrderId order by id desc limit 1", nativeQuery = true)
	String getAmendedLotNo(int supplyOrderId);
	
	// Modified by: Shridhar R
	// Date: 13-10-2025	
	boolean existsByIlLetterNoAndLotNoAndIlLetterDateAndSupplyOrderId(String letterNo, String lotNo, Date letterDate, int supplyOrderId);

	boolean existsByIlLetterNoAndLotNoAndIlLetterDateAndSoLineItemId(String letterNo, String lotNo, Date letterDate, int soLineItemId);

//	boolean existsByIlLetterNo(String ilLetterNo);

	@Query(value = "select COUNT(i)>0 from tdms.item_lots i where i.il_letter_no = :ilLetterNo and i.supply_order_id = :supplyOrderId", nativeQuery = true)
	boolean existsByIlLetterNoAndSupplyOrderId(String ilLetterNo, Integer supplyOrderId);

	@Query(value = "", nativeQuery = true)
	boolean existsByLotNoAndSupplyOrderId(String lotNo, Integer supplyOrderId);
	
}
