package com.airshop.service;

import com.airshop.bo.SearchRO;
import com.airshop.item.pojo.Spu;
import com.airshop.pojo.Goods;
import com.airshop.vo.SearchResult;

import java.io.IOException;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 12:54
 */
public interface SearchService {

    Goods buildGoods(Spu spu) throws IOException;

    SearchResult search(SearchRO searchRO);

    void createIndex(Long spuId) throws IOException;

    void deleteIndex(Long spuId);
}
