package com.airshop.service.impl;

import com.airshop.bo.SearchRO;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.client.BrandClient;
import com.airshop.item.client.CategoryClient;
import com.airshop.item.client.GoodsClient;
import com.airshop.item.client.SpecClient;
import com.airshop.item.pojo.*;
import com.airshop.pojo.Goods;
import com.airshop.repository.GoodsRepository;
import com.airshop.service.SearchService;
import com.airshop.utils.NumberUtils;
import com.airshop.vo.SearchResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.stats.InternalStats;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 16:24
 */
@Service
public class SearchServiceImpl implements SearchService {

    public static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).getBody();

        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId());

        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());

        if (spuDetail == null){
            log.error("spu id[{}]，SpuDetail不存在", spu.getId());
            return null;
        }

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

    @Override
    public SearchResult search(SearchRO searchRO) {
        String key = searchRO.getKey();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        QueryBuilder basicQuery = buildBasicQueryWithFilter(searchRO);

        queryBuilder.withQuery(basicQuery);

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //分页、排序
        searchWithPageAndSort(queryBuilder, searchRO);

        //商品分类聚合
        String categoryAggName = "category";
        //品牌聚合
        String brandAggName = "brand";

        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        AggregatedPage<Goods> pageInfo = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());

        List<Category> categories = getCategoryAggResult(pageInfo.getAggregation(categoryAggName));

        List<Brand> brandList = getBrandAggResult(pageInfo.getAggregation(brandAggName));

        List<Map<String, Object>> specs = null;
        if (categories.size() == 1){
            specs = getSpec(categories.get(0).getId(), basicQuery);
        }

        return new SearchResult(pageInfo.getTotalElements(), (long)pageInfo.getTotalPages(), pageInfo.getContent(), categories, brandList, specs);
    }

    @Override
    public void createIndex(Long spuId) throws IOException {
        SpuBO spuBO = this.goodsClient.querySpuById(spuId);
        Goods goods = this.buildGoods(spuBO);

        this.goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long spuId) {
        this.goodsRepository.deleteById(spuId);
    }

    private List<Map<String, Object>> getSpec(Long id, QueryBuilder basicQuery) {
        String specsJSON = specClient.querySpecificationByCategoryId(id).getBody();
        List<Map<String, Object>> specs = JSONArray.parseObject(specsJSON, List.class);
        //2.过滤出可以搜索的规格参数名称，分成数值类型、字符串类型
        Set<String> strSpecSet = new HashSet<>();
        //准备map，用来保存数值规格参数名及单位
        Map<String, String> numericalUnits = new HashMap<>();
        String searchable = "searchable";
        String numerical = "numerical";
        String k = "k";
        String unit = "unit";

        for (Map<String, Object> spec : specs) {
            List<Map<String, Object>> params = (List<Map<String, Object>>) spec.get("params");
            params.forEach(param->{
                if ((boolean)param.get(searchable)){
                    if (param.containsKey(numerical) && (boolean)param.get(numerical)){
                        numericalUnits.put(param.get(k).toString(), param.get(unit).toString());
                    }else {
                        strSpecSet.add(param.get(k).toString());
                    }
                }
            });
        }
        //3.聚合计算数值类型的interval
        Map<String, Double> numericalInterval = getNumberInterval(id, numericalUnits.keySet());
        return this.aggForSpec(strSpecSet, numericalInterval, numericalUnits, basicQuery);
    }

    /**
     * 根据规格参数，聚合得到过滤属性值
     * @param strSpecSet
     * @param numericalInterval
     * @param numericalUnits
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> aggForSpec(Set<String> strSpecSet, Map<String, Double> numericalInterval, Map<String, String> numericalUnits, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //准备查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(basicQuery);
        //聚合数值类型
        for (Map.Entry<String, Double> entry : numericalInterval.entrySet()) {
            queryBuilder.addAggregation(AggregationBuilders.histogram(entry.getKey()).field("specs." + entry.getKey()).interval(entry.getValue())
            .minDocCount(1));
        }
        //聚合字符串
        for (String key : strSpecSet) {
            queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs."+key+".keyword"));
        }

        Map<String, Aggregation> aggregationMap = this.elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();

        for (Map.Entry<String, Double> entry : numericalInterval.entrySet()) {
            Map<String, Object> spec = new HashMap<>();
            String key = entry.getKey();
            spec.put("k", key);
            spec.put("unit", numericalUnits.get(key));
            //获取聚合结果
            InternalHistogram histogram = (InternalHistogram) aggregationMap.get(key);
            spec.put("options", histogram.getBuckets().stream().map(bucket -> {
                Double begin = (Double) bucket.getKey();
                Double end = begin + numericalInterval.get(key);
                if (!NumberUtils.isInt(begin)){
                    begin = NumberUtils.scale(begin, 2);
                }
                if (!NumberUtils.isInt(end)){
                    end = NumberUtils.scale(end, 2);
                }
                return begin + "-" + end;
            }).collect(Collectors.toList()));
            specs.add(spec);
        }

        //解释字符串类型
        strSpecSet.forEach(key->{
            Map<String, Object> spec = new HashMap<>();
            spec.put("k", key);
            StringTerms terms = (StringTerms) aggregationMap.get(key);
            spec.put("options", terms.getBuckets().stream().map((Function<StringTerms.Bucket, Object>)StringTerms.Bucket::getKeyAsString)
                    .collect(Collectors.toList()));
        });

        return specs;
    }

    /**
     * 通过聚合计算间隔
     * @param id
     * @param keySet
     * @return
     */
    private Map<String, Double> getNumberInterval(Long id, Set<String> keySet) {
        Map<String, Double> numbericalSpecs = new HashMap<>();
        //准备查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //不需要查询数据
        queryBuilder.withQuery(QueryBuilders.termQuery("cid3", id.toString()))
                .withSourceFilter(new FetchSourceFilter(new String[]{""}, null))
                .withPageable(PageRequest.of(0, 1));
        //添加status类型的聚合，同时返回avg、max、min、sum、count等
        for (String key : keySet) {
            queryBuilder.addAggregation(AggregationBuilders.stats(key).field("specs."+key));
        }
        Map<String, Aggregation> aggregationMap = this.elasticsearchTemplate.query(queryBuilder.build(),
                searchResponse -> searchResponse.getAggregations().asMap());

        for (String key : keySet) {
            InternalStats stats = (InternalStats) aggregationMap.get(key);
            double interval = this.getInterval(stats.getMin(), stats.getMax(), stats.getSum());
            numbericalSpecs.put(key, interval);
        }
        return numbericalSpecs;
    }

    /**
     * 根据最大值，最小值，sum，计算间隔interval
     * @param min
     * @param max
     * @param sum
     * @return
     */
    private double getInterval(double min, double max, Double sum) {
        //显示7个区间
        double interval = (max - min) / 6.0d;
        //判断是否是小数
        if (sum.intValue() == sum){
            //不是小数，要取整十、整百
            int length = StringUtils.substringBefore(String.valueOf(interval), ".").length();
            double factor = Math.pow(10.0, length - 1);
            return Math.round(interval / factor)* factor;
        }else {
            return NumberUtils.scale(interval, 1);
        }

    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRO searchRO) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(searchRO.getKey())){
            queryBuilder.must(QueryBuilders.matchQuery("all", searchRO.getKey()).operator(Operator.AND));
        }
        //过滤条件构造器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        Map<String, String> filter = searchRO.getFilter();

        String searchKey = "key";
        String price = "price";

        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String regex = "^(\\d+\\.?\\d*)-(\\d+\\.?\\d*)$";
            if (!searchKey.equals(key)){
                if (price.equals(key)){
                    if (!value.contains("元以上")){
                        String[] nums = StringUtils.substringBefore(value, "元").split("-");
                        filterQueryBuilder.must(QueryBuilders.rangeQuery(key).gte(Double.valueOf(nums[0]) * 100).lt(Double.valueOf(nums[1]) * 100));
                    }else {
                        String num = StringUtils.substringBefore(value, "元以上");
                        filterQueryBuilder.must(QueryBuilders.rangeQuery(key).gte(Double.valueOf(num)*100));
                    }
                }else {
                    if (value.matches(regex)){
                        Double[] nums = NumberUtils.searchNumber(value, regex);
                        //数值类型范围查询 lt:小于 gte:大于等于
                        filterQueryBuilder.must(QueryBuilders.rangeQuery("specs."+ key).gte(nums[0]).lt(nums[1]));
                    }else {
                        if (!key.equals("cid3") && !key.equals("brandId")){
                            key = "specs." + key + ".keyword";
                        }

                        filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    }
                }
            }
        }

        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms brandAgg = (LongTerms) aggregation;
        List<Long> brandIds = new ArrayList<>();
        for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
            brandIds.add(bucket.getKeyAsNumber().longValue());
        }
        return this.brandClient.queryBrandByIds(brandIds);
    }

    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        LongTerms categoryAgg = (LongTerms) aggregation;
        List<Long> cids = new ArrayList<>();
        for (LongTerms.Bucket bucket : categoryAgg.getBuckets()) {
            cids.add(bucket.getKeyAsNumber().longValue());
        }
        return this.categoryClient.queryCategoryByIds(cids).getBody();
    }

    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder, SearchRO searchRO) {
        Integer page = searchRO.getPage();
        Integer size = searchRO.getDefaultSize();

        queryBuilder.withPageable(PageRequest.of(page-1, size));
        String sortBy = searchRO.getSortBy();
        Boolean desc = searchRO.getDescending();
        if (StringUtils.isNotBlank(sortBy)){
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
    }
}
