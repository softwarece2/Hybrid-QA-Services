package in.co.bel.olms.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import in.co.bel.core.constants.ApplicationConstants;
import in.co.bel.olms.master.dto.EmpDeptMappingDto;
import in.co.bel.olms.master.dto.EmployeeDto;
import in.co.bel.olms.master.entity.DepartmentType;
import in.co.bel.olms.master.entity.EmpDeptMapping;
import in.co.bel.olms.master.entity.EmpRole;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.entity.User;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.repository.DepartmentTypeRepository;
import in.co.bel.olms.master.repository.EmpDeptMappingRepository;
import in.co.bel.olms.master.repository.EmpRoleRepository;
import in.co.bel.olms.master.repository.EmployeeRepository;
import in.co.bel.olms.master.repository.OrganizationRepository;
import in.co.bel.olms.master.repository.SalesGroupRepository;
import in.co.bel.olms.master.repository.UserRepository;
import in.co.bel.olms.user.dto.UserDto;
import in.co.bel.olms.user.helper.PasswordHelper;
import in.co.bel.olms.validator.InputNameValidation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final EmployeeRepository employeeRepository;

    private final EmpRoleRepository empRoleRepository;

    private final PasswordHelper passwordHelper;

    private final OrganizationRepository organizationRepository;

    private final SalesGroupRepository salesGroupRepository;

    private final EmpDeptMappingRepository empDeptMappingRepository;

    private final DepartmentTypeRepository departmentTypeRepository;

    private final Mapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * @param id
     * @return
     */
    public User getUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new DataNotFoundException("User not found by Id: " + id);
        }
    }

    /**
     * @return
     */
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * @param userDto
     * @return
     */
    public User saveUser(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        user.setCreatedDatetime(new Date());
        return userRepository.save(user);
    }

    @Transactional
    public Boolean createUser(UserDto userDto) {

        String userName = userDto.getUsername();
        String password = passwordHelper.decrypt(userDto.getPassword());
        String originalUserName = passwordHelper.decrypt(userName);


        // Validate employee name and username
        if (StringUtils.isNotBlank(originalUserName)) {
            if (!InputNameValidation.checkString(originalUserName)) {
                throw new DataNotValidException("Username contains invalid characters");
            }
        }
        if (StringUtils.isNotBlank(userDto.getEName())) {
            if (!InputNameValidation.checkString(userDto.getEName())) {
                throw new DataNotValidException("Employee name contains invalid characters");
            }
        }

        // Check if the username already exists
        int usernameExists = userRepository.findUsersCountByUsername(originalUserName);
        if (usernameExists > 0) {
            throw new IllegalArgumentException("Username already exists");
        }

        String staffNo = userDto.getStaffNo();

        EmployeeDto empDto = new EmployeeDto();
        empDto.setEname(userDto.getEName());
        empDto.setEmpRole(userDto.getRoleId());
        empDto.setEmpDesignation(userDto.getRoleName());
        empDto.setGender(userDto.getGenderName());
        empDto.setSealNo(userDto.getSealNo());
        empDto.setCreatedBy(userDto.getCreatedBy());
        empDto.setOrganization(userDto.getOrgId());
        empDto.setSalesGroup(userDto.getSalesGrpId());
        empDto.setCreatedDateTime(new Date());
        empDto.setReportingOfficer(userDto.getReportingOfficerId());
        empDto.setDeptTypeId(userDto.getDepartmentTypeId());
        empDto.setStaffNo(staffNo);

        Employee emp = mapper.map(empDto, Employee.class);
        emp.setActive(true);
        emp.setStaffNo(staffNo);

        Employee saveEmp = employeeRepository.save(emp);
        if (saveEmp != null) {
            userDto.setEmpId(saveEmp.getId());
            User user = mapper.map(userDto, User.class);
            user.setCreatedDatetime(new Date());
            String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setHashPassword(hashedPwd);
            user.setSaltPassword(hashedPwd);
            user.setActive(true);
            user.setUsername(originalUserName);
            user.setLoginStatus(false);
            userRepository.save(user);

            List<EmpDeptMapping> empDeptMaps = new ArrayList<>();

            if (userDto.getDepartmentIds() != null) {
                userDto.getDepartmentIds().forEach(deptId -> {
                    EmpDeptMappingDto empDept = new EmpDeptMappingDto();
                    empDept.setDeptId(deptId);
                    empDept.setEmpId(saveEmp.getId());
                    empDeptMaps.add(mapper.map(empDept, EmpDeptMapping.class));
                });

                empDeptMappingRepository.saveAll(empDeptMaps);
            }

            return true;
        } else {
            logger.error("Error in saving User");
            return false;

        }


//		empDeptMappingRepository

    }

    public List<UserDto> getUsersByOrgId(int orgId) {

        List<UserDto> userDtos = new ArrayList<>();

        List<Object[]> usersList = userRepository.getAllUsersByOrgIdV2(orgId);
        for (Object[] user : usersList) {
            UserDto usrDto = new UserDto();
            usrDto.setEName(user[0] != null ? user[0].toString() : "");
            usrDto.setGenderName(user[1] != null ? user[1].toString().charAt(0) : null);
            usrDto.setSealNo(user[2] != null ? user[2].toString() : "");
            usrDto.setUsername(user[3] != null ? user[3].toString() : "");
            usrDto.setActiveStatus(user[4] != null ? user[4].toString() : "");
            usrDto.setOrgName(user[5] != null ? user[5].toString() : "");
            usrDto.setRoleName(user[6] != null ? user[6].toString() : "");
            usrDto.setRoleId(user[7] != null ? (int) user[7] : null);
            usrDto.setReportingOfficerId(user[8] != null ? (int) user[8] : null);
            usrDto.setReportingOfficerName(user[9] != null ? user[9].toString() : "");
            usrDto.setId(user[10] != null ? (int) user[10] : null);
            usrDto.setEmpId(user[11] != null ? (int) user[11] : null);
            usrDto.setDepartmentIds(user[12] != null ? (List<Integer>) user[12] : null);
            usrDto.setDepartmentName(user[13] != null ? user[13].toString() : "");
            userDtos.add(usrDto);
        }

        return userDtos;
    }

    //getAllUserByOrgTypeId
    public List<UserDto> getUsersByOrgTypeId(int orgTypeId) {

        List<UserDto> userDtos = new ArrayList<>();

        List<Object[]> usersList = userRepository.getAllUserByOrgTypeId(orgTypeId);

        for (Object[] user : usersList) {
            UserDto usrDto = new UserDto();
            usrDto.setEName(user[0] != null ? user[0].toString() : "");
            usrDto.setGenderName(user[1] != null ? user[1].toString().charAt(0) : null);
            usrDto.setSealNo(user[2] != null ? user[2].toString() : "");
            usrDto.setUsername(user[3] != null ? user[3].toString() : "");
            usrDto.setActiveStatus(user[4] != null ? user[4].toString() : "");
            usrDto.setOrgName(user[5] != null ? user[5].toString() : "");
            usrDto.setRoleName(user[6] != null ? user[6].toString() : "");
            usrDto.setRoleId(user[7] != null ? (int) user[7] : null);
            usrDto.setReportingOfficerId(user[8] != null ? (int) user[8] : null);
            usrDto.setReportingOfficerName(user[9] != null ? user[9].toString() : "");
            usrDto.setId(user[10] != null ? (int) user[10] : null);
            usrDto.setEmpId(user[11] != null ? (int) user[11] : null);
            usrDto.setDepartmentName(user[13] != null ? user[13].toString() : "");
            usrDto.setOrgId(user[14] != null ? (int) user[14] : null);
            usrDto.setSalesGrpId(user[15] != null ? (int) user[15] : null);
            usrDto.setDepartmentTypeId(user[16] != null ? (int) user[16] : null);
            usrDto.setStaffNo(user[17] != null ? user[17].toString() : "");

            if (user[12] != null) {
                List<Integer> deptIdList = new ArrayList<>();
                for (String s : user[12].toString().split(",")) {
                    Integer parseInt = Integer.parseInt(s);
                    deptIdList.add(parseInt);
                }
                usrDto.setDepartmentIds(deptIdList);
            }
            
            //28-08-26
            //Since existing employees dept type id is null.
            if(usrDto.getDepartmentTypeId() != null) {
            	Optional<DepartmentType> deptType = departmentTypeRepository.findById(usrDto.getDepartmentTypeId());
            	usrDto.setDeptTypeName(deptType.get().getDepartmentType());
            }
            
            userDtos.add(usrDto);
        }

        return userDtos;
    }

    public List<UserDto> getAllUsers() {

        List<UserDto> userDtos = new ArrayList<>();

        List<Object[]> usersList = userRepository.getAllUsers();
        try {
            for (Object[] user : usersList) {
                UserDto usrDto = new UserDto();
                try {
                    usrDto.setId((Integer) (user[0]));
                    usrDto.setUsername(user[1] != null ? user[1].toString() : "");
                    usrDto.setActiveStatus(user[2] != null ? user[2].toString() : "");
                    usrDto.setEmpId((Integer) (user[3]));
                    usrDto.setEName(user[4] != null ? user[4].toString() : "");

                    if (user[5] != null) {
                        usrDto.setGenderName(user[5].toString().charAt(0));
                    } else {
                        usrDto.setGenderName(null);
                    }
                    usrDto.setSealNo(user[6] != null ? user[6].toString() : "");
                    // usrDto.setUsername(user[7] != null ? user[7].toString() : "");
                    usrDto.setOrgName(user[7] != null ? user[7].toString() : "");
                    usrDto.setOrgId(user[8] != null ? (int) user[8] : null);
                    usrDto.setRoleName(user[9] != null ? user[9].toString() : "");
                    usrDto.setRoleId(user[10] != null ? (int) user[10] : null);
                    usrDto.setSalesGrpId(user[11] != null ? (Integer) (user[11]) : null);
                    usrDto.setSalesGrp(user[12] != null ? user[12].toString() : "");
                    usrDto.setOrgTypeId(user[13] != null ? (int) user[13] : null);
                    userDtos.add(usrDto);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userDtos;
    }

    public List<UserDto> getAllActiveLoginUsers() {


        List<UserDto> userDtos = new ArrayList<>();


        List<Object[]> usersList = userRepository.getAllActiveLoginUsers();
        for (Object[] user : usersList) {
            UserDto usrDto = new UserDto();
            usrDto.setId(user[0] != null ? Integer.parseInt(user[0].toString()) : null);
            usrDto.setEName(user[1] != null ? user[1].toString() : "");
            usrDto.setGenderName(user[2].toString().charAt(0));
            usrDto.setSealNo(user[3] != null ? user[3].toString() : "");
            usrDto.setUsername(user[4] != null ? user[4].toString() : "");
            usrDto.setActiveStatus(user[5] != null ? user[5].toString() : "");
            usrDto.setOrgName(user[6] != null ? user[6].toString() : "");
            usrDto.setRoleName(user[7] != null ? user[7].toString() : "");
            userDtos.add(usrDto);
        }


        return userDtos;
    }

    public User editUser(@Valid UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Optional<Employee> optional = employeeRepository.findById(userDto.getEmpId());
            Employee emp = optional.get();
            emp.setEname(userDto.getEName());

            Optional<EmpRole> emprole = empRoleRepository.findById(userDto.getRoleId());
            if (emprole.isPresent())
                emp.setEmpRole(emprole.get());

            if (userDto.getReportingOfficerId() != null) {
                Optional<Employee> optionalRepo = employeeRepository.findById(userDto.getReportingOfficerId());
                if (optionalRepo.isPresent())
                    emp.setReportingOfficer(optionalRepo.get());
            }

            if (userDto.getSalesGrpId() != null) {
                Optional<SalesGroup> optionalsales = salesGroupRepository.findById(userDto.getSalesGrpId());
                optionalsales.ifPresent(emp::setSalesGroup);
            }
            emp.setGender(userDto.getGenderName());

            if (userDto.getOrgId() != null) {
                Optional<Organization> orgOptional = organizationRepository.findById(userDto.getOrgId());
                orgOptional.ifPresent(emp::setOrganization);
            }

            String staffNo = userDto.getStaffNo();
            emp.setStaffNo(staffNo);
            emp.setModifiedDateTime(new Date());
            Employee saveEmp = employeeRepository.save(emp);

            if (saveEmp != null) {
                user.setModifiedDatetime(new Date());
                String password = passwordHelper.decrypt(userDto.getPassword());
                String username = passwordHelper.decrypt(userDto.getUsername());

                int usernameExists = userRepository.findUsersCountByUsername(username);

                if (!username.equals(user.getUsername()) && usernameExists > 0) {
                    throw new IllegalArgumentException("Username already exists");
                }

                String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
                user.setHashPassword(hashedPwd);
                user.setSaltPassword(hashedPwd);
                user.setActive(userDto.getActive());
                user.setUsername(username);
                user.setLoginStatus(false);
                user = userRepository.save(user);
            }
            return user;
        } else {
            throw new DataNotFoundException("User not found with Id: " + userDto.getId());
        }
    }

    @Transactional
    @Modifying
    public User updateUser(UserDto UserDtos) {

        Optional<User> UserOptional = userRepository.findById(UserDtos.getId());
        User users = UserOptional.get();
        try {
            String newUsername = passwordHelper.decrypt(UserDtos.getUsername());
            boolean usernameExists = userRepository.existsByUsernameAndIdNot(newUsername, UserDtos.getId());

            if (usernameExists) {
                throw new IllegalArgumentException("Personnel No. already exists!");
            }

            if (UserOptional.isPresent()) {
                Optional<Employee> optional = employeeRepository.findById(UserDtos.getEmpId());
                Employee emp = optional.get();
                emp.setEname(UserDtos.getEName());
                emp.setSealNo(UserDtos.getSealNo());

                Optional<EmpRole> emprole = empRoleRepository.findById(UserDtos.getRoleId());
                emprole.ifPresent(emp::setEmpRole);

                if (UserDtos.getReportingOfficerId() != null) {
                    Optional<Employee> optionalRepo = employeeRepository.findById(UserDtos.getReportingOfficerId());
                    optionalRepo.ifPresent(emp::setReportingOfficer);
                } else {
                    emp.setReportingOfficer(null);
                }

                emp.setGender(UserDtos.getGenderName());

                if (UserDtos.getOrgId() != null) {
                    Optional<Organization> orgOptional = organizationRepository.findById(UserDtos.getOrgId());
                    if (orgOptional.isPresent())
                        emp.setOrganization(orgOptional.get());
                }

                if (UserDtos.getDepartmentTypeId() != null) {
                    Optional<DepartmentType> deptType = departmentTypeRepository.findById(UserDtos.getDepartmentTypeId());
                    if (deptType.isPresent())
                        emp.setDeptType(deptType.get());
                }

                if (UserDtos.getSalesGrpId() != null) {
                    Optional<SalesGroup> salesGrpOptional = salesGroupRepository.findById(UserDtos.getSalesGrpId());
                    if (salesGrpOptional.isPresent())
                        emp.setSalesGroup(salesGrpOptional.get());
                }
                
                emp.setStaffNo(UserDtos.getStaffNo());

                emp.setModifiedDateTime(new Date());
                Employee saveEmp = employeeRepository.save(emp);
                if (saveEmp != null) {
                    users.setModifiedDatetime(new Date());
                    String password = passwordHelper.decrypt(UserDtos.getPassword());
                    String username = passwordHelper.decrypt(UserDtos.getUsername());
                    String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());
                    users.setHashPassword(hashedPwd);
                    users.setSaltPassword(hashedPwd);
                    users.setActive(UserDtos.getActive());
                    emp.setActive(UserDtos.getActive());
                    users.setUsername(username);
                    users.setLoginStatus(false);
                    users = userRepository.save(users);

                    if (UserDtos.getDepartmentIds() != null && !UserDtos.getDepartmentIds().isEmpty()) {
                        empDeptMappingRepository.deleteByEmployeeId(emp.getId());

                        List<EmpDeptMapping> empDeptMaps = new ArrayList<>();
                        UserDtos.getDepartmentIds().forEach(deptId -> {
                            EmpDeptMappingDto empDept = new EmpDeptMappingDto();
                            empDept.setDeptId(deptId);
                            empDept.setEmpId(saveEmp.getId());
                            empDeptMaps.add(mapper.map(empDept, EmpDeptMapping.class));
                        });

                        empDeptMappingRepository.saveAll(empDeptMaps);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

    }

    public List<UserDto> getOrgCqae() {

        try {
            List<UserDto> orgCqaeMappingDtos = new ArrayList<>();
            List<Object[]> orgDetails = employeeRepository.findByorgId(ApplicationConstants.OrgType.ID_CQAE);

            for (Object[] objectArray : orgDetails) {
                UserDto orgCqaeMappingDto = new UserDto();
                orgCqaeMappingDto.setId(Integer.parseInt(objectArray[0].toString()));
                orgCqaeMappingDto.setOrgName(objectArray[1].toString());
                orgCqaeMappingDto.setActive(Boolean.valueOf(objectArray[2].toString()));
                orgCqaeMappingDtos.add(orgCqaeMappingDto);
            }

            return orgCqaeMappingDtos;
        } catch (Exception e) {
            logger.error("Error in fetching Organisation Names: {}", e.getMessage());
            throw e;
        }
    }

}
