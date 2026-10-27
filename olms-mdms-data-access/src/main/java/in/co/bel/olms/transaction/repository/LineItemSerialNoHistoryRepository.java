package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.LineItemSerialNoHistory;

public interface LineItemSerialNoHistoryRepository extends JpaRepository<LineItemSerialNoHistory, Integer> {

	@Query(value = "SELECT * FROM tdms.line_item_serial_no_history l "
			+ "WHERE l.line_item_serial_no_id = :lisnId "
			+ "ORDER BY l.created_at DESC ", nativeQuery = true)
    List<LineItemSerialNoHistory> findByLineItemSerialNoId(Integer lisnId);

	/**
     * @author Chirag Swamy
     * @since 09-10-2026
     * 
     */
	@Modifying
	@Query(value = "DELETE FROM tdms.line_item_serial_no_history h "
			+ "WHERE h.line_item_serial_no_id = :lisnId ", nativeQuery = true)
	void deleteByLineItemSerialNoId(Integer lisnId);

}
