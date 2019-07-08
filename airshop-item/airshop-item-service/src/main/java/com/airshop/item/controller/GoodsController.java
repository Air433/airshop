package com.airshop.item.controller;

import com.airshop.common.pojo.PageResult;
import com.airshop.controller.BaseController;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.service.GoodsService;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 12:45
 */
@RestController
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBO>> querySpuByPage(
            SpuQueryByPageParameter parameter){

        PageResult<SpuBO> result = this.goodsService.querySpuByPageAndSort(parameter);

        log.warn("查询数据量：{}", result.getTotal());
        return ResponseEntity.ok(result);
    }
}
