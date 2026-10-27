package in.co.bel.olms.user.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
	private int orgId;
	private String orgName;
			
	private String roleName;
	private int roleId;
	private String specialization;
	private String genderName;
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
	private String password;
	private int empId;
	private String staffIdString;
	private String eName;
	
	private String currentPassword;
	private String token;
	private String type = "Bearer";
	private List<String> roles;
	private Integer stateId;
	private Integer countryId;
	private Integer cityId;	
	private String cityName;

	private String loginName;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDto() {

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
		

		//var role = loggedInUser.getEmp().getEmployeeRole();
		List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));// role.getEmployeeRole()));
		var userDto = new UserDto(loggedInUser.getId(), loggedInUser.getUsername(), loggedInUser.getSaltPassword(),
				authorities);
		userDto.setId(loggedInUser.getId());		

//		if (null != loggedInUser.getEmp() ) {			
//			var emp = loggedInUser.getEmp();
//			userDto.setEmpId(emp.getId());
//			userDto.setEName(emp.getEname());			
//		}	

//		if (null != loggedInUser.getEmp().getEmployeeRole()) {
//			role = loggedInUser.getEmp().getEmployeeRole();
//			userDto.setRoleId(role.getId());
//			userDto.setRoleName(role.getEmployeeRole());
//		}
//		
		userDto.setMessage(ApplicationConstants.SUCCESS);

		return userDto;
	}

}
