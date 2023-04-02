package com.wester.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wester.shop.entity.LoginResponse;
import com.wester.shop.entity.UserLoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class AbstractIntegrationTest {
    @Autowired
    Environment env;

    @BeforeEach
    public void setUp() {

    }

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static class HttpResponse {
        int code;
        String body;
        Map<String, List<String>> headers;

        HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }
    }

    public HttpResponse doHttpRequest(String path, Object body, String cookie, String requestMethod) throws IOException {
        URL url;
        url = new URL(getUrl(path));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (cookie != null) {
            connection.setRequestProperty("Cookie", cookie);
        }

        connection.setRequestMethod(requestMethod);
        if ("POST" == requestMethod) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(objectMapper.writeValueAsString(body));
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
        connection.disconnect();
        return new HttpResponse(responseCode,   content == null ? "" : content.toString(), connection.getHeaderFields());
    }

    private String getSessionIdFromSetCookie(String setCookie) {
        // JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b; Path=/; HttpOnly; SameSite=lax -> JSESSIONID=7c647c1a-373e-49e4-9412-9bd998bcfa1b
        int semiColonIndex = setCookie.indexOf(";");
        return setCookie.substring(0, semiColonIndex);
    }

    private String getUrl(String path) {
        return "http://localhost:" + env.getProperty("local.server.port") + path;
    }

    public UserLoginResponse loginAndGetCookie() throws IOException {
        // 获取验证码
        int responseCode = doHttpRequest("/api/v1/code", CheckTelServiceTest.validParam, null, "POST").code;
        Assertions.assertEquals(HTTP_OK, responseCode);
        // 使用验证码登录
        HttpResponse loginResponse = doHttpRequest("/api/v1/login", CheckTelServiceTest.validParam,
                null, RequestMethod.POST.name());
        List<String> setCookie = loginResponse.headers.get("Set-Cookie");
        // 得到cookie
        String cookie = getSessionIdFromSetCookie(setCookie.stream().filter(c -> c.contains("JSESSIONID"))
                .findFirst()
                .get());
        String statusResponse = doHttpRequest("/api/v1/status", null, cookie, RequestMethod.GET.name()).body;
        LoginResponse statusResponseData = objectMapper.readValue(statusResponse, LoginResponse.class);
        return new UserLoginResponse(cookie, statusResponseData.getUser());
    }
}
