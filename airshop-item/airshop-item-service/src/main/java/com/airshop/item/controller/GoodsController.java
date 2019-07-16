package com.airshop.item.controller;

import com.airshop.common.pojo.PageResult;
import com.airshop.controller.BaseController;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.Spu;
import com.airshop.item.pojo.SpuDetail;
import com.airshop.item.service.GoodsService;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
@RestController
@RequestMapping("goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable",defaultValue = "true") Boolean saleable){

        SpuQueryByPageParameter spuQueryByPageParameter = new SpuQueryByPageParameter(page,rows,sortBy,desc,key,saleable);
        PageResult<SpuBO> result = this.goodsService.querySpuByPageAndSort(spuQueryByPageParameter);

        log.warn("查询数据量：{}", result.getTotal());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBO spuBO){
        this.goodsService.saveGoods(spuBO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id")Long id){
        SpuDetail spuDetail = this.goodsService.querySpuDetailById(id);

        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("sku/list/{id}")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@PathVariable("id")Long id){
        List<Sku> skus = this.goodsService.querySkuBySpuId(id);

        if (skus == null || skus.size()==0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }

    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuBO> queryGoodsById(@PathVariable("id")Long id){
        SpuBO spuBO = this.goodsService.queryGoodsById(id);

        return Optional.ofNullable(spuBO).map(x-> ResponseEntity.ok(x))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBO spuBO){
        this.goodsService.updateGoods(spuBO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
