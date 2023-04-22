package com.wester.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wester.shop.entity.LoginResponse;
import com.wester.shop.entity.UserLoginResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

public class AbstractIntegrationTest {
    @Autowired
    Environment env;

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @BeforeEach
    public void setUp() {
        // 在每个测试开始前，执行一次flyway:clean flyway:migrate
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
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
        OkHttpClient httpClient;
        httpClient = new OkHttpClient();
        Request.Builder builder;
        RequestBody requestBody = null;
        if(cookie != null) {
            builder = new Request.Builder().addHeader("Cookie", cookie)
                    .url(getUrl(path));
        } else {
            builder = new Request.Builder()
                    .url(getUrl(path));
        }
        switch (requestMethod) {
            case "GET":
                builder = builder.get();
                break;
            case "DELETE":
                builder = builder.delete();
                break;
            case "PATCH":
                requestBody = RequestBody.create(objectMapper.writeValueAsString(body), okhttp3.MediaType.parse("application/json"));
                builder = builder.patch(requestBody);
                break;
            case "POST":
                if (body != null) {
                    requestBody = RequestBody.create(objectMapper.writeValueAsString(body), okhttp3.MediaType.parse("application/json"));
                }
                builder = builder.post(requestBody);
                break;
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + requestMethod);
        }

        Request request = builder.build();

        Response response = httpClient.newCall(request).execute();

        return new HttpResponse(response.code(), response.body().string(), response.headers().toMultimap());
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
