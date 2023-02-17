package com.wester.shop.controller;

import com.wester.shop.generate.User;
import com.wester.shop.service.AuthService;
import com.wester.shop.service.CheckTelService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final CheckTelService checkTelService;

    @Autowired
    public AuthController(AuthService authService, CheckTelService checkTelService) {
        this.checkTelService = checkTelService;
        this.authService = authService;
    }

    @GetMapping("/code")
    public void sendCode(@RequestParam("tel") String tel, HttpServletResponse response) {
        if (checkTelService.verifyTelParams(tel)) {
            authService.sendVerificationCode(tel);
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/login")
    public User login(@RequestBody TelAndCode telAndCode) {
        // https://shiro.apache.org/authentication.html
        UsernamePasswordToken token = new UsernamePasswordToken(telAndCode.getTel(), telAndCode.getCode());
        token.setRememberMe(true);

        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
        return null;
    }

    public static class TelAndCode {

        public TelAndCode(String tel, String code) {
            this.tel = tel;
            this.code = code;
        }

        private String tel;
        private String code;

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
