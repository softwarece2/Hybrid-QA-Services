package in.co.bel.olms.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.core.security.HmisAppSecurity;
import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.exception.UserException;
import in.co.bel.olms.master.repository.UserRepository;
import in.co.bel.olms.user.dto.ReturnTokenResponse;
import in.co.bel.olms.user.dto.UserCredentialsDto;
import in.co.bel.olms.user.dto.UserDto;
import in.co.bel.olms.user.helper.LoginHelper;
import in.co.bel.olms.user.helper.PasswordHelper;
import in.co.bel.olms.user.helper.UserHelper;
import in.co.bel.olms.user.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 01-08-23
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")

public class UserController implements UserDetailsService {

	Gson gson = new Gson();

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private  UserRepository userRepository;

	@Autowired
	private  LoginHelper loginHelper;

	@Autowired
	private  PasswordHelper passwordHelper;

	@Autowired
	private  JwtUtils jwtUtils;

	@Autowired
	private  UserHelper userHelper;
	
	HmisAppSecurity hmisAppSecurity = new in.co.bel.core.security.HmisAppSecurity();
	
	/**
	 * User login validation 
	 * @author Manasa B
	 * @param credentials
	 * @return
	 */
	@PostMapping("/userLogin")
	public ResponseEntity<UserDto> validateLogin(@RequestBody String credentials, HttpServletRequest request) {

		String userName;
		String password;
		if (null != credentials) {
			String userIP = request.getRemoteAddr();
			UserCredentialsDto userDto = gson.fromJson(credentials, UserCredentialsDto.class);
//			String captchaResponse = captchaService.validateCaptcha(userDto.getId(), userDto.getCaptcha());
//			if (!captchaResponse.equals(ApplicationConstants.SUCCESS)) {
//				UserDto userDtoObj = new UserDto(captchaResponse);
//				return new ResponseEntity<>(userDtoObj, HttpStatus.UNAUTHORIZED);
//			}
			userName = userDto.getUserName();
			password = userDto.getPassword();
			String originalUserName = passwordHelper.decrypt(userName);
			String originalPassword = passwordHelper.decrypt(password);
			return loginHelper.validateLogin(originalUserName, originalPassword, userIP);
		} else {
			return ResponseEntity.badRequest().body(new UserDto(ApplicationConstants.RETURN_INVALID_INPUT));
		}
	}
	
	/**
	 * Log out of the User
	 * @author Manasa B
	 * @param credentials
	 * @return
	 */
//	@PostMapping("/logout")
//	public String logout(@RequestBody String credentials) {
//		if (null != credentials) {
//			UserCredentialsDto userDto = gson.fromJson(credentials, UserCredentialsDto.class);
//			return loginHelper.logout(userDto);
//		} else {
//			return ApplicationConstants.RETURN_INVALID_INPUT;
//		}
//	}
	
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		if (null != request) {
 			String jwt = parseLogOut(request);
			String userName = hmisAppSecurity.getUserNameFromJwtToken(jwt);
			UserCredentialsDto userDto = new UserCredentialsDto();
			userDto.setUserName(userName);
			loginHelper.logout(userDto);
			return ApplicationConstants.SUCCESS;

		} else {
			return ApplicationConstants.RETURN_INVALID_INPUT;
		}
	}

	/**
	 * Overloaded function for validation
	 * @author Manasa B
	 */
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}
		var userDto = UserDto.build(user);		
		return userDto;
		
	}
	
	
	@PostMapping("/refreshLogin")
	public ResponseEntity<UserDto> refreshLogin(@RequestBody String credentials, HttpServletRequest request) {

		String userName;
		String password;
		if (null != credentials) {
			UserCredentialsDto userDto = gson.fromJson(credentials, UserCredentialsDto.class);
			userName = userDto.getUserName();
			password = userDto.getPassword();
			String originalUserName = passwordHelper.decrypt(userName);
			String originalPassword = passwordHelper.decrypt(password);
			return loginHelper.validateLogin(originalUserName, originalPassword, request.getRemoteAddr());
		} else {
			return new ResponseEntity<>(new UserDto(ApplicationConstants.RETURN_INVALID_INPUT), HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestBody String inputStr) {
		String response = ApplicationConstants.FAILURE;
		if (StringUtils.isNotBlank(inputStr)) {
			response = passwordHelper.changePassword(inputStr);
		}
		return response;
	}
	
	@PostMapping("/validateToken")
	public ReturnTokenResponse validateToken(@RequestBody String token) throws UserException {
		ReturnTokenResponse responseDto = new ReturnTokenResponse();		
		if (token != null && jwtUtils.validateJwtToken(token)) {
			String username = jwtUtils.getUserNameFromJwtToken(token);
			var user = userRepository.getUserByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("User Not Found with username: " + username);
			}
			var userDto = UserDto.build(user);			
			boolean isValidToken = userHelper.validateUserToken(username, token);
			if (isValidToken) {
				responseDto.setUsername(userDto.getUsername());			
				responseDto.setAuthorities(userDto.getRoleName());
			}
			else
			{
				logger.error("User Token expired/invalid");
				throw new UsernameNotFoundException("Invalid user");
			}			
		}
		return responseDto;
	}	
	
	
	private String parseLogOut(HttpServletRequest request) {
		try {
			String headerAuth = request.getHeader("Authorization");

			if (headerAuth != null && !headerAuth.trim().isEmpty() && headerAuth.startsWith("Bearer ")) {
				return headerAuth.substring(7);
			}
		} catch (Exception e) {
			return "Error parsing Authorization header: " + e.getMessage();
		}
		return null;
	}

	/**
	 * This function terminates the existing session and allows the user to login against valid credentials
	 * @author Awadhesh Verma
	 * @param credentials, request
	 * @return
	 */
	@PostMapping("/forceLogin")
	public ResponseEntity<UserDto> forceLogin(@RequestBody String credentials, HttpServletRequest request) {

		String userName;
		String password;
		if (null != credentials) {
			String userIP = request.getRemoteAddr();
			UserCredentialsDto userDto = gson.fromJson(credentials, UserCredentialsDto.class);
//			String captchaResponse = captchaService.validateCaptcha(userDto.getId(), userDto.getCaptcha());
//			if (!captchaResponse.equals(ApplicationConstants.SUCCESS)) {
//				UserDto userDtoObj = new UserDto(captchaResponse);
//				return new ResponseEntity<>(userDtoObj, HttpStatus.UNAUTHORIZED);
//			}
			userName = userDto.getUserName();
			password = userDto.getPassword();
			String originalUserName = passwordHelper.decrypt(userName);
			String originalPassword = passwordHelper.decrypt(password);
			
			return loginHelper.forceLogin(originalUserName, originalPassword, userIP);
		} else {
			return ResponseEntity.badRequest().body(new UserDto(ApplicationConstants.RETURN_INVALID_INPUT));
		}
	}
	
}
