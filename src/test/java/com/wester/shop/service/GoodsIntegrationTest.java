package com.wester.shop.service;

import com.wester.shop.ShopApplication;
import com.wester.shop.generate.Goods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.config.location=classpath:test-application.yml" })
public class GoodsIntegrationTest extends AbstractIntegrationTest{
    @Test
    public void addGoodsOk() throws IOException {
        Goods goods = new Goods();
        goods.setName("花裤衩");
        goods.setDetails("这个花裤衩子真好啊");
        goods.setImgUrl("http://");
        goods.setPrice(BigDecimal.valueOf(100.22));
        goods.setStock(100);
        HttpResponse response = doHttpRequest("/api/v1/goods", goods, null, "POST");
        Assertions.assertEquals(200, response.code);
    }

    @Test
    public void deleteGoodsOk() throws IOException {
        HttpResponse response = doHttpRequest("/api/v1/goods", CheckTelServiceTest.validParam, null, "DELETE");
        Assertions.assertEquals(200, response.code);
    }
}
