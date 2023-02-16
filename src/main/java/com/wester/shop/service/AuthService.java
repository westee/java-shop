package com.wester.shop.service;

import com.wester.shop.generate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@SuppressWarnings("EI_EXPOSE_REP2")
@Service
public class AuthService {
    private final UserService userService;
    private final MockSmsCodeService smsCodeService;
    private final VerificationCodeCheckService verificationCodeCheckService;
    @Autowired
    public AuthService(UserService userService,
                       MockSmsCodeService smsCodeService,
                       VerificationCodeCheckService verificationCodeCheckService) {
        this.userService = userService;
        this.smsCodeService = smsCodeService;
        this.verificationCodeCheckService = verificationCodeCheckService;
    }

    public User sendVerificationCode(String tel) {
        User user = userService.createUserIfNotExist(tel);
        String code = smsCodeService.sendCode(tel);
        verificationCodeCheckService.addCode(tel, code);
        return user;
    }


}
