package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.INoteLineItem;

public interface INoteLineItemRepository extends JpaRepository<INoteLineItem, Integer>{

	@Query(value = "SELECT * FROM tdms.inote_line_item ili "
			+ "WHERE ili.inote_id = :inoteId ", nativeQuery = true)
	INoteLineItem getByINoteId(Integer inoteId);

}
