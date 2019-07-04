package com.airshop.item.service;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.pojo.Brand;
import com.airshop.parameter.pojo.BrandQueryByPageParameter;

/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:28
 */
public interface BrandService {

    PageResult<Brand> queryBrandByPage(
            BrandQueryByPageParameter brandQueryByPageParameter
    );
}
