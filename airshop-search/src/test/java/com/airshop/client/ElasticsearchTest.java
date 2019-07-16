package com.airshop.client;

import com.airshop.AirSearchService;
import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import com.airshop.pojo.Goods;
import com.airshop.repository.GoodsRepository;
import com.airshop.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 14:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirSearchService.class)
public class ElasticsearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SearchService searchService;

    @Test
    public void createIndex(){
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData(){
        List<SpuBO> list = new ArrayList<>();
        int page = 1;
        int row = 100;
        int size;
        do{
            PageResult<SpuBO> result = this.goodsClient.querySpuByPage(page, row, null, true, null, true);
            List<SpuBO> spus = result.getItems();
            size = spus.size();
            page ++;
            list.addAll(spus);
        }while (size==100);

        List<Goods> goodsList = new ArrayList<>();
        for (SpuBO spuBO : list) {
            try {
                System.out.println("spu id" + spuBO.getId());
                Goods goods = this.searchService.buildGoods(spuBO);
                Optional.ofNullable(goods).ifPresent(good-> goodsList.add(good));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.goodsRepository.saveAll(goodsList);
    }
}
