package com.airshop.item.controller;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.pojo.Brand;
import com.airshop.item.service.BrandService;
import com.airshop.parameter.pojo.BrandQueryByPageParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:28
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(BrandQueryByPageParameter brandQueryByPageParameter){

        PageResult<Brand> result = this.brandService.queryBrandByPage(brandQueryByPageParameter);

        if (Objects.isNull(result)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }
}
