package com.wester.shop.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class VerificationCodeCheckService {
    Map<String, String> telToCode = new ConcurrentHashMap<>();

    public void addCode(String tel, String code) {
        telToCode.put(tel, code);
    }

    public String getCorrectCode(Object tel) {
        return telToCode.get(tel);
    }
}
