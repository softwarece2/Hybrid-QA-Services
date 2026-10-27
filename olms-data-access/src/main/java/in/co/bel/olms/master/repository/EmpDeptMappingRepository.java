package in.co.bel.olms.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.master.entity.EmpDeptMapping;

@Repository
public interface EmpDeptMappingRepository extends JpaRepository<EmpDeptMapping, Integer>{

	@Modifying
	@Query(value = "DELETE from EmpDeptMapping edm where edm.employee.id = :empId")
	void deleteByEmployeeId(@Param("empId") Integer empId);

}
