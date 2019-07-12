package com.airshop.item.service;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.pojo.Brand;
import com.airshop.parameter.pojo.BrandQueryByPageParameter;

import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:28
 */
public interface BrandService {

    PageResult<Brand> queryBrandByPage(
            BrandQueryByPageParameter brandQueryByPageParameter
    );

    void saveBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> categories);

    void deleteBrand(long id);

    List<Brand> queryBrandByCategoryId(Long cid);

    List<Brand> queryBrandByIds(List<Long> ids);
}
