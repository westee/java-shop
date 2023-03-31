package com.wester.shop.data;

import com.wester.shop.entity.GoodsWithNumber;
import com.wester.shop.generate.Goods;
import com.wester.api.generate.Order;
import com.wester.shop.generate.Shop;

import java.util.List;

public class OrderWithShopAndGoodsList extends Order {
    Shop shop;
    List<GoodsWithNumber> goods;

    public OrderWithShopAndGoodsList(Order order) {
        this.setId(order.getId());
        this.setUserId(order.getUserId());
        this.setTotalPrice(order.getTotalPrice());
        this.setAddress(order.getAddress());
        this.setExpressCompany(order.getExpressCompany());
        this.setExpressId(order.getExpressId());
        this.setStatus(order.getStatus());
        this.setCreatedAt(order.getCreatedAt());
        this.setUpdatedAt(order.getUpdatedAt());
        this.setShopId(order.getShopId());
    }

    public OrderWithShopAndGoodsList(Shop shop, List<GoodsWithNumber> goods) {
        this.shop = shop;
        this.goods = goods;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<GoodsWithNumber> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsWithNumber> goods) {
        this.goods = goods;
    }
}
