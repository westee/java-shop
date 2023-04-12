package com.wester.shop.controller;

import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.Response;
import com.wester.shop.generate.Shop;
import com.wester.shop.service.ShopService;
import com.wester.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class ShopController {
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shop")
    public PageResponse<Shop> getShop(@RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        return shopService.getShopsByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);
    }

    @GetMapping("/shop/{shopId}")
    public Response<Shop> getShopByShopId(@PathVariable(name = "shopId", required = false) long shopId) {
        return Response.of("ok", shopService.getShopByShopId(shopId));
    }

    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop, HttpServletResponse response) {
        Response<Shop> ret = Response.of(shopService.createShop(shop, UserContext.getCurrentUser().getId()));
        response.setStatus(HttpStatus.CREATED.value());
        return ret;
    }

    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long id,
                                     @RequestBody Shop shop) {
        shop.setId(id);
        return Response.of(shopService.updateShop(shop, UserContext.getCurrentUser().getId()));
    }

    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(@PathVariable("id") Long shopId) {
        return Response.of(shopService.deleteShop(shopId, UserContext.getCurrentUser().getId()));

    }
}
