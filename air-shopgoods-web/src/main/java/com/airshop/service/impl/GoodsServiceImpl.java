package com.airshop.service.impl;

import com.airshop.item.bo.SpuBO;
import com.airshop.item.client.BrandClient;
import com.airshop.item.client.CategoryClient;
import com.airshop.item.client.GoodsClient;
import com.airshop.item.pojo.Brand;
import com.airshop.item.pojo.Category;
import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.SpuDetail;
import com.airshop.service.GoodsService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author ouyanggang
 * @Date 2019/7/16 - 17:28
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;

    @Override
    public Map<String, Object> loadModel(Long spuId) {
        SpuBO spuBO = this.goodsClient.querySpuById(spuId);

        SpuDetail spuDetail = spuBO.getSpuDetail();

        List<Sku> skuList = spuBO.getSkus();

        List<Long> cids = new ArrayList<>();
        cids.add(spuBO.getCid1());
        cids.add(spuBO.getCid2());
        cids.add(spuBO.getCid3());
        List<Category> categoryList = this.categoryClient.queryCategoryByIds(cids).getBody();

        Brand brand = this.brandClient.queryBrandByIds(Collections.singletonList(spuBO.getBrandId())).get(0);

        String allSpecJson = spuDetail.getSpecifications();

        List<Map<String, Object>> allSpecs = JSON.parseObject(allSpecJson, List.class);
        Map<Integer, String> specName = new HashMap<>();
        Map<Integer, Object> specValue = new HashMap<>();
        this.getAllSpecifications(allSpecs, specName, specValue);

        //获取独有规格参数
        String specJson = spuDetail.getSpecTemplate();

        Map<String, String[]> specMap = JSON.parseObject(specJson, Map.class);
        Map<Integer, String> specialParamName = new HashMap<>();
        Map<Integer, String[]> specialParamValue = new HashMap<>();
        this.getSpecialSepc(specMap, specName, specValue, specialParamName, specialParamValue);

        List<Map<String, Object>> groups = this.getGroupsSpec(allSpecs, specName, specValue);

        Map<String, Object> map = new HashMap<>();
        map.put("spu", spuBO);
        map.put("spuDetail", spuDetail);
        map.put("skus", skuList);
        map.put("brand", brand);
        map.put("categories", categoryList);
        map.put("specName", specName);
        map.put("specValue",specValue);
        map.put("groups", groups);
        map.put("specialParamName", specialParamName);
        map.put("specialParamValue", specialParamValue);

        return map;
    }

    private List<Map<String, Object>> getGroupsSpec(List<Map<String, Object>> allSpecs, Map<Integer, String> specName, Map<Integer, Object> specValue) {
        List<Map<String, Object>> groups = new ArrayList<>();
        int i = 0;
        int j = 0;
        String k = "k";
        String idKey = "id";
        String nameKey = "name";
        String valueKey = "value";
        String paramsKey = "params";
        String groupKey = "group";
        for (Map<String, Object> specMap : allSpecs) {
            List<Map<String, Object>> params = (List<Map<String, Object>>) specMap.get(paramsKey);
            List<Map<String, Object>> temp = new ArrayList<>();
            for (Map<String, Object> param : params) {
                for (Map.Entry<Integer, String> entry : specName.entrySet()) {
                    if (entry.getValue().equals(param.get(k).toString())){
                        String value = Optional.ofNullable(specValue.get(entry.getKey())).map(v -> v.toString()).orElse("无");
                        Map<String, Object> temp3 = new HashMap<>(16);
                        temp3.put(idKey, ++j);
                        temp3.put(nameKey, entry.getValue());
                        temp3.put(valueKey, value);
                        temp.add(temp3);
                    }
                }
            }
            Map<String, Object> temp2 = new HashMap<>(16);
            temp2.put(paramsKey, temp);
            temp2.put(idKey, ++i);
            temp2.put(nameKey, specMap.get(groupKey));
            groups.add(temp2);
        }
        return groups;
    }

    private void getSpecialSepc(Map<String, String[]> specMap, Map<Integer, String> specName, Map<Integer, Object> specValue, Map<Integer, String> specialParamName, Map<Integer, String[]> specialParamValue) {
        if (specMap == null){
            return;
        }
        for (Map.Entry<String, String[]> entry : specMap.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<Integer, String> integerStringEntry : specName.entrySet()) {
                if (integerStringEntry.getValue().equals(key)){
                    specialParamName.put(integerStringEntry.getKey(), integerStringEntry.getValue());
                    //因为是放在数组里面，所以要先去除两个方括号，然后再以逗号分割成数组
                    String s = specValue.get(integerStringEntry.getKey()).toString();
                    String result = StringUtils.substring(s, 2, s.length() - 2);
                    specialParamValue.put(integerStringEntry.getKey(), result.split("\",\""));
                }
            }
        }
    }

    private void getAllSpecifications(List<Map<String, Object>> allSpecs, Map<Integer, String> specName, Map<Integer, Object> specValue) {
        String k = "k";
        String v = "v";
        String unit = "unit";
        String numerical = "numerical";
        String options = "options";
        String params = "params";
        int i = 0;
        if (allSpecs != null){
            for (Map<String, Object> spec : allSpecs) {
                List<Map<String, Object>> paramList = (List<Map<String, Object>>) spec.get(params);
                for (Map<String, Object> param : paramList) {
                    String result = Optional.ofNullable(param.get(v)).map(value-> value.toString()).orElse("无");

                    if (param.containsKey(numerical) && (boolean)param.get(numerical)){
                        if (result.contains(".")){
                            Double d = Double.valueOf(result);
                            if (d.intValue() == d){
                                result = d.intValue()+"";
                            }
                        }
                        i++;
                        specName.put(i, param.get(k).toString());
                        specValue.put(i, result + param.get(unit).toString());
                    }else if (param.containsKey(options)){
                        i++;
                        specName.put(i, param.get(k).toString());
                        specValue.put(i, param.get(options));
                    }else {
                        i++;
                        specName.put(i, param.get(k).toString());
                        specValue.put(i, param.get(v));
                    }
                }
            }
        }

    }
}
