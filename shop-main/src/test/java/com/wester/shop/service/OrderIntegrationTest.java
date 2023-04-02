package com.wester.shop.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wester.api.data.DataStatus;
import com.wester.api.data.GoodsInfo;
import com.wester.api.data.OrderInfo;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.generate.Order;
import com.wester.shop.ShopApplication;
import com.wester.shop.entity.OrderResponse;
import com.wester.shop.entity.Response;
import com.wester.shop.entity.UserLoginResponse;
import com.wester.shop.mock.MockOrderRpcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test-application.yml"})
public class OrderIntegrationTest extends AbstractIntegrationTest {
    private static long userId = 2L;
    @Autowired
    MockOrderRpcService mockOrderRpcService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(mockOrderRpcService);
    }

    @Test
    public void canCreateOrder() throws IOException {
        UserLoginResponse loginResponse = loginAndGetCookie();
        String cookie = loginResponse.getCookie();

        OrderInfo orderInfo = new OrderInfo();
        GoodsInfo goodsInfo1 = new GoodsInfo();
        GoodsInfo goodsInfo2 = new GoodsInfo();
        goodsInfo1.setId(1);
        goodsInfo1.setNumber(1);
        goodsInfo2.setId(2);
        goodsInfo2.setNumber(1);
        orderInfo.setGoods(Arrays.asList(goodsInfo1, goodsInfo2));

        when(mockOrderRpcService.orderRpcService.placeOrder(any(), any())).thenAnswer(new Answer<Object>() {
            /**
             * @param invocation the invocation on the mock.
             * @return the value to be returned
             * @throws Throwable the throwable to be thrown
             */
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Order order = invocation.getArgument(1);
                order.setId(1234L);
                return order;
            }
        });

        HttpResponse post = doHttpRequest("/api/v1/order", orderInfo, cookie, "POST");
        Response<OrderResponse> response = objectMapper.readValue(post.body, new TypeReference<Response<OrderResponse>>() {});
        assertEquals(1234L, response.getData().getId());
        System.out.println(response);

        // 获取
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        Order order = new Order();
        order.setId(1234L);
        rpcOrderGoods.setOrder(order);
        rpcOrderGoods.setGoods(Arrays.asList(goodsInfo1, goodsInfo2));

        when(mockOrderRpcService.orderRpcService.getOrderById(1234L, userId)).thenReturn(rpcOrderGoods);

        HttpResponse get = doHttpRequest("/api/v1/order/1234", null, cookie, "GET");
        Response<RpcOrderGoods> getResponse = objectMapper.readValue(get.body, new TypeReference<Response<RpcOrderGoods>>() {});

        assertEquals(getResponse.getData().getOrder().getId(), order.getId());

    }

    @Test
    void canDeleteOrder() throws IOException {
        UserLoginResponse loginResponse = loginAndGetCookie();
        String cookie = loginResponse.getCookie();

        when(mockOrderRpcService.orderRpcService.deleteOrderById(100L, userId))
                .thenReturn(mockRpcOderGoods(100, 1, 3, 2, 5, DataStatus.DELETED));

        HttpResponse delete = doHttpRequest("/api/v1/order/100", null, cookie, "DELETE");
        RpcOrderGoods deleteResponse = objectMapper.readValue(delete.body, new TypeReference<RpcOrderGoods>() {});

        assertEquals(delete.code, 200);
        assertEquals(DataStatus.DELETED.getName(), deleteResponse.getOrder().getStatus());
        assertEquals(100L, deleteResponse.getOrder().getId());
        assertEquals(1, deleteResponse.getGoods().size());
        assertEquals(3L, deleteResponse.getGoods().get(0).getId());
        assertEquals(5, deleteResponse.getGoods().get(0).getNumber());
    }

    @Test
    public void canRollBackIfDeductStockFailed() throws Exception {
        UserLoginResponse loginResponse = loginAndGetCookie();

        OrderInfo orderInfo = new OrderInfo();
        GoodsInfo goodsInfo1 = new GoodsInfo();
        GoodsInfo goodsInfo2 = new GoodsInfo();

        goodsInfo1.setId(4);
        goodsInfo1.setNumber(3);
        goodsInfo2.setId(5);
        goodsInfo2.setNumber(6);

        orderInfo.setGoods(Arrays.asList(goodsInfo1, goodsInfo2));

        HttpResponse response = doHttpRequest("/api/v1/order",  orderInfo, loginResponse.getCookie(),"POST");
        assertEquals(HttpStatus.GONE.value(), response.code);

        // 确保扣库存成功的回滚了
        canCreateOrder();
    }

    private RpcOrderGoods mockRpcOderGoods(long orderId,
                                           long userId,
                                           long goodsId,
                                           long shopId,
                                           int number,
                                           DataStatus status) {
        RpcOrderGoods orderGoods = new RpcOrderGoods();
        Order order = new Order();
        GoodsInfo goodsInfo = new GoodsInfo();

        goodsInfo.setId(goodsId);
        goodsInfo.setNumber(number);

        order.setId(orderId);
        order.setUserId(userId);
        order.setShopId(shopId);
        order.setStatus(status.getName());

        orderGoods.setGoods(Arrays.asList(goodsInfo));
        orderGoods.setOrder(order);
        return orderGoods;
    }
}
