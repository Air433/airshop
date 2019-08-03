package com.airshop.cart.service.impl;

import com.airshop.auth.entity.UserInfo;
import com.airshop.cart.interceptor.LoginInterceptor;
import com.airshop.cart.pojo.Cart;
import com.airshop.cart.service.CartService;
import com.airshop.item.client.GoodsClient;
import com.airshop.item.pojo.Sku;
import com.airshop.myexception.AirExceptionEnum;
import com.airshop.myexception.MyException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author ouyanggang
 * @Date 2019/8/1 - 11:15
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    private static String KEY_PREFIX = "airshop:cart:uid:";

    @Override
    public void addCart(Cart cart) {
        //1.获取用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //2.Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        //3.获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(key);
        //4.查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean hasKey = hashOperations.hasKey(skuId.toString());

        if (hasKey){
            String json = hashOperations.get(skuId.toString()).toString();
            cart = JSON.parseObject(json, Cart.class);
            //6.修改购物车数量
            cart.setNum(cart.getNum() + num);
        }else {
            cart.setUserId(userInfo.getId());
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        };
        hashOperations.put(cart.getSkuId().toString(), JSON.toJSONString(cart));
    }

    @Override
    public List<Cart> queryCartList() {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();

        if (!this.stringRedisTemplate.hasKey(key)){
            return Collections.EMPTY_LIST;
        }

        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        List<Object> carts = hashOps.values();
        if (CollectionUtils.isEmpty(carts)){
            return Collections.EMPTY_LIST;
        }

        return carts.stream().map(c->JSON.parseObject(c.toString(), Cart.class)).collect(Collectors.toList());
    }

    @Override
    public void insertCarts(List<Cart> cartList) {
        //1.获取用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //2.Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        //3.获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(key);

        Map<Long, Cart> cartMap = cartList.stream().collect(Collectors.toMap(Cart::getSkuId, Function.identity()));

        for (Map.Entry<Long, Cart> entry : cartMap.entrySet()) {
            Long skuId = entry.getKey();
            Cart cart = entry.getValue();
            if (!hashOperations.hasKey(skuId.toString())){
                cart.setUserId(userInfo.getId());
                Sku sku = this.goodsClient.querySkuById(skuId);
                cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(), ",")[0]);
                cart.setPrice(sku.getPrice());
                cart.setTitle(sku.getTitle());
                cart.setOwnSpec(sku.getOwnSpec());
            }
            hashOperations.put(cart.getSkuId().toString(), JSON.toJSONString(cart));
        }
    }

    @Override
    public void updateNum(Long skuId, Integer num) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();

        String key = KEY_PREFIX + userInfo.getId();

        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        if (!hashOps.hasKey(skuId.toString())){
            throw new MyException(AirExceptionEnum.CART_NOT_FOUND);
        }

        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JSON.parseObject(json, Cart.class);
        cart.setNum(num);

        hashOps.put(skuId.toString(), JSON.toJSONString(cart));

    }

    @Override
    public void deleteCart(Long skuId) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();

        String key = KEY_PREFIX + userInfo.getId();

        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);

        hashOps.delete(skuId.toString());
    }
}
