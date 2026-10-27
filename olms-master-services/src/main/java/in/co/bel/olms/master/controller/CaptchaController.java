package in.co.bel.olms.master.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.apiclub.captcha.Captcha;
import in.co.bel.olms.master.dto.CaptchaDto;
import in.co.bel.olms.master.entity.LoginCaptcha;
import in.co.bel.olms.master.service.CaptchaService;
import in.co.bel.olms.util.CaptchaUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    // to generate captcha on load of login page
    @GetMapping("/get")
    public ResponseEntity<CaptchaDto> getCaptchaVal() {

        Captcha captcha = CaptchaUtil.createCaptcha(130, 50);
        LoginCaptcha loginCaptcha = new LoginCaptcha(captcha.getAnswer());
        LoginCaptcha savedCaptcha = captchaService.saveCaptcha(loginCaptcha);
        CaptchaDto captchaDto = new CaptchaDto();
        captchaDto.setCaptchaId(savedCaptcha.getCaptchaId());
        String encocedString = CaptchaUtil.encodeCaptcha(captcha);
        captchaDto.setCaptchaString(encocedString);
        return ResponseEntity.ok(captchaDto);
    }

}
