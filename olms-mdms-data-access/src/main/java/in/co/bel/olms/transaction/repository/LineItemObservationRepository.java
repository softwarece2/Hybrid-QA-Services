package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.LineItemObservation;

public interface LineItemObservationRepository extends JpaRepository<LineItemObservation, Integer> {

	@Query(value = "SELECT "
			+ "f.id AS f_id, f.remarks, o.created_by AS f_created_by,  o.created_datetime As f_created_datetime, f.line_item_obs_id, "
			+ "e.ename AS f_created_by_name, " + "o.id AS o_id, o.supply_order_id, o.sent_to AS o_sent_to, o.tittle "
			+ "FROM tdms.line_item_observation_file f "
			+ "JOIN tdms.line_item_observation o ON f.line_item_obs_id = o.id "
			+ "LEFT JOIN mdms.employee e ON o.created_by = e.id WHERE f.send_to = :sentToId OR e.id=:sentToId order by f.id desc", nativeQuery = true)
	List<Object[]> findBySendToId(@Param("sentToId") int sentToId);

}