package com.airshop.service.impl;

import com.airshop.client.CategoryClient;
import com.airshop.client.GoodsClient;
import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.Spu;
import com.airshop.item.pojo.SpuDetail;
import com.airshop.pojo.Goods;
import com.airshop.service.SearchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 16:24
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;

    public static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).getBody();

        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId());

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());

        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();

        //过滤规格模板，把所有可搜索的信息保存到Map中
        Map<String,Object> specMap = new HashMap<>();

        String searchable = "searchable";
        String v = "v";
        String k = "k";
        String options = "options";

        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());

            skuMap.put("image", StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(), ",")[0]);
            skuMapList.add(skuMap);
        });
        List<Map<String,Object>> genericSpecs = mapper.readValue(spuDetail.getSpecifications(), new TypeReference<List<Map<String, Object>>>() {});

        genericSpecs.forEach(m->{
            List<Map<String, Object>> params = (List<Map<String, Object>>) m.get("params");
            params.forEach(spe->{
                if ((boolean)spe.get("searchable")){
                    if (spe.get(v)!=null){
                        specMap.put(spe.get(k).toString(), spe.get(v));
                    }else if (spe.get(options)!=null){
                        specMap.put(spe.get(k).toString(), spe.get(options));
                    }
                }
            });
        });

        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(spu.getTitle()+" "+StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuMapList));
        goods.setSpecs(specMap);
        return goods;
    }
}
