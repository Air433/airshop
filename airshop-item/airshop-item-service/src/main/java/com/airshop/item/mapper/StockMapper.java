package com.airshop.item.mapper;

import com.airshop.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/7/9 - 11:54
 */
@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends Mapper<Stock>, SelectByIdListMapper<Stock, Long> {

    @Update("update tb_stock set stock = stock - #{num} where sku_id = #{skuId} and stock >=#{num}")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
