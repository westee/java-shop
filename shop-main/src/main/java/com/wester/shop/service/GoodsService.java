package com.wester.shop.service;

import com.github.pagehelper.PageHelper;
import com.wester.api.data.DataStatus;
import com.wester.api.exceptions.HttpException;
import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.GoodsStatus;
import com.wester.shop.entity.Response;
import com.wester.shop.generate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    private final GoodsMapper goodsMapper;
    private final ShopMapper shopMapper;

    @Autowired
    public GoodsService(GoodsMapper goodsMapper, ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
        this.goodsMapper = goodsMapper;
    }

    public Goods deleteGoods(long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if(goods == null) {
            throw HttpException.forbidden("参数非法");
        }
        Shop shop = shopMapper.selectByPrimaryKey(goods.getShopId());
        if (Objects.equals(UserContext.getCurrentUser().getId(), shop.getOwnerUserId())) {
            goods.setStatus(GoodsStatus.DELETED.getName());
            goodsMapper.updateByPrimaryKey(goods);
            return goods;
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
            goods.setStatus(DataStatus.OK.getName());
            long id = goodsMapper.insert(goods);
            goods.setId(id);
            return goods;
        }
        throw HttpException.forbidden("无权访问");
    }

    public PageResponse<Goods> getGoods(int pageNum, int pageSize, long shopId) {

        GoodsExample goodsExample = new GoodsExample();
        goodsExample.createCriteria().andShopIdEqualTo(shopId).andStatusEqualTo(GoodsStatus.OK.getName());

        long totalGoods = goodsMapper.countByExample(goodsExample);
        long totalPage = totalGoods % pageSize == 0 ? totalGoods / pageSize : totalGoods / pageSize + 1;

        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goods = goodsMapper.selectByExample(goodsExample);
        PageResponse<Goods> goodsPageResponse = new PageResponse<>();
        goodsPageResponse.setData(goods);
        goodsPageResponse.setPageNum(pageNum);
        goodsPageResponse.setPageSize(pageSize);
        goodsPageResponse.setTotalPage(totalPage);
        return goodsPageResponse;
    }

    public Goods getGoods(long goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    public Map<Long, Goods> getGoodsToMapByGoodsIds(List<Long> goodsIds) {
        GoodsExample example = new GoodsExample();
        example.createCriteria().andIdIn(goodsIds);
        Map<Long, Goods> collect = goodsMapper.selectByExample(example).stream().collect(Collectors.toMap(Goods::getId, x -> x));
        return collect;
    }

    public Response<Goods> updateGoodsByGoodsId(long goodsId, Goods goods) {
        goods.setId(goodsId);
        goodsMapper.updateByPrimaryKeySelective(goods);
        return Response.of(goods);
    }
}
