package in.co.bel.olms.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import in.co.bel.olms.exception.AppSecurityException;
import in.co.bel.olms.user.dto.UserDto;

/**
 * @author Manasa B
 * @since 29-10-22
 */
public class UserSecurity {

	
	/**
	 * @author Manasa B
	 * @since 29-10-22
	 * Validate the current login user with userid parameter
	 */
	public static void validateRequest(String userId) throws AppSecurityException
	{		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDetails = (UserDto) authentication.getPrincipal();
		if(!userDetails.getUsername().equals(userId))
		{
			throw new AppSecurityException("Invalid user ");
		}
	}
	
}
