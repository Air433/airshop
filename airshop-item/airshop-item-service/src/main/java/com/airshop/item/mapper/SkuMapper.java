package com.airshop.item.mapper;

import com.airshop.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/7/9 - 11:49
 */
@org.apache.ibatis.annotations.Mapper
public interface SkuMapper extends Mapper<Sku>, SelectByIdListMapper<Sku, Long> {
}
