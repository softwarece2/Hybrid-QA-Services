package in.co.bel.olms.user.helper;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.master.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Manasa B
 * @since 18-10-22
 */
@Service
@RequiredArgsConstructor
public class UserHelper {

	private final UserRepository userRepository;
	
	/**This function checks for the validity of the token by username 
	 * @author Manasa B
	 */
	@Transactional
	public boolean validateUserToken(String username, String token) throws UsernameNotFoundException {
		var user = userRepository.getValidUserByToken(username, token);
		if (user == null) {
			return false;
		}		
		else
		{
			return true;
		}		
	}
}
