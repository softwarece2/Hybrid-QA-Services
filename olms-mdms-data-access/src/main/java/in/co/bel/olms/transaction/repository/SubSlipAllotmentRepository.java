package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.SubSlipAllotment;

public interface SubSlipAllotmentRepository extends JpaRepository<SubSlipAllotment,Integer>{
    
	@Query(value = "SELECT STRING_AGG( DISTINCT   emp.ename,',') " +
			" FROM tdms.sub_slip_allotment ol " +
			" left join mdms.employee emp  on emp.id = ol.alloted_to " +
			" WHERE ol.sub_slip_id = :olId "
			, nativeQuery = true)
	public String getAllotedSubSlipToEmployeeName(int olId);

@Query(value="SELECT * FROM tdms.sub_slip_allotment WHERE sub_slip_id=:olId AND alloted_to = :empId AND alloted_by = :allotedBy "
		+ "order by created_datetime desc limit 1 ",nativeQuery=true)
	public SubSlipAllotment getAllotedSubSlipToEmp(Integer olId, Integer empId, Integer allotedBy);

@Query(value="select * from tdms.sub_slip_allotment where sub_slip_id = :offerLetterId ORDER BY created_datetime DESC",nativeQuery=true)
public List<SubSlipAllotment> findBySubSlipIdOrderByCreatedDateDesc(Integer offerLetterId);

@Query(value = "select ssa.alloted_to from tdms.sub_slip_allotment ssa where ssa.sub_slip_id =:subSlipId ", nativeQuery = true)
public List<Integer> findAllotedEmpIdsforSslip(int subSlipId);

}
