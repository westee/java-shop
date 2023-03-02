package com.wester.shop.service;

import com.wester.shop.controller.ShoppingCartController;
import com.wester.shop.dao.ShoppingCartQueryMapper;
import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.GoodsStatus;
import com.wester.shop.entity.ShoppingCartData;
import com.wester.shop.entity.ShoppingCartGoods;
import com.wester.shop.generate.Goods;
import com.wester.shop.generate.ShoppingCart;
import com.wester.shop.generate.ShoppingCartMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {
    private ShoppingCartMapper shoppingCartMapper;
    private ShoppingCartQueryMapper shoppingCartQueryMapper;
    private GoodsService goodsService;
    private SqlSessionFactory sqlSessionFactory;

    public ShoppingCartService(ShoppingCartMapper shoppingCartMapper, ShoppingCartQueryMapper shoppingCartQueryMapper,
                               GoodsService goodsService, SqlSessionFactory sqlSessionFactory) {
        this.goodsService = goodsService;
        this.sqlSessionFactory = sqlSessionFactory;
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartQueryMapper = shoppingCartQueryMapper;
    }

    public PageResponse<ShoppingCartData> getShoppingCartOfUser(int pageNum, int pageSize, Long userId) {
        int count = shoppingCartQueryMapper.countShopsInUserShoppingCart(userId);
        int offset = (pageNum - 1) * pageSize;
        System.out.println(offset);
        List<ShoppingCartData> shoppingCartData = shoppingCartQueryMapper.selectShoppingCartDataByUserId(userId, pageSize, offset)
                .stream()
                .collect(Collectors.groupingBy(shoppingCartData1 -> shoppingCartData1.getShop().getId()))
                .values()
                .stream()
                .map(this::merge)
                .collect(Collectors.toList());
        return PageResponse.pageData(pageNum, pageSize, count, shoppingCartData);
    }

    private ShoppingCartData merge(List<ShoppingCartData> shoppingCartData) {
        ShoppingCartData result = new ShoppingCartData();
        result.setShop(shoppingCartData.get(0).getShop());
        List<ShoppingCartGoods> goods = shoppingCartData.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        result.setGoods(goods);
        return result;

    }

    public void deleteShoppingCart(long goodsId, Long userId) {
        shoppingCartQueryMapper.deleteShoppingCart(goodsId, userId);
    }

    public ShoppingCartData addGoodsToShoppingCart(ShoppingCartController.AddToShoppingCartRequest request, Long userId) {
        List<Long> goodsIds = request.getGoods().stream()
                .map(ShoppingCartController.AddToShoppingCartItem::getId)
                .collect(Collectors.toList());

        Map<Long, Goods> goodsToMapByGoodsIds = goodsService.getGoodsToMapByGoodsIds(goodsIds);

        List<ShoppingCart> collect = request.getGoods().stream().map(item -> makeShoppingCartRow(item, goodsToMapByGoodsIds)).collect(Collectors.toList());


        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ShoppingCartMapper mapper = sqlSession.getMapper(ShoppingCartMapper.class);
            collect.forEach(mapper::insert);
            sqlSession.commit();
        }

        return getLatestShoppingCartDataByUserIdShopId(new ArrayList<>(goodsToMapByGoodsIds.values()).get(0).getShopId(), userId);
    }

    private ShoppingCartData getLatestShoppingCartDataByUserIdShopId(long shopId, long userId) {
        List<ShoppingCartData> result = shoppingCartQueryMapper.selectAllShoppingCartDataByUserId(userId, shopId);
        return merge(result);
    }

    private ShoppingCart makeShoppingCartRow(ShoppingCartController.AddToShoppingCartItem goodsItem, Map<Long, Goods> goodsToMapByGoodsIds) {
        Goods goods = goodsToMapByGoodsIds.get(goodsItem.getId());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setNumber(goodsItem.getNumber());
        shoppingCart.setCreatedAt(new Date());
        shoppingCart.setUpdatedAt(new Date());
        shoppingCart.setStatus(GoodsStatus.OK.getName());

        shoppingCart.setGoodsId(goodsItem.getId());
        shoppingCart.setShopId(goods.getShopId());
        shoppingCart.setUserId(UserContext.getCurrentUser().getId());

        return shoppingCart;
    }


}
