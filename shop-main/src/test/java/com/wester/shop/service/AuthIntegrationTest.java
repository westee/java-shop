package com.wester.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wester.shop.ShopApplication;
import com.wester.shop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest {
    public static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    Environment env;

    private static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

    @Test
    public void returnHttpOKIfParamsCorrect() throws IOException {
        HttpResponse response = doHttpRequest("/api/v1/code", CheckTelServiceTest.validParam, null, "POST");
        Assertions.assertEquals(200, response.code);
    }

    @Test
    public void returnHttpBadIfParamsCorrect() throws IOException {
        HttpResponse response = doHttpRequest("/api/v1/code", CheckTelServiceTest.invalidParam, null, "POST");
        Assertions.assertEquals(400, response.code);
    }

    @Test
    public void getCodeAndLoginSuccess() throws IOException {
        // 获取验证码
        doHttpRequest("/api/v1/code", CheckTelServiceTest.validParam, null, "POST");
        // 登录
        HttpResponse response = doHttpRequest("/api/v1/login", CheckTelServiceTest.validParam, null, "POST");
        Assertions.assertEquals(200, response.code);
    }

    @Test
    public void loginLogoutTest() throws IOException {
        // 获取验证码
        doHttpRequest("/api/v1/code", CheckTelServiceTest.validParam, null, "POST");
        // 登录
        HttpResponse loginResponse = doHttpRequest("/api/v1/login", CheckTelServiceTest.validParam, null, "POST");

        List<String> setCookie = loginResponse.headers.get("Set-Cookie");
        String jsessionid = getSessionIdFromSetCookie(setCookie.stream()
                .filter(cookie -> cookie.contains("JSESSIONID")).findFirst().get());
        String statusResponseBody = doHttpRequest("/api/v1/status", CheckTelServiceTest.validParam, jsessionid, "GET").body;
        LoginResponse loginResponse1 = objectMapper.readValue(statusResponseBody, LoginResponse.class);

        Assertions.assertEquals(CheckTelServiceTest.validParam.getTel(), loginResponse1.getUser().getTel());

        doHttpRequest("/api/v1/logout", CheckTelServiceTest.validParam, jsessionid, "GET");
        String statusResponseBodyOnLogout = doHttpRequest("/api/v1/status", CheckTelServiceTest.validParam, jsessionid, "GET").body;
        LoginResponse responseBodyOnLogout = objectMapper.readValue(statusResponseBodyOnLogout, LoginResponse.class);
        Assertions.assertEquals(false, responseBodyOnLogout.isLogin());
    }

    private String getSessionIdFromSetCookie(String setCookie) {
        // JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b; Path=/; HttpOnly; SameSite=lax -> JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b
        int semiColonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semiColonIndex);
    }

    public HttpResponse doHttpRequest(String path, Object body, String cookie, String requestMethod) throws IOException {
        URL url;
        url = new URL(getUrl(path));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        if ("POST" == requestMethod) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(objectMapper.writeValueAsString(body));
        }
        if (cookie != null) {
            connection.setRequestProperty("Cookie", cookie);
        }

        connection.setInstanceFollowRedirects(false);
        connection.connect();
        StringBuffer content = null;
        int responseCode = connection.getResponseCode();
        if(responseCode >= 200 && responseCode < 300) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        }

        return new HttpResponse(responseCode,   content == null ? "" : content.toString(), connection.getHeaderFields());
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
