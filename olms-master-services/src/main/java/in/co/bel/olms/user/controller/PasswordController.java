package in.co.bel.olms.user.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.user.helper.PasswordHelper;
import lombok.RequiredArgsConstructor;


/**
 * Controller for password management
 * @author Manasa B
 * @since 08-12-22
 */

@RestController
@RequestMapping("/pwd")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PasswordController {

	private final PasswordHelper passwordHelper;
	
	@PostMapping("/changePassword")
	public String changePassword(@RequestBody String inputStr) {
		String response = ApplicationConstants.FAILURE;
		if (StringUtils.isNotBlank(inputStr)) {
			response = passwordHelper.changePassword(inputStr);
		}
		return response;
	}

}
