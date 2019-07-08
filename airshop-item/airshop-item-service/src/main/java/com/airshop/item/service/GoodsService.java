package com.airshop.item.service;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
public interface GoodsService {
    PageResult<SpuBO> querySpuByPageAndSort(SpuQueryByPageParameter pageParameter);
}
