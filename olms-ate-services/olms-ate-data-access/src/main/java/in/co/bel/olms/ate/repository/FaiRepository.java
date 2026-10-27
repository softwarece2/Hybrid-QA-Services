package in.co.bel.olms.ate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.FAI;


@Repository
public interface FaiRepository extends JpaRepository<FAI, Integer> {

	@Query(value = "SELECT MAX(page_no) AS max_page_no FROM ate.observation where fai_id = :id", nativeQuery = true)
	Integer findMaxPageNo(@Param("id") Integer id);

	List<FAI> findByOlLineItemId(Integer olLineItemId);

	@Query(value = "select * from ate.fai where ol_line_item_id = :olLineItemId and is_qa = true", nativeQuery = true)
	List<FAI> findByOlLineItemIdForQa(Integer olLineItemId);
	
	

}
