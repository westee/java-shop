package com.wester.shop.controller;

import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.Response;
import com.wester.shop.entity.ShoppingCartData;
import com.wester.shop.service.ShoppingCartService;
import com.wester.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ShoppingCartController {
    ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("shoppingCart")
    public PageResponse<ShoppingCartData> getShoppingCart(
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum) {
        Long userId = UserContext.getCurrentUser().getId();
        return shoppingCartService.getShoppingCartOfUser(pageNum, pageSize, userId);
    }

    @PostMapping("shoppingCart")
    public Response<ShoppingCartData> getShoppingCart(@RequestBody AddToShoppingCartRequest request) {
        Long userId = UserContext.getCurrentUser().getId();
        return Response.of(shoppingCartService.addGoodsToShoppingCart(request, userId));
    }

    @DeleteMapping("shoppingCart/{goodsId}")
    public void deleteShoppingCart(@PathVariable("goodsId") long goodsId) {
        Long userId = UserContext.getCurrentUser().getId();
        shoppingCartService.deleteShoppingCart(goodsId, userId);
    }

    public static class AddToShoppingCartRequest {
        List<AddToShoppingCartItem> goods;

        public AddToShoppingCartRequest() {
        }

        public List<AddToShoppingCartItem> getGoods() {
            return goods;
        }

        public void setGoods(List<AddToShoppingCartItem> goods) {
            this.goods = goods;
        }
    }

    public static class AddToShoppingCartItem {
        private long id;
        private int number;

        public AddToShoppingCartItem() {
        }

        public long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
