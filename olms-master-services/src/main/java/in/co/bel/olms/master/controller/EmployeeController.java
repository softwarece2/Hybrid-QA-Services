package in.co.bel.olms.master.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.dto.EmployeeDto;
import in.co.bel.olms.master.dto.EqptGroupDto;
import in.co.bel.olms.master.dto.InspectorDto;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.service.EmployeeService;
import in.co.bel.olms.user.dto.UserDto;
import lombok.RequiredArgsConstructor;


/**
 * 
 * @author Manasa B
 *
 */

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	private final Mapper mapper;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	
	private HmisAppSecurity appSecurity = new HmisAppSecurity();

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable int id) throws DataNotFoundException {
		Employee employee = employeeService.getEmployeeById(id);
		EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
		return ResponseEntity.ok(employeeDto);

	}

	/**
	 * 
	 * @return
	 */

	@GetMapping("/getAll")
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();
		List<Employee> employees = employeeService.getAllEmployee();
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return ResponseEntity.ok(employeeDtoList);
	}

	/**
	 * 
	 * @return
	 */

	@GetMapping("/getAllInspectors")
	public ResponseEntity<List<EmployeeDto>> getAllInspectors() {
		return ResponseEntity.ok(employeeService.getAllInspectors());
	}

	@GetMapping("/getAllDOs")
	public ResponseEntity<List<EmployeeDto>> getAllDOs() {
		return ResponseEntity.ok(employeeService.getAllDOs());
	}

	
	/**
	 * 
	 * @return
	 */

	@GetMapping("/getAllSecOfficer")
	public ResponseEntity<List<EmployeeDto>> getAllSecOfficer() {
		return ResponseEntity.ok(employeeService.getAllInspectors());
	}

	
	@GetMapping("/getByOrgId/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllInspectorsByOrgId(@PathVariable int orgId)
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getAllInspectorsByOrgId(orgId);

		return ResponseEntity.ok(employeeDtoList);
	}

	
	@GetMapping("/getSecOffByOrgId/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getSecOfficerByOrgId(@PathVariable int orgId)
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getSecOfficerByOrgId(orgId);
		return ResponseEntity.ok(employeeDtoList);
	}
	
	
	@GetMapping("/getAllSecOffByOrgId/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllSecOffByOrgId(@PathVariable int orgId)
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getAllSecOffByOrgId();
		return ResponseEntity.ok(employeeDtoList);
	}
	
	/**
	 * Get immediate subordinate employees
	 * @author Manasa B
	 */
	@GetMapping("/getSubByEmp/{empId}")
	public ResponseEntity<List<EmployeeDto>> getSubByEmp(@PathVariable int empId)
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getSubByEmp(empId);
		return ResponseEntity.ok(employeeDtoList);
	}
	
	/**
	 * Get immediate subordinate employees by subordinate role id
	 * @author Manasa B
	 */
	@GetMapping("/getSubByEmpBySubRole/{empId}/{roleId}")
	public ResponseEntity<List<EmployeeDto>> getSubByEmpBySubRole(@PathVariable int empId,@PathVariable int roleId )
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getSubByEmpBySubRole(empId, roleId);
		return ResponseEntity.ok(employeeDtoList);
	}
	
	
	
	/**
	 * Get immediate subordinate dcqao employees
	 * @author Manasa B
	 */
	@GetMapping("/getSubDcqaoBySubRole/{empId}")
	public ResponseEntity<List<EmployeeDto>> getSubDcqaoBySubRole(@PathVariable int empId)
			throws DataNotFoundException {
		List<EmployeeDto> employeeDtoList = employeeService.getSubByDcqaoBySubRole(empId);
		return ResponseEntity.ok(employeeDtoList);
	}
	
	
	/**
	 * 
	 * @param employeeDto
	 * @return
	 */
	@PostMapping("/saveEmployee")
	public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee savedEmployee = employeeService.saveEmployee(employeeDto);
		EmployeeDto savedEmployeeDto = mapper.map(savedEmployee, EmployeeDto.class);
		return new ResponseEntity<>(savedEmployeeDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/getAllSubordinateInspectors/{orgId}/{empId}")
	public ResponseEntity<List<InspectorDto>> getAllSubordinateInspectors(@PathVariable int orgId,
			@PathVariable int empId) {

		List<InspectorDto> inspectorDtoList = new ArrayList<>();
		List<Object[]> subordinates = employeeService.getAllSubordinateInspectors(orgId, empId);
		for (Object[] objects : subordinates) {
			if ((int) objects[2] == ApplicationConstants.INSPECTOR) {
				InspectorDto inspectorDto = new InspectorDto();
				inspectorDto.setId((int) objects[0]);
				inspectorDto.setName(objects[3].toString());
				inspectorDtoList.add(inspectorDto);
			}
		}
		return ResponseEntity.ok(inspectorDtoList);
	}

	/**
	 * @author Shubham Anand
	 * @param empId
	 * @param roleId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/getAllSubOfEmpByRoleId/{empId}/{roleId}")
	public ResponseEntity<List<EmployeeDto>> getAllSubOfEmpByRoleId(@PathVariable int empId, @PathVariable int roleId) throws DataNotFoundException {
		try {
			return ResponseEntity.ok(employeeService.getAllSubByEmpAndRole(empId, roleId));
		}catch(DataNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	/**
	 * @author Shubham Anand
	 * @param empId
	 * @param roleId
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/getAllParentOfEmpByRoleId/{empId}/{roleId}")
	public ResponseEntity<List<EmployeeDto>> getAllParentOfEmpByRoleId(@PathVariable int empId, @PathVariable int roleId) throws DataNotFoundException {
		try {
			return ResponseEntity.ok(employeeService.getAllParentByEmpAndRole(empId, roleId));
		}catch(DataNotFoundException e) {
			e.printStackTrace();
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	/**
	 * @author Awadhesh Verma
	 * @param empId
	 * @return
	 */
	@GetMapping("/getParentTillJcByEmpAndRole/{empId}")
	public ResponseEntity<List<EmployeeDto>> getParentTillJcByEmpAndRole(@PathVariable int empId) {
		try {
			return ResponseEntity.ok(employeeService.getParentTillJcByEmpAndRole(empId));
		}catch(DataNotFoundException e) {
			return ResponseEntity.ok(Arrays.asList());
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	/**
	 * @author Awadhesh Verma
	 * @param orgId
	 * @return
	 */
	@GetMapping("/getEmpForInote/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getEmpForInote(@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getEmpForInote(orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getActiveEmpForInote/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getActiveEmpForInote(@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpForInote(orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getParentEmpById/{seniorityId}")
	public ResponseEntity<List<EmployeeDto>> getParentEmpById(@PathVariable int seniorityId) {
		try {
			return ResponseEntity.ok(employeeService.getEmpBySeniority(seniorityId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getActiveParentEmpById/{seniorityId}")
	public ResponseEntity<List<EmployeeDto>> getActiveParentEmpById(@PathVariable int seniorityId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpBySeniority(seniorityId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@GetMapping("/getActiveParentEmpByIdDeptId")
	public ResponseEntity<List<EmployeeDto>> getActiveParentEmpByIdDeptId(@RequestParam(required = true) Integer seniorityId, @RequestParam(required = true) List<Integer> deptIds) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpBySeniorityDeptId(seniorityId, deptIds));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getHosaList")
	public ResponseEntity<List<EmployeeDto>> getHosaList() {
		try {
			return ResponseEntity.ok(employeeService.getHosaList());
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getActiveSubOrdinateEmpById/{seniorityId}")
	public ResponseEntity<List<EmployeeDto>> getActiveSubOrdinateEmpById(@PathVariable int seniorityId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpBySubOrdinate(seniorityId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getActiveSubOrdinateEmpByIdDeptId/{seniorityId}/{deptId}")
	public ResponseEntity<List<EmployeeDto>> getActiveSubOrdinateEmpByIdDeptId(@PathVariable int seniorityId, @PathVariable int deptId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpBySubOrdinateDeptId(seniorityId, deptId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getActiveSubOrdinateEmpByIdDeptId")
	public ResponseEntity<List<EmployeeDto>> getActiveSubOrdinateEmpByIdDeptId(@RequestParam(required = true) Integer seniorityId, @RequestParam(required = true) List<Integer> deptIds) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpBySubOrdinateDeptIdV2(seniorityId, deptIds));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllEmpsByOrgId/{empId}/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllEmpsByOrgId(@PathVariable int empId ,@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getEmpsByOrgId(empId,orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getAllEmpsOfOrgn/{empId}/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllEmpsOfOrgn(@PathVariable int empId ,@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getEmpsOfOrgn(empId,orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getAllEmpsOfOrg/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllEmpsOfOrg(@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getEmpsOfOrg(orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllActiveEmpsOfOrgn/{empId}/{orgId}")
	public ResponseEntity<Set<EmployeeDto>> getAllActiveEmpsOfOrgn(@PathVariable int empId, @PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpsOfOrgn(empId,orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllActiveEmpsOfOrg/{orgId}")
	public ResponseEntity<Set<EmployeeDto>> getAllActiveEmpsOfOrg(@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpsOfOrg(orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getAllActiveEmpsByOrgId/{empId}/{orgId}")
	public ResponseEntity<List<EmployeeDto>> getAllActiveEmpsByOrgId(@PathVariable int empId ,@PathVariable int orgId) {
		try {
			return ResponseEntity.ok(employeeService.getActiveEmpsByOrgId(empId,orgId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getAdminsBySalesgrpId/{salesGrpId}")
	public ResponseEntity<List<UserDto>> getAdminsBySalesgrpId(@PathVariable int salesGrpId ) {
		try {
			return ResponseEntity.ok(employeeService.getAdminsBySalesgrpId(salesGrpId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	@GetMapping("/getAllActiveHOD")
	public ResponseEntity<List<EmployeeDto>> getAllActiveHOD() {
		try {
			return ResponseEntity.ok(employeeService.getAllActiveHOD());
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllActiveEmpsForOrgType/{seniorityId}/{orgTypeId}")
	public ResponseEntity<List<EmployeeDto>> getAllActiveEmpsForOrgType(@PathVariable int seniorityId, @PathVariable int orgTypeId) {
		try {
			return ResponseEntity.ok(employeeService.getAllActiveEmpsForOrgType(seniorityId, orgTypeId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
	}
	
	@GetMapping("/getAllHoSByDeptId/{deptTypeId}")
	public ResponseEntity<EqptGroupDto> getAllHoSByDept(@PathVariable int deptTypeId) {
		return ResponseEntity.ok(employeeService.getAllHoSByDept(deptTypeId));
	}	
	
	@GetMapping("/getEmpsByDept/{deptId}/partno/{partNoId}/role/{roleId}")
	public ResponseEntity<List<EmpRoleDto>> getAllEmpsByDeptIdAndPartNoId(@PathVariable Integer deptId, @PathVariable Integer partNoId, @PathVariable Integer roleId) {
		return ResponseEntity.ok(employeeService.getAllEmpsByDeptIdAndPartNoId(deptId, partNoId, roleId));
	}	
	
	
	@GetMapping("/getDcAcDac")
	public ResponseEntity<List<EmployeeDto>> getDcAcDac(){
		return ResponseEntity.ok(employeeService.getDcAcDacForQA());
	}
	
	@GetMapping("/getAllHosByProdId/{productIds}")
	public ResponseEntity<List<EmployeeDto>> getAllHosByProdId(@PathVariable List<Integer> productIds) {
		try {
		return ResponseEntity.ok(employeeService.getAllHosByProdId(productIds));
	}catch(Exception e) {
		return ResponseEntity.internalServerError().body(null);
	}
		
	}
		@GetMapping("/getAllHosBySoId/{soId}")
		public ResponseEntity<List<EmployeeDto>> getAllHosBySoId(@PathVariable int soId) {
			try {
			return ResponseEntity.ok(employeeService.getAllHosBySoId(soId));
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(null);
		}
		}
		
		
		// 30-01-2026
		@GetMapping("/getAllNotifyToEmpByDeptId/{deptTypeId}")
		public ResponseEntity<EqptGroupDto> getAllNotifyToEmpByDeptId(@PathVariable int deptTypeId) {
			return ResponseEntity.ok(employeeService.getAllNotifyToEmpByDeptId(deptTypeId));
		}	
				
		
		// 31-01-2026
		@GetMapping("/getAllActiveHosBydepId/{deptTypeId}")
		public ResponseEntity<EqptGroupDto> getAllActiveHosBydepId(@PathVariable int deptTypeId) {
			return ResponseEntity.ok(employeeService.getAllHoSByDept(deptTypeId));
		}	
				
		//14-08-26 - updated to fetch emp staff no for approval cycle		
		@GetMapping("/getAllActiveEmpBydepId/{deptTypeId}/{subSlipId}")
		public ResponseEntity<EqptGroupDto> getAllActiveEmpBydepId(@PathVariable int deptTypeId, @PathVariable int subSlipId, @RequestHeader(name = "Authorization") String token ) {
			Integer roleId = appSecurity.getRoleIdFromJwtToken(token);

			return ResponseEntity.ok(employeeService.getAllActiveEmpBydepId(deptTypeId, subSlipId, roleId));
		}	
		
		@GetMapping("/getAllDoAndHosQc")
		public ResponseEntity<EqptGroupDto> getAllDoAndHosQc() {
			return ResponseEntity.ok(employeeService.getAllDoAndHosQc());
		}	
}
	