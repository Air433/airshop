package com.airshop.controller;

import com.airshop.bo.SearchRO;
import com.airshop.common.pojo.PageResult;
import com.airshop.pojo.Goods;
import com.airshop.service.SearchService;
import com.airshop.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 10:08
 */
@RestController
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> search(RequestEntity<SearchRO> requestEntity){
        SearchResult result = this.searchService.search(requestEntity.getBody());
        return ResponseEntity.ok(result);
    }

}
