package com.wester.api;

import com.wester.api.data.OrderInfo;
import com.wester.api.generate.Order;
import com.wester.api.rpc.OrderRpcService;
import com.wester.order.mapper.MyOrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.ibatis.annotations.Mapper;

import java.util.function.BooleanSupplier;

@DubboService
public class OrderServiceImpl implements OrderRpcService {
    @Mapper
    private MyOrderMapper myOrderMapper;

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        insertOrder(order);
        orderInfo.setOrderId(order.getId());
        myOrderMapper.insertOrders(orderInfo);
        return order;
    }

    private void insertOrder(Order order) {
        verifyParams(() -> order.getAddress() != null , "地址不能为空");
        verifyParams(() -> order.getUserId() != null , "不能为空");
        verifyParams(() -> order.getTotalPrice() != null , "不能为空");
    }

    private void verifyParams(BooleanSupplier booleanSupplier, String message) {
        if(booleanSupplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

}
