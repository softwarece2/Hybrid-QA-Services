package in.co.bel.olms.user.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.User;
import in.co.bel.olms.master.exception.UserException;
import in.co.bel.olms.master.repository.UserRepository;
import in.co.bel.olms.user.dto.UserCredentialsDto;
import in.co.bel.olms.user.dto.UserDto;
import in.co.bel.olms.user.jwt.JwtUtils;
import in.co.bel.olms.validator.EntityValidator;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 18-10-22
 */
@Service
@RequiredArgsConstructor
public class LoginHelper {

	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;
	
	Gson gson = new Gson();

	private static final Logger logger = LoggerFactory.getLogger(LoginHelper.class);

	private final EntityValidator ahmisEntityValidator;

	private final JwtUtils jwtUtils;

	private static final long LOCK_TIME_DURATION = 0;//10 * 60 * 1000; // 15 minutes

	private static final long LOGIN_TIME_EXPIRE = 0; //10 * 60 * 1000; // 15 minutes

	private static final int MAX_FAIL_ATTEMPT = 3;

	/**
	 * @author Manasa B
	 * This function validate the username and password during login. 
	 * JWT token will be generated for the valid user credentials
	 * @param userName
	 * @param password
	 * @return
	 */
	public ResponseEntity<UserDto> validateLogin(String userName, String password, String userIP) {

		Authentication authentication;
		UserDto userDto = new UserDto();

		Boolean isvalidLogin = false;

		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			userDto = (UserDto) authentication.getPrincipal();

            List<String> roles = new ArrayList<>();
			roles.add(userDto.getRoleName());			
			userDto.setRoles(roles);
			userDto.setToken(jwt);
			userDto.setPassword(null);
			isvalidLogin = true;

		} catch (AuthenticationException e) {
		}

