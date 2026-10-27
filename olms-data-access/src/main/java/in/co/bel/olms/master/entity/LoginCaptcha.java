package in.co.bel.olms.master.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Captcha entity class for captcha table
 *
 * @author  Awadhesh Verma
 * @since   24-08-2023
 */

@Entity
@Table(name = "captcha", schema = "mdms")
@Data
public class LoginCaptcha implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "captcha_id", columnDefinition = "serial", unique = true, nullable = false)
    private int captchaId;

    @Column(name = "captcha_val", length = 10)
    private String captchaVal;

    public LoginCaptcha() {
    }

    public LoginCaptcha(String captchaVal) {
        this.captchaVal = captchaVal;
    }
}
