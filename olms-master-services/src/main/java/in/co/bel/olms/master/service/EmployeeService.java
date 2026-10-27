package in.co.bel.olms.master.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.dozer.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.MasterConstants;
import in.co.bel.olms.master.dto.EmpRoleDto;
import in.co.bel.olms.master.dto.EmployeeDto;
import in.co.bel.olms.master.dto.EqptGroupDto;
import in.co.bel.olms.master.entity.EmpDesignation;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.User;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.UserRepository;
import in.co.bel.olms.transaction.entity.AolAod;
import in.co.bel.olms.transaction.repository.AolAodRepository;
import in.co.bel.olms.transaction.repository.LotAllotmentRepository;
import in.co.bel.olms.transaction.repository.SubSlipAllotmentRepository;
import in.co.bel.olms.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Manasa B
 *
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	private final Mapper mapper;

	private final AolAodRepository aolAodRepository;

	private final UserRepository userRepository;

	private final SubSlipAllotmentRepository subSlipAllotmentRepository;

	private final LotAllotmentRepository lotAllotmentRepository;

	/**
	 * 
	 * @param id
	 * @return
	 */
	public Employee getEmployeeById(int id) {

		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (employeeOptional.isPresent()) {
			return employeeOptional.get();
		} else {
			throw new DataNotFoundException("Employee not found Id: " + id);
		}

	}

	/**
	 * @author Manasa B
	 * 
	 * @return
	 */
	public List<EmployeeDto> getAllInspectors() {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();
		List<Employee> employees = employeeRepository.getEmpByRole(MasterConstants.EMP_ROLE.ID_INSPECTOR);
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;

	}

	public List<EmployeeDto> getAllDOs() {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();
		List<Employee> employees = employeeRepository.getEmpByRole(MasterConstants.EMP_ROLE.ID_DO);
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDto.setOrgName(employee.getOrganization().getOrganizationName());
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;

	}

	/**
	 * 
	 * @param orgId
	 * @return
	 */
	public List<EmployeeDto> getAllInspectorsByOrgId(int orgId) {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();

		List<Employee> employees = employeeRepository.getEmpByOrgId(MasterConstants.EMP_ROLE.ID_INSPECTOR, orgId);
		if (employees.isEmpty()) {
			throw new DataNotFoundException("Inspectors not found by OrgId: " + orgId);
		}
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;
	}

	/**
	 * 
	 * @param orgId
	 * @return
	 */
	public List<EmployeeDto> getSecOfficerByOrgId(int orgId) {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();
		List<Integer> roleIds= new ArrayList<>();
//		roleIds.add(MasterConstants.EMP_ROLE.ID_DCQAO);
//		roleIds.add(MasterConstants.EMP_ROLE.ID_ACQAO);
		List<Employee> employees = employeeRepository.getEmpByOrgId(orgId);
		if (employees.isEmpty()) {
			throw new DataNotFoundException("Section Officers not found by OrgId: " + orgId);
		}
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;
	}

	/**
	 * @return List<EmployeeDto>
	 */
	public List<EmployeeDto> getAllSecOffByOrgId() {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();
		List<Integer> roleIds= new ArrayList<>();
		roleIds.add(MasterConstants.EMP_ROLE.ID_DCQAO);
		roleIds.add(MasterConstants.EMP_ROLE.ID_ACQAO);
		List<Employee> employees = employeeRepository.getEmpByRole(roleIds);
		if (employees.isEmpty()) {
			throw new DataNotFoundException("Section Officers not found by OrgId: " );
		}
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}

	/**
	 * 
	 * @param employeeDto
	 * @return
	 */
	public Employee saveEmployee(EmployeeDto employeeDto) {
		Employee employee = mapper.map(employeeDto, Employee.class);
		employee.setCreatedDateTime(new Date());
		return employeeRepository.save(employee);

	}
	
	public List<Object[]> getAllSubordinateInspectors(int orgId, int empId) {
		return employeeRepository.getAllSubordinates(orgId, empId);
	}

	/**	 
	 * Get immediate subordinate employees
	 * @author Manasa B	 
	 * @param empId
	 * @return
	 */
	public List<EmployeeDto> getSubByEmp(int empId ) {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();		
		List<Employee> employees = employeeRepository.getSubByEmp(empId);
		if (employees.isEmpty()) {
			throw new DataNotFoundException("Section Officers not found by OrgId: " );
		}
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;
	}
	
	
	/**	 
	 * Get immediate subordinate employees
	 * @author Manasa B	 
	 * @param empId
	 * @return
	 */
	public List<EmployeeDto> getSubByEmpBySubRole(int empId, int roleId ) {
		List<EmployeeDto> employeeDtoList = new ArrayList<>();		
		List<Employee> employees = employeeRepository.getSubByEmpBySubRole(empId, roleId);
		if (employees.isEmpty()) {
			throw new DataNotFoundException("Section Officers not found by OrgId: " );
		}
		for (Employee employee : employees) {
			EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
			employeeDtoList.add(employeeDto);
		}
		return employeeDtoList;
	}
	
	/**	 
	 * Get immediate subordinate employees
	 * @author Manasa B	 
	 * @param empId
	 * @return
	 */
	public List<EmployeeDto> getSubByDcqaoBySubRole(int empId ) {
		List<EmployeeDto> employeeDtoList =getSubByEmpBySubRole (empId, MasterConstants.EMP_ROLE.ID_DCQAO );		
		return employeeDtoList;
	}
	
	/**
	 * @author Shubham Anand
	 * @param empId
	 * @param roleId
	 * @return
	 * 
	 * Get all subordinates of employee by role id
	 */
	public List<EmployeeDto> getAllSubByEmpAndRole(int empId, int roleId){
		List<Object[]> list = employeeRepository.getAllSubOfEmpByRoleId(empId, roleId);
		
		if(list.isEmpty())
			throw new DataNotFoundException("No Subordinates found!");
		
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for(Object[] obj : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setEname(obj[3].toString());
			dto.setRoleId(Integer.parseInt(obj[2].toString()));
			dto.setRoleName(obj[4].toString());
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	
	/**
	 * @author Shubham Anand
	 * @param empId
	 * @param roleId
	 * @return
	 * 
	 * Get all parents of employee by role id
	 */
	public List<EmployeeDto> getAllParentByEmpAndRole(int empId, int roleId){
		List<Object[]> list = employeeRepository.getAllParentOfEmpByRoleId(empId, roleId);
		
		if(list.isEmpty())
			throw new DataNotFoundException("No Subordinates found!");
		
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for(Object[] obj : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setEname(obj[3].toString());
			dto.setRoleId(Integer.parseInt(obj[2].toString()));
			dto.setRoleName(obj[4].toString());
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	/**
	 * @author Awadhesh Verma
	 * @param empId
	 * @return
	 * 
	 * Get all parents of employee(till JC excluding CQAE Admin) by emp id
	 */
	public List<EmployeeDto> getParentTillJcByEmpAndRole(int empId){
		
		List<Object[]> list = employeeRepository.getParentTillJcByEmpAndRole(empId);
		
		if(list.isEmpty())
			throw new DataNotFoundException("No Parent found!");
		
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for(Object[] obj : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setEname(obj[3].toString());
			dto.setRoleId(Integer.parseInt(obj[2].toString()));
			dto.setRoleName(obj[4].toString());
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}

	public List<EmployeeDto> getEmpForInote(int orgId) {

		List<Employee> list = employeeRepository.getEmpForInote(orgId);

		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Employee employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(employee.getId());
			dto.setEname(employee.getEname());
			dto.setEnameHi(employee.getEnameHindi());
			EmpRole empRole = employee.getEmpRole();
			if (empRole != null) {
				dto.setRoleId(empRole.getId());
				dto.setRoleName(empRole.getEmpRole());
				dto.setRoleNameHi(empRole.getEmpRoleHindi());
			}
			EmpDesignation empDesignation = employee.getEmpDesignation();
			if (empDesignation != null) {
				dto.setDesignation(empDesignation.getDesignation());
				dto.setDesignationHi(empDesignation.getDesignationHindi());
			}
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getActiveEmpForInote(int orgId) {

		List<Employee> list = employeeRepository.getActiveEmpForInote(orgId);

		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Employee employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(employee.getId());
			dto.setEname(employee.getEname());
			dto.setEnameHi(employee.getEnameHindi());
			EmpRole empRole = employee.getEmpRole();
			if (empRole != null) {
				dto.setRoleId(empRole.getId());
				dto.setRoleName(empRole.getEmpRole());
				dto.setRoleNameHi(empRole.getEmpRoleHindi());
			}
			EmpDesignation empDesignation = employee.getEmpDesignation();
			if (empDesignation != null) {
				dto.setDesignation(empDesignation.getDesignation());
				dto.setDesignationHi(empDesignation.getDesignationHindi());
			}
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	
	public List<EmployeeDto> getEmpBySeniority(int seniorityId) {

		List<Object[]> list = employeeRepository.getEmpBySeniority(seniorityId);
       List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}

	public List<EmployeeDto> getActiveEmpBySeniority(int seniorityId) {

		List<Object[]> list = employeeRepository.getActiveEmpBySeniority(seniorityId);
       List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}

	public List<EmployeeDto> getActiveEmpBySeniorityDeptId(int seniorityId, List<Integer> deptIds) {

		List<Object[]> list = employeeRepository.getActiveEmpBySeniorityofDeptIdV2(seniorityId, deptIds);
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	public List<EmployeeDto> getHosaList() {

		List<Object[]> list = employeeRepository.getHosaList();
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getActiveEmpBySubOrdinate(int seniorityId) {

		List<Object[]> list = employeeRepository.getActiveEmpBySubOrdinate(seniorityId);
       List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getActiveEmpBySubOrdinateDeptId(int seniorityId, int deptId) {

		List<Object[]> list = employeeRepository.getActiveEmpBySubOrdinateofDeptId(seniorityId, deptId);
       List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getActiveEmpBySubOrdinateDeptIdV2(int seniorityId, List<Integer> deptIds) {

		List<Object[]> list = employeeRepository.getActiveEmpBySubOrdinateofDeptIdV2(seniorityId, deptIds);
       List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getEmpsByOrgId(int empId,int orgId) {

     List<Object[]> list = employeeRepository.getAllEmpsByOrgId(empId, orgId);
		if(list.isEmpty())
			throw new DataNotFoundException("No Subordinates found!");
		else{
			List<EmployeeDto> employeeDtos = new ArrayList<>();
		for(Object[] obj : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(obj[0].toString()));
			dto.setEname(obj[1].toString());
			dto.setRoleId(Integer.parseInt(obj[2].toString()));
			dto.setRoleName(obj[3].toString());
			employeeDtos.add(dto);
		
	}
		return employeeDtos;
		}
	}
	
	public List<EmployeeDto> getActiveEmpsByOrgId(int empId,int orgId) {

		 List<Object[]> list = employeeRepository.getAllActiveEmpsByOrgId(empId, orgId);
				if(list.isEmpty())
					throw new DataNotFoundException("No Subordinates found!");
				else{
					List<EmployeeDto> employeeDtos = new ArrayList<>();
				for(Object[] obj : list) {
					EmployeeDto dto = new EmployeeDto();
					dto.setId(Integer.parseInt(obj[0].toString()));
					dto.setEname(obj[1].toString());
					dto.setRoleId(Integer.parseInt(obj[2].toString()));
					dto.setRoleName(obj[3].toString());
					if (obj[4] != null && !obj[4].toString().trim().isEmpty()) {
						dto.setEnameAndStaffNo(dto.getEname() + " - " + obj[4].toString());
					} else {
						dto.setEnameAndStaffNo(dto.getEname());
					}
					employeeDtos.add(dto);
				
			}
				return employeeDtos;
				}
			}
	
	
	public List<EmployeeDto> getEmpsOfOrgn(int empId,int orgId) {
		List<Object[]> list = new ArrayList<>();
		if (empId != 378) { 
		  list = employeeRepository.getAllEmpsOfOrgn(empId, orgId);
		} else {
			list = employeeRepository.getAllEmpsOfOrgn(orgId);
		}
				if(list.isEmpty())
					throw new DataNotFoundException("No Subordinates found!");
				else{
					List<EmployeeDto> employeeDtos = new ArrayList<>();
				for(Object[] obj : list) {
					EmployeeDto dto = new EmployeeDto();
					dto.setId(Integer.parseInt(obj[0].toString()));
					dto.setEname(obj[1].toString());
					dto.setRoleId(Integer.parseInt(obj[2].toString()));
					dto.setRoleName(obj[3].toString());
					employeeDtos.add(dto);
				
			}
				return employeeDtos;
				}
			}
	public List<EmployeeDto> getEmpsOfOrg(int orgId) {

		 List<Object[]> list = employeeRepository.getAllEmpsOfOrg(orgId);
				if(list.isEmpty())
					throw new DataNotFoundException("No Subordinates found!");
				else{
					List<EmployeeDto> employeeDtos = new ArrayList<>();
				for(Object[] obj : list) {
					EmployeeDto dto = new EmployeeDto();
					dto.setId(Integer.parseInt(obj[0].toString()));
					dto.setEname(obj[1].toString());
					dto.setRoleId(Integer.parseInt(obj[2].toString()));
					dto.setRoleName(obj[3].toString());
					employeeDtos.add(dto);
				
			}
				return employeeDtos;
				}
			}

	public Set<EmployeeDto> getActiveEmpsOfOrgn(int empId, int orgId) {

		 List<Object[]> list = employeeRepository.getAllActiveEmpsOfOrgn(empId, orgId);
				if(list.isEmpty())
					throw new DataNotFoundException("No Subordinates found!");
				else{
					Set<EmployeeDto> employeeDtos = new LinkedHashSet<>();
				for(Object[] obj : list) {
					EmployeeDto dto = new EmployeeDto();
					dto.setId(Integer.parseInt(obj[0].toString()));
					dto.setEname(obj[1].toString());
					dto.setRoleId(Integer.parseInt(obj[2].toString()));
					dto.setRoleName(obj[3].toString());
					employeeDtos.add(dto);
				
			}
				

				List<AolAod> aolAods=aolAodRepository.getByOrgId(orgId);
				
				Date today=new Date();
				for(AolAod aolAod : aolAods) {
					if (!today.before(aolAod.getFromDate()) && !today.after(aolAod.getToDate())) {
						EmployeeDto dto = new EmployeeDto();
					dto.setId(aolAod.getAsnEmp().getId());
					String ename=aolAod.getAsnEmp().getEname()+"("+aolAod.getType()+" )";
					dto.setEname(ename);
					dto.setRoleId(aolAod.getAolEmp().getId());
					String emprole=aolAod.getAsnDesg()+"("+aolAod.getType()+" )";				
					dto.setRoleName(emprole);
					employeeDtos.add(dto);
					}	
			}
				return employeeDtos;
				}
			}

	public Set<EmployeeDto> getActiveEmpsOfOrg(int orgId) {

		 List<Object[]> list = employeeRepository.getAllActiveEmpsOfOrg(orgId);
				if(list.isEmpty())
					throw new DataNotFoundException("No Subordinates found!");
				else{
					Set<EmployeeDto> employeeDtos = new HashSet<>();
				for(Object[] obj : list) {
					EmployeeDto dto = new EmployeeDto();
					dto.setId(Integer.parseInt(obj[0].toString()));
					dto.setEname(obj[1].toString());
					dto.setRoleId(Integer.parseInt(obj[2].toString()));
					dto.setRoleName(obj[3].toString());
					employeeDtos.add(dto);
				
			}
				
				List<AolAod> aolAods=aolAodRepository.getByOrgId(orgId);
				  // Convert today's date to LocalDate
			    LocalDate today = LocalDate.now();
			    // Convert fromDate and toDate to LocalDate
			
				for(AolAod aolAod : aolAods) {
				    LocalDate fromDate = aolAod.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				    LocalDate toDate = aolAod.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					 if ((today.isEqual(fromDate) || today.isAfter(fromDate)) && (today.isEqual(toDate) || today.isBefore(toDate))) {
						EmployeeDto dto = new EmployeeDto();
					dto.setId(aolAod.getAsnEmp().getId());
					String ename=aolAod.getAsnEmp().getEname()+"("+aolAod.getType()+" )";
					dto.setEname(ename);
					dto.setRoleId(aolAod.getAolEmp().getId());
					String emprole=aolAod.getAsnDesg()+"("+aolAod.getType()+" )";
					dto.setRoleName(emprole);
					employeeDtos.add(dto);
					}	
			}
				return employeeDtos;
				}
			}
	public List<UserDto> getAdminsBySalesgrpId(int salesGrpId) {
	    List<Object[]> list = employeeRepository.getAdminsBySalesgrpId(salesGrpId);
	    if(list.isEmpty())
			return null;
	    
	    else {
	    List<UserDto>dtos=new ArrayList<>();
	    for(Object[] obj : list) {
	    	UserDto dto = new UserDto();
			dto.setId(Integer.parseInt(obj[1].toString()));
			dto.setUsername((String) obj[0]);
			dto.setEName(obj[3].toString());
			dto.setEmpId(Integer.parseInt(obj[2].toString()));
			//dto.setRoleId(Integer.parseInt(obj[2].toString()));
			//dto.setRoleName(obj[3].toString());
			dtos.add(dto);
		
	}
	    
	    
	    return dtos;
	}
  }
	
	public List<EmployeeDto> getAllActiveHOD(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUserName = authentication.getName();
		
		User loggedInUser = userRepository.getUserByUsername(loggedInUserName);
		Integer loggedInEmpId = loggedInUser.getEmp().getId();
		
		List<Object[]> list = employeeRepository.getAllActiveHOD();
		
		if(list.isEmpty())
			throw new DataNotFoundException("No HOD found!");
		
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			int id = Integer.parseInt(employee[0].toString());
			if(id == loggedInEmpId) {
				continue; // Skip the current logged in employee
			}
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public List<EmployeeDto> getAllActiveEmpsForOrgType(int seniorityId, int orgTypeId) {

		List<Object[]> list = employeeRepository.getAllActiveEmpsForOrgType(seniorityId, orgTypeId);
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1] != null ? (employee[1].toString()) : "");
			dto.setEnameHi(employee[2] != null ? (employee[2].toString()) : "");
			dto.setRoleName(employee[3] != null ? (employee[3].toString()) : "");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	
	public EqptGroupDto getAllHoSByDept(int deptTypeId){	
		EqptGroupDto dto=new EqptGroupDto();
		List<Object[]> list1 = employeeRepository.getAllHoS(deptTypeId);	
		List<EmpRoleDto> userDto = new ArrayList<>();
		for (Object[] emp : list1) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(Integer.parseInt(emp[0].toString()));
			dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
			userDto.add(dto2);
		}
		
		List<Object[]> list2 =employeeRepository.getEmpsForQA();
		List<EmpRoleDto> userDto2 = new ArrayList<>();
		for (Object[] emp : list2) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(Integer.parseInt(emp[0].toString()));
			dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
			userDto2.add(dto2);
		}
		
		dto.setQaUsers(userDto2);
		dto.setQcUsers(userDto);
		
		return dto;
	}
	
	public List<EmpRoleDto> getAllEmpsByDeptIdAndPartNoId(Integer deptId, Integer partNoId, Integer roleId) {
		List<Object[]> list = employeeRepository.getAllEmpsByDeptIdAndPartNoId(deptId, partNoId, roleId);	
		List<EmpRoleDto> userDto = new ArrayList<>();
		for (Object[] emp : list) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(Integer.parseInt(emp[0].toString()));
			dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
			userDto.add(dto2);
		}
		return userDto;
	}

	public List<EmployeeDto> getDcAcDacForQA(){
		List<Object[]> list = employeeRepository.getDcAcDacForQA();

		if(list.isEmpty())
			throw new DataNotFoundException("No HOD found!");

		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1]!=null?(employee[1].toString()):"");
			dto.setEnameHi(employee[2]!=null?(employee[2].toString()):"");
			dto.setRoleName(employee[3]!=null?(employee[3].toString()):"");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	public List<EmployeeDto> getAllHosByProdId(List<Integer> productIds) {

		List<Object[]> list = employeeRepository.getAllHosByProdId(productIds);
		List<EmployeeDto> employeeDtos = new ArrayList<>();
		for (Object[] employee : list) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(Integer.parseInt(employee[0].toString()));
			dto.setEname(employee[1] != null ? (employee[1].toString()) : "");
			dto.setEnameHi(employee[2] != null ? (employee[2].toString()) : "");
			dto.setRoleName(employee[3] != null ? (employee[3].toString()) : "");
			employeeDtos.add(dto);
		}
		return employeeDtos;
	}
	public List<EmployeeDto> getAllHosBySoId(int soId) {

        List<Object[]> list = employeeRepository.getAllHosBySoId(soId);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (Object[] employee : list) {
            EmployeeDto dto = new EmployeeDto();
            dto.setId(Integer.parseInt(employee[0].toString()));
            dto.setEname(employee[1] != null ? (employee[1].toString()) : "");
            dto.setEnameHi(employee[2] != null ? (employee[2].toString()) : "");
            dto.setRoleName(employee[3] != null ? (employee[3].toString()) : "");
            employeeDtos.add(dto);
        }
        return employeeDtos;
    }

	public List<EmployeeDto> getAllActiveHODAndDo(Integer empId){
		List<Object[]> list = employeeRepository.getAllActiveHODAndDo(empId);
		
		if(list.isEmpty())
			throw new DataNotFoundException("No HOD found!");
		
		return list.stream()
	            .filter(employee -> !empId.equals(Integer.parseInt(employee[0].toString())))
	            .map(employee -> {
	                EmployeeDto dto = new EmployeeDto();
	                dto.setId(Integer.parseInt(employee[0].toString()));
	                dto.setEname(employee[1] != null ? employee[1].toString() : "");
	                dto.setEnameHi(employee[2] != null ? employee[2].toString() : "");
	                dto.setRoleName(employee[3] != null ? employee[3].toString() : "");
	                dto.setDeptName(employee[4] != null ? employee[4].toString() : "");
	                return dto;
	            })
	            .collect(Collectors.toList());
	}

	public EqptGroupDto getAllNotifyToEmpByDeptId(int deptTypeId){	
		EqptGroupDto dto=new EqptGroupDto();
		
		List<Integer> qc = new ArrayList<>();
		qc.add(14);
		qc.add(15);
		
		List<Object[]> list1 = employeeRepository.getAllNotifyToEmpByDeptId(qc,deptTypeId);	
		List<EmpRoleDto> userDto = new ArrayList<>();
		try {
			for (Object[] emp : list1) {
				try {
					EmpRoleDto dto2 = new EmpRoleDto();
					dto2.setId(Integer.parseInt(emp[0].toString()));
					dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
					userDto.add(dto2);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dto.setUsers(userDto);
		
		return dto;
	}
	
	
	//15-09-26 - updated to fetch emp staff no from emp table for approval cycle
	public EqptGroupDto getAllActiveEmpBydepId(int deptTypeId, int subSlipId, int roleId){	
		EqptGroupDto dto=new EqptGroupDto();
		List<Object[]> list1 = employeeRepository.getAllActiveEmpBydepId(deptTypeId);	
		
	

		if(roleId == MasterConstants.EMP_ROLE.ID_HOSQC) {
			Set<Integer> existingIds = new HashSet<>(
					subSlipAllotmentRepository.findAllotedEmpIdsforSslip(subSlipId));
			list1 = list1.stream()
				    .filter(obj -> !existingIds.contains((Integer) obj[0]))
				    .collect(Collectors.toList());
		}
		
		
		List<EmpRoleDto> userDto = new ArrayList<>();
		for (Object[] emp : list1) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(Integer.parseInt(emp[0].toString()));
			dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
			
			if (emp[2] != null && !emp[2].toString().trim().isEmpty()) {
				dto2.setNameAndStaffNo(dto2.getName() + " - " + emp[2].toString());
            } else {
            	dto2.setNameAndStaffNo(dto2.getName());
            }

			userDto.add(dto2);
		}
		
	
		dto.setUsers(userDto);
		
		return dto;
	}

//	public EqptGroupDto getAllActiveDoBydepTypeIdLotId(int deptTypeId, int mergeSubSlipId, int empId) {
//		EqptGroupDto dto=new EqptGroupDto();
//		
//		List<LotAllotment> lotAllotmentHistory = lotAllotmentRepository.findByMergeSubSlipIdOrderByCreatedAtDesc(mergeSubSlipId);
//		List<LotAllotmentDto>
//		List<Employee> empList = employeeRepository.getAllActiveDoBydepTypeId(deptTypeId, MasterConstants.EMP_ROLE.ID_DO );
//		Set<Integer> existingIds = new HashSet<>(lotAllotmentRepository.findExisitngIdByMergeSubSlipId(mergeSubSlipId));
//		List<Employee> filteredEmp = empList.stream().filter(emp-> !existingIds.contains(emp.getId()) )
//				.collect(Collectors.toList());
//		List<EmpRoleDto> userDto = new ArrayList<>();
//		for (Employee emp : filteredEmp) {
//			EmpRoleDto dto2 = new EmpRoleDto();
//			dto2.setId(emp.getId());
//			dto2.setName(emp.getEname());
//			userDto.add(dto2);
//		}
//		dto.setUsers(userDto);		
//		return dto;
//	}
	
	public EqptGroupDto getAllDoAndHosQc() {
		EqptGroupDto dto=new EqptGroupDto();
		List<Object[]> list1 = employeeRepository.getAllDoAndHosQc();	
		List<EmpRoleDto> userDto = new ArrayList<>();
		for (Object[] emp : list1) {
			EmpRoleDto dto2 = new EmpRoleDto();
			dto2.setId(Integer.parseInt(emp[0].toString()));
			dto2.setName(emp[1] != null ? (emp[1].toString()) : "");
			userDto.add(dto2);
		}
		dto.setUsers(userDto);		
		return dto;
	}
}
