package com.wester.shop.service;

import com.wester.shop.controller.AuthController;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CheckTelService {
    private static Pattern pattern =  Pattern.compile("1\\d{10}");
    public Boolean verifyTelParams(AuthController.TelAndCode telAndCode) {
        if (telAndCode == null) {
            return false;
        }
        if (telAndCode.getTel() == null){
            return false;
        } else {
            return pattern.matcher(telAndCode.getTel()).find();
        }

    }
}
