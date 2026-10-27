package in.co.bel.olms.master.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dto.DepartmentDto;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.DepartmentType;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.repository.DepartmentTypeRepository;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

	private final DepartmentTypeRepository departmentTypeRepository;

	private final Mapper mapper;

	private final SalesGroupRepository salesGroupRepository;
	
	public List<Department> getAllDepartment() {
		return departmentRepository.findAllByStatusTrue();
	}

	public List<DepartmentType> getAllDepartmentType() {
		return departmentTypeRepository.findAll();
	}
	
	public Department saveDepartment(DepartmentDto departmentDto) {
		DepartmentType departmentType = departmentTypeRepository.findById(departmentDto.getDepartmentTypeId())
				.orElse(null);
		 if (departmentRepository.existsByDepartmentName(departmentDto.getDepartmentName())) {
		        throw new IllegalArgumentException("Department Name Already Exists");
		    }
		Department department = mapper.map(departmentDto, Department.class);
		if (departmentType != null) {
			department.setDepartmentType(departmentType);
		}
		return departmentRepository.save(department);
	}
	
	public Department updateDepartment (DepartmentDto departmentDto) {
		
		Optional<Department> departmentOptional = departmentRepository.findById(departmentDto.getId());
		if (departmentOptional.isPresent()) {
			Department department =  departmentOptional.get();
			department.setDepartmentName(departmentDto.getDepartmentName());
			department.setStatus(departmentDto.getStatus());
			 
		if (departmentDto.getSectionCode() != null) {
	        department.setSectionCode(departmentDto.getSectionCode());
	        }
		
			Optional<SalesGroup> salesGroupOptional = salesGroupRepository.findById(departmentDto.getSalesGrpId());
			if (salesGroupOptional.isPresent()) {
				department.setSalesGroup(salesGroupOptional.get());
			}
			else {
				throw new DataNotFoundException("Sales Group not found with Id: " + departmentDto.getSalesGrpId());
			}
			Optional<DepartmentType> departmentTypeOptional = departmentTypeRepository
					.findById(departmentDto.getDepartmentTypeId());
			if (departmentTypeOptional.isPresent()) {
				department.setDepartmentType(departmentTypeOptional.get());
			} else {
				throw new DataNotFoundException(
						"DepartmentType not found with Id: " + departmentDto.getDepartmentTypeId());
			}
			return departmentRepository.save(department);
		
	} else {
		throw new DataNotFoundException("Department not found with Id: " + departmentDto.getId());
	}

}

	public List<DepartmentDto> getDepartmentsByDeptTypeId(Integer deptTypeId) {
		
		List<Department> deptlist=	 departmentRepository.findAllByDepartmentTypeId(deptTypeId);
		List<DepartmentDto> dtoLsit = new ArrayList<>();

		for (Department bo : deptlist) {
			DepartmentDto dto = new DepartmentDto();
			dto.setId(bo.getId());
			dto.setDepartmentName(bo.getDepartmentName());

			dtoLsit.add(dto);
		}
	 
	  
		return dtoLsit;
	}

	public List<DepartmentType> getAllDepartmentTypeAppCycle() {
		return departmentTypeRepository.findAllByIsApprovalCycleTrue();
	}

}
