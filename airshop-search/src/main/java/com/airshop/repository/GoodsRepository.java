package com.airshop.repository;

import com.airshop.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 12:52
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
