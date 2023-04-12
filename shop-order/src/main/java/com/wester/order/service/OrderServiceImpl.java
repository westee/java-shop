package com.wester.order.service;

import com.github.pagehelper.PageHelper;
import com.wester.api.data.*;
import com.wester.api.exceptions.HttpException;
import com.wester.api.generate.Order;
import com.wester.api.generate.OrderExample;
import com.wester.api.generate.OrderGoods;
import com.wester.api.generate.OrderGoodsExample;
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
        order.setStatus(DataStatus.PENDING.getName());
        insertOrder(order);
        orderInfo.setOrderId(order.getId());
        myOrderMapper.insertOrders(orderInfo);
        return order;
    }

    @Override
    public RpcOrderGoods getOrderById(long orderId, long userId) {

        Order order = orderMapper.selectByPrimaryKey(orderId);
//        if(order == null) {
//            throw HttpException.notFound("订单未找到");
//        }
//
//        if(order.getUserId() != userId) {
//            throw HttpException.forbidden("没有权限");
//        }
        if (order == null) {
            return null;
        }

        List<GoodsInfo> goodsInfos = myOrderMapper.getGoodsInfos(orderId);
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        rpcOrderGoods.setOrder(order);
        rpcOrderGoods.setGoods(goodsInfos);
        return rpcOrderGoods;
    }

    @Override
    public RpcOrderGoods deleteOrderById(long orderId, long userId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order == null) {
           throw HttpException.notFound("订单未找到");
        }

        if(order.getUserId() != userId) {
            throw HttpException.forbidden("没有权限");
        }

        List<GoodsInfo> goodsInfos = myOrderMapper.getGoodsInfos(orderId);

        order.setStatus(DataStatus.DELETED.getName());
        order.setUpdatedAt(new Date());
        orderMapper.updateByPrimaryKey(order);

        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        rpcOrderGoods.setGoods(goodsInfos);
        rpcOrderGoods.setOrder(order);
        return rpcOrderGoods;
    }

    @Override
    public RpcOrderGoods updateOrderById(long orderId, Order order) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andIdEqualTo(orderId);
        order.setId(orderId);
        orderMapper.updateByExampleSelective(order, orderExample);

        List<GoodsInfo> goodsInfos = myOrderMapper.getGoodsInfos(orderId);
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        rpcOrderGoods.setGoods(goodsInfos);
        rpcOrderGoods.setOrder(order);
        return rpcOrderGoods;
    }

    @Override
    public PageOrderResponse<Order> getOrdersByUserId(int pageNum, int pageSize, DataStatus status, long userId) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdEqualTo(userId);
        if(status != null) {
            example.createCriteria().andStatusEqualTo(status.getName());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectByExample(example);

        long totalNum = orderMapper.countByExample(example);
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;

        PageOrderResponse<Order> pageOrderResponse = new PageOrderResponse<>();
        pageOrderResponse.setData(orders);
        pageOrderResponse.setPageNum(pageNum);
        pageOrderResponse.setPageSize(pageSize);
        pageOrderResponse.setTotalPage(totalPage);
        return pageOrderResponse;
    }

    @Override
    public List<OrderGoods> getGoodsIdsByOrderId(Long orderId) {
        OrderGoodsExample orderGoodsExample = new OrderGoodsExample();
        orderGoodsExample.createCriteria().andOrderIdEqualTo(orderId);
        return orderGoodsMapper.selectByExample(orderGoodsExample);
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
