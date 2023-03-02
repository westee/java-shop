package com.wester.shop.service;

import com.github.pagehelper.PageHelper;
import com.wester.shop.entity.GoodsStatus;
import com.wester.shop.exceptions.HttpException;
import com.wester.shop.generate.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    private final GoodsMapper goodsMapper;
    private final ShopMapper shopMapper;

    public GoodsService(GoodsMapper goodsMapper, ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
        this.goodsMapper = goodsMapper;
    }

    public void deleteGoods(long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        if (Objects.equals(UserContext.getCurrentUser().getId(), shop.getOwnerUserId())) {
            goods.setStatus(GoodsStatus.DELETED.getName());
            goodsMapper.updateByPrimaryKey(goods);
        } else {
            throw HttpException.forbidden("无权访问");
        }
    }

    public Goods insertGoods(Goods goods) {
        Long shopId = goods.getShopId();
        Shop shop = shopMapper.selectByPrimaryKey(shopId);
        if (shop == null) {
            throw new HttpException(403, "无权访问");
        }
        if (Objects.equals(UserContext.getCurrentUser().getId(), shop.getOwnerUserId())) {
            long id = goodsMapper.insert(goods);
            goods.setId(id);
            return goods;
        }
        throw HttpException.forbidden("无权访问");
    }

    public List<Goods> getGoods(int pageNum, int pageSize, long shopId) {
        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andShopIdEqualTo(shopId).andStatusEqualTo(GoodsStatus.OK.getName());
        PageHelper.startPage(pageNum, pageSize);
        return goodsMapper.selectByExample(goodsExample);
    }

    public List<Goods> getGoods(long goodsId) {
        return Arrays.asList(goodsMapper.selectByPrimaryKey(goodsId));
    }

    public Map<Long, Goods> getGoodsToMapByGoodsIds(List<Long> goodsIds) {
        GoodsExample example = new GoodsExample();
        example.createCriteria().andIdIn(goodsIds);
        Map<Long, Goods> collect = goodsMapper.selectByExample(example).stream().collect(Collectors.toMap(Goods::getId, x -> x));
        return collect;
    }
}
