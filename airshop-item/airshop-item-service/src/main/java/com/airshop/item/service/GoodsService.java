package com.airshop.item.service;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.Spu;
import com.airshop.item.pojo.SpuDetail;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;

import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
public interface GoodsService {
    PageResult<SpuBO> querySpuByPageAndSort(SpuQueryByPageParameter pageParameter);

    void saveGoods(SpuBO spuBO);

    SpuDetail querySpuDetailById(Long id);

    List<Sku> querySkuBySpuId(Long id);

    SpuBO queryGoodsById(Long id);

    void updateGoods(SpuBO spuBO);

    void deleteGoods(long spuId);

    List<Sku> querySkuByIds(List<Long> skuIds);

    Sku querySkuById(Long id);
}
