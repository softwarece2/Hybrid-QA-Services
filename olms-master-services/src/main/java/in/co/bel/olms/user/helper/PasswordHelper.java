package in.co.bel.olms.user.helper;

import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.repository.UserRepository;
import in.co.bel.olms.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

/**
 * Service for password management
 * @author Manasa B
 * @since 18-10-22
 */
@Service
@RequiredArgsConstructor
public class PasswordHelper {

	private final UserRepository userRepository;
	
	public String decrypt(String data) {
		try
        {
            String key = ApplicationConstants.LOGIN_SECRET_KEY;
            String iv = ApplicationConstants.LOGIN_SECRET_KEY;

            var decoder = Base64.getDecoder();   
            byte[] encrypted1 = decoder.decode(data);

            var cipher = Cipher.getInstance("AES/CBC/NoPadding");
            var keyspec = new SecretKeySpec(key.getBytes(), "AES");
            var ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            var originalString = new String(original);
            return originalString.trim();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ApplicationConstants.FAILURE;
        }
    
	}

	public String passwordHash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	// Verifying password with the
	// hashed password.
	public boolean verifyPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}

	
	/**
	 * This function is changes the password of the user 
	 * @param inputStr
	 * @return
	 */
	public String changePassword(String inputStr) {
		String response = ApplicationConstants.FAILURE;
		try {
			var gson = new Gson();
			UserDto userDto = gson.fromJson(inputStr, new TypeToken<UserDto>() {
			}.getType());
			String encryptedUsername = userDto.getUsername();
			String username = decrypt(encryptedUsername);
			int userCountByUsername = userRepository.findUsersCountByUsername(username);
			if (userCountByUsername != 0) {
				var user = userRepository.getUserByUsername(username);
				String encPwdFromUser = userDto.getCurrentPassword();
				String encNewPassword = userDto.getPassword();
				String pwdFromUser = decrypt(encPwdFromUser);
				String newPassword = decrypt(encNewPassword);
				String existingPwd = user.getHashPassword();
				boolean isPasswordCorrect = verifyPassword(pwdFromUser, existingPwd);
				if (isPasswordCorrect) {
					String hashedPwd = passwordHash(newPassword);
					user.setHashPassword(hashedPwd);
					user.setSaltPassword(hashedPwd);
					user.setModifiedDatetime(new Date());
					userRepository.save(user);
					response = ApplicationConstants.SUCCESS;
				} else {
					response = "Current password is wrong";
				}
			} else {
				response = "User doesn't exist";
			}
		} catch (Exception e) {
			response = ApplicationConstants.FAILURE;
		}
		return response;
	}

}
