package com.airshop.cart.controller;

import com.airshop.cart.pojo.Cart;
import com.airshop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 11:14
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        List<Cart> carts = this.cartService.queryCartList();
        return ResponseEntity.ok(carts);
    }

    @PostMapping("/insert")
    public ResponseEntity<Void> insertCart(RequestEntity<List<Cart>> requestEntity){
        this.cartService.insertCarts(requestEntity.getBody());
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId")Long skuId, @RequestParam("num")Integer num){
        this.cartService.updateNum(skuId, num);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
