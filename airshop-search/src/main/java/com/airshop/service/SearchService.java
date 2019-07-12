package com.airshop.service;

import com.airshop.item.pojo.Spu;
import com.airshop.pojo.Goods;

import java.io.IOException;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 12:54
 */
public interface SearchService {

    Goods buildGoods(Spu spu) throws IOException;
}
