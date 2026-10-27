package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.LineItemObservationDetails;

public interface LineItemObservationDetailsRepository extends JpaRepository<LineItemObservationDetails, Integer> {
 
	 @Query(value = 
			    "SELECT line_item_obs_id " +
			    "FROM tdms.line_item_observation_details " +
			    "WHERE so_line_item_id IN (:lineItemIds) " +
			    "GROUP BY line_item_obs_id " +
			    "HAVING COUNT(DISTINCT so_line_item_id) = :size",
			    nativeQuery = true
			)
			Integer findCommonObservationId(@Param("lineItemIds") List<Integer> lineItemIds, @Param("size") int size);

}
