package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.LineItemLots;

public interface LineItemLotsRepository  extends JpaRepository<LineItemLots, Integer>{
	
	@Query(value = "SELECT * FROM tdms.line_item_lots where so_line_item_id =:soLiId order by id DESC", nativeQuery = true)
	List<LineItemLots> getItemLotsBySoLiId(@Param("soLiId") int soLiId);
	
	
	@Query(value = "SELECT COUNT(*) FROM tdms.line_item_lots where so_line_item_id =:soLiId AND financial_year=:financialYear", nativeQuery = true)
	Integer getItemLotsCountBySoLiIdAndFinancialYear(@Param("soLiId") int soLiId, @Param("financialYear") String financialYear);
	
	@Query(value = "SELECT * FROM tdms.line_item_lots where so_line_item_id =:soLiId AND financial_year=:financialYear order by financial_year ASC", nativeQuery = true)
	List<LineItemLots> getItemLotsBySoLiIdAndFinancialYear(@Param("soLiId") int soLiId, @Param("financialYear") String financialYear);

	
}
