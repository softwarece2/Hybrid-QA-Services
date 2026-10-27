package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.EmpRole;

public interface EmpRoleRepository extends JpaRepository<EmpRole, Integer> {
	
	@Query(value = "select * from mdms.emp_role where organization_type =:orgTypeId", nativeQuery = true)
    public List<EmpRole> findByOrgTypeId(@Param("orgTypeId") int orgTypeId);
}
