package in.co.bel.olms.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import in.co.bel.olms.transaction.entity.AolAod;



public interface AolAodRepository extends JpaRepository<AolAod,Integer>{
	@Query(value = " select * from tdms.aol_aod a "
			+ " left join mdms.employee  e on a.aol_emp_id = e.id "
			+ " where e.organization_id =:orgId order by a.id desc ", nativeQuery = true)
	List<AolAod> getByOrgId(@Param("orgId") int orgId);
}
