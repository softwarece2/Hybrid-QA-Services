package in.co.bel.olms.iol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.iol.entity.InspTestMapping;




@Repository
public interface InspTestMappingRepository extends JpaRepository<InspTestMapping, Integer>{

	
	@Query(value = "SELECT submitted_date FROM iol.insp_test_mapping im WHERE im.insp_id = :inspId AND im.test_id = :testId", nativeQuery = true)
	Object getSubmittedDate(@Param("inspId") int inspId, @Param("testId") Integer testId);

	@Query(value = "SELECT * FROM iol.insp_test_mapping WHERE test_id = :testId AND insp_id = :inspId", nativeQuery = true)
	InspTestMapping findByInspTestId(	@Param("inspId") int inspId, @Param("testId") int testId);

	
	@Query(value= "SELECT * FROM iol.insp_test_mapping e LEFT JOIN ate.insp_round ir ON ir.id = e.insp_id WHERE ir.sslip_serial_no_id = :subSerialNo AND e.test_id IN :ids", nativeQuery = true)
	List<InspTestMapping> findByIdsIn(@Param("ids") List<Integer> ids, @Param("subSerialNo") int subSerialNo);
	
	@Query(value= "SELECT * FROM iol.insp_test_mapping e "
      + " LEFT JOIN ate.insp_round ir ON ir.id = e.insp_id and ir.ol_line_item_id = :olLiId "
      + " LEFT JOIN tdms.ol_line_item_serial_no olisn on olisn.serial_no_id = ir.ol_ser_no_id"
			+ "	LEFT JOIN tdms.line_item_serial_no lisn on lisn.id = olisn.serial_no_id "
      + " WHERE olisn.id = :olSerialNo AND e.test_id IN :ids", nativeQuery = true)
  List<InspTestMapping> findByIdsInQa(@Param("ids") List<Integer> ids, @Param("olSerialNo") int olSerialNo, @Param("olLiId") int olLiId);
}
