package com.airshop.client;


import com.airshop.AirSearchService;
import com.airshop.item.pojo.Brand;
import com.airshop.item.pojo.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 11:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirSearchService.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;

    @Test
    public void testQueryCategories(){
        List<Long> longList = Arrays.asList(1L, 2L, 3L);
        List<String> names = this.categoryClient.queryNameByIds(longList).getBody();
        names.forEach(System.err::println);
        List<Long> longs = Arrays.asList(1115L, 2L, 3L);
        List<Brand> brands = this.brandClient.queryBrandByIds(longs);
        brands.forEach(System.err::println);
    }
}