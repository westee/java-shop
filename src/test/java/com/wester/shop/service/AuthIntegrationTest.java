package com.wester.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wester.shop.ShopApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class CodeIntegrationTest {
    public static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    Environment env;
    @Test
    public void returnHttpOKIfParamsCorrect() throws IOException {
        URL url = new URL(getUrl("/api/code") + "?tel=" + CheckTelServiceTest.validParam.getTel());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        int status = con.getResponseCode();
        Assertions.assertEquals(status, 200);
    }

    @Test
    public void returnHttpBadIfParamsCorrect() throws IOException{
        URL url = new URL(getUrl("/api/code") + "?tel=" + CheckTelServiceTest.invalidParam.getTel());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        int status = con.getResponseCode();
        Assertions.assertEquals(400, status);
    }

    @Test
    public void getCodeAndLoginSuccess() throws IOException{
        // 获取验证码
        URL urlCode = new URL(getUrl("/api/code") + "?tel=" + CheckTelServiceTest.validParam.getTel());
        HttpURLConnection conCode = (HttpURLConnection) urlCode.openConnection();
        conCode.connect();

        // 登录
        URL urlLogin = new URL(getUrl("/api/login"));
        HttpURLConnection conLogin = (HttpURLConnection) urlLogin.openConnection();
        conLogin.setRequestMethod("POST");
        conLogin.setDoOutput(true);
        conLogin.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        DataOutputStream out = new DataOutputStream(conLogin.getOutputStream());
        out.writeBytes(objectMapper.writeValueAsString(CheckTelServiceTest.validParam));
        conLogin.connect();
        int status = conLogin.getResponseCode();
        Assertions.assertEquals(status, 200);
    }

    private String getUrl(String path) {
        return "http://localhost:" + env.getProperty("local.server.port") + path;
    }

    private static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
