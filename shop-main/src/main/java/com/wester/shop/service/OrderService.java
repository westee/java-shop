package com.wester.shop.service;

import com.wester.api.data.GoodsInfo;
import com.wester.api.data.OrderInfo;
import com.wester.api.generate.Order;
import com.wester.api.rpc.OrderRpcService;
import com.wester.shop.entity.GoodsWithNumber;
import com.wester.shop.entity.OrderResponse;
import com.wester.shop.exceptions.HttpException;
import com.wester.shop.generate.Goods;
import com.wester.shop.generate.ShopMapper;
import com.wester.shop.generate.UserMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @DubboReference
    private OrderRpcService orderRpcService;

    GoodsService goodsService;

    UserMapper userMapper;

    ShopMapper shopMapper;

    @Autowired
    public OrderService(GoodsService goodsService, UserMapper userMapper, ShopMapper shopMapper) {
        this.goodsService = goodsService;
        this.userMapper = userMapper;
        this.shopMapper = shopMapper;
    }

    public OrderResponse createOrder(OrderInfo orderInfo, Long userId) {
        List<Long> goodsIds = orderInfo.getGoods().stream().map(GoodsInfo::getId).collect(Collectors.toList());

        // goodsId , goods
        Map<Long, Goods> goodsToMapByGoodsIds = goodsService.getGoodsToMapByGoodsIds(goodsIds);
        Order order = new Order();
        order.setUserId(userId);
        order.setUserId(userId);
        order.setTotalPrice(calcTotalPrice(orderInfo.getGoods(), goodsToMapByGoodsIds));
        order.setAddress(userMapper.selectByPrimaryKey(userId).getAddress());

        Order createdOrder = orderRpcService.placeOrder(orderInfo, order);

        OrderResponse orderResponse = new OrderResponse(createdOrder);
        orderResponse.setShop(shopMapper.selectByPrimaryKey(new ArrayList<>(goodsToMapByGoodsIds.values()).get(0).getShopId()));
        orderResponse.setGoods(orderInfo.getGoods().stream().
                map(goodsInfo -> generateGoodsWithNumber(goodsInfo, goodsToMapByGoodsIds)).collect(Collectors.toList()));
        return orderResponse;
    }

    private GoodsWithNumber generateGoodsWithNumber(GoodsInfo goodsInfo, Map<Long, Goods> goodsToMapByGoodsIds) {
        // 从提交的购买信息中获取商品id，从goodsId -> goods的映射中找到指定商品
        GoodsWithNumber goodsWithNumber = new GoodsWithNumber(goodsToMapByGoodsIds.get(goodsInfo.getId()));
        goodsWithNumber.setNumber(goodsInfo.getNumber());
        return goodsWithNumber;
    }

    private BigDecimal calcTotalPrice(List<GoodsInfo> goodsInfos, Map<Long, Goods> goodsToMapByGoodsIds) {
        BigDecimal n = new BigDecimal(0);
        for (GoodsInfo goodsInfo : goodsInfos) {
            if (goodsInfo.getNumber() == 0) {
                throw HttpException.badRequest("id不合法");
            }
            Goods goods = goodsToMapByGoodsIds.get(goodsInfo.getId());
            n = goods.getPrice().multiply(new BigDecimal(goodsInfo.getNumber())).add(n);
        }

        return n;
    }
}
