package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.AmendmentsLetter;


public interface AmendmentsLetterRepository extends JpaRepository<AmendmentsLetter,Integer>{
	@Query(value = "SELECT * FROM tdms.amendments_letter where supply_order_id =:soId ORDER BY id DESC", nativeQuery = true)
	List<AmendmentsLetter> getBySoId(@Param("soId") int soId);

	@Query(value = "SELECT * FROM tdms.amendments_letter where so_line_item_id =:soLiId ORDER BY id DESC", nativeQuery = true)
	List<AmendmentsLetter> getBySoLiId(@Param("soLiId") int soLiId);
	
	boolean existsByLetterNoAndSupplyOrderId(String letterNo, Integer supplyOrderId);
}
