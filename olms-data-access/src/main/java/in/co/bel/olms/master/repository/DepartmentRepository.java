package in.co.bel.olms.master.repository;

import in.co.bel.olms.master.entity.Department;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	 boolean existsByDepartmentName(String departmentName);
	 
	@Query(value = "SELECT * FROM mdms.department d WHERE d.status = 'true'", nativeQuery = true)
	public List<Department> findAllByStatusTrue();

	List<Department> findAllByStatusTrue(Sort descending);

	Department findBySectionCode(Integer secCode);

	@Query(value = "SELECT * FROM mdms.department d WHERE d.department_type = :deptTypeId", nativeQuery = true)
	List<Department> findAllByDepartmentTypeId(Integer deptTypeId);
	
	
	
}
