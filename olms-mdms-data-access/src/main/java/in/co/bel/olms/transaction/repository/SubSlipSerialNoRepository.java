package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.SubSlipSerialNo;

public interface SubSlipSerialNoRepository extends JpaRepository<SubSlipSerialNo, Integer> {

	@Query(value = "SELECT * FROM tdms.line_item_lots_register where ol_line_item_id =:id order by id ASC", nativeQuery = true)
	List<SubSlipSerialNo> getRegisterNosByOlLineItemId(@Param("id") Integer id);

	List<SubSlipSerialNo> findBySubSlipLineItemIdOrderByIdAsc(Integer id);

	@Query(value = "SELECT * FROM tdms.sub_slip_serial_no where sub_slip_line_item_id =:subSlipLineItemId order by id ASC", nativeQuery = true)
	List<SubSlipSerialNo> getRegisterNosBySubSlipLineItemId(Integer subSlipLineItemId);

  Optional<SubSlipSerialNo> findByWarrantDetailsLineItemSerialNoIdAndWarrantDetailsCategoryId(Integer devId, Integer categoryId);

    @Query(value = "SELECT * FROM tdms.sub_slip_serial_no sssn " +
            "left join tdms.warrant_details wd ON wd.id = sssn.warrant_details_id " +
            "left join tdms.line_item_serial_no lisn ON lisn.id = wd.line_item_serial_no_id " +
            "where sssn.sub_slip_id = :sSlipId and lisn.id = :lisnId " +
            "ORDER BY sssn.id ASC ", nativeQuery = true)
    Optional<SubSlipSerialNo> findBySslipIdAndLisnId(Integer sSlipId, Integer lisnId);

	@Query(value = "SELECT lisn.id FROM tdms.sub_slip_serial_no s LEFT JOIN tdms.warrant_details wd ON wd.id= s.warrant_details_id "
			+ "LEFT JOIN tdms.line_item_serial_no lisn ON lisn.id = wd.line_item_serial_no_id "
      + "WHERE s.sub_slip_id IN :subSlipIds AND s.final_status = :status", nativeQuery = true)
  List<Integer> findLineItemSerialNosByStatus(@Param("subSlipIds") List<Integer> subSlipIds, @Param("status") String status);

	@Query(value = "SELECT * FROM tdms.sub_slip_serial_no where sub_slip_id =:sslipid order by id ASC", nativeQuery = true)
	List<SubSlipSerialNo> getRegisterNosBySubSlipId(Integer sslipid);

	@Transactional
	@Modifying
	@Query(value = " DELETE FROM tdms.sub_slip_serial_no WHERE sub_slip_id = :slipId AND final_status = 'in-process' ", nativeQuery = true)
	void deleteInProcessBySubmissionSlipId(@Param("slipId") Integer slipId);

	@Query(value = "Select * From tdms.sub_slip_serial_no ssn LEFT JOIN tdms.submission_slip ss ON ss.id = ssn.sub_slip_id WHERE ss.warrant_id = :warrantId  ", nativeQuery = true)
	List<SubSlipSerialNo> getSerialNosByCategoryId(@Param("warrantId") Integer warrantId);

	@Query(value = "SELECT l.* FROM tdms.sub_slip_serial_no l "
			+ "JOIN tdms.warrant_details w ON l.warrant_details_id = w.id "
			+ "JOIN tdms.line_item_serial_no s ON w.line_item_serial_no_id = s.id "
			+ "WHERE s.id = :serialNo", nativeQuery = true)
	SubSlipSerialNo findBySerialNoId(Integer serialNo);

	@Query(value = "SELECT s.id FROM tdms.sub_slip_serial_no s WHERE s.sub_slip_id = :sslipId ", nativeQuery = true)
	List<Integer> getIdsBySubSlipId(int sslipId);

}
