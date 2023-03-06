package com.wester.shop.dao;

import com.wester.api.data.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsStockMapper {
    int deductStock(OrderInfo orderInfo);
}
