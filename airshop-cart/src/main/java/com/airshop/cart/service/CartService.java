package com.airshop.cart.service;

import com.airshop.cart.pojo.Cart;

import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 11:14
 */
public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCartList();

    void insertCarts(List<Cart> cartList);

    void updateNum(Long skuId, Integer num);

    void deleteCart(Long skuId);
}
