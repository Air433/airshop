package com.airshop.item.mapper;

import com.airshop.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/7/9 - 11:54
 */
@org.apache.ibatis.annotations.Mapper
public interface StockMapper extends Mapper<Stock>, SelectByIdListMapper<Stock, Long> {
}
