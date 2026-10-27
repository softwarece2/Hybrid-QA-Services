package in.co.bel.olms.user.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import in.co.bel.olms.master.entity.OrgWing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * @author Manasa B
 * 
 */
@Getter
@Setter
@ToString
public class UserDto implements UserDetails {

	private static final long serialVersionUID = -796692032540494275L;
	private Integer id;
    private Integer orgId;
	private String orgName;
	private int orgTypeId;

	private String orgAddress;
	private String roleName;
	private int roleId;
	private String authority;
	
	private String specialization;
	private Character genderName;
	private String staffType;
	private String username;
	
	private Date createdDatetime;
	private Date modifiedDatetime;
	private Boolean active;
	private String activeStatus;
	private Date lastLogin;
	private Boolean loginStatus;
	private Integer lastUpdatedBy;
	
	private String message;
	private int messageId;

	private String password;
	
	private int empId;
	private String staffIdString;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z\\s]+$", message = "Only alphabet characters allowed")
	private String eName;
	
	private String currentPassword;
	private String token;
	private String type = "Bearer";
	private List<String> roles;
	private Integer stateId;
	private Integer countryId;
	private Integer cityId;	
	private String cityName;
	
	private Integer salesGrpId;	
	private String salesGrp;
	
	private Integer reportingOfficerId;
	private String reportingOfficerName;
	private String sealNo;
	private Integer createdBy;
	private Integer wingId;
	private String wingName;
	private byte[] logo;
	
	private String loginName;
	private Collection<? extends GrantedAuthority> authorities;
	private List<Integer> departmentIds;
	private String departmentName;
	private String deptTypeName;
	private Integer seniority;
	private Integer departmentTypeId;
	private String staffNo;
	public UserDto() {

	}
	public UserDto(String message) {
		this.message = message;
	}

	public UserDto(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDto user = (UserDto) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static UserDto build(User loggedInUser) {
		

		var role = loggedInUser.getEmp().getEmpRole();
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(loggedInUser.getEmp().getEmpRole().getSecEmpRoleId()));// role.getEmployeeRole()));
		
		
		var userDto = new UserDto(loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getSaltPassword(),
				authorities);
		userDto.setId(loggedInUser.getId());		
		userDto.setAuthority(loggedInUser.getEmp().getEmpRole().getSecEmpRoleId());
		if (null != loggedInUser.getEmp() ) {			
			var emp = loggedInUser.getEmp();
			userDto.setEmpId(emp.getId());
			userDto.setEName(emp.getEname());	
			
			if(null!=emp.getSalesGroup()) {
				var sales= emp.getSalesGroup();
				userDto.setSalesGrp(sales.getName());
				userDto.setSalesGrpId(sales.getId());
			}
			
			if(null!=emp.getReportingOfficer())
			{
				var reportOfficer = emp.getReportingOfficer();
				userDto.setReportingOfficerId(reportOfficer.getId());
				userDto.setReportingOfficerName(reportOfficer.getEname() );
			}
		}	

		if (null != loggedInUser.getEmp().getEmpRole()) {
			role = loggedInUser.getEmp().getEmpRole();
			userDto.setRoleId(role.getId());
			userDto.setRoleName(role.getEmpRole());
		}

		var emp = loggedInUser.getEmp();
		var org = emp.getOrganization();
		var deptType = emp.getDeptType();
//		var dept = emp.getDepartment();
		var salesGrp = emp.getSalesGroup();
		var empRole = emp.getEmpRole();
		
		if( null != org)
		{
			userDto.setOrgId(org.getId());
			userDto.setOrgName(org.getOrganizationName());
			userDto.setOrgAddress(org.getAddress());
			userDto.setLogo(org.getLogo());
			if(empRole != null){
				userDto.setSeniority(empRole.getSeniority());
			}
//			if(dept != null) {
//				userDto.setDepartmentId(dept.getId());
//				userDto.setDepartmentName(dept.getDepartmentName());
//			}
			if(salesGrp != null)
				userDto.setSalesGrpId(salesGrp.getId());
			OrgWing orgWing = org.getOrgWing();
			if (orgWing != null) {
				userDto.setWingId(orgWing.getId());
				userDto.setWingName(orgWing.getWingName());
			}
			try {
				userDto.setOrgTypeId(org.getOrganizationType().getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		userDto.setMessage(ApplicationConstants.SUCCESS);

		return userDto;
	}

}
