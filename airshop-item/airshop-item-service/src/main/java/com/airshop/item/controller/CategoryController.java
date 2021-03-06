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

        return Optional.ofNullable(list).filter(x->!CollectionUtils.isEmpty(x))
                .map(x->ResponseEntity.ok(x))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("bid/{brandId}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("brandId") Long brandId){
        List<Category> list = this.categoryService.queryByBrandId(brandId);

        return Optional.ofNullable(list).filter(x-> !CollectionUtils.isEmpty(x)).map(x-> ResponseEntity.ok(x))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据商品分类id查询名称
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam List<Long> ids){
        List<String> list = this.categoryService.queryNameByIds(ids);
        return ResponseEntity.ok(list);
    }

    @GetMapping("all/level/{cid3}")
    public ResponseEntity<List<Category>> queryAllCategoryLevelByCid3(@PathVariable("cid3") Long id){
        List<Category> list = categoryService.queryAllCategoryLevelByCid(id);
        return ResponseEntity.ok(list);
    }

    @GetMapping("all")
    public ResponseEntity<List<Category>> queryAllCategoryByIds(@RequestParam("ids")List<Long> ids){
        List<Category> list = categoryService.queryCategoryByIds(ids);
        return ResponseEntity.ok(list);
    }
}
