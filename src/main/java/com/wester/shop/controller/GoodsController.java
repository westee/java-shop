package com.wester.shop.controller;

import com.wester.shop.entity.Response;
import com.wester.shop.exceptions.HttpException;
import com.wester.shop.generate.Goods;
import com.wester.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class GoodsController {
    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("goods")
    public Response<Goods> addGoods(@RequestBody Goods goods, HttpServletResponse response) {
        clean(goods);
        response.setStatus(HttpServletResponse.SC_CREATED);
        try{
            return Response.of(goodsService.insertGoods(goods));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(e.getMessage(), null);
        }
    }

    @DeleteMapping("goods/{id}")
    public void deleteGoods(@PathVariable("id") long id) {
        try {
            goodsService.deleteGoods(id);
        } catch (HttpException e) {
            Response.of(e.getMessage(), null);
        }
    }

    @GetMapping("goods")
    public List<Goods> getGoodsByGoodsId(@RequestParam(name = "shopId", required = false) Integer shopId,
                                         @RequestParam(name = "pageSize", required = false,defaultValue = "10") int pageSize,
                                         @RequestParam(name = "pageNum",required = false, defaultValue = "1") int pageNum,
                                         @RequestParam(name ="goodsId", required = false) Optional<Integer> goodsId) {

        if(goodsId.isEmpty()) {
            return goodsService.getGoods(pageNum, pageSize, shopId);
        } else {
            return goodsService.getGoods(goodsId.get());
        }
    }

    private void clean(Goods goods) {
        goods.setId(null);
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
    }
}
