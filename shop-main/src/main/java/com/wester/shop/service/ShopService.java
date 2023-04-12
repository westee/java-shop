package com.wester.shop.service;

import com.github.pagehelper.PageHelper;
import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.GoodsStatus;
import com.wester.api.exceptions.HttpException;
import com.wester.shop.generate.Shop;
import com.wester.shop.generate.ShopExample;
import com.wester.shop.generate.ShopMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ShopService {
    ShopMapper shopMapper;

    public ShopService(ShopMapper mapper) {
        this.shopMapper = mapper;
    }

    public PageResponse<Shop> getShopsByUserId(Long userId, Integer pageNum, Integer pageSize) {
        ShopExample shopExample = new ShopExample();
        shopExample.createCriteria().andOwnerUserIdEqualTo(userId).andStatusEqualTo(GoodsStatus.OK.getName());
        long count = shopMapper.countByExample(shopExample);
        PageHelper.startPage(pageNum, pageSize);
        List<Shop> shops = shopMapper.selectByExample(shopExample);
        return PageResponse.pageData(pageNum, pageSize, count, shops);
    }

    public Shop createShop(Shop shop, Long userId) {
        shop.setOwnerUserId(userId);
        shop.setId(null);
        shop.setStatus(GoodsStatus.OK.getName());
        shop.setUpdatedAt(new Date());
        shop.setCreatedAt(new Date());
        int shopId = shopMapper.insert(shop);
        shop.setId((long) shopId);
        return shop;
    }

    public Shop updateShop(Shop shop, Long userId) {
        Shop shopResult = shopMapper.selectByPrimaryKey(shop.getId());
        if (Objects.equals(shopResult, null)) {
            throw HttpException.forbidden("参数不合法");
        }
        Long ownerUserId = shopResult.getOwnerUserId();
        if (Objects.equals(ownerUserId, userId)) {
            shop.setUpdatedAt(new Date());
            shop.setCreatedAt(new Date());
            shopMapper.updateByPrimaryKeySelective(shop);
            return shop;
        } else {
            throw HttpException.forbidden("拒绝访问");
        }
    }

    public Shop deleteShop(Long shopId, Long userId) {
        Shop shop = shopMapper.selectByPrimaryKey(shopId);
        if (Objects.equals(shop, null)) {
            throw HttpException.forbidden("参数不合法");
        }
        Long ownerUserId = shop.getOwnerUserId();
        if (Objects.equals(ownerUserId, userId)) {
            shop.setStatus(GoodsStatus.DELETED.getName());
            shop.setUpdatedAt(new Date());
            shop.setCreatedAt(new Date());
            shopMapper.updateByPrimaryKeySelective(shop);
            return shop;
        } else {
            throw HttpException.forbidden("拒绝访问");
        }
    }

    public Shop getShopByShopId(long shopId) {
        return shopMapper.selectByPrimaryKey(shopId);
    }
}
