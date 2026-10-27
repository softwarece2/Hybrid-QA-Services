package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.WarrantDetails;

public interface WarrantDetailsRepository extends JpaRepository<WarrantDetails, Integer>  {

	@Query(value = "Select w.lineItemSerialNo.id from WarrantDetails w where w.lineItemSerialNo.soLineItem.id =:soLiId order by w.lineItemSerialNo.id asc ")
	List<Integer> findBySoLineItemId(@Param("soLiId") Integer soLiId);

	@Query(value = "Select w.lineItemSerialNo.id from WarrantDetails w where w.lineItemSerialNo.soLineItem.id =:soLiId and w.category.id = :warrantId order by w.lineItemSerialNo.id asc")
	List<Integer> findIdsBySoLineItemIdAndWarrantId(@Param("soLiId") Integer soLiId, @Param("warrantId") Integer warrantId);

  @Query("SELECT w FROM WarrantDetails w " +
      "WHERE w.id IN (" +
      "  SELECT MAX(w2.id) FROM WarrantDetails w2 " +
      "  WHERE w2.lineItemSerialNo.soLineItem.id = :soLiId " +
      "  AND w2.category.id = :warrantId " +
      "  GROUP BY w2.lineItemSerialNo.id" +
      ") " +
      "ORDER BY w.lineItemSerialNo.id ASC")
  List<WarrantDetails> findBySoLineItemIdAndWarrantId(
      @Param("soLiId") Integer soLiId,
      @Param("warrantId") Integer warrantId
  );

	@Modifying
	@Transactional
	@Query(value = "Delete from WarrantDetails wd where wd.category.id = :warrantId and wd.id = :id")
	void deleteByWarrantIdAndId(@Param("warrantId") Integer warrantId, @Param("id") Integer id);

	@Query(value = "Select w from WarrantDetails w where w.lineItemSerialNo.id =:serialNoId and w.category.id = :warrantId")
	Optional<WarrantDetails> findByWarrantIdAndSerialNoId(Integer warrantId, Integer serialNoId);
  
  @Query(value = "SELECT wd.* FROM tdms.warrant_details wd "
			+ " WHERE wd.category_id = :id  AND wd.id NOT IN ( "
			+ " SELECT distinct warrant_details_id FROM tdms.sub_slip_serial_no where warrant_details_id is not null )", nativeQuery = true)
	List<WarrantDetails> findByCategoryId(Integer id);

    @Query(value = "Select * from tdms.warrant_details w where w.line_item_serial_no_id = :serialNoId and w.category_id = :warrantId ORDER BY w.id DESC LIMIT 1", nativeQuery = true)
	Optional<WarrantDetails> findByLineItemSerialNoId(@Param("serialNoId")Integer serialNoId, @Param("warrantId")Integer warrantId);
	
	@Query(value= "Select * from tdms.warrant_details w where w.line_item_serial_no_id = :serialNoId", nativeQuery = true)
	List<WarrantDetails> findByLineItemSerialNo(@Param("serialNoId")Integer serialNoId);
}
