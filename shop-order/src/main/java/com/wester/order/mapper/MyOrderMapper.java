package com.wester.order.mapper;

import com.wester.api.data.GoodsInfo;
import com.wester.api.data.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyOrderMapper {
    void insertOrders(OrderInfo orderInfo);

    List<GoodsInfo> getGoodsInfos(long orderId);
}
