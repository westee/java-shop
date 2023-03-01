package com.wester.shop.controller;

import com.wester.shop.data.PageResponse;
import com.wester.shop.entity.Response;
import com.wester.shop.exceptions.HttpException;
import com.wester.shop.generate.Shop;
import com.wester.shop.service.ShopService;
import com.wester.shop.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ShopController {
    private ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shop")
    public PageResponse<Shop> getShop(@RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                      @RequestParam(name = "shopId", required = false) Optional<Long> shopId) {
        if (shopId.isPresent()) {
            return shopService.getShopByShopId(shopId.get());
        } else {
            return shopService.getShopByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);
        }
    }

    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop, HttpServletResponse response) {
        Response<Shop> ret = Response.of(shopService.createShop(shop, UserContext.getCurrentUser().getId()));
        response.setStatus(HttpStatus.CREATED.value());
        return ret;
    }

    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long id,
                                     @RequestBody Shop shop,
                                     HttpServletResponse response) {
        shop.setId(id);
        try {
            return Response.of(shopService.updateShop(shop, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(e.getMessage(), null);
        }
    }

    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(@PathVariable("id") Long shopId, HttpServletResponse response) {
        try {
            return Response.of(shopService.deleteShop(shopId, UserContext.getCurrentUser().getId()));
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.of(e.getMessage(), null);
        }
    }
}
