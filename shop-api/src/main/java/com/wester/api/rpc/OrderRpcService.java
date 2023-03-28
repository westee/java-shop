package com.wester.api.rpc;

import com.wester.api.data.OrderInfo;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.generate.Order;

public interface OrderRpcService {
    Order placeOrder(OrderInfo orderInfo, Order order);

    RpcOrderGoods getOrderById(long orderId);

    boolean isOrderBelongToUser(Long id, long orderId);

    boolean isOrderExist(long orderId);
}