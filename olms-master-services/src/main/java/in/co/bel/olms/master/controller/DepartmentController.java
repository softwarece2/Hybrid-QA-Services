package in.co.bel.olms.master.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.DepartmentDto;
import in.co.bel.olms.master.dto.DepartmentTypeDto;
import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.DepartmentType;
import in.co.bel.olms.master.repository.DepartmentRepository;
import in.co.bel.olms.master.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/department")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

	private final DepartmentService departmentService;

    private final Mapper mapper;
    
    Gson gson = new Gson();
    
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @GetMapping("/getAllDepts")
    public ResponseEntity<Object> getAllDepts(){
        try{
            List<Department> departments = departmentRepository.findAll(Sort.by("id").descending());
            List<DepartmentDto> departmentDtos = departments.stream().map(dept -> mapper.map(dept, DepartmentDto.class)).collect(Collectors.toList());
            return ResponseEntity.ok(departmentDtos);
        } catch (Exception e){
            log.error("/department/getAllDepts Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getAllActiveDepts")
    public ResponseEntity<Object> getAllActiveDepts() {
        try {
            List<Department> departments = departmentRepository.findAllByStatusTrue(Sort.by("id").descending());
            
            List<DepartmentDto> departmentDtos = departments.stream()
                    .map(dept -> mapper.map(dept, DepartmentDto.class))
                    .collect(Collectors.toList());
                    
            return ResponseEntity.ok(departmentDtos);
        } catch (Exception e) {
            log.error("/department/getAllActiveDepts Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/getAllDepartmentType")
	public ResponseEntity<Object> getAllDepartmentType() {
		try {
			List<DepartmentType> departmentType = departmentService.getAllDepartmentType();
			List<DepartmentTypeDto> departmentTypeDtos = departmentType.stream()
					.map(dept -> mapper.map(dept, DepartmentTypeDto.class)).collect(Collectors.toList());
			return ResponseEntity.ok(departmentTypeDtos);
		} catch (Exception e) {
			log.error("/department/getAllDepts Exception: {}", e.getMessage());
			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
		}
	}
    
    /**
     * Retrieves all Department Types which are enabled for Approval Cycle
     * 
     * @since 01-09-2026
     * 
     */
    @GetMapping("/getAllDeptTypeAppCycle")
   	public ResponseEntity<Object> getAllDeptTypeAppCycle() {
   		try {
   			List<DepartmentType> departmentType = departmentService.getAllDepartmentTypeAppCycle();
   			List<DepartmentTypeDto> departmentTypeDtos = departmentType.stream()
   					.map(dept -> mapper.map(dept, DepartmentTypeDto.class)).collect(Collectors.toList());
   			return ResponseEntity.ok(departmentTypeDtos);
   		} catch (Exception e) {
   			log.error("/department/getAllDeptTypeAppCycle Exception: {}", e.getMessage());
   			return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
   		}
   	}
    
    
    @GetMapping("/getAllByStatusTrue")
    public ResponseEntity<Object> getAllByStatusTrue(){
        try{
            List<Department> departments = departmentRepository.findAllByStatusTrue();
            List<DepartmentDto> departmentDtos = departments.stream().map(dept -> mapper.map(dept, DepartmentDto.class)).collect(Collectors.toList());
            return ResponseEntity.ok(departmentDtos);
        } catch (Exception e){
            log.error("/department/getAllByStatusTrue Exception: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 
     * @param departmentDto
     * @return
     */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @PostMapping("/saveDept")
	public ResponseEntity<String> saveDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
		try {
		Department savedDepartment = departmentService.saveDepartment(departmentDto);
		DepartmentDto savedDepartmentDto = mapper.map(savedDepartment, DepartmentDto.class);
		return new ResponseEntity<>(gson.toJson(savedDepartmentDto), HttpStatus.CREATED);
	}catch(IllegalArgumentException e){
	return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
	}
		
	}
    
	
	/**
	 * 
	 * @param departmentDto
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @PostMapping("/updateDept")
	public ResponseEntity<String> updateDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
		DepartmentDto savedDepartmentDto = null;
		try {
			if (departmentDto != null) {

				Department savedDepartment = departmentService.updateDepartment(departmentDto);
				savedDepartmentDto = mapper.map(savedDepartment, DepartmentDto.class);
				return new ResponseEntity<String>(gson.toJson(savedDepartmentDto), HttpStatus.CREATED);

			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@GetMapping("/getDeptsByDeptTypeId/{deptTypeId}")
	public ResponseEntity<List<DepartmentDto>> getDeptartmentsByDeptId(@PathVariable Integer deptTypeId) {
		List<DepartmentDto> departments = departmentService.getDepartmentsByDeptTypeId(deptTypeId);
		return ResponseEntity.ok(departments);
	}
}
