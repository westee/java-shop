package com.wester.shop.controller;

import com.wester.api.data.OrderInfo;
import com.wester.shop.entity.OrderResponse;
import com.wester.shop.service.OrderService;
import com.wester.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public OrderResponse placeOrder(@RequestBody OrderInfo orderInfo, HttpServletResponse response) {
         return orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId());
    }

//    @RequestMapping("testRpc")
//    public String testRpc() {
//        orderService.placeOrder();
//        return "";
//    }
}
