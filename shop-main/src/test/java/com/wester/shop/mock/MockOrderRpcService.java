package com.wester.shop.mock;

import com.wester.api.data.DataStatus;
import com.wester.api.data.OrderInfo;
import com.wester.api.data.PageOrderResponse;
import com.wester.api.data.RpcOrderGoods;
import com.wester.api.generate.Order;
import com.wester.api.generate.OrderGoods;
import com.wester.api.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.mockito.Mock;

import java.util.List;

@DubboService
public class MockOrderRpcService implements OrderRpcService {
    @Mock
    public OrderRpcService orderRpcService;

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        return orderRpcService.placeOrder(orderInfo, order);
    }

    @Override
    public RpcOrderGoods getOrderById(long orderId, long userId) {
        return orderRpcService.getOrderById(orderId, userId);
    }

    @Override
    public RpcOrderGoods deleteOrderById(long orderId, long userId) {
        return orderRpcService.deleteOrderById(orderId, userId);
    }

    @Override
    public RpcOrderGoods updateOrderById(long orderId, Order order) {
        return orderRpcService.updateOrderById(orderId, order);
    }

    @Override
    public PageOrderResponse<Order> getOrdersByUserId(int pageNum, int pageSize, DataStatus status, long userId) {
        return null;
    }

    @Override
    public List<OrderGoods> getGoodsIdsByOrderId(Long orderId) {
        return null;
    }
}
