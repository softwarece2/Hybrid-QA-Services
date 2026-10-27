package in.co.bel.olms.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.co.bel.olms.transaction.entity.OLLineItemSerialNo;
import java.util.List;
import java.util.Optional;

public interface OLLineItemSerialNoRepository extends JpaRepository<OLLineItemSerialNo, Integer> {

	List<OLLineItemSerialNo> findBySerialNoIdAndOlLineItemId( Integer serialNo,Integer olLineItemId);
       
    List<OLLineItemSerialNo> findByOlLineItemIdOrderBySerialNoIdAsc(Integer olLineItemId);

  @Query(value = "SELECT olsn.id as olisnId, lis.id as soLineId ,lis.serial_no,olsn.ol_line_item_id,sli.line_item_no, olsn.status , oli.offer_letter_id "
			+ " FROM tdms.ol_line_item_serial_no olsn "
			+ "left join tdms.line_item_serial_no  lis on lis.id=olsn.serial_no_id "
			+ "left join tdms.so_line_item sli on sli.id=lis.so_line_item_id  "
			+ "left join tdms.ol_line_item oli on oli.id=olsn.ol_line_item_id "
      + "where ol_line_item_id=:olLineItemId and olsn.is_sample IS NOT TRUE and olsn.is_fai_sample IS NOT TRUE ", nativeQuery = true)
	List<Object[]> findUnSampledByOlLineItemId(Integer olLineItemId);

	
	// 7 is for Sample 
  @Query(value = " SELECT olsn.id,lis.serial_no,olsn.ol_line_item_id,sli.line_item_no, olsn.status as ol_sno_status,  oli.offer_letter_id,lis.id as sNoId , ppm.product_id, sli.registration_no FROM tdms.ol_line_item_serial_no olsn "
			+ "left join tdms.line_item_serial_no  lis on lis.id=olsn.serial_no_id "
			+ "left join tdms.so_line_item sli on sli.id=lis.so_line_item_id  "
			+ "left join tdms.ol_line_item oli on oli.id=olsn.ol_line_item_id "
			+ "left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id "
			+ "where ol_line_item_id=:olLineItemId and olsn.is_sample is true ", nativeQuery = true)
	List<Object[]> findSampledByOlLineItemId(Integer olLineItemId);

  @Query(value = "SELECT olsn.id as olisnId, lis.id,lis.serial_no,olsn.ol_line_item_id,sli.line_item_no, olsn.status as ol_sno_status , olsn.serial_no_id , oli.offer_letter_id, ppm.product_id FROM tdms.ol_line_item_serial_no olsn "
			+ "left join tdms.line_item_serial_no  lis on lis.id=olsn.serial_no_id "
			+ "left join tdms.so_line_item sli on sli.id=lis.so_line_item_id  "
			+ "left join tdms.ol_line_item oli on oli.id=olsn.ol_line_item_id "
			+ "left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id "
      + "where olsn.ol_line_item_id=:olLineItemId and olsn.is_fai_sample IS NOT TRUE and olsn.is_sample IS NOT TRUE", nativeQuery = true)
	List<Object[]> findUnSampledForFaiByOlLineItemId(@Param("olLineItemId")Integer olLineItemId);

	// Status 8 for FAI
  @Query(value = "SELECT olsn.id,lis.serial_no,olsn.ol_line_item_id,sli.line_item_no, olsn.status as ol_sno_status,  oli.offer_letter_id,lis.id as sNoId FROM tdms.ol_line_item_serial_no olsn "
			+ "left join tdms.line_item_serial_no  lis on lis.id=olsn.serial_no_id "
			+ "left join tdms.so_line_item sli on sli.id=lis.so_line_item_id  "
			+ "left join tdms.ol_line_item oli on oli.id=olsn.ol_line_item_id "
			+ "where ol_line_item_id=:olLineItemId and olsn.is_fai_sample is true ", nativeQuery = true)
	List<Object[]> findFaiByOlLineItemId(Integer olLineItemId);

	@Query(value = "SELECT olsn.* FROM tdms.ol_line_item_serial_no olsn "
			   + "    left join tdms.line_item_serial_no  lis on lis.id=olsn.serial_no_id "
			   + "   left join tdms.so_line_item sli on sli.id=lis.so_line_item_id  "
			   + "     left join tdms.ol_line_item oli on oli.id=olsn.ol_line_item_id "
			      + "     where olsn.id in (:serIds)", nativeQuery = true)
	List<OLLineItemSerialNo> findBySerialNoIds(List<Integer> serIds);
	
	@Query(value = " select   \r\n"
			+ "   lisn.id, olisn.ol_line_item_id, lisn.serial_no , sli.line_item_no,sli.part_number_id ,\r\n"
			+ "   ppm.product_id ,olisn.status as litemsts, olisn.status as qassts  \r\n"
			+ "   from tdms.ol_line_item_serial_no olisn     \r\n"
			+ "   left join tdms.line_item_serial_no lisn on lisn.id = olisn.serial_no_id   \r\n"
			+ "   left join tdms.ol_line_item oli on oli.id = olisn.ol_line_item_id     \r\n"
			+ "   left join tdms.offer_letter ol on ol.id = oli.offer_letter_id     \r\n"
			+ "   left join tdms.so_line_item sli on sli.id = oli.so_line_item_id     \r\n"
			+ "   left join ate.partno_product_mapping ppm on ppm.part_number_id = sli.part_number_id    \r\n"
			+ "   where ol.id = :olId and olisn.status  = 'pass'  ", nativeQuery = true)
	List<Object[]> getProductAndRegNosByOlId(@Param("olId") Integer olId);

  Optional<OLLineItemSerialNo> findFirstBySerialNoIdOrderByCreatedAtDesc(Integer serialNoId);
	
	@Query(value = "Select olisn.* from tdms.ol_line_item_serial_no olisn "
			+ " left join tdms.ol_line_item oli on oli.id = olisn.ol_line_item_id "
			+ " left join tdms.offer_letter ol on ol.id = oli.offer_letter_id "
			+ " where ol.id = :olId", nativeQuery = true)
	List<OLLineItemSerialNo> findRegNosByOlId(@Param("olId") Integer olId);
	
	@Query(value = "Select olsn.is_fai_sample from tdms.ol_line_item_serial_no olsn where olsn.ol_line_item_id = :olLineItem AND olsn.serial_no_id = :devId ", nativeQuery = true)
	Optional<Boolean> getIsFaiSample(Integer olLineItem, Integer devId);

	@Query(value = "Select olsn.id from tdms.ol_line_item_serial_no olsn where serial_no_id = :devId AND ol_line_item_id = :olLiId ", nativeQuery = true)
	Integer findBySerialNoOlLiId(Integer devId, Integer olLiId);
}

