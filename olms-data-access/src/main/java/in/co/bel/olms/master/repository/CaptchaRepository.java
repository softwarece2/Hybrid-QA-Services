package in.co.bel.olms.master.repository;

import in.co.bel.olms.master.entity.LoginCaptcha;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* CaptchaRepository JPA repository class
*
* @author  Awadhesh Verma
* @since   24-08-2023
*/

public interface CaptchaRepository extends JpaRepository<LoginCaptcha, Integer> {

}
