package com.wester.shop.controller;

import com.wester.api.data.DataStatus;
import com.wester.api.data.OrderInfo;
import com.wester.api.data.PageOrderResponse;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.exceptions.HttpException;
import com.wester.api.generate.Order;
import com.wester.shop.data.OrderWithShopAndGoodsList;
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
        try {
            return Response.of(orderService.createOrder(orderInfo, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(e.getMessage(), null);
        }
    }

    @GetMapping("order/{orderId}")
    public Response<RpcOrderGoods> getOrder(@PathVariable("orderId") long orderId) {
        return Response.of(orderService.getOrderById(orderId));
    }

    @GetMapping("order")
    public PageOrderResponse<OrderWithShopAndGoodsList> getOrderList(@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                                     @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
                                                                     @RequestParam(name = "status", required = false) String status) {
        if (status != null && DataStatus.fromStatus(status) == null) {
            throw HttpException.badRequest("非法status: " + status);
        }
        return orderService.getOrderList(pageSize, pageNum, DataStatus.fromStatus(status));
    }

    @PatchMapping("order/{orderId}")
    public RpcOrderGoods getOrder(@RequestBody Order order,
                                  @PathVariable long orderId) {
        return orderService.updateOrderByOrderId(orderId, order);
    }

    @DeleteMapping("order/{orderId}")
    public RpcOrderGoods deleteOrderBuOrderId(@PathVariable long orderId) {
        return orderService.deleteOrderByOrderId(orderId);
    }
}
