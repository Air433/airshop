package com.airshop.item.mapper;

import com.airshop.item.pojo.Categroy;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:27
 */
@org.apache.ibatis.annotations.Mapper
public interface CategoryMapper extends Mapper<Categroy>, SelectByIdListMapper<CategoryMapper, Long> {

}
