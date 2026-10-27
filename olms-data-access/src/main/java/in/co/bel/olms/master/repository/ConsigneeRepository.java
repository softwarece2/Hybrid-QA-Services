package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.Consignee;


/**
* Repository interface for Consignee entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-13 
*/

public interface ConsigneeRepository extends JpaRepository<Consignee, Integer>{
	
	@Query(value = "SELECT * FROM mdms.consignee where id in :consigneeIds", nativeQuery = true)
	List<Consignee> getConsigneesByIds(@Param("consigneeIds") List<Integer> consigneeIds);
	
	Boolean existsByname(String name);
	Boolean existsByphone(String phone);
	
	
	@Query(value = "select name from mdms.consignee where id =:consigneeId", nativeQuery = true)
	String getConsigneeNameById(int consigneeId);

  @Query(value = "SELECT DISTINCT c.id, c.name "
      + "FROM mdms.consignee c "
      + "LEFT JOIN tdms.so_consignee_mapping scm ON scm.consignee_id = c.id "
      + "LEFT JOIN tdms.so_line_item sli ON sli.id = scm.so_line_item_id "
      + "LEFT JOIN tdms.sslip_line_item ssli ON ssli.so_line_item_id = sli.id "
      + "LEFT JOIN tdms.merge_sslip_mapping msm ON msm.sslip_id = ssli.sub_slip_id "
      + "WHERE msm.merge_sslip_id = :mergedSslipId", nativeQuery = true)
  List<Object[]> getConsigneeIdsAndNames(@Param("mergedSslipId") Integer mergedSslipId);


	@Query(value= "SELECT * from mdms.consignee WHERE active = true", nativeQuery = true)
	public List<Consignee> findAllActiveConsignees();
	
  	Boolean existsByNameAndIdNot(String name, int id);
	Boolean existsByPhoneAndIdNot(String phone, int id);

	
}
