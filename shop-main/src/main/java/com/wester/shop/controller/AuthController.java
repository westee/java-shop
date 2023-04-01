package com.wester.shop.controller;

import com.wester.shop.entity.LoginResponse;
import com.wester.shop.service.AuthService;
import com.wester.shop.service.CheckTelService;
import com.wester.shop.service.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;
    private final CheckTelService checkTelService;

    @Autowired
    public AuthController(AuthService authService, CheckTelService checkTelService) {
        this.checkTelService = checkTelService;
        this.authService = authService;
    }

    @PostMapping("/code")
    public void sendCode(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        if (checkTelService.verifyTelParams(telAndCode)) {
            authService.sendVerificationCode(telAndCode.getTel());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    @PostMapping("/login")
    public void login(@RequestBody TelAndCode telAndCode) {
        // https://shiro.apache.org/authentication.html
        UsernamePasswordToken token = new UsernamePasswordToken(telAndCode.getTel(), telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    @GetMapping("/status")
    public LoginResponse getStatus() {
        if (UserContext.getCurrentUser() != null) {
            return LoginResponse.alreadyLogin(UserContext.getCurrentUser());
        } else {
            return LoginResponse.notLogin();
        }
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
