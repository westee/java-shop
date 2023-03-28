package com.wester.order.service;

import com.wester.api.data.GoodsInfo;
import com.wester.api.data.OrderInfo;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.generate.Order;
import com.wester.api.rpc.OrderRpcService;
import com.wester.order.generate.OrderGoodsMapper;
import com.wester.order.generate.OrderMapper;
import com.wester.order.mapper.MyOrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.function.BooleanSupplier;

@DubboService
public class OrderServiceImpl implements OrderRpcService {

    private final MyOrderMapper myOrderMapper;
    private final OrderMapper orderMapper;
    private final OrderGoodsMapper orderGoodsMapper;

    @Autowired
    public OrderServiceImpl(MyOrderMapper myOrderMapper, OrderMapper orderMapper,OrderGoodsMapper orderGoodsMapper) {
        this.myOrderMapper = myOrderMapper;
        this.orderMapper = orderMapper;
        this.orderGoodsMapper = orderGoodsMapper;
    }

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        insertOrder(order);
        orderInfo.setOrderId(order.getId());
        myOrderMapper.insertOrders(orderInfo);
        return order;
    }

    @Override
    public RpcOrderGoods getOrderById(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        List<GoodsInfo> goodsInfos = myOrderMapper.getGoodsInfos(orderId);
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        rpcOrderGoods.setOrder(order);
        rpcOrderGoods.setGoods(goodsInfos);
        return rpcOrderGoods;
    }

    private void insertOrder(Order order) {
        verifyParams(() -> order.getAddress() == null , "地址不能为空");
        verifyParams(() -> order.getUserId() == null , "不能为空");
        verifyParams(() -> order.getTotalPrice() == null , "不能为空");

        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        orderMapper.insert(order);
    }

    private void verifyParams(BooleanSupplier booleanSupplier, String message) {
        if(booleanSupplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

}
