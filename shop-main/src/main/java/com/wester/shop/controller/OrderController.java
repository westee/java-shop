package com.wester.shop.controller;

import com.wester.api.data.OrderInfo;
import com.wester.api.data.RpcOrderGoods;
import com.wester.shop.entity.OrderResponse;
import com.wester.shop.entity.Response;
import com.wester.shop.service.OrderService;
import com.wester.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/")
public class OrderController {
    OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("order")
    public Response<OrderResponse> placeOrder(@RequestBody OrderInfo orderInfo, HttpServletResponse response) {
         return Response.of(orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId()));
    }

    @GetMapping("order/{orderId}")
    public Response<RpcOrderGoods> getOrder(@PathVariable("orderId") long orderId) {
         return Response.of(orderService.getOrderById(orderId));
    }

//    @RequestMapping("testRpc")
//    public String testRpc() {
//        orderService.placeOrder();
//        return "";
//    }
}