		try {
			User user = userRepository.getUserByUsername(userName);
			if (Boolean.TRUE.equals(isvalidLogin)) {
				if (null != user.getLocked() && user.getLocked().equals(Boolean.TRUE)) {
					boolean isunlocked = unlockUserWhenTimeExpired(user);
					if (!isunlocked) {
						throw  new UserException("Account is locked");
					}
				}
				if (null != user.getLoginStatus() && user.getLoginStatus().equals(Boolean.TRUE)) {
//					boolean isloginExpired = isLoginExpired(user);
//					if (!isloginExpired) {

						logger.error("User is already logged in.");
						String errorMsg = "";
						if(StringUtils.isNotBlank(user.getUserIP())) {
							errorMsg = "There is an active session from IP: "+user.getUserIP();
						}
						else {
							errorMsg = "There is an active session.";
						}
						return new ResponseEntity<>(new UserDto(errorMsg), HttpStatus.CONFLICT);

//					}
				}
			/*	if (null != user.getLoginStatus() && user.getLoginStatus().equals(Boolean.TRUE)) {
					boolean isloginExpired = isLoginExpired(user);
					if (!isloginExpired) {

						ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

						// Schedule the task to run after 5 minutes
						executorService.schedule(new Runnable() {
							@Override
							public void run() {
								System.out.println("Here");
								try {
									User user = userRepository.getUserByUsername(userName);
									if (null != user) {
										user.setLoginStatus(Boolean.FALSE);
										user.setFailedAttempts(0);
										user.setLocked(Boolean.FALSE);
										user.setLockTime(null);
										user.setLoginStatus(false);
										user.setTokenValue(null);
										boolean isvalid = ahmisEntityValidator.isValidEntity(user);
										if (isvalid) {
											userRepository.save(user);
										}
									} else {
										logger.error("Error in killExistingSession: User does not exist");
									}
								} catch (Exception e) {
									logger.error("Error in killExistingSession {}", e.getMessage());
								}
							}
						}, 5, TimeUnit.SECONDS);

						// Shutdown the executor when done to release resources
						executorService.shutdown();
						logger.error("User is already logged in.");
						throw new UserException("There is an active session. Plz try after 1 min.");

					}
				}
				*/
				user.setLastLogin(new Date());
				user.setLoginStatus(true);
				if(null==user.getCreatedDatetime()) {
					user.setCreatedDatetime(new Date());
				}
				user.setTokenValue(userDto.getToken());
				user.setUserIP(userIP);
				
				Map<String, Object> depts = userRepository.getDeptIdsAndNames(user.getEmp().getId());

				if (depts.get("dept_ids") != null && depts.get("dept_names") != null) {
				    String deptIdsStr = depts.get("dept_ids").toString();
				    String deptNamesStr = depts.get("dept_names").toString();
				
				    List<Integer> deptIdList = new ArrayList<>();
				    
				    userDto.setDepartmentName(deptNamesStr);
				
				    if (deptIdsStr != null && !deptIdsStr.isEmpty()) {
				        deptIdList = Arrays.stream(deptIdsStr.split(","))
				                .map(Integer::parseInt)
				                .collect(Collectors.toList());
				        userDto.setDepartmentIds(deptIdList);
				    } else {
				        userDto.setDepartmentIds(deptIdList);
				    }
				}
				
				userDto.setDepartmentTypeId(user.getEmp().getDeptType() != null ? user.getEmp().getDeptType().getId() : null);
				
				boolean isvalid = ahmisEntityValidator.isValidEntity(user);					
				if(isvalid) {
					userRepository.save(user);		
				}
				else
				{
					userDto=null;
				}			
			} else {
				if (null != user) {
//					user = increaseFailedAttempt(user);
					if (user != null && user.getFailedAttempts().equals(MAX_FAIL_ATTEMPT)) {
						throw  new UserException("Account is locked");
					} else {
						throw  new UserException("Wrong Credentials");
					}
				} else {
					throw  new UserException("Wrong Credentials");
				}
			}
		} 
		catch(UserException userex)
		{
			userDto=new UserDto();
			userDto.setMessageId(userex.getMessageId());
			userDto.setMessage(userex.getMessage());			
		}
		catch (Exception e) {
			e.printStackTrace();
			userDto=new UserDto();
			userDto.setMessage("Login failed. Kindly try after sometime");	
		}		
		logger.info("userDto: {}", userDto);
		return ResponseEntity.ok(userDto);
	}

	
	/**
	 * 
	 * This function logs out the user and invalidate the user token in database
	 * @author Manasa B
	 * @param dto
	 * @return
	 */
	public String logout(UserCredentialsDto dto) {
		String retValue = ApplicationConstants.FAILURE;
		try {
			User user = userRepository.getUserByUsername(dto.getUserName());
			if (null != user) {
				user.setLoginStatus(Boolean.FALSE);

				// release lock during logout also
				// Assumption Login was successful so clear lock status
				user.setFailedAttempts(0);
				user.setLocked(Boolean.FALSE);
				user.setLockTime(null);
				user.setLoginStatus(false);
				user.setModifiedDatetime(new Date());
				if (null == user.getCreatedDatetime()) {
					user.setCreatedDatetime(new Date());
				}
				user.setTokenValue(null);
				boolean isvalid = ahmisEntityValidator.isValidEntity(user);
				if (isvalid) {
					userRepository.save(user);
					retValue = ApplicationConstants.SUCCESS;
				}
			} else {
				logger.error("Logout: User does not exist");
			}
		} catch (Exception e) {
			logger.error("Error in Logout {}", e.getMessage());
		}
		return retValue;
	}

	private User increaseFailedAttempt(User loggedInUser) {

		// Add code for number of attempts
		if (null != loggedInUser.getFailedAttempts()) {
			if (loggedInUser.getFailedAttempts() < MAX_FAIL_ATTEMPT) {
				loggedInUser.setFailedAttempts(loggedInUser.getFailedAttempts() + 1);
			}
		} else {
			loggedInUser.setFailedAttempts(1);
		}
		if (loggedInUser.getFailedAttempts().equals(MAX_FAIL_ATTEMPT)) {
			loggedInUser.setLocked(Boolean.TRUE);
			loggedInUser.setLockTime(new Date());
		}
		loggedInUser.setModifiedDatetime(new Date());

		if (null == loggedInUser.getCreatedDatetime()) {
			loggedInUser.setCreatedDatetime(new Date());
		}
		boolean isvalid = ahmisEntityValidator.isValidEntity(loggedInUser);
		if (isvalid) {
			loggedInUser = userRepository.save(loggedInUser);
		} else {
			loggedInUser = null;
		}
		return loggedInUser;
	}

	private boolean unlockUserWhenTimeExpired(User user) {
		try {

			if (null == user.getLockTime()) {
				return true;
			}
			long lockTimeInMillis = user.getLockTime().getTime();
			long currentTimeInMillis = System.currentTimeMillis();

			if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
				user.setFailedAttempts(0);
				user.setLocked(Boolean.FALSE);
				user.setModifiedDatetime(new Date());
				user.setLockTime(null);

				if (null == user.getCreatedDatetime()) {
					user.setCreatedDatetime(new Date());
				}
				boolean isvalid = ahmisEntityValidator.isValidEntity(user);
				if (isvalid) {
					userRepository.save(user);
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean isLoginExpired(User user) {
		try {
			if (null == user.getLastLogin()) {
				return true;
			}
			long loginTimeInMillis = user.getLastLogin().getTime();
			long currentTimeInMillis = System.currentTimeMillis();
			if (loginTimeInMillis + LOGIN_TIME_EXPIRE > currentTimeInMillis) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * @author Awadhesh Verma
	 * This function terminates the existing session and allows the user to login against valid credentials
	 * @param userName
	 * @param password
	 * @param userIP
	 * @return
	 */
	public ResponseEntity<UserDto> forceLogin(String userName, String password, String userIP) {

		try {

			User user = userRepository.getUserByUsername(userName);
			if (null != user) {
				user.setLoginStatus(Boolean.FALSE);
				user.setFailedAttempts(0);
				user.setLocked(Boolean.FALSE);
				user.setLockTime(null);
				user.setLoginStatus(false);
				user.setTokenValue(null);
				userRepository.save(user);
				return validateLogin(userName, password, userIP);
			} else {
				logger.error("Error in killExistingSession: User does not exist");
				return ResponseEntity.badRequest().body(new UserDto(""));
			}
		} catch (Exception e) {
			e.printStackTrace();
			UserDto userDto = new UserDto();
			userDto.setMessage("Login failed. Kindly try after sometime");
			return ResponseEntity.internalServerError().body(userDto);
		}
	}
}
