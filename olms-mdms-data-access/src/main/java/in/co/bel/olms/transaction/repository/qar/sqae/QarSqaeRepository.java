package in.co.bel.olms.transaction.repository.qar.sqae;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.qar.sqae.QarSqae;

public interface QarSqaeRepository  extends JpaRepository<QarSqae, Integer>{

	@Query(value="select * from tdms_army.qar where ol_id = :olId order by created_datetime desc limit 1", nativeQuery = true)
	QarSqae getQarByOfferLetter(@Param("olId") int olId);
	
	@Query(value="FROM QarSqae WHERE id = :id")
	QarSqae getQarById(@Param("id") int id);

}
