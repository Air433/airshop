package com.airshop.item.controller;

import com.airshop.item.pojo.Category;
import com.airshop.item.service.CategoryService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:22
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryByParentId(
            @RequestParam(value = "pid", defaultValue = "0") Long pid){
        List<Category> list = categoryService.queryCategoryByPid(pid);

        if (CollectionUtils.isEmpty(list)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("bid/{brandId}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("brandId") Long brandId){
        List<Category> list = this.categoryService.queryByBrandId(brandId);

        return Optional.ofNullable(list).filter(x-> !CollectionUtils.isEmpty(x)).map(x-> ResponseEntity.ok(x))
                .orElseGet(()-> (ResponseEntity<List<Category>>) ResponseEntity.status(HttpStatus.NOT_FOUND));
    }
}
