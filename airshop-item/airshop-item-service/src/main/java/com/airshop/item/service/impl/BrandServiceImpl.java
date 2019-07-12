package com.airshop.item.service.impl;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.mapper.BrandMapper;
import com.airshop.item.pojo.Brand;
import com.airshop.item.service.BrandService;
import com.airshop.parameter.pojo.BrandQueryByPageParameter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.provider.ExampleProvider;

import java.util.List;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:29
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(BrandQueryByPageParameter pageParameter) {
        PageHelper.startPage(pageParameter.getPage(), pageParameter.getRows());

        Example example = new Example(Brand.class);
        Optional.ofNullable(pageParameter.getSortBy()).filter(x-> StringUtils.isNotBlank(x))
                .ifPresent(x-> example.setOrderByClause(x+ (pageParameter.getDesc()? " DESC":" ASC")));

        Optional.ofNullable(pageParameter.getKey()).filter(x-> StringUtils.isNotBlank(x))
                .ifPresent(x-> example.createCriteria().orLike("name", x+"%")
                .orEqualTo("letter", x.toUpperCase()));

        List<Brand> brandList = brandMapper.selectByExample(example);

        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Transactional
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);

        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBrand(Brand brand, List<Long> categories) {
        deleteByBrandIdInCategoryBrand(brand.getId());

        this.brandMapper.updateByPrimaryKeySelective(brand);

        for (Long categoryId : categories) {
            this.brandMapper.insertCategoryBrand(categoryId, brand.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBrand(long id) {
        this.brandMapper.deleteByPrimaryKey(id);

        this.brandMapper.deleteByBrandIdInCategoryBrand(id);
    }

    @Override
    public List<Brand> queryBrandByCategoryId(Long cid) {
        return this.brandMapper.queryBrandByCategoryId(cid);
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        return this.brandMapper.selectByIdList(ids);
    }

    private void deleteByBrandIdInCategoryBrand(Long brandId) {
        this.brandMapper.deleteByBrandIdInCategoryBrand(brandId);
    }
}
