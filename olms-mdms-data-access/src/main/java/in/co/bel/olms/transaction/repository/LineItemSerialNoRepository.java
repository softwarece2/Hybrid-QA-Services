package in.co.bel.olms.transaction.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.LineItemSerialNo;

public interface LineItemSerialNoRepository extends JpaRepository<LineItemSerialNo, Integer> {
	
	@Query(value="delete from tdms.line_item_serial_no where serial_no=:serialNo ",nativeQuery=true)
	void deleteByLineItemLotsRegisterByRegisterNo(String serialNo);

	void deleteBySerialNo(String serialNo1);

	@Query(value = "SELECT * FROM tdms.line_item_serial_no WHERE so_line_item_id = :soLiId AND (ser_no_sts_id = :crt OR ser_no_sts_id = :rfr) ORDER BY id ASC", nativeQuery = true)
	List<LineItemSerialNo> findBySoLineItemIdOrderByIdAsc(@Param("soLiId") Integer soLiId,@Param("crt") Integer crt, @Param("rfr") Integer rfr );

	@Query(value = "Select sl.id, sl.serial_no, sl.ser_no_sts_id, sl.created_datetime, sl.modified_datetime , sl.so_line_item_id ,p.part_no,p.material_description,sli.line_item_no from tdms.warrant_details wd\r\n"
			+ "left join tdms.line_item_serial_no sl on wd.line_item_serial_no_id = sl.id \r\n"
			+ "left join tdms.category c on c.id=wd.category_id\r\n"
			+ "left join tdms.so_line_item sli on sli.id=c.so_line_item_id\r\n"
			+ "left join mdms.part_number p on p.id=sli.part_number_id\r\n"
			+ "where wd.category_id IN :warrantIds", nativeQuery = true)
	List<Object[]> findRegNos(List<Integer> warrantIds);
	
	@Query(value = "SELECT * FROM tdms.line_item_serial_no WHERE so_line_item_id = :soLiId ORDER BY id ASC", nativeQuery = true)
    List<LineItemSerialNo> getLineitemSerialNosBySoLiId(@Param("soLiId") Integer soLiId);
	
	@Query(value = " SELECT * from tdms.line_item_serial_no lisn "
			+ "  left join tdms.warrant_details wd on wd.line_item_serial_no_id = lisn.id "
			+ "  left join tdms.category c on c.id = wd.category_id "
			+ "  where lisn.so_line_item_id = :soLiId and c.id = :warrantId ", nativeQuery = true)
	List<LineItemSerialNo> findBySoLineItemIdAndWarrantId(Integer soLiId, Integer warrantId);


  @Query(value = "SELECT lisn.* FROM tdms.line_item_serial_no lisn "
			+ "LEFT JOIN tdms.warrant_details wd ON lisn.id = wd.line_item_serial_no_id "
			+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.warrant_details_id = wd.id "
			+ "LEFT JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = sssn.sub_slip_id "
			+ "LEFT JOIN tdms.merge_submission_slip mss ON mss.id = msm.merge_sslip_id "
      + "LEFT JOIN tdms.ol_line_item_serial_no ol ON lisn.id = ol.serial_no_id and ol.status <> 'fail' "
			+ "WHERE mss.id = :id AND sssn.final_status = 'pass' AND ol.serial_no_id IS NULL", nativeQuery = true)
	List<LineItemSerialNo> findPassedRegsByMergedSSlipId(Integer id);
	
	@Query(value = "SELECT string_agg(distinct(lisn.serial_no), ', ') from tdms.line_item_serial_no lisn "
		+ "left join tdms.ol_line_item_serial_no olisn on olisn.serial_no_id = lisn.id "
		+ "left join tdms.ol_line_item oli on oli.id = olisn.ol_line_item_id "
		+ "where oli.offer_letter_id = :olId", nativeQuery = true)
	String getRegsByOlId(Integer olId);

	@Query(value = "SELECT lisn.* "
			+ "FROM tdms.line_item_serial_no lisn "
			+ "LEFT JOIN tdms.warrant_details wd ON lisn.id = wd.line_item_serial_no_id "
			+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.warrant_details_id = wd.id "
			+ "LEFT JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = sssn.sub_slip_id "
			+ "LEFT JOIN tdms.merge_submission_slip mss ON mss.id = msm.merge_sslip_id "
			+ "LEFT JOIN tdms.ol_line_item_serial_no ol ON lisn.id = ol.serial_no_id "
			+ "WHERE mss.id = :id AND sssn.final_status = 'pending' order by lisn.id asc ", nativeQuery = true)
	List<LineItemSerialNo> findPendingRegsByMergedSSlipId(Integer id);
	
	@Query(value = "SELECT * FROM tdms.line_item_serial_no where sub_slip_id =:subSlipId order by id ASC", nativeQuery = true)
	List<LineItemSerialNo> getLineItemSerialNosBySubSlipId(Integer subSlipId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE tdms.line_item_serial_no lsn SET sub_slip_id = NULL , ser_no_sts_id = 2"
			+ " WHERE lsn.sub_slip_id = :slipId "
			+ " AND lsn.ser_no_sts_id = 3 ", nativeQuery = true)
	void resetSubmissionSlipIdForSlip(@Param("slipId") Integer slipId);
	
	@Query(value = "SELECT lisn.* "
			+ "FROM tdms.line_item_serial_no lisn "
			+ "LEFT JOIN tdms.warrant_details wd ON lisn.id = wd.line_item_serial_no_id "
			+ "LEFT JOIN tdms.sub_slip_serial_no sssn ON sssn.warrant_details_id = wd.id "
			+ "LEFT JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = sssn.sub_slip_id "
			+ "LEFT JOIN tdms.merge_submission_slip mss ON mss.id = msm.merge_sslip_id "
			+ "WHERE mss.id = :id AND sssn.final_status = 'pass' ", nativeQuery = true)
	List<LineItemSerialNo> findPassedRegsByMergedSSlipIdIntInote(Integer id);

  @Query(value = "SELECT distinct lisn.* from tdms.line_item_serial_no lisn " +
      " left join tdms.ol_line_item_serial_no olisn on olisn.serial_no_id = lisn.id " +
      " left join tdms.ol_line_item oli on oli.id = olisn.ol_line_item_id " +
      " where oli.offer_letter_id = :olId ", nativeQuery = true)
  List<LineItemSerialNo> getLineItemSerialNosByOlId(@Param("olId") Integer olId);
  
  @Query(value="SELECT serial_no FROM tdms.line_item_serial_no WHERE so_line_item_id = :soLiId", nativeQuery = true)
  List<String> findSrNoBySoLineItemId (@Param("soLiId")Integer soLiId);

  @Query(value="SELECT lisn.* FROM tdms.line_item_serial_no lisn "
  		+ " LEFT JOIN tdms.category c ON c.so_line_item_id = lisn.so_line_item_id WHERE c.id = :warrantId", nativeQuery = true)
  List<LineItemSerialNo> findByCategoryId(@Param("warrantId")Integer warrantId);
}
