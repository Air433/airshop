package com.airshop.item.mapper;

import com.airshop.item.pojo.Specification;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 11:07
 */
@org.apache.ibatis.annotations.Mapper
public interface SpecificationMapper extends Mapper<Specification>, SelectByIdListMapper<Specification, Long> {
}
