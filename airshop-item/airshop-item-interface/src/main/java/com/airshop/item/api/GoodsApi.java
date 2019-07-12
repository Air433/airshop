package com.airshop.item.api;

import com.airshop.common.pojo.PageResult;
import com.airshop.item.bo.SpuBO;
import com.airshop.item.pojo.Sku;
import com.airshop.item.pojo.SpuDetail;
import com.airshop.parameter.pojo.SpuQueryByPageParameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author ouyanggang
 * @Date 2019/7/12 - 10:37
 */
@RequestMapping("goods")
public interface GoodsApi {

    /**
     * 分页查询
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuBO> querySpuByPage(
            SpuQueryByPageParameter parameter);
    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("id") Long id);

    /**
     * 根据Spu的id查询其下所有的sku
     * @param id
     * @return
     */
    @GetMapping("sku/list/{id}")
    List<Sku> querySkuBySpuId(@PathVariable("id") Long id);
}
