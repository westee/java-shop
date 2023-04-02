package com.wester.api.rpc;

import com.wester.api.data.DataStatus;
import com.wester.api.data.OrderInfo;
import com.wester.api.data.PageOrderResponse;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.generate.Order;
import com.wester.api.generate.OrderGoods;

import java.util.List;

public interface OrderRpcService {
    Order placeOrder(OrderInfo orderInfo, Order order);

    RpcOrderGoods getOrderById(long orderId, long userId);

    RpcOrderGoods deleteOrderById(long orderId, long userId);

    RpcOrderGoods updateOrderById(long orderId, Order order);

    PageOrderResponse<Order> getOrdersByUserId(int pageNum, int pageSize, DataStatus status, long userId);

    List<OrderGoods> getGoodsIdsByOrderId(Long orderId);
}