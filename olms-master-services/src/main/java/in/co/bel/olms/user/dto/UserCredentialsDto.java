package in.co.bel.olms.user.dto;

import lombok.Data;



/**
 * @author Manasa B
 */
@Data
public class UserCredentialsDto {

	private String userName;
	private String password;
	private String captcha;
	private int id;
}
