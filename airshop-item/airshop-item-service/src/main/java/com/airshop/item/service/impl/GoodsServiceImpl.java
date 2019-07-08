package com.airshop.item.service.impl;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.mapper.BrandMapper;
import com.airshop.item.mapper.SpuMapper;
import com.airshop.item.pojo.Brand;
import com.airshop.item.pojo.Spu;
import com.airshop.item.service.CategoryService;
import com.airshop.item.service.GoodsService;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<SpuBO> querySpuByPageAndSort(SpuQueryByPageParameter pageParameter) {

        PageHelper.startPage(pageParameter.getPage(), Math.min(pageParameter.getRows(), 100));

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        Optional.ofNullable(pageParameter.getSaleable()).ifPresent(
                saleable->criteria.orEqualTo("saleable", saleable));

        if (StringUtils.isNotBlank(pageParameter.getKey())){
            criteria.andLike("title", "%"+pageParameter.getKey()+"%");
        }

        Page<Spu> pageInfo = (Page<Spu>)this.spuMapper.selectByExample(example);

        List<SpuBO> list = pageInfo.getResult().stream().map(spu -> {

            SpuBO spuBO = new SpuBO();
            BeanUtils.copyProperties(spu, spuBO);

            List<String> nameList =
                    this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            spuBO.setCname(StringUtils.join(nameList, "/"));
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());

            spuBO.setBname(brand.getName());

            return spuBO;

        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(), list);
    }
}
