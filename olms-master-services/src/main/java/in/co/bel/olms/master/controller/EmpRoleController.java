package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.EmpRoleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/empRole")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EmpRoleController {

	private final EmpRoleService empRoleService;

	private final Mapper mapper;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	@GetMapping("/{id}")
	public ResponseEntity<EmpRoleDto> getEmpRoleById(@PathVariable int id) throws DataNotFoundException {
		EmpRole empRole = empRoleService.getEmpRoleById(id);
		EmpRoleDto empRoleDto = mapper.map(empRole, EmpRoleDto.class);
		return ResponseEntity.ok(empRoleDto);
	}
	
	/**
	 * 
	 * @return
	 */
	
	@GetMapping("/getAll")
	public ResponseEntity<List<EmpRoleDto>> getAllEmpRole() {
		
		List<EmpRoleDto>  empRoleDtoList = new ArrayList<>();
		List<EmpRole> empRoles = empRoleService.getAllEmpRole();
		for(EmpRole empRole: empRoles) {
			EmpRoleDto empRoleDto = mapper.map(empRole, EmpRoleDto.class);
			empRoleDtoList.add(empRoleDto);
		}
		return ResponseEntity.ok(empRoleDtoList);
		}
	
	/**
	 * 
	 * @param orgTypeId
	 * @return
	 */
	
	@GetMapping("/getEmpRoleByOrgTypeId/{orgTypeId}")
	public ResponseEntity<List<EmpRoleDto>> getAllEmpRoleByOrgTypeId(@PathVariable int orgTypeId) {
		List<EmpRoleDto> empRoleDtosList = new ArrayList<>();
		List<EmpRole> empRoleList = empRoleService.getEmpRoleByOrgTypeId(orgTypeId);
		for(EmpRole empRole : empRoleList) {
			EmpRoleDto empRoleDto =new EmpRoleDto();
			empRoleDto.setId(empRole.getId());
			empRoleDto.setAdmin(empRole.isIs_admin());
			if(empRole.getSeniority()!=null)empRoleDto.setSeniorityId(empRole.getSeniority());
			empRoleDto.setName(empRole.getEmpRole());
			empRoleDtosList.add(empRoleDto);
			
		}
		return ResponseEntity.ok(empRoleDtosList);
		
	}

}
