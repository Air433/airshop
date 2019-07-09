package com.airshop.item.service.impl;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.mapper.*;
import com.airshop.item.pojo.*;
import com.airshop.item.service.CategoryService;
import com.airshop.item.service.GoodsService;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    public static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Resource
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGoods(SpuBO spuBO) {

        spuBO.setSaleable(true);
        spuBO.setValid(true);
        spuBO.setCreateTime(new Date());
        spuBO.setLastUpdateTime(spuBO.getCreateTime());
        this.spuMapper.insert(spuBO);

        SpuDetail spuDetail = spuBO.getSpuDetail();
        spuDetail.setSpuId(spuBO.getId());
        log.warn(spuDetail.getSpecifications().length()+"");
        this.spuDetailMapper.insert(spuDetail);

        saveSkuAndStock(spuBO.getSkus(), spuBO.getId());

    }

    @Override
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);

        skus.forEach(sku -> {
            sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        });
        return skus;
    }

    @Override
    public SpuBO queryGoodsById(Long id) {
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spu.getId());

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId", spu.getId());
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        List<Long> skuIdList = skuList.stream().map(sku -> sku.getId()).collect(Collectors.toList());

        List<Stock> stockList = this.stockMapper.selectByIdList(skuIdList);

        Map<Long, Long> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));

        skuList.forEach(sku -> sku.setStock(stockMap.get(sku.getId())));

        SpuBO spuBO = new SpuBO();
        BeanUtils.copyProperties(spu, spuBO);

        spuBO.setSpuDetail(spuDetail);
        spuBO.setSkus(skuList);

        return spuBO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateGoods(SpuBO spuBO) {

        spuBO.setSaleable(true);
        spuBO.setValid(true);
        spuBO.setLastUpdateTime(new Date());

        SpuDetail spuDetail = spuBO.getSpuDetail();
        String oldTemp = this.spuDetailMapper.selectByPrimaryKey(spuBO.getId()).getSpecTemplate();

        if (spuDetail.getSpecTemplate().equals(oldTemp)){
            updateSkuAndStock(spuBO.getSkus(), spuBO.getId(), true);
        }else {

        }

    }

    private void updateSkuAndStock(List<Sku> skus, Long spuId, boolean tag) {

    }

    private void saveSkuAndStock(List<Sku> skus, Long id) {
        for (Sku sku : skus) {
            if (!sku.getEnable()){
                continue;
            }
            sku.setSpuId(id);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }
}
