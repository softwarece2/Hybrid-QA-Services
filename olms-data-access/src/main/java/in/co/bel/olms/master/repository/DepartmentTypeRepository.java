package in.co.bel.olms.master.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.co.bel.olms.master.entity.DepartmentType;

public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Integer>{

	List<DepartmentType> findAllByIsApprovalCycleTrue();

}
