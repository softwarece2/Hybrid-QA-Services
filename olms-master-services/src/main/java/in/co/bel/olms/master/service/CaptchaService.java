package in.co.bel.olms.master.service;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.master.entity.LoginCaptcha;
import in.co.bel.olms.master.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service Class to generate and validate the captcha
 *
 * @author  Awadhesh Verma
 * @since   24-08-2023
 */
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final CaptchaRepository captchaRepository;

    public LoginCaptcha saveCaptcha(LoginCaptcha captcha){
        LoginCaptcha response = null;
        if(captcha != null){
            response = captchaRepository.save(captcha);
        }
        return response;
    }

    public String validateCaptcha(int id, String captchaVal){
        String response = null;
        if(id != 0 && StringUtils.isNotBlank(captchaVal)){
            Optional<LoginCaptcha> captcha = captchaRepository.findById(id);
            if(captcha.isPresent()){
                LoginCaptcha loginCaptcha = captcha.get();
                String generatedCaptcha = loginCaptcha.getCaptchaVal();
                if(captchaVal.equals(generatedCaptcha)){
                    response = ApplicationConstants.SUCCESS;
                }
                else {
                    response = "Wrong Captcha";
                }

            }
            else {
                response = "Wrong Captcha";
            }
        }
        else{
            response = ApplicationConstants.RETURN_INVALID_INPUT;
        }
        return response;
    }

}
