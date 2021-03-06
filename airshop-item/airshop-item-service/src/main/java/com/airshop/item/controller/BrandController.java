package com.airshop.item.controller;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.pojo.Brand;
import com.airshop.item.service.BrandService;
import com.airshop.parameter.pojo.BrandQueryByPageParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        return Optional.ofNullable(result).map(x->ResponseEntity.ok(x))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.saveBrand(brand, cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 更新品牌
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> categories){
        this.brandService.updateBrand(brand, categories);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") String bid){
        String separator = "-";
        if (bid.contains(separator)){
            String[] ids = bid.split(separator);
            for (String id : ids) {
                this.brandService.deleteBrand(Long.parseLong(id));
            }
        }else {
            this.brandService.deleteBrand(Long.parseLong(bid));
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCategoryId(@PathVariable("cid") Long cid){
        List<Brand> list = this.brandService.queryBrandByCategoryId(cid);

        return Optional.ofNullable(list).map(x-> ResponseEntity.ok(x))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brand> brands = this.brandService.queryBrandByIds(ids);
        return ResponseEntity.ok(brands);
    }
}
