package com.wester.shop.service;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CheckTelService {
    private static Pattern pattern =  Pattern.compile("1\\d{10}");
    public Boolean verifyTelParams(String tel) {
        if (tel == null){
            return false;
        } else {
            return pattern.matcher(tel).find();
        }

    }
}
